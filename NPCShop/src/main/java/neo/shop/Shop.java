package neo.shop;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import neo.data.DataManager;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

public class Shop {
    Main plugin = Main.getPlugin();
    DataManager data = Main.getData();
    // NPC 생성
    public void createShop(Player p, String npcName) {
        if (!data.getFile().contains("npc." + npcName)) {
            NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(plugin, npcName, p.getLocation());
//            NPC.Personal npc = NPCLib.getInstance().generatePersonalNPC(p, plugin, npcName, p.getLocation());
            npc.setFollowLookType(NPC.FollowLookType.PLAYER);
            data.getFile().set("npc." + npcName + ".location", p.getLocation());
            data.saveConfig();
            npc = setNPCRandomSkin(npc, npcName);
            npc.setText(ChatColor.AQUA + npcName, ChatColor.GOLD + "상인");
            npc.setAutoCreate(true);
            npc.setAutoShow(true);
            npc.update();
            npc.forceUpdateText();
            p.sendMessage(ChatColor.GREEN + "정상적으로 상점이 생성되었습니다!");
        }else{
            p.sendMessage(ChatColor.RED + "이미 존재하는 이름입니다. 다른 이름을 입력해주세요!");
        }
    }
    // 상점 삭제
    public void deleteShop(Player p, String npcName){
        boolean check = false;
        Set<NPC.Global> global = NPCLib.getInstance().getAllGlobalNPCs();
        for(NPC.Global g : global){
            if(g.getCode().equals("npcshop."+npcName)){
                NPCLib.getInstance().removeGlobalNPC(g);
                data.getFile().set("npc." + npcName, null);
                data.saveConfig();
                check = true;
                p.sendMessage(ChatColor.GOLD + npcName + ChatColor.GREEN + "상점이 성공적으로 제거되었습니다!");
            }
        }
        if(!check)
            p.sendMessage(ChatColor.RED + "존재하지 않는 상점 이름입니다.");
    }

    // 모든 NPC 불러와서 생성
    public void reloadShop(){
        ConfigurationSection section = data.getFile().getConfigurationSection("npc");
        for(String key: section.getKeys(false)){
            String npcName = key;
            Location location = data.getFile().getLocation("npc."+npcName+".location");
            String value = data.getFile().getString("npc."+npcName+".skin.value");
            String signature = data.getFile().getString("npc."+npcName+".skin.signature");
            NPC.Global npc = NPCLib.getInstance().generateGlobalNPC(plugin, npcName, location);
            npc.setSkin(value, signature);
            npc.setFollowLookType(NPC.FollowLookType.PLAYER);
            npc.setText(ChatColor.AQUA + npcName, ChatColor.GOLD + "상인");
            npc.setAutoCreate(true);
            npc.setAutoShow(true);
            npc.update();
            npc.forceUpdateText();
        }
    }

    // NPC 랜덤 스킨 적용
    public NPC.Global setNPCRandomSkin(NPC.Global npc, String npcName){
        // NPC 추가 (mineskin.org)
        ArrayList<String>[] skins =  new ArrayList[7];
        for(int i = 0; i < 7; i++){
            skins[i] = new ArrayList<>();
        }
        skins[0].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2OTU3MDkwNCwKICAicHJvZmlsZUlkIiA6ICJmMTkyZGU3MDUzMTQ0ODcxOTAwMjQ1MmIzZWE3MzA3NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJOZVhvU2V0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2E5NGMxYWQwYWRhYmJkNGE1YjUyNGU2ZjJjZDBiZDhlOWZhODFlNzdjOWViOTc0OTcwNzgzM2ZiYmZmYmM3YzciLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
        skins[0].add("kElNcrjM3oEILPg67x8Hf0O96MUfUTsshTjfGb8Y2BX5qAXJuvEqCqEB3g2120ENw4EBpncPnx9M");
        skins[1].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2NjA0NzI1OCwKICAicHJvZmlsZUlkIiA6ICJjOWRlZTM4MDUzYjg0YzI5YjZlZjA5YjJlMDM5OTc0ZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTQVJfRGVjZW1iZXI1IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY2MTI5YWUyOTU5Yjc4N2UwMjAxMzQwYWRhMWRjNThiNWZmMzgyYmVjNDdlNTE2N2MzYTczMzQ5NTVlZTMzZjIiCiAgICB9CiAgfQp9");
        skins[1].add("o2jTfse9zcMHtiiXFjGlyUsrVzXAKL9yu++bwKKjXSVio+drNZ11nAMRpVk8LE2caBL7aKvPOtx3AEmwinPNBehjpwl3scn10Rh574UvLmVXbQSKEacZZjlgEk4nYeT0TJd/gU15cdtHh2gzcXsTUErQHDit/3MvnOixN9LJ5QoOwjmDf8HVAKyN7y9f5ufBDpDso+NTQI9A5gsFV+YagENpyyybjqrjTq46XO7C2Pr3F9ErI34GHxTZ3Nrjqvpti79OL8U7gfFTVrD7yvE5fo/EPAXwu0K3cZgxMKv12mo6xQvTFVjNMUYwkFCG40uHrf+iP20oCuorELMzQohbPfe1wiVtB50X30wULOAhbEISCKFaiIhtSpDx6XDRI8qka/YEIkRVPUJJ5bUKc+quv/jsK/GxZxgZnAgqcLNcbL/fW0fYYoZMP/Gg87sB9VctWypuwpOubmd99v+MVF3sYIHc7j57M0tM4Rw6H5vfwAgvuyvYpkT4uUjKZcOu4qs1cVDiomWSgjKTRWcaJWG6iwSXDFWLNcGtuRHtebq8qLMIuQvDF2ZMemrJOQPZ9FvVbBKMGtrIz+DwTyYAQ7j6Ut1RpDOtW//YQ379oebDwSSD2U0yKfISNBqWSzoKM/LHHVWWIlp52r6uOxhVbDEGFVCzMz/p7iOD6LXrVjdL450=");
        skins[2].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2ODIzNjM1OSwKICAicHJvZmlsZUlkIiA6ICIyMWUzNjdkNzI1Y2Y0ZTNiYjI2OTJjNGEzMDBhNGRlYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJHZXlzZXJNQyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS85ODMxYzcyNDY3OWY5ZDNkMGU2N2JhZDAxMGQ4MDcxMzYyM2VmZWYxZjgyYmY1ODQyMjczMDY0ZDZlYjczODM0IgogICAgfQogIH0KfQ==");
        skins[2].add("sYc5w0dG6GfLKd2j3qAQ8ypmWHc4Wgqw7zz/vz+WfWgmHWKRtuPdfgFWIeMVmJKwL5VIyE5azZJPOVO2lo2uIKmXwNCjrUMLhwomjmKguGfP60hJSHGIfJbz0qmGxFGGkciDxTIBzMz2vlYHA9EeOfnbF1N+Xpa97nfHiB0FoCuB7THA2vrJI+2RE+E+zn0tYDHBBc+B2ZFI9sctPaHyLkxbtqIz6IaWq9cxMkuqIP6m8gPtWzvKfBfHREszIKDhxlQboGH/ENndCJJeW6hXAuAlB+7aKOH90azhws9oQ8cMgy1rFfTJKGFFcS0WNV6BJt7IDB61pkXJEKKZ/jh1LVm1Lyem8otmRUbrPyPRgmpvdgvlSgIjBEMSzZmlqzHc9salRS+QVFN168Pqv2Kk+zfYE3rW2vwfVRBJoxscDXhNITSo5QfCx5NJQcPOkNnfE+maMvkiHdGcOAlvcZqp2OursLnqv9u8XIvFxDZfLUasAKdTY5JxOgVcFlUQsUKzRkdsfbZkMCVPR86fEkOtdYMRCLHZ3lM39bLS9IpAjTLm+vHZ0hhsQnNGSGUcBu5rHW7Yxyy2/tYhxQknMxXTuohoIbNr6Ktp8h95rPZPlmNkE3zF4SUzne0QVo25NJUFS4J7G2x7wjD3szOaguhssRj9rhHaKttSsX3VgZ4CW9E=");
        skins[3].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2MzQ0MTcyMiwKICAicHJvZmlsZUlkIiA6ICJkOGNkMTNjZGRmNGU0Y2IzODJmYWZiYWIwOGIyNzQ4OSIsCiAgInByb2ZpbGVOYW1lIiA6ICJaYWNoeVphY2giLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmYyODE5ZTBkYWNhZWY0OGMyMGM2ZDFkYjkxZmM5NmZiNWE4ZGMxNDRkYTNjNzcxODA4NTlkODljZjBiMTVmMiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
        skins[3].add("d51VZSEXHg0xGo0vsDicLzbIeYvbASAPC7nSksLBEAUiujOJMSxKe1YWmtsWt4u");
        skins[4].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2NjQ0MTkxNCwKICAicHJvZmlsZUlkIiA6ICIyZDlhNWExYjIzZWQ0MWRkYjgzMWMzZjM3Zjk2NzA3ZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJBd2Vzb21lS2FsaW41NSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS8xMmM4M2M4YmJkZWVmZjY5MmZiYmU5ZmE3YTRiN2I4MTZkNmNkMzhjMDk0OTA1N2ViODdkMGQ5NDkyNmFmODU3IgogICAgfQogIH0KfQ==");
        skins[4].add("FrV1y1IkKCk8Epbp36MQI+JARhIar0yu+NyKV8ph8PcaMApMAFLhkyl9b6bLhfEh8BkeQO8En7rQY0vj5eaQXXxjD8hm0GI8TsTD0nd/qsstu8vYTW1mZZVpQe2FvYZtmMlNd3lvpshN6dUHaic4VICGpELoUjQMMl+60ATl1DA4uxwDCc5s/6q5JKxhBBHJruec1Vf+PatCPSwkIgLEGaza7ZTxKxe8H4VMTnmtG1EXgaj0j5KybvuGW3yryGfY36zsISWFPPSGUxj2NqNV0V1DWsz3xmnT83qWx0W0kcogdOx3qbyaZ9iB20z0Yp4V2FuuAxKtUAvu8wpr5RoDLWude0QrRxQ2hKrGT2kR6Z30LtjTVtZ3h+2XDsSgiiTxnwtwgGjGwk65ULxf9mVR+fyq3nxQfJrwlOuUnkqbmXIJTgo+OlZAzQWAFHVLjJqrhnt4Rpf6SUfHIY8NO3MI9fkWdwnklE9Wy7fnEL00hVvuHqJwt7qu4DNinA+DYMYCb5hdsvUO90oqLxO/iptLXye0R7avUODSvCMmkw1FD1qCWL59kRH4CvwoigzMAtCYM780Ekw0RciKnxzoO0fOcWeRExu3A/sXrHOQUkg9aenRCOafchPEyGAe7WbzOwu8WlEgDmgftdR3s6w9JeDTwJh7uT5ZjKqSihaOvnWs5dg=");
        skins[5].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU2MjAzMjQwNSwKICAicHJvZmlsZUlkIiA6ICJkYmQ4MDQ2M2EwMzY0Y2FjYjI3OGNhODBhMDBkZGIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ4bG9nMjEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFhZjMwYzYzMzI5M2FiNDZiZGJhY2UyOTBiMDA4OTUyNjIzYjQ3NzNhZGE4NzU2MmYxNDY5ODkwNzk2ZjA2MCIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9");
        skins[5].add("WcuTQtpe+YFgzJxAywn9YCkaRzXHyKOyWTRcbfLjYq1KIRuRdSUhPzuZRISAEXe7kS2sTWmshyfZD2sndtIEIzlho1HAvk5muWMJgLwuDrURmxgIBGhJDt+Ahs+MY2Ir5Kr4yTOp+ub17oYJ/lMJhqtVWa+TZLfxIwSaa7fAXwg/pHW6VI7EuqQaRmUESZrlup7QHzfbVgNxGPbl1AZ5hY6gFb23dHGkpOTb2dApTQ3n0Ox3a+KyI3u3XYSRcrjRbdDN7W5sSMTU9mXN9Crqagjmkm6nJLcvnK/r8Nwnmhwu3RtikrjfeAQuXCjgUxTTmTA9iNMozkjAULb10n5ZQGlVUxu3wKub/TWft1QXooSauiwjRBwxEd5vl32+kN52uJlkATm8RPq15YOfFSzJ1T3OaRlcw6IOzYV5FBz/v9RbXlz1ebDNcKd/T9jWP2EPmdotDObYb25JcsnNDjUM/csu4YFu0MtH4a4HE2Oq29aD8ndUNoxQOmGFuQjqDUPIKX96EbIW683kf4/S067+1hBw4xxhzmPqnkGVEPS9hQtRHaNRwQUYNhpLfDkkQrnRMLC2pGNCA5otVZQ+QIwam+5wga8o+sU3vmu9vPDbspN1O2HASiiEaiLkyikgSoheR7EgV0XDVFIFii1yaZ0UAyuzMSXT2chKh9wRNc7VF/o=");
        skins[6].add("ewogICJ0aW1lc3RhbXAiIDogMTY1NDU0OTY4MjE4NCwKICAicHJvZmlsZUlkIiA6ICIxNDU1MDNhNDRjZmI0NzcwYmM3NWNjMTRjYjUwMDE4NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJMaWtlbHlFcmljIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI2NzMzMjI3YTRlMjlmNmIzZDdkZWY5NDVhZmQwZDRhN2RjNjQ4Mzc2NTU4Yzg5ZGI1OTY1YTRmZDg2ZjY1MDAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==");
        skins[6].add("VvrItSrELYv4N570e2etdq+91ksqnTFKIztc+Dr/l31GP6DNTaSxTlCyfRuBzRYBy62qIb7c9JiESdMKBTMOwToEFlxTHOXjPewbDaQRiM4Z2vxXVzu42OnKJ7QYewsc2ErobtWDPXH4gbwh9CRcSabPerBVcVTNdTX7Ey+B4ADaTG5rTXpzmw5aqsPgCIHfOG2gZfsL00r++3Z5UXMHP/YnlexQHRaTdWWx+UpxrKkk4nm67NB8fI4aV3JlwP+Q/t6I7ztg50k0mHHrrjXfD1gLUGzNcK0GLfvkOIF4VKKMSzqgbUX+uTgCKdz7DRVNX5sTUTBfDp918R3MhmrfxYd6+Ydb4w0wg0dFfZ1pFHVsj7knWrwlFDE7ZGxGq1fGaSLvjUkaTjvhgisRj08aF3hOGrWTHCGC8NezvRB2hD0I1NTqxaGZSRui/3lZdVHTr0w1USPuiu/+cJP9RjZVNH3uJko/lizsx2dMWp8D9XAFWwBDlFTnZAuYlXOlcUN1Cg4CkB32HgJ+/L+YvjqA0Xf8keOJgXTQkkO5hwdYPwEIPab2C+o+eRMwFRfgzXf88dKvmpAA0yamWyoyW8e6z9b3j6opgHgtL80/lspojIlr1ujs/OSYVMJZCDtui7qMwcvXll68lUmCEDxNFpkI2Lx1i07/ejIr9cPIGgqeLHw=");
        int rand = ((int)(Math.random() * 7));
        String value = skins[rand].get(0);
        String signature = skins[rand].get(1);
        data.getFile().set("npc."+npcName+".skin.value", value);
        data.getFile().set("npc."+npcName+".skin.signature", signature);
        data.saveConfig();
        npc.setSkin(value, signature);
        return npc;
    }
}
