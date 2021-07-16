package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent.Callback;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CastModContainerComponent extends AbstractCastComponent<AbstractCastComponent<?>> {
	private List<AbstractCastModComponent> childrenCastMod;
	private List<AbstractCastEffectComponent> childrenCast;

	public CastModContainerComponent() throws RunePropertiesException {
		super();
		childrenCastMod = new ArrayList<AbstractCastModComponent>();
		childrenCast = new ArrayList<AbstractCastEffectComponent>();
	}

	@Override
	public Boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			Boolean checkCd = checkCooldown(cap, context);
			if (checkCd == null || !checkCd)
				return checkCd;

			Boolean checkManaCost = checkManaCost(cap, context);
			if (checkManaCost == null || !checkManaCost)
				return checkCd;

			if (!checkManaCost(cap, context))
				return false;

			Boolean checkChildrens = checkChildrenCastType(context);
			if (checkChildrens == null || !checkChildrens)
				return checkChildrens;

			return true;
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public int getSize() {
		return childrenCastMod.size() + childrenCast.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return ((children instanceof AbstractCastModComponent) || (children instanceof AbstractCastEffectComponent));
	}

	@Override
	public List<AbstractCastComponent<?>> getChildrens() {
		List<AbstractCastComponent<?>> retour = new ArrayList<AbstractCastComponent<?>>();
		retour.addAll(childrenCast);
		retour.addAll(childrenCastMod);
		return retour;
	}

	@Override
	protected void addChildrenInternal(AbstractSpellComponent newEffect) {
		if  (newEffect instanceof AbstractCastEffectComponent) {
		childrenCast.add((AbstractCastEffectComponent) newEffect);
		} else if (newEffect instanceof AbstractCastModComponent){
			childrenCastMod.add((AbstractCastModComponent) newEffect);
		}
	}

	public List<AbstractCastEffectComponent> getCastEffect() {
		return childrenCast;
	}

	public List<AbstractCastModComponent> getCastMod() {
		return childrenCastMod;
	}

	@Override
	protected boolean internalCast(SpellContext context) {
		Set<Callback> setCB = new HashSet<Callback>();
		for (AbstractCastModComponent mod : childrenCastMod) {
			mod.internalCastGetCallback(context, this, setCB);
		}
		return true;
	}

	public boolean update(Callback cb, SpellContext context, boolean failed) {
		Set<Callback> setCB = cb.getSetCB();
		if (failed) {
			for (Callback callback : setCB) {
				callback.cancel(false);
			}
			return false;
		}
		// PAS ECHEC, CAST ?
		for (Callback callback : setCB) {
			if (!callback.isTriggered()) {
				return false;
			}
		}
		return castChildren(context);
	}

	private boolean castChildren(SpellContext context) {
		boolean retour = false;
		for (AbstractCastEffectComponent child : childrenCast) {
			retour |= child.internalCast(context);
		}
		return retour;
	}

	@Override
	public SpellProperties getDefaultProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getManaCost() {
		float retour = super.getManaCost();
		for (AbstractCastModComponent mod : childrenCastMod) {
			retour = mod.applyManaMod(retour);
		}
		return Math.max(0f, retour);
	}

	@Override
	public int getCooldown() {
		int retour = super.getCooldown();
		for (AbstractCastModComponent mod : childrenCastMod) {
			retour = mod.applyCDMod(retour);
		}
		return Math.max(0, retour);
	}
}
