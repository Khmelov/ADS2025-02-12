package by.it.group410902.menshikov.lesson13;

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
        run("0 -> 1, 0 -> 2, 0 -> 3, 1 -> 4, 2 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("5 -> 1, 5 -> 2, 1 -> 3, 2 -> 3, 3 -> 4", true).include("5 1 2 3 4");
        run("A -> B, B -> C, C -> D, D -> E, E -> F", true).include("A B C D E F");
        run("X -> Y, X -> Z, Y -> W, Z -> W, W -> V", true).include("X Y Z W V");
        run("0 -> 1, 1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
        run("M -> N, M -> O, M -> P, N -> Q, O -> Q, P -> Q, Q -> R", true).include("M N O P Q R");
        run("1 -> 2, 1 -> 3, 1 -> 4, 2 -> 5, 3 -> 5, 4 -> 5, 4 -> 6", true).include("1 2 3 4 5 6");
        run("a -> b, a -> c, b -> d, b -> e, c -> e, d -> f, e -> f", true).include("a b c d e f");
        run("0 -> 1, 2 -> 3, 1 -> 4, 3 -> 4", true).include("0 1 2 3 4");
        run("10 -> 20, 10 -> 30, 20 -> 40, 30 -> 40, 40 -> 50", true).include("10 20 30 40 50");
        run("P -> Q, P -> R, Q -> S, R -> S, S -> T, T -> U", true).include("P Q R S T U");
        run("A -> B, A -> C, A -> D, B -> E, C -> E, D -> E, E -> F, E -> G", true).include("A B C D E F G");
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 20 (сейчас их 8).
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z", true).include("no").exclude("yes");
        run("A -> B, B -> A", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("0->1, 1->2, 2->0, 2->3, 3->4, 4->3", true)
                .include("012\n34");
        run("A->B, B->A, B->C, C->D, D->C", true)
                .include("AB\nCD");
        run("1->2, 2->3, 3->4, 4->5, 5->1, 5->6, 6->7", true)
                .include("12345\n6\n7");
        run("a->b, b->c, c->a, c->d, d->e, e->f, f->d", true)
                .include("abc\ndef");
        run("0->1, 1->0, 1->2", true)
                .include("01\n2");
        run("P->Q, Q->P, P->R, R->S, S->R", true)
                .include("PQ\nRS");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 8 (сейчас их 2).
    }


}