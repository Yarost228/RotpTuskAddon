package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.util.TuskUtil;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.init.InitStands;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.brain.task.LookAtEntityTask;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
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

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class WormholeArmEntity extends Entity {
    protected static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(WormholeArmEntity.class, DataSerializers.OPTIONAL_UUID);

    public WormholeArmEntity(EntityType<? extends WormholeArmEntity> type, World world) {
        super(type, world);
    }
    public WormholeArmEntity(LivingEntity owner, World world) {
        super(InitEntities.WORMHOLE_WITH_ARM.get(), world);
        this.setOwnerUUID(owner.getUUID());
    }
    private int nailsShot = 0;
    @Override
    public void tick() {
        super.tick();
        if (getOwner() == null || tickCount >= 2000 || !getOwner().isAlive()){
            this.remove();
            return;
        }
        World world = this.level;
        this.move(MoverType.SELF, this.getDeltaMovement().add(0.0D, -0.7D, 0.0D));
        this.setNoGravity(false);
        RayTraceResult result = JojoModUtil.rayTrace(getOwner(), 100, entity -> !(entity instanceof ProjectileEntity));
        this.lookAt(EntityAnchorArgument.Type.EYES, result.getLocation());

        if (!world.isClientSide()){
            getOwner().getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                if (nailCapability.getNailCount() == 0){
                    this.remove();
                }
                else if (nailCapability.getNailCount() - nailsShot == 0 && nailsShot > 0){
                    this.remove();
                }
                IStandPower.getStandPowerOptional(getOwner()).ifPresent(stand -> {
                    Action heldAction = stand.getHeldAction();
                    LivingEntity user = getOwner();
                    int ticksHeld = stand.getHeldActionTicks();

                    if (heldAction == InitStands.NAIL_SHOT.get() && ticksHeld % 5 == 0){
                        NailEntity nail = new NailEntity(user, world, 0);
                        float velocity = 2.0F + (float)this.getDeltaMovement().length();
                        nail.shootFromRotation(this, velocity, 1.0F);
                        nail.moveTo(this.position().add(0, 0.5F, 0));
                        world.addFreshEntity(nail);
                        this.nailsShot ++;
                    }
                    if (heldAction == InitStands.MOVE_WORMHOLE_WITH_ARM.get()){
                        RayTraceResult result2 = TuskUtil.rayTrace(user, 12, entity -> false, 1);
                        Vector3d movement = this.position().vectorTo(result2.getLocation());
                        BlockPos pos = new BlockPos(result2.getLocation());
                        boolean isInAir = world.getBlockState(pos).isAir();
                        this.setDeltaMovement(movement.multiply(0.25, 1, 0.25).add(0, isInAir ? 0 : 0.7F, 0));
                    }
                    else {
                        this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
                    }

                    if (nailCapability.chargedShot()){
                        NailEntity nail = new NailEntity(user, world, ticksHeld/5);
                        float velocity = 2.0F + (float)this.getDeltaMovement().length() + ticksHeld/5F;
                        nail.shootFromRotation(this, velocity, 1.0F);
                        nail.moveTo(this.position().add(0, 0.5F, 0));
                        world.addFreshEntity(nail);
                        this.nailsShot ++;
                        nailCapability.setChargedShot(false);
                    }
                });
            });
        }
    }

    @Override
    public void remove(){
        if (getOwner() != null){
            getOwner().getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                nailCapability.setWormhole(false);
            });
        }
        super.remove();
    }

    @Nullable
    private UUID getOwnerUUID() {
        return entityData.get(OWNER_UUID).orElse(null);
    }

    private void setOwnerUUID(@Nullable UUID uuid) {
        entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(OWNER_UUID, Optional.empty());
    }
    @Override
    protected void readAdditionalSaveData (CompoundNBT nbt){
        UUID ownerId = nbt.hasUUID("Owner") ? nbt.getUUID("Owner") : null;
        if (ownerId != null) {
            setOwnerUUID(ownerId);
        }
    }
    @Override
    protected void addAdditionalSaveData (CompoundNBT nbt){
        if (getOwnerUUID() != null) {
            nbt.putUUID("Owner", getOwnerUUID());
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerUUID();
            if (uuid == null) return null;
            PlayerEntity owner = level.getPlayerByUUID(uuid);
            return owner;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
