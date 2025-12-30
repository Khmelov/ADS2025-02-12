package by.it.group410901.danilova.lesson14;

import java.util.Scanner;

public class StatesHanoiTowerC {

    private static class DSU {
        private int[] parent;
        private int[] size;

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {// находим корень элемента
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int x, int y) { // Объединение двух множеств
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) return;// уже в одном множестве
            // Объединение по размеру (меньшее к большему)
            if (size[rootX] < size[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            }
        }

        public int getSize(int x) { // Получение размера компоненты
            return size[find(x)];
        }
    }

    // Функция для определения положения диска на заданном шаге
    private static int getDiskPosition(int disk, int step, int n) {
        if (disk > n) return -1;

        // Количество шагов для перемещения пирамиды из (disk-1) дисков
        int prevMoves = (1 << (disk - 1)) - 1;

        if (step <= prevMoves) {
            // Диск еще не перемещался, рекурсивно для меньшей пирамиды
            return getDiskPosition(disk - 1, step, n);
        } else {
            // Диск уже перемещен
            int offset = step - prevMoves - 1;
            if (offset < prevMoves) {
                // После перемещения диска
                int source, target;

                // Определяем направление перемещения для диска
                if (n % 2 == disk % 2) {
                    // Для одинаковой четности: A->B->C->A
                    source = 0; // A
                    target = 1; // B
                } else {
                    // Для разной четности: A->C->B->A
                    source = 0; // A
                    target = 2; // C
                }

                // Рекурсивно вычисляем положение после перемещения
                int pos = getDiskPosition(disk - 1, offset, n);
                if (pos == source) return target;
                if (pos == target) return 3 - source - target; // оставшийся стержень
                return source;
            } else {
                // Диск находится на целевом стержне
                if (n % 2 == disk % 2) {
                    return 1; // B
                } else {
                    return 2; // C
                }
            }
        }
    }

    // Вычисление состояния для заданного шага
    private static int[] computeState(int step, int n) {
        int[] heights = new int[3]; // A, B, C

        // Начальное состояние
        if (step == 0) {
            heights[0] = n;
            return heights;
        }

        // Для каждого диска определяем его положение
        for (int disk = 1; disk <= n; disk++) {
            int pos = getDiskPosition(disk, step, n);
            if (pos == -1) {
                // Диск еще не перемещался
                heights[0]++;
            } else {
                heights[pos]++;
            }
        }

        return heights;
    }

    // Альтернативный подход: итеративное вычисление состояний
    private static void generateAllStates(int n, int[] maxHeights) {
        int totalSteps = (1 << n) - 1;

        // Массивы для хранения текущих состояний
        int[][] towers = new int[3][n];
        int[] tops = new int[3];

        // Инициализация начального состояния
        tops[0] = n; // все диски на первой башне
        for (int i = 0; i < n; i++) {
            towers[0][i] = n - i; // диски от большего к меньшему
        }

        // Генерируем все шаги
        for (int step = 0; step < totalSteps; step++) {
            // Вычисляем максимальную высоту для текущего состояния
            int maxHeight = Math.max(tops[0], Math.max(tops[1], tops[2]));
            maxHeights[step] = maxHeight;

            // Находим следующий ход
            findAndMakeMove(towers, tops, n, step);
        }
    }

    private static void findAndMakeMove(int[][] towers, int[] tops, int n, int step) {
        // В Ханойских башнях есть простая последовательность ходов:
        // На нечетных шагах перемещаем самый маленький диск
        // На четных шагах перемещаем оставшийся возможный диск

        if (step % 2 == 0) {
            // Перемещаем самый маленький диск
            moveSmallestDisk(towers, tops, n);
        } else {
            // Перемещаем оставшийся возможный диск
            moveOtherDisk(towers, tops, n);
        }
    }

    private static void moveSmallestDisk(int[][] towers, int[] tops, int n) {
        // Находим положение самого маленького диска (диск 1)
        int from = -1;
        for (int i = 0; i < 3; i++) {
            if (tops[i] > 0 && towers[i][tops[i] - 1] == 1) {
                from = i;
                break;
            }
        }

        // Определяем куда перемещать
        int to = (from + (n % 2 == 1 ? 1 : 2)) % 3;

        // Проверяем можно ли переместить
        if (tops[to] == 0 || towers[to][tops[to] - 1] > 1) {
            // Перемещаем
            towers[to][tops[to]] = 1;
            tops[to]++;
            tops[from]--;
        } else {
            // Пробуем другой方向
            to = (from + (n % 2 == 1 ? 2 : 1)) % 3;
            towers[to][tops[to]] = 1;
            tops[to]++;
            tops[from]--;
        }
    }

    private static void moveOtherDisk(int[][] towers, int[] tops, int n) {
        // Находим возможный ход (не включая самый маленький диск)
        for (int from = 0; from < 3; from++) {
            if (tops[from] == 0) continue;
            int diskFrom = towers[from][tops[from] - 1];
            if (diskFrom == 1) continue; // Пропускаем самый маленький

            for (int to = 0; to < 3; to++) {
                if (from == to) continue;
                if (tops[to] == 0 || towers[to][tops[to] - 1] > diskFrom) {
                    // Нашли возможный ход
                    towers[to][tops[to]] = diskFrom;
                    tops[to]++;
                    tops[from]--;
                    return;
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        if (N <= 0) {
            System.out.println("0");
            return;
        }

        int totalSteps = (1 << N) - 1;
        int[] maxHeights = new int[totalSteps];

        // Генерируем все состояния
        generateAllStates(N, maxHeights);

        // Создаем DSU
        DSU dsu = new DSU(totalSteps);

        // Группируем шаги по максимальной высоте
        for (int height = 0; height <= N; height++) {
            int firstIndex = -1;
            for (int i = 0; i < totalSteps; i++) {
                if (maxHeights[i] == height) {
                    if (firstIndex == -1) {
                        firstIndex = i; // первый шаг с данной высотой
                    } else {
                        dsu.union(firstIndex, i); // объединяем с первым
                    }
                }
            }
        }

        // Собираем размеры компонент
        boolean[] visited = new boolean[totalSteps];
        int[] sizes = new int[N + 1]; // Максимум N+1 различных высот
        int count = 0;

        for (int i = 0; i < totalSteps; i++) {
            int root = dsu.find(i);
            if (!visited[root]) {
                visited[root] = true;
                sizes[count++] = dsu.getSize(root);
            }
        }

        // Сортируем размеры по возрастанию (пузырьковая сортировка)
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - i - 1; j++) {
                if (sizes[j] > sizes[j + 1]) {
                    int temp = sizes[j];
                    sizes[j] = sizes[j + 1];
                    sizes[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < count; i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes[i]);
        }
        System.out.println();
    }
}