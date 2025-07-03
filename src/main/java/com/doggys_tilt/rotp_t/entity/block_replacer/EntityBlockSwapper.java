package com.doggys_tilt.rotp_t.entity.block_replacer;

import com.doggys_tilt.rotp_t.entity.WormholeArmEntity;
import com.doggys_tilt.rotp_t.init.InitEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by BobMowzie on 7/8/2018.
 * https://www.curseforge.com/minecraft/mc-mods/mowzies-mobs
 */
public class EntityBlockSwapper extends Entity {
    private static final DataParameter<Optional<BlockState>> ORIG_BLOCK_STATE = EntityDataManager.defineId(EntityBlockSwapper.class, DataSerializers.BLOCK_STATE);
    private static final DataParameter<Integer> RESTORE_TIME = EntityDataManager.defineId(EntityBlockSwapper.class, DataSerializers.INT);
    private static final DataParameter<BlockPos> POS = EntityDataManager.defineId(EntityBlockSwapper.class, DataSerializers.BLOCK_POS);
    private NonNullList<ItemStack> items = NonNullList.withSize(0, ItemStack.EMPTY);
    protected int duration;
    protected boolean breakParticlesEnd;
    private BlockPos pos;

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world) {
        super(type, world);
        breakParticlesEnd = false;
    }

    public EntityBlockSwapper(EntityType<? extends EntityBlockSwapper> type, World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        super(type, world);
        setStorePos(pos);
        setRestoreTime(duration);
        this.breakParticlesEnd = breakParticlesEnd;
        setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        if (!world.isClientSide) {
            setOrigBlock(world.getBlockState(pos));
            if (world.getBlockEntity(pos) instanceof LockableLootTileEntity){
                LockableLootTileEntity tileEntity = (LockableLootTileEntity) world.getBlockEntity(pos);
                items = NonNullList.withSize(tileEntity.getContainerSize(), ItemStack.EMPTY);
                for (int i = 0; i < tileEntity.getContainerSize(); i++) {
                    items.set(i, tileEntity.getItem(i));
                }
                tileEntity.clearContent();
            }
            if (breakParticlesStart) world.destroyBlock(pos, false);
            world.setBlock(pos, newBlock, 19);
        }
        List<EntityBlockSwapper> swappers = world.getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
        for (EntityBlockSwapper swapper : swappers) {
            if (swapper == this) continue;
            else {
                setOrigBlock(swapper.getOrigBlock());
                swapper.remove();
            }
        }
    }

    public static void swapBlock(World world, BlockPos pos, BlockState newBlock, int duration, boolean breakParticlesStart, boolean breakParticlesEnd) {
        if (!world.isClientSide) {
            EntityBlockSwapper swapper = new EntityBlockSwapper(InitEntities.BLOCK_SWAPPER.get(), world, pos, newBlock, duration, breakParticlesStart, breakParticlesEnd);
            world.addFreshEntity(swapper);
        }
    }

    public boolean isBlockPosInsideSwapper(BlockPos pos) {
        return pos.equals(getStorePos());
    }

    @Override
    public boolean shouldRender(double p_145770_1_, double p_145770_3_, double p_145770_5_) {
        return false;
    }

    @Override
    protected void defineSynchedData() {
        getEntityData().define(ORIG_BLOCK_STATE, Optional.of(Blocks.DIRT.defaultBlockState()));
        getEntityData().define(RESTORE_TIME, 20);
        getEntityData().define(POS, new BlockPos(0, 0, 0));
    }

    public int getRestoreTime() {
        return entityData.get(RESTORE_TIME);
    }

    public void setRestoreTime(int restoreTime) {
        entityData.set(RESTORE_TIME, restoreTime);
        duration = restoreTime;
    }

    public BlockPos getStorePos() {
        return entityData.get(POS);
    }

    public void setStorePos(BlockPos bpos) {
        entityData.set(POS, bpos);
        pos = bpos;
    }

    @Nullable
    public BlockState getOrigBlock() {
        Optional<BlockState> opState = getEntityData().get(ORIG_BLOCK_STATE);
        return opState.orElse(null);
    }

    public void setOrigBlock(BlockState block) {
        getEntityData().set(ORIG_BLOCK_STATE, Optional.of(block));
    }

    public void restoreBlock() {
        List<EntityBlockSwapper> swappers = level.getEntitiesOfClass(EntityBlockSwapper.class, getBoundingBox());
        if (!level.isClientSide) {
            boolean canReplace = true;
            for (EntityBlockSwapper swapper : swappers) {
                if (swapper == this) continue;
                if (swapper.isBlockPosInsideSwapper(pos)) {
                    canReplace = false;
                    break;
                }
            }
            if (canReplace) {
                level.getBlockState(pos);
                if (breakParticlesEnd) level.destroyBlock(pos, false);
                level.setBlock(pos, getOrigBlock(), 19);
                if (this.level.getBlockEntity(pos) instanceof LockableLootTileEntity){
                    LockableLootTileEntity tileEntity = (LockableLootTileEntity) this.level.getBlockEntity(pos);
                    for (ItemStack itemStack : items) {
                        tileEntity.setItem(items.indexOf(itemStack), itemStack);
                    }
                }
            }
            remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (canRestoreBlock()) restoreBlock();
    }

    protected boolean canRestoreBlock() {
        return tickCount > duration && level.getEntitiesOfClass(PlayerEntity.class, getBoundingBox()).isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        Optional<BlockState> blockState = getEntityData().get(ORIG_BLOCK_STATE);
        ItemStackHelper.saveAllItems(compound, this.items);
        compound.put("block", NBTUtil.writeBlockState(blockState.get()));
        compound.putInt("restoreTime", getRestoreTime());
        compound.putInt("storePosX", getStorePos().getX());
        compound.putInt("storePosY", getStorePos().getY());
        compound.putInt("storePosZ", getStorePos().getZ());
    }
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        INBT blockNBT = compound.get("block");
        if (blockNBT != null) {
            BlockState blockState = NBTUtil.readBlockState((CompoundNBT) blockNBT);
            setOrigBlock(blockState);
        }
        ItemStackHelper.loadAllItems(compound, this.items);
        setRestoreTime(compound.getInt("restoreTime"));
        setStorePos(new BlockPos(
                compound.getInt("storePosX"),
                compound.getInt("storePosY"),
                compound.getInt("storePosZ")
        ));
    }
}

