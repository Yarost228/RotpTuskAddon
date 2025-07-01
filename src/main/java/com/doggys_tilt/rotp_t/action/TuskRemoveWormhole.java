package com.doggys_tilt.rotp_t.action;

import com.doggys_tilt.rotp_t.init.InitStands;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

public class TuskRemoveWormhole extends StandAction {
    public TuskRemoveWormhole(StandAction.Builder builder) {
        super(builder);
    }
    @Override
    public boolean isUnlocked(IStandPower power) {
        return InitStands.WORMHOLE.get().isUnlocked(power);
    }
}
