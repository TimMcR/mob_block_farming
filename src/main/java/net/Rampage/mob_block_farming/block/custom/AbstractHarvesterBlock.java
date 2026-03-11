package net.Rampage.mob_block_farming.block.custom;

import net.Rampage.mob_block_farming.block.entity.custom.AbstractHarvesterBlockEntity;
import net.Rampage.mob_block_farming.block.entity.custom.PigBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHarvesterBlock<T extends AbstractHarvesterBlockEntity> extends BaseEntityBlock {
    private RegistryObject<BlockEntityType<T>> blockEntityTypeRegistryObject;

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AbstractHarvesterBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.SOUTH));
    }

    protected AbstractHarvesterBlock(Properties pProperties, RegistryObject<BlockEntityType<T>> blockEntityTypeRegistryObject) {
        this(pProperties);
        this.blockEntityTypeRegistryObject = blockEntityTypeRegistryObject;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState,
                            boolean pMovedByPiston) {
        if(pState.getBlock() != pNewState.getBlock()) {
            if (pLevel.getBlockEntity(pPos) instanceof AbstractHarvesterBlockEntity harvesterBlockEntity) {
                harvesterBlockEntity.drops();
                pLevel.updateNeighbourForOutputSignal(pPos, this);
            }

            super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
        }
    }

    @Override
    protected void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        if(pLevel.isClientSide) return;

        tryConnect(pLevel, pPos, pState);

        super.onPlace(pState, pLevel, pPos, pOldState, pMovedByPiston);
    }

    @Override
    protected void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        if(pLevel.isClientSide) return;

        tryConnect(pLevel, pPos, pState);
    }

    private void tryConnect(Level level, BlockPos pos, BlockState state) {
        if (!state.hasProperty(AbstractHarvesterBlock.FACING)) {
            return;
        }

        Direction facing = state.getValue(AbstractHarvesterBlock.FACING);

        BlockPos neighbor = pos.relative(facing);
        BlockEntity be = level.getBlockEntity(neighbor);

        if (be instanceof PigBlockEntity mobBlock &&
                level.getBlockEntity(pos) instanceof AbstractHarvesterBlockEntity harvesterBlockEntity) {
            harvesterBlockEntity.connectToMobBlock(mobBlock);
        }
    }

    @Nullable
    @Override
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(Level pLevel, BlockState pState, BlockEntityType<B> pBlockEntityType) {
        if(pLevel.isClientSide())
            return null;

        return createTickerHelper(pBlockEntityType, blockEntityTypeRegistryObject.get(),
                (level, blockPos, blockState, harvesterBlockEntity) -> harvesterBlockEntity.tick(level, blockPos, blockState));
    }
}
