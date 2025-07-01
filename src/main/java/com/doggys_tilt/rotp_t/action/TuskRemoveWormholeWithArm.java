package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

public class TuskRemoveWormholeWithArm extends StandAction {
    public TuskRemoveWormholeWithArm(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public boolean isUnlocked(IStandPower power) {
        return InitStands.WORMHOLE_WITH_ARM.get().isUnlocked(power);
    }
}
