package by.it.group410902.kozincev.lesson13;

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

        run("1 -> 2, 0 -> 2", true).include("0 1 2");
        run("D -> E, C -> E", true).include("C D E");
        run("1 -> 4, 2 -> 4, 3 -> 4", true).include("1 2 3 4");
        run("0 -> 1, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F, A -> F", true).include("A B C D E F");
        run("1 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("1 -> 10, 2 -> 10, 10 -> 11", true).include("1 2 10 11");
        run("M -> N, M -> P, M -> Q, N -> X, P -> X, Q -> X", true).include("M N P Q X");
        run("G -> F, C -> G, A -> B, D -> C", true).include("A B D C G F");
        run("1 -> 3, 2 -> 3, 4 -> 5, 5 -> 6, 1 -> 4", true).include("1 2 3 4 5 6");
        run("D -> A, D -> B, D -> C", true).include("D A B C");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E", true).include("A B C D E");

    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1, 4 -> 5", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1, 3 -> 4, 4 -> 3", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, A -> D", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
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

        run("1 -> 2, 2 -> 1", true).include("12");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("1\n2\n3\n4");
        run("A -> B, B -> C, C -> A, A -> D", true).include("ABC\nD");
        run("1 -> 2, 2 -> 3, 3 -> 1, 4 -> 5, 5 -> 4, 1 -> 4", true).include("123\n45");
        run("Z -> Y, Y -> Z, A -> B, B -> A, Z -> A", true).include("YZ\nAB");
        run("A -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1, 2 -> 5, 5 -> 6, 6 -> 5", true)
                .include("A\n1234\n56");
    }


}