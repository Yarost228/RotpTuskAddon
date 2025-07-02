package com.doggys_tilt.rotp_t.util;

import com.github.standobyte.jojo.client.ui.standstats.StandStatsRenderer;
import com.github.standobyte.jojo.entity.stand.StandStatFormulas;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import net.minecraft.network.PacketBuffer;

public class TuskStandStats extends StandStats {
    private final TuskActStats statsAct2;
    private final TuskActStats statsAct3;
    private final TuskActStats statsAct4;
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
        this.statsAct4 = new TuskActStats(builder.powerAct4, builder.speedAct4, builder.durabilityAct4, builder.precisionAct4);
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
        this.statsAct4 = new TuskActStats(buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.rangeEffectiveAct2 = buf.readDouble();
        this.rangeMaxAct2 = buf.readDouble();
        this.rangeEffectiveAct3 = buf.readDouble();
        this.rangeMaxAct3 = buf.readDouble();
        this.rangeEffectiveAct4 = buf.readDouble();
        this.rangeMaxAct4 = buf.readDouble();
    }

    public void write(PacketBuffer buf) {
        super.write(buf);
        buf.writeDouble(statsAct2.power);
        buf.writeDouble(statsAct2.speed);
        buf.writeDouble(statsAct2.durability);
        buf.writeDouble(statsAct2.precision);

        buf.writeDouble(statsAct3.power);
        buf.writeDouble(statsAct3.speed);
        buf.writeDouble(statsAct3.durability);
        buf.writeDouble(statsAct3.precision);

        buf.writeDouble(statsAct4.power);
        buf.writeDouble(statsAct4.speed);
        buf.writeDouble(statsAct4.durability);
        buf.writeDouble(statsAct4.precision);

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

    public double getActPower(int act) {
        switch (act){
            case 1:
                return statsAct2.power;
            case 2:
                return statsAct3.power;
            case 3:
                return statsAct4.power;
            default:
                return getBasePower();
        }
    }

    public double getActAttackSpeed(int act) {
        switch (act){
            case 1:
                return statsAct2.speed;
            case 2:
                return statsAct3.speed;
            case 3:
                return statsAct4.speed;
            default:
                return getBaseAttackSpeed();
        }
    }

    public double getActMovementSpeed(int act) {
        switch (act){
            case 1:
                return StandStatFormulas.getMovementSpeed(statsAct2.speed);
            case 2:
                return StandStatFormulas.getMovementSpeed(statsAct3.speed);
            case 3:
                return StandStatFormulas.getMovementSpeed(statsAct4.speed);
            default:
                return StandStatFormulas.getMovementSpeed(getBaseAttackSpeed());
        }
    }

    public double getActDurability(int act) {
        switch (act){
            case 1:
                return statsAct2.power;
            case 2:
                return statsAct3.power;
            case 3:
                return statsAct4.power;
            default:
                return getBaseDurability();
        }
    }

    public double getActPrecision(int act) {
        switch (act){
            case 1:
                return statsAct2.precision;
            case 2:
                return statsAct3.precision;
            case 3:
                return statsAct4.precision;
            default:
                return getBasePrecision();
        }
    }

    public double getActRange(int act) {
        switch (act){
            case 1:
                return rangeEffectiveAct2;
            case 2:
                return rangeEffectiveAct3;
            case 3:
                return rangeEffectiveAct4;
            default:
                return getEffectiveRange();
        }
    }

    public double getActRangeMax(int act) {
        switch (act){
            case 1:
                return rangeMaxAct2;
            case 2:
                return rangeMaxAct3;
            case 3:
                return rangeMaxAct4;
            default:
                return getMaxRange();
        }
    }

    public static class Builder extends AbstractBuilder<TuskStandStats.Builder, TuskStandStats> {
        private double powerAct2;
        private double powerAct3;
        private double powerAct4;

        private double speedAct2;
        private double speedAct3;
        private double speedAct4;

        private double rangeAct2;
        private double rangeAct3;
        private double rangeAct4;

        private double rangeMaxAct2;
        private double rangeMaxAct3;
        private double rangeMaxAct4;

        private double durabilityAct2;
        private double durabilityAct3;
        private double durabilityAct4;

        private double precisionAct2;
        private double precisionAct3;
        private double precisionAct4;

        public Builder powerForActs(double powerAct2, double powerAct3, double powerAct4) {
            this.powerAct2 = Math.max(powerAct2, 0);
            this.powerAct3 = Math.max(powerAct3, 0);
            this.powerAct4 = Math.max(powerAct4, 0);
            return getThis();
        }
        public Builder speedForActs(double speedAct2, double speedAct3, double speedAct4) {
            this.speedAct2 = Math.max(speedAct2, 0);
            this.speedAct3 = Math.max(speedAct3, 0);
            this.speedAct4 = Math.max(speedAct4, 0);
            return getThis();
        }
        public Builder durabilityForActs(double durabilityAct2, double durabilityAct3, double durabilityAct4) {
            this.durabilityAct2 = Math.max(durabilityAct2, 0);
            this.durabilityAct3 = Math.max(durabilityAct3, 0);
            this.durabilityAct4 = Math.max(durabilityAct4, 0);
            return getThis();
        }
        public Builder precisionForActs(double precisionAct2, double precisionAct3, double precisionAct4) {
            this.precisionAct2 = Math.max(precisionAct2, 0);
            this.precisionAct3 = Math.max(precisionAct3, 0);
            this.precisionAct4 = Math.max(precisionAct4, 0);
            return getThis();
        }
        public Builder rangeForActs(double rangeAct2, double rangeMaxAct2,
                             double rangeAct3, double rangeMaxAct3,
                             double rangeAct4, double rangeMaxAct4) {
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
