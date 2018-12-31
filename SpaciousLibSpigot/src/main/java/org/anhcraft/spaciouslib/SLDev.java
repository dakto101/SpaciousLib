package org.anhcraft.spaciouslib;

import org.anhcraft.spaciouslib.annotations.AnnotationHandler;
import org.anhcraft.spaciouslib.builders.command.ArgumentType;
import org.anhcraft.spaciouslib.builders.command.ChildCommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandBuilder;
import org.anhcraft.spaciouslib.builders.command.CommandCallback;
import org.anhcraft.spaciouslib.events.*;
import org.anhcraft.spaciouslib.inventory.BookManager;
import org.anhcraft.spaciouslib.inventory.HandSlot;
import org.anhcraft.spaciouslib.inventory.anvil.Anvil;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilHandler;
import org.anhcraft.spaciouslib.inventory.anvil.AnvilSlot;
import org.anhcraft.spaciouslib.mojang.MojangAPI;
import org.anhcraft.spaciouslib.mojang.SkinAPI;
import org.anhcraft.spaciouslib.protocol.CustomPayload;
import org.anhcraft.spaciouslib.utils.InventoryUtils;
import org.anhcraft.spaciouslib.utils.PlayerUtils;
import org.anhcraft.spaciouslib.utils.TimedSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class SLDev implements Listener {
    public SLDev(){
        AnnotationHandler.register(this.getClass(), this);

        new CommandBuilder("sls", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                builder.sendHelpMessages(sender, true, true);
            }
        })
                .addChild(new ChildCommandBuilder().path("anvil", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                if(sender instanceof Player){
                    new Anvil((Player) sender, new AnvilHandler() {
                        @Override
                        public void handle(Player player, String input, ItemStack item, AnvilSlot slot) {
                            player.sendMessage(input);
                        }
                    }).setItem(AnvilSlot.INPUT_LEFT, new ItemStack(Material.DIAMOND, 1)).open();
                }
            }
        }).var("player", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                new Anvil(Bukkit.getPlayer(value), new AnvilHandler() {
                    @Override
                    public void handle(Player player, String input, ItemStack item, AnvilSlot slot) {
                        player.sendMessage(input);
                    }
                }).setItem(AnvilSlot.INPUT_LEFT, new ItemStack(Material.DIAMOND, 1)).open();
            }
        }, ArgumentType.ONLINE_PLAYER).build())

                .addChild(new ChildCommandBuilder().path("book").var("player", new CommandCallback() {
            @Override
            public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                CustomPayload.openBook(HandSlot.MAINHAND).sendPlayer(Bukkit.getPlayer(value));
            }
        }, ArgumentType.ONLINE_PLAYER).build())

                .addChild(new ChildCommandBuilder().path("utils freeze").var("player", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        PlayerUtils.freeze(Bukkit.getPlayer(value));
                    }
                }, ArgumentType.ONLINE_PLAYER).build())

                .addChild(new ChildCommandBuilder().path("utils unfreeze").var("player", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        PlayerUtils.unfreeze(Bukkit.getPlayer(value));
                    }
                }, ArgumentType.ONLINE_PLAYER).build())

                .addChild(new ChildCommandBuilder().path("utils changeskin").var("skin", ArgumentType.ANYTHING).var("player", new CommandCallback() {
                    @Override
                    public void run(CommandBuilder builder, CommandSender sender, int command, String[] args, int arg, String value) {
                        try {
                            PlayerUtils.changeSkin(Bukkit.getPlayer(value), SkinAPI.getSkin(MojangAPI.getUniqueId(args[arg-1]).getB()).getSkin());
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, ArgumentType.ONLINE_PLAYER).build())
                .build(SpaciousLib.instance).clone("spaciouslibspigot").addAlias("slspigot").build(SpaciousLib.instance);
    }


    @EventHandler
    public void reload(ServerReloadEvent event){
        Bukkit.getServer().broadcastMessage(ChatColor.YELLOW+"Server is reloading...");
    }

    @EventHandler
    public void stop(ServerStopEvent event){
        Bukkit.getServer().broadcastMessage(ChatColor.RED+"Server is stopping...");
    }

    @EventHandler
    public void equip(ArmorEquipEvent event){
        if(!InventoryUtils.isNull(event.getNewArmor())){
            if(event.getNewArmor().getType().equals(Material.DIAMOND_HELMET)) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 9999999, 1));
            }
            return;
        }
        if(!InventoryUtils.isNull(event.getOldArmor())) {
            if(event.getOldArmor().getType().equals(Material.DIAMOND_HELMET)) {
                event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }

    @EventHandler
    public void bow(BowArrowHitEvent event){
        event.getShooter().teleport(event.getArrow().getLocation());
    }

    @EventHandler
    public void npc(NPCInteractEvent event){
        event.getPlayer().getInventory().addItem(
                new BookManager("&aBook", 1)
                        .addPage("Page 1")
                        .addPage("Page 2")
                        .addPage("Page 3")
                        .addPage("Page 4")
                        .addPage("Page 5")
                        .setAuthor("anhcraft")
                        .setBookGeneration(BookManager.BookGeneration.COPY_ORIGINAL)
                        .getItem());
        event.getPlayer().updateInventory();
    }

    public static TimedSet<UUID> jumpers = new TimedSet<>();

    @EventHandler
    public void jump(PlayerJumpEvent event){
        if(event.isOnSpot()){
            if(jumpers.contains(event.getPlayer().getUniqueId())){
                event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(2));
            }
            jumpers.add(event.getPlayer().getUniqueId(), 1);
        }
    }
}
