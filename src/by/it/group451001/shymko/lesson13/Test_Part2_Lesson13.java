package by.it.group451001.shymko.lesson13;

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
        run("1 -> 4, 1 -> 2, 2 -> 3, 2 -> 5, 3 -> 6, 4 -> 5, 4 -> 7, 5 -> 6, 5 -> 8, 6 -> 9, 7 -> 8, 8 -> 9", true).include("1 2 3 4 5 6 7 8 9");
        run("X -> Y, Y -> Z, Z -> W", true).include("X Y Z W");
        run("A -> C, B -> C, B -> D, C -> E, D -> E", true).include("A B C D E");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("1 2 3 4 5");
        run("P -> R, Q -> R, R -> S, R -> T", true).include("P Q R S T");
        run("alpha -> gamma, beta -> gamma, gamma -> delta", true).include("alpha beta gamma delta");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
        run("A -> B, B -> C, A -> D, D -> E, E -> C", true).include("A B D E C");
        run("1 -> 3, 2 -> 3, 3 -> 4, 3 -> 5, 4 -> 6, 5 -> 6", true).include("1 2 3 4 5 6");
        run("start -> task1, start -> task2, task1 -> task3, task2 -> task3, task3 -> end", true).include("start task1 task2 task3 end");
        run("M -> O, N -> O, O -> P, O -> Q", true).include("M N O P Q");
        run("Z -> A, Y -> A, X -> A", true).include("X Y Z A");
        run("1 -> A, 2 -> A, A -> B, A -> C", true).include("1 2 A B C");
        run("A -> B, C -> D", true).include("A B C D");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 3, 3 -> 4", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 4, 3 -> 5, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5, 5 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 4 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
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
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D", true)
                .include("ABC\nDEF");
        run("1->2, 2->3, 3->4, 4->5, 5->1", true)
                .include("12345");
        run("A->B, B->C, C->D, D->E, E->A, B->F, F->G, G->H, H->F", true)
                .include("ABCDE\nFGH");
        run("P->Q, Q->R, R->P, R->S, S->T, T->U, U->S, U->V", true)
                .include("PQR\nSTU\nV");
        run("X->Y, Y->Z, Z->X, Z->W, W->V, V->U, U->W, U->T", true)
                .include("XYZ\nUVW\nT");
        run("1->2, 2->3, 3->1, 4->5, 5->6, 6->4, 3->4, 6->7", true)
                .include("123\n456\n7");
        run("A->B, B->C, C->A, D->E, E->D, C->D, E->F, F->G, G->F", true)
                .include("ABC\nDE\nFG");
        run("M->N, N->O, O->M, O->P, P->Q, Q->R, R->P, R->S, S->T, T->S", true)
                .include("MNO\nPQR\nST");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}