package by.it.group410902.podryabinkin.lesson13;

import java.util.ArrayList;

class Node1 {
    String value;
    ArrayList<Node1> after = new ArrayList<>();
    int inDegree = 0; // количество входящих рёбер

    Node1(String v) {
        this.value = v;
    }
}