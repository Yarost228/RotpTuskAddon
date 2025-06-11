package com.rotpaddon.exampleaddon.client.render;

import com.github.standobyte.jojo.client.render.entity.model.stand.HumanoidStandModel;
import com.rotpaddon.exampleaddon.entity.ExampleStandEntity;

import net.minecraft.client.renderer.model.ModelRenderer;

/* 
 * The new way of importing Stand model animations has arrived, 
 * so hard-coding the animation keyframes inside models is no longer needed.
 * 
 * Although with great power comes great responsibility, 
 * so you will now have to add animations for all abilities, including the basic attacks.
 * On the other hand, this means that you can now do anything with them! (or at least should be able to)
 */
public class ExampleStandModel extends HumanoidStandModel<ExampleStandEntity> {

	public ExampleStandModel() {
		super();
	}
}