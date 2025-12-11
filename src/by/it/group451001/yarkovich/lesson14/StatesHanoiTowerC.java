package by.it.group451001.yarkovich.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // Класс DSU (Disjoint Set Union) для объединения состояний по максимальной высоте пирамид
    static class DSU {
        int[] parent; // массив родительских элементов
        int[] size;   // массив размеров множеств

        // Конструктор инициализирует DSU для n элементов
        DSU(int n) {
            parent = new int[n + 1]; // +1 т.к. работаем с высотами от 1 до N
            size = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i; // каждый элемент - корень своего множества
                size[i] = 0;   // начальный размер множества 0
            }
        }

        // Метод find с эвристикой сжатия пути
        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]); // сжатие пути
            return parent[v];
        }

        // Метод union для объединения двух множеств с эвристикой объединения по размеру
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return; // уже в одном множестве
            // Объединяем меньшее множество с большим
            if (size[a] < size[b]) {
                int t = a;
                a = b;
                b = t;
            }
            parent[b] = a;
            size[a] += size[b]; // обновляем размер большего множества
        }

        // Метод для добавления элемента в множество
        void addToSet(int x) {
            size[x] += 1; // увеличиваем размер множества с корнем x
        }
    }

    static DSU dsu;    // структура DSU для группировки состояний
    static int maxN;   // максимальная высота пирамиды

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt(); // считываем высоту пирамиды

        maxN = N;
        dsu = new DSU(N); // инициализируем DSU для высот от 1 до N

        // Запускаем рекурсивное решение Ханойской башни
        // Начальное состояние: на стержне A - N дисков, на B и C - 0
        hanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // Собираем информацию о размерах кластеров (поддеревьев)
        int[] clusterSize = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i); // находим корень для каждой высоты
            clusterSize[root] = dsu.size[root]; // сохраняем размер кластера
        }

        // Формируем массив только с ненулевыми размерами кластеров
        int[] result = new int[N + 1];
        int count = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSize[i] > 0) {
                result[count++] = clusterSize[i];
            }
        }

        // Сортировка вставками (по условию нельзя использовать коллекции)
        for (int i = 0; i < count - 1; i++) {
            for (int j = i + 1; j < count; j++) {
                if (result[i] > result[j]) {
                    int t = result[i];
                    result[i] = result[j];
                    result[j] = t;
                }
            }
        }

        // Вывод результатов в порядке возрастания
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(result[i]);
        }
        System.out.println();
    }

    // Рекурсивный алгоритм решения задачи Ханойской башни
    // n - количество дисков для перемещения
    // from - исходный стержень, to - целевой стержень, aux - вспомогательный стержень
    // heights - массив текущих высот пирамид на стержнях A, B, C
    static void hanoi(int n, char from, char to, char aux, int[] heights) {
        if (n == 0) return; // базовый случай рекурсии

        // Шаг 1: переместить n-1 дисков с from на aux (используя to как вспомогательный)
        hanoi(n - 1, from, aux, to, heights);

        // Шаг 2: переместить самый большой диск n с from на to
        moveDisk(n, from, to, heights);

        // Шаг 3: переместить n-1 дисков с aux на to (используя from как вспомогательный)
        hanoi(n - 1, aux, to, from, heights);
    }

    // Метод для обработки перемещения одного диска
    static void moveDisk(int disk, char from, char to, int[] heights) {
        // Обновляем высоты пирамид после перемещения
        if (from == 'A') heights[0]--; // уменьшаем высоту на исходном стержне
        else if (from == 'B') heights[1]--;
        else if (from == 'C') heights[2]--;

        if (to == 'A') heights[0]++; // увеличиваем высоту на целевом стержне
        else if (to == 'B') heights[1]++;
        else if (to == 'C') heights[2]++;

        // Находим максимальную высоту среди всех трех пирамид
        int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));

        // Добавляем текущее состояние в соответствующую группу DSU
        // Группируем состояния с одинаковой максимальной высотой пирамид
        dsu.addToSet(maxHeight);
    }
}