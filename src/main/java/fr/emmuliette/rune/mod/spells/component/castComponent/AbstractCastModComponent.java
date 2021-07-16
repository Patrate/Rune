package fr.emmuliette.rune.mod.spells.component.castComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.emmuliette.rune.RuneMain;
import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.AbstractSpellComponent;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetAir;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetBlock;
import fr.emmuliette.rune.mod.spells.component.castComponent.targets.TargetLivingEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RuneMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class AbstractCastModComponent extends AbstractCastComponent<AbstractCastEffectComponent> {
	private static final Map<Integer, List<Callback>> callBackList = new HashMap<Integer, List<Callback>>();
	private static final Set<Callback> listeningCB = new HashSet<Callback>();
	private static int currentTick = 0;
	private List<AbstractCastEffectComponent> children;

	public AbstractCastModComponent() throws RunePropertiesException {
		super();
		children = new ArrayList<AbstractCastEffectComponent>();
	}

	public abstract class Callback {
		private Set<Callback> setCB;
		private CastModContainerComponent container;
		private AbstractCastModComponent parent;
		private SpellContext context;
		private int triggerTick;
		private boolean listening;
		private boolean triggered;

		public SpellContext getContext() {
			return context;
		}

		public Callback(AbstractCastModComponent parent, SpellContext context) {
			this(parent, context, -1, false);
		}

		public Callback(AbstractCastModComponent parent, SpellContext context, int delay) {
			this(parent, context, delay, false);
		}

		public Callback(AbstractCastModComponent parent, SpellContext context, int delay, boolean listening) {
			this.parent = parent;
			this.context = context;
			this.triggerTick = (delay == -1) ? -1 : (currentTick + delay);
			this.listening = listening;
			this.triggered = false;
			this.container = null;
		}

		public boolean isTriggered() {
			return triggered;
		}

		public AbstractCastModComponent getParent() {
			return parent;
		}

		public CastModContainerComponent getContainer() {
			return container;
		}

		public Set<Callback> getSetCB() {
			return setCB;
		}

		public final boolean callBack() {
			triggered = true;
			boolean result = false;
			if (_callBack()) {
				if (container == null) {
					result = castChildren();
				} else {
					container.update(this, context, false);
				}
			}
			finalize(result);
			return result;
		}

		public void register() {
			if (triggerTick == -1) {
				callBack();
				return;
			}
			if (triggerTick <= currentTick) {
				// TODO throw badTickException
				System.out.println(
						"WARNING registering on bad tick ! trigger " + triggerTick + " vs current " + currentTick + " class " + this.getClass().getCanonicalName());
				return;
			}
			if (listening && listeningCB.contains(this)) {
				// TODO throw alreadyRegisteredException
				System.out.println("WARNING CallBack already registered !");
				return;
			}
			List<Callback> cbList;
			if ((cbList = callBackList.get(triggerTick)) == null) {
				cbList = new ArrayList<Callback>();
				callBackList.put(triggerTick, cbList);
			}
			cbList.add(this);
			if (listening)
				listeningCB.add(this);
		}

		public void unregister() {
			List<Callback> cbList;
			if ((cbList = callBackList.get(triggerTick)) != null) {
				cbList.remove(this);
			}
			if (listeningCB.contains(this)) {
				listeningCB.remove(this);
			}
		}

		public boolean castChildren() {
			boolean result = false;
			for (AbstractCastEffectComponent child : getChildrens()) {
				result |= child.internalCast(context);
			}
			return result;
		}

		public void cancel(boolean triggerUpdate) {
			if (triggerUpdate && container != null) {
				container.update(this, context, true);
			}
			unregister();
			finalize(false);
		}

		// BEFORE REGISTERING
		public abstract boolean begin();

		// WHEN TICK IS DONE
		public abstract boolean _callBack();

		// EVERY TICK
		public abstract boolean tick();

		// AFTER TICK IS DONE
		public abstract boolean finalize(boolean result);
	}

	@SubscribeEvent
	public static void worldTickEvent(TickEvent.WorldTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}
		currentTick++;
		for (Callback cb : listeningCB) {
			cb.tick();
		}
		if (!callBackList.containsKey(currentTick)) {
			return;
		}
		for (Callback cb : callBackList.get(currentTick)) {
			cb.callBack();
		}
		callBackList.remove(currentTick);
	}

	protected abstract Callback modInternalCast(SpellContext context);

	@Override
	protected final boolean internalCast(SpellContext context) {
		Callback cb = modInternalCast(context);

		if (cb != null) {
			cb.begin();
			cb.register();
			return true;
		}
		return false;
	}

	protected final Callback internalCastGetCallback(SpellContext context, CastModContainerComponent container,
			Set<Callback> setCB) {
		Callback cb = modInternalCast(context);
		if (cb != null) {
			cb.container = container;
			cb.setCB = setCB;
			setCB.add(cb);
			cb.begin();
			cb.register();
		}
		return cb;
	}

	@Override
	public boolean canCast(SpellContext context) {
		try {
			ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
					.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
			if (!checkCooldown(cap, context))
				return false;
			
			if (!checkManaCost(cap, context))
				return false;
			
			if (!checkChildrenCastType(context))
				return false;
			
			return true;
		} catch (CasterCapabilityException e) {
			e.printStackTrace();
		}
		return false;
	}

	protected boolean checkCooldown(ICaster cap, SpellContext context) {
		return (!cap.isCooldown());
	}

	protected boolean checkManaCost(ICaster cap, SpellContext context) {
		return (this.getManaCost() <= cap.getMana());
	}

	protected boolean checkChildrenCastType(SpellContext context) {
		// at least one valid target necessary
		for (AbstractCastEffectComponent child : getChildrens()) {
			// target entity
			if (context.getTargetType() == SpellContext.TargetType.ENTITY && child instanceof TargetLivingEntity) {
				return true;
			}
			// target block
			if (context.getTargetType() == SpellContext.TargetType.BLOCK && child instanceof TargetBlock) {
				return true;
			}
			// target air
			if (context.getTargetType() == SpellContext.TargetType.AIR && child instanceof TargetAir) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<AbstractCastEffectComponent> getChildrens() {
		return children;
	}

	@Override
	public int getMaxSize() {
		return 999;
	}

	@Override
	public int getSize() {
		return children.size();
	}

	@Override
	public boolean canAddChildren(AbstractSpellComponent children) {
		return (children instanceof AbstractCastEffectComponent);
	}

	public float applyManaMod(float in) {
		return in;
	}

	public int applyCDMod(int in) {
		return in;
	}
}
