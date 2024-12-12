package tree;

import java.util.ArrayList;

import game.Board;
import game.Score;

/**
 * Not effective : we do a new tree for each move.
 * @author frete
 *
 */
public class TreeIutShort implements Program {
    
    private final byte MAXDEPTH = 9;
    
    private byte m_bestMove;
    
    private int m_eval;
    
    private int m_cumul;
    
    public TreeIutShort() {
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
       ArrayList<Board> nextBoards = board.getNextBoardsSortedByThreats();
       int[][] allScores = new int[nextBoards.size()][2];
       int i = 0;
       for (Board b : nextBoards) {
           int[] res = alphaBeta(b, 1, -100, 100, -1000, 1000);
           allScores[i] = res;
           System.out.println("move " + (i) + " : " + res[0] + "," + res[1]*res[1]);
           i++;
       }
       bestMove(allScores, board.getToMove());
       return m_bestMove;
    }
    
    private int[] bestMove (int[][] evals, boolean toMove) {
        int[] bestMove = evals[0];
        m_bestMove = 0;
        for (byte i = 1; i < evals.length; i++) {
            if (evals[i][0] > bestMove[0] && toMove) {
                bestMove = evals[i];
                m_bestMove = i;
            }
            else if (evals[i][0] == bestMove[0] && toMove) {
                if (evals[i][1] >= bestMove[1]) {
                    bestMove = evals[i];
                    m_bestMove = i;
                }
            }
            else if (evals[i][0] < bestMove[0] && !toMove) {
                bestMove = evals[i];
                m_bestMove = i;
            }
            else if (evals[i][0] == bestMove[0] && !toMove) {
                if (evals[i][1] <= bestMove[1]) {
                    bestMove = evals[i];
                    m_bestMove = i;
                }
            }
        }
        return bestMove;
    }
    
    /**
     * @return 0: eval value, 1: cumul value
     */
    private int[] alphaBeta(Board b, int depth, int alpha, int beta, int alphaC, int betaC) {
        if (depth == MAXDEPTH) {
            int score = b.getScore().getScoreDiffOrWin();
            return new int[] {score, score};
        }
        int v = 0; //value
        int c = 0; //addition of values in a branch (cumul)
        if (b.getToMove()) {
            v = -100;
            c = -200000;
            for (Board child : b.getNextBoardsSortedByThreats()) {
                Score score = child.getScore();
                
                // fin de partie
                if (score.win()) {
                    return new int[] {20, 20};
                }
                int[] res = alphaBeta(child, depth + 1, alpha, beta, alphaC, betaC);
                v = max(v, res[0]);
                c = res[1] + v;
                if (v == beta) {
                    if (betaC <= c) {
                            return new int[] {v, c};
                    }
                }
                if (v >= beta) {
                        return new int[] {v, c};
                }
                alpha = max(alpha, v);
                alphaC = max(alphaC, c);
            }       
        }
        else {
            v = 100;
            c = 200000;
            for (Board child: b.getNextBoardsSortedByThreats()) {
                
                Score score = child.getScore();
                if (score.lose()) {
                    return new int[] {-20, -20};
                }
                int[] res = alphaBeta(child, depth + 1, alpha, beta, alphaC, betaC);
                v = min(v, res[0]);
                c = res[1] + v;
                if (v == alpha) {
                    if (alphaC >= c) {
                            return new int[] {v, c};
                    }
                }
                if (alpha > v) {
                        return new int[] {v, c};
                }
                beta = min(beta, v);
                betaC = min(betaC, c);
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