package tree;

import game.Board;

/**
 * This class is for reference only, do not use
 * @deprecated
 * @author frete
 *
 */
public class Tree {
    
    private final byte MAXDEPTH = 9;
    
    private byte m_bestMove;
    
    private byte m_eval;
    
    public Tree() {
        m_bestMove = -1;
        m_eval = 0;
    }
    
    public byte getEval() {
        return m_eval;
    }
    
    public byte bestMove(Board board) {
       m_eval = alphaBeta(board, 0, (byte)-20, (byte)20);
       return m_bestMove;
       
    }
    
    private byte alphaBeta(Board b, int depth, byte alpha, byte beta) {
        //System.out.println("depth = " + depth);
        byte v = 100;
        if (depth == MAXDEPTH) {
            return b.getScore().getScoreDiffOrWin();
        }
        if (b.getToMove()) {
            v = -20;
            byte i = 0;
            for (Board child : b.getNextBoards()) {
                v = max(v, alphaBeta(child, depth + 1, alpha, beta));
                if (v >= beta) {
                    if (depth == 1)
                        m_bestMove = i;
                    return v;
                }
                alpha = max(alpha, v);
                i++;
            }
            
        }
        else {
            v = 20;
            byte i = 0;
            for (Board child: b.getNextBoards()) {
                v = min(v, alphaBeta(child, depth + 1, alpha, beta));
                if (alpha >= v) {
                    if (depth == 1)
                        m_bestMove = i;
                    return v;
                }
                beta = min(beta, v);
                i++;
            }
        }
        return v;
    }
    
    private byte min(byte a, byte b) {
        if (a < b)
            return a;
        return b;
    }
    
    private byte max(byte a, byte b) {
        if (a > b)
            return a;
        return b;
    }

}
