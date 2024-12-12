package tools;

import game.Board;
import tree.Program;
import tree.TC2;
import tree.TreeCumul;

public class TwoPrograms {

    public static void main (String[] args)
    {

        int nbGames = 1;

        Program p1 = new TC2();

        Program p2 = new TreeCumul();

        int v1 = 0;

        int v2 = 0;

        Board b = new Board((byte)9);

        for (int i = 0; i < nbGames; i++) {
            while (! b.getScore().win() && !b.getScore().lose()) {
                System.out.println("nouveau tour");
                b.play(p1.bestMove(b));
                b.play(p2.bestMove(b));
            }
            boolean win = false;
            if (b.getScore().win()) {
                System.out.println("program 1 won");
                win = true;
            }

            else {
                System.out.println("program 2 won");
            }
            if (win && i % 2 == 0)
                v1 ++;
            else
                v2 ++;
        }
    }
}