package com.doggys_tilt.rotp_t.action;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TuskCreateWormhole extends StandAction {
    public TuskCreateWormhole(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        user.swinging = false;
        user.swing(Hand.MAIN_HAND);

    }
}
