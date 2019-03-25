package game.raminduweeraman.colour.memory.com.colourgame;

/**
 * Created by Ramindu.WEERAMAN on 3/1/2016.
 */
public class ScoreObject {

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private int highestScore ;
    private String userName ;
    private String lastUpdated;
}
