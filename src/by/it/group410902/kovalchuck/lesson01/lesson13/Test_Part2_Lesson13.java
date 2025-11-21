package by.it.group410902.kovalchuck.lesson01.lesson13;

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
        run("5 -> 6, 6 -> 7", true).include("5 6 7");
        run("X -> Y, Y -> Z", true).include("X Y Z");
        run("1 -> 2, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, C -> D, B -> D", true).include("A B C D");
        run("M -> N, N -> O, O -> P", true).include("M N O P");
        run("1 -> 3, 2 -> 3", true).include("1 2 3");
        run("Start -> Middle, Middle -> End", true).include("Start Middle End");
        run("Alpha -> Beta, Beta -> Gamma", true).include("Alpha Beta Gamma");
        run("First -> Second, First -> Third", true).include("First Second Third");
        run("A -> C, B -> C", true).include("A B C");
        run("X -> Z, Y -> Z", true).include("X Y Z");
        run("Begin -> Step1, Begin -> Step2, Step1 -> End, Step2 -> End", true).include("Begin Step1 Step2 End");
        run("Input -> Process, Process -> Output", true).include("Input Process Output");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y", true).include("no").exclude("yes");
        run("Start -> End", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("Alpha -> Beta, Beta -> Gamma, Gamma -> Alpha", true).include("yes").exclude("no");
        run("First -> Second, Second -> Third", true).include("no").exclude("yes");
        run("Input -> Process, Process -> Output", true).include("no").exclude("yes");
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
        run("A->B, B->C, C->A, C->D, D->E", true)
                .include("ABC\nD\nE");
        run("X->Y, Y->Z, Z->X, Z->W, W->V, V->W", true)
                .include("XYZ\nVW");
        run("M->N, N->O, O->P, P->M, Q->R", true)
                .include("MNOP\nQ\nR");
        run("1->2, 2->1, 3->4, 4->5, 5->3", true)
                .include("12\n345");
        run("A->B, B->C, C->D, D->A, E->F, F->G, G->E, H->I", true)
                .include("ABCD\nEFG\nH\nI");
        run("P->Q, Q->R, R->S, S->P, T->U, U->T, V->W", true)
                .include("PQRS\nTU\nV\nW");
    }


}