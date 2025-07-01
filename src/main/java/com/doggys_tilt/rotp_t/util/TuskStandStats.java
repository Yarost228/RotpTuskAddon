package com.doggys_tilt.rotp_t.util;

import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import net.minecraft.network.PacketBuffer;

public class TuskStandStats extends StandStats {
    private final TuskActStats statsAct2;
    private final TuskActStats statsAct3;
    private final double rangeEffectiveAct2;
    private final double rangeMaxAct2;
    private final double rangeEffectiveAct3;
    private final double rangeMaxAct3;
    private final double rangeEffectiveAct4;
    private final double rangeMaxAct4;

    protected TuskStandStats(Builder builder) {
        super(builder);
        this.statsAct2 = new TuskActStats(builder.powerAct2, builder.speedAct2, builder.durabilityAct2, builder.precisionAct2);
        this.statsAct3 = new TuskActStats(builder.powerAct3, builder.speedAct3, builder.durabilityAct3, builder.precisionAct3);
        this.rangeEffectiveAct2 = builder.rangeAct2;
        this.rangeMaxAct2 = builder.rangeMaxAct2;
        this.rangeEffectiveAct3 = builder.rangeAct3;
        this.rangeMaxAct3 = builder.rangeMaxAct3;
        this.rangeEffectiveAct4 = builder.rangeAct4;
        this.rangeMaxAct4 = builder.rangeMaxAct4;
    }

    protected TuskStandStats(PacketBuffer buf) {
        super(buf);
        this.statsAct2 = new TuskActStats(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.statsAct3 = new TuskActStats(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.rangeEffectiveAct2 = buf.readDouble();
        this.rangeMaxAct2 = buf.readDouble();
        this.rangeEffectiveAct3 = buf.readDouble();
        this.rangeMaxAct3 = buf.readDouble();
        this.rangeEffectiveAct4 = buf.readDouble();
        this.rangeMaxAct4 = buf.readDouble();
    }

    public void write(PacketBuffer buf) {
        buf.writeDouble(statsAct2.power);
        buf.writeDouble(statsAct2.speed);
        buf.writeDouble(statsAct2.durability);
        buf.writeDouble(statsAct2.precision);

        buf.writeDouble(statsAct3.power);
        buf.writeDouble(statsAct3.speed);
        buf.writeDouble(statsAct3.durability);
        buf.writeDouble(statsAct3.precision);

        buf.writeDouble(rangeEffectiveAct2);
        buf.writeDouble(rangeMaxAct2);

        buf.writeDouble(rangeEffectiveAct3);
        buf.writeDouble(rangeMaxAct3);

        buf.writeDouble(rangeEffectiveAct4);
        buf.writeDouble(rangeMaxAct4);
    }
    static {
        registerFactory(TuskStandStats.class, TuskStandStats::new);
    }

    public static class Builder extends AbstractBuilder<TuskStandStats.Builder, TuskStandStats> {
        private double powerBase;
        private double powerAct2;
        private double powerAct3;
        private double powerMax;

        private double speedBase;
        private double speedAct2;
        private double speedAct3;
        private double speedMax;

        private double rangeEffective;
        private double rangeAct2;
        private double rangeAct3;
        private double rangeAct4;

        private double rangeMax;
        private double rangeMaxAct2;
        private double rangeMaxAct3;
        private double rangeMaxAct4;

        private double durabilityBase;
        private double durabilityAct2;
        private double durabilityAct3;
        private double durabilityMax;

        private double precisionBase;
        private double precisionAct2;
        private double precisionAct3;
        private double precisionMax;

        public Builder power(double powerAct1, double powerAct2, double powerAct3, double powerAct4) {
            this.powerBase = Math.max(powerAct1, 0);
            this.powerAct2 = Math.max(powerAct2, 0);
            this.powerAct3 = Math.max(powerAct3, 0);
            this.powerMax = Math.max(powerAct4, 0);
            return getThis();
        }
        public Builder speed(double speedAct1, double speedAct2, double speedAct3, double speedAct4) {
            this.speedBase = Math.max(speedAct1, 0);
            this.speedAct2 = Math.max(speedAct2, 0);
            this.speedAct3 = Math.max(speedAct3, 0);
            this.speedMax = Math.max(speedAct4, 0);
            return getThis();
        }
        public Builder durability(double durabilityAct1, double durabilityAct2, double durabilityAct3, double durabilityAct4) {
            this.durabilityBase = Math.max(durabilityAct1, 0);
            this.durabilityAct2 = Math.max(durabilityAct2, 0);
            this.durabilityAct3 = Math.max(durabilityAct3, 0);
            this.durabilityMax = Math.max(durabilityAct4, 0);
            return getThis();
        }
        public Builder precision(double precisionAct1, double precisionAct2, double precisionAct3, double precisionAct4) {
            this.precisionBase = Math.max(precisionAct1, 0);
            this.precisionAct2 = Math.max(precisionAct2, 0);
            this.precisionAct3 = Math.max(precisionAct3, 0);
            this.precisionMax = Math.max(precisionAct4, 0);
            return getThis();
        }
        public Builder range(double rangeAct1, double rangeMaxAct1,
                             double rangeAct2, double rangeMaxAct2,
                             double rangeAct3, double rangeMaxAct3,
                             double rangeAct4, double rangeMaxAct4) {
            this.rangeEffective = Math.max(rangeAct1, 1.0);
            this.rangeMax = Math.max(rangeMaxAct1, rangeAct1);
            this.rangeAct2 = Math.max(rangeAct2, 1.0);
            this.rangeMaxAct2 = Math.max(rangeMaxAct2, rangeAct2);
            this.rangeAct3 = Math.max(rangeAct3, 1.0);
            this.rangeMaxAct3 = Math.max(rangeMaxAct3, rangeAct3);
            this.rangeAct4 = Math.max(rangeAct4, 1.0);
            this.rangeMaxAct4 = Math.max(rangeMaxAct4, rangeAct4);
            return getThis();
        }

        @Override
        protected Builder getThis() {
            return this;
        }
        @Override
        protected TuskStandStats createStats() {
            return new TuskStandStats(this);
        }
    }
    private static class TuskActStats {
        private final double power;
        private final double speed;
        private final double durability;
        private final double precision;

        private TuskActStats(double power, double speed, double durability, double precision) {
            this.power = power;
            this.speed = speed;
            this.durability = durability;
            this.precision = precision;
        }
    }
}
