package tree;

import game.Board;
import game.Score;

public class TreeCumul implements Program{
    
    private final byte MAXDEPTH = 9;
    
    private byte m_bestMove;
    
    private int m_eval;
    
    private int m_cumul;
    
    public TreeCumul() {
        m_bestMove = -1;
        m_eval = 0;
    }
    
    public byte getBestMove() {
        return m_bestMove;
    }
    
    public int getEval() {
        return m_eval;
    }
    
    public int getCumul() {
        return m_cumul;
    }
    
    public float getDynamicEval() {
        return m_eval + (float)m_cumul / 100;
    }
    
    public byte bestMove(Board board) {
       int[] res = alphaBeta(board, 0, -100, 100, -10000, 10000);
       m_eval = res[0];
       m_cumul = res[1];
       return m_bestMove;
    }
    
    /**
     * @return 0: eval value, 1: cumul value
     */
    private int[] alphaBeta(Board b, int depth, int alpha, int beta, int alphaC, int betaC) {
        if (depth == MAXDEPTH) {
            int score = b.getScore().getScoreDiff();
            return new int[] {score, score};
        }
        int v = 0; //value
        int c = 0; //addition of values in a branch (cumul)
        if (b.getToMove()) {
            v = -20;
            c = -10000;
            byte i = 0;
            for (Board child : b.getNextBoards()) {
                Score score = child.getScore();
                
                // fin de partie
                if (score.win()) {
                    if (depth == 0)
                        m_bestMove = i;
                    //else
                        return new int[] {20, 20};
                }
                int[] res = alphaBeta(child, depth + 1, alpha, beta, alphaC, betaC);
                v = max(v, res[0]);
                if (v == res[0])
                    c = res[1] + v;
                if (v == beta) {
                    if (c >= betaC) {
                        if (depth != 0)
                            return new int[] {v, c};
                    }
                }
                if (v >= beta) {
                    if (depth != 0)
                        return new int[] {v, c};
                }
                int copy = alpha;
                alpha = max(alpha, v);
                if (alpha > copy && depth == 0) {// Si alpha est nouveau alpha et que cest le coup à jouer
                    m_bestMove = i;
                }
                i++;
            }
            
        }
        else {
            v = 20;
            c = 10000;
            byte i = 0;
            for (Board child: b.getNextBoards()) {
                
                Score score = child.getScore();
                
                // fin de partie
                if (score.lose()) {
                    if (depth == 0)
                        m_bestMove = i;
                    //else
                    //if (depth != 0)
                        return new int[] {-20, -20};
                }
                int[] res = alphaBeta(child, depth + 1, alpha, beta, alphaC, betaC);
                v = min(v, res[0]);
                if (v == res[0])
                    c = res[1] + v;
                if (v == alpha) {
                    if (alphaC >= c) {
                        if (depth == 0)
                            m_bestMove = i;
                        else
                            return new int[] {v, c};
                    }
                }
                if (alpha >= v) {
                    if (depth == 0)
                        m_bestMove = i;
                    else
                        return new int[] {v, c};
                }
                int copy = beta;
                beta = min(beta, v);
                if (beta < copy && depth == 0)
                    m_bestMove = i;
                i++;
            }
        }
        return new int[] {v, (v + c)};
    }
    
    private int min(int a, int b) {
        if (a < b)
            return a;
        return b;
    }
    
    private int max(int a, int b) {
        if (a > b)
            return a;
        return b;
    }
    
    public String toString() {
        return "Best move : " + m_bestMove + "\nEvaluation : " + m_eval + "\nDynamic : " + getDynamicEval();
    }

}
