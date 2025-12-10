package by.it.group451003.halubionak.lesson13;

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
        run("10 -> 20, 10 -> 30, 20 -> 40, 30 -> 40, 40 -> 50", true).include("10 20 30 40 50");
        run("K -> L, K -> M, L -> N, M -> N, N -> O", true).include("K L M N O");
        run("Alpha -> Beta, Beta -> Gamma, Gamma -> Delta", true).include("Alpha Beta Gamma Delta");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1, 4 -> 5", true).include("yes").exclude("no");
        run("P -> Q, Q -> R, R -> P, R -> S, S -> T", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("1->2, 2->3, 3->4, 4->2, 4->5, 5->6, 6->5", true)
                .include("1\n234\n56");
        run("M->N, N->O, O->M, O->P, P->Q", true)
                .include("MNO\nP\nQ");
    }

}