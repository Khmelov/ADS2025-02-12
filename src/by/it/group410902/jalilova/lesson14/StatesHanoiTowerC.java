package by.it.group410902.jalilova.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // система непересекающихся множеств для отслеживания состояний
    static class DisjointSetUnion {
        int[] parent;
        int[] setSize;

        DisjointSetUnion(int n) {
            parent = new int[n + 1];
            setSize = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
                setSize[i] = 0;
            }
        }

        // находим корневой элемент с оптимизацией пути
        int find(int v) {
            if (parent[v] != v)
                parent[v] = find(parent[v]);
            return parent[v];
        }

        // объединяем два множества
        void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) return;
            // присоединяем меньшее множество к большему
            if (setSize[a] < setSize[b]) {
                int temp = a;
                a = b;
                b = temp;
            }
            parent[b] = a;
            setSize[a] += setSize[b];
        }

        // добавляем элемент в множество
        void addElement(int x) {
            setSize[x] += 1;
        }
    }

    static DisjointSetUnion dsu;
    static int disksCount;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();

        disksCount = N;
        dsu = new DisjointSetUnion(N);

        // запускаем алгоритм Ханойских башен
        solveHanoi(N, 'A', 'B', 'C', new int[]{N, 0, 0});

        // анализируем полученные множества
        int[] clusterSizes = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            int root = dsu.find(i);
            clusterSizes[root] = dsu.setSize[root];
        }

        // собираем ненулевые размеры кластеров
        int[] resultArray = new int[N + 1];
        int resultCount = 0;
        for (int i = 1; i <= N; i++) {
            if (clusterSizes[i] > 0) {
                resultArray[resultCount++] = clusterSizes[i];
            }
        }

        // сортируем размеры кластеров по возрастанию
        for (int i = 0; i < resultCount - 1; i++) {
            for (int j = i + 1; j < resultCount; j++) {
                if (resultArray[i] > resultArray[j]) {
                    int temp = resultArray[i];
                    resultArray[i] = resultArray[j];
                    resultArray[j] = temp;
                }
            }
        }

        // выводим результат
        for (int i = 0; i < resultCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(resultArray[i]);
        }
        System.out.println();
    }

    // рекурсивное решение задачи Ханойских башен
    static void solveHanoi(int n, char fromRod, char toRod, char auxRod, int[] rodHeights) {
        if (n == 0) return;

        // перемещаем n-1 дисков на вспомогательный стержень
        solveHanoi(n - 1, fromRod, auxRod, toRod, rodHeights);

        // перемещаем n-й диск на целевой стержень
        moveSingleDisk(n, fromRod, toRod, rodHeights);

        // перемещаем n-1 дисков с вспомогательного на целевой стержень
        solveHanoi(n - 1, auxRod, toRod, fromRod, rodHeights);
    }

    // обработка перемещения одного диска
    static void moveSingleDisk(int disk, char fromRod, char toRod, int[] rodHeights) {
        // уменьшаем высоту исходного стержня
        if (fromRod == 'A') rodHeights[0]--;
        if (fromRod == 'B') rodHeights[1]--;
        if (fromRod == 'C') rodHeights[2]--;

        // увеличиваем высоту целевого стержня
        if (toRod == 'A') rodHeights[0]++;
        if (toRod == 'B') rodHeights[1]++;
        if (toRod == 'C') rodHeights[2]++;

        // находим максимальную высоту среди всех стержней
        int maxHeight = rodHeights[0];
        if (rodHeights[1] > maxHeight) maxHeight = rodHeights[1];
        if (rodHeights[2] > maxHeight) maxHeight = rodHeights[2];

        // регистрируем состояние в системе множеств
        dsu.addElement(maxHeight);
    }
}