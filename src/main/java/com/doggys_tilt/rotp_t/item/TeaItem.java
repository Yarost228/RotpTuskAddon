package com.doggys_tilt.rotp_t.item;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.github.standobyte.jojo.item.StandDiscItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class TeaItem extends Item {

    public TeaItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, World pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);
        PlayerEntity playerentity = pEntityLiving instanceof PlayerEntity ? (PlayerEntity)pEntityLiving : null;

        if (!pLevel.isClientSide) {
            playerentity.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCapability -> {
                if (nailCapability.getNailCount() <= 10){
                    nailCapability.setNailCount(nailCapability.getNailCount() + 1);
                }
            });
        }

        if (playerentity == null || !playerentity.abilities.instabuild) {
            if (pStack.isEmpty()) {
                return new ItemStack(Items.GLASS_BOTTLE);
            }
            if (playerentity != null) {
                playerentity.inventory.add(new ItemStack(Items.GLASS_BOTTLE));
            }
            pStack.shrink(1);
        }
        return pStack;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    @Override
    public UseAction getUseAnimation(ItemStack pStack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    @Override
    public ActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {

        return DrinkHelper.useDrink(pLevel, pPlayer, pHand);
    }
}
