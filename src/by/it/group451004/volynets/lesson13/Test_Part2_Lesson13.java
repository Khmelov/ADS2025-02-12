package by.it.group451004.volynets.lesson13;

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
        run("", true).include("");
        run("0", true).include("0");
        run("0 -> 1, 0 -> 2, 1 -> 3, 1 -> 4, 2 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("");
        run("3 -> 2, 2 -> 1, 1 -> 0", true).include("3 2 1 0");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("0 1 2 3 4 5");
        run("a -> b, a -> c, b -> d, c -> d", true).include("a b c d");
        run("0 -> 1, 0 -> 2, 1 -> 3, 1 -> 4, 2 -> 4, 2 -> 5, 3 -> 6, 4 -> 6, 5 -> 6", true)
                .include("0 1 2 3 4 5 6");
        run("0 -> 1, 0 -> 1, 1 -> 2, 1 -> 2, 2 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("C -> A, C -> B, A -> D, B -> D", true).include("C A B D");
        run("start -> a, start -> b, a -> end, b -> end", true).include("start a b end");
        run("5 -> 4, 4 -> 3, 3 -> 2, 2 -> 1", true).include("5 4 3 2 1");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("", true).include("no").exclude("yes");
        run("0", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 1 -> 4, 2 -> 5", true).include("no").exclude("yes");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 0, 3 -> 4, 4 -> 5", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("A -> 1, 1 -> B, B -> A", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("1, 2, 3", true).include("1\n2\n3");
        run("1->2, 2->3, 3->1, 1->3, 2->1, 3->2", true)
                .include("123");
        run("A->B, B->A, C->D, D->C", true)
                .include("AB\nCD");
        run("1->2, 2->3, 3->1, 4", true)
                .include("123\n4");
        run("1->2, 2->3, 3->4", true)
                .include("1\n2\n3\n4");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D, G->H, H->G", true)
                .include("ABC\nDEF\nGH");
        run("1->1, 2->2, 1->2", true)
                .include("1\n2");
        run("A1->B2, B2->C3, C3->A1, D4->E5, E5->F6, F6->D4", true)
                .include("A1B2C3\nD4E5F6");
        run("Z->Y, Y->X, X->Z, A->B, B->C, C->A", true)
                .include("ABC\nXYZ");
        run("1->2, 2->3, 3->2, 3->4, 4->5, 5->4, 5->6", true)
                .include("1\n23\n45\n6");
    }


}