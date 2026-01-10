package by.it.group451002.koltsov.lesson14;

import java.util.List;
import java.util.Scanner;

public class PointsA {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int distance = scan.nextInt();
        int amount = scan.nextInt();

        if (distance == 0)
            System.out.print(" ");
        DSUtaskA dsu = new DSUtaskA(distance);
        for (int i = 0; i < amount; i++) {
            dsu.add(scan.nextInt(), scan.nextInt(), scan.nextInt());
        }

        List<Integer> sizes = dsu.getUnionsSizes();
        for (int i = sizes.size() - 1; i >= 0; i--)
            System.out.print(sizes.get(i) + " ");
    }
}
