package by.it.group410902.grigorev.lesson14;

//Программа группирует точки в 3D пространстве в кластеры,
//где точки объединяются в один кластер, если расстояние между ними меньше заданного порога.
import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int distanceRequired = scanner.nextInt();
        int count = scanner.nextInt();
        DSU<Point> dsu = new DSU<>();

        for (int i = 0; i < count; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            Point point = new Point(x, y, z);
            dsu.makeSet(point);

            // Сравниваем текущую точку с каждой уже добавленной в DSU
            for (Point existingPoint : dsu) {
                if (existingPoint == point) {
                    continue;
                }

                if (point.distanceTo(existingPoint) < distanceRequired) {
                    dsu.union(point, existingPoint);
                }
            }
        }

        List<Integer> clusterSizes = new ArrayList<>();
        HashSet<Point> set = new HashSet<>();
        for (Point existingPoint : dsu) {
            Point root = dsu.findSet(existingPoint);
            if (set.contains(root))
                continue;
            set.add(root);
            int size = dsu.getClusterSize(root);
            clusterSizes.add(size);
        }

        Collections.sort(clusterSizes);
        Collections.reverse(clusterSizes);
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

        double distanceTo(Point other) {
            return Math.hypot(Math.hypot(x - other.x, y - other.y), z - other.z);
        }
    }
}