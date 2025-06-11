package com.rotpaddon.exampleaddon.action;

import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import com.rotpaddon.exampleaddon.entity.WormholeArmEntity;
import com.rotpaddon.exampleaddon.entity.WormholeTeleporterEntity;
import com.rotpaddon.exampleaddon.init.InitItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
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
        if (!world.isClientSide()) {


        }
    }
}
