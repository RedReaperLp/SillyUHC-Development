/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_17_R1.block.impl;

public final class CraftDaylightDetector extends org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData implements org.bukkit.block.data.type.DaylightDetector, org.bukkit.block.data.AnaloguePowerable {

    public CraftDaylightDetector() {
        super();
    }

    public CraftDaylightDetector(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_17_R1.block.data.type.CraftDaylightDetector

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty INVERTED = getBoolean(net.minecraft.world.level.block.DaylightDetectorBlock.class, "inverted");

    @Override
    public boolean isInverted() {
        return get(CraftDaylightDetector.INVERTED);
    }

    @Override
    public void setInverted(boolean inverted) {
        set(CraftDaylightDetector.INVERTED, inverted);
    }

    // org.bukkit.craftbukkit.v1_17_R1.block.data.CraftAnaloguePowerable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty POWER = getInteger(net.minecraft.world.level.block.DaylightDetectorBlock.class, "power");

    @Override
    public int getPower() {
        return get(CraftDaylightDetector.POWER);
    }

    @Override
    public void setPower(int power) {
        set(CraftDaylightDetector.POWER, power);
    }

    @Override
    public int getMaximumPower() {
        return getMax(CraftDaylightDetector.POWER);
    }
}
