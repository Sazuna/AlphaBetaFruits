package game;

public class BoardDecorative {

    private Board board;
    
    private byte amountOfNeighbours;
    
    public BoardDecorative(Board board, byte amountOfNeighbours) {
        this.board = board;
        this.amountOfNeighbours = amountOfNeighbours;
    }

    public byte getAmountOfNeighbours()
    {
        return amountOfNeighbours;
    }
    
    public void setAmountOfNeighbours(byte amount)
    {
        this.amountOfNeighbours = amount;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    
}
