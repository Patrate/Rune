package fr.emmuliette.rune.mod.blocks.spelliverse;

import fr.emmuliette.rune.mod.gui.spelliverse.SpelliverseContainer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class SpelliverseBlock extends Block {
	private static final ITextComponent CONTAINER_TITLE = new TranslationTextComponent("container.spelliverse");

	public SpelliverseBlock(AbstractBlock.Properties prop) {
		super(prop);
	}

	public ActionResultType use(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (world.isClientSide) {
			return ActionResultType.SUCCESS;
		} else {
			player.openMenu(blockState.getMenuProvider(world, blockPos));
			return ActionResultType.CONSUME;
		}
	}

	public INamedContainerProvider getMenuProvider(BlockState blockState, World world, BlockPos blockPos) {
		return new SimpleNamedContainerProvider((containerId, playerInventory, postCall) -> {
			return new SpelliverseContainer(containerId, playerInventory, IWorldPosCallable.create(world, blockPos));
		}, CONTAINER_TITLE);
	}
}