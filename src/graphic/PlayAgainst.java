package graphic;

import game.Board;
import tree.Program;
import tree.TreeCumul;

public class PlayAgainst implements Runnable {
    
    private Board b;
    private Program tree;
    private final MonkeyFruits mf;
    private Boolean computer = null;
    
    public PlayAgainst(Board board, Program tree, final MonkeyFruits mf) {
        b = board;
        this.tree = tree;
        this.mf = mf;
    }
    
    public void setComputer(boolean side) {
        this.computer = side;
    }

    public void setBoard(Board board) {
        this.b = board;
    }
    
    @Override
    public synchronized void run() {
        System.out.println("run");
        while (! b.getScore().win() && ! b.getScore().lose()) {
            System.out.println("OKOKOK");
            if (b.getToMove() != computer) {
                System.out.print("");
            }
            else {
                mf.protect();
                long timeSinceThinking = System.currentTimeMillis();
                byte bestMove = tree.bestMove(b);
                b.play(bestMove);
                mf.bs.stack.push(b.copy());
                /*long sleep = 1000 - (System.currentTimeMillis() - timeSinceThinking);
                if (sleep >= 0)
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                    //    System.err.println("The computer has been interrupted");
                    }*/
                mf.actualize();
                mf.unprotect();
            }
        }
        notifyAll();
    }

}
