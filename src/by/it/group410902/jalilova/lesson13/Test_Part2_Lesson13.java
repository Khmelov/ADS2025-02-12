package by.it.group410902.jalilova.lesson13;

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
        //доп тесты
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("10 -> 20, 20 -> 30, 30 -> 40", true).include("10 20 30 40");

        run("1 -> 3, 2 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("A -> C, B -> C, C -> D, E -> F", true).include("A B C D E F");

        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("S -> A, S -> B, A -> E, B -> E", true).include("S A B E");

        run("1 -> 2, 3 -> 4, 5 -> 6", true).include("1 2 3 4 5 6");
        run("X -> Y, Z -> W", true).include("X Y Z W");

        run("0 -> 1, 0 -> 2, 1 -> 3, 1 -> 4, 2 -> 4, 2 -> 5, 3 -> 6, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("A -> B, A -> C, B -> D, B -> E, C -> E, C -> F, D -> G, E -> G, F -> G", true).include("A B C D E F G");

        run("1 -> 2, 1 -> 2, 2 -> 3, 2 -> 3", true).include("1 2 3");
        run("X -> Y, X -> Y, Y -> Z, Y -> Z", true).include("X Y Z");

        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 6, 6 -> 7", true).include("1 2 3 4 5 6 7");
        run("A -> B, B -> C, C -> D, D -> E, E -> F, F -> G", true).include("A B C D E F G");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        //доп тесты
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");

        run("1 -> 2, 1 -> 3, 2 -> 4, 2 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4", true).include("no").exclude("yes");
        run("A -> B, A -> C, B -> D, C -> E", true).include("no").exclude("yes");

        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
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

        //доп тесты
        run("A->B, B->C, C->A", true)
                .include("ABC");

        run("1->2, 2->1, 3->4, 4->3", true)
                .include("12\n34");

        run("A->B, B->C, C->D, D->E, E->F", true)
                .include("A\nB\nC\nD\nE\nF");

        run("1->2, 2->3, 3->1, 2->4, 4->5, 5->6, 6->4, 3->7, 7->8, 8->9, 9->7", true)
                .include("123\n456\n789");

        run("A->A", true)
                .include("A");

        run("1->2, 2->3, 3->4, 4->5, 5->1, 2->6, 6->7, 7->8, 8->6, 4->9", true)
                .include("12345\n678\n9");

    }
}