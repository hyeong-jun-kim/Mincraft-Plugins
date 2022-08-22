package neo.util;

import java.util.regex.Pattern;

public class Regex {
    public static boolean checkHangulOrEnglish(String text){
        return Pattern.matches("^[ㄱ-ㅎ가-힣a-zA-Z]*$", text);
    }
}
