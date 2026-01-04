package by.it.group410901.volkov.lesson14;

import java.util.Scanner;

/**
 * Класс для решения задачи Ханойских башен с группировкой состояний
 *
 * Задача: перенести пирамиду из N колец со стержня A на стержень B,
 * используя вспомогательный стержень C, соблюдая правила:
 * - за один раз можно переносить только одно кольцо
 * - нельзя класть большее кольцо на меньшее
 *
 * После решения задачи состояния группируются с помощью DSU:
 * состояния с одинаковой максимальной высотой пирамид A, B, C объединяются
 *
 * Ограничение: коллекциями пользоваться нельзя (только массивы)
 */
public class StatesHanoiTowerC {
    // Массивы для хранения состояний (без использования коллекций)
    private static int[][] states;      // Состояния: states[i] = [высотаA, высотаB, высотаC, maxHeight]
    private static int stateCount;      // Количество состояний
    private static int capacity;        // Емкость массива состояний

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // Высота стартовой пирамиды

        // Валидация входных данных
        if (n <= 0) {
            System.out.println();
            return;
        }

        // Инициализируем массив состояний
        // Максимальное количество состояний: 2^n - 1 (без начального состояния)
        // Валидация: проверяем, что n не слишком большой для избежания переполнения
        if (n > 30) {
            // Для n > 30 количество состояний превысит Integer.MAX_VALUE
            System.out.println();
            return;
        }
        
        capacity = (1 << n) - 1; // Точное количество состояний без начального
        // Дополнительная валидация: проверяем, что capacity > 0
        if (capacity <= 0) {
            System.out.println();
            return;
        }
        
        states = new int[capacity][4];
        stateCount = 0;

        // Храним текущее состояние отдельно (не в массиве, так как начальное не сохраняется)
        int[] heightA = new int[1]; // Высота стержня A
        int[] heightB = new int[1]; // Высота стержня B
        int[] heightC = new int[1]; // Высота стержня C

        // Инициализация: все диски на стержне A
        heightA[0] = n;
        heightB[0] = 0;
        heightC[0] = 0;

        // Начальное состояние НЕ сохраняется (по условию задания)
        // Решаем задачу Ханойских башен рекурсивно
        solveHanoi(n, heightA, heightB, heightC, 'A', 'B', 'C');

        // Проверка на пустой массив состояний (для n=0 или других граничных случаев)
        if (stateCount == 0) {
            System.out.println();
            return;
        }

        // Создаем массив индексов для сортировки
        // Используем сортировку подсчетом, так как maxHeight ограничен значением n (1..n)
        // Это намного быстрее быстрой сортировки для больших массивов
        int[] indices = countingSortIndices(n);

        // Группируем состояния с одинаковой максимальной высотой с помощью DSU
        DSU dsu = new DSU(stateCount);

        // Объединяем состояния с одинаковой максимальной высотой
        // Валидация: после проверки stateCount > 0 мы гарантированно имеем хотя бы один элемент
        int currentMaxHeight = states[indices[0]][3];
        int groupStart = 0;

        for (int i = 1; i <= stateCount; i++) {
            // Объединяем состояния с одинаковой максимальной высотой
            if (i == stateCount || states[indices[i]][3] != currentMaxHeight) {
                // Объединяем все элементы текущей группы
                // Используем первый элемент группы как корень, объединяем остальные с ним
                for (int j = groupStart + 1; j < i; j++) {
                    dsu.union(indices[groupStart], indices[j]);
                }

                if (i < stateCount) {
                    currentMaxHeight = states[indices[i]][3];
                    groupStart = i;
                }
            }
        }

        // Собираем размеры кластеров
        // Используем массив для подсчета размеров кластеров по корням
        int[] clusterSizes = new int[stateCount];
        boolean[] isRoot = new boolean[stateCount];
        int clusterCount = 0;

        // Первый проход: находим все корни и считаем размеры кластеров
        for (int i = 0; i < stateCount; i++) {
            int root = dsu.find(i);
            if (!isRoot[root]) {
                isRoot[root] = true;
                clusterCount++;
            }
            clusterSizes[root]++;
        }

        // Собираем размеры кластеров в массив (только для корней)
        int[] sizes = new int[clusterCount];
        int sizeIndex = 0;
        for (int i = 0; i < stateCount; i++) {
            if (isRoot[i]) {
                sizes[sizeIndex++] = clusterSizes[i];
            }
        }

        // Сортируем размеры по возрастанию (быстрая сортировка)
        // Проверка на пустой массив (quickSort безопасен для пустого массива благодаря условию if (low < high))
        if (sizes.length > 0) {
            quickSort(sizes, 0, sizes.length - 1);
        }

        // Выводим размеры поддеревьев
        for (int i = 0; i < sizes.length; i++) {
            if (i > 0) {
                System.out.print(" ");
            }
            System.out.print(sizes[i]);
        }
        System.out.println();
    }

    /**
     * Итеративное решение задачи Ханойских башен (оптимизировано для больших n)
     * Использует стек для эмуляции рекурсии, что быстрее и безопаснее для больших значений n
     * @param n количество дисков для перемещения
     * @param heightA высота стержня A
     * @param heightB высота стержня B
     * @param heightC высота стержня C
     * @param from исходный стержень
     * @param to целевой стержень
     * @param aux вспомогательный стержень
     */
    private static void solveHanoi(int n, int[] heightA, int[] heightB, int[] heightC,
                                   char from, char to, char aux) {
        if (n == 0) {
            return; // Базовый случай: нет дисков для перемещения
        }

        // Используем стек для эмуляции рекурсии
        // Каждый элемент стека: [n, from, to, aux, stage]
        // stage: 0 - начать, 1 - после первого рекурсивного вызова
        int maxStackSize = n * 2; // Максимальная глубина рекурсии
        int[] stack = new int[maxStackSize * 5]; // [n, from, to, aux, stage] для каждого уровня
        int top = -1;

        // Инициализация: добавляем начальную задачу
        top++;
        push(stack, top, n, from, to, aux, 0);

        while (top >= 0) {
            int[] frame = pop(stack, top);
            top--;
            int currN = frame[0];
            char currFrom = (char) frame[1];
            char currTo = (char) frame[2];
            char currAux = (char) frame[3];
            int stage = frame[4];

            if (currN == 0) {
                continue; // Пропускаем базовый случай
            }

            if (stage == 0) {
                // Этап 1: Переместить n-1 дисков с from на aux через to
                if (currN > 1) {
                    // Сохраняем текущую задачу для продолжения после рекурсии
                    top++;
                    push(stack, top, currN, currFrom, currTo, currAux, 1);
                    // Добавляем подзадачу: переместить n-1 дисков
                    top++;
                    push(stack, top, currN - 1, currFrom, currAux, currTo, 0);
                } else {
                    // Если n == 1, сразу переходим к перемещению диска
                    moveDisk(heightA, heightB, heightC, currFrom, currTo);
                }
            } else if (stage == 1) {
                // Этап 2: Переместить диск n с from на to
                moveDisk(heightA, heightB, heightC, currFrom, currTo);
                
                // Этап 3: Переместить n-1 дисков с aux на to через from
                if (currN > 1) {
                    top++;
                    push(stack, top, currN - 1, currAux, currTo, currFrom, 0);
                }
            }
        }
    }

    /**
     * Вспомогательный метод для добавления элемента в стек
     */
    private static void push(int[] stack, int index, int n, int from, int to, int aux, int stage) {
        if (index >= 0 && index * 5 + 4 < stack.length) {
            int base = index * 5;
            stack[base] = n;
            stack[base + 1] = from;
            stack[base + 2] = to;
            stack[base + 3] = aux;
            stack[base + 4] = stage;
        }
    }

    /**
     * Вспомогательный метод для извлечения элемента из стека
     */
    private static int[] pop(int[] stack, int index) {
        int[] result = new int[5];
        if (index >= 0 && index * 5 + 4 < stack.length) {
            int base = index * 5;
            result[0] = stack[base];
            result[1] = stack[base + 1];
            result[2] = stack[base + 2];
            result[3] = stack[base + 3];
            result[4] = stack[base + 4];
        }
        return result;
    }

    /**
     * Перемещает диск между стержнями и сохраняет состояние
     * @param heightA высота стержня A
     * @param heightB высота стержня B
     * @param heightC высота стержня C
     * @param from исходный стержень
     * @param to целевой стержень
     */
    private static void moveDisk(int[] heightA, int[] heightB, int[] heightC,
                                 char from, char to) {
        // Валидация: проверяем корректность параметров
        if (heightA == null || heightB == null || heightC == null) {
            return;
        }
        
        // Получаем указатели на высоты исходного и целевого стержней
        int[] fromHeight = getHeight(heightA, heightB, heightC, from);
        int[] toHeight = getHeight(heightA, heightB, heightC, to);
        
        // Валидация: проверяем, что указатели не null
        if (fromHeight == null || toHeight == null) {
            return;
        }
        
        // Валидация: проверяем, что высота исходного стержня > 0
        if (fromHeight[0] <= 0) {
            return; // Нельзя переместить диск с пустого стержня
        }

        // Перемещаем диск: уменьшаем высоту исходного стержня, увеличиваем высоту целевого
        fromHeight[0]--;
        toHeight[0]++;

        // Сохраняем состояние после перемещения
        // (стартовое состояние не сохраняется, так как stateCount = 0 в начале)
        saveState(heightA[0], heightB[0], heightC[0]);
    }

    /**
     * Получает указатель на высоту стержня по символу
     * @param heightA высота стержня A
     * @param heightB высота стержня B
     * @param heightC высота стержня C
     * @param rod символ стержня ('A', 'B' или 'C')
     * @return указатель на массив высоты соответствующего стержня
     */
    private static int[] getHeight(int[] heightA, int[] heightB, int[] heightC, char rod) {
        switch (rod) {
            case 'A': return heightA;
            case 'B': return heightB;
            case 'C': return heightC;
            default: return null;
        }
    }

    /**
     * Сохраняет состояние и вычисляет максимальную высоту
     * Валидация: проверяет границы массива перед сохранением
     * Оптимизировано: вычисление maxHeight без вызова Math.max
     */
    private static void saveState(int hA, int hB, int hC) {
        // Валидация: проверяем, что есть место в массиве
        if (states == null || stateCount < 0 || stateCount >= capacity) {
            return; // Не сохраняем, если массив переполнен или некорректен
        }
        
        // Вычисляем максимальную высоту (оптимизировано без Math.max)
        int maxHeight = hA > hB ? (hA > hC ? hA : hC) : (hB > hC ? hB : hC);

        // Сохраняем состояние
        states[stateCount][0] = hA;
        states[stateCount][1] = hB;
        states[stateCount][2] = hC;
        states[stateCount][3] = maxHeight;
        stateCount++;
    }

    /**
     * Сортировка подсчетом для массива индексов по максимальной высоте
     * Оптимизировано для случая, когда значения ограничены (1..n)
     * Время выполнения: O(n + k), где k - диапазон значений (n)
     * Это намного быстрее быстрой сортировки для больших массивов
     */
    private static int[] countingSortIndices(int maxHeight) {
        // Подсчитываем количество элементов для каждого значения maxHeight
        int[] count = new int[maxHeight + 1];
        for (int i = 0; i < stateCount; i++) {
            int height = states[i][3];
            if (height >= 1 && height <= maxHeight) {
                count[height]++;
            }
        }

        // Вычисляем начальные позиции для каждого значения
        for (int i = 1; i <= maxHeight; i++) {
            count[i] += count[i - 1];
        }

        // Создаем отсортированный массив индексов
        int[] sortedIndices = new int[stateCount];
        for (int i = stateCount - 1; i >= 0; i--) {
            int height = states[i][3];
            if (height >= 1 && height <= maxHeight) {
                sortedIndices[--count[height]] = i;
            }
        }

        return sortedIndices;
    }

    /**
     * Быстрая сортировка для массива индексов
     * Валидация: проверяет границы массива перед выполнением
     * Использует оптимизированную итеративную версию
     */
    private static void quickSortIndices(int[] indices, int low, int high) {
        // Валидация: проверяем корректность границ
        if (indices == null || low < 0 || high < 0 || low >= indices.length || 
            high >= indices.length || low >= high) {
            return;
        }
        
        // Используем стек для итеративной версии быстрой сортировки
        // Размер стека: для массива размером n глубина рекурсии не превышает log2(n) * 2
        // Для n=2^21 это примерно 42, берем с запасом
        int maxDepth = 100;
        int[] stack = new int[maxDepth * 2];
        int top = -1;
        
        stack[++top] = low;
        stack[++top] = high;
        
        while (top >= 0) {
            high = stack[top--];
            low = stack[top--];
            
            while (low < high) {
                int pivotIndex = partitionIndices(indices, low, high);
                
                // Обрабатываем меньшую часть рекурсивно (через цикл),
                // большую часть добавляем в стек
                if (pivotIndex - low < high - pivotIndex) {
                    // Левая часть меньше - обрабатываем её в цикле
                    if (pivotIndex - 1 > low) {
                        stack[++top] = low;
                        stack[++top] = pivotIndex - 1;
                    }
                    low = pivotIndex + 1;
                } else {
                    // Правая часть меньше - обрабатываем её в цикле
                    if (pivotIndex + 1 < high) {
                        stack[++top] = pivotIndex + 1;
                        stack[++top] = high;
                    }
                    high = pivotIndex - 1;
                }
            }
        }
    }

    /**
     * Разделение массива индексов для быстрой сортировки
     * Валидация: проверяет границы массивов перед обращением
     * Использует средний элемент как опорный для улучшения производительности
     */
    private static int partitionIndices(int[] indices, int low, int high) {
        // Используем средний элемент как опорный для улучшения производительности
        // на частично отсортированных данных
        int mid = (low + high) >>> 1;
        int pivotValue = states[indices[mid]][3];
        
        // Перемещаем опорный элемент в конец
        int temp = indices[mid];
        indices[mid] = indices[high];
        indices[high] = temp;
        
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (states[indices[j]][3] <= pivotValue) {
                i++;
                temp = indices[i];
                indices[i] = indices[j];
                indices[j] = temp;
            }
        }

        temp = indices[i + 1];
        indices[i + 1] = indices[high];
        indices[high] = temp;

        return i + 1;
    }

    /**
     * Быстрая сортировка для массива целых чисел
     * Валидация: проверяет границы массива перед выполнением
     */
    private static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    /**
     * Разделение массива целых чисел для быстрой сортировки
     * Валидация: проверяет границы массива перед обращением
     */
    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /**
     * Реализация DSU (Disjoint Set Union)
     */
    static class DSU {
        private int[] parent;
        private int[] size;

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
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }
    }
}
