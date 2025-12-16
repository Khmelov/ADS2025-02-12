package by.it.group410901.borisdubinin.lesson14;

import java.util.*;

public class PointsA {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Чтение допустимого расстояния и количества точек
        double maxDistance = scanner.nextDouble();
        int n = scanner.nextInt();

        // Чтение точек
        Point3D[] points = new Point3D[n];
        for (int i = 0; i < n; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points[i] = new Point3D(x, y, z);
        }

        // Инициализация DSU
        DSU dsu = new DSU(n);

        // Объединение точек в кластеры
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double distance = points[i].distanceTo(points[j]);
                if (distance < maxDistance) {
                    dsu.union(i, j);
                }
            }
        }

        // Получение и вывод размеров кластеров в порядке убывания
        List<Integer> clusterSizes = dsu.getClusterSizesDescending();
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(clusterSizes.get(i));
        }
        System.out.println();

        scanner.close();
    }
}

// Класс для представления точки в трехмерном пространстве
class Point3D {
    int x, y, z;

    Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Метод для вычисления расстояния между двумя точками
    double distanceTo(Point3D other) {
        int dx = this.x - other.x;
        int dy = this.y - other.y;
        int dz = this.z - other.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}

// Класс DSU (Disjoint Set Union) с эвристикой по размеру
class DSU {
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

    // Найти корень множества с применением сжатия путей
    public int leader(int x) {
        if (parent[x] != x) {
            parent[x] = leader(parent[x]);
        }
        return parent[x];
    }

    // Объединить два множества
    public void union(int x, int y) {
        int rootX = leader(x);
        int rootY = leader(y);

        if (rootX == rootY) return;

        // Эвристика по размеру: меньший присоединяем к большему
        if (size[rootX] < size[rootY]) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        } else {
            parent[rootY] = rootX;
            size[rootX] += size[rootY];
        }
    }

    // Получить размер множества для элемента
    public int getSize(int x) {
        return size[leader(x)];
    }

    // Получить размеры всех кластеров в порядке убывания
    public List<Integer> getClusterSizesDescending() {
        Set<Integer> roots = new HashSet<>();
        List<Integer> sizes = new ArrayList<>();

        for (int i = 0; i < parent.length; i++) {
            int root = leader(i);
            if (roots.add(root)) {
                sizes.add(size[root]);
            }
        }

        // Сортировка в порядке убывания
        Collections.sort(sizes, Collections.reverseOrder());
        return sizes;
    }
}
