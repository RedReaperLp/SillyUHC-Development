/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_17_R1.block.impl;

public final class CraftChorusFlower extends org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData implements org.bukkit.block.data.Ageable {

    public CraftChorusFlower() {
        super();
    }

    public CraftChorusFlower(net.minecraft.world.level.block.state.BlockState state) {
        super(state);
    }

    // org.bukkit.craftbukkit.v1_17_R1.block.data.CraftAgeable

    private static final net.minecraft.world.level.block.state.properties.IntegerProperty AGE = getInteger(net.minecraft.world.level.block.ChorusFlowerBlock.class, "age");

    @Override
    public int getAge() {
        return get(CraftChorusFlower.AGE);
    }

    @Override
    public void setAge(int age) {
        set(CraftChorusFlower.AGE, age);
    }

    @Override
    public int getMaximumAge() {
        return getMax(CraftChorusFlower.AGE);
    }
}
