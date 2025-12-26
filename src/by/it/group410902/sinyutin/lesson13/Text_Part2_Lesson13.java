package by.it.group410902.sinyutin.lesson13;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import by.it.HomeWork;
import org.junit.Test;

public class Text_Part2_Lesson13 extends HomeWork {
    public Text_Part2_Lesson13() {
    }

    @Test
    public void testGraphA() {
        this.run("0 -> 1", true).include("0 1");
        this.run("0 -> 1, 1 -> 2", true).include("0 1 2");
        this.run("0 -> 2, 1 -> 2, 0 -> 1", true).include("0 1 2");
        this.run("0 -> 2, 1 -> 3, 2 -> 3, 0 -> 1", true).include("0 1 2 3");
        this.run("1 -> 3, 2 -> 3, 2 -> 3, 0 -> 1, 0 -> 2", true).include("0 1 2 3");
        this.run("0 -> 1, 0 -> 2, 0 -> 2, 1 -> 3, 1 -> 3, 2 -> 3", true).include("0 1 2 3");
        this.run("A -> B, A -> C, B -> D, C -> D", true).include("A B C D");
        this.run("A -> B, A -> C, B -> D, C -> D, A -> D", true).include("A B C D");
    }

    @Test
    public void testGraphB() {
        this.run("0 -> 1", true).include("no").exclude("yes");
        this.run("0 -> 1, 1 -> 2", true).include("no").exclude("yes");
        this.run("0 -> 1, 1 -> 2, 2 -> 0", true).include("yes").exclude("no");
    }

    @Test
    public void testGraphC() {
        this.run("1->2, 2->3, 3->1, 3->4, 4->5, 5->6, 6->4", true).include("123\n456");
        this.run("C->B, C->I, I->A, A->D, D->I, D->B, B->H, H->D, D->E, H->E, E->G, A->F, G->F, F->K, K->G", true).include("C\nABDHI\nE\nFGK");
    }
}

