package game;

public class Score {
    
    private byte myScore;
    private byte hisScore;
    
    private final byte WIN = 30;
    
    public Score() {
        myScore = 0;
        hisScore = 0;
    }
    
    public Score(Score initialScore) {
        myScore = initialScore.myScore;
        hisScore = initialScore.hisScore;
    }

    public byte getOwnScore(boolean toMove) {
        if (toMove)
            return (byte) (myScore - hisScore);
        return (byte) (hisScore - myScore);
    }
    
    public byte getScoreDiff() {
        return (byte) (myScore - hisScore);
    }
    
    public boolean win() {
        return myScore >= WIN;
    }
    
    public boolean lose() {
        return hisScore >= WIN;
    }
    
    public int getMyScore() {
        return myScore;
    }
    
    public int getHisScore() {
        return hisScore;
    }
    
    public void addToMyScore(byte points) {
        myScore += points;
    }
    
    public void addToHisScore(byte points) {
        hisScore += points;
    }
    
    public String toString() {
        String res = " ";
        if (myScore < 10)
            res += " ";
        res += myScore + "-";
        if (hisScore < 10)
            res += " ";
        res += hisScore;
        res += " ";
        return res;
    }

    public Score copy() {
        return new Score(this);
    }

    public byte getScoreDiffOrWin() {
        if (win())
            return WIN * 2;
        if (lose())
            return -WIN * 2;
        return getScoreDiff();
    }
}