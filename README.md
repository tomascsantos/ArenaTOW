# Arena Tug of War
Minecraft was my first exposure to programming. This (now 5000+ lines of code) plugin is the culmination of several summers worth of learning and experimentation. It uses BattleArena to setup a MOBA style arena, and then spawns towers, minions and nexus. The two teams battle to destroy the opposite nexus similarly to the popular League of Legends video game.
##### Credit to: [BattleArena](https://github.com/BattlePlugins/BattleArena)

### Installation
ArenaTOW is a plugin that runs on Spigot minecraft servers. It is compatible with minecraft versions 1.10, 1.11, 1.12
Spigot Server Installation tutorial [here](https://www.spigotmc.org/wiki/spigot-installation/)
Setup tutorial [here](https://www.youtube.com/watch?v=dDaS4_0cbno&t=49s).

### Features Include
- Nexus (Elder Guardian):
Each Arena must have two Nexi, (Red and Blue for the respective teams). When either nexus is destroyed the game is finished. The nexus has a powerful, long-range attack that targets enemy minions and enemy players.

- Tower (Golem):
Each arena can have multiple Towers for each team color. The stationary towers will target enemy players and Minions with fireball attacks in a 5x5 area.

- Minions (Zombies):
Minion spawn periodically and take a configurable path to the enemy nexus. The Minions wear colored hats that correspond to their team color. They will attack Minions, Towers, Nexus, and Players in that order.

- No friendly fire
Entities on the same team don't hurt each other, including players.

- Extremely Configurable:
All Nexus, Tower, Minion Spawning and Player spawning locations are up to you! Minions take a path that is marked by you!

### Highlights
Sophisticated maven outline for backwards compatibility over multiple NetMinecraftServer (obfuscated api) versions.
Robust configurability for customized gameplay.
Seemless integration with popular BattleArena plugin.

##### Project Scope

I used [cloc](https://github.com/AlDanial/cloc#quick-start-) to see how large the project is

| ------------------------------------------------------------------------------- |
| Language          |           files      |    blank    |    comment     |      code |
-------------------------------------------------------------------------------
| Java               |             77     |      1200      |     1039     |      4012 |
| ----------------- |
| XML                |             28        |      1       |       0     |      1373 |
| ----------------- |
| Maven                |            6             17             11            266 |
| ----------------- |
| YAML                |             2              4             71             52 |
| ----------------- |
| Markdown           |              1      |        2      |        0     |         5 |
| ------------------------------------------------------------------------------- |
| SUM:             |              114     |      1224    |       1121       |    5708 |
| ------------------------------------------------------------------------------- |
