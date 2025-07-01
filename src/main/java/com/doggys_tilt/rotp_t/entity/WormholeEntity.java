package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class WormholeEntity extends AbstractWormholeEntity {
    public WormholeEntity(EntityType<? extends WormholeEntity> type, World world) {
        super(type, world, 0);
    }
    private LivingEntity target;
    public WormholeEntity(LivingEntity owner, World world, int lifetime, LivingEntity target) {
        super(InitEntities.DAMAGING_WORMHOLE.get(), world, lifetime);
        this.setOwnerUUID(owner.getUUID());
        this.target = target;
    }

    @Override
    public void tick() {
        super.tick();
        if (getOwner() != null){
            getOwner().getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                nailCapability.addToNailWormholes(this);
            });
            Entity closestEntity = target;
            if (closestEntity != null){
                Vector3d movement = this.position().vectorTo(closestEntity.position()).multiply(1, 1, 1);
                this.move(MoverType.SELF, this.getDeltaMovement().add(movement.multiply(0.15, 1, 0.15)));
                if (closestEntity.distanceToSqr(this) < 0.2){
                    closestEntity.hurt(DamageSource.mobAttack(getOwner()), 1F);
                }
            }
        }
        else {
            remove();
        }
    }
    @Override
    public void remove(){
        this.remove(false);
    }
}
