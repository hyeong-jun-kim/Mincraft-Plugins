package neo.entity;

import org.bukkit.entity.Player;

public class GuestBook implements Comparable<GuestBook>{
    private String author;
    private String content;
    private String timestamp;

    public GuestBook(String author, String content, String timestamp){
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    // 내림차순 (날짜 최신 순으로)
    @Override
    public int compareTo(GuestBook guestBook){
        return guestBook.timestamp.compareTo(this.timestamp);
    }


    public void show(Player p, int num){
        p.sendMessage("-------------");
        p.sendMessage("글 번호: " + (num + 1));
        p.sendMessage("글 작성자: " + author);
        p.sendMessage("내용: " + content);
        p.sendMessage("날짜: " + timestamp);
        p.sendMessage("-------------");
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
