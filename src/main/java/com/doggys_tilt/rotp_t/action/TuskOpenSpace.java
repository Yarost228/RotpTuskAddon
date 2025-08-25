package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.capability.TuskCapability;
import com.doggys_tilt.rotp_t.capability.TuskCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.block_replacer.EntityBlockSwapper;
import com.doggys_tilt.rotp_t.init.InitEntities;
import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Optional;

public class TuskOpenSpace extends StandEntityAction {
    public TuskOpenSpace(StandEntityAction.Builder builder) {
        super(builder);
    }
    @Override
    public TargetRequirement getTargetRequirement() {
        return TargetRequirement.BLOCK;
    }
    @Override
    public ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        Optional<TuskCapability> cap = user.getCapability(TuskCapabilityProvider.CAPABILITY).resolve();
        StandEntity standEntity = (StandEntity) power.getStandManifestation();
        if (cap.isPresent() && standEntity != null){
            if (cap.get().getAct() >= 3 && standEntity.canBreakBlock(target.getBlockPos(), user.level.getBlockState(target.getBlockPos()))) {
                return ActionConditionResult.POSITIVE;
            }
        }
        return ActionConditionResult.NEGATIVE;
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()){
            ActionTarget result = standEntity.aimWithThisOrUser(6, task.getTarget(), false);
            if (result.getType() == ActionTarget.TargetType.BLOCK){
                for (int i = 0; i < 12; i++) {
                    BlockPos pos = result.getBlockPos().relative(result.getFace().getOpposite(), i);
                    EntityBlockSwapper.swapBlock(world, pos, Blocks.AIR.defaultBlockState(), 200, false, false);
                    if (!world.getBlockState(pos.relative(result.getFace().getOpposite())).getMaterial().isSolid() || world.getBlockState(pos.relative(result.getFace().getOpposite())).getBlock() == Blocks.BEDROCK){
                        break;
                    }
                }
            }
        }
    }
}
