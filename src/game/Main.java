package game;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board board = new Board(false, (byte) 5); //Non program mode
        
        //init stack
 //       board.aleaNexts();
        board.setNexts(new byte[] { 0, 0, 0, 0, 0} );
        
        boolean end = false;
        boolean iWin = false;
        boolean iLose = false;
        Scanner sc = new Scanner(System.in);
        while (! end) {
            //display board
            System.out.println(board);
            //display score
            System.out.println(board.getScore());
            //display player
            System.out.println("player " + board.getToMove());
            //ask the move
            byte move;
            do {
                System.out.println("Choose a column where to move : ");
                move = sc.nextByte();
            } while (move < 0 || move >= board.WIDTH);
            //play the move
            board.play(move);
            //check if the game is finished
            iWin = board.getScore().win();
            iLose = board.getScore().lose();
            end = iWin || iLose;
        }
        //display score
        System.out.println(board.getScore());
    }

}