package com.ryanmichela.trees.rendering;

import com.ryanmichela.trees.history.WorldEditHistoryTracker;
import me.desht.dhutils.block.CraftMassBlockUpdate;
import me.desht.dhutils.block.MassBlockUpdate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright 2014 Ryan Michela
 */
public class WorldChangeTracker {
    private Map<WorldChangeKey, WorldChange> changes = new HashMap<WorldChangeKey, WorldChange>(10000);
    private CraftMassBlockUpdate massBlockUpdate;
    private MassBlockUpdate.RelightingStrategy relightingStrategy;
    private boolean recordHistory;

    public WorldChangeTracker(CraftMassBlockUpdate massBlockUpdate, MassBlockUpdate.RelightingStrategy relightingStrategy, boolean recordHistory) {
        this.massBlockUpdate = massBlockUpdate;
        this.relightingStrategy = relightingStrategy;
        this.recordHistory = recordHistory;
    }

    public void addChange(Vector location, Material material, byte materialData, boolean overwrite) {
        addChange(new WorldChange(location, material, materialData), overwrite);
    }

    public void addChange(WorldChange worldChange, boolean overwrite) {
        WorldChangeKey key = new WorldChangeKey(worldChange.location.getBlockX(), worldChange.location.getBlockY(), worldChange.location.getBlockZ());
        if (changes.containsKey(key)) {
            if (overwrite) {
                changes.put(key, worldChange);
            }
        } else {
            changes.put(key, worldChange);
        }
    }

    public WorldChange getChange(WorldChangeKey key) {
        return changes.get(key);
    }

    public Collection<WorldChange> getChanges() {
        return changes.values();
    }

    public void applyChanges(Location refPoint, Player byPlayer) {
        Bukkit.getLogger().info("[GiantTrees] Affected blocks: " + changes.size());
        if (relightingStrategy == MassBlockUpdate.RelightingStrategy.HYBRID || relightingStrategy == MassBlockUpdate.RelightingStrategy.DEFERRED) {
            massBlockUpdate.setDeferredBufferSize(changes.size());
        }

        WorldEditHistoryTracker historyTracker = null;
        if (recordHistory && Bukkit.getServer().getPluginManager().isPluginEnabled("WorldEdit")) {
            historyTracker = new WorldEditHistoryTracker(refPoint, byPlayer);
        } else {
            Bukkit.getLogger().warning("[GiantTrees] WorldEdit not installed. Undo capability disabled.");
        }

        if (historyTracker != null) {
            for (WorldChange change : changes.values()) {
                Location changeLoc = refPoint.clone().add(change.location);
                int blockY = changeLoc.getBlockY();
                if (blockY <= 255 && blockY >= 0) {
                    historyTracker.recordHistoricChange(changeLoc, change.material.getId(), change.materialData);
                }
            }
        }

        for (WorldChange change : changes.values()) {
            Location changeLoc = refPoint.clone().add(change.location);
            int blockY = changeLoc.getBlockY();
            if (blockY <= 255 && blockY >= 0) {
                massBlockUpdate.setBlock(changeLoc.getBlockX(), blockY, changeLoc.getBlockZ(), change.material.getId(), change.materialData);
            }
        }

        if (historyTracker != null) {
            historyTracker.finalizeHistoricChanges();
        }
        massBlockUpdate.notifyClients();
    }


}
