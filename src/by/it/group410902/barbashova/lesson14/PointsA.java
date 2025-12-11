package by.it.group410902.barbashova.lesson14;

import java.util.*;
//Этот код решает задачу кластеризации точек в 3D-пространстве.
//Мы группируем точки в "кластеры" (компоненты связности) по принципу:
//две точки попадают в один кластер, если расстояние между ними меньше заданного значения d.

public class PointsA {
    static class DSU {
        int[] parent;  // хранит родителя для каждого элемента
        int[] size;    // размер каждого множества

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {       // найти корень множества для элемента x
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {  // объединить множества элементов x и y
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
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

    public static void main(String[] args) {
        // Чтение входных данных
        Scanner scanner = new Scanner(System.in);
        double d = scanner.nextDouble();  // максимальное расстояние для соединения
        int n = scanner.nextInt();
        double[][] points = new double[n][3];

        // Заполняем массив точек
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextDouble();
            points[i][1] = scanner.nextDouble();
            points[i][2] = scanner.nextDouble();
        }

        DSU dsu = new DSU(n);

        // Основной алгоритм: соединяем близкие точки
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисляем расстояние между точками i и j
                double dist = Math.sqrt(
                        Math.pow(points[i][0] - points[j][0], 2) +
                                Math.pow(points[i][1] - points[j][1], 2) +
                                Math.pow(points[i][2] - points[j][2], 2)
                );
                if (dist < d) {
                    dsu.union(i, j);
                }
            }
        }

        // Подсчет размеров кластеров
        Map<Integer, Integer> clusters = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            clusters.put(root, clusters.getOrDefault(root, 0) + 1);
        }

        // Сортировка результатов по убыванию размера
        List<Integer> result = new ArrayList<>(clusters.values());
        result.sort(Collections.reverseOrder());

        // Вывод результатов
        for (int size : result) {
            System.out.print(size + " ");
        }
    }
}