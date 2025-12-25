package by.it.group451002.sidarchuk.lesson13;

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
        run("B -> D, A -> C, C -> D", true).include("A B C D");
        run("Z -> Y, X -> Y", true).include("X Z Y");
        run("M -> P, N -> P, O -> P", true).include("M N O P");
        // Графы с числовыми вершинами больше 10
        run("10 -> 11, 11 -> 12", true).include("10 11 12");
        run("5 -> 10, 10 -> 15, 15 -> 20", true).include("5 10 15 20");
        // Графы с несколькими начальными вершинами
        run("0 -> 2, 1 -> 2", true).include("0 1 2");
        run("A -> C, B -> C, C -> D", true).include("A B C D");
        // Простые линейные цепочки
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("1 2 3 4");
        run("5 -> 6, 6 -> 7, 7 -> 8", true).include("5 6 7 8");
        // Графы, где порядок важен для лексикографической сортировки
        run("C -> A, B -> A", true).include("B C A");
        run("Z -> X, Y -> X", true).include("Y Z X");
        // Большой граф
        run("A -> B, A -> C, B -> D, C -> E, D -> F, E -> G, F -> H, G -> H", true).include("A B C D E F G H");
    }

    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        //Дополните эти тесты СВОИМИ более сложными примерами и проверьте их работоспособность.
        //Параметр метода run - это ввод. Параметр метода include - это вывод.
        //Общее число примеров должно быть не менее 12 (сейчас их 3).
        // Дополнительные тесты С циклами
        run("1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 1", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 2", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 1", true).include("yes").exclude("no");
        // Дополнительные тесты БЕЗ циклов
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 4", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4, 4 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 1 -> 3, 2 -> 4, 3 -> 5", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 1 -> 3, 3 -> 4", true).include("no").exclude("yes");
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
        //Полный граф из 4 вершин
        run("A->B, A->C, A->D, B->A, B->C, B->D, C->A, C->B, C->D, D->A, D->B, D->C", true)
                .include("ABCD");
        //Граф-цепочка (каждая вершина связана только со следующей)
        run("A->B, B->C, C->D, D->E, E->F", true)
                .include("A\nB\nC\nD\nE\nF");
        //Пустой граф (только вершины без рёбер)
        run("", true)
                .include("");
        //Одна вершина с петлёй
        run("A->A", true)
                .include("A");
        //Один большой цикл (все вершины в одной компоненте)
        run("A->B, B->C, C->D, D->A", true)
                .include("ABCD");
        //Граф-треугольник (3 вершины все связаны друг с другом)
        run("A->B, B->C, C->A", true)
                .include("ABC");

    }


}