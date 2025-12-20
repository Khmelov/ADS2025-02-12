package by.it.group451003.galuzo.lesson13;

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
        run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        run("A -> B, D -> E, K -> L, B -> E, B -> D, B -> L, D -> K, E -> K", true).include("A B D E K L");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D, B -> C", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D, B -> C, A -> D", true).include("A B C D");
        run("0 -> 1, 0 -> 1, 0 -> 1, 0 -> 1, 0 -> 1, 0 -> 1, 0 -> 1", true).include("0 1");
        run("0 -> 0", true).include("0");
        run("1 -> 1", true).include("1");
        run("1 -> 1, 1 -> 2", true).include("1 2");
        run("A -> B, D -> E, K -> L, B -> E, B -> D, B -> L, D -> K, E -> K", true).include("A B D E K L");
        run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D, B -> C", true).include("A B C D");
        run("A -> B, A -> C, B -> D, C -> D, A -> D, B -> C, A -> D", true).include("A B C D");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 0, 2 -> 3", true).include("yes").exclude("no");
        run("A -> C, B -> D, C -> A, D -> B", true).include("yes").exclude("no");
        run("A -> A").include("yes").exclude("no");
        run("B -> C, A -> B, A -> A").include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E, E -> F, F -> G, G -> H, H -> I, I -> J, J -> K, K -> L, L -> M, M -> N, N -> O, O -> P, P -> Q, Q -> R, R -> S, S -> T, T -> U, U -> V, V -> W, W -> X, X -> Y, Y -> Z, Z -> A")
                .include("yes").exclude("no");
        run("A -> B, B -> C, C -> D, D -> E, E -> F, F -> G, G -> H, H -> I, I -> J, J -> K, K -> L, L -> M, M -> N, N -> O, O -> P, P -> Q, Q -> R, R -> S, S -> T, T -> U, U -> V, V -> W, W -> X, X -> Y, Y -> Z")
                .include("no").exclude("yes");
        run("CCD -> CD").include("no").exclude("yes");
        run("CD -> CD, CDD -> CDD, CD -> CDD").include("yes").exclude("no");
        run("C -> C, M -> N, N -> O, O -> P, P -> R, R -> Q, R -> S, Q -> P, S -> T, T -> U, U -> V, V -> W")
                .include("yes")
                .exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true).include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("1->2, 2->3, 3->1, 3->4, 4->5", true).include("123");
        run("A->C, A->C, C->A", true).include("AC");
        run("A->B, B->A, C->D, D->C, E->F, F->E", true).include("AB\nCD\nEF");
        run("C->E, M->K, L->N, H->L, D->L, C->D", true).include("C\nD\nE\nM\nK\nH\nL\nN");
        run("C->E, M->K, L->N, H->L, D->L, C->D, D->C, E->M, M->L", true).include("CD\nE\nM\nK\nH\nL\nN");
        run("C->E, M->K, L->N, H->C, E->H, A->M, K->L, N->A", true).include("AKLMN\nCEH");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}