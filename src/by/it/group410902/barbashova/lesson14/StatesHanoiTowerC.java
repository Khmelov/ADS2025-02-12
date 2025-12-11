package by.it.group410902.barbashova.lesson14;

import java.util.Scanner;
//Этот код анализирует состояния Ханойской башни в процессе перемещения дисков
//и группирует диски по максимальной высоте башни в каждый момент времени.
//Проще говоря: мы смотрим, какие диски находятся в башне максимальной высоты
//в каждый момент перемещения, и объединяем их в кластеры.

public class StatesHanoiTowerC {

    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 0;
            }
        }

        int find(int v) {           // найти корень множества
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        void union(int a, int b) {  // объединить множества
            a = find(a);
            b = find(b);
            if (a == b) return;
            // объединяем меньшее с большим
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        void addToSet(int x) {      // добавить элемент в множество
            size[x] += 1;
        }
    }

    static DSU dsu;
    static int maxN;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();  // количество колец

        maxN = N;
        dsu = new DSU(N);


        // Начальное состояние: все диски на стержне 'A'
        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // Анализируем результаты
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            clusterSize[root] = dsu.size[root];
        }

        // Собираем и сортируем размеры кластеров
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортировка пузырьком (по возрастанию)
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int t = result[i];
                    result[i] = result[j];
                    result[j] = t;
                }
            }
        }

        // Вывод результатов
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }
    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return;

        // 1. Перемещаем n-1 дисков на вспомогательный стержень
        hanoi(n - 1, from, aux, to, heights);

        // 2. Перемещаем самый большой диск на целевой стержень
        moveDisk(n, from, to, heights);

        // 3. Перемещаем n-1 дисков с вспомогательного на целевой
        hanoi(n - 1, aux, to, from, heights);
    }

    static void moveDisk(int disk, char from, char to, int[] heights) {
        // Обновляем высоты стержней
        if (from == 'A') heights[0]--;
        if (from == 'B') heights[1]--;
        if (from == 'C') heights[2]--;

        if (to == 'A') heights[0]++;
        if (to == 'B') heights[1]++;
        if (to == 'C') heights[2]++;

        // Находим максимальную высоту среди всех стержней
        int maxHeight = heights[0];
        if (heights[1] > maxHeight) maxHeight = heights[1];
        if (heights[2] > maxHeight) maxHeight = heights[2];

        // Добавляем текущий диск в множество для этой максимальной высоты
        dsu.addToSet(maxHeight);
    }
}