package by.it.group451004.volynets.lesson14;

import java.util.*;

public class StatesHanoiTowerC {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();

        Map<Integer, Integer> groups = new HashMap<>();

        hanoi(N, 'A', 'B', 'C', groups, new int[]{N, 0, 0});

        List<Integer> sizes = new ArrayList<>(groups.values());
        Collections.sort(sizes);

        for (int i = 0; i < sizes.size(); i++) {
            System.out.print(sizes.get(i));
            if (i + 1 < sizes.size()) System.out.print(" ");
        }
    }

    static void hanoi(int n, char from, char to, char aux,
                      Map<Integer, Integer> groups, int[] tops) {
        if (n == 0) return;

        // Переносим n-1 дисков на вспомогательный стержень
        hanoi(n - 1, from, aux, to, groups, tops);

        // Переносим самый большой диск (номер n) на целевой стержень
        moveDisk(from, to, groups, tops);

        // Переносим n-1 дисков со вспомогательного стержня на целевой
        hanoi(n - 1, aux, to, from, groups, tops);
    }

    static void moveDisk(char from, char to,
                         Map<Integer, Integer> groups, int[] tops) {
        // снимаем диск
        if (from == 'A') tops[0]--;
        if (from == 'B') tops[1]--;
        if (from == 'C') tops[2]--;

        // кладём диск
        if (to == 'A') tops[0]++;
        if (to == 'B') tops[1]++;
        if (to == 'C') tops[2]++;

        int max = Math.max(tops[0], Math.max(tops[1], tops[2]));

        groups.put(max, groups.getOrDefault(max, 0) + 1);
    }
}
