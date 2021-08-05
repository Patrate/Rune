package fr.emmuliette.rune.mod.spells.component.effectComponent;

import fr.emmuliette.rune.mod.spells.SpellContext;

public interface ChannelEffect {
	/**
	 * Is the effect with a timer or canalised ?
	 * @return true if the effect is a timer
	 */
//	public boolean isTimed();
	
	/**
	 * Start the effect for duration seconds
	 * @param duration
	 * @return true if the effect started successfully
	 */
//	public boolean startFor(int duration);
	
	/**
	 * Start the effect without duration
	 * @return true if the effect started successfully
	 */
	public default boolean registerChannelEffect(SpellContext context) {
		System.out.println("Registering channel effect !");
		if(context.isChanneling()) {
			System.out.println("Registering ok");
			context.addChanneledEffect(this);
			return true;
		}
		System.out.println("But it's not channeling ?");
		return false;
	}
	
	/**
	 * Is the effect currently started, either canalised or with a timer ?
	 * @return true if the effect is started
	 */
	public boolean isStarted();
	
	/**
	 * Stop the effect, wether from a timer or stopping canalisation
	 * @return true if the effect was running and is now succesfully stopped
	 */
	public boolean stop();
}
