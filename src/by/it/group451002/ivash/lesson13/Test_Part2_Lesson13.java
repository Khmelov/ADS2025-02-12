package by.it.group451002.ivash.lesson13;

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
        run("A -> B, C -> D", true).include("A B C D");
        run("B -> C, A -> C", true).include("A B C");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> B, A -> C, B -> E, C -> E", true).include("A B C E");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true)
                .include("A B C D E F");
        run("10 -> 2, 2 -> 3", true).include("10 2 3");
        run("aa -> ab, aa -> ac", true).include("aa ab ac");
        run("A -> B, A -> B, A -> C, B -> C", true).include("A B C");
        run("K -> L, K -> M, L -> N, M -> N, A -> B", true)
                .include("A B K L M N");
        run("C -> D, B -> D, A -> D", true).include("A B C D");
        run("1 -> 2, 1 -> 3, 2 -> 4, 2 -> 5, 3 -> 6", true)
                .include("1 2 3 4 5 6");
        run("X -> Y, A -> B, C -> D", true).include("A B C D X Y");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1", true)
                .include("yes").exclude("no");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true)
                .include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 2, 3 -> 4", true)
                .include("yes").exclude("no");
        run("A -> B, C -> D, D -> C", true)
                .include("yes").exclude("no");
        run("A -> B, C -> D, E -> F", true)
                .include("no").exclude("yes");
        run("1 -> 2, 1 -> 2, 2 -> 3", true)
                .include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1, 3 -> 1", true)
                .include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true)
                .include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true)
                .include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->C, C->A", true)
                .include("ABC");
        run("1->2, 2->3, 3->4", true)
                .include("1\n2\n3\n4");
        run("A->B, B->C, C->A, C->D, D->E, E->D", true)
                .include("ABC\nDE");
        run("A->A, A->B, B->C", true)
                .include("A\nB\nC");
        run("M->N, N->O, O->M, O->P, P->Q, Q->P", true)
                .include("MNO\nPQ");
        run("1->A, A->1, A->B, B->C, C->B", true)
                .include("1A\nBC");
    }
}