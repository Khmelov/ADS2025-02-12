package by.it.group410902.sinyutin.lesson14;
import java.util.Scanner;

public class StatesHanoiTowerC {

    //  Поля для DSU
    private static int[] parent;
    private static int[] size;

    //  Поля для симуляции
    private static int[] counts; // Количество дисков на стержнях 0, 1, 2
    private static int stepIndex; // Текущий номер шага (0-based)

    // Массив для запоминания первого индекса шага для каждой конкретной высоты.
    // firstOccurrenceByHeight[h] хранит индекс шага, где впервые max высота была h.
    private static int[] firstOccurrenceByHeight;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N;
        if (scanner.hasNextInt()) {
            N = scanner.nextInt();
        } else {
            return;
        }

        // Общее количество ходов
        int totalMoves = (1 << N) - 1;

        // Инициализация DSU
        parent = new int[totalMoves];
        size = new int[totalMoves];
        for (int i = 0; i < totalMoves; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Инициализация симуляции
        counts = new int[3];
        counts[0] = N; // На стержне A (индекс 0) изначально N дисков
        counts[1] = 0;
        counts[2] = 0;
        stepIndex = 0;

        // Инициализация трекера высот (максимальная высота может быть N)
        firstOccurrenceByHeight = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            firstOccurrenceByHeight[i] = -1;
        }

        // Запуск рекурсивного алгоритма
        // 0 - Start (A), 1 - Destination (B), 2 - Auxiliary (C)
        hanoi(N, 0, 2, 1);

        // Сбор результатов
        // Нам нужно найти размеры всех поддеревьев (компонент связности).
        // Так как мы объединяли ВСЕ шаги с одинаковой высотой в одно дерево,
        // количество деревьев будет равно количеству уникальных высот, встретившихся в решении.

        // Используем массив для сбора размеров (максимум N+1 возможных вариантов высот)
        int[] resultSizes = new int[N + 1];
        int resultCount = 0;

        // Проходим по всем элементам DSU
        for (int i = 0; i < totalMoves; i++) {
            // Если элемент является корнем своего множества
            if (parent[i] == i) {
                resultSizes[resultCount++] = size[i];
            }
        }

        // Сортировка пузырьком (коллекции запрещены)
        for (int i = 0; i < resultCount - 1; i++) {
            for (int j = 0; j < resultCount - i - 1; j++) {
                if (resultSizes[j] > resultSizes[j + 1]) {
                    int temp = resultSizes[j];
                    resultSizes[j] = resultSizes[j + 1];
                    resultSizes[j + 1] = temp;
                }
            }
        }

        // Вывод
        for (int i = 0; i < resultCount; i++) {
            System.out.print(resultSizes[i]);
            if (i < resultCount - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * Рекурсивный метод Ханойских башен с учетом перемещения и DSU группировки.
     */
    private static void hanoi(int n, int from, int auxiliary, int to) {
        if (n == 0) {
            return;
        }

        // Перенести n-1 на вспомогательный
        hanoi(n - 1, from, to, auxiliary);

        // --- ВЫПОЛНЕНИЕ ХОДА ---

        // 1. Обновляем счетчики на стержнях
        counts[from]--;
        counts[to]++;

        // 2. Вычисляем максимальную высоту пирамиды среди всех стержней
        int maxH = counts[0];
        if (counts[1] > maxH) maxH = counts[1];
        if (counts[2] > maxH) maxH = counts[2];

        // 3. DSU Группировка
        // Если такая высота уже встречалась, объединяем текущий шаг с первым шагом той высоты
        if (firstOccurrenceByHeight[maxH] != -1) {
            union(firstOccurrenceByHeight[maxH], stepIndex);
        } else {
            // Если высота встретилась впервые, запоминаем индекс этого шага
            firstOccurrenceByHeight[maxH] = stepIndex;
        }

        stepIndex++; // Переходим к следующему шагу

        // --- КОНЕЦ ХОДА ---

        // Перенести n-1 со вспомогательного на целевой
        hanoi(n - 1, auxiliary, from, to);
    }

    // --- Реализация DSU ---

    private static int find(int i) {
        if (parent[i] == i) {
            return i;
        }
        // Эвристика: сжатие пути
        parent[i] = find(parent[i]);
        return parent[i];
    }

    private static void union(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI != rootJ) {
            // Эвристика: объединение по размеру (меньшее к большему)
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ;
                size[rootJ] += size[rootI];
            } else {
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }
    }
}