package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.init.InitStands;
import com.doggys_tilt.rotp_t.util.TuskUtil;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class WormholeTeleporterEntity extends AbstractWormholeEntity {
    protected static final DataParameter<Integer> CONNECTED_TELEPORTER_ID = EntityDataManager.defineId(WormholeTeleporterEntity.class, DataSerializers.INT);
    private WormholeTeleporterEntity connectedTeleporter;
    private int ticksUntilTeleport = 120;
    private boolean isExit;

    public WormholeTeleporterEntity(EntityType<? extends WormholeTeleporterEntity> type, World world) {
        super(type, world, 600);
    }
    public WormholeTeleporterEntity(World world, LivingEntity owner) {
        super(InitEntities.WORMHOLE.get(), world, 600);
        this.setOwnerUUID(owner.getUUID());

    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity user = getOwner();
        if (user != null){
            user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability ->
                    IStandPower.getStandPowerOptional(user).ifPresent(stand -> {
                    Action heldAction = stand.getHeldAction();
                    if (heldAction == InitStands.REMOVE_WORMHOLE.get()){
                        this.remove();
                    }
                    if (heldAction == InitStands.MOVE_WORMHOLE.get() && isExit()){
                        RayTraceResult result2 = TuskUtil.rayTrace(user, 12, entity -> false, 1);
                        Vector3d movement = this.position().vectorTo(result2.getLocation());
                        BlockPos pos = new BlockPos(result2.getLocation());
                        boolean isInAir = level.getBlockState(pos).isAir();
                        this.setDeltaMovement(movement.multiply(0.25, 1, 0.25).add(0, isInAir ? 0 : 0.7F, 0));
                    }
                })
            );
        }

        List<Entity> entities = this.level.getEntities(this, this.getBoundingBox());
        if (!entities.isEmpty()) {
            ticksUntilTeleport--;
        }
        else ticksUntilTeleport = 60;
        entities.forEach(entity -> {
            if (ticksUntilTeleport <= 0 && !entities.isEmpty() && entity != null && connectedTeleporter != null){
                entity.teleportToWithTicket(
                                connectedTeleporter.position().x,
                                connectedTeleporter.position().y,
                                connectedTeleporter.position().z);
                ticksUntilTeleport = 60;
            }
        });

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(CONNECTED_TELEPORTER_ID, -1);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
    }




    @Override
    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);

    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public WormholeTeleporterEntity getConnectedTeleporter() {
        return (WormholeTeleporterEntity) this.level.getEntity(entityData.get(CONNECTED_TELEPORTER_ID));
    }

    public void setConnectedTeleporter(WormholeTeleporterEntity connectedTeleporter) {
        this.connectedTeleporter = connectedTeleporter;
        entityData.set(CONNECTED_TELEPORTER_ID, connectedTeleporter.getId());
    }
    public boolean isExit() {
        return isExit;
    }
    public void setExit(boolean exit) {
        isExit = exit;
    }
}
