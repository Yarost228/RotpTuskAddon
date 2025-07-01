package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.client.ui.screen.ChangeActScreen;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class TuskSelectAct extends StandAction {
    public TuskSelectAct(StandAction.Builder builder){
        super(builder);
    }

    @Override
    protected void perform(World world, LivingEntity user, IStandPower power, ActionTarget target) {
        if (world.isClientSide() && user == ClientUtil.getClientPlayer()){
            ChangeActScreen.openWindowOnClick();
        }
    }
}
