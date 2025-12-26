package by.it.group410902.kavtsevich.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Класс для системы непересекающихся множеств
    static class DSU {
        int[] parent;  // массив родительских элементов
        int[] size;    // массив размеров компонент

        DSU(int n) {
            parent = new int[n + 1];  // +1 т.к. работаем с дисками от 1 до N
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;  // каждый элемент - корень самого себя
                size[i] = 0;    // изначально размеры нулевые
            }
        }

        // Поиск корня с эвристикой сжатия пути
        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);  // рекурсивно находим корень
            return parent[v];
        }


        // Добавление элемента в множество (увеличиваем счетчик размера)
        void addToSet(int x) {
            size[x] += 1;  // просто увеличиваем счетчик для данного корня
        }
    }

    static DSU dsu;    // глобальная DSU структура
    static int maxN;   // максимальное количество дисков

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();  // считываем количество дисков

        maxN = N;
        dsu = new DSU(N);  // создаем DSU для N дисков

        // Запускаем рекурсивное решение Ханойской башни
        // Начальное состояние: все диски на стержне A
        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // Собираем размеры всех кластеров (компонент связности)
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);  // находим корень для каждого элемента
            clusterSize[root] = dsu.size[root];  // сохраняем размер компоненты
        }

        // Формируем массив только непустых размеров кластеров
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортировка вставками по возрастанию (т.к. нельзя использовать Collections)
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

    // Рекурсивное решение задачи Ханойской башни
    // n - количество дисков для перемещения
    // from - исходный стержень
    // to - целевой стержень
    // aux - вспомогательный стержень
    // heights - массив высот на каждом стержне [A, B, C]

    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return;  // базовый случай рекурсии

        // Переместить n-1 дисков с from на aux (используя to как вспомогательный)
        hanoi(n - 1, from, aux, to, heights);

        // Переместить самый большой диск n с from на to
        moveDisk(n, from, to, heights);

        // Переместить n-1 дисков с aux на to (используя from как вспомогательный)
        hanoi(n - 1, aux, to, from, heights);
    }

    // Обработка перемещения одного диска
    static void moveDisk(int disk, char from, char to, int[] heights) {
        // Уменьшаем высоту на исходном стержне
        if (from == 'A') heights[0]--;
        else if (from == 'B') heights[1]--;
        else if (from == 'C') heights[2]--;

        // Увеличиваем высоту на целевом стержне
        if (to == 'A') heights[0]++;
        else if (to == 'B') heights[1]++;
        else if (to == 'C') heights[2]++;

        // Находим максимальную высоту среди всех стержней
        int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));

        // Добавляем текущее состояние в соответствующую группу DSU
        dsu.addToSet(maxHeight);


    }
}