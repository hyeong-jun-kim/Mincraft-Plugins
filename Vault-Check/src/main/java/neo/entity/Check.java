package neo.entity;

/**
 * 수표 엔티티
 */
public class Check {
    private String UUID;
    private String playerName;
    private double amount;

    public Check(String UUID, String playerName, double amount){
        this.UUID = UUID;
        this.playerName = playerName;
        this.amount = amount;
    }
}
