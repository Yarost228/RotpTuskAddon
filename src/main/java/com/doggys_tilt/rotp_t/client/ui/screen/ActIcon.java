package com.doggys_tilt.rotp_t.client.ui.screen;

import com.github.standobyte.jojo.client.standskin.StandSkinsManager;
import com.github.standobyte.jojo.client.ui.BlitFloat;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.doggys_tilt.rotp_t.RotpTuskAddon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.atomic.AtomicReference;

public class ActIcon {
    private static ResourceLocation FIRST = new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act1.png");
    private static ResourceLocation SECOND = new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act2.png");
    private static ResourceLocation THIRD = new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act3.png");
    private static ResourceLocation FOURTH = new ResourceLocation(RotpTuskAddon.MOD_ID, "textures/power/act4.png");


    public static ResourceLocation getIconByType(ActType type){
        switch (type){
            case SECOND:
                return SECOND;
            case THIRD:
                return THIRD;
            case FOURTH:
                return FOURTH;
            default:
                return FIRST;
        }
    }

    public static void renderIcon(ActType type, MatrixStack matrixStack, float x, float y){
        AtomicReference<ResourceLocation> icon = new AtomicReference<>(getIconByType(type));
        IStandPower.getStandPowerOptional(Minecraft.getInstance().player).ifPresent(power -> {
            icon.set(StandSkinsManager.getInstance().getRemappedResPath(manager -> manager
                    .getStandSkin(power.getStandInstance().get()), icon.get()));
        });
        Minecraft.getInstance().getTextureManager().bind(icon.get());
        BlitFloat.blitFloat(matrixStack, x, y, 0, 0, 16, 16, 16, 16);
    }
}
