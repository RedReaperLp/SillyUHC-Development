package com.github.redreaperlp.sillyuhc.game;

import com.github.redreaperlp.sillyuhc.NameSpacedKeyWrapper;
import com.github.redreaperlp.sillyuhc.SillyUHC;
import com.github.redreaperlp.sillyuhc.VotedMap;
import com.github.redreaperlp.sillyuhc.game.participators.Participator;
import com.github.redreaperlp.utils.AdventureUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.redreaperlp.sillyuhc.SillyUHC.adventureUtil;

public class Game {
    private PhaseType currentPhase = PhaseType.WAITING;
    private List<Participator> participators = new ArrayList<>();
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
        Random random = new Random();
        VotedMap vM = uhc.votedMap;
        currentPhase = PhaseType.NO_PVP;
        Bukkit.getOnlinePlayers().forEach(player -> {
            PersistentDataContainer pdc = player.getPersistentDataContainer();
            if (pdc.has(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING)) {
                if (pdc.get(NameSpacedKeyWrapper.keyParticipator, PersistentDataType.STRING).equals("participating")) {
                    participators.add(new Participator(player));

                    int x = random.nextInt(vM.size() * 2) - vM.size();
                    int z = random.nextInt(vM.size() * 2) - vM.size();


                    player.teleport(new Location(vM.world(), random.nextInt(vM.size() * 2) - vM.size(), 255, random.nextInt(vM.size() * 2) - vM.size()));
                } else {
                    adventureUtil.sendWithPrefix(Component.text("You are spectating", TextColor.color(0xff0000)), player);
                }
            }
        });
    }

    public int searchHighestNonAirBelow(int maxHeight, int x, int z) {
        for (int y = maxHeight; y > 0; y--) {
            if (uhc.votedMap.world().getBlockAt(x, y, z).getType().isAir()) {
                continue;
            }
            return y;
        }
        return -1;
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
