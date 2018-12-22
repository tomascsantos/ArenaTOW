package io.github.TcFoxy.ArenaTOW.Plugin;

import io.github.TcFoxy.ArenaTOW.API.TOWEntityHandler;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.*;
import io.github.TcFoxy.ArenaTOW.Plugin.Serializable.PersistInfo.BaseType;
import mc.alk.arena.BattleArena;
import mc.alk.arena.controllers.MoneyController;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.serializers.Persist;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.*;


public class TugArena extends Arena {


    /*
     * basic variables used throughout the class:
     */
    public Arena arena = this;
    public TugArena tug = this;
    public ArenaTeam redTeam, blueTeam;
    Integer deathtimer;


    /*
     * setup persisted data that is stored via BattleArena's NMS
     * this information is stored when the plugin unloads and
     * is loaded when the plugin loads
     */
//	@Persist 
//	public HashMap<String, Location> deathrooms;
    @Persist
    public HashMap<String, String> savedInfo;


    /*
     * setup hashmaps used throughout the class.
     * most of these values are not actually used
     * in this class, should I move them?
     */
    public HashMap<String, PersistInfo> activeInfo = new HashMap<String, PersistInfo>();
    HashMap<ArenaPlayer, Color> playerTeamLookup = new HashMap<>();


    /*
     * setup refrences to objects used throughout the class
     * Is this the best way to do it?
     */

    //public UpgradeGUI uGUI;
    //ArenaClassExecutor ACexecutor;
    //public ScoreHelper sh;
    TugTimers timers;
    TugListener tuglistener;
    private TOWEntityHandler towEntityHandler;
    public TOWEntityHandler getEntityHandler() {return towEntityHandler;}


    //MinionFactory minionFactory = new MinionFactory(minionFactorySpawners);
    //PlayerEnhancements PlayerE = new PlayerEnhancements(arena);
    //public TeamLevel teamLevel = new TeamLevel();


    /*
     * These are the 4 stages of battle arena that are called every time a game is started:
     *
     * onOpen() = when game is being setup internally -- don't really know whats happening
     * onstart() = when game is actually starting -- players are spawned and fighting each other ect.
     * onFinish() = when the game victory condition is fullfilled, some things are still running...
     * onComplete() = when game has "closed" -- all the internal stuff of turning of the game has been completed
     */
    public TugArena() {
        super();
        /*
         * No Idea what's happening here...
         * Nothing seems to work though
         */

    }


    /*
     * occurs when player joins match?
     */
    @Override
    public void onJoin(ArenaPlayer player, ArenaTeam team) {


    }

    /*
     * Classes should be initialized in this method because
     * it is the first thing that occurs everytime an arena
     * is started.
     */
    @Override
    public void onOpen() {
        //teamLevel.resetTeams();


        /*
         * load the towers from
         * the persist annotation
         * above.
         * THIS HAS TO HAPPEN FIRST TO INITIALIZE ALL PERSISTED STUFF!
         */
        TugExecutor.initSaves(tug);


        // Must create new objects of all classes in onOpen method.
        //minionFactory = new MinionFactory(minionFactorySpawners);
        //PlayerE.resetdefaultStats();
        timers = new TugTimers(tug);
        //PlayerE = new PlayerEnhancements(arena);
        tuglistener = new TugListener(tug);
        //this.sh = new ScoreHelper(tug);


    }


    private void enableListeners() {
        /*
         * All listeners have to be enabled here because
         * if they are enabled in the main class they cant
         * be passed in an instance of this arena.
         */
        //Bukkit.getServer().getPluginManager().registerEvents(PlayerE, ArenaTOW.getSelf());
        //Bukkit.getServer().getPluginManager().registerEvents(uGUI, ArenaTOW.getSelf());
        Bukkit.getServer().getPluginManager().registerEvents(tuglistener, ArenaTOW.getSelf());
    }


    @Override
    public void onStart() {
        /*
         * Dont create new objects in this method!
         */

        /*
         * stop the match if not valid arena
         */
        if (!isValidArena()) {
            this.getMatch().cancelMatch();
            return;
        }


        /*
         * start the bukkit
         * scheduled tasks
         */

        timers.gameTime();
        timers.startEntitySpawn();
        timers.startEntityChecker();

        /**
         * Create the TowEntityHandler for this arena.
         */
        towEntityHandler = ArenaTOW.getEntityHandler();


        /*
         * set the teams so they can be
         * referenced in the future
         */
        List<ArenaTeam> teams = getTeams();
        String teamname = "";
        ArenaTeam team;
        for (int i = 0; i < teams.size(); i++) {
            teamname = Utils.getTeamName(i);
            team = teams.get(i);
            team.setName(teamname);
            for (ArenaPlayer p : team.getPlayers()) {
                playerTeamLookup.put(p, Utils.getTeamColor(teamname));    //Add Players to team lookup.
                this.towEntityHandler.addEntity(new TowPlayer(p, this, this.towEntityHandler));
            }
        }
        /*
         * set the team level
         */

        //teamLevel.setTeamLev(blueTeam);
        //teamLevel.setTeamLev(redTeam);

        /*
         * spawn the towers and the golems.
         *
         */

        for (PersistInfo base : activeInfo.values()) {
            if (base instanceof Tower || base instanceof Nexus) {
                base.spawnMob(this);
            }
        }

        /*
         * set up the arena economy
         */

        //ArenaEcon.onStart(arena);

        /*
         * initialize some classes
         */

        //this initializes the "ArenaClass powers ex: lightning"
        //ACexecutor = new ArenaClassExecutor(this, plugin, match);

        //this initializes the shop
        //uGUI = new UpgradeGUI(match);

        //setup the scoreboard for everyone
        //this.sh.onStart();

        /*
         * turn on the listeners
         */
        enableListeners();


    }

    @Override
    public void onFinish() {
        resetTowers();
        if (timers == null)
            return;
        timers.cancelTimers();
        timers.killminions();
        //Necromancer.instakillnecros(Necromancer.necro);
        //PlayerE.resetdefaultStats();
    }

    public void onComplete() {
        Set<ArenaTeam> winners = this.getMatch().getVictors();
        for (ArenaTeam team : winners) {
            Double money = 500.0;
            for (Player p : team.getBukkitPlayers()) {
                MoneyController.add(p.getName(), money);
                p.sendMessage(ChatColor.DARK_GREEN + "You have been awarded" + ChatColor.YELLOW + " $" + money);
            }
        }
        Set<ArenaTeam> losers = this.getMatch().getLosers();
        for (ArenaTeam team : losers) {
            Double money = 100.0;
            for (Player p : team.getBukkitPlayers()) {
                MoneyController.add(p.getName(), money);
                p.sendMessage(ChatColor.DARK_GREEN + "You have been awarded" + ChatColor.YELLOW + " $" + money);
            }
        }
    }

    /*
     * kill all the towers when game ends
     */

    private void resetTowers() {
        if (activeInfo.isEmpty())
            return;
        for (PersistInfo base : activeInfo.values()) {
            if (base.getMob() != null)
                base.getMob().setHealth(0);
        }
    }


    @ArenaEventHandler
    public void endGame(PlayerQuitEvent e) {
        if (this.getMatch().getAlivePlayers() == null) {
            this.endGame(e);
        }
    }

    @ArenaEventHandler
    public void nohunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @ArenaEventHandler
    public void noPlayerDrop(PlayerDeathEvent event) {
        event.setDroppedExp(0);
        event.getDrops().clear();
    }

    @ArenaEventHandler
    public void waterDamage(PlayerMoveEvent event) {
        if (event.getTo().getBlock() != event.getFrom().getBlock()) {
            Player p = event.getPlayer();
            if (p.getLocation().getBlock().getType() == Material.WATER || p.getLocation().getBlock().getType() == Material.WATER) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 5, 1));
            }
        }

    }

    //	@ArenaEventHandler
    //	public void extradamage(EntityDamageByEntityEvent event){
    //		if(event.getDamager().getClass().getName() == NMSConstants.entityPlayer){
    //			Player p = (Player) event.getDamager();
    //			ArenaTeam team = getTeam((Player) event.getDamager());
    //			Double multiplier = 1.0;
    //			if(PlayerEnhancements.damageMulti.containsKey(p.getUniqueId())){
    //				multiplier = PlayerEnhancements.damageMulti.get(p.getUniqueId());
    //			}
    //			Double damage = event.getDamage()*multiplier;
    //			damage += teamLevel.getTeamLev(team.getDisplayName());
    //			//event.setDamage((Double)damage);
    //		}
    //	}

    //	@ArenaEventHandler
    //	public void coolEffects(PlayerInteractEvent event){
    //		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
    //			if(event.getItem() == null){
    //				return; //no item in hand
    //			}
    //			if(event.getItem().getType() == Material.DIAMOND_AXE ||
    //					event.getItem().getType() == Material.GOLD_AXE ||
    //					event.getItem().getType() == Material.IRON_AXE ||
    //					event.getItem().getType() == Material.STONE_AXE ||
    //					event.getItem().getType() == Material.WOOD_AXE ||
    //					event.getItem().getType() == Material.NETHER_STAR){
    //				ACexecutor.determineEffect(event);
    //			}
    //
    //		}else{
    //			return;
    //		}
    //
    //	}


    public void keepInventory(final Player player) {
        if (player.getInventory().contains(Material.NETHER_STAR)) {
            player.getInventory().remove(Material.NETHER_STAR);
        }

        final ItemStack[] armor = player.getInventory().getArmorContents();
        Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                player.getInventory().setArmorContents(armor);
            }
        });

        final ItemStack[] inventory = player.getInventory().getContents();
        Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable() {
            public void run() {
                player.getInventory().setContents(inventory);
            }
        });
    }

    @ArenaEventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        event.setDeathMessage("");//no deathmessages all over
        /*
         * get the player and reset anything that
         * might be wrong since he died:
         * health, velocity, fireticks...
         */
        final Player p = event.getEntity();
        p.setVelocity(new Vector(0, 0, 0));
        Double maxhealth = p.getMaxHealth();
        p.setMaxHealth(20);
        p.setHealth(20);
        p.setInvulnerable(true);
        /*
         * delayed task for fire ticks because they last a little time.
         */
        Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                p.setFireTicks(0);
            }
        });
        p.setFoodLevel(20);
        p.setMaxHealth(maxhealth);
        p.setHealth(p.getMaxHealth());
        keepInventory(p);

        /*
         * give out money for the death of the player
         */
        final ArenaPlayer ap = BattleArena.toArenaPlayer(p);

        //		if(ap.getTeam()==blueTeam){
        //			for(Player player:redTeam.getBukkitPlayers()){
        //				ArenaEcon.addCash(player, 25);
        //			}
        //		}else{
        //			for(Player player:blueTeam.getBukkitPlayers()){
        //				ArenaEcon.addCash(player, 25);
        //			}
        //		}

        /*
         * teleport to the correct deathroom
         */

        Deathroom dr = PersistInfo.getDeathroom(ap.getTeam().getDisplayName(), activeInfo);
        p.teleport(dr.getSpawnLoc());

        /*
         * calculate the respawn time
         */

        Long longrespawntime = (long) (5 + (timers.gameTime / 21.8));
        if (longrespawntime >= 60) {
            longrespawntime = 60L;
        }

        final Integer respawntime = longrespawntime.intValue();

        /*
         * display countdown to players
         */

        deathtimer = Bukkit.getScheduler().scheduleSyncRepeatingTask(ArenaTOW.getSelf(), new Runnable() {
            Integer time = 0;

            @Override
            public void run() {

                //Utils.sendTitle(p, 0, 21, 0, "&5You Have FALLEN!", (respawntime-time) + " seconds left!");
                //TODO send message to player
                time++;
            }
        }, 0 * Utils.TPS, 1 * Utils.TPS);

        /*
         * at the end of the countdown, teleport to the home spawn.
         */

        Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
            @Override
            public void run() {
                p.setVelocity(new Vector(0, 0, 0));
                p.setHealth(p.getMaxHealth());
                p.setFireTicks(0);
                p.setFoodLevel(20);
                if (ap.getTeam() == null) return;//in case the game ends it wont throw a NPException
                SpawnLocation spawnloc = tug.getSpawn(ap.getTeam().getIndex(), false);
                p.teleport(spawnloc.getLocation());
                //p.getInventory().setItem(8, new ItemStack(Material.NETHER_STAR, 1));
                Bukkit.getScheduler().cancelTask(deathtimer);
                p.setInvulnerable(false);
            }

        }, respawntime * Utils.TPS);
        p.sendMessage(ChatColor.DARK_RED + "You will respawn in: " + ChatColor.DARK_GREEN +
                respawntime + ChatColor.DARK_RED + " seconds! Get Ready for Revenge!");

    }



    /*
     *

     *
     *
     * Everything after this point is for arena Creation ONLY!
     *
     * Battle arena seems to link the creation of arenas really tightly
     * with this class, Im not sure how to sepperete them to make this
     * a cleaner class...
     *
     *
     *
     *
     *
     */

    private boolean saveBase(String key, Location loc, String info, Player sender, Color color, TugArena arena) {
        if (activeInfo.containsKey(key)) {
            sender.sendMessage(ChatColor.DARK_RED + "You have already created " +
                    PersistInfo.getSimpleKey(key) + ChatColor.DARK_RED + ", its position is being updated");
        } else {
            sender.sendMessage(PersistInfo.getSimpleKey(key) + " added!");
        }

        activeInfo.put(key, new PersistInfo(key, color, loc, info));
        savedInfo = PersistInfo.saveObject(activeInfo);
        BattleArena.saveArenas();
        return true;
    }

    public boolean addNexus(Location loc, Player sender, Color color) {
        String key = "ba_" + this.name + "_" + BaseType.NEXUS.toString() + "_" + String.valueOf(color.asRGB());
        String info = "null";
        return saveBase(key, loc, info, sender, color, this);
    }

    public boolean addDeathroom(Location loc, Player sender, Color color) {
        String key = "ba_" + this.name + "_" + BaseType.DEATHROOM.toString() + "_" + String.valueOf(color.asRGB());
        String info = "null";
        return saveBase(key, loc, info, sender, color, this);
    }

    public boolean addTower(Location loc, Player sender, Color color, Integer i) {
        String key = "ba_" + this.name + "_" + BaseType.TOWER.toString() + "_" + String.valueOf(color.asRGB()) + "_" + i;
        String info = "null";
        return saveBase(key, loc, info, sender, color, this);
    }

    public boolean addSpawner(Location location, Player sender, Color color, Integer i) {
        String key = "ba_" + this.name + "_" + BaseType.SPAWNER.toString() + "_" + String.valueOf(color.asRGB()) + "_" + i;
        String info = "nopaths";
        sender.sendMessage("Don't forget to create pathpoints with /tow add pp");
        return saveBase(key, location, info, sender, color, this);
    }

    public boolean addPathPoints(Location loc, Player sender, Color col, Integer i) {
        String key = "ba_" + this.name + "_" + BaseType.SPAWNER.toString() + "_" + String.valueOf(col.asRGB()) + "_" + i;
        if (!activeInfo.containsKey(key)) {
            sender.sendMessage("There is no spawner with corrosponding data! Make a spawner whos key matches: " + PersistInfo.getSimpleKey(key));
            return true;
        }
        Spawner spawnr = (Spawner) activeInfo.get(key);
        if (spawnr.containsPps(loc)) {
            sender.sendMessage("There was already a pp from this spawner in this location. No duplicate was created");
            return true;
        }
        spawnr.addPp(loc, sender);
        savedInfo = PersistInfo.saveObject(activeInfo);
        BattleArena.saveArenas();
        return true;
    }


    public boolean removePersistInfo(String key, Player sender) {
        if (!activeInfo.containsKey(key)) {
            sender.sendMessage("There is no spawner with corrosponding data! Make a spawner whos key matches: "
                    + PersistInfo.getSimpleKey(key));
            return true;
        } else {
            activeInfo.remove(key);
            savedInfo = PersistInfo.saveObject(activeInfo);
            BattleArena.saveArenas();
            sender.sendMessage(PersistInfo.getSimpleKey(key) + " has been removed");
            return true;
        }
    }

    public boolean removeTower(Player sender, Location loc, Color col, Integer i) {
        String key = "ba_" + this.name + "_" + BaseType.TOWER.toString() + "_" + String.valueOf(col.asRGB()) + "_" + i;
        return removePersistInfo(key, sender);
    }

    public boolean removeSpawner(Player sender, Location loc, Color col, Integer i) {
        String key = "ba_" + this.name + "_" + BaseType.SPAWNER.toString() + "_" + String.valueOf(col.asRGB()) + "_" + i;
        return removePersistInfo(key, sender);
    }

    public boolean removeNexus(Player sender, Location loc, Color col) {
        String key = "ba_" + this.name + "_" + BaseType.TOWER.toString() + "_" + String.valueOf(col.asRGB());
        return removePersistInfo(key, sender);
    }

    public boolean removeDeathroom(Player sender, Location loc, Color col) {
        String key = "ba_" + this.name + "_" + BaseType.DEATHROOM.toString() + "_" + String.valueOf(col.asRGB());
        return removePersistInfo(key, sender);
    }

    public void clearInfo(Player sender) {
        activeInfo.clear();
        savedInfo.clear();
        sender.sendMessage("Everything but player spawns has been cleared");
        BattleArena.saveArenas();
    }

    public boolean clearInfo(BaseType type, Player sender) {
        ArrayList<String> keys = PersistInfo.getTypeObject(type, activeInfo);
        for (int i = 0; i < keys.size(); i++) {
            activeInfo.remove(keys.get(i));
        }
        savedInfo = PersistInfo.saveObject(activeInfo);
        sender.sendMessage("All <" + type + "> type have been cleared");
        BattleArena.saveArenas();
        return true;
    }


    public HashMap<String, aValid> preVerify() {
        /*
         * Create Valid class for each type
         */
        HashMap<String, aValid> ObjectsReady = new HashMap<String, aValid>();

        aValid towers = new aValid("Tower");
        aValid nexuses = new aValid("Nexus");
        aValid drooms = new aValid("Deathroom");
        aValid waitrooms = new aValid("Waitroom");
        aValid spawners = new aValid("Spawner");
        aValid pps = new aValid("PathPoint");

        /*
         * Nexi/Towers/Spawners/Pathpoints Check
         * and add checked valud to list
         */

        for (PersistInfo base : activeInfo.values()) {
            if (base instanceof Nexus) {
                nexuses.setColor(base.getTeamColor(), true);
            } else if (base instanceof Tower) {
                towers.setColor(base.getTeamColor(), true);
            } else if (base instanceof Deathroom) {
                drooms.setColor(base.getTeamColor(), true);
            } else if (base instanceof Spawner) {
                spawners.setColor(base.getTeamColor(), true);
                if (((Spawner) base).hasPps()) {
                    pps.setColor(base.getTeamColor(), true);
                }
            }
        }
        if (this.getWaitroom() != null) {
            if (this.getWaitRoomSpawnLoc(1) != null) {
                waitrooms.setColor(Color.RED, true);
            }
            if (this.getWaitRoomSpawnLoc(2) != null) {
                waitrooms.setColor(Color.BLUE, true);
            }
        }

        ObjectsReady.put(towers.deets, towers);
        ObjectsReady.put(nexuses.deets, nexuses);
        ObjectsReady.put(drooms.deets, drooms);
        ObjectsReady.put(waitrooms.deets, waitrooms);
        ObjectsReady.put(spawners.deets, spawners);
        ObjectsReady.put(pps.deets, pps);

        return ObjectsReady;
    }

    public boolean isValidArena() {
        HashMap<String, aValid> ObjectsReady = preVerify();

        /*
         * first loop through just for the message
         */
        for (aValid valid : ObjectsReady.values()) {
            if (!valid.blue) {
                Bukkit.broadcastMessage(valid.getDeets(Color.BLUE) + componentReady(valid.blue));
            }
            if (!valid.red) {
                Bukkit.broadcastMessage(valid.getDeets(Color.RED) + componentReady(valid.red));
            }
        }

        /*
         * then loop through for the final result
         */
        for (aValid valid : ObjectsReady.values()) {
            if (!valid.blue || !valid.red) {
                Bukkit.broadcastMessage(ChatColor.DARK_RED + "The arena is not complete. Add the missing components to start successfully. \n Use /tow check [arena name]");
                return false;
            }
        }
        return true;
    }

    public void verify(Player sender) {
        HashMap<String, aValid> ObjectsReady = preVerify();

        for (aValid valid : ObjectsReady.values()) {
            sender.sendMessage(valid.getDeets(Color.RED) + componentReady(valid.red));
            sender.sendMessage(valid.getDeets(Color.BLUE) + componentReady(valid.blue));
        }
    }

    private String componentReady(boolean bool) {
        if (bool) {
            return ChatColor.DARK_GREEN + "Ready";
        } else {
            return ChatColor.DARK_RED + "Missing";
        }
    }

    private static class aValid {

        boolean red, blue;
        String deets;

        public aValid(String deets) {
            this.red = false;
            this.blue = false;
            this.deets = deets;
        }

        public void setColor(Color col, boolean bool) {
            if (col.equals(Color.BLUE)) {
                this.blue = bool;
            } else if (col.equals(Color.RED)) {
                this.red = bool;
            } else {
                Bukkit.broadcastMessage("invalid color class aType");
            }
        }

        private String colorToString(Color col) {
            if (col.equals(Color.BLUE)) {
                return ChatColor.BLUE + "Blue";
            } else if (col.equals(Color.RED)) {
                return ChatColor.RED + "Red";
            } else {
                return "Invalid color";
            }
        }

        public String getDeets(Color col) {
            return ChatColor.GOLD + this.deets + " " + colorToString(col) + " : ";
        }

//		public boolean getColor(Color col){
//			if(col == Color.BLUE){
//				return blue;
//			}else if(col == Color.RED){
//				return this.red;
//			}else{
//				Bukkit.broadcastMessage("invalid color class aType getColor()");
//				return false;
//			}
//		}

    }


}
