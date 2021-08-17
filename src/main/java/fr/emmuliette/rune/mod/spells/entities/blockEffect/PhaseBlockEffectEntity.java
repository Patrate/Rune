package fr.emmuliette.rune.mod.spells.entities.blockEffect;

import fr.emmuliette.rune.mod.spells.entities.ModEntities;
import fr.emmuliette.rune.mod.spells.properties.BlockGrid;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PhaseBlockEffectEntity extends BlockEffectEntity {
	public PhaseBlockEffectEntity(EntityType<? extends PhaseBlockEffectEntity> blockEffectEntity, World world) {
		super(blockEffectEntity, world, null, null, 0);
	}

	public PhaseBlockEffectEntity(World world, BlockPos center, BlockGrid blocks, int duration) {
		super(ModEntities.PHASE_BLOCK_EFFECT.get(), world, center, blocks, duration);
	}

	protected void onRemove() {
		for (BlockPos newBlock : blocks.getBlockPos(this.level, center)) {
			// TODO le mettre en version blockstate sauvegardé
			this.level.setBlockAndUpdate(newBlock, Blocks.STONE.defaultBlockState());
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT nbt) {
		super.readAdditionalSaveData(nbt);
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT nbt) {
		super.addAdditionalSaveData(nbt);
	}

}