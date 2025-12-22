package by.bsuir.dsa.csv2025.gr451004.Кудрявцев;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;


public class Solution {
    private Map<String, Boolean> w;
    private int k, m;

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("=== Многооперационный Ним с ограничениями ===");
        int t = getValidIntInput(s, "Введите количество тестовых случаев (1-20): ", 1, 20);
        for (int i = 0; i < t; i++) {
            System.out.println("\n--- Тестовый случай #" + (i + 1) + " ---");
            int n = getValidIntInput(s, "Введите количество кучек (n, 1-10): ", 1, 10);
            int k = getValidIntInput(s, "Введите параметр k для группового хода (1 ≤ k ≤ n): ", 1, n);
            int m = getValidIntInput(s, "Введите параметр m для операции деления кучки (2 ≤ m ≤ 10): ", 2, 10);
            List<Integer> p = new ArrayList<>();
            System.out.println("Введите количество камней в каждой кучке (1 ≤ a_i ≤ 50):");
            for (int j = 0; j < n; j++) p.add(getValidIntInput(s, "Кучка #" + (j + 1) + ": ", 1, 50));
            Solution solver = new Solution();
            String r = solver.solveGame(k, m, p);
            System.out.println("\nАнализ игры...\nРезультат для тестового случая #" + (i + 1) + ": " + r);
        }
        System.out.println("\n=== Анализ завершен ===");
        s.close();
    }

    private static int getValidIntInput(Scanner s, String p, int min, int max) {
        while (true) {
            System.out.print(p);
            try {
                if (s.hasNextInt()) {
                    int v = s.nextInt();
                    if (v < min) System.out.println("Ошибка: значение не может быть меньше " + min);
                    else if (v > max) System.out.println("Ошибка: значение не может быть больше " + max);
                    else return v;
                } else {
                    String inv = s.next();
                    System.out.println("Ошибка: '" + inv + "' не является целым числом");
                }
            } catch (Exception e) {
                System.out.println("Произошла ошибка при вводе: " + e.getMessage());
                s.nextLine();
            }
        }
    }

    public String solveGame(int k, int m, List<Integer> piles) {
        this.k = k;
        this.m = m;
        this.w = new HashMap<>();
        List<Integer> sorted = new ArrayList<>(piles);
        Collections.sort(sorted, Collections.reverseOrder());
        return isWinningPosition(sorted) ? "First" : "Second";
    }

    private boolean isWinningPosition(List<Integer> piles) {
        if (piles.isEmpty()) return false;
        String key = piles.toString();
        if (w.containsKey(key)) return w.get(key);

        for (int i = 0; i < piles.size(); i++) {
            int orig = piles.get(i);
            for (int r = 1; r <= orig; r++) {
                List<Integer> newP = new ArrayList<>(piles);
                newP.set(i, orig - r);
                newP.removeIf(x -> x == 0);
                Collections.sort(newP, Collections.reverseOrder());
                if (!isWinningPosition(newP)) {
                    w.put(key, true);
                    return true;
                }
            }
        }

        int maxSel = Math.min(k, piles.size());
        for (int sel = 1; sel <= maxSel; sel++) {
            List<List<Integer>> combs = generateCombinations(piles.size(), sel);
            for (List<Integer> comb : combs) {
                List<Integer> newP = new ArrayList<>(piles);
                boolean valid = true;
                for (int idx : comb) {
                    int sz = newP.get(idx);
                    if (sz == 0) {
                        valid = false;
                        break;
                    }
                    newP.set(idx, sz - 1);
                }
                if (!valid) continue;
                newP.removeIf(x -> x == 0);
                Collections.sort(newP, Collections.reverseOrder());
                if (!isWinningPosition(newP)) {
                    w.put(key, true);
                    return true;
                }
            }
        }

        for (int i = 0; i < piles.size(); i++) {
            int sz = piles.get(i);
            if (sz > 0 && sz % m == 0) {
                int newSize = sz / m;
                List<Integer> newP = new ArrayList<>(piles);
                newP.remove(i);
                for (int j = 0; j < m; j++) newP.add(newSize);
                Collections.sort(newP, Collections.reverseOrder());
                if (!isWinningPosition(newP)) {
                    w.put(key, true);
                    return true;
                }
            }
        }

        w.put(key, false);
        return false;
    }

    private List<List<Integer>> generateCombinations(int n, int r) {
        List<List<Integer>> res = new ArrayList<>();
        if (r > n) return res;
        genCombHelper(res, new ArrayList<>(), 0, n, r);
        return res;
    }

    private void genCombHelper(List<List<Integer>> res, List<Integer> curr, int start, int n, int r) {
        if (r == 0) {
            res.add(new ArrayList<>(curr));
            return;
        }
        for (int i = start; i <= n - r; i++) {
            curr.add(i);
            genCombHelper(res, curr, i + 1, n, r - 1);
            curr.remove(curr.size() - 1);
        }
    }

    @Test
    //@DisplayName("Тест 1: Одна кучка с одним камнем")
    public void t1() {
        assertEquals("First", new Solution().solveGame(1, 2, Arrays.asList(1)));
    }

    @Test
    //@DisplayName("Тест 2: Нулевая XOR-сумма (1,2,3)")
    public void t2() {
        assertEquals("Second", new Solution().solveGame(1, 2, Arrays.asList(1, 2, 3)));
    }

    @Test
    //@DisplayName("Тест 3: Групповой ход (2 кучки по 1 камню, k=2)")
    public void t3() {
        assertEquals("First", new Solution().solveGame(2, 3, Arrays.asList(1, 1)));
    }

    @Test
    //@DisplayName("Тест 4: Деление кучки (1 кучка из 2 камней, m=2)")
    public void t4() {
        assertEquals("First", new Solution().solveGame(1, 2, Arrays.asList(2)));
    }

    @Test
    //@DisplayName("Тест 5: Симметричная позиция (4 кучки по 3 камня)")
    public void t5() {
        assertEquals("Second", new Solution().solveGame(2, 3, Arrays.asList(3, 3, 3, 3)));
    }

    @Test
    //@DisplayName("Тест 6: Две кучки по 4 камня (k=2, m=2)")
    public void t6() {
        assertEquals("Second", new Solution().solveGame(2, 2, Arrays.asList(4, 4)));
    }

    @Test
    //@DisplayName("Тест 7: Пять кучек по 1 камню (k=3)")
    public void t7() {
        assertEquals("First", new Solution().solveGame(3, 2, Arrays.asList(1, 1, 1, 1, 1)));
    }

    @Test
    //@DisplayName("Тест 8: Кучка из 9 камней (m=3)")
    public void t8() {
        assertEquals("First", new Solution().solveGame(1, 3, Arrays.asList(9)));
    }

    @Test
    //@DisplayName("Тест 9: Позиция (1,1,2 камня, k=2, m=2)")
    public void t9() {
        assertEquals("First", new Solution().solveGame(2, 2, Arrays.asList(1, 1, 2)));
    }

    @Test
    //@DisplayName("Тест 10: (5, 4, 3, 2, 1 камней, k=3, m=3)")
    public void t10() {
        assertEquals("First", new Solution().solveGame(3, 3, Arrays.asList(5, 4, 3, 2, 1)));
    }
}