package fr.emmuliette.rune.mod.blocks.scripting;

import java.util.function.Consumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

public class SpellBindingRecipeBuilder {
	   private final SpellBindingRecipeSerializer<?> serializer;

	   public SpellBindingRecipeBuilder(SpellBindingRecipeSerializer<?> p_i50786_1_) {
	      this.serializer = p_i50786_1_;
	   }

	   public static SpellBindingRecipeBuilder build(SpellBindingRecipeSerializer<?> p_218656_0_) {
	      return new SpellBindingRecipeBuilder(p_218656_0_);
	   }

	   public void save(Consumer<IFinishedRecipe> p_200499_1_, final String p_200499_2_) {
	      p_200499_1_.accept(new IFinishedRecipe() {
	         public void serializeRecipeData(JsonObject p_218610_1_) {
	         }

	         public IRecipeSerializer<?> getType() {
	            return SpellBindingRecipeBuilder.this.serializer;
	         }

	         public ResourceLocation getId() {
	            return new ResourceLocation(p_200499_2_);
	         }

	         @Nullable
	         public JsonObject serializeAdvancement() {
	            return null;
	         }

	         public ResourceLocation getAdvancementId() {
	            return new ResourceLocation("");
	         }
	      });
	   }
	}