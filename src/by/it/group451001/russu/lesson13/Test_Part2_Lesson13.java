package by.it.group451001.russu.lesson13;

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
        run("M -> N, M -> O, N -> P, O -> P", true).include("M N O P");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8, 8 -> 9", true).include("5 6 7 8 9");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("K -> L, K -> M, L -> N, M -> N, N -> O", true).include("K L M N O");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("0 1 2 3 4 5");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("Q -> R, Q -> S, R -> T, S -> T, T -> U", true).include("Q R S T U");
        run("0 -> 2, 0 -> 3, 1 -> 3, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, B -> C, A -> D, D -> E, C -> F, E -> F", true).include("A B C D E F");
        run("1 -> 2, 2 -> 5, 1 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> W", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> P, P -> Q", true).include("no").exclude("yes");
        run("M -> N, N -> O, O -> M", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");


        run("1->2, 2->3, 3->4, 4->5", true)
                .include("1\n2\n3\n4\n5");
        run("0->1, 1->2, 2->3, 3->0, 4->5", true)
                .include("4\n0123\n5");
        run("X->Y, Y->Z, Z->X, Z->W, W->V, V->W", true)
                .include("XYZ\nVW");
        run("M->N, N->O, O->P, P->M, Q->R, R->S, S->Q", true)
                .include("QRS\nMNOP");
        run("1->2, 2->3, 3->4, 4->5", true)
                .include("1\n2\n3\n4\n5");
        run("A->B, B->C, C->D, D->E, E->A", true)
                .include("ABCDE");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}