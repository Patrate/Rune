package fr.emmuliette.rune.mod.spells.cost;

import java.util.HashMap;
import java.util.Map;

import fr.emmuliette.rune.mod.caster.capability.ICaster;
import fr.emmuliette.rune.mod.spells.SpellContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCost extends Cost<Map<Item, Integer>> {
	private Map<Item, Integer> itemCost;

	public ItemCost(Item item, Integer nbr) {
		this(null, item, nbr);
	}

	public ItemCost(Map<Class<? extends Cost<?>>, Cost<?>> internalCost, Item item, Integer nbr) {
		super(internalCost);
		this.itemCost = new HashMap<Item, Integer>();
		this.itemCost.put(item, nbr);
	}

	@Override
	protected void internalAdd(Cost<?> other) {
		if (!(other instanceof ItemCost))
			return;
		ItemCost mOther = (ItemCost) other;
		for (Item otherItem : mOther.itemCost.keySet()) {
			if (itemCost.containsKey(otherItem)) {
				itemCost.put(otherItem, itemCost.get(otherItem) + mOther.itemCost.get(otherItem));
			} else {
				itemCost.put(otherItem, mOther.itemCost.get(otherItem));
			}
		}
	}

	@Override
	protected void internalRemove(Cost<?> other) {
		if (!(other instanceof ItemCost))
			return;
		ItemCost mOther = (ItemCost) other;
		for (Item otherItem : mOther.itemCost.keySet()) {
			if (itemCost.containsKey(otherItem)) {
				itemCost.put(otherItem, itemCost.get(otherItem) - mOther.itemCost.get(otherItem));
				if (itemCost.get(otherItem) <= 0) {
					itemCost.remove(otherItem);
				}
			}
		}
	}

	@Override
	public Map<Item, Integer> getCost() {
		return itemCost;
	}

	@Override
	protected boolean internalPayCost(ICaster cap, SpellContext context) {
		if(context.getCaster() instanceof PlayerEntity) {
			PlayerEntity pCaster = (PlayerEntity) context.getCaster();
			for(ItemStack is:pCaster.inventory.items) {
				if(itemCost.containsKey(is.getItem())) {
					if(is.isStackable()) {
						is.setCount(Math.max(0, is.getCount() - itemCost.get(is.getItem())));
					}
				}
			}
		} else {
			
		}
		System.out.println("TODO");
		//context.getCaster().
		return false;
	}

	@Override
	protected boolean internalCanPay(ICaster cap, SpellContext context) {
		// TODO Auto-generated method stub
		return false;
	}
}
