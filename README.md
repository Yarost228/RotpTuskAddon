# Gecko format animations

For a long time adding Stand animations was a tedious task, having to manually input each keyframe of each pose into the model's code. This new animation import feature is being added to fix that problem, so that both the main mod and the addons can easily implement new model animations.

<b>Fair warning:</b> the feature is still in development, and some small things about the model structure or the animations might change in the future. You will also need to implement <i>all</i> animations, including the regular punches, that were automatically added before. On the other hand, your model doesn't have to use the same animations for attacks, and you can improve upon the old animations that are being used in the mod for now.

First, install GeckoLib Animation Utils plugin for Blockbench. This will allow you to make both the model and the animations in the GeckoLib Animated Model format, which is what we're after. You might also find the [GeckoLib guide for making models](https://github.com/bernie-g/geckolib/wiki/Making-Your-Models-(Blockbench)) helpful.

This branch includes [the .bbmodel file](https://github.com/StandoByte/RotP-Addon-example/blob/new-model-anim-import/example_stand.geo.bbmodel) of our example Stand, which you can download and open in Blockbench. This is just for you to use as a template, you don't need to include the .bbmodel file of your model in your addon. 

## [New/changed files](https://github.com/StandoByte/RotP-Addon-example/compare/master...new-model-anim-import):
- `build.gradle` and `gradle.properties` - we use a newer version of the RotP mod, which includes the new animation system, and add a library that lets us use math expressions in the animation keyframes (this library is now also included in the main mod via Gradle shading)<br>
- `assets/<mod_id>/geo/<model_id>.geo.json` - updated hierarchy of the model parts, to be able to correctly rotate the limbs around the X axis in the animations<br>
- `assets/<mod_id>/animations/<model_id>.animation.json` - the model animation file made by Blockbench<br>
- `client/render/ExampleStandModel.java` - removed the old hard-coded animations from the Java code of the model<br>
