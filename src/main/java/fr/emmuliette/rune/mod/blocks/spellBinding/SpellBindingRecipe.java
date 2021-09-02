package fr.emmuliette.rune.mod.blocks.spellBinding;

import fr.emmuliette.rune.mod.gui.spellbinding.SpellBindingInventory;
import fr.emmuliette.rune.setup.Registration;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public abstract class SpellBindingRecipe implements IRecipe<SpellBindingInventory> {	
	private final ResourceLocation id;
	
	public SpellBindingRecipe(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	@Override
	public IRecipeType<?> getType() {
		return Registration.SPELLBINDING_RECIPE;
	}
}
