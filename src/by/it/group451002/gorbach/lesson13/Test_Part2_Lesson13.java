package by.it.group451002.gorbach.lesson13;

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

        // ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ:
        run("5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8", true).include("5 6 7 8");
        run("X -> Y, Y -> Z, Z -> W", true).include("X Y Z W");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("M -> N, O -> P, N -> P, M -> O", true).include("M N O P");
        run("1 -> 2, 2 -> 3, 3 -> 4, 1 -> 3, 2 -> 4", true).include("1 2 3 4");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("Alpha -> Beta, Beta -> Gamma, Alpha -> Gamma", true).include("Alpha Beta Gamma");
        run("1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("1 2 3 4 5");
        run("Start -> Middle, Middle -> End", true).include("Start Middle End");
        run("P -> Q, Q -> R, R -> S, P -> R, Q -> S", true).include("P Q R S");
        run("A -> C, B -> C, C -> D, C -> E", true).include("A B C D E");
        run("One -> Two, Two -> Three, One -> Three", true).include("One Two Three");
        run("A -> B, C -> D, B -> D", true).include("A B C D");
        run("X1 -> X2, X2 -> X3, X3 -> X4, X1 -> X3, X2 -> X4", true).include("X1 X2 X3 X4");
        run("K -> L, L -> M, M -> N, K -> M, L -> N", true).include("K L M N");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        // ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ:
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> X, Z -> W", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1, 3 -> 4, 4 -> 5", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E", true).include("no").exclude("yes");
        run("P -> Q, Q -> R, R -> P, R -> S, S -> T", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> P", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 3", true).include("yes").exclude("no");
        run("Alpha -> Beta, Beta -> Gamma, Gamma -> Delta", true).include("no").exclude("yes");
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

        // ДОПОЛНИТЕЛЬНЫЕ ТЕСТЫ:
        run("A->B, B->A, B->C, C->D, D->C", true).include("AB\nCD");
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4, 6->7, 7->8, 8->7", true)
                .include("123\n456\n78");
        run("P->Q, Q->R, R->P, R->S, S->T, T->S", true).include("PQR\nST");
        run("X->Y, Y->Z, Z->X, Z->W, W->V, V->W", true).include("XYZ\nVW");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D", true).include("ABC\nDEF");
        run("1->2, 2->1, 2->3, 3->4, 4->3, 4->5, 5->6, 6->5", true)
                .include("12\n34\n56");
        run("M->N, N->O, O->M, O->P, P->Q, Q->P, Q->R", true).include("MNO\nPQ\nR");
        run("Alpha->Beta, Beta->Gamma, Gamma->Alpha, Gamma->Delta, Delta->Epsilon, Epsilon->Delta", true)
                .include("AlphaBetaGamma\nDeltaEpsilon");
    }


}