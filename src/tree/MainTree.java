package tree;

import java.util.Scanner;

import game.Board;

public class MainTree {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        System.out.println("1: Player vs IA\n2: AI vs AI");
        String ans = "";
        do {
            ans = sc.nextLine();
        }while (!ans.equals("1") && !ans.equals("2"));
        

        boolean p2ai = false;
        if (ans.equals("2")) {
            p2ai = true;
        }
        
        Board board = new Board(false, (byte) 9);
        
        board.aleaNexts();
        
        boolean end = false;
        boolean iWin = false;
        boolean iLose = false;
        while (! end) {
            //display player
            System.out.println("\n\n\n\n\nplayer " + board.getToMove() + "\n\n\n\n\n");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //display board
            System.out.println(board);
            //ask the move
            byte move = -1;
            if (p2ai && !board.getToMove()) {
                Tree t = new Tree();
                move = t.bestMove(board);
                board.play(move);
            }
            else {
                do {
                    System.out.println("Choose a column where to move : ");
                    move = sc.nextByte();
                } while (move < 0 || move >= board.WIDTH);
                board.playAt(move);
            }
            //play the move
            //check if the game is finished
            iWin = board.getScore().win();
            iLose = board.getScore().lose();
            end = iWin || iLose;
        }
        //display score
        System.out.println(board.getScore());
    }
}
