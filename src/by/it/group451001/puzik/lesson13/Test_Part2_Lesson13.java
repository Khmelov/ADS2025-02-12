package by.it.group451001.puzik.lesson13;

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
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("X -> Y, Y -> Z, Z -> W", true).include("X Y Z W");
        run("0 -> 1, 0 -> 2, 0 -> 3, 1 -> 4, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("A -> B, B -> C, D -> E, E -> F", true).include("A B C D E F");
        run("1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("1 2 3 4 5 6");
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> F", true).include("A B C D E F");
        run("0 -> 1, 1 -> 2, 0 -> 3, 3 -> 4, 4 -> 2", true).include("0 1 3 4 2");
        run("X -> A, Y -> A, A -> B, B -> C", true).include("X Y A B C");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 5, 5 -> 6", true).include("1 2 3 4 5 6");
        run("A -> B, B -> C, A -> D, D -> E, E -> C", true).include("A B D E C");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("A -> B, C -> D, E -> F", true).include("A B C D E F");
        run("1 -> 2, 2 -> 3, 1 -> 4, 4 -> 5, 2 -> 5, 3 -> 6, 5 -> 6", true).include("1 2 3 4 5 6");
        run("X -> Y, X -> Z, Y -> W, Z -> W, W -> V", true).include("X Y Z W V");
        run("A -> B, B -> C, A -> D, D -> E, B -> E, C -> F, E -> F", true).include("A B C D E F");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 1 -> 5, 2 -> 5, 4 -> 6, 5 -> 6", true).include("0 1 2 3 4 5 6");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E, E -> F", true).include("A B C D E F");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 6, 5 -> 6, 6 -> 7", true).include("1 2 3 4 5 6 7");
        run("X -> A, Y -> A, Z -> A, A -> B, B -> C, C -> D", true).include("X Y Z A B C D");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("X -> Y, Y -> X, Z -> W", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, D -> E", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> W, W -> X", true).include("yes").exclude("no");
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
        run("1->2, 2->1", true).include("12");
        run("A->B, B->C, C->A, D->E", true).include("D\nE\nABC");
        run("X->Y, Y->Z, Z->X, A->B", true).include("XYZ\nA\nB");
        run("1->2, 2->3, 3->1, 4->5, 5->6, 6->4", true).include("456\n123");
        run("A->B, B->A, C->D, D->C, E->F", true).include("E\nF\nCD\nAB");
        run("1->2, 2->3, 3->4, 4->1, 5->6, 6->7, 7->5", true).include("567\n1234");
        run("X->Y, Y->X, A->B, B->C, C->D, D->A", true).include("XY\nABCD");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }
}
