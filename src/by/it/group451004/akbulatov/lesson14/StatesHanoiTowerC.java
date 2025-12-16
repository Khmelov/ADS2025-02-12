package by.it.group451004.akbulatov.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
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
            if (parent[x] != x) {
                parent[x] = find(parent[x]); // эвристика сокращения пути
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;

            // эвристика по размеру поддерева
            if (size[rootX] < size[rootY]) {
                int temp = rootX;
                rootX = rootY;
                rootY = temp;
            }

            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }

        int getSize(int x) {
            return size[find(x)];
        }
    }

    // Вспомогательный класс для хранения состояния
    static class State {
        int maxHeight;
        int step;

        State(int maxHeight, int step) {
            this.maxHeight = maxHeight;
            this.step = step;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int totalSteps = (1 << N) - 1; // 2^N - 1

        // Массив для хранения максимальных высот на каждом шаге
        State[] states = new State[totalSteps];

        // Стек для эмуляции рекурсии
        int[] stackN = new int[1000];
        int[] stackFrom = new int[1000];
        int[] stackTo = new int[1000];
        int[] stackAux = new int[1000];
        int stackPtr = 0;

        // Инициализация стека с начальной задачей
        stackN[stackPtr] = N;
        stackFrom[stackPtr] = 0; // A
        stackTo[stackPtr] = 1;   // B
        stackAux[stackPtr] = 2;  // C
        stackPtr++;

        // Массивы для хранения высот пирамид
        int[] heights = new int[3];
        heights[0] = N; // начальная высота A

        int step = 0;

        // Итеративное решение Ханойских башен
        while (stackPtr > 0) {
            stackPtr--;
            int n = stackN[stackPtr];
            int from = stackFrom[stackPtr];
            int to = stackTo[stackPtr];
            int aux = stackAux[stackPtr];

            if (n == 1) {
                // Перемещаем диск
                heights[from]--;
                heights[to]++;
                step++;

                // Находим максимальную высоту
                int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
                states[step - 1] = new State(maxHeight, step - 1);
            } else {
                // Помещаем подзадачи в стек в обратном порядке
                // move(n-1, from, aux, to)
                stackN[stackPtr] = n - 1;
                stackFrom[stackPtr] = from;
                stackTo[stackPtr] = aux;
                stackAux[stackPtr] = to;
                stackPtr++;

                // move(1, from, to, aux) - будет обработано на следующей итерации
                stackN[stackPtr] = 1;
                stackFrom[stackPtr] = from;
                stackTo[stackPtr] = to;
                stackAux[stackPtr] = aux;
                stackPtr++;

                // move(n-1, aux, to, from)
                stackN[stackPtr] = n - 1;
                stackFrom[stackPtr] = aux;
                stackTo[stackPtr] = to;
                stackAux[stackPtr] = from;
                stackPtr++;
            }
        }

        // Создаем DSU для группировки шагов
        DSU dsu = new DSU(totalSteps);

        // Группируем шаги по максимальной высоте
        int[] firstStepWithHeight = new int[N + 2]; // +2 для индексации от 0 до N+1
        for (int i = 0; i <= N + 1; i++) {
            firstStepWithHeight[i] = -1;
        }

        for (int i = 0; i < totalSteps; i++) {
            int height = states[i].maxHeight;
            if (firstStepWithHeight[height] == -1) {
                firstStepWithHeight[height] = i;
            } else {
                dsu.union(firstStepWithHeight[height], i);
            }
        }

        // Собираем размеры групп
        int[] groupSizes = new int[N + 2];
        boolean[] visited = new boolean[totalSteps];

        for (int i = 0; i < totalSteps; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                groupSizes[dsu.getSize(root)]++;
            }
        }

        // Выводим размеры групп в порядке возрастания
        boolean first = true;
        for (int i = 1; i <= totalSteps; i++) {
            if (groupSizes[i] > 0) {
                // Для каждого размера группы выводим его столько раз, сколько групп такого размера
                for (int j = 0; j < groupSizes[i]; j++) {
                    if (!first) {
                        System.out.print(" ");
                    }
                    System.out.print(i);
                    first = false;
                }
            }
        }
    }
}