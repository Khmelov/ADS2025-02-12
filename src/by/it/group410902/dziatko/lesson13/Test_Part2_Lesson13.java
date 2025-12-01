package by.it.group410902.dziatko.lesson13;

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
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 3", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 0 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 1, 1 -> 2, 1 -> 2", true).include("0 1 2");
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, B -> C, C -> D", true).include("A B C D");
        run("A -> C, B -> C, C -> D", true).include("A B C D");
        run("A -> B, A -> C", true).include("A B C");
        run("X -> Y, Y -> Z, Z -> W", true).include("X Y Z W");
        run("0 -> A, A -> 1, 1 -> B", true).include("0 A 1 B");
        run("A -> D, B -> D, C -> D, D -> E, B -> C", true).include("A B C D E");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 0 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");

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
        run("1->2, 2->3, 3->4", true).include("1\n2\n3\n4");
        run("1->2, 2->3, 3->4, 4->1", true).include("1234");
        run("A->B, B->A, B->C, C->D", true)
                .include("AB\nC\nD");
        run("X->Y, Y->X, Y->Z, Z->W, W->Z", true)
                .include("XY\nWZ");
        run("A->B, A->C, B->D, C->D", true)
                .include("A\nC\nB\nD");
        run("M->N, N->O, O->M, O->P, P->Q, Q->P", true)
                .include("MNO\nPQ");

        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}