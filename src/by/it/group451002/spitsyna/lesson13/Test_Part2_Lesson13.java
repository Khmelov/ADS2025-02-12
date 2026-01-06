package by.it.group451002.spitsyna.lesson13;

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
        run("X -> Y, X -> Z, Y -> W, Z -> W, W -> V", true).include("X Y Z W V");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 2 -> 5", true).include("0 1 2 3 4 5");
        run("S -> T, R -> T, P -> R, P -> S, Q -> P", true).include("Q P R S T");
        run("1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("1 2 3 4 5");
        run("A -> D, B -> D, C -> E, D -> F, E -> G, F -> H, G -> H, H -> I", true).include("A B C D E F G H I");
        run("0 -> 3, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5, 5 -> 6, 3 -> 7, 6 -> 8, 7 -> 8", true).include("0 1 2 3 4 5 6 7 8");
        run("X -> Y, X -> Z, Y -> A, Z -> B, A -> C, B -> C, C -> D, D -> E", true).include("X Y A Z B C D E");
        run("X -> Y, Y -> Z, Z -> A, A -> B", true).include("X Y Z A B");
        run("K -> L, L -> M, M -> N, N -> O, O -> P", true).include("K L M N O P");
        run("0 -> 2, 2 -> 5, 5 -> 7, 7 -> 9, 9 -> 10", true).include("0 2 5 7 9 10");
        run("M -> N, N -> O, O -> P, P -> Q, Q -> R, R -> S", true).include("M N O P Q R S");
        run("X -> Z, Z -> Y, Y -> W, W -> V, V -> U", true).include("X Z Y W V U");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("A -> B", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 3", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, C -> D", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> X, X -> W", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
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
        run("A->B, B->C, C->A, C->D, D->E", true)
                .include("ABC\nD\nE");
        run("1->2, 2->3, 3->4, 4->5", true)
                .include("1\n2\n3\n4\n5");
        run("P->Q, Q->R, R->P, R->S, S->T, T->S", true)
                .include("PQR\nST");
        run("M->N, N->O, O->P, P->M, P->Q, Q->R, R->Q", true)
                .include("MNOP\nQR");
        run("X->Y, Y->Z, Z->X, Z->W, W->V", true)
                .include("XYZ\nW\nV");
        run("X->Y, Y->Z, Z->X, W->V, V->U, U->W, T->S", true)
                .include("XYZ\nUVW\nT\nS");
    }


}