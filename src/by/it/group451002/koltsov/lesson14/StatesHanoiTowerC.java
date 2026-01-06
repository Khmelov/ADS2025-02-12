package by.it.group451002.koltsov.lesson14;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StatesHanoiTowerC {
    public static void hanoi(Integer height, List<List<Integer>> towers, Integer startIndex, Integer helpIndex, Integer endIndex, DSUtaskC dsu) {
        if (height == 1) {
            towers.get(endIndex).addLast(towers.get(startIndex).removeLast());
            dsu.add(towers);
        }
        else
        {
            hanoi(height - 1, towers, startIndex, endIndex, helpIndex, dsu);
            towers.get(endIndex).addLast(towers.get(startIndex).removeLast());
            dsu.add(towers);
            hanoi(height - 1, towers, helpIndex, startIndex, endIndex, dsu);
        }
    }

    public static void main(String[] args) {
        DSUtaskC dsu = new DSUtaskC();
        List<List<Integer>> towers = new ArrayList<>();
        towers.add(new ArrayList<>());
        towers.add(new ArrayList<>());
        towers.add(new ArrayList<>());

        Scanner scan = new Scanner(System.in);
        int N = scan.nextInt();
        for (int i = 1; i <= N; i++)
            towers.getFirst().addFirst(i);
        hanoi(N, towers, 0, 1, 2, dsu);

        dsu.unionAll();
        List<Integer> sizes = dsu.getUnionsSizes();
        for (Integer size : sizes)
            System.out.print(size + " ");
    }
}
