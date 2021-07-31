package fr.emmuliette.rune.mod.spells.entities;

import java.util.function.Supplier;

import fr.emmuliette.rune.setup.Registration;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.CampfireTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

public class ModEntities {

	public static final RegistryObject<EntityType<AreaEffectCloudEntity>> AREA_EFFECT_CLOUD = register(
			"area_effect_cloud",
			() -> EntityType.Builder.<AreaEffectCloudEntity>of(AreaEffectCloudEntity::new, EntityClassification.MISC)
					.fireImmune().sized(6.0F, 0.5F).clientTrackingRange(10).updateInterval(Integer.MAX_VALUE)
					.build("area_effect_cloud"));

	public static final RegistryObject<EntityType<MagicEntity>> MAGIC_ENTITY = register("magic_entity",
			() -> EntityType.Builder.<MagicEntity>of(MagicEntity::new, EntityClassification.MISC).fireImmune()
					.sized(2F, 2F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("magic_entity"));

	
	//register("campfire", TileEntityType.Builder.of(CampfireTileEntity::new, Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE));
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String name,
			Supplier<EntityType<T>> supplier) {
		return Registration.ENTITIES.register(name, supplier);
	}

	public static void register() {
	}
}
