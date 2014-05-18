package com.ryanmichela.trees;

import com.ryanmichela.trees.rendering.TreeRenderer;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Random;

/**
 * Copyright 2014 Ryan Michela
 */
public class CreateTreeEventHandler implements Listener {
    private Plugin plugin;
    private TreeRenderer renderer;
    private PopupHandler popup;

    public CreateTreeEventHandler(Plugin plugin) {
        this.plugin = plugin;
        renderer = new TreeRenderer(plugin);
        popup = new PopupHandler(plugin);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("gianttrees.create"))
        if (event.getItem() != null)
        if (event.getItem().getType() == Material.GOLD_HOE && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Chunk chunk = event.getClickedBlock().getChunk();
            World world = chunk.getWorld();
            Random seed = new Random(world.getSeed());

            popup.sendPopup(event.getPlayer(), "Stand back!");

            File treeFile = new File(plugin.getDataFolder(), "tree.xml");
            File rootFile = new File(plugin.getDataFolder(), "tree.root.xml");
            renderer.renderTree(event.getClickedBlock().getLocation(), treeFile, rootFile, true, seed.nextInt());
        }
    }
}
