package net.Rampage.mob_block_farming.block.custom;

import net.Rampage.mob_block_farming.block.entity.custom.AbstractMobBlockEntity;
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

public abstract class AbstractMobBlock<T extends AbstractMobBlockEntity> extends BaseEntityBlock {
    private RegistryObject<BlockEntityType<T>> blockEntityTypeRegistryObject;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public AbstractMobBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH));
    }

    protected AbstractMobBlock(Properties pProperties, RegistryObject<BlockEntityType<T>> blockEntityTypeRegistryObject) {
        this(pProperties);
        this.blockEntityTypeRegistryObject = blockEntityTypeRegistryObject;
    }

    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    public abstract BlockEntity newBlockEntity(BlockPos pPos, BlockState pState);

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState()
                .setValue(FACING, pContext.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public <B extends BlockEntity> BlockEntityTicker<B> getTicker(Level pLevel, BlockState pState,
                                                                  BlockEntityType<B> pBlockEntityType) {
        if(pLevel.isClientSide())
            return null;

        return createTickerHelper(pBlockEntityType, blockEntityTypeRegistryObject.get(),
                (level, blockPos, blockState,
                 mobBlockEntity) -> mobBlockEntity.tick(level, blockPos, blockState));
    }
}
