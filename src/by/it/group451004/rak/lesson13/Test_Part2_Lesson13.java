package by.it.group451004.rak.lesson13;

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

        run("X -> Y, Y -> Z", true).include("X Y Z");
        run("M -> N, L -> N, K -> L", true).include("K L M N");
        run("1 -> 2, 2 -> 3, 1 -> 3, 0 -> 1", true).include("0 1 2 3");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("A -> B, C -> B, B -> D, C -> D", true).include("A C B D");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, B -> E, C -> D, D -> E", true).include("A B C D E");
        run("1 -> 4, 2 -> 4, 3 -> 4, 1 -> 2, 2 -> 3", true).include("1 2 3 4");
        run("P -> Q, P -> R, Q -> S, R -> S, S -> T", true).include("P Q R S T");
        run("A -> C, B -> C, C -> D, D -> E, B -> D", true).include("A B C D E");
        run("0 -> 2, 1 -> 2, 2 -> 3, 3 -> 4, 0 -> 1", true).include("0 1 2 3 4");
        run("X -> Y, X -> Z, Y -> W, Z -> W, W -> V", true).include("X Y Z W V");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        run("1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D, D -> E", true).include("no").exclude("yes");
        run("P -> Q, Q -> R, R -> P", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        run("A->B, B->A, C->D, D->C, E->F", true)
                .include("AB\nCD\nE\nF");
        // Большой цикл
        run("A->B, B->C, C->D, D->A", true)
                .include("ABCD");
        // Линейная цепь без циклов
        run("A->B, B->C, C->D", true)
                .include("A\nB\nC\nD");
        // Большой цикл и с двумя мини-цикликами
        run("A->B, B->A, B->C, C->D, D->C, D->A", true)
                .include("ABCD");

        // Несколько одиночных + один большой цикл
        run("X->Y, Y->Z, Z->X, A->B, C->D", true)
                .include("XYZ\nA\nB\nC\nD");

        // Два цикла
        run("M->N, N->M, M->O, O->P, P->O", true)
                .include("MN\nOP");
    }
}