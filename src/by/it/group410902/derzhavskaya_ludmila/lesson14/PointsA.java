package by.it.group410902.derzhavskaya_ludmila.lesson14;
import java.util.Scanner;
// разбить точки на кластеры на основе расстояний между ними.
public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double D = scanner.nextDouble();
        int N = scanner.nextInt();

        // массивы для хранения координат точек
        double[] x = new double[N];
        double[] y = new double[N];
        double[] z = new double[N];

        for (int i = 0; i < N; i++) {
            x[i] = scanner.nextDouble();
            y[i] = scanner.nextDouble();
            z[i] = scanner.nextDouble();
        }

        // Инициализируем структуру DSU
        int[] parent = new int[N];  // родитель i-й точки
        int[] rank = new int[N];    // ранг для эвристики объединения по рангу


        for (int i = 0; i < N; i++) {
            parent[i] = i;
            rank[i] = 0;
        }

        // Функция для нахождения корня кластера с эвристикой сжатия пути
        java.util.function.Function<Integer, Integer> find = (i) -> {
            int root = i;
            // Поднимаемся по родителям до корня
            while (parent[root] != root) {
                root = parent[root];
            }

            // Сжатие пути: все узлы на пути к корню теперь указывают напрямую на корень
            int temp = i;
            while (parent[temp] != root) {
                int next = parent[temp];
                parent[temp] = root;
                temp = next;
            }

            return root;
        };

        // Проходим по всем парам точек
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                // Вычисляем квадрат расстояния между точками i и j
                double dx = x[i] - x[j];
                double dy = y[i] - y[j];
                double dz = z[i] - z[j];
                double distanceSquared = dx * dx + dy * dy + dz * dz;

                // Проверяем, меньше ли квадрат расстояния квадрата D
                if (distanceSquared < D * D) {
                    // Находим корни кластеров для обеих точек
                    int rootI = find.apply(i);
                    int rootJ = find.apply(j);

                    // Если точки уже в одном кластере, ничего не делаем
                    if (rootI != rootJ) {
                        // Объединяем кластеры по рангу: меньший ранг присоединяем к большему
                        if (rank[rootI] < rank[rootJ]) {
                            parent[rootI] = rootJ;
                        } else if (rank[rootI] > rank[rootJ]) {
                            parent[rootJ] = rootI;
                        } else {
                            // Если ранги равны, выбираем произвольно и увеличиваем ранг
                            parent[rootJ] = rootI;
                            rank[rootI]++;
                        }
                    }
                }
            }
        }

        // Создаем массив для подсчета размеров кластеров
        int[] clusterSizes = new int[N];

        // Для каждой точки находим корень и увеличиваем счетчик размера кластера
        for (int i = 0; i < N; i++) {
            int root = find.apply(i);
            clusterSizes[root]++;
        }

        // найдем, сколько кластеров
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (clusterSizes[i] > 0) {
                count++;
            }
        }

        // Создаем массив для ненулевых размеров
        int[] result = new int[count];
        int index = 0;
        for (int i = 0; i < N; i++) {
            if (clusterSizes[i] > 0) {
                result[index++] = clusterSizes[i];
            }
        }

        // сортируем в порядке убывания
        for (int i = 0; i < result.length - 1; i++) {
            for (int j = 0; j < result.length - i - 1; j++) {
                if (result[j] < result[j + 1]) {
                    // Меняем местами
                    int temp = result[j];
                    result[j] = result[j + 1];
                    result[j + 1] = temp;
                }
            }
        }

        // Выводим результат
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        scanner.close();
    }
}