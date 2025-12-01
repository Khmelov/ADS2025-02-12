package by.it.group410902.kovalchuck.lesson01.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Структура данных для хранения компонент связности
    static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            // Инициализация: каждый элемент — сам себе родитель, размер 0
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                size[i] = 0;
            }
        }

        // Поиск корня множества с путевой компрессией
        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        // Объединение двух множеств по размеру
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }

        // Увеличение размера множества, к которому принадлежит элемент x
        void addToSet(int x) {
            size[x] += 1;
        }
    }

    static DSU dsu;
    static int maxN;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // Чтение количества дисков

        maxN = N;
        dsu = new DSU(N); // Инициализация DSU для N дисков

        // Запуск алгоритма Ханойской башни
        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0}); // Начальное состояние: все диски на стержне A

        // Подсчет размеров компонент (групп дисков)
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            clusterSize[root] = dsu.size[root];
        }

        // Сбор результатов в массив
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортировка размеров компонент по возрастанию
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int t = result[i];
                    result[i] = result[j];
                    result[j] = t;
                }
            }
        }

        // Вывод отсортированных размеров компонент
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    // Рекурсивная реализация алгоритма Ханойской башни
    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return;

        hanoi(n - 1, from, aux, to, heights); // Переместить n-1 дисков на вспомогательный стержень

        moveDisk(n, from, to, heights); // Переместить диск n

        hanoi(n - 1, aux, to, from, heights); // Переместить n-1 дисков на целевой стержень
    }

    // Обработка перемещения диска: обновление высот и регистрация в DSU
    static void moveDisk(int disk, char from, char to, int[] heights) {
        // Уменьшаем высоту исходного стержня
        if (from == 'A') heights[0]--;
        if (from == 'B') heights[1]--;
        if (from == 'C') heights[2]--;

        // Увеличиваем высоту целевого стержня
        if (to == 'A') heights[0]++;
        if (to == 'B') heights[1]++;
        if (to == 'C') heights[2]++;

        // Определяем максимальную высоту среди всех стержней
        int maxHeight = heights[0];
        if (heights[1] > maxHeight) maxHeight = heights[1];
        if (heights[2] > maxHeight) maxHeight = heights[2];

        // Регистрируем диск в компоненте с высотой maxHeight
        dsu.addToSet(maxHeight);
    }
}
