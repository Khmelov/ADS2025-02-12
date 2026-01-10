package by.it.group451003.fedorcov.lesson13;

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

        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("5 -> 6, 6 -> 7, 5 -> 7", true).include("5 6 7");
        run("A -> B, B -> C, A -> D, D -> C", true).include("A B D C");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("0 -> 1, 0 -> 2, 0 -> 3, 1 -> 4, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("M -> N, N -> O, O -> P, M -> P", true).include("M N O P");
        run("1 -> 3, 2 -> 3, 4 -> 5, 5 -> 6, 3 -> 7", true).include("1 2 3 4 5 6 7");
        run("A -> C, B -> C, C -> D, D -> E, F -> G", true).include("A B C D E F G");
        run("START -> STEP1, START -> STEP2, STEP1 -> STEP3, STEP2 -> STEP3, STEP3 -> END", true).include("START STEP1 STEP2 STEP3 END");
        run("10 -> 20, 20 -> 30, 10 -> 40, 30 -> 50, 40 -> 50", true).include("10 20 30 40 50");
        run("J -> K, K -> L, L -> M, J -> M, K -> N, N -> M", true).include("J K L N M");
        run("ALPHA -> BETA, BETA -> GAMMA, ALPHA -> DELTA, DELTA -> GAMMA, GAMMA -> OMEGA", true).include("ALPHA BETA DELTA GAMMA OMEGA");
        run("Z -> Y, Y -> X, X -> W", true).include("Z Y X W");
        run("3 -> 1, 1 -> 2, 2 -> 4", true).include("3 1 2 4");
        run("C -> A, A -> B, C -> B", true).include("C A B");
        run("M -> K, K -> L, M -> L", true).include("M K L");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> P, P -> N", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 0", true).include("yes").exclude("no");
        run("A -> B, A -> C, B -> D, C -> D", true).include("no").exclude("yes");
        run("START -> MID, MID -> END", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("ALPHA -> BETA, BETA -> GAMMA, GAMMA -> DELTA", true).include("no").exclude("yes");
        run("P -> Q, Q -> R, R -> S", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> A, C -> D, D -> C", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("1->2, 2->1, 3->4, 4->3", true)
                .include("34\n12");
        run("A->B, B->C, C->A, D->E, E->F, F->D", true)
                .include("DEF\nABC");
        run("1->2, 2->3, 3->1, 4->5, 5->4, 3->4", true)
                .include("123\n45");
        run("X->Y, Y->Z, Z->X, A->B, B->C, C->A, Z->A", true)
                .include("XYZ\nABC");
        run("M->N, N->M, O->P, P->O, Q->R, R->Q, N->O, P->Q", true)
                .include("MN\nOP\nQR");
        run("1->2, 2->3, 3->4, 4->1, 5->6, 6->7, 7->5, 4->5", true)
                .include("1234\n567");
        run("A->B, B->A, C->D, D->C, E->F, F->E, B->C, D->E", true)
                .include("AB\nCD\nEF");
        run("1->2, 2->3, 3->1, 1->4, 4->5, 5->4", true)
                .include("123\n45");
        run("X->Y, Y->X, Y->Z, Z->W, W->Z", true)
                .include("XY\nWZ");
    }
}