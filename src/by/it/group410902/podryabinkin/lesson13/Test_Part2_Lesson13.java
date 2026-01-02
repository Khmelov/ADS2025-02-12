package by.it.group410902.podryabinkin.lesson13;

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

        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, B -> E, C -> D, D -> E", true).include("A B C D E");
        run("X -> Y, X -> Z, Y -> W, Z -> W", true).include("X Y Z W");
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("1 -> 2, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, B -> C, D -> E", true).include("A B C D E");
        run("0 -> 1, 0 -> 2, 1 -> 4, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("a -> b, a -> c, b -> d, c -> d, e -> f", true).include("a b c d e f");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");
        run("0 -> 2, 0 -> 3, 1 -> 3, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("5 -> 6, 5 -> 7, 6 -> 8, 7 -> 8, 0 -> 2, 2 -> 4", true).include("0 2 4 5 6 7 8");
        run("K -> L, K -> M, L -> N, M -> N, N -> O", true).include("K L M N O");

    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 0, 2 -> 3", true).include("yes").exclude("no");
        run("1 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 4 -> 5, 5 -> 6", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1, 4 -> 5, 5 -> 6", true).include("yes").exclude("no");

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
    }


}