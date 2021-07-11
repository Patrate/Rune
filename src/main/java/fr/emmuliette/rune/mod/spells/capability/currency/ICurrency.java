package fr.emmuliette.rune.mod.spells.capability.currency;

public interface ICurrency {

	public int getAmount();

	public void setAmount(int amount);

	public void addOrSubtractAmount(int amount);
}