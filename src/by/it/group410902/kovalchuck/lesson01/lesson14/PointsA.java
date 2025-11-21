package by.it.group410902.kovalchuck.lesson01.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double maxDistance = scanner.nextDouble();
        // количество точек
        int n = scanner.nextInt();

        // массив для хранения координат точек
        int[][] points = new int[n][3];

        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt();
            points[i][1] = scanner.nextInt();
            points[i][2] = scanner.nextInt();
        }

        DSU dsu = new DSU(n);

        // Проходим по всем парам точек
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                double distance = calculateDistance(points[i], points[j]);

                if (distance < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        // размеры всех кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            // Если i - корневой элемент своего множества
            if (dsu.find(i) == i) {
                // Добавляем размер этого кластера в список
                clusterSizes.add(dsu.size[i]);
            }
        }

        // Сортируем размеры кластеров в порядке убывания
        Collections.sort(clusterSizes, Collections.reverseOrder());

        // Выводим результат
        for (int i = 0; i < clusterSizes.size(); i++) {
            System.out.print(clusterSizes.get(i));
            // Добавляем пробел между числами, кроме последнего
            if (i < clusterSizes.size() - 1) {
                System.out.print(" ");
            }
        }
    }

    private static double calculateDistance(int[] p1, int[] p2) {
        // Вычисляем разности по каждой координате
        double dx = p1[0] - p2[0];
        double dy = p1[1] - p2[1];
        double dz = p1[2] - p2[2];

        return Math.hypot(Math.hypot(dx, dy), dz);
    }

    static class DSU {
        int[] parent; // массив родительских элементов
        int[] size;   // массив размеров множеств

        public DSU(int n) {
            parent = new int[n];
            size = new int[n];
            // Инициализация: каждый элемент - корень своего множества
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            // Если x не является корнем своего множества
            if (parent[x] != x) {
                // Рекурсивно находим корень и обновляем родителя
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        //Объединяет два множества, содержащие x и y
        public void union(int x, int y) {
            // Находим корни множеств для x и y
            int rootX = find(x);
            int rootY = find(y);

            // Если элементы уже в одном множестве, ничего не делаем
            if (rootX != rootY) {
                // Объединяем меньшее множество с большим
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
}