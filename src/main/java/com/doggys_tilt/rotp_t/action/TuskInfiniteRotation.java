package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.RotpTuskAddon;
import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.init.InitEffects;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.action.stand.StandEntityHeavyAttack;
import com.github.standobyte.jojo.action.stand.punch.StandBlockPunch;
import com.github.standobyte.jojo.action.stand.punch.StandEntityPunch;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.entity.stand.StandPose;
import com.github.standobyte.jojo.entity.stand.StandStatFormulas;
import com.github.standobyte.jojo.init.ModParticles;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.general.MathUtil;
import com.github.standobyte.jojo.util.mc.damage.StandEntityDamageSource;
import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;

public class TuskInfiniteRotation extends StandEntityHeavyAttack {

    private boolean transferedInfiniteRotation = false;

    public static final StandPose INFINITE_ROTATION_ANIM = new StandPose("tusk_infinite_rotation");

    public TuskInfiniteRotation(StandEntityHeavyAttack.Builder builder) {
        super(builder);
        friendlyFire = true;
    }

    @Override
    public int getStandWindupTicks(IStandPower standPower, StandEntity standEntity) {
        return 0;
    }

    @Override
    public int getStandActionTicks(IStandPower standPower, StandEntity standEntity) {
        return Math.max(StandStatFormulas.getHeavyAttackWindup(standEntity.getAttackSpeed(), standEntity.getLastHeavyFinisherValue())*2, 2);
    }

    @Override
    public void phaseTransition(World world, StandEntity standEntity, IStandPower standPower,
                                StandEntityAction.Phase from, StandEntityAction.Phase to, StandEntityTask task, int ticks) {
        super.phaseTransition(world, standEntity, standPower, from, to, task, ticks);
        if (to == StandEntityAction.Phase.PERFORM) {
            if (standEntity.isFollowingUser() && standEntity.getAttackSpeed() < 24) {
                LivingEntity user = standEntity.getUser();
                if (user != null) {
                    Vector3d vec = new Vector3d(0, 0, 1).yRot(-user.yRot * MathUtil.DEG_TO_RAD);
                    standEntity.setPos(user.getX() + vec.x,
                            standEntity.getY(),
                            user.getZ() + vec.z);
                }
            }
            task.getAdditionalData().push(Vector3d.class, standEntity.getLookAngle().scale(10D / (double) ticks));
        }
    }



    @Override
    public void standTickPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        float completion = task.getPhaseCompletion(1.0F);
        boolean lastTick = task.getTicksLeft() <= 1;
        boolean moveForward = completion <= 0.5F;
        if (moveForward) {
            standEntity.setNoPhysics(true);
            for (RayTraceResult rayTraceResult : JojoModUtil.rayTraceMultipleEntities(standEntity,
                    standEntity.getAttributeValue(ForgeMod.REACH_DISTANCE.get()),
                    standEntity.canTarget(), 0.25, standEntity.getPrecision())) {
                if (ActionTarget.fromRayTraceResult(rayTraceResult).getEntity() instanceof LivingEntity){
                    LivingEntity livingTarget = (LivingEntity) ActionTarget.fromRayTraceResult(rayTraceResult).getEntity();
                    if (!transferedInfiniteRotation && livingTarget != null){
                        transferedInfiniteRotation = true;
                        livingTarget.level.addParticle(ModParticles.LIGHT_MODE_FLASH.get(), livingTarget.getX(), livingTarget.getEyeY(), livingTarget.getZ(), 0, 0, 0);
                        if (livingTarget.getEffect(InitEffects.INFINITE_ROTATION.get()) == null){
                            livingTarget.addEffect(new EffectInstance(InitEffects.INFINITE_ROTATION.get(), Integer.MAX_VALUE, 0, true, true, true));
                        }
                        else {
                            livingTarget.removeEffect(InitEffects.INFINITE_ROTATION.get());
                        }
                    }

                }
            }
        } else if (!Vector3d.ZERO.equals(standEntity.getDeltaMovement())) {
            if (task.getTarget().getEntity() instanceof LivingEntity){
                LivingEntity livingTarget = (LivingEntity) task.getTarget().getEntity();
                if (!transferedInfiniteRotation && livingTarget != null){
                    transferedInfiniteRotation = true;
                    livingTarget.level.addParticle(ModParticles.LIGHT_MODE_FLASH.get(), livingTarget.getX(), livingTarget.getEyeY(), livingTarget.getZ(), 0, 0, 0);

                    if (livingTarget.getEffect(InitEffects.INFINITE_ROTATION.get()) == null){
                        livingTarget.addEffect(new EffectInstance(InitEffects.INFINITE_ROTATION.get(), Integer.MAX_VALUE, 0, true, true, true));
                    }
                    else {
                        livingTarget.removeEffect(InitEffects.INFINITE_ROTATION.get());
                    }
                }
            }
        }
        if (!world.isClientSide() && lastTick && standEntity.isFollowingUser()) {
            standEntity.retractStand(false);
            transferedInfiniteRotation = false;
            TuskCapability tuskCapability = userPower.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
            if (tuskCapability != null){
                tuskCapability.setHasInfiniteRotationCharge(false);
                tuskCapability.setNailCount(10);
            }

        }
        standEntity.setDeltaMovement(moveForward ? task.getAdditionalData().peek(Vector3d.class) : Vector3d.ZERO);
    }

    @Override
    public StandEntityPunch punchEntity(StandEntity stand, Entity target, StandEntityDamageSource dmgSource) {
        StandEntityPunch stab = super.punchEntity(stand, target, dmgSource);
        return stab.disableBlocking(100).damage(0);
    }

    @Override
    public StandBlockPunch punchBlock(StandEntity stand, BlockPos pos, BlockState state, Direction face) {
        return new StandBlockPunch(stand, pos, state);
    }

    @Override
    protected boolean standKeepsTarget(ActionTarget target) {
        return true;
    }

    @Override
    public boolean isChainable(IStandPower standPower, StandEntity standEntity) {
        return false;
    }

    @Override
    protected boolean canBeQueued(IStandPower standPower, StandEntity standEntity) {
        return false;
    }

    @Override
    protected boolean canStandTargetEntity(StandEntity standEntity, LivingEntity target, IStandPower power) {
        return super.canStandTargetEntity(standEntity, target, power);
    }



    @Override
    public boolean isUnlocked(IStandPower power) {
        TuskCapability tuskCapability = power.getUser().getCapability(TuskCapabilityProvider.CAPABILITY).orElse(null);
        return tuskCapability != null && tuskCapability.isHasInfiniteRotationCharge() && tuskCapability.getAct() >= 3;
    }

    @Override
    protected boolean standMovesByItself(IStandPower standPower, StandEntity standEntity) {
        return true;
    }

    @Override
    public ActionConditionResult checkStandTarget(ActionTarget target, StandEntity standEntity, IStandPower standPower) {
        return ActionConditionResult.NEGATIVE;
    }
}