package tree;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.Board;

class TreeTest {

    /*@Test
    void test() {
        Board b = new Board((byte)9);
        Tree t = new Tree();
        b.aleaNexts();
        //b.setNexts(new byte[] { 2, 0, 1, 0, 2, 0, 3, 2, 1});
        System.out.println(b);
        System.out.println(t.bestMove(b));
        System.out.println(t.getEval());
        assertFalse(true);
    }*/
    
    /*@Test
    void cumulTest() {
        Board b = new Board((byte)9);
        TreeCumul t = new TreeCumul();
        b.aleaNexts();
        System.out.println(b);
        System.out.println("best move : " + t.bestMove(b));
        System.out.println("eval      : " + t.getEval());
        System.out.println("cumul     : " + t.getCumul());
        System.out.println("dynamic eval : " + t.getDynamicEval());
        assertFalse(true);
    }*/
    
    @Test
    void choiceTest() {
        Board b = new Board((byte) 8);
        b.setNexts(new byte[] {0, 0, 0, 0, 1, 1, 1, 0});
        b.playAt(1);
        b.playAt(2);
        b.playAt(4);
        b.playAt(5);
        b.playAt(6);
        b.playAt(7);
        System.out.println(b);
        Program t = new TreeSpeed();
        assertEquals(t.bestMove(b), 3);
    }
    
    /*
    @Test
    void debug() {
        Board b = new Board((byte)9);
        TreeCumul t = new TreeCumul();
        b.setNexts(new byte[] {0, 2, 0, 2, 0, 0, 2, 0, 2});
        System.out.println(b);
        System.out.println("best move : " + t.bestMove(b));
        System.out.println("eval      : " + t.getEval());
        System.out.println("cumul     : " + t.getCumul());
        System.out.println("dynamic eval : " + t.getDynamicEval());
        assertTrue(t.getBestMove() != -1);
    }*/
}
