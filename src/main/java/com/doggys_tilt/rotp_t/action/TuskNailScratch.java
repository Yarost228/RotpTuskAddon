package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.NailCapability;
import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitParticles;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.non_stand.VampirismClawLacerate;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.github.standobyte.jojo.util.mc.damage.DamageUtil;
import com.github.standobyte.jojo.util.mc.damage.KnockbackCollisionImpact;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskNailScratch extends StandAction {
    public TuskNailScratch(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<NailCapability> cap = user.getCapability(NailCapabilityProvider.CAPABILITY).resolve();
        if (cap.isPresent()){
            if (cap.get().getNailCount() > 0){
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    protected ActionConditionResult checkHeldItems(LivingEntity user, IStandPower power) {
        if (!(MCUtil.isHandFree(user, Hand.MAIN_HAND) && MCUtil.isHandFree(user, Hand.OFF_HAND))) {
            return conditionMessage("hands");
        }
        return ActionConditionResult.POSITIVE;
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.swinging = false;
        user.swing(Hand.MAIN_HAND);
        if (!world.isClientSide() ) {
            punchPerform(world, user, power, target, null, 1, 1);
        }
        else {
            if (target.getEntity() != null){
                Entity targetedEntity = target.getEntity();
                Vector3d vectorToEntity = (user.getEyePosition(1).vectorTo(targetedEntity.getPosition(targetedEntity.getBbHeight()/2))).scale(0.5F);
                Vector3d particlePos = user.getPosition(1).add(vectorToEntity);
                RotpTuskAddon.LOGGER.info(particlePos.toString());
                world.addParticle(InitParticles.NAIL_SWIPE.get(), particlePos.x(), particlePos.y() + (user.getBbHeight()*1.25F), particlePos.z(),
                        0, 0.0D, 0);
            }
        }
    }

    public void punchPerform(World world, LivingEntity user, IStandPower power, ActionTarget target, SoundEvent sound, float volume, float pitch) {
        if (!world.isClientSide()) {
            Entity entity = target.getEntity();
            if (entity instanceof LivingEntity) {
                LivingEntity targetEntity = (LivingEntity) entity;
                PlayerEntity pEntity = (PlayerEntity) user;
                if (entity.hurt(EntityDamageSource.playerAttack(pEntity), DamageUtil.addArmorPiercing(VampirismClawLacerate.getDamage(world, user) - 3f, 4F, targetEntity))) {
                    world.playSound(null, targetEntity.getX(), targetEntity.getEyeY(), targetEntity.getZ(), sound, targetEntity.getSoundSource(), volume, pitch);
                    targetEntity.knockback(0.75F, user.getX() - targetEntity.getX(), user.getZ() - targetEntity.getZ());
                    KnockbackCollisionImpact.getHandler(targetEntity).ifPresent(cap -> cap
                            .onPunchSetKnockbackImpact(targetEntity.getDeltaMovement(), user)
                    );
                }
            }
        }
    }
}
