package by.it.group451003.plyushchevich.lesson13;

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
        run("X -> Y", true).include("X Y");
        run("X -> Z, Y -> Z", true).include("X Y Z");
        run("A -> B, C -> D", true).include("A B C D");
        run("A -> B, B -> C, C -> D, D -> E", true).include("A B C D E");
        run("A -> C, B -> C, D -> C", true).include("A B D C");
        run("K -> M, K -> L, L -> N, M -> N", true).include("K L M N");
        run("Z -> A, Z -> B, A -> C, B -> C", true).include("Z A B C");
        run("node1 -> node2, node1 -> node3, node2 -> node4", true)
                .include("node1 node2 node3 node4");
        run("5 -> 10, 5 -> 7, 7 -> 8, 10 -> 11", true)
                .include("5 7 8 10 11");
        run("A -> B, C -> D, E -> F", true).include("A B C D E F");
        run("0 -> 2, 0 -> 3, 1 -> 3, 1 -> 4, 4 -> 5", true)
                .include("0 1 2 3 4 5");
        run("Start -> A, Start -> B, A -> End, B -> End", true)
                .include("Start A B End");

    }


    @Test
    public void testGraphB() {
        run("0 -> 1", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("1 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("A -> B, B -> C, C -> D", true).include("no").exclude("yes");
        run("1 -> 2, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> A", true).include("yes").exclude("no");
        run("1 -> 2, 2 -> 3, 3 -> 1, 4 -> 5", true).include("yes").exclude("no");
        run("1 -> 1", true).include("yes").exclude("no");
        run("0 -> 1, 0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        run("0 -> 1, 0 -> 2, 2 -> 3, 3 -> 4", true).include("no").exclude("yes");
        run("0 -> 1, 1 -> 2, 2 -> 3, 3 -> 1", true).include("yes").exclude("no");
        run("X -> Y, Y -> Z, Z -> X, A -> B", true).include("yes").exclude("no");
        run("A -> B, B -> C, C -> B", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true)
                .include("123\n456");
        run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true)
                .include("C\nABDHI\nE\nFGK");
        run("A->B, B->C, C->A", true).include("ABC");
        run("A->B, C->D", true).include("A\nB\nC\nD");
        run("1->2, 2->1, 3->4", true).include("12\n3\n4");
        run("X->Y, Y->Z, Z->X, A->B, B->A", true).include("X Y Z".replace(" ", "") + "\nAB");
        run("a->b, b->c, c->d, d->b, e->f", true).include("bcd\ne\nf");
        run("1->2, 2->3, 3->4, 4->5", true).include("1\n2\n3\n4\n5");
    }
}