package fr.emmuliette.rune.setup.modelBake;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import fr.emmuliette.rune.RuneMain;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraftforge.client.model.data.IModelData;

/**
 * Created by TheGreyGhost on 19/04/2015. This class modifies the displayed item
 * (a chessboard) to show a number of "pieces" (blue squares) on the chessboard,
 * one square for each item in the itemstack.
 */
public class RuneModel implements IBakedModel {

	/**
	 * Create our model, using the given baked model as a base to add extra
	 * BakedQuads to.
	 * 
	 * @param i_baseRuneModel the base model
	 */
	public RuneModel(IBakedModel i_baseRuneModel) {
		baseRuneModel = i_baseRuneModel;
		runeItemOverrideList = new RuneItemOverrideList();
	}

	// create a tag (ModelResourceLocation) for our model.
	// "inventory" is used for item. If you don't specify it, you will end up with
	// "" by default,
	// which is used for blocks.
	public static final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(RuneMain.MOD_ID + ":" + "projectile_rune", "inventory");

	// called for item rendering
	@Override
	@SuppressWarnings("deprecation")
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
		return baseRuneModel.getQuads(state, side, rand);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return runeItemOverrideList;
	}

	// not needed for item, but hey
	@Override
	public boolean useAmbientOcclusion() {
		return baseRuneModel.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return baseRuneModel.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return baseRuneModel.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer() {
		return baseRuneModel.isCustomRenderer();
	}

	@Override
	@SuppressWarnings("deprecation")
	public TextureAtlasSprite getParticleIcon() {
		return baseRuneModel.getParticleIcon();
	}

	// This is a forge extension that is expected for blocks only.
	@Override
	@Nonnull
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand,
			@Nonnull IModelData extraData) {
		throw new AssertionError("RuneModel::getQuads(IModelData) should never be called");
	}

	// This is a forge extension that is expected for blocks only.
	@Override
	@Nonnull
	public IModelData getModelData(@Nonnull IBlockDisplayReader world, @Nonnull BlockPos pos, @Nonnull BlockState state,
			@Nonnull IModelData tileData) {
		throw new AssertionError("RuneModel::getModelData should never be called");
	}

	private IBakedModel baseRuneModel;
	private RuneItemOverrideList runeItemOverrideList;
}
