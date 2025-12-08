package by.it.group410902.yarmashuk.lesson13;

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
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> C, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("0 -> 1, 0 -> 2, 0 -> 3, 1 -> 4, 2 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("1 -> A, A -> 2, 2 -> B, B -> 3", true).include("1 A 2 B 3");
        run("1 -> 10, 1 -> 2, 10 -> 11, 2 -> 11, 11 -> 3", true).include("1 10 2 11 3");
        run("START -> A, START -> B, A -> C, B -> D, C -> END, D -> END", true).include("START A B C D END");
        run("Source1 -> MidA, Source2 -> MidA, MidA -> Final1, MidA -> Final2", true).include("Source1 Source2 MidA Final1 Final2");
        run("0 -> 2, 1 -> 2, 2 -> 3, 2 -> 4", true).include("0 1 2 3 4");
        run("2 -> 4, 3 -> 4, 1 -> 2, 0 -> 1, 4 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6, 6 -> 7", true).include("0 1 2 3 4 5 6 7");

        run("1 -> 10, 1 -> 2, 10 -> 11, 2 -> 11, 11 -> 3", true).include("1 10 2 11 3");
        run("0 -> 1, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5, 5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8", true).include("0 1 2 3 4 5 6 7 8");
    }



    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("start -> A, start -> B, A -> end, B -> end", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("X -> Y, Y -> Z, Z -> Y", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0, A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2, 0 -> 1", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> P, P -> M", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6, 6 -> 7", true).include("no").exclude("yes");
        run("1 -> 10, 1 -> 2, 10 -> 11, 2 -> 11", true).include("no").exclude("yes");
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

        run("A->B, B->C, X->Y, Y->Z", true)
                .include("X\nY\nZ\nA\nB\nC");
        run("1->2, 2->3, 3->1, 3->4, 4->5", true)
                .include("123\n4\n5");
        run("1->2, 2->3, 3->4, 4->2, 0->1, 5->3", true)
                .include("5\n0\n1\n234");
        run("0->10, 0->2, 10->1, 1->2, 2->10", true)
                .include("0\n1102");
        run("0->1, 1->2, 2->0, 3->4, 4->5, 5->3, 0->3", true)
                .include("012\n345");
        run("1->A, A->2, 2->B, B->1", true)
                .include("12AB");
    }


}