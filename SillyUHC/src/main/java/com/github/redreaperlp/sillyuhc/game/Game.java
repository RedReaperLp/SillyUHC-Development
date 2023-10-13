package com.github.redreaperlp.sillyuhc.game;

import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private PhaseType currentPhase = PhaseType.WAITING;
    private List<Phase> phases;
    private SillyUHC uhc;

    public Game(List<Phase> phases, SillyUHC uhc) {
        this.phases = new ArrayList<>(phases);
        this.uhc = uhc;
    }

    public void tick() {
        if (currentPhase == PhaseType.WAITING) return;
        if (phases.isEmpty()) return;
        if (phases.get(0).tick()) return;
        AdventureUtil.broadcast(Component.text("Phase " + phases.get(0).getPhaseType() + " ended!", TextColor.color(0xff0000)));
        phases.remove(0);
        if (phases.isEmpty()) {
            phases.add(new Phase(PhaseType.PVP, 100));
            return;
        }
        currentPhase = phases.get(0).getPhaseType();
    }

    public void start() {
        currentPhase = PhaseType.NO_PVP;
    }
    public Phase getCurrentPhase() {
        if (phases.isEmpty()) return null;
        return phases.get(0);
    }

    public PhaseType currentPhaseType() {
        if (currentPhase == null) return PhaseType.END;
        return currentPhase;
    }

    public enum PhaseType {
        WAITING,
        NO_PVP,
        PVP,
        END
    }
}
