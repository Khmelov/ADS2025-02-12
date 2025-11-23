package by.it.group410902.kozincev.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DSU {
        private final int[] parent;
        private final int[] rank; // или size
        private int count; // Количество множеств

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            count = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i; // Изначально каждый элемент - корень своего множества
                rank[i] = 1;   // Изначально ранг (или размер) каждого множества равен 1
            }
        }

        // Операция Find с сжатием путей
        public int find(int i) {
            if (parent[i] != i) {
                parent[i] = find(parent[i]); // Сжатие пути
            }
            return parent[i];
        }

        // Операция Union по рангу (или размеру)
        public boolean union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                // Объединение по рангу (или размеру)
                if (rank[rootI] < rank[rootJ]) {
                    parent[rootI] = rootJ;
                } else if (rank[rootI] > rank[rootJ]) {
                    parent[rootJ] = rootI;
                } else {
                    parent[rootJ] = rootI;
                    rank[rootI]++; // Увеличение ранга, если ранги одинаковы
                }
                count--; // Уменьшаем количество множеств
                return true;
            }
            return false;
        }

        // Возвращает количество непересекающихся множеств
        public int getCount() {
            return count;
        }
    }

    // Вспомогательный класс для хранения данных о шаге
    private static class StepInfo {
        final int stepIndex;
        final int max_height;

        StepInfo(int index, int height) {
            this.stepIndex = index;
            this.max_height = height;
        }
    }

    // Динамический массив на основе массива (для обхода ограничения "Коллекциями пользоваться нельзя!")
    private static class StepList {
        private StepInfo[] data;
        private int size;

        public StepList() {
            // Инициализация с достаточной емкостью для N=10 (1023 шага)
            this.data = new StepInfo[2048];
            this.size = 0;
        }

        public void add(StepInfo info) {
            if (size == data.length) {
                StepInfo[] newData = new StepInfo[data.length * 2];
                System.arraycopy(data, 0, newData, 0, size);
                data = newData;
            }
            data[size++] = info;
        }

        public int size() { return size; }
        public StepInfo get(int index) { return data[index]; }
    }

    // --- Глобальное состояние ---
    private int stepCount = 0;
    private final StepList stepInfos = new StepList();

    // N - общее количество дисков
    private int N;
    // diskLocation[i] - стержень (0='A', 1='B', 2='C'), на котором находится диск i
    // Диски нумеруются с 1 до N. diskLocation[0] не используется.
    private int[] diskLocation;

    /**
     * Вычисляет ключ кластеризации: наибольшая высота пирамид A, B, C.
     * Высота - это просто количество дисков на стержне.
     */
    private int calculateMaxHeight() {
        int hA = 0, hB = 0, hC = 0;
        for (int i = 1; i <= N; i++) {
            if (diskLocation[i] == 0) hA++; // Rod A
            else if (diskLocation[i] == 1) hB++; // Rod B
            else hC++; // Rod C
        }
        return Math.max(hA, Math.max(hB, hC));
    }

    /**
     * Рекурсивный алгоритм Ханойских башен с отслеживанием состояния.
     */
    private void hanoi(int n, int source, int destination, int auxiliary) {
        if (n == 0) {
            return;
        }

        // 1. Переместить n-1 дисков с source на auxiliary
        hanoi(n - 1, source, auxiliary, destination);

        // 2. Переместить n-й диск с source на destination (ШАГ)
        // Обновляем состояние: диск n перемещен
        diskLocation[n] = destination;

        // Вычисляем ключ и сохраняем информацию о шаге
        int max_height = calculateMaxHeight();
        stepInfos.add(new StepInfo(stepCount++, max_height));

        // 3. Переместить n-1 дисков с auxiliary на destination
        hanoi(n - 1, auxiliary, destination, source);
    }

    private void run(int n) {
        this.N = n;
        this.diskLocation = new int[n + 1];

        // Начальное состояние: все диски на стержне 'A' (индекс 0)
        for (int i = 1; i <= n; i++) {
            diskLocation[i] = 0;
        }

        // Запуск Ханойских башен (A=0 to B=1 via C=2).
        hanoi(n, 0, 1, 2);

        int numSteps = stepInfos.size();
        if (numSteps == 0) {
            System.out.println();
            return;
        }

        // 1. DSU Объединение по ключу (max_height)
        DSU dsu = new DSU(numSteps);

        // Массив для хранения индекса первого шага (корня кластера) для каждой высоты
        int[] heightRoot = new int[n + 1];
        for(int i = 0; i <= n; i++){
            heightRoot[i] = -1;
        }

        for (int i = 0; i < numSteps; i++) {
            StepInfo currentStep = stepInfos.get(i);
            int height = currentStep.max_height; // Ключ кластеризации

            if (heightRoot[height] == -1) {
                heightRoot[height] = i;
            } else {
                dsu.union(i, heightRoot[height]);
            }
        }

        // 2. Подсчет размеров кластеров (без коллекций, используя массивы)
        int[] rootList = new int[numSteps];
        int[] sizeList = new int[numSteps];
        int uniqueRootsCount = 0;

        for (int i = 0; i < numSteps; i++) {
            int root = dsu.find(i);
            boolean found = false;

            for (int j = 0; j < uniqueRootsCount; j++) {
                if (rootList[j] == root) {
                    sizeList[j]++;
                    found = true;
                    break;
                }
            }
            if (!found) {
                rootList[uniqueRootsCount] = root;
                sizeList[uniqueRootsCount] = 1;
                uniqueRootsCount++;
            }
        }

        // 3. Сортировка размеров кластеров (в порядке возрастания [cite: 6])
        for (int i = 0; i < uniqueRootsCount - 1; i++) {
            for (int j = 0; j < uniqueRootsCount - i - 1; j++) {
                if (sizeList[j] > sizeList[j + 1]) {
                    int temp = sizeList[j];
                    sizeList[j] = sizeList[j + 1];
                    sizeList[j + 1] = temp;
                }
            }
        }

        // 4. Вывод
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uniqueRootsCount; i++) {
            sb.append(sizeList[i]);
            if (i < uniqueRootsCount - 1) {
                sb.append(" ");
            }
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        int n = scanner.nextInt();
        scanner.close();
        new StatesHanoiTowerC().run(n);
    }
}