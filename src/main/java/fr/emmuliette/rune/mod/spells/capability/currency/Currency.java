package fr.emmuliette.rune.mod.spells.capability.currency;

public class Currency implements ICurrency {
    private int amount = 0;

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;

        if(this.amount < 0)
            this.amount = 0;
    }

    @Override
    public void addOrSubtractAmount(int amount) {
        this.amount += amount;

        if(this.amount < 0)
            this.amount = 0;
    }
}