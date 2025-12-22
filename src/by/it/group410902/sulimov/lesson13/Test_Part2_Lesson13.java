package by.it.group410902.sulimov.lesson13;

import by.it.HomeWork;
import org.junit.Test;

@SuppressWarnings("NewClassNamingConvention")
public class Test_Part2_Lesson13 extends HomeWork {

    @Test
    public void testGraphA() {
        run("0 -> 1", true).include("0 1");
        run("0 -> 1, 1 -> 2", true).include("0 1 2");
        run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");

        // Additional tests for GraphA
        run("5 -> 4, 5 -> 3, 4 -> 2, 3 -> 2, 2 -> 1", true).include("5 3 4 2 1");
        run("A -> C, A -> B, C -> D, B -> D", true).include("A B C D");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("F -> E, E -> D, D -> C, C -> B, B -> A", true).include("F E D C B A");
        run("0 -> 1, 0 -> 2, 0 -> 3, 0 -> 4", true).include("0 1 2 3 4");
        run("1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0", true).include("1 2 3 4 0");
        run("0 -> 1, 1 -> 2, 0 -> 3, 3 -> 2", true).include("0 1 3 2");
        run("0 -> 1, 0 -> 3, 1 -> 2, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true).include("A B C D E F");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        // Additional tests for GraphB
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 0 -> 3", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 0 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 1", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        // Additional tests for GraphC
        run("A->B, B->A", true).include("AB");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, B->C, C->D, D->E", true).include("A\nB\nC\nD\nE");
        run("A->B, B->C, C->D, D->E, E->A", true).include("ABCDE");
        run("A->B, B->C, C->A, C->D, D->E, E->D", true).include("ABC\nDE");
        run("A->B, B->A, B->C, C->D, D->C", true).include("AB\nCD");
    }
}