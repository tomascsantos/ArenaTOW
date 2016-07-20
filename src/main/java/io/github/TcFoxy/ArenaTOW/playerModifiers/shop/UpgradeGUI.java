package io.github.TcFoxy.ArenaTOW.playerModifiers.shop;

import io.github.TcFoxy.ArenaTOW.ScoreHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mc.alk.arena.competition.match.Match;
import mc.alk.arena.objects.ArenaPlayer;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class UpgradeGUI implements Listener{

	HashMap <Material, shopItem> myShopItems;
	HashMap<Material, playerStats> playerStatTypes;
	HashMap <Player, HashMap<Material, playerStats>> myPlayerStats;
	shopItem shopObject;
	ArenaEcon arenaecon;
	Match match;

	public UpgradeGUI(Match match) {
		this.match = match;
		myShopItems = new HashMap <Material, shopItem>();
		// Load from file
		setUpItems();
		shopObject = new shopItem();
		
		playerStatTypes = new HashMap<Material, playerStats>();
		myPlayerStats = new HashMap<Player, HashMap<Material, playerStats>>();
		setupStats();
	}
	

	@EventHandler
	public void guiClick(InventoryClickEvent e){
		if(e.getClickedInventory() == null){//if they click outside the GUI
			return;
		}
		if(e.getClickedInventory().getTitle() == null){//if it has no name for some reason;
			return;
		}
		if(e.getClickedInventory().getName().equals("container.inventory")){
			//e.setCancelled(true);
		}
		if(e.getClickedInventory().getTitle().equals(ChatColor.DARK_GREEN + "Upgrade Menu")){
			e.setCancelled(true);
			tryToBuy(e.getCurrentItem(), (Player) e.getWhoClicked(), e.getClickedInventory());
		}
	}


	private void tryToBuy(ItemStack currentItem, Player p, Inventory inv) {
		//is the item air or glass?
		if(currentItem.getType() == Material.AIR){
			return;
		}
		if(currentItem.getType() == Material.GLASS){
			return;
		}
		
		//is the player trying to buy something? if he has it in his inventory ==> he should not buy it
		
		ItemStack[] armor = p.getInventory().getArmorContents();
		for(ItemStack buff : armor){
			if(buff.getType() == currentItem.getType()){
				p.sendMessage(ChatColor.GOLD + "You already have the item: " + ChatColor.DARK_GREEN +currentItem.getType());
				return;
			}
		}
		
		/*
		 * check if we want the player to be able to buy the item
		 * even though it IS in his inventory:
		 * ex: its an arrow or it's one of the stat-upgrades
		 */
		if(!playerStatTypes.containsKey(currentItem.getType()) ||
				currentItem.getType() != Material.ARROW){//not one of the statType materials, and not an arrow
			if(p.getInventory().contains(currentItem.getType())){//the inventory DOES contain this item.
				
				Boolean stop = false;
				for(ItemStack invItem : p.getInventory().getContents()){
					if(invItem == null){
						continue;
					}
					if(invItem.getType() == Material.BOW){
						if(getBowShopItem(invItem) == getBowShopItem(currentItem)){
							stop = true;
						}
					}
				}
				if(stop == true){
					p.sendMessage(ChatColor.GOLD + "You already have the item: " + ChatColor.DARK_GREEN +currentItem.getType());
					return;
				}
				
			}
		}
		
		
		/*
		 * what is the player trying to buy???
		 *                \/
		 * turn the current item into a recognizable
		 * shopItem or playerstat.
		 */
		
		Integer price = 0;
		shopItem shopitem = null;
		playerStats playerstats = null;
		
		if(myShopItems.containsKey(currentItem.getType())){
			
			//if its a bow you have to check the enchant level to get the right shopItem.
			if(currentItem.getType() == Material.BOW){
				shopitem = getBowShopItem(currentItem);
			}else{
				shopitem = myShopItems.get(currentItem.getType());
			}

			price = shopitem.cost;
			
		}else if(playerStatTypes.containsKey(currentItem.getType())){
			HashMap<Material, playerStats> currentStats =  myPlayerStats.get(p);
			playerstats = currentStats.get(currentItem.getType());
			price = playerstats.price;
		}else{
			return;
		}
		
		/*
		 * if its the max teir you cant buy it again.
		 */
		
		if(shopitem != null && shopitem.isHighestTier()){
			p.sendMessage(ChatColor.DARK_RED + "You have the highest tier already!");
			return;
		}
		
		/*
		 * calculate player money, return if not enough.
		 * && if its the highest tier
		 */
		
		Integer wallet = ArenaEcon.getCash(p);
		if (wallet < price){
			p.sendMessage(ChatColor.DARK_RED + "You do not have enough Money to buy that item!");
			return;
		}
		ArenaEcon.subtractCash(p, price);
		
		/*
		 * we are branching off here...
		 * either its a statType or a shopItem
		 */
		ScoreHelper.setupScoreboard(p);
		if(shopitem != null){
			buyShopItem(shopitem, p, inv);
		}else if(playerstats != null){
			buyPlayerStats(playerstats, p, inv);
		}
		
		
		
		
	}

	private void buyPlayerStats(playerStats playerstats, Player p, Inventory inv) {
		
		/*
		 * get the Type of enchantment depending on the playerstats Type
		 */
		
		if(playerstats.statType == "Strength"){
			PlayerEnhancements.enhancePlayer(1, p);
		}else if(playerstats.statType == "Speed"){
			PlayerEnhancements.enhancePlayer(3, p);
		}else if(playerstats.statType == "Health"){
			PlayerEnhancements.enhancePlayer(2, p);
		}else if(playerstats.statType == "Jump"){
			PlayerEnhancements.enhancePlayer(4, p);
		}
		
		/*
		 * update the playerstats tier
		 */
		
		playerstats.upgradeTeir();
		
		p.sendMessage(ChatColor.DARK_GREEN + "You have successfully updated your " 
		+ ChatColor.GOLD +playerstats.statType + 
		ChatColor.DARK_GREEN + "!");
		
		buildChestItems(inv, p);
	}


	private void buyShopItem(shopItem shopitem, Player p, Inventory inv) {
		
		ItemStack olditem = shopitem.getPreviousTier();
		p.getInventory().remove(olditem);
		
		/*
		 * depending on what inventory slot the item was, replace it in the same slot.
		 */
		ItemStack newarmor;
		if(shopitem.invSlot == 100){
			newarmor = new ItemStack(shopitem.getItemStack().getType());
			newarmor = makeInvincible(newarmor);
			p.getInventory().setBoots(newarmor);
			
		}else if(shopitem.invSlot == 101){
			newarmor = new ItemStack(shopitem.getItemStack().getType());
			newarmor = makeInvincible(newarmor);
			p.getInventory().setLeggings(newarmor);
			
		}else if(shopitem.invSlot == 102){
			newarmor = new ItemStack(shopitem.getItemStack().getType());
			newarmor = makeInvincible(newarmor);
			p.getInventory().setChestplate(newarmor);

		}else if(shopitem.invSlot == 103){
			newarmor = new ItemStack(shopitem.getItemStack().getType());
			newarmor = makeInvincible(newarmor);
			p.getInventory().setHelmet(newarmor);

		}else if(shopitem.invSlot == 2){//if its a bow, give it the enchants
			newarmor = shopitem.getItemStack();
			newarmor = makeInvincible(newarmor);
			p.getInventory().setItem(2, newarmor);

		}else if(shopitem.invSlot == 3){
			newarmor = new ItemStack(Material.ARROW, 20);
			p.getInventory().addItem(newarmor);
		}else{
			newarmor = new ItemStack(shopitem.getItemStack().getType());
			newarmor = makeInvincible(newarmor);
			p.getInventory().setItem(shopitem.invSlot, newarmor);
		}
		p.sendMessage(ChatColor.DARK_GREEN + "You have succesfully purchased the item: " + shopitem.getItemStack().getType().toString());
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 1, 1);

		//reload the shop inventory
		
			//delete old invetory?
		buildChestItems(inv, p);
		
	}


	public void openGUI(PlayerInteractEvent event) {
		Inventory chest = Bukkit.createInventory(event.getPlayer(), 54, ChatColor.DARK_GREEN + "Upgrade Menu");
		Player p = event.getPlayer();
		p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
		buildChestItems(chest, event.getPlayer());

	}


	private void buildChestItems(Inventory chest, Player p) {
		
		chest.clear();
		
		/*
		 * get the players inventory items and convert them into shop items
		 */

		Material pboots = p.getInventory().getBoots() == null ? Material.GLASS : p.getInventory().getBoots().getType();
		Material plegs = p.getInventory().getLeggings() == null ? Material.GLASS : p.getInventory().getLeggings().getType();
		Material pchest = p.getInventory().getChestplate() == null ? Material.GLASS : p.getInventory().getChestplate().getType();
		Material phelm = p.getInventory().getHelmet() == null ? Material.GLASS : p.getInventory().getHelmet().getType();
		Material psword = p.getInventory().getItem(0) == null ? Material.GLASS : p.getInventory().getItem(0).getType();
		Material pmagic = p.getInventory().getItem(1) == null ? Material.GLASS : p.getInventory().getItem(1).getType();
		Material parrow = p.getInventory().getItem(3) == null ? Material.GLASS : p.getInventory().getItem(3).getType();
		//bow needs to be an itemStack so we can check enchantments
		ItemStack pbow = p.getInventory().getItem(2) == null ? new ItemStack(Material.GLASS) : p.getInventory().getItem(2);
		

		/*
		 * Convert to shop items or player stats
		 */

		shopItem currentboots = getShopItem(pboots);
		shopItem currentlegs = getShopItem(plegs);
		shopItem currentchest = getShopItem(pchest);
		shopItem currenthelm = getShopItem(phelm);
		shopItem currentsword = getShopItem(psword);
		shopItem currentmagic = getShopItem(pmagic);
		shopItem currentarrow = getShopItem(parrow);
		
		
		//if its a bow i have to check enchantment because there are multiple of the same key
		shopItem currentbow = getBowShopItem(pbow);
		
		
		////////
		
		HashMap<Material, playerStats> customPStats = myPlayerStats.get(p);
		playerStats strength = customPStats.get(Material.IRON_BLOCK);
		playerStats speed = customPStats.get(Material.FEATHER);
		playerStats jump = customPStats.get(Material.FIREWORK);
		playerStats health = customPStats.get(Material.PORK);
		
		
		//check if made of glass ? --> setcorrect armor slots
		
		if(currentboots.mat == Material.GLASS){//boots
			currentboots = new shopItem(Material.GLASS, 0, -1, "Nothing!", 100);
			currentboots.setLoreCurrent();
		}
		if(currentlegs.mat == Material.GLASS){//leggings
			currentlegs = new shopItem(Material.GLASS, 0, -1, "Nothing!", 101);
			currentlegs.setLoreCurrent();

		}
		if(currentchest.mat == Material.GLASS){//chest
			currentchest = new shopItem(Material.GLASS, 0, -1, "Nothing!", 102);
			currentchest.setLoreCurrent();

		}
		if(currenthelm.mat == Material.GLASS){//helmet
			currenthelm = new shopItem(Material.GLASS, 0, -1, "Nothing!", 103);
			currenthelm.setLoreCurrent();

		}
		if(currentsword.mat == Material.GLASS){
			currentsword = new shopItem(Material.GLASS, 0, -1, "Nothing!", 0);
			currentsword.setLoreCurrent();
		}
		if(currentmagic.mat == Material.GLASS){//axe
			currentmagic = new shopItem(Material.GLASS, 0, -1, "Nothing", 1);
			currentmagic.setLoreCurrent();
		}
		if(currentbow.mat == Material.GLASS){//bow
			currentbow = new shopItem(Material.GLASS, 0, -1, "Nothing", 2);
			currentbow.setLoreCurrent();
		}
		if(currentarrow.mat == Material.GLASS){//arrow
			currentarrow = new shopItem(Material.GLASS, 0, -1, "Nothing", 3);
			currentarrow.setLoreCurrent();
		}
		
		/*
		 * Set lore to "current item"
		 */

		currentboots.setLoreCurrent();
		currentlegs.setLoreCurrent();
		currentchest.setLoreCurrent();
		currenthelm.setLoreCurrent();
		currentsword.setLoreCurrent();
		currentmagic.setLoreCurrent();
		currentbow.setLoreCurrent();
		currentarrow.setLoreCurrent();
		
		///////
		
		strength.setUpgradeLore();
		speed.setUpgradeLore();
		jump.setUpgradeLore();
		health.setUpgradeLore();
		

		/*
		 *  Add player materials to chest
		 */
		
		chest.setItem(10,  currenthelm.getItemStack());
		chest.setItem(19, currentchest.getItemStack());
		chest.setItem(28, currentlegs.getItemStack());
		chest.setItem(37, currentboots.getItemStack());
		chest.setItem(18, currentsword.getItemStack());
		chest.setItem(27, currentmagic.getItemStack());
		chest.setItem(9, currentbow.getItemStack());
		chest.setItem(0, currentarrow.getItemStack());
		
		//Above = weapons and armor, Below = Player Stats//
		
		chest.setItem(15, strength.getUpgradeItem());
		chest.setItem(24, speed.getUpgradeItem());
		chest.setItem(33, jump.getUpgradeItem());
		chest.setItem(42, health.getUpgradeItem());
		
		

		/*
		 *Figure out next tier materials && add to chest
		 */

		chest.setItem(13, currenthelm.getNextTeir());
		chest.setItem(22, currentchest.getNextTeir());
		chest.setItem(31, currentlegs.getNextTeir());
		chest.setItem(40, currentboots.getNextTeir());
		chest.setItem(21, currentsword.getNextTeir());
		chest.setItem(30, currentmagic.getNextTeir());
		chest.setItem(12, currentbow.getNextTeir());
		chest.setItem(3, currentarrow.getNextTeir());
		
		//////
		
		chest.setItem(16, strength.getSkullItem());
		chest.setItem(25, speed.getSkullItem());
		chest.setItem(34, jump.getSkullItem());
		chest.setItem(43, health.getSkullItem());

		

		p.openInventory(chest);
	}

	
	public class playerStats {
		
		Material mat;
		Player p;
		String statType;
		Integer tier, price;
		ItemStack upgradeitem, skullitem;
		
		playerStats(Material mat, String statType, Integer price){
			this.mat = mat;
			this.statType = statType;
			this.tier = 1;
			this.price = price;
			upgradeitem = new ItemStack(mat);
		}
		
		public void upgradeTeir() {
			tier++;
			price+=20;
		}

		public ItemStack getUpgradeItem(){
			return upgradeitem;
		}
		
		public ItemStack getSkullItem(){
			return skullitem;
		}
		
		private String getUpgradeCost() {
			Integer price = 0;
			switch(tier){
			case 1:
				price = 40;
				break;
			case 2:
				price = 80;
				break;
			case 3:
				price = 160;
				break;
			case 4:
				price = 320;
				break;
			default:
				price = this.tier*100;
				break;
			}
			
			return price.toString();
		}
		
		public void setUpgradeLore(){
			ItemMeta meta = upgradeitem.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade " + statType + "!");
			List<String> lore = Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "Costs: ",
					ChatColor.AQUA + getUpgradeCost().toString(),
					"",
					ChatColor.DARK_PURPLE + "Clicking here",
					ChatColor.DARK_PURPLE + "will increase your ",
					ChatColor.DARK_PURPLE + statType});
			meta.setLore(lore);
			upgradeitem.setItemMeta(meta);
		}

		public void setSkullItem(){
			ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            meta.setOwner(p.getName());
            skull.setItemMeta(meta);
            skullitem = skull;
            setSkullLore();
		}
		
		private void setSkullLore() {
			ItemStack skull = skullitem;
			ItemMeta meta = skull.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN + p.getName() + "'s " + statType);
			List<String> lore = Arrays.asList(new String[]{ChatColor.DARK_GREEN + "Current Level:",
					ChatColor.DARK_PURPLE + tier.toString(),
					"",
					ChatColor.DARK_PURPLE + "Your " + statType,
					ChatColor.DARK_PURPLE + "is Level",
					ChatColor.AQUA + tier.toString()});
			meta.setLore(lore);
			skull.setItemMeta(meta);
			skullitem = skull;
		}

		public void setPlayer(Player p){
			this.p = p;
			setSkullItem();
		}
	}
	
	public void setupStats(){
		
		playerStatTypes.put(Material.IRON_BLOCK, new playerStats(Material.IRON_BLOCK, "Strength", 40));
		playerStatTypes.put(Material.FEATHER, new playerStats(Material.FEATHER, "Speed", 40));
		playerStatTypes.put(Material.FIREWORK, new playerStats(Material.FIREWORK, "Jump", 40));
		playerStatTypes.put(Material.PORK, new playerStats(Material.PORK, "Health", 40));
		
		for(ArenaPlayer ap: match.getPlayers()){//for each player
			Player p = ap.getPlayer();//get the player
			/*
			 * create a hashmap that contains all the types
			 * of stats available. the "playerStats" objects are linked to each player.
			 */
			HashMap<Material, playerStats> customPStats = setPlayerStats(p, playerStatTypes);
			/*
			 * this is a hashmap with the player linked to his hashmap of 
			 * "playerStats" object that are all linked to him.
			 */
			myPlayerStats.put(p, customPStats);
		}
		
	}
	
	public HashMap<Material, playerStats> setPlayerStats(Player p, HashMap<Material, playerStats> playerStatTypes){
		HashMap<Material, playerStats> newstats = new HashMap<Material, playerStats>();
		
		playerStats attack = new playerStats(Material.IRON_BLOCK, "Strength", 40);
		attack.setPlayer(p);
		newstats.put(Material.IRON_BLOCK, attack);
		
		playerStats speed = new playerStats(Material.FEATHER, "Speed", 40);
		speed.setPlayer(p);
		newstats.put(Material.FEATHER, speed);
		
		playerStats jump = new playerStats(Material.FIREWORK, "Jump", 40);
		jump.setPlayer(p);
		newstats.put(Material.FIREWORK, jump);
		
		playerStats health = new playerStats(Material.PORK, "Health", 40);
		health.setPlayer(p);
		newstats.put(Material.PORK, health);
		
		return newstats;
		/*
		 * the stats are now bonded to the player.
		 */
	}

	public class shopItem {
		Integer cost, tier, invSlot;
		Material mat;
		ItemStack item;
		String name;

		shopItem(){

		}

		shopItem(Material mat, Integer cost, Integer tier, String name, Integer invSlot) {
			this.mat = mat;
			this.cost = cost;
			this.name = name;
			this.tier = tier;
			this.item = new ItemStack(mat);
			this.invSlot = invSlot;
		}

		public ItemStack getItemStack() {
			if(this.invSlot == 2){//if its a bow
				Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
				switch(this.tier){
				case 1:
					//no enchant
					break;
				case 2:
					this.item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
					break;
				case 3:
					this.item.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
					break;
				case 4:
					enchants.put(Enchantment.ARROW_DAMAGE, 3);
					this.item.addEnchantments(enchants);
					break;
				case 5:
					enchants.put(Enchantment.ARROW_DAMAGE, 3);
					enchants.put(Enchantment.ARROW_FIRE, 1);
					this.item.addEnchantments(enchants);
					break;
				case 6:
					enchants.put(Enchantment.ARROW_DAMAGE, 3);
					enchants.put(Enchantment.ARROW_FIRE, 1);
					enchants.put(Enchantment.ARROW_KNOCKBACK, 1);
					this.item.addEnchantments(enchants);
					break;
				case 7:
					enchants.put(Enchantment.ARROW_DAMAGE, 3);
					enchants.put(Enchantment.ARROW_FIRE, 1);
					enchants.put(Enchantment.ARROW_KNOCKBACK, 2);
					enchants.put(Enchantment.ARROW_INFINITE, 1);
					this.item.addEnchantments(enchants);
					break;
				}
			}
			return this.item;
		}

		public ItemStack getPreviousTier(){
			Integer thistier = this.tier;
			
			for(shopItem sitem:myShopItems.values()){
				if(sitem.tier == thistier - 1){
					if(sitem.invSlot == this.invSlot){
						return sitem.getItemStack();
					}
				}
			}
			return null;
		}
		
		public ItemStack getNextTeir(){
			Integer thistier = this.tier;
			ItemStack returnitem = null;

			if(thistier < 0){
				if(this.invSlot == 100){
					shopItem buff = myShopItems.get(Material.LEATHER_BOOTS);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 101){
					shopItem buff = myShopItems.get(Material.LEATHER_LEGGINGS);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 102){
					shopItem buff = myShopItems.get(Material.LEATHER_CHESTPLATE);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 103){
					shopItem buff = myShopItems.get(Material.LEATHER_HELMET);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 0){
					shopItem buff = myShopItems.get(Material.WOOD_SWORD);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 1){
					shopItem buff = myShopItems.get(Material.WOOD_AXE);
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 2){
					shopItem buff = new shopItem(Material.BOW, 20, 1, "Upgrade Bow!", 2);//Have to create new because there are multiple with the same key.
					buff.setLoreNext();
					return buff.getItemStack();
				}else if(this.invSlot == 3){
					this.item = new ItemStack(Material.ARROW, 20);
					this.setLoreNext();
					return item;
				}
			}
			
			if(this.invSlot == 2){//if its a bow, you need to get the next tier differently
				if(this.tier++ <= 7){//if its not already tier 7, then get next tier and retrieve itemStack
					this.item = this.getItemStack();
					this.setLoreNext();
					return this.item;
				}else{
					this.setLoreMax();
					return this.item;
				}
			}
			
			for(shopItem sitem:myShopItems.values()){
				if(sitem.tier == thistier + 1){
					if(sitem.invSlot == this.invSlot){
						sitem.setLoreNext();
						return sitem.getItemStack();
					}
				}
			}

			for(shopItem sitem:myShopItems.values()){
				if(sitem.tier <= thistier){
					this.setLoreMax();
					return this.getItemStack();
				}
			}
			returnitem =  null;
			return returnitem;
		}
		
		public boolean isHighestTier(){
			if(this.tier < 0){
				return false;
			}
			Boolean buff = null;
			for(shopItem possible: myShopItems.values()){
				if(possible.invSlot == this.invSlot){
					if(possible.tier >= this.tier){
						return false;
					}else{
						buff = true;
					}
				}
			}
			return buff;
		}

		public void setLoreMax(){
			ItemMeta meta = item.getItemMeta();
			List<String> lore = Arrays.asList(new String[]{"This is the highest level ", mat.toString() + " possible!"});
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
		
		public void setLoreNext(){
			ItemMeta meta = item.getItemMeta();
			List<String> lore;
			if(this.invSlot == 1){
				lore = Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "Do not click!", " Eats money currently..."});
						//ChatColor.AQUA.toString() + this.tier,
						//ChatColor.DARK_PURPLE + "Price:" + ChatColor.DARK_GREEN + this.cost.toString()});
			}else if(this.invSlot == 3){
				lore = Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "Get 20 more Arrows!"});
			}else{
				lore = Arrays.asList(new String[]{ChatColor.DARK_PURPLE + "Click Here To Upgrade to: ", 
						ChatColor.AQUA + mat.toString(), 
						ChatColor.DARK_PURPLE +"Price: " + ChatColor.DARK_GREEN + this.cost.toString()});
			}
			meta.setLore(lore);
			meta.setDisplayName(this.name);
			item.setItemMeta(meta);
		}

		public void setLoreCurrent(){
			ItemMeta meta = item.getItemMeta();
			List<String> lore;
			if(this.mat == Material.GLASS){
				meta.setDisplayName("Nothing!");
				if(this.invSlot == 0){
					lore = Arrays.asList(new String[]{ChatColor.DARK_RED + "You have no weapon!"});
				}else if(this.invSlot == 1){
					lore = Arrays.asList(new String[]{ChatColor.DARK_RED + "You have no magic abilities!"});
				}else{
					lore = Arrays.asList(new String[]{ChatColor.DARK_RED + "You have no armor in this slot!"});
				}
				
			}else{
				meta.setDisplayName("Current Item");
				lore = Arrays.asList(new String[]{ChatColor.DARK_GREEN + "This is your current " ,ChatColor.DARK_GREEN + mat.toString()});
			}
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}
	public shopItem getShopItem(Material mat){
		if(myShopItems.containsKey(mat)){
			return myShopItems.get(mat);
		}else{
			Bukkit.broadcastMessage("not a valid material || how did that get in your inventory???");
			return null;
		}
	}
	
	private shopItem getBowShopItem(ItemStack pbow){
		shopItem currentbow = new shopItem(Material.BOW, 20, 1, "Upgrade Bow!", 2);
		if(pbow.containsEnchantment(Enchantment.ARROW_INFINITE)){
			currentbow = new shopItem(Material.BOW, 350, 7, "Upgrade Bow!", 2);
		}else if(pbow.containsEnchantment(Enchantment.KNOCKBACK)){
			currentbow = new shopItem(Material.BOW, 200, 6, "Upgrade Bow!", 2);
		}else if(pbow.containsEnchantment(Enchantment.ARROW_FIRE)){
			currentbow = new shopItem(Material.BOW, 150, 5, "Upgrade Bow!", 2);
		}else if(pbow.containsEnchantment(Enchantment.ARROW_DAMAGE)){
			if(pbow.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 3){
				currentbow = new shopItem(Material.BOW, 100, 4, "Upgrade Bow!", 2);
			}else if(pbow.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 2){
				currentbow = new shopItem(Material.BOW, 60, 3, "Upgrade Bow!", 2);
			}else if(pbow.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) == 1){
				currentbow = new shopItem(Material.BOW, 40, 2, "Upgrade Bow!", 2);
			}
		}else if(pbow.getType() == Material.GLASS){
			currentbow = getShopItem(Material.GLASS);
		}
		return currentbow;
	}
	
	public ItemStack makeInvincible(ItemStack item){
		ItemMeta meta = item.getItemMeta();
		meta.spigot().setUnbreakable(true);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		item.setItemMeta(meta);
		return item;
	}
	
	private void setUpItems() {
		// This is our static data about items
		//for line in myfile{
		//	myShopItems.put(line.col1, new shopItem(line.col1, line.col2, line.col3, line.col4))
		//}
		myShopItems.put(Material.GLASS, new shopItem(Material.GLASS, 0, -1, "Nothing!", 0));
		
		myShopItems.put(Material.ARROW, new shopItem(Material.ARROW, 10, -1, "Get Arrows!", 3));
		
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 20, 1, "Upgrade Bow!", 2));//no enchant
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 40, 2, "Upgrade Bow!", 2));//1 damage
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 60, 3, "Upgrade Bow!", 2));//2damage
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 100, 4, "Upgrade Bow!", 2));//3 damage 1 fire
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 150, 5, "Upgrade Bow!", 2));//3 damage 3 fire 1 knockback
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 200, 6, "Upgrade Bow!", 2));//3 damage 3 fire 3 knockback
		myShopItems.put(Material.BOW, new shopItem(Material.BOW, 350, 7, "Upgrade Bow!", 2));//3 damage 3 fire 3 knockback & infinity
		
		myShopItems.put(Material.WOOD_AXE, new shopItem(Material.WOOD_AXE, 15, 1, "Coming Soon! Upgrade Magic...", 1));
		//myShopItems.put(Material.STONE_AXE, new shopItem(Material.STONE_AXE, 35, 2, "Upgrade Magic!", 1));
		//myShopItems.put(Material.IRON_AXE, new shopItem(Material.IRON_AXE, 55, 3, "Upgrade Magic!", 1));
		//myShopItems.put(Material.DIAMOND_AXE, new shopItem(Material.DIAMOND_AXE, 75, 4, "Upgrade Magic!", 1));
		
		myShopItems.put(Material.WOOD_SWORD, new shopItem(Material.WOOD_SWORD, 15, 1, "Upgrade Weapon!", 0));
		myShopItems.put(Material.STONE_SWORD, new shopItem(Material.STONE_SWORD, 25, 2, "Upgrade Weapon!", 0));
		myShopItems.put(Material.IRON_SWORD, new shopItem(Material.IRON_SWORD, 35, 3, "Upgrade Weapon!", 0));
		myShopItems.put(Material.DIAMOND_SWORD, new shopItem(Material.DIAMOND_SWORD, 45, 4, "Upgrade Weapon!", 0));
		
		myShopItems.put(Material.LEATHER_BOOTS, new shopItem(Material.LEATHER_BOOTS, 10, 1, "Upgrade Boots", 100));
		myShopItems.put(Material.LEATHER_LEGGINGS, new shopItem(Material.LEATHER_LEGGINGS, 10, 1, "Upgrade Leggings", 101));
		myShopItems.put(Material.LEATHER_CHESTPLATE, new shopItem(Material.LEATHER_CHESTPLATE, 10, 1, "Upgrade Chestplate", 102));
		myShopItems.put(Material.LEATHER_HELMET, new shopItem(Material.LEATHER_HELMET, 10, 1, "Upgrade Helmet", 103));

		myShopItems.put(Material.CHAINMAIL_BOOTS, new shopItem(Material.CHAINMAIL_BOOTS, 10, 2, "Upgrade Boots", 100));
		myShopItems.put(Material.CHAINMAIL_LEGGINGS, new shopItem(Material.CHAINMAIL_LEGGINGS, 12, 2, "Upgrade Leggings", 101));
		myShopItems.put(Material.CHAINMAIL_CHESTPLATE, new shopItem(Material.CHAINMAIL_CHESTPLATE, 15, 2, "Upgrade Chestplate", 102));
		myShopItems.put(Material.CHAINMAIL_HELMET, new shopItem(Material.CHAINMAIL_HELMET, 10, 2, "Upgrade Helmet", 103));

		myShopItems.put(Material.IRON_BOOTS, new shopItem(Material.IRON_BOOTS, 20, 3, "Upgrade Boots", 100));
		myShopItems.put(Material.IRON_LEGGINGS, new shopItem(Material.IRON_LEGGINGS, 22, 3, "Upgrade Leggings", 101));
		myShopItems.put(Material.IRON_CHESTPLATE, new shopItem(Material.IRON_CHESTPLATE, 25, 3, "Upgrade Chestplate", 102));
		myShopItems.put(Material.IRON_HELMET, new shopItem(Material.IRON_HELMET, 20, 3, "Upgrade Helmet", 103));

		myShopItems.put(Material.DIAMOND_BOOTS, new shopItem(Material.DIAMOND_BOOTS, 50, 4, "Upgrade Boots", 100));
		myShopItems.put(Material.DIAMOND_LEGGINGS, new shopItem(Material.DIAMOND_LEGGINGS, 60, 4, "Upgrade Leggings", 101));
		myShopItems.put(Material.DIAMOND_CHESTPLATE, new shopItem(Material.DIAMOND_CHESTPLATE, 75, 4, "Upgrade Chestplate", 102));
		myShopItems.put(Material.DIAMOND_HELMET, new shopItem(Material.DIAMOND_HELMET, 50, 4, "Upgrade Helmet", 103));
		
	}

}





