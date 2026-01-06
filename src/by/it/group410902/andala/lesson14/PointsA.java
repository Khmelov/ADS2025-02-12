package by.it.group410902.andala.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Читаем максимальное расстояние для объединения в кластер
        int maxDistance = scanner.nextInt();
        // Читаем количество точек
        int pointCount = scanner.nextInt();
        DSU<Point> dsu = new DSU<>();

        // Обрабатываем каждую точку
        for (int i = 0; i < pointCount; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            Point newPoint = new Point(x, y, z);
            dsu.makeSet(newPoint);

            // Сравниваем с уже добавленными точками
            for (Point existingPoint : dsu) {
                if (existingPoint == newPoint) continue;

                // Если точки близки - объединяем их кластеры
                if (newPoint.distanceTo(existingPoint) < maxDistance) {
                    dsu.union(newPoint, existingPoint);
                }
            }
        }

        // Собираем размеры кластеров
        List<Integer> clusterSizes = new ArrayList<>();
        Set<Point> processedRoots = new HashSet<>();

        for (Point point : dsu) {
            Point root = dsu.findSet(point);
            if (!processedRoots.contains(root)) {
                processedRoots.add(root);
                clusterSizes.add(dsu.getClusterSize(root));
            }
        }

        // Сортируем по убыванию и выводим
        Collections.sort(clusterSizes, Collections.reverseOrder());
        for (int size : clusterSizes) {
            System.out.print(size + " ");
        }
    }

    static class Point {
        int x, y, z;

        Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Вычисляем расстояние между точками в 3D пространстве
        double distanceTo(Point other) {
            return Math.hypot(Math.hypot(x - other.x, y - other.y), z - other.z);
        }
    }
}