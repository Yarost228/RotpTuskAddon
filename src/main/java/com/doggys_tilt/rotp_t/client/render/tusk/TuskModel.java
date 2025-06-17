package com.doggys_tilt.rotp_t.client.render.tusk;

import com.doggys_tilt.rotp_t.capability.NailCapabilityProvider;
import com.doggys_tilt.rotp_t.entity.TuskEntity;
import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;

public class TuskModel extends HumanoidStandModel<TuskEntity> {
    private ModelRenderer tusk_act_1;
    private ModelRenderer tusk_act_2;
    private ModelRenderer tusk_act_3;
    private ModelRenderer Tusk_act_1_Head;
    private ModelRenderer Tusk_act_2_Head;
    private ModelRenderer Tusk_act_3_Head;
    private ModelRenderer cube_r1;
    private ModelRenderer cube_r2;
    private ModelRenderer cube_r3;
    private ModelRenderer cube_r4;
    private ModelRenderer cube_r5;
    private ModelRenderer cube_r6;
    private ModelRenderer cube_r7;
    private ModelRenderer cube_r8;
    private ModelRenderer bone13;
    private ModelRenderer bone6;
    private ModelRenderer cube_r9;
    private ModelRenderer bone14;
    private ModelRenderer cube_r10;
    private ModelRenderer Tusk_act_1_Body;
    private ModelRenderer cube_r11;
    private ModelRenderer cube_r12;
    private ModelRenderer cube_r13;
    private ModelRenderer bone10;
    private ModelRenderer cube_r14;
    private ModelRenderer bone16;
    private ModelRenderer heartSmall22;
    private ModelRenderer smallHeartCube26;
    private ModelRenderer smallHeartCube27;
    private ModelRenderer smallHeartCube28;
    private ModelRenderer smallHeartCube6_r1;
    private ModelRenderer bone9;
    private ModelRenderer cube_r15;
    private ModelRenderer heartSmall13;
    private ModelRenderer smallHeartCube17;
    private ModelRenderer smallHeartCube6_r2;
    private ModelRenderer smallHeartCube18;
    private ModelRenderer smallHeartCube19;
    private ModelRenderer bone;
    private ModelRenderer cube_r16;
    private ModelRenderer bone12;
    private ModelRenderer heartSmall16;
    private ModelRenderer smallHeartCube20;
    private ModelRenderer heartSmall17;
    private ModelRenderer smallHeartCube21;
    private ModelRenderer heartSmall18;
    private ModelRenderer smallHeartCube22;
    private ModelRenderer smallHeartCube6_r3;
    private ModelRenderer bone8;
    private ModelRenderer cube_r17;
    private ModelRenderer heartSmall12;
    private ModelRenderer smallHeartCube14;
    private ModelRenderer smallHeartCube15;
    private ModelRenderer smallHeartCube16;
    private ModelRenderer smallHeartCube6_r4;
    private ModelRenderer Tusk_act_1_Left_arm;
    private ModelRenderer cube_r18;
    private ModelRenderer Tusk_act_1_Right_arm;
    private ModelRenderer cube_r19;

    public TuskModel() {
        super();
    }
    @Override
    public void setupAnim(TuskEntity entity, float walkAnimPos, float walkAnimSpeed, float ticks, float yRotationOffset, float xRotation) {
        super.setupAnim(entity, walkAnimPos, walkAnimSpeed, ticks, yRotationOffset, xRotation);
        LivingEntity user = entity.getUser();
        if (user != null){
            user.getCapability(NailCapabilityProvider.CAPABILITY).ifPresent(nailCap -> {
                switch (nailCap.getAct()){
                    case 0:
                        Tusk_act_1_Head.xRot = this.head.xRot;
                        Tusk_act_1_Head.yRot = this.head.yRot;
                        Tusk_act_1_Head.zRot = this.head.zRot;
                        tusk_act_1.visible = body.visible;
                        tusk_act_2.visible = false;
                        tusk_act_3.visible = false;
                        break;
                    case 1:
                        Tusk_act_2_Head.xRot = this.head.xRot;
                        Tusk_act_2_Head.yRot = this.head.yRot;
                        Tusk_act_2_Head.zRot = this.head.zRot;
                        tusk_act_1.visible = false;
                        tusk_act_2.visible = body.visible;
                        tusk_act_3.visible = false;
                        break;
                    case 2:
                        Tusk_act_3_Head.xRot = this.head.xRot;
                        Tusk_act_3_Head.yRot = this.head.yRot;
                        Tusk_act_3_Head.zRot = this.head.zRot;
                        tusk_act_1.visible = false;
                        tusk_act_2.visible = false;
                        tusk_act_3.visible = body.visible;
                        break;
                    default:
                        break;
                }
            });
        }

    }

}
