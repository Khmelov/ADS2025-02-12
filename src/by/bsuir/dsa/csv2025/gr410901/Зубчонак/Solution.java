package by.bsuir.dsa.csv2025.gr410901.Зубчонак;

import java.util.*;

public class Solution {

    public static int solve(List<List<Integer>> piles) {
        int maxSuits = countSuits(piles);
        for (int from = 0; from < 10; from++) {
            List<Integer> src = piles.get(from);
            if (src.isEmpty()) continue;
            for (int len = 1; len <= src.size(); len++) {
                boolean valid = true;
                for (int i = 0; i < len - 1; i++) {
                    if (src.get(i) != src.get(i + 1) + 1) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                List<Integer> seq = new ArrayList<>(src.subList(0, len));
                for (int to = 0; to < 10; to++) {
                    if (from == to) continue;
                    List<Integer> dst = piles.get(to);
                    boolean canMove = dst.isEmpty() ||
                            (!seq.isEmpty() && dst.get(0) == seq.get(seq.size() - 1) - 1);
                    if (canMove) {
                        List<List<Integer>> copy = deepCopy(piles);
                        copy.get(from).subList(0, len).clear();
                        copy.get(to).addAll(0, seq);
                        int suits = countSuits(copy);
                        if (suits > maxSuits) maxSuits = suits;
                    }
                }
            }
        }
        return maxSuits;
    }

    public static List<List<Integer>> readPiles(Scanner sc) {
        List<List<Integer>> piles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int k = sc.nextInt();
            List<Integer> pile = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                pile.add(sc.nextInt());
            }
            piles.add(pile);
        }
        return piles;
    }

    static List<List<Integer>> deepCopy(List<List<Integer>> piles) {
        List<List<Integer>> res = new ArrayList<>();
        for (List<Integer> p : piles) res.add(new ArrayList<>(p));
        return res;
    }

    static int countSuits(List<List<Integer>> piles) {
        int cnt = 0;
        for (List<Integer> p : piles) {
            if (p.size() < 13) continue;
            boolean isSuite = true;
            for (int i = 0; i < 13; i++) {
                if (p.get(i) != 13 - i) {
                    isSuite = false;
                    break;
                }
            }
            if (isSuite) cnt++;
        }
        return cnt;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<List<Integer>> piles = readPiles(sc);
        System.out.println(solve(piles));
    }
}

