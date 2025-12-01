package by.it.group451001.serganovskij.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // Класс для представления состояния башен
    static class State {
        int a, b, c;
        State(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }

    // DSU (Union-Find)
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void union(int x, int y) {
            int px = find(x);
            int py = find(y);
            if (px == py) return;
            if (size[px] < size[py]) {
                parent[px] = py;
                size[py] += size[px];
            } else {
                parent[py] = px;
                size[px] += size[py];
            }
        }
    }

    // Генерация всех состояний (только высоты башен)
    static State[] generateStates(int N) {
        int totalSteps = (1 << N) - 1;
        State[] states = new State[totalSteps];
        int[] index = {0};
        int[] heights = new int[3];
        heights[0] = N; // старт: все на A
        move(N, 0, 1, 2, heights, states, index);
        return states;
    }

    static void move(int n, int from, int to, int aux, int[] heights, State[] states, int[] index) {
        if (n == 0) return;
        move(n - 1, from, aux, to, heights, states, index);

        // перенос диска
        heights[from]--;
        heights[to]++;
        states[index[0]++] = new State(heights[0], heights[1], heights[2]);

        move(n - 1, aux, to, from, heights, states, index);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.close();

        State[] states = generateStates(N);
        int total = states.length;

        DSU dsu = new DSU(total);

        // Оптимизация: объединяем состояния с одинаковой max высотой через "корзины"
        int maxPossibleHeight = N;
        int[][] bucket = new int[3 * N + 1][total]; // 3*N - максимум max(A,B,C)
        int[] sizes = new int[3 * N + 1];

        for (int i = 0; i < total; i++) {
            int max = Math.max(Math.max(states[i].a, states[i].b), states[i].c);
            bucket[max][sizes[max]++] = i;
        }

        // Объединяем состояния с одинаковой max высотой
        for (int h = 0; h <= 3 * N; h++) {
            for (int i = 1; i < sizes[h]; i++) {
                dsu.union(bucket[h][0], bucket[h][i]);
            }
        }

        // Подсчет размеров поддеревьев
        int[] count = new int[total];
        for (int i = 0; i < total; i++) {
            int p = dsu.find(i);
            count[p]++;
        }

        // Заполняем массив уникальных размеров
        int[] result = new int[total];
        int idx = 0;
        for (int c : count) {
            if (c > 0) result[idx++] = c;
        }

        // Сортировка простым пузырьком (коллекции запрещены)
        for (int i = 0; i < idx - 1; i++) {
            for (int j = i + 1; j < idx; j++) {
                if (result[i] > result[j]) {
                    int temp = result[i];
                    result[i] = result[j];
                    result[j] = temp;
                }
            }
        }

        // Вывод
        for (int i = 0; i < idx; i++) {
            System.out.print(result[i]);
            if (i != idx - 1) System.out.print(" ");
        }
    }
}
