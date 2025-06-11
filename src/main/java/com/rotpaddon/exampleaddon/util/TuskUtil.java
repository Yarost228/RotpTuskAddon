package com.rotpaddon.exampleaddon.util;

import com.github.standobyte.jojo.util.mod.JojoModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class TuskUtil {
    public static RayTraceResult rayTrace(Entity entity, double reachDistance, @Nullable Predicate<Entity> entityFilter,
                                          double rayTraceInflate) {
        return rayTrace(entity, reachDistance, entityFilter, rayTraceInflate, 0);
    }

    public static RayTraceResult rayTrace(Entity entity, double reachDistance,
                                          @Nullable Predicate<Entity> entityFilter,
                                          double rayTraceInflate, double standPrecision) {
        return rayTraceMultipleEntities(entity, reachDistance, entityFilter, rayTraceInflate, standPrecision)[0];
    }

    public static RayTraceResult[] rayTraceMultipleEntities(Entity entity, double reachDistance,
                                                            @Nullable Predicate<Entity> entityFilter,
                                                            double rayTraceInflate, double standPrecision) {
        return JojoModUtil.rayTraceMultipleEntities(entity.getEyePosition(1.0F), entity.getViewVector(1.0F), reachDistance,
                entity.level, entity,
                entityFilter, RayTraceContext.BlockMode.COLLIDER,
                rayTraceInflate, standPrecision);
    }
}
