package fr.emmuliette.rune.mod.currency.capability;

public interface ICurrency {

	public int getAmount();

	public void setAmount(int amount);

	public void addOrSubtractAmount(int amount);
}