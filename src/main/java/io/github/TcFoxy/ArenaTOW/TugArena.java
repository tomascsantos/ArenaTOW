package io.github.TcFoxy.ArenaTOW;

import io.github.TcFoxy.ArenaTOW.Listeners.TugListener;
import io.github.TcFoxy.ArenaTOW.Serializable.MinionFactory;
import io.github.TcFoxy.ArenaTOW.Serializable.SerializableBase;
import io.github.TcFoxy.ArenaTOW.Serializable.SerializableBase.BaseType;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import mc.alk.arena.BattleArena;
import mc.alk.arena.controllers.MoneyController;
import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.arenas.Arena;
import mc.alk.arena.objects.events.ArenaEventHandler;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.objects.teams.ArenaTeam;
import mc.alk.arena.serializers.Persist;
import net.minecraft.server.v1_10_R1.EntityLiving;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class TugArena extends Arena {



	/*
	 * basic variables used throughout the class:
	 */
	static ArenaTOW plugin;
	public Arena arena = this;
	public TugArena tug = this;
	public ArenaTeam redTeam, blueTeam;
	Integer deathtimer;



	/*
	 * setup persisted data that is stored via BattleArena's API
	 * this information is stored when the plugin unloads and
	 * is loaded when the plugin loads
	 */
	@Persist 
	public HashMap<String, Location> deathrooms;
	@Persist 
	HashMap<String, String> minionFactorySpawners;
	@Persist 
	public HashMap<String, String> serializedSave;


	/*
	 * setup hashmaps used throughout the class.
	 * most of these values are not actually used
	 * in this class, should I move them?
	 */
	HashMap<String, Integer> tasks = new HashMap<String, Integer>();
	HashMap<Integer, Location> pathpointsSave = new HashMap<Integer, Location>();
	
	//public HashMap<String, Tower> towerteams = new HashMap<String, Tower>();
	public HashMap<String, SerializableBase> activeBases = new HashMap<String, SerializableBase>();


	/*
	 * setup refrences to objects used throughout the class
	 * Is this the best way to do it?
	 */

	//public UpgradeGUI uGUI;
	//ArenaClassExecutor ACexecutor;
	public ScoreHelper sh;
	TugTimers timers;
	TugListener tuglistener;

	MinionFactory minionFactory = new MinionFactory(minionFactorySpawners);
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
		 */
		
//		if(serializedSave == null){
//			serializedSave = new HashMap<String, String>();
//		}
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
		 * cancel the game if there are no deathrooms.
		 */

		if(deathrooms == null){
			this.getMatch().cancelMatch();
			Bukkit.broadcastMessage("You must add deathrooms to start this arena!");
		}

		// Must create new objects of all classes in onOpen method.
		minionFactory = new MinionFactory(minionFactorySpawners);
		//PlayerE.resetdefaultStats();
		timers = new TugTimers(this);
		//PlayerE = new PlayerEnhancements(arena);
		tuglistener = new TugListener(tug, serializedSave);
		this.sh = new ScoreHelper(tug);


	}

	private void enableListeners() {
		/*
		 * All listeners have to be enabled here because
		 * if they are enabled in the main class they cant 
		 * be passed in an instance of this arena.
		 */
		//Bukkit.getServer().getPluginManager().registerEvents(PlayerE, plugin);
		//Bukkit.getServer().getPluginManager().registerEvents(uGUI, plugin);
		Bukkit.getServer().getPluginManager().registerEvents(tuglistener, plugin);
	}


	@Override
	public void onStart() {
		/*
		 * Dont create new objects in this method!
		 */

		/*
		 * load the towers from
		 * the persist annotation 
		 * above.
		 */

		//towerteams = Tower.loadTowers(towerSave);
		activeBases = SerializableBase.getObject(serializedSave);

		/*
		 * start the bukkit
		 * scheduled tasks
		 */

		timers.gameTime();
		timers.startEntitySpawn();
		timers.startEntityChecker();

		/*
		 * set the teams so they can be 
		 * referenced in the future
		 */

		List<ArenaTeam> t = getTeams();
		for (int i = 0; i < t.size(); i++) {
			if (i == 0) {
				redTeam = t.get(i);
				redTeam.setName("Red");
			} else if (i == 1) {
				blueTeam = t.get(i);
				blueTeam.setName("Blue");
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
		 * if its a blue team golem
		 * --> spawn Entity Blue Golem
		 * 
		 * if its a red team Golem
		 * --> soawn an entity Red Golem, ect.
		 */
		
		for(SerializableBase base : activeBases.values()){
			base.spawnMob();
		}

//		for(Tower tow: towerteams.values()){
//			if(tow.getType() == TowerType.TOWER){
//				MyEntityGolem golem = NMSUtils.spawnTeamGolem(tow.getWorld(), tow.getLoc().getX(), tow.getLoc().getY(), tow.getLoc().getZ(), tow.getTeamColor());
//				tow.setMob(golem);
//			}else if(tow.getType() == TowerType.NEXUS){
//				MyEntityGuardian guardian = NMSUtils.spawnTeamGuardian(tow.getWorld(), tow.getLoc().getX(), tow.getLoc().getY(), tow.getLoc().getZ(), tow.getTeamColor());
//				tow.setMob(guardian);
//			}
//		}

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
		this.sh.onStart();

		/*
		 * turn on the listeners
		 */
		enableListeners();


	}

	@Override
	public void onFinish(){
		resetTowers();
		timers.cancelTimers();
		timers.killminions();
		//Necromancer.instakillnecros(Necromancer.necro);
		//PlayerE.resetdefaultStats();
	}

	public void onComplete(){
		Set<ArenaTeam> winners = this.getMatch().getVictors();
		for(ArenaTeam team : winners){
			Double money = 500.0;
			for(Player p : team.getBukkitPlayers()){
				for(int i=0;i<20;i++){
					String perm = String.format("arenatow.rank.%s", i);
					if(p.hasPermission(perm)){
						money*=1.5;
					}
				}
				MoneyController.add(p.getName(), money);
				p.sendMessage(ChatColor.DARK_GREEN + "You have been awarded" + ChatColor.YELLOW +" $" + money);
			}	
		}
		Set<ArenaTeam> losers = this.getMatch().getLosers();
		for(ArenaTeam team : losers){
			Double money = 100.0;
			for(Player p : team.getBukkitPlayers()){
				for(int i=0;i<20;i++){
					String perm = String.format("arenatow.rank.%s", i);
					if(p.hasPermission(perm)){
						money*=1.5;
					}
				}
				MoneyController.add(p.getName(), money);
				p.sendMessage(ChatColor.DARK_GREEN + "You have been awarded" + ChatColor.YELLOW +" $" + money);
			}	
		}
	}





	private void resetTowers() {
		for(SerializableBase base: activeBases.values()){
			((EntityLiving) base.getMob()).setHealth(0);
		}
		
//		for(Tower tow: towerteams.values()){
//			tow.getMob().setHealth(0);
//		}
	}



	@ArenaEventHandler
	public void endGame(PlayerQuitEvent e){
		if(this.getMatch().getAlivePlayers() == null){
			this.endGame(e);
		}
	}

	@ArenaEventHandler
	public void nohunger(FoodLevelChangeEvent e){
		e.setCancelled(true);
	}

	@ArenaEventHandler
	public void noPlayerDrop(PlayerDeathEvent event){
		event.setDroppedExp(0);
		event.getDrops().clear();
	}

	@ArenaEventHandler
	public void waterDamage(PlayerMoveEvent event){
		if(event.getTo().getBlock() != event.getFrom().getBlock()){
			Player p = event.getPlayer();
			if(p.getLocation().getBlock().getType() == Material.STATIONARY_WATER || p.getLocation().getBlock().getType() == Material.WATER ){
				p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20*5, 1));
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



	public void keepInventory(final Player player){
		if(player.getInventory().contains(Material.NETHER_STAR)){
			player.getInventory().remove(Material.NETHER_STAR);
		}

		final ItemStack[] armor = player.getInventory().getArmorContents();
		Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run(){
				player.getInventory().setArmorContents(armor);
			}
		});

		final ItemStack[] inventory = player.getInventory().getContents();
		Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable()
		{
			public void run()
			{
				player.getInventory().setContents(inventory);
			}
		});
	}

	@ArenaEventHandler
	public void playerDeathEvent(PlayerDeathEvent event){

		/*
		 * get the player and reset anything that
		 * might be wrong since he died:
		 * health, velocity, fireticks...
		 */
		final Player p = event.getEntity();
		p.setVelocity(new Vector(0,0,0));
		Double maxhealth = p.getMaxHealth();
		p.setMaxHealth(20);
		p.setHealth(20);
		/*
		 * delayed task for fire ticks because they last a little time.
		 */
		Bukkit.getScheduler().scheduleSyncDelayedTask(ArenaTOW.getSelf(), new Runnable(){
			@Override
			public void run(){
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
		final ArenaPlayer  ap = BattleArena.toArenaPlayer(p);

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

		String team = ap.getTeam().getDisplayName();
		p.teleport(deathrooms.get(team));

		/*
		 * calculate the respawn time
		 */

		Long longrespawntime = (long) (5+(timers.gameTime/21.8));
		if(longrespawntime >= 60){
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

				Utils.sendTitle(p, 8, 20, 8, "&5You Have FALLEN!", (respawntime-time) + " seconds left!");
				time++;
			}		
		}, 0*Utils.TPS, 1*Utils.TPS);

		/*
		 * at the end of the countdown, teleport to the home spawn. 
		 */

		Bukkit.getScheduler().runTaskLater(ArenaTOW.getSelf(), new Runnable() {
			@Override
			public void run() {
				p.setVelocity(new Vector(0,0,0));
				p.setHealth(p.getMaxHealth());
				p.setFireTicks(0);
				p.setFoodLevel(20);
				if(ap.getTeam() == null)return;//in case the game ends it wont throw a NPException
				SpawnLocation spawnloc = tug.getSpawn(ap.getTeam().getIndex(), false);
				p.teleport(spawnloc.getLocation());
				p.getInventory().setItem(8, new ItemStack(Material.NETHER_STAR, 1));
				Bukkit.getScheduler().cancelTask(deathtimer);
			}

		}, respawntime*Utils.TPS);
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

	public void addDeathroom(Player sender, Location location, String string) {
		if(deathrooms == null){
			deathrooms = new HashMap<String, Location>();
		}
		deathrooms.put(string, location);
		sender.sendMessage("deathroom created successfully");
	}
	
	private void saveBase(String key, Location loc, Player sender, Color color, TugArena arena){
		if(serializedSave != null){
			activeBases = SerializableBase.getObject(serializedSave);
			Bukkit.broadcastMessage("get object called");
		}
		if(activeBases.containsKey(key)){
			sender.sendMessage(ChatColor.DARK_RED+"You have already created "+
					ChatColor.YELLOW+key+ChatColor.DARK_RED+", its position is being updated");
		}else{
			sender.sendMessage(key + " added!");
		}

		SerializableBase base = new SerializableBase(key, color, loc);
		if(base == null){
			Bukkit.broadcastMessage("base is null before HashMap");
		}
		
		activeBases.put(key, base);
		
		if(activeBases.get(key) == null){
			Bukkit.broadcastMessage("value from HashMap is null");
		}
		
		serializedSave = SerializableBase.saveObject(activeBases);
	}
	
	public void addNexus(Location loc, Player sender, Color color, TugArena arena){
		//important key order, [2] must be BaseType
		String key = "ba_" + arena.name + "_" + BaseType.NEXUS + "_" + String.valueOf(color.asRGB());
		
		saveBase(key, loc, sender, color, arena);
	}
	
	public void addTower(Integer i, Location loc, Player sender, Color color, TugArena arena) {
		//important key order, [2] must be BaseType
		String key = "ba_" + arena.name + "_" + BaseType.TOWER + "_" + String.valueOf(color.asRGB()) + "_" + i;
		saveBase(key, loc, sender, color, arena);
		
		
//		if(towerSave != null){
//			towerteams = Tower.loadTowers(towerSave);
//		}
//		if(towerteams.containsKey(key)){
//			sender.sendMessage(ChatColor.DARK_RED+"You have already created "+
//					ChatColor.YELLOW+key+ChatColor.DARK_RED+", its position is being updated");
//		}
//		else{
//			sender.sendMessage(key + " added!");
//		}
//
//		Tower t = new Tower(key, sender.getWorld(), team, loc, type);
//		towerteams.put(key, t);	
//		towerSave = t.createSaveableTowers(towerteams);		
	}

	public void addSpawner(Player sender, Location location, String color, TugArena arena, Integer index) {
		String name = color + "lane" + index;
		if(minionFactorySpawners != null){
			minionFactory.loadPersistence(minionFactorySpawners);
		}
		minionFactorySpawners = minionFactory.generatePersistence();
		if(minionFactorySpawners.containsKey(name)){
			sender.sendMessage(ChatColor.DARK_RED + "This spawner has already been created, it's location is being updated");
		}
		minionFactory.addMinionSpawner(name, color, location);
		minionFactorySpawners = minionFactory.generatePersistence();
		sender.sendMessage(ChatColor.DARK_GREEN + color +" Team Spawner "+ ChatColor.YELLOW + "#" +index + 
				ChatColor.DARK_GREEN + " added! \n " + ChatColor.DARK_RED + "You must now add the pathfinding points!");
	}

	public void addPathPoints(Player sender, Location location, String color, Integer index){
		String name = color + "lane" + index;
		if(minionFactorySpawners != null){
			minionFactory.loadPersistence(minionFactorySpawners);
		}
		minionFactorySpawners = minionFactory.generatePersistence();
		String blab = minionFactory.addMinionPathPoint(name, location);
		if(blab == ""){
			sender.sendMessage(ChatColor.DARK_RED + "There is no spawner with corrosponding data! Create a tower with Color: "
					+ChatColor.DARK_GREEN +color+ ChatColor.DARK_RED +"and idNumber: " + ChatColor.DARK_GREEN +index+ ChatColor.DARK_RED + 
					" before you create this pathpoint");
			return;
		}
		if(blab.equals("same location")){
			sender.sendMessage(ChatColor.DARK_RED+"WARNING: You have already create a PathPoint in this location!");
		}
		minionFactorySpawners = minionFactory.generatePersistence();
		sender.sendMessage(ChatColor.DARK_GREEN + "Pathpoint " + ChatColor.AQUA + "#" + blab+ ChatColor.DARK_GREEN 
				+ " for Spawner " + ChatColor.YELLOW + "#" +index+ ChatColor.DARK_GREEN + " of color" + color + ".");
	}

	public Boolean removeTower(String baseKey) {
		if (! activeBases.containsKey(baseKey))
			return false;
		activeBases = SerializableBase.getObject(serializedSave);
		activeBases.remove(baseKey);
		serializedSave = SerializableBase.saveObject(activeBases);
		return true;
		
		/*
		 * 
		 * 
		 */
//		if (! towerteams.containsKey(towername))
//			return false;
//		towerteams = Tower.loadTowers(towerSave);
//		Tower t = towerteams.get(towername);
//		towerteams.remove(towername);
//		towerSave = t.createSaveableTowers(towerteams);
//		return true;
	}

	public Boolean removeSpawner(String spawnername) {
		minionFactory.removeMinionSpawner(spawnername);
		return true;
	}

	public void clearTowers() {
		
		activeBases = SerializableBase.getObject(serializedSave);
		activeBases.clear();
		serializedSave.clear();
		
		//Tower tower = new Tower();
//		towerteams = Tower.loadTowers(towerSave);
//		for (Tower t : towerteams.values()) {
//			org.bukkit.World world = t.getWorld();
//			if(world == null){
//				Bukkit.broadcastMessage(ChatColor.DARK_RED+"That world is = Null");
//			}
//		}
//		towerteams.clear();
//		towerSave.clear();


	}

	public void clearSpawners() {
		minionFactory.clearSpawners();
	}

//	public String getTowerList() {
//		String buf = "";
//		towerteams = Tower.loadTowers(towerSave);
//		for (Entry <String, Tower> e: towerteams.entrySet()) {
//			buf += ChatColor.DARK_PURPLE + ""+ChatColor.BOLD +"Tower Name: " +ChatColor.YELLOW + e.getKey() +"\n"  
//					+ChatColor.DARK_GREEN+"Tower Region Name: "+ChatColor.YELLOW+e.getValue().getKey() +"\n"
//					+ChatColor.DARK_GREEN+"Tower Team: "+ChatColor.YELLOW+e.getValue().getTeamColor()+"\n"
//					+ChatColor.DARK_GREEN+"Tower World: "+ChatColor.YELLOW+e.getValue().getWorld()+"\n"
//					+ChatColor.DARK_GREEN + "Tower Location: " + ChatColor.YELLOW+ "X-cord: " +e.getValue().getLoc().getBlockX() + ", " +
//					"Y-cord: " +e.getValue().getLoc().getBlockY() + ", " +
//					"Z-cord: " +e.getValue().getLoc().getBlockZ() +"\n\n";
//		}
//		if (buf == ""){
//			return ChatColor.DARK_PURPLE+""+ ChatColor.BOLD+ "You have not created any Towers";
//		}else{
//			return buf;
//		}
//	}


//	public static void setMain(ArenaTOW tow){
//		plugin = tow;
//	}








}
