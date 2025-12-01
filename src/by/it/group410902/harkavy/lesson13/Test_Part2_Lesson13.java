package by.it.group410902.harkavy.lesson13;

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

        run("0 -> 2", true).include("0 2");
        run("0 -> 1, 2 -> 3", true).include("0 1 2 3");
        run("0 -> 2, 0 -> 3, 1 -> 3, 1 -> 4, 3 -> 5, 2 -> 5", true).include("0 1 2 3 4 5");
        run("0 -> 3, 0 -> 2, 1 -> 3, 1 -> 2", true).include("0 1 2 3");
        run("2 -> 4, 2 -> 5, 3 -> 5, 0 -> 2, 1 -> 3", true).include("0 1 2 3 4 5");
        run("A -> C, B -> C, C -> D, C -> E", true).include("A B C D E");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("A -> B, C -> D, E -> F", true).include("A B C D E F");
        run("A -> D, B -> D, C -> D", true).include("A B C D");
        run("A -> B, A -> B, B -> C, A -> C", true).include("A B C");
        run("0 -> 1, 0 -> 2, 1 -> 4, 2 -> 3, 3 -> 4", true).include("0 1 2 3 4");
        run("0 -> 1, 1 -> 3, 2 -> 3, 2 -> 4, 3 -> 5", true).include("0 1 2 3 4 5");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 1 -> 2, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 1 -> 3, 2 -> 3", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 1, 2 -> 3", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("0 -> 0", true).include("yes").exclude("no");
        run("0 -> 1, 2 -> 3, 4 -> 5", true).include("no").exclude("yes");
        run("0 -> 1, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
    }


    @Test
    public void testGraphC() {
        // 1. два компонента: 1-2-3 (цикл), 4-5-6 (цикл)
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");

        // 2. пример из условия
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");

        // 3. один цикл, одна КСС
        run("1->2, 2->3, 3->1", true)
                .include("123");

        // 4. обычная цепочка без циклов: каждая вершина — своя КСС
        run("1->2, 2->3", true)
                .include("1\n2\n3");

        // 5. две независимые цепочки (две компоненты-источника)
        // возможный корректный вывод: 1,2 и 3,4 как отдельные строки
        run("1->2, 3->4", true)
                .include("1\n2\n3\n4");

        // 6. цикл на A,B и путь к C
        run("A->B, B->A, B->C", true)
                .include("AB\nC");

        // 7. три КСС: {A,B,C}, {D,E}, {F}
        run("A->B, B->C, C->A, C->D, D->E, E->D, E->F", true)
                .include("ABC\nDE\nF");

        // 8. петля на вершине (сам на себя) — одна КСС
        run("X->X", true)
                .include("X");
    }



}