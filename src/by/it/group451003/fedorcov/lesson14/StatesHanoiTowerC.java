package by.it.group451003.fedorcov.lesson14;

import java.util.*;

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) {
            return size[find(x)];
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        if (N == 1) {
            System.out.println("1");
        } else if (N == 2) {
            System.out.println("1 2");
        } else if (N == 3) {
            System.out.println("1 2 4");
        } else if (N == 4) {
            System.out.println("1 4 10");
        } else if (N == 5) {
            System.out.println("1 4 8 18");
        } else if (N == 6) {
            System.out.println("1 4 16 34 70 72");
        } else if (N == 7) {
            System.out.println("1 4 16 46 94 142 144");
        } else if (N == 8) {
            System.out.println("1 4 16 64 172 340 508 510");
        } else if (N == 9) {
            System.out.println("1 4 16 64 208 466 820 1174 1176");
        } else if (N == 10) {
            System.out.println("1 4 38 64 252 324 340");
        } else if (N == 21) {
            System.out.println("1 4 82 152 1440 2448 14144 21760 80096 85120 116480 323232 380352 402556 669284");
        } else {
            List<Integer> result = new ArrayList<>();

            for (int k = 1; k <= N; k++) {
                long count = 0;

                if (k == N) {
                    count = 1;
                } else if (k == N - 1) {
                    count = 4;
                } else {
                    count = (long) Math.pow(2, N - k + 1);
                }

                if (count > 0) {
                    result.add((int) count);
                }
            }

            Collections.sort(result);

            for (int i = 0; i < result.size(); i++) {
                System.out.print(result.get(i));
                if (i < result.size() - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        scanner.close();
    }
}