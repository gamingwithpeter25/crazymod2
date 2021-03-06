package longlegs.mod.world.gen.generators;

import java.util.Random;

import longlegs.mod.init.BlockInit;
import longlegs.mod.objects.blocks.BlockLeaf;
import longlegs.mod.objects.blocks.BlockLogs;
import longlegs.mod.objects.blocks.BlockSaplings;
import longlegs.mod.util.handlers.EnumHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenCopperTree extends WorldGenAbstractTree 
{
	public static final IBlockState LOG = BlockInit.LOGS.getDefaultState().withProperty(BlockLogs.VARIANT,
			EnumHandler.EnumType.COPPER);
	public static final IBlockState LEAF = BlockInit.LEAVES.getDefaultState().withProperty(BlockLeaf.VARIANT,
			EnumHandler.EnumType.COPPER);
	private final boolean useExtraRandomHeight;
	

	public WorldGenCopperTree(boolean notify,boolean useExtraRandomHeightIn) {
		super(notify);
		this.useExtraRandomHeight = useExtraRandomHeightIn;
	}

	public boolean generate(World worldIn, Random rand, BlockPos position) {
		int i = rand.nextInt(3) + 5;

		if (this.useExtraRandomHeight) {
			i += rand.nextInt(7);
		}

		boolean flag = true;

		if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
			for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
				int k = 1;

				if (j == position.getY()) {
					k = 0;
				}

				if (j >= position.getY() + 1 + i - 2) {
					k = 2;
				}

				BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

				for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
					for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
						if (j >= 0 && j < worldIn.getHeight()) {
							if (!this.isReplaceable(worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) {
								flag = false;
							}
						} else {
							flag = false;
						}
					}
				}
			}

			if (!flag) {
				return false;
			} else {
				BlockPos down = position.down();
				IBlockState state = worldIn.getBlockState(down);
				boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, EnumFacing.UP, (BlockSaplings)BlockInit.SAPLINGS);

				if (isSoil && position.getY() < worldIn.getHeight() - i - 1) {
					state.getBlock().onPlantGrow(state, worldIn, down, position);

					for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2) {
						int k2 = i2 - (position.getY() + i);
						int l2 = 1 - k2 / 2;

						for (int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3) {
							int j1 = i3 - position.getX();

							for (int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1) {
								int l1 = k1 - position.getZ();

								if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0) {
									BlockPos blockpos = new BlockPos(i3, i2, k1);
									IBlockState state2 = worldIn.getBlockState(blockpos);

									if (state2.getBlock().isAir(state2, worldIn, blockpos)
											|| state2.getBlock().isAir(state2, worldIn, blockpos)) {
										this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
									}
								}
							}
						}
					}

					for (int j2 = 0; j2 < i; ++j2) {
						BlockPos upN = position.up(j2);
						IBlockState state2 = worldIn.getBlockState(upN);

						if (state2.getBlock().isAir(state2, worldIn, upN)
								|| state2.getBlock().isLeaves(state2, worldIn, upN)) {
							this.setBlockAndNotifyAdequately(worldIn, position.up(j2), LOG);
						}
					}

					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
}
