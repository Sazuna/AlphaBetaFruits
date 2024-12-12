package game;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Board {

    public final byte HEIGHT = 8;
    public final byte WIDTH = 9;
    public byte NEXTS = 9;
    private final byte NBFRUITS = 4;

    private boolean PROGRAMMODE;

    private byte[][] position;

    private byte[] nexts;

    private Score score;

    private boolean toMove;
    
    private byte varIndex;

    public Board(boolean programMode,  byte nextSize) {
        score = new Score();
        NEXTS = nextSize;
        nexts = new byte[nextSize];
        position = new byte[HEIGHT][WIDTH];
        for (byte i = 0; i < HEIGHT; i++)
            for (byte j = 0; j < WIDTH; j++)
                position[i][j] = -1;
        toMove = true;
        PROGRAMMODE = programMode;
    }
    public Board(byte nextSize) {
        score = new Score();
        nexts = new byte[nextSize];
        NEXTS = nextSize;
        position = new byte[HEIGHT][WIDTH];
        for (byte i = 0; i < HEIGHT; i++)
            for (byte j = 0; j < WIDTH; j++)
                position[i][j] = -1;
        toMove = true;
        PROGRAMMODE = false;
    }

    public void aleaNexts() {
        for (byte i = 0; i < NEXTS ; i++) {
            nexts[i] = (byte)(Math.random() * 4);
        }
    }

    public void insertNexts() {
        for (byte i = 0; i < NEXTS; i++) {
            Scanner sc = new Scanner(System.in);
            System.out.println("New fruit : ");
            nexts[i] = sc.nextByte();
        }
    }

    public void setNexts(byte[] nexts) {
        for (byte i = 0; i < NEXTS ; i++) {
            this.nexts[i] = nexts[i];
        }
    }

    public void setLast(byte i) {
        nexts[nexts.length - 1] = i;
    }

    public byte[][] getPosition() {
        return position;
    }
    
    public ArrayList<Board> getNextBoards() {
        ArrayList<Board> nextBoards = new ArrayList<>();
        Byte[] possibleMoves = getPossibleMoves();
        for (Byte b : possibleMoves) {
            Board copy = copy();
            copy.playAt(b);
            nextBoards.add(copy);
        }
        return nextBoards;
    }
    
    public ArrayList<Board> getNextBoardsSortedByThreats() {
        return sort(getPossibleMoves());
    }

    public void setPosition(byte[][] position) {
        for (byte i = 0; i < HEIGHT; i++)
            for (byte j = 0 ; j < WIDTH; j++)
                this.position[i][j] = position[i][j];
        //	this.position = position;
    }

    public byte[] getNexts() {
        return nexts;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public boolean getToMove() {
        return toMove;
    }
    
    public void setVar(byte i)
    {
        varIndex = i;
    }
    
    public byte getVarIndex() {
        return varIndex;
    }

    public Board copy() {
        Board copy = new Board(this.NEXTS);
        copy.PROGRAMMODE = false;
        copy.setNexts(nexts);
        for (byte i = 0; i < HEIGHT; i++)
            for (byte j = 0 ; j < WIDTH; j++)
                copy.position[i][j] = position[i][j];
        copy.setPosition(position);
        copy.setScore(score.copy());
        copy.toMove = toMove;
        return copy;
    }

    /**
     * 
     * @param moveNumber The index in the possible move's list
     * @return
     */
    public boolean play(byte moveNumber) {
        Byte[] pm = getPossibleMoves();
        byte column = pm[moveNumber];
        playAt(column, nexts[0]);
        fall();
        shiftNexts();
        return false;
    }
    public boolean playAt(int column) {
        playAt((byte)column, nexts[0]);
        fall();
        shiftNexts();
        return false;
    }

    public boolean play(byte column, byte fruit) {
        playAt(column, fruit);
        fall();
        shiftNexts();
        if (toMove)
            return score.win();
        return score.lose();
    }

    /**
     * Considering that the specified column is right
     * @param column
     */
    private void playAt(byte column, byte fruit) {
        boolean flag = false;
        for (byte i = 0; i < HEIGHT && ! flag; i++) {
            if (position[i][column] == -1) {
                flag = true;
                position[i][column] = fruit;
            }
        }
        //Change player
        toMove = !toMove;
    }

    private void shiftNexts() {
        for (byte i = 0; i < NEXTS - 1; i ++) {
            nexts[i] = nexts[i + 1];
        }
        if (PROGRAMMODE) {
            Scanner sc = new Scanner(System.in);
            System.out.println("New fruit : ");
            nexts[NEXTS - 1] = sc.nextByte();
        }
        else {
            nexts[NEXTS-1] = (byte)(Math.random()* NBFRUITS);
        }
    }

    public void fall() {
        boolean [][] scratch = fruitsToScratch();
        byte scratchSize = scratch(scratch);
        while (scratchSize > 0)
        {
           // System.out.println("scratching " + scratchSize + " fruits");
            //Cascade falling
            cascadeFalling();
            //Score adding
            addScore(scratchSize);
            scratch = fruitsToScratch();
            scratchSize = scratch(scratch);
        }
    }
    
    public void fallPop() {
        cascadeFalling();
        fall();
    }

    private void cascadeFalling() {
        for (byte i = 0; i < WIDTH; i++) {
            byte [] col = new byte[HEIGHT];
            do {
                for (byte j = 0; j < HEIGHT; j++) {
                    col [j] = position[j][i];
                }
                for (byte height = HEIGHT - 2; height >= 0; height--) {
                    if (col[height + 1] != -1 && col[height] == -1) {
                        position[height][i] = position[height + 1][i]; 
                        position[height + 1][i] = -1;
                    }
                }
            }while (! isBottom(col));
        }
    }
    
    /**
     * 
     * @return the columns where it is possible to play
     */
    public Byte[] getPossibleMoves() {
        ArrayList<Byte> possibleMoves = new ArrayList<Byte>();
        for (byte i = 0; i < WIDTH; i++)
            if (this.position[HEIGHT - 1][i] == -1)
                possibleMoves.add(i);
        return possibleMoves.toArray(new Byte[possibleMoves.size()]);
    }

    private boolean isBottom(byte[] column) {
        boolean sawFruit = false;
        for (byte i = HEIGHT - 1; i >= 0; i--) {
            if (sawFruit && column[i] == -1)
                return false;
            if (column[i] != -1)
                sawFruit = true;
        }
        return true;
    }

    private void addScore(byte score) {
        if (toMove)
            this.score.addToHisScore(score);
        else
            this.score.addToMyScore(score);
    }

    /**
     * 
     * @return a boolean tab of the fruits to scratch
     */
    private boolean[][] fruitsToScratch() {
        boolean[][] scratched = new boolean[HEIGHT][WIDTH];
        ArrayList<ArrayList<byte[]>> allGroups = new ArrayList<>();
        for (byte i = 0; i < HEIGHT; i++) {
            for (byte j = 0; j < WIDTH; j++) {
                byte currentFruit = position[i][j];

                while (j < WIDTH && i < HEIGHT && (currentFruit == -1  || containsFruit(allGroups, new byte[] {i, j}))){
                    j++;
                    if (j == WIDTH) {
                        j = 0;
                        i++;
                        if (i == HEIGHT) {
                            for (ArrayList<byte[]> a : allGroups)
                                if (a.size() >= 3)
                                    for (byte[] b : a)
                                        scratched[b[0]][b[1]] = true;
                            return scratched;
                        }
                    }
                    currentFruit = position[i][j];
                }
                byte cpt = 0;
                ArrayList<byte[]> currentGroup = new ArrayList<>();
                allGroups.add(currentGroup);
                currentGroup.add(new byte[] {i, j});
                while(addNeighbours(currentGroup, currentFruit, cpt)) { cpt++; }
            }
        }

        for (ArrayList<byte[]> a : allGroups)
            if (a.size() >= 3)
                for (byte[] b : a)
                    scratched[b[0]][b[1]] = true;
        return scratched;
    }

    private boolean addNeighbours(ArrayList<byte[]> currentGroup, byte color, byte current) {
        if (color == -1) {
            System.err.println("addNeighbours : no fruit to test");
            return false;
        }
        boolean res = false;
        for (byte i = -1; i <= 1; i++) {
            for (byte j = -1; j <= 1; j ++) {
                if (j == 0 && i == 0 || j != 0 && i != 0) {
                    j++;
                    if (j == 2) {
                        i++;
                        j = 0;
                        if (i == 2)
                            break;
                    }
                }
                byte[] currentFruit = currentGroup.get(current);
                if (currentFruit[0] + i >= 0 && currentFruit[0] + i < HEIGHT) {
                    if (currentFruit[1] + j >= 0 && currentFruit[1] + j < WIDTH) {
                        byte[] neighbour = new byte[] {(byte) (currentFruit[0] + i), (byte) (currentFruit[1] + j)};
                        if (position[neighbour[0]][neighbour[1]] == color) {
                            if (! contains(currentGroup, neighbour)) {
                                res = true;
                                currentGroup.add(neighbour);
                            }
                        }
                    }
                }
            }
        }
        return res;
    }
    private boolean contains(ArrayList<byte[]> currentGroup, byte[] neighbour) {
        for (byte[] a: currentGroup) {
            if (a[0] == neighbour[0] && a[1] == neighbour[1]) return true;
        }
        return false;
    }
    private boolean containsFruit(ArrayList<ArrayList<byte[]>> currentGroup, byte[] fruit) {
        for (ArrayList<byte[]> a: currentGroup)
            for (byte[] b : a)
                if (b[0] == fruit[0] && b[1] == fruit[1]) return true;
        return false;
    }

    private byte scratch(boolean[][] scratched) {
        byte res = 0;
        for (byte i = 0; i < scratched.length; i++) {
            for (byte j = 0; j < scratched[0].length; j++) {
                if (scratched[i][j]) {
                    res += 1;
                    position[i][j] = -1;
                }
            }
        }

        return res;
    }

    public String toString() {
        String res = "";
        res += "to move : " + toMove + "\n";
        for (byte i = HEIGHT - 1; i >= 0; i--) {
            res += "| ";
            for (byte j = 0; j < WIDTH; j++) {
                if (position[i][j] == -1)
                    res += " .";
                else
                    res += " " + position[i][j];
            }
            res += " |\n";
        }
        res += "  ";
        for (byte i = 0; i < WIDTH; i++)
            res += " " + i;
        res += "\n";
        for (byte i = 0; i < nexts.length; i++)
            res += "__";
        res += "\n";
        for (byte i = 0; i < nexts.length; i++)
            res += " " + nexts[i];
        res += "\n";
        for (byte i = 0; i < nexts.length; i++)
            res += "__";
        res += "\n";
        res += score.toString() + "\n";
        return res;
    }
    public void garbage() {
        this.score = null;
        this.nexts = null;
        this.position = null;
    }
    public void pop(Byte fruit, Byte x, Byte y) {
        position[y][x] = fruit;
    }
    public void popNext(Byte fruit, Byte x) {
        this.nexts[x] = fruit;
    }
    
    /**
     * Sort the possible moves in a priority order of direct threats
     * @param moves
     * @return
     */
    public ArrayList<Board> sort(Byte[] moves) {
        ArrayList<BoardDecorative> sorted = new ArrayList<>();
        for (byte i = 0; i < moves.length; i++) {
            Board copy = this.copy();
            byte amountOfNeighbours = copy.amountOfNeighbours(i);
            copy.play(i);
            copy.setVar(i);
            BoardDecorative bd = new BoardDecorative(copy, amountOfNeighbours);
            sorted.add(bd);
        }
        sorted.sort(new Comparator <BoardDecorative>() {
            @Override
            public int compare(BoardDecorative b1, BoardDecorative b2) {
                int diff = 0;
                diff = b2.getBoard().getScore().getScoreDiffOrWin() - b1.getBoard().getScore().getScoreDiffOrWin();
                if (!toMove)
                {
                    diff = -diff;
                }
                if (diff == 0)
                {
                    diff = b2.getAmountOfNeighbours() - b1.getAmountOfNeighbours();
                }

                return diff;
            }
        });
        ArrayList<Board> result = new ArrayList<Board>();
        for (BoardDecorative bd : sorted)
        {
            result.add(bd.getBoard());
        }
        return result;
    }
    
    public byte amountOfNeighbours(byte move)
    {
        int heightOfMove = 0;
        for (int j = 0; j < HEIGHT; j++)
        {
            if (position[j][move] != -1)
            {
                heightOfMove ++;
            }
        }
        
        byte amountOfNeighbours = 0;
        for (int i = -1 + move; i <= 1 + move; i++)
        {
            for (int j = -1 + heightOfMove; j <= 1 + heightOfMove; j++)
            {
                if ((i != move || j == -1 + heightOfMove) && j >= 0 && j < HEIGHT)
                {
                    if (i >= 0 && i < WIDTH)
                    {
                        if (position[j][i] != -1)
                            amountOfNeighbours ++;
                    }
                }
            }
        }
        return amountOfNeighbours;
    }
    
}