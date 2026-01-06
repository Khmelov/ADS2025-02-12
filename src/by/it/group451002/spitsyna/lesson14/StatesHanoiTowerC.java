package by.it.group451002.spitsyna.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {
    private static int[] parent;
    private static int[] size;
    private static int[] heightToStep;  // для каждой макс. высоты - первый шаг с ней
    private static int[] towerHeights;  // высоты башен A, B, C
    private static int stepIndex;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();

        int totalSteps = (1 << n) - 1;  // 2^n - 1 шагов
        parent = new int[totalSteps];
        size = new int[totalSteps];
        heightToStep = new int[n + 1];
        towerHeights = new int[3];

        // Инициализация DSU
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // -1 - нет шага с такой высотой
        for (int i = 0; i <= n; i++) {
            heightToStep[i] = -1;
        }

        // Начальное состояние: все диски на башне A
        towerHeights[0] = n;  // A
        towerHeights[1] = 0;  // B
        towerHeights[2] = 0;  // C

        stepIndex = 0;

        //перемещаем n дисков с A(0) на B(1), используя C(2)
        hanoi(n, 0, 1, 2);

        int[] finalSizes = new int[n + 1];
        int sizeCount = 0;

        for (int h = 1; h <= n; h++) {
            if (heightToStep[h] != -1) {
                // Находим корень кластера для высоты h и берём его размер
                finalSizes[sizeCount++] = size[find(heightToStep[h])];
                }
        }

        // Сортируем только sizeCount элементов (максимум n, а не 2^n)
        for (int i = 0; i < sizeCount - 1; i++) {
            for (int j = 0; j < sizeCount - i - 1; j++) {
                if (finalSizes[j] > finalSizes[j + 1]) {
                    int temp = finalSizes[j];
                    finalSizes[j] = finalSizes[j + 1];
                    finalSizes[j + 1] = temp;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < sizeCount; i++) {
            result.append(finalSizes[i]);
            result.append(" ");
        }

        result.deleteCharAt(result.length() - 1);
        System.out.println(result);
    }

    // Рекурсивное решение Ханойских башен
    private static void hanoi(int n, int from, int to, int aux) {
        if (n == 0) return;

        // Сначала перемещаем n-1 дисков на вспомогательный стержень
        hanoi(n - 1, from, aux, to);

        // Перемещаем диск n с from на to
        towerHeights[from]--;
        towerHeights[to]++;

        // Находим максимальную высоту
        int maxHeight = Math.max(towerHeights[0],Math.max(towerHeights[1], towerHeights[2]));

        // Группируем шаги с одинаковой макс. высотой
        if (heightToStep[maxHeight] == -1) {
            // Первый шаг с такой макс. высотой
            heightToStep[maxHeight] = stepIndex;
        } else {
            // Объединяем с предыдущими шагами той же макс. высоты
            union(stepIndex, heightToStep[maxHeight]);
        }

        stepIndex++;

        // Перемещаем n-1 дисков со вспомогательного на целевой
        hanoi(n - 1, aux, to, from);
    }

    private static int find(int elem) {
        if (parent[elem] != elem) {
            parent[elem] = find(parent[elem]);  // сжатие пути
        }
            return parent[elem];
    }

    private static void union(int elem1, int elem2) {
        int root1 = find(elem1);
        int root2 = find(elem2);

        if (root1 != root2) {
            // младший объединяется со старшим
            if (size[root1] < size[root2]) {
                parent[root1] = root2;
                size[root2] += size[root1];
            }
            else {
                parent[root2] = root1;
                size[root1] += size[root2];
            }
        }
    }
}

