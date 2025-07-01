package com.doggys_tilt.rotp_t.entity;

import com.doggys_tilt.rotp_t.util.TuskUtil;
import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.doggys_tilt.rotp_t.init.InitStands;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class WormholeArmEntity extends AbstractWormholeEntity {
    public WormholeArmEntity(EntityType<? extends WormholeArmEntity> type, World world) {
        super(type, world, 0);
    }
    public WormholeArmEntity(LivingEntity owner, World world) {
        super(InitEntities.WORMHOLE_WITH_ARM.get(), world, 600);
        this.setOwnerUUID(owner.getUUID());
    }

    private int nailsShot = 0;
    @Override
    public void tick() {
        super.tick();
        World world = this.level;
        if (getOwner() != null){
            RayTraceResult result = JojoModUtil.rayTrace(getOwner(), 100, entity -> !(entity instanceof ProjectileEntity));
            this.lookAt(EntityAnchorArgument.Type.EYES, result.getLocation());
            if (!world.isClientSide()){
                IStandPower.getStandPowerOptional(getOwner()).ifPresent(stand ->
                    getOwner().getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                    Action heldAction = stand.getHeldAction();
                    if (nailCapability.getNailCount() == 0 || heldAction == InitStands.REMOVE_WORMHOLE_WITH_ARM.get()){
                        this.remove();
                    }
                    else if (nailCapability.getNailCount() - nailsShot == 0 && nailsShot > 0){
                        this.remove();
                    }
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
                    })
                );
            }
        }
    }
}
