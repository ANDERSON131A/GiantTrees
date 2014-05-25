package com.ryanmichela.trees;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.plugin.Plugin;

import java.util.Random;

/**
 * Copyright 2014 Ryan Michela
 */
public class TreePopulator extends BlockPopulator {
    private Plugin plugin;

    public TreePopulator(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        plugin.getLogger().info(chunk.getX() + ":" + chunk.getZ());
        /*File treeFile = new File(plugin.getDataFolder(), "tree.xml");
        File rootFile = new File(plugin.getDataFolder(), "tree.root.xml");

        Location refPoint = new Location(world, chunk.getX() * 16 + 8, 64, chunk.getZ() * 16 + 8);
        refPoint.setY(world.getHighestBlockYAt(refPoint));

        TreeRenderer renderer = new TreeRenderer(plugin);
        renderer.renderTree(refPoint, treeFile, rootFile, true, random.nextInt());*/
    }
}
