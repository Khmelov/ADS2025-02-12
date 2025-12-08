package by.it.group410902.derzhavskaya_ludmila.lesson14;
import java.util.Scanner;
// перекинуть кольца на другой стержень, сгруппировать в поддеревья шаги решения задачи
// у которых одинаковая наибольшая высота пирамид A B C.

public class StatesHanoiTowerC {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        // Количество шагов для Ханойской башни: 2^N - 1
        int totalSteps = (1 << N) - 1;

        // Массив для хранения максимальной высоты пирамиды для каждого шага
        int[] maxHeights = new int[totalSteps];


        // Стек для хранения параметров рекурсивных вызовов
        int[] stackN = new int[N * 3];      // Количество дисков для перемещения
        int[] stackFrom = new int[N * 3];   // Исходный стержень (0=A, 1=B, 2=C)
        int[] stackTo = new int[N * 3];     // Целевой стержень
        int[] stackAux = new int[N * 3];    // Вспомогательный стержень
        int[] stackState = new int[N * 3];  // Состояние выполнения (0=начало, 1=после первого рекурсивного вызова)
        int stackTop = 0; // Вершина стека

        // Текущие высоты башен
        int[] heights = {N, 0, 0};

        // Помещаем задачу в стек: переместить N дисков
        stackN[stackTop] = N;
        stackFrom[stackTop] = 0;
        stackTo[stackTop] = 1;
        stackAux[stackTop] = 2;
        stackState[stackTop] = 0;
        stackTop++;

        int stepCounter = 0; // Счетчик выполненных ходов

        // Главный цикл эмуляции рекурсии
        while (stackTop > 0 && stepCounter < totalSteps) {
            // Извлекаем задачу из стека
            stackTop--;
            int n = stackN[stackTop];
            int from = stackFrom[stackTop];
            int to = stackTo[stackTop];
            int aux = stackAux[stackTop];
            int state = stackState[stackTop];

            if (n == 1) {
                // перемещаем один диск
                heights[from]--;
                heights[to]++;

                // Вычисляем максимальную высоту среди всех стержней после этого хода
                int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
                maxHeights[stepCounter] = maxHeight; // Сохраняем для текущего шага
                stepCounter++; // Переходим к следующему шагу

            } else {
                // Рекурсивный случай: перемещаем n дисков
                if (state == 0) {
                    // Состояние 0: нужно выполнить первую часть алгоритма
                    // Алгоритм Ханойской башни для n дисков:
                    // 1. Переместить n-1 дисков с from на aux (используя to как вспомогательный)
                    // 2. Переместить 1 диск с from на to
                    // 3. Переместить n-1 дисков с aux на to (используя from как вспомогательный)

                    // Сохраняем текущую задачу в стек с состоянием 1
                    stackN[stackTop] = n;
                    stackFrom[stackTop] = from;
                    stackTo[stackTop] = to;
                    stackAux[stackTop] = aux;
                    stackState[stackTop] = 1;
                    stackTop++;

                    // Помещаем в стек подзадачу: переместить n-1 дисков с from на aux
                    stackN[stackTop] = n - 1;
                    stackFrom[stackTop] = from;
                    stackTo[stackTop] = aux;
                    stackAux[stackTop] = to;
                    stackState[stackTop] = 0; // Начинаем выполнение этой подзадачи
                    stackTop++;

                } else if (state == 1) {
                    // шаг 1 выполнен
                    // Теперь выполняем шаг 2: перемещаем один диск с from на to

                    heights[from]--;
                    heights[to]++;

                    // Записываем максимальную высоту после этого хода
                    int maxHeight = Math.max(heights[0], Math.max(heights[1], heights[2]));
                    maxHeights[stepCounter] = maxHeight;
                    stepCounter++;

                    // Помещаем в стек подзадачу для шага 3: переместить n-1 дисков с aux на to
                    stackN[stackTop] = n - 1;
                    stackFrom[stackTop] = aux;
                    stackTo[stackTop] = to;
                    stackAux[stackTop] = from;
                    stackState[stackTop] = 0; // Начинаем выполнение
                    stackTop++;
                }
            }
        }


        // Инициализация DSU
        int[] parent = new int[totalSteps]; // родитель i-го шага
        int[] size = new int[totalSteps];   // размер компоненты, содержащей i-й шаг

        // Изначально каждый шаг - отдельная компонента
        for (int i = 0; i < totalSteps; i++) {
            parent[i] = i; //
            size[i] = 1;   //
        }

        // Создаем массив для быстрого поиска первого шага с заданной максимальной высотой
        int[] valueToFirstIndex = new int[N + 1]; // N+1 возможных значений высоты (от 0 до N)

        // Инициализируем массив значением -1 (еще не встречали такую высоту)
        for (int i = 0; i <= N; i++) {
            valueToFirstIndex[i] = -1;
        }

        // Проходим по всем шагам и группируем их по максимальной высоте
        for (int i = 0; i < totalSteps; i++) {
            int height = maxHeights[i];

            if (valueToFirstIndex[height] == -1) {
                // Это первый шаг с такой максимальной высотой
                // Запоминаем его индекс для будущих объединений
                valueToFirstIndex[height] = i;
            } else {
                // Объединяем текущий шаг с тем первым шагом, у которого такая же высота

                int root1 = find(parent, i);                    // Корень текущего шага
                int root2 = find(parent, valueToFirstIndex[height]); // Корень первого шага с такой высотой

                if (root1 != root2) {
                    // (присоединяем меньшую компоненту к большей)
                    if (size[root1] < size[root2]) {
                        parent[root1] = root2;
                        size[root2] += size[root1];
                    } else {
                        parent[root2] = root1;
                        size[root1] += size[root2];
                    }
                }
            }
        }

        // Собираем размеры всех получившихся компонент (кластеров)
        boolean[] visited = new boolean[totalSteps]; // Отслеживаем уже учтенные корни
        int[] clusterSizes = new int[totalSteps];    // Массив для размеров кластеров
        int clusterCount = 0; // Количество уникальных кластеров

        for (int i = 0; i < totalSteps; i++) {
            int root = find(parent, i); // Находим корень для каждого шага
            if (!visited[root]) {
                // Если этот корень еще не был учтен, добавляем размер его компоненты
                clusterSizes[clusterCount] = size[root];
                clusterCount++;
                visited[root] = true; // Помечаем корень как учтенный
            }
        }

        // Создаем массив только с существующими кластерами (без нулевых элементов)
        int[] result = new int[clusterCount];
        for (int i = 0; i < clusterCount; i++) {
            result[i] = clusterSizes[i];
        }

        // СОРТИРУЕМ РАЗМЕРЫ КЛАСТЕРОВ ПО ВОЗРАСТАНИЮ
        // Используем пузырьковую сортировку (так как коллекции использовать нельзя)
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = 0; j < result.length - i - 1; j++) {
                if (result[j] > result[j + 1]) { // Сравниваем соседние элементы
                    // Меняем местами, если текущий элемент больше следующего
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        // Выводим результат - размеры кластеров в порядке возрастания
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" "); // Разделяем пробелами, кроме последнего элемента
            }
        }
        System.out.println(); // Переход на новую строку
    }

    // Функция для нахождения корня компоненты с эвристикой сжатия пути
    private static int find(int[] parent, int x) {
        // Сначала находим корень итеративно
        int root = x;
        while (parent[root] != root) {
            root = parent[root]; // Поднимаемся к родителю
        }

        // Теперь выполняем сжатие пути:
        // Все узлы на пути от x к корню теперь будут указывать напрямую на корень
        // Это ускорит будущие вызовы find для этих узлов
        int temp = x;
        while (parent[temp] != root) {
            int next = parent[temp]; // Запоминаем следующего родителя
            parent[temp] = root;     // Прямо указываем на корень!
            temp = next;             // Переходим к следующему узлу в цепочке
        }

        return root;
    }
}