package io.github.TcFoxy.ArenaTOW.BattleArena.controllers;

import mc.alk.arena.util.EffectUtil;
import mc.alk.arena.util.InventoryUtil;
import mc.alk.arena.util.InventoryUtil.PInv;
import mc.alk.arena.util.Log;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.TcFoxy.ArenaTOW.BattleArena.MyDefaults;
import io.github.TcFoxy.ArenaTOW.BattleArena.listeners.MyBAPlayerListener;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyArenaPlayer;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.MyPlayerSave;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MyPlayerStoreController {
    static final MyPlayerStoreController INSTANCE = new MyPlayerStoreController();

    final HashMap<UUID, MyPlayerSave> saves = new HashMap<UUID, MyPlayerSave>();
    public MyPlayerStoreController(){}

    public MyPlayerStoreController(MyPlayerSave save) {
        saves.put(save.getID(), save);
    }

    private MyPlayerSave getOrCreateSave(final MyArenaPlayer player) {
        MyPlayerSave save = saves.get(player.getID());
        if (save !=null)
            return save;
        save = new MyPlayerSave(player);
        saves.put(player.getID(), save);
        return save;
    }

    /**
     * Warning, money is not stored here.. but will be restored with restoreAll if set separately
     * @param player ArenaPlayer
     */
    public void storeAll(final MyArenaPlayer player) {
        storeEffects(player);
        storeExperience(player);
        //storeFlight(player);
        storeGamemode(player);
        storeHealth(player);
        //storeHeroClass(player);
        storeHunger(player);
        storeItems(player);
        //storeMagic(player);
        storeMatchItems(player);
        storeScoreboard(player);
    }

    public void restoreAll(final MyArenaPlayer player) {
        restoreEffects(player);
        restoreExperience(player);
       // restoreFlight(player);
        restoreGamemode(player);
        restoreHealth(player);
       // restoreHeroClass(player);
        restoreHunger(player);
        restoreItems(player);
        //restoreMagic(player);
        restoreMoney(player);
        restoreMatchItems(player);
        restoreScoreboard(player);
    }

    private MyPlayerSave getSave(final MyArenaPlayer player) {
        return saves.get(player.getID());
    }
    private static boolean restoreable(MyArenaPlayer p) {
        return (p.isOnline() && !p.isDead());
    }

    public int storeExperience(MyArenaPlayer player) {
        return getOrCreateSave(player).storeExperience();
    }

    public void restoreExperience(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getExp()==null)
            return;
        if (restoreable(p)){
            save.restoreExperience();
        } else {
            MyBAPlayerListener.restoreExpOnReenter(p, save.removeExperience());
        }
    }

    public void storeHealth(MyArenaPlayer player) {
        getOrCreateSave(player).storeHealth();
    }

    public void restoreHealth(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getHealth()==null)
            return;
        if (restoreable(p)){
            save.restoreHealth();
        } else {
            MyBAPlayerListener.restoreHealthOnReenter(p, save.removeHealth());
        }
    }

    public void storeHunger(MyArenaPlayer player) {
        getOrCreateSave(player).storeHunger();
    }

    public void restoreHunger(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getHunger()==null)
            return;
        if (restoreable(p)){
            save.restoreHunger();
        } else {
            MyBAPlayerListener.restoreHungerOnReenter(p, save.removeHunger());
        }
    }

    public void storeEffects(MyArenaPlayer player) {
        getOrCreateSave(player).storeEffects();
    }

    public void restoreEffects(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getEffects()==null)
            return;
        if (restoreable(p)){
            save.restoreEffects();
        } else {
            MyBAPlayerListener.restoreEffectsOnReenter(p, save.removeEffects());
        }
    }

    public void restoreMoney(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getMoney()==null)
            return;
        save.restoreMoney();
    }


//    public void storeMagic(MyArenaPlayer player) {
//        getOrCreateSave(player).storeMagic();
//    }
//
//    public void restoreMagic(MyArenaPlayer p) {
//        MyPlayerSave save = getSave(p);
//        if (save == null || save.getMagic()==null)
//            return;
//        if (restoreable(p)){
//            save.restoreMagic();
//        } else {
//            MyBAPlayerListener.restoreMagicOnReenter(p, save.removeMagic());
//        }
//    }


    public void storeItems(MyArenaPlayer player) {
        getOrCreateSave(player).storeItems();
    }

    /**
     * Warning!!! Unlike most other methods in the StoreController, this one
     * overwrites previous values
     * @param player MyArenaPlayer
     */
    public void storeMatchItems(MyArenaPlayer player) {
        getOrCreateSave(player).storeMatchItems();
    }

    public void clearMatchItems(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getMatchItems()==null)
            return;
        if (restoreable(p)){
            save.removeMatchItems();
        }
    }

    public void restoreItems(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getItems()==null)
            return;
        if (restoreable(p)){
            save.restoreItems();
        } else {
            MyBAPlayerListener.restoreItemsOnReenter(p, save.removeItems());
        }
    }

    public void restoreMatchItems(MyArenaPlayer p){
        MyPlayerSave save = getSave(p);
        if (save == null || save.getMatchItems()==null)
            return;
        if (restoreable(p)){
            save.restoreMatchItems();
        } else {
            MyBAPlayerListener.restoreItemsOnReenter(p, save.removeMatchItems());
        }
    }


    public static void setMatchInventory(MyArenaPlayer p, PInv pinv) {
        if (MyDefaults.DEBUG_STORAGE) Log.info("restoring match items for " + p.getName() +"= "+" o="+p.isOnline() +"  dead="+p.isDead() +" h=" + p.getHealth()+"");
        if (p.isOnline() && !p.isDead()){
            InventoryUtil.addToInventory(p.getPlayer(), pinv);
        } else {
            MyBAPlayerListener.restoreItemsOnReenter(p, pinv);
        }
    }

    public static void setInventory(MyArenaPlayer p, PInv pinv) {
        if (MyDefaults.DEBUG_STORAGE) Log.info("setInventory items to " + p.getName() +"= "+" o="+p.isOnline() +"  dead="+p.isDead() +" h=" + p.getHealth()+"");
        if (p.isOnline() && !p.isDead()){
            InventoryUtil.addToInventory(p.getPlayer(), pinv);
        } else {
            MyBAPlayerListener.restoreItemsOnReenter(p, pinv);
        }
    }

//    public void storeFlight(MyArenaPlayer p) {
//        getOrCreateSave(p).storeFlight();
//    }
//
//    public void restoreFlight(MyArenaPlayer p) {
//        MyPlayerSave save = getSave(p);
//        if (save == null || save.getFlight()==null)
//            return;
//        if (restoreable(p)){
//            save.restoreFlight();
//        }
//    }


//    public void storeGodmode(MyArenaPlayer p) {
//        getOrCreateSave(p).storeGodmode();
//    }
//
//    public void restoreGodmode(MyArenaPlayer p) {
//        MyPlayerSave save = getSave(p);
//        if (save == null || save.getGodmode()==null)
//            return;
//        if (restoreable(p)){
//            save.restoreGodmode();
//        }
//    }


    public void storeGamemode(MyArenaPlayer p) {
        getOrCreateSave(p).storeGamemode();
    }

    public void restoreGamemode(MyArenaPlayer p) {
        MyPlayerSave save = getSave(p);
        if (save == null || save.getGamemode()==null)
            return;
        if (restoreable(p)){
            save.restoreGamemode();
        } else {
            MyBAPlayerListener.restoreGameModeOnEnter(p, save.removeGamemode());
        }
    }

    public void clearInv(MyArenaPlayer player) {
        if (player.isOnline()){
            InventoryUtil.clearInventory(player.getPlayer());
        }else{
            MyBAPlayerListener.clearInventoryOnReenter(player);
        }
    }

    public static void removeItem(MyArenaPlayer p, ItemStack is) {
        if (p.isOnline()){
            InventoryUtil.removeItems(p.getInventory(),is);
        } else {
            MyBAPlayerListener.removeItemOnEnter(p,is);
        }
    }

    public static void removeItems(MyArenaPlayer p, List<ItemStack> items) {
        if (p.isOnline()){
            InventoryUtil.removeItems(p.getInventory(),items);
        } else {
            MyBAPlayerListener.removeItemsOnEnter(p,items);
        }
    }

//    public void addMember(MyArenaPlayer p, WorldGuardRegion region) {
//        WorldGuardController.addMember(p.getName(), region);
//    }
//    public void removeMember(MyArenaPlayer p, WorldGuardRegion region) {
//        WorldGuardController.removeMember(p.getName(), region);
//    }
//
//    public void storeHeroClass(MyArenaPlayer player) {
//        getOrCreateSave(player).storeArenaClass();
//    }
//
//    public void restoreHeroClass(MyArenaPlayer p) {
//        MyPlayerSave save = getSave(p);
//        if (save == null || save.getArenaClass()==null)
//            return;
//        if (restoreable(p)){
//            save.restoreArenaClass();
//        }
//    }

//    public void cancelExpLoss(MyArenaPlayer p, boolean cancel) {
//        if (!HeroesController.enabled())
//            return;
//        HeroesController.cancelExpLoss(p.getPlayer(),cancel);
//    }

    public static MyPlayerStoreController getPlayerStoreController() {
        return INSTANCE;
    }

//    public void deEnchant(Player p) {
//        try{ EffectUtil.deEnchantAll(p);} catch (Exception e){/* do nothing */}
//        HeroesController.deEnchant(p);
//        if (!p.isOnline() || p.isDead()){
//            MyBAPlayerListener.deEnchantOnEnter(BattleArena.toArenaPlayer(p));
//        }
//    }


    public void storeScoreboard(MyArenaPlayer player) {
        getOrCreateSave(player).storeScoreboard();
    }

    public void restoreScoreboard(MyArenaPlayer player) {
        MyPlayerSave save = getSave(player);
        if (save == null || save.getScoreboard()==null)
            return;
        if (restoreable(player)){
            save.restoreScoreboard();
        }
    }

}
