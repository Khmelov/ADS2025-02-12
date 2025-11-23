package by.it.group410902.kavtsevich.lesson13;

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

        run("5 -> 6, 5 -> 7, 6 -> 7", true).include("5 6 7");
        run("X -> Y, Y -> Z, X -> Z", true).include("X Y Z");
        run("1 -> 2, 2 -> 3, 1 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("M -> N, N -> O, O -> P, M -> O, M -> P", true).include("M N O P");
        run("I -> J, J -> K, I -> K, K -> L", true).include("I J K L");
        run("P -> Q, Q -> R, R -> S, S -> T", true).include("P Q R S T");
        run("U -> V, V -> W, W -> X, X -> Y, Y -> Z", true).include("U V W X Y Z");
        run("A -> C, B -> C, C -> D", true).include("A B C D");
        run("F -> G, G -> H, F -> H, H -> I", true).include("F G H I");
        run("K -> L, L -> M, M -> N, K -> M, K -> N", true).include("K L M N");
        run("O -> P, P -> Q, Q -> R, O -> Q, O -> R", true).include("O P Q R");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).

        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> B", true).include("yes").exclude("no");
        run("M -> N, N -> O, O -> M", true).include("yes").exclude("no");
        run("P -> Q, Q -> R, R -> P", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A, C -> D", true).include("yes").exclude("no");
        run("I -> J, J -> K, K -> I, K -> L", true).include("yes").exclude("no");
        run("U -> V, V -> W, W -> U, W -> X", true).include("yes").exclude("no");
        run("A -> B, B -> C", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
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

        run("A->B, B->A", true).include("AB");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, B->C, C->D, D->C", true).include("A\nB\nCD"); // Исправленный ожидаемый результат
        run("X->Y, Y->Z, Z->X, Z->W", true).include("XYZ\nW");
        run("P->Q, Q->R, R->P, R->S, S->T, T->S", true)
                .include("PQR\nST");
        run("A->B, B->C, C->A, C->D, D->E, E->F, F->D", true)
                .include("ABC\nDEF");
        run("M->N, N->O, O->M, O->P, P->Q, Q->P", true)
                .include("MNO\nPQ");
    }


}