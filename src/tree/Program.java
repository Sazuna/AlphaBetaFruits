package tree;

import game.Board;

public interface Program {
    
    public byte bestMove(Board b);
    
    public byte getBestMove();
    
    public int getEval();
    
    public float getDynamicEval();

}
