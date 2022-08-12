package neo.feature;

import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ChannelManager {
    public HashMap<String, ArrayList<Player>> channels = new HashMap<>();
    public HashMap<Player, String> playerChannel = new HashMap<Player, String>();

    public void joinChannel(Player player, String channelName){
        if(playerChannel.get(player) != null){
            String prevChannel = playerChannel.get(player);
            if(!prevChannel.equals("General"))
                channelName = "General";
            leaveChannel(player, prevChannel);
        }

        ArrayList<Player> players = channels.get(channelName);
        if(players == null){
            players = new ArrayList<Player>();
        }
        players.add(player);
        channels.put(channelName, players);
        playerChannel.put(player, channelName);

        if(channelName.equals("General")){
            player.sendMessage(ChatColor.GOLD + "일반 " + ChatColor.GOLD + "채널에 입장하셨습니다.");
        }else{
            player.sendMessage(ChatColor.GOLD + channelName + ChatColor.GOLD + " 해적단 채널에 입장하셨습니다.");
        }
    }

    public void leaveChannel(Player player, String channelName){
        ArrayList<Player> players = channels.get(channelName);
        players.remove(player);
        channels.put(channelName, players);
        playerChannel.remove(player);

        if(channelName.equals("General")){
            player.sendMessage(ChatColor.GOLD + "일반" + ChatColor.DARK_AQUA + " 채널에서 나가셨습니다.");
        }else{
            player.sendMessage(ChatColor.GOLD + channelName + ChatColor.DARK_AQUA + " 해적단 채널에서 나가셨습니다.");
        }
    }

    public ArrayList<Player> getChannel(Player player){
        String channelName = playerChannel.get(player);
        return channels.get(channelName);
    }
}
