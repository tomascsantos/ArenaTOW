package io.github.TcFoxy.ArenaTOW.BattleArena.objects.joining;

import mc.alk.arena.objects.ArenaPlayer;
import mc.alk.arena.objects.MatchParams;
import mc.alk.arena.objects.exceptions.InvalidOptionException;
import mc.alk.arena.objects.spawns.SpawnLocation;
import mc.alk.arena.util.MessageUtil;
import mc.alk.arena.util.MinMax;
import mc.alk.arena.util.PermissionsUtil;
import mc.alk.arena.util.TeamUtil;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;

import io.github.TcFoxy.ArenaTOW.BattleArena.MyBattleArena;
import io.github.TcFoxy.ArenaTOW.BattleArena.objects.arenas.MyArena;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MyJoinOptions {

    public static enum MyJoinOption{
        ARENA("<arena>",false), TEAM("<team>",false),
        WANTEDTEAMSIZE("<teamSize>",false);

        final public boolean needsValue;
        final String name;
        MyJoinOption(String name, boolean needsValue){
            this.needsValue = needsValue;
            this.name = name;
        }
        public String getName(){
            return name;
        }
        public static MyJoinOption fromName(String str){
            str = str.toUpperCase();
            try {return MyJoinOption.valueOf(str);} catch (Exception e){/*do nothing*/}
            throw new IllegalArgumentException();
        }
        public static String getValidList() {
            StringBuilder sb = new StringBuilder();
            boolean first = true;
            for (MyJoinOption r: MyJoinOption.values()){
                if (!first) sb.append(", ");
                first = false;
                String val = "";
                switch (r){
                    default: break;
                }
                sb.append(r.getName()).append(val);
            }
            return sb.toString();
        }
    }

    /** All options for joining */
    final Map<MyJoinOption,Object> options = new EnumMap<MyJoinOption,Object>(MyJoinOption.class);

    /** Location they have joined from */
    Location joinedLocation = null;

    MatchParams params;

    MinMax teamSize;

    /** When the player joined, (defaults to when the JoinOptions was created) */
    long joinTime;

    public MyJoinOptions(){
        joinTime = System.currentTimeMillis();
    }

    public void setJoinLocation(Location joinLocation) {
        this.joinedLocation = joinLocation;
    }

    public boolean nearby(MyArena arena, double distance) {
        if (joinedLocation == null)
            return false;
        UUID wid = joinedLocation.getWorld().getUID();
        Location arenajoinloc = arena.getJoinLocation();
        if (arenajoinloc != null){
            return (wid == arenajoinloc.getWorld().getUID() &&
                    arenajoinloc.distance(joinedLocation) <= distance);
        }

        for (List<SpawnLocation> list : arena.getSpawns()){
            for (SpawnLocation l : list){
                if (l.getLocation().getWorld().getUID() != wid)
                    return false;
                if (l.getLocation().distance(joinedLocation) <= distance)
                    return true;

            }
        }
        return false;
    }

    public boolean sameWorld(MyArena arena) {
        UUID wid = joinedLocation.getWorld().getUID();
        Location arenajoinloc = arena.getJoinLocation();
        if (arenajoinloc != null){
            return (wid == arenajoinloc.getWorld().getUID());}

        for (List<SpawnLocation> list : arena.getSpawns()){
            for (SpawnLocation l : list){
                if (l.getLocation().getWorld().getUID() != wid)
                    return false;
            }
        }
        return true;
    }

    public static MyJoinOptions parseOptions(final MatchParams omp, ArenaPlayer player, String[] args)
            throws InvalidOptionException, NumberFormatException{
        MyJoinOptions jos = new MyJoinOptions();
        MatchParams mp = new MatchParams(omp.getType());
        mp.setParent(omp);
        jos.setMatchParams(mp);
        jos.setJoinTime(System.currentTimeMillis());
        if (player != null)
            jos.joinedLocation = player.getLocation();
        Map<MyJoinOption,Object> ops = jos.options;
        MyArena arena = null;
        String lastArg = args.length > 0 ? args[args.length-1] : "";
        int length = args.length;

        for (int i=0;i<length;i++){
            String op = args[i];
            if (op.isEmpty())
                continue;
            Object obj = null;
            op = MessageUtil.decolorChat(op);
            MyArena a = MyBattleArena.getBAController().getArena(op);
            if (a != null){
                if (arena != null){
                    throw new InvalidOptionException("&cYou specified 2 arenas!");}
                if (!a.valid()){
                    throw new InvalidOptionException("&cThe specified arena is not valid!");}
                arena = a;
                ops.put(MyJoinOption.ARENA, arena);
                continue;
            }
            try {
                Integer wantedSize = Integer.valueOf(op);
                ops.put(MyJoinOption.WANTEDTEAMSIZE, wantedSize);
                mp.setTeamSize(wantedSize);
                continue;
            } catch (Exception e){
                /* do nothing*/
            }
            Integer teamIndex = TeamUtil.getFromHumanTeamIndex(op);
            if (teamIndex != null){
                if (player != null && !PermissionsUtil.hasTeamPerm(player.getPlayer(), mp,teamIndex)){
                    throw new InvalidOptionException("&cYou don't have permissions to join this team");}
                ops.put(MyJoinOption.TEAM, teamIndex);
                continue;
            }

            MyJoinOption jo;
            try{
                jo = MyJoinOption.fromName(op);
                if (jo.needsValue && i+1 >= args.length){
                    throw new InvalidOptionException("&cThe option " + jo.name()+" needs a value!");}
            } catch(IllegalArgumentException e){
                throw new InvalidOptionException("&cThe arena or option " + op+" does not exist, \n&cvalid options=&6"+
                        MyJoinOption.getValidList());
            }
            switch(jo){
                default:
                    break;
            }

            if (!jo.needsValue){
                ops.put(jo,null);
                continue;
            }
            String val = args[++i];
            switch(jo){
                case ARENA:
                    obj = MyBattleArena.getBAController().getArena(val);
                    if (obj==null){
                        throw new InvalidOptionException("&cCouldnt find the arena &6" +val);}
                    a = (MyArena) obj;
                    if (!a.valid()){
                        throw new InvalidOptionException("&cThe specified arena is not valid!");}
                    arena = a;
                default:
                    break;
            }
            ops.put(jo, obj);
        }
        if (arena != null){
            mp.setParent(arena.getParams());
            if ((!arena.matches(jos))){
                throw new InvalidOptionException("&cThe arena &6" +arena.getName() +
                        "&c doesn't match your add requirements. "  +
                        StringUtils.join( arena.getInvalidMatchReasons(mp, jos), '\n'));
            }
        }
        MinMax mm = null;
        try{mm = MinMax.valueOf(lastArg);} catch (Exception e){/* do nothing */}

        if (mm != null)
            ops.put(MyJoinOption.WANTEDTEAMSIZE, mm);

        jos.params= mp;
        return jos;
    }

    public void setJoinTime(Long currentTimeMillis) {
        this.joinTime = currentTimeMillis;
    }

    public Long getJoinTime(){
        return joinTime;
    }

    public String optionsString(MatchParams mp) {
        StringBuilder sb = new StringBuilder(mp.toPrettyString()+" ");
        for (MyJoinOption op: options.keySet()){
            sb.append(op.getName());
            if (op.needsValue){
                sb.append("=").append(options.get(op));
            }
            sb.append(" ");
        }
        return sb.toString();
    }

    public boolean hasWantedTeamSize() {
        return options.containsKey(MyJoinOption.WANTEDTEAMSIZE);
    }

    public boolean hasOption(MyJoinOption option) {
        return options.containsKey(option);
    }

    public Object getOption(MyJoinOption option) {
        return options.get(option);
    }

    public Object setOption(MyJoinOption option, Object value) {
        return options.put(option, value);
    }

    public boolean hasArena() {
        return options.containsKey(MyJoinOption.ARENA) && options.get(MyJoinOption.ARENA)!=null;
    }

    public MyArena getArena() {
        return hasArena() ? (MyArena) options.get(MyJoinOption.ARENA) : null;
    }


    public void setArena(MyArena arena) {
        options.put(MyJoinOption.ARENA, arena);
    }

    public MatchParams getMatchParams() {
        return params;
    }

    public void setMatchParams(MatchParams matchParams) {
        this.params = matchParams;
    }

    @Override
    public MyJoinOptions clone() {
        MyJoinOptions jo = new MyJoinOptions();
        jo.options.putAll(this.options);
        jo.joinedLocation = this.joinedLocation;
        jo.params = this.params;
        jo.teamSize = this.teamSize;
        jo.joinTime = this.joinTime;
        return jo;
    }

    public void setPlayer(ArenaPlayer player) throws InvalidOptionException {
        this.joinedLocation = player.getLocation();
        this.joinTime = System.currentTimeMillis();
        Object teamIndex = options.get(MyJoinOption.TEAM);
        if (teamIndex != null){
            if (!PermissionsUtil.hasTeamPerm(player.getPlayer(), getMatchParams(),(Integer) teamIndex)){
                throw new InvalidOptionException("&cYou don't have permissions to add this team");}
        }

    }

}

