package by.it.group410901.garkusha.lesson13;

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
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).

        run("5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8", true).include("5 6 7 8");
        run("X -> Y, Y -> Z", true).include("X Y Z");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> C, B -> C, C -> D", true).include("A B C D");
        run("P -> Q, P -> R, Q -> S, R -> S", true).include("P Q R S");
        run("10 -> 20, 10 -> 30, 20 -> 40, 30 -> 40", true).include("10 20 30 40");
        run("M -> N, N -> O, O -> P", true).include("M N O P");
        run("K -> L, K -> M, L -> N, M -> N, N -> O", true).include("K L M N O");
        run("A -> B, B -> C, A -> D, D -> C", true).include("A B D C");
        run("1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("1 2 3 4 5 6");
        run("X -> Z, Y -> Z, Z -> W", true).include("X Y Z W");
        run("Alpha -> Beta, Beta -> Gamma", true).include("Alpha Beta Gamma");
        run("Start -> Middle, Middle -> End", true).include("Start Middle End");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("P -> Q, Q -> R, R -> P", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> M", true).include("yes").exclude("no");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("Start -> Middle, Middle -> End", true).include("no").exclude("yes");
        run("P -> Q, Q -> R, R -> S", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("Alpha -> Beta, Beta -> Gamma, Gamma -> Delta", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).

        run("A->B, B->C, C->A", true).include("ABC");
        run("1->2, 2->3, 3->4, 4->2", true).include("1\n234");
        run("X->Y, Y->Z, Z->X, Z->W", true).include("XYZ\nW");
        run("P->Q, Q->R, R->P, R->S, S->T, T->S", true).include("PQR\nST");
        run("A->B, B->C, C->D, D->E, E->C", true).include("A\nB\nCDE");
        run("1->2, 2->3, 3->1, 1->4, 4->5, 5->6, 6->4", true).include("123\n456");
        run("M->N, N->O, O->M, O->P, P->Q, Q->P", true).include("MNO\nPQ");
        run("Start->A, A->B, B->C, C->A, C->End", true).include("Start\nABC\nEnd");
        run("X1->X2, X2->X3, X3->X1, X3->X4, X4->X5, X5->X6, X6->X4, X6->X7", true)
                .include("X1X2X3\nX4X5X6\nX7");
        run("Alpha->Beta, Beta->Gamma, Gamma->Alpha, Gamma->Delta, Delta->Epsilon", true)
                .include("AlphaBetaGamma\nDelta\nEpsilon");
        run("P1->P2, P2->P3, P3->P1, P3->P4, P4->P5, P5->P6, P6->P4, P4->P7", true)
                .include("P1P2P3\nP4P5P6\nP7");
    }


}