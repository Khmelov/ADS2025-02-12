package by.it.group410901.kalach.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    // Упрощенная реализация DSU (без использования коллекций)
    static class DSU {
        private int[] parent; // массив родительских элементов
        private int[] size;   // массив размеров компонент
        private int maxSteps; // максимальное количество шагов

        public DSU(int maxSteps) {
            this.maxSteps = maxSteps;
            parent = new int[maxSteps];
            size = new int[maxSteps];
            // Инициализация: каждый элемент - корень своего множества
            for (int i = 0; i < maxSteps; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        // Поиск корневого элемента с сжатием пути
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // сжатие пути
            }
            return parent[x];
        }

        // Объединение двух множеств по размеру
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return; // уже в одном множестве
            }

            // Присоединяем меньшее дерево к большему
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        // Получение размера компоненты
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    // Глобальные переменные для представления трех башен
    static int[] towerA;
    static int[] towerB;
    static int[] towerC;
    static int topA, topB, topC; // указатели вершин башен
    static int stepCount;         // счетчик выполненных шагов
    static int[] stepMaxHeights;  // массив максимальных высот после каждого шага
    static int maxN;              // максимальное количество дисков

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // количество дисков
        scanner.close();

        maxN = n;
        // Максимальное количество шагов для n дисков = 2^n - 1
        int maxSteps = (1 << n) - 1;

        // Инициализация башен
        towerA = new int[n + 1];
        towerB = new int[n + 1];
        towerC = new int[n + 1];
        stepMaxHeights = new int[maxSteps];

        // Начальное состояние: все диски на башне A
        topA = n;
        topB = 0;
        topC = 0;

        // Заполняем башню A дисками (большие внизу, маленькие сверху)
        for (int i = 0; i < n; i++) {
            towerA[i] = n - i; // самый большой диск = n, самый маленький = 1
        }

        stepCount = 0;

        // Запускаем рекурсивное решение Ханойских башен
        hanoi(n, 'A', 'B', 'C');

        // Создаем DSU для группировки шагов с одинаковой максимальной высотой
        DSU dsu = new DSU(maxSteps);

        // Массив для отслеживания последнего шага с каждой высотой
        int[] lastStepWithHeight = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            lastStepWithHeight[i] = -1; // инициализация
        }

        // Объединяем шаги с одинаковой максимальной высотой
        for (int i = 0; i < stepCount; i++) {
            int height = stepMaxHeights[i];
            if (lastStepWithHeight[height] != -1) {
                // Объединяем текущий шаг с предыдущим шагом той же высоты
                dsu.union(i, lastStepWithHeight[height]);
            }
            lastStepWithHeight[height] = i;
        }

        // Собираем размеры уникальных групп (кластеров)
        int[] clusterSizes = new int[n + 1];
        int clusterCount = 0;
        boolean[] visited = new boolean[maxSteps];

        for (int i = 0; i < stepCount; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                clusterSizes[clusterCount++] = dsu.getSize(root);
            }
        }

        // Сортируем размеры кластеров по возрастанию (пузырьковая сортировка)
        for (int i = 0; i < clusterCount - 1; i++) {
            for (int j = 0; j < clusterCount - i - 1; j++) {
                if (clusterSizes[j] > clusterSizes[j + 1]) {
                    // Обмен элементов
                    int temp = clusterSizes[j];
                    clusterSizes[j] = clusterSizes[j + 1];
                    clusterSizes[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < clusterCount; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes[i]);
        }
        System.out.println();
    }

    // Рекурсивный алгоритм решения Ханойских башен
    static void hanoi(int n, char from, char to, char aux) {
        if (n == 1) {
            moveDisk(from, to);
            return;
        }

        // Рекурсивно перемещаем n-1 дисков на вспомогательную башню
        hanoi(n - 1, from, aux, to);
        // Перемещаем самый большой диск на целевую башню
        moveDisk(from, to);
        // Рекурсивно перемещаем n-1 дисков с вспомогательной на целевую башню
        hanoi(n - 1, aux, to, from);
    }

    // Функция перемещения одного диска
    static void moveDisk(char from, char to) {
        int disk = 0;

        // Снимаем диск с исходной башни
        if (from == 'A') {
            disk = towerA[--topA];
        } else if (from == 'B') {
            disk = towerB[--topB];
        } else {
            disk = towerC[--topC];
        }

        // Кладём диск на целевую башню
        if (to == 'A') {
            towerA[topA++] = disk;
        } else if (to == 'B') {
            towerB[topB++] = disk;
        } else {
            towerC[topC++] = disk;
        }

        // Вычисляем максимальную высоту среди всех башен после этого хода
        int maxHeight = Math.max(topA, Math.max(topB, topC));
        stepMaxHeights[stepCount++] = maxHeight;
    }
}