package neo.service;

import neo.data.DataManager;
import neo.entity.GuestBook;
import neo.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GuestBookService {
    public final int PAGE_SIZE = 6;
    public HashMap<String, DataManager> dataMap;

    public GuestBookService() {
        this.dataMap = new HashMap<>();
    }

    /**
     * OP 권한
     */
    // 방명록 목록
    public void showBoardName(Player p) {
        List<String> fileNames = DataManager.getFileNames();
        p.sendMessage(ChatColor.GOLD + "======== [방명록 목록] ========");

        for (String name : fileNames)
            p.sendMessage(name);

        p.sendMessage(ChatColor.GOLD + "===========================");
    }

    // 방명록 생성
    public void createBoard(Player p, String name) {
        if (checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "이미 존재하는 방명록입니다.");
        } else {
            DataManager dataManager = getFile(name);
            p.sendMessage(ChatColor.YELLOW + "" + name + ChatColor.GREEN + " 방명록이 생성되었습니다.");
        }
    }

    // 방명록 삭제
    public void deleteBoard(Player p, String name) {
        if (!checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "존재하지 않는 방명록입니다.");
        } else {
            DataManager dataManager = getFile(name);

            dataManager.deleteFile();
            dataMap.remove(name);
            p.sendMessage(ChatColor.YELLOW + "" + name + ChatColor.GREEN + " 방명록이 삭제되었습니다.");
        }
    }

    // 방명록 보기
    public void showGuestBook(Player p, String name, int page) {
        name = name.toLowerCase();
        if (!checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "해당 방명록 항목은 존재하지 않습니다.");
        } else {
            DataManager dataManager = getFile(name);
            FileConfiguration file = dataManager.getFile();

            ArrayList<GuestBook> guestBooks = new ArrayList<>();
            for(String author : file.getKeys(false)){
                String content = file.getString(author + ".content");
                String timestamp = file.getString(author + ".timestamp");

                guestBooks.add(new GuestBook(author, content, timestamp));
            }

            Collections.sort(guestBooks);

            // ex) 0 ~ 5, 6 ~ 10
            int start = (page - 1) * PAGE_SIZE;
            int end = page * PAGE_SIZE;

            if(page < 1 || start >= guestBooks.size()){
                p.sendMessage(ChatColor.RED + "해당 페이지에 글이 존재하지 않습니다.");
                return;
            }

            p.sendMessage(ChatColor.GREEN + "==================");
            for(int i = start; i < end; i++) {
                if(guestBooks.size() <= i){
                    break;
                }

                GuestBook guestBook = guestBooks.get(i);
                guestBook.show(p, i);
            }
            p.sendMessage(ChatColor.GREEN + "========" + ChatColor.GOLD + "[" + page + "]" + ChatColor.GREEN + "========");
        }
    }

    // 방명록 글 삭제 (번호로 삭제)
    public void deleteGuestBook(Player p, String name, int num) {
        name = name.toLowerCase();
        if (!checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "해당 방명록 항목은 존재하지 않습니다.");
        } else {
            DataManager dataManager = getFile(name);
            FileConfiguration file = dataManager.getFile();

            ArrayList<GuestBook> guestBooks = new ArrayList<>();
            for(String author : file.getKeys(false)){
                String content = file.getString(author + ".content");
                String timestamp = file.getString(author + ".timestamp");

                guestBooks.add(new GuestBook(author, content, timestamp));
            }

            if(guestBooks.size() < num){
                p.sendMessage(ChatColor.RED + "존재하지 않는 글 번호입니다. 번호를 다시 확인해주세요.");
                return;
            }
            Collections.sort(guestBooks);

            String author = guestBooks.get(num - 1).getAuthor();
            file.set(author, null);

            p.sendMessage(ChatColor.GOLD + "" + num + "번" + ChatColor.GREEN + " 방명록 글 삭제가 완료되었습니다.");
        }
    }

    /**
     * 유저 권한
     */
    // 방명록 작성
    public void createGuestBook(Player p, String name, String content) {
        name = name.toLowerCase();
        if (!checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "해당 방명록 항목은 존재하지 않습니다.");
        } else {
            DataManager dataManager = getFile(name);
            FileConfiguration file = dataManager.getFile();

            String author = p.getName();
            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String timestamp = now.format(formatter);

            file.set(author + ".content", content);
            file.set(author + ".timestamp", timestamp);
            dataManager.saveConfig();

            p.sendMessage(ChatColor.GOLD + name + ChatColor.GREEN + " 방명록에 해당 내용이 저장되었습니다.");
        }
    }

    // 방명록 삭제 (항목명으로 제거)
    public void deleteMyGuestBook(Player p, String name) {
        name = name.toLowerCase();
        if (!checkFileExist(name)) {
            p.sendMessage(ChatColor.RED + "해당 방명록 항목은 존재하지 않습니다.");
        } else {
            DataManager dataManager = getFile(name);
            FileConfiguration file = dataManager.getFile();

            for(String author : file.getKeys(false)){
                if(p.getName().equals(author)){
                    file.set(author, null);
                    p.sendMessage(ChatColor.GREEN + "방명록에 쓰신 글을 성공적으로 삭제했습니다.");
                    return;
                }
            }

            p.sendMessage(ChatColor.RED + "방명록에 쓰신 글이 존재하지 않습니다.");
        }
    }

    private DataManager getFile(String name) {
        if (dataMap.containsKey(name)) {
            return dataMap.get(name);
        } else {
            DataManager dataManager = new DataManager(Main.instance, name);
            dataMap.put(name, dataManager);
            return dataManager;
        }
    }

    private boolean checkFileExist(String name) {
        File configFile = new File(Main.instance.getDataFolder(), name + ".yml");
        if (!configFile.exists())
            return false;

        return true;
    }
}
