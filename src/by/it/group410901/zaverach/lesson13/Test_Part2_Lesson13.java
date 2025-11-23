package by.it.group410901.zaverach.lesson13;

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

        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("A -> A", true).include("A");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("0 -> 1, 0 -> 2, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, B -> 1, 1 -> 2, 2 -> 3", true).include("A B 1 2 3");
        run("0 -> 1, 0 -> 1, 1 -> 2, 1 -> 2", true).include("0 1 2");
        run("A -> B, A -> C, B -> E, C -> D, D -> E", true).include("A B C D E");
        run("A -> B, C -> D", true).include("A B C D");
        run("A -> B, B -> C, C -> D, D -> E, E -> F, F -> G", true).include("A B C D E F G");
        run("A -> B, A -> C, B -> D, C -> D, B -> C", true).include("A B C D");
        run("0 -> 1, 1 -> 2, 3 -> 4", true).include("0 1 2 3 4");
        run("M -> N, O -> P, P -> Q, N -> Q", true).include("M N O P Q");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("A -> B, B -> C, D -> E", true).include("A B C D E");
        run("0 -> 2, 1 -> 2, 3 -> 4", true).include("0 1 2 3 4");
        run("Z -> X, Y -> X, X -> W", true).include("Y Z X W");
        run("A -> B, C -> D, E -> F", true).include("A B C D E F");
        run("1 -> 2, 3 -> 1, 2 -> 3", true).include("1 2 3");
        run("A -> B, B -> D, A -> C, C -> D", true).include("A B C D");
        run("0 -> 1, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        run("A -> B, B -> C, C -> D", true).include("no");
        run("A -> B, B -> C, C -> A", true).include("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes");
        run("A -> B, B -> C, D -> E", true).include("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes");
        run("0 -> 1, 2 -> 3, 3 -> 4", true).include("no");
        run("A -> B, B -> C, C -> D, D -> A", true).include("yes");
        run("P -> Q, Q -> R", true).include("no");
        run("R -> S, S -> T, T -> R", true).include("yes");
        run("1 -> 2, 3 -> 1, 2 -> 3", true).include("yes");
        run("M -> N, N -> O, O -> P", true).include("no");
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

        run("A->B, B->A", true)
                .include("AB");

        run("A->B, B->A, C->D, D->C, E->F, F->E", true)
                .include("EF\nCD\nAB");

        run("A->B, B->C, C->D, D->A", true)
                .include("ABCD");

        run("A->B, B->C, C->D", true)
                .include("A\nB\nC\nD");

        run("1->2, 2->1, 2->3, 3->4, 4->5, 5->3, 6->7", true)
                .include("6\n7\n12\n345");

        run("A->B, B->C, C->A, D->E", true)
                .include("D\nE\nABC");

        run("M->N, N->M, O->P, Q->R", true)
                .include("Q\nR\nO\nP\nMN");

        run("1->2, 2->3, 3->1, 3->4, 5->6, 6->7, 7->5, 8->9", true)
                .include("8\n9\n567\n123\n4");
    }


}
