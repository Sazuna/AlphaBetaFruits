package graphic;

import game.Board;
import tree.Program;
import tree.TreeCumul;

public class Automatic implements Runnable {

    Board b;
    Program tree1;
    Program tree2;
    final MonkeyFruits mf;

    public Automatic(Board board, Program tree1, Program tree2, final MonkeyFruits mf ) {
        b = board;
        this.tree1 = tree1;
        this.tree2 = tree2;
        this.mf = mf;
    }

    @Override
    public synchronized void run() {
        mf.protect();

        try {
            while (! b.getScore().win() && ! b.getScore().lose()) {
                long timeSinceThinking = System.currentTimeMillis();
                byte bestmove = 0;
                if (b.getToMove())
                    bestmove = tree1.bestMove(b);
                else
                    bestmove = tree2.bestMove(b);
                b.play(bestmove);
                mf.bs.stack.push(b.copy());
                long sleep = 400 - (System.currentTimeMillis() - timeSinceThinking);
                if (sleep >= 0)
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                mf.actualize();
            }
        }catch (Exception e) {
            e.printStackTrace();
            mf.unprotect();
        }
        mf.unprotect();
    }
}

