package neo.entity;

import java.util.List;

public class Board {
    private int page;
    private String name;
    private List<GuestBook> guestBooks;

    public Board(String name){
        this.name = name;
    }

    public Board(int page, String name){
        this.page = page;
        this.name = name;

        // TODO 게스트 북 추가하는 로직 넣기
    }
}
