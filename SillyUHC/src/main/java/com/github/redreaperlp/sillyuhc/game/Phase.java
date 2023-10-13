package com.github.redreaperlp.sillyuhc.game;

import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public class Phase {
    private long timeRemaining;
    private Game.PhaseType phaseType;

    public Phase(Game.PhaseType phaseType, long timeRemaining) {
        this.phaseType = phaseType;
        this.timeRemaining = timeRemaining;
    }

    public long getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(long timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Game.PhaseType getPhaseType() {
        return phaseType;
    }

    public void setPhaseType(Game.PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    public boolean tick() {
        timeRemaining--;
        return timeRemaining >= 0;
    }
}
