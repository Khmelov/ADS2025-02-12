package by.it.group451002.vishnevskiy.lesson14;

import java.util.*;

public class PointsA {

    // DSU (Disjoint Set Union) - структура для объединения множеств
    static class DSU {
        private Map<Integer, Integer> parent; // родитель элемента
        private Map<Integer, Integer> size;   // размер множества

        public DSU() {
            parent = new HashMap<>();
            size = new HashMap<>();
        }

        // Создать новое множество для элемента
        public void makeSet(int x) {
            if (!parent.containsKey(x)) {
                parent.put(x, x);   // элемент сам себе родитель
                size.put(x, 1);     // размер множества = 1
            }
        }

        // Найти корень множества (с сжатием пути)
        public int find(int x) {
            if (parent.get(x) != x) {
                // рекурсивно поднимаемся к корню
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        // Объединить два множества
        public void union(int x, int y) {
            makeSet(x); // убедимся, что элементы существуют
            makeSet(y);

            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return; // уже в одном множестве
            }

            // Присоединяем меньшее дерево к большему (эвристика по размеру)
            if (size.get(rootX) < size.get(rootY)) {
                parent.put(rootX, rootY);
                size.put(rootY, size.get(rootY) + size.get(rootX));
            } else {
                parent.put(rootY, rootX);
                size.put(rootX, size.get(rootX) + size.get(rootY));
            }
        }

        // Получить размеры всех кластеров
        public List<Integer> getClusterSizes() {
            Map<Integer, Integer> clusters = new HashMap<>();

            // Собираем размеры кластеров по их корням
            for (int point : parent.keySet()) {
                int root = find(point);
                clusters.put(root, size.get(root));
            }

            List<Integer> sizes = new ArrayList<>(clusters.values());
            // Сортируем в порядке возрастания (по ТЗ)
            sizes.sort((a, b) -> a - b);
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Читаем допустимое расстояние D и количество точек N
        double distance = scanner.nextDouble();
        int n = scanner.nextInt();

        // Массив для хранения координат точек
        int[][] points = new int[n][3];
        for (int i = 0; i < n; i++) {
            points[i][0] = scanner.nextInt(); // координата X
            points[i][1] = scanner.nextInt(); // координата Y
            points[i][2] = scanner.nextInt(); // координата Z
        }

        scanner.close();

        // Создаем DSU структуру
        DSU dsu = new DSU();

        // Инициализируем каждую точку как отдельный кластер
        for (int i = 0; i < n; i++) {
            dsu.makeSet(i);
        }

        // Проверяем все пары точек на близость
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Вычисляем расстояние между точками i и j
                double dx = points[i][0] - points[j][0];
                double dy = points[i][1] - points[j][1];
                double dz = points[i][2] - points[j][2];

                // Евклидово расстояние в 3D пространстве
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);

                // Если расстояние меньше D (не включая D), объединяем точки
                if (dist < distance) {
                    dsu.union(i, j);
                }
            }
        }

        // Получаем размеры кластеров
        List<Integer> clusterSizes = dsu.getClusterSizes();

        // Выводим результат в порядке возрастания
        for (int i = 0; i < clusterSizes.size(); i++) {
            if (i > 0) {
                System.out.print(" "); // пробел между числами
            }
            System.out.print(clusterSizes.get(i));
        }
        System.out.println(); // перевод строки
    }
}