package org.bukkit.craftbukkit.v1_17_R1.block.data.type;

import org.bukkit.block.data.type.Tripwire;
import org.bukkit.craftbukkit.v1_17_R1.block.data.CraftBlockData;

public abstract class CraftTripwire extends CraftBlockData implements Tripwire {

    private static final net.minecraft.world.level.block.state.properties.BooleanProperty DISARMED = getBoolean("disarmed");

    @Override
    public boolean isDisarmed() {
        return get(CraftTripwire.DISARMED);
    }

    @Override
    public void setDisarmed(boolean disarmed) {
        set(CraftTripwire.DISARMED, disarmed);
    }
}
