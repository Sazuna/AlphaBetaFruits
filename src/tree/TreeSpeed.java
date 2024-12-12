package tree;

import java.util.ArrayList;

import game.Board;
import game.Score;

/**
 * Not effective : we do a new tree for each move.
 * @author frete
 *
 */
public class TreeSpeed implements Program {

    private final byte MAXDEPTH = 9;

    private byte m_bestMove;

    private int m_eval;

    public TreeSpeed() {
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
        return 0;
    }

    public float getDynamicEval() {
        return m_eval;
    }

    public byte bestMove(Board board) {
        ArrayList<Board> nextBoards = board.getNextBoardsSortedByThreats();
        byte bestMove = 0;
        byte currentMove = 0;

        int alpha = -100, beta = 100;

        if (board.getToMove()) {
            System.out.println("to move = false");

            for (Board child : nextBoards) {
                Score score = child.getScore();
                currentMove = child.getVarIndex();

                // fin de partie
                if (score.win()) {
                    m_bestMove = currentMove;
                    return currentMove;
                }

                int res = this.alphaBeta(child, 1, alpha, beta);

                System.out.println(currentMove + " : " + res +"\n alpha = "+alpha);

                if (res > alpha)
                {
                    alpha = res;
                    bestMove = currentMove;
                }
            }
        }
        else
        {
            for (Board child : nextBoards) {
                Score score = child.getScore();
                currentMove = child.getVarIndex();

                // fin de partie
                if (score.lose()) {
                    m_bestMove = currentMove;
                    return currentMove;
                }

                int res = this.alphaBeta(child, 1, alpha, beta);

                System.out.println(currentMove + " : " + res +"\n beta = "+beta);

                if (res < beta)
                {
                    bestMove = currentMove;
                    beta = res;
                }
            }
        }
        m_bestMove = bestMove;
        return bestMove;
    }

    /*private int bestMove (int[] allScores, boolean toMove) {
        int bestMove = allScores[0];
        m_bestMove = 0;
        for (byte i = 1; i < allScores.length; i++) {
            if (allScores[i] > bestMove && toMove) {
                bestMove = allScores[i];
                m_bestMove = i;
            }
            else if (allScores[i] < bestMove && !toMove) {
                bestMove = allScores[i];
                m_bestMove = i;
            }
        }
        return bestMove;
    }*/

    /**
     * @return the evaluation.
     */
    private int alphaBeta(Board b, int depth, int alpha, int beta) {
        if (depth == MAXDEPTH) {
            int score = b.getScore().getScoreDiffOrWin();
            return score;
        }
        int v = 0; //value
        if (b.getToMove()) {
            v = -100;
            for (Board child : b.getNextBoardsSortedByThreats()) {
                Score score = child.getScore();

                // fin de partie
                if (score.win()) {
                    return score.getScoreDiffOrWin();
                }
                int res = alphaBeta(child, depth + 1, alpha, beta);
                v = max(v, res);
                if (v >= beta) {
                    return v;
                }
                alpha = max(alpha, v);
            }       
        }
        else {
            v = 100;
            for (Board child: b.getNextBoardsSortedByThreats()) {

                Score score = child.getScore();
                if (score.lose()) {
                    return score.getScoreDiffOrWin();
                }
                int res = alphaBeta(child, depth + 1, alpha, beta);
                v = min(v, res);
                if (alpha > v) {
                    return v;
                }
                beta = min(beta, v);
            }
        }
        return v;
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