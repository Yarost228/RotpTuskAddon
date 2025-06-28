package com.doggys_tilt.rotp_t.client.ui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ActType {
    FIRST(new TranslationTextComponent("gui.rotp_t.first"), 0),
    SECOND(new TranslationTextComponent("gui.rotp_t.second"),  1),
    THIRD(new TranslationTextComponent("gui.rotp_t.third"),  2),
    FOURTH(new TranslationTextComponent("gui.rotp_t.fourth"), 3);
    public static final ActType[] VALUES = values();
    final ITextComponent name;
    final int formationType;
    public void drawIcon(ActType type, int x, int y) {
        ActIcon.renderIcon(type, new MatrixStack(), x, y);
    }

    ActType(ITextComponent name, int FormationType){
        this.name = name;
        this.formationType = FormationType;
    }

    public ITextComponent getName() {
        return this.name;
    }

    public int getFormationType() {
        return this.formationType;
    }
    public static ActType getByFormationType(int FormationType){
        List<ActType> types = Arrays.stream(values()).filter(type -> type.getFormationType() == FormationType).collect(Collectors.toList());
        Optional<ActType> matchType = types.stream().findFirst();
        return matchType.orElse(null);
    }

    public static Optional<ActType> getFromFormationType(ActType type) {
        if (type == null) {
            return Optional.empty();
        }

        switch (type) {
            case SECOND:
                return Optional.of(SECOND);
            case THIRD:
                return Optional.of(THIRD);
            case FOURTH:
                return Optional.of(FOURTH);
            default:
                return Optional.of(FIRST);
        }
    }
}
