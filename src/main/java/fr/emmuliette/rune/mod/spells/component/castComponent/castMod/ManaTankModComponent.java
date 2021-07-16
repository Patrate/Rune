package fr.emmuliette.rune.mod.spells.component.castComponent.castMod;

import com.google.common.base.Function;

import fr.emmuliette.rune.exception.CasterCapabilityException;
import fr.emmuliette.rune.exception.CasterCapabilityExceptionSupplier;
import fr.emmuliette.rune.exception.DuplicatePropertyException;
import fr.emmuliette.rune.exception.NotEnoughManaException;
import fr.emmuliette.rune.mod.RunePropertiesException;
import fr.emmuliette.rune.mod.caster.capability.CasterCapability;
import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import fr.emmuliette.rune.mod.spells.component.castComponent.AbstractCastModComponent;
import fr.emmuliette.rune.mod.spells.properties.Grade;
import fr.emmuliette.rune.mod.spells.properties.Property;
import fr.emmuliette.rune.mod.spells.properties.SpellProperties;
import fr.emmuliette.rune.mod.spells.properties.possibleValue.PossibleInt;

public class ManaTankModComponent extends AbstractCastModComponent {

	public ManaTankModComponent() throws RunePropertiesException {
		super();
	}
	
	@Override
	protected Callback modInternalCast(SpellContext context) {
		return new Callback(this, context) {

			@Override
			public boolean _callBack() {
				int currentMana = (int) Math.floor(((ManaTankModComponent)this.getParent()).getCurrentMana());
				int cost = (int) Math.ceil(getManaCost());
				if(currentMana >= cost) {
					setCurrentMana(currentMana - cost);
					return true;
				}
				try {
					ICaster cap = context.getCaster().getCapability(CasterCapability.CASTER_CAPABILITY)
							.orElseThrow(new CasterCapabilityExceptionSupplier(context.getCaster()));
					int m = (int)Math.min(cap.getMana(), cost - currentMana);
					cap.delMana((float) m);
					setCurrentMana(currentMana + m);
				} catch (CasterCapabilityException | NotEnoughManaException e) {
					System.err.println("CAN'T COLLECT MANA");
					e.printStackTrace();
				}
				return false;
			}
			
			@Override
			public boolean begin() { // SKIP
				return true;
			}

			@Override
			public boolean finalize(boolean result) { // SKIP
				return true;
			}

			@Override
			public boolean tick() { // SKIP
				System.err.println("Method tick of Class ManaTankModComponent.Callback should never be called");
				return false;
			}
		};
	}
	
	// PROPERTIES
	
	
	private static final String KEY_CURRENT_MANA = "current_mana";
	private static SpellProperties DEFAULT_PROPERTIES;
	{
		if (DEFAULT_PROPERTIES == null) {
			try {
				DEFAULT_PROPERTIES = new SpellProperties();
				DEFAULT_PROPERTIES.addNewProperty(Grade.SECRET, new Property<Integer>(KEY_CURRENT_MANA,
						new PossibleInt(), new Function<Integer, Float>() {
							@Override
							public Float apply(Integer val) {
								return 0f;
							}
						}));
			} catch (DuplicatePropertyException e) {
				e.printStackTrace();
			}
		}
	}
	
	private float getCurrentMana() {
		return this.getProperty(KEY_CURRENT_MANA, new Integer(0)).getValue();
	}
	
	private void setCurrentMana(int currentMana) {
		this.setProperty(KEY_CURRENT_MANA, currentMana);
	}

	@Override
	public SpellProperties getDefaultProperties() {
		return DEFAULT_PROPERTIES;
	}
	
	@Override
	protected boolean checkManaCost(ICaster cap, SpellContext context) {
		return true;//(this.getManaCost() <= cap.getMana());
	}
	
	@Override
	protected void payManaCost(ICaster cap, SpellContext context) throws NotEnoughManaException {
		//cap.delMana(this.getManaCost());
	}
	
	@Override
	public float applyManaMod(float in) {
		int currentMana = (int) Math.floor(getCurrentMana());
		return Math.max(0, in - currentMana);
	}
}
