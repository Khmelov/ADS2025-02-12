package by.it.group410901.kliaus.lesson13;

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
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("A B C D E F");
        run("0 -> 2, 1 -> 2, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, A -> D, A -> E", true).include("A B C D E");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 0 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("A -> B, B -> C, A -> D, D -> C, C -> E", true).include("A B D C E");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("0 2 4 1 3 5");
        run("0 -> 1, 0 -> 2, 1 -> 3, 1 -> 4, 2 -> 4, 2 -> 5, 3 -> 6, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("A -> B, B -> C, C -> D, A -> E, E -> D", true).include("A B E C D");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 0", true).include("yes").exclude("no");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("0->1, 1->2, 2->0", true).include("012");
        run("0->1, 1->0, 2->3, 3->2", true).include("23\n01");
        run("0->1, 1->0, 2->3, 3->4, 4->2, 5->6", true).include("5\n6\n234\n01");
        run("0->1, 1->0, 0->2, 2->3, 3->2, 2->4", true).include("01\n23\n4");
        run("A->B, B->C, C->A, C->D, D->E, E->D, D->F", true).include("ABC\nDE\nF");
    }
}