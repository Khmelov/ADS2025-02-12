package by.it.group410901.borisdubinin.lesson13;

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
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");

        run("0 -> 1, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");

        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");

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

        run("A->B, B->C, C->A", true)
                .include("ABC");
        run("1->2, 2->1, 3->4, 4->3, 5->6, 6->5", true)
                .include("56\n34\n12");
        run("A->B, B->C, C->B, C->D, D->E, E->D, F->G, G->F", true)
                .include("FG\nA\nBC\nDE");
        run("1->2, 2->3, 3->1, 2->4, 4->5, 5->6, 6->4, 6->7, 7->8, 8->7", true)
                .include("123\n456\n78");
        run("A->B, B->C, C->D", true)
                .include("A\nB\nC\nD");
        run("X->Y, Y->Z, Z->Y, Z->W, W->V, V->U, U->W, X->A, A->B, B->A", true)
                .include("X\nYZ\nUVW\nAB");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}