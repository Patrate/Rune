package fr.emmuliette.rune.mod;

public class NotABlockException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotABlockException(AbstractModObject e) {
		super(e.getName() + " is not a block !");
	}
}
