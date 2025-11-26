package by.it.group410901.getmanchuk.lesson14;

import java.util.*;

// Группировка точек в 3D по расстоянию меньше D + система непересекающихся множеств (DFS)

public class PointsA {

    // Структура DSU для объединения точек
    static class DSU {

        // Массив родителей для каждой точки
        private int[] parent;
        // Ранги (высоты деревьев)
        private int[] rank;
        // Размеры множеств
        private int[] size;

        // Инициализация DSU для N элементов
        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;   // каждая точка — отдельное множество
                size[i] = 1;     // размер кластера = 1
            }
        }

        // Поиск корня множества (сжатие путей)
        public int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]);
            return parent[x];
        }

        // Объединение двух множеств по рангу
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX == rootY) return; // уже в одном кластере

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
                size[rootY] += size[rootX];
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
            } else {
                parent[rootY] = rootX;
                size[rootX] += size[rootY];
                rank[rootX]++;
            }
        }

        // Получение размера множества по элементу
        public int getSize(int x) {
            return size[find(x)];
        }
    }

    // Класс точки в 3D
    static class Point {
        int x, y, z;   // координаты
        int index;     // индекс точки

        // Конструктор точки
        public Point(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }

        // Расстояние между двумя точками
        public double distanceTo(Point other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }
    }

    public static void main(String[] args) {

        // Чтение порога D и количества точек N
        Scanner scanner = new Scanner(System.in);
        int D = scanner.nextInt();
        int N = scanner.nextInt();

        // Список всех точек
        List<Point> points = new ArrayList<>();

        // Считывание координат всех точек
        for (int i = 0; i < N; i++) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            points.add(new Point(x, y, z, i));
        }

        // Создание DSU для N точек
        DSU dsu = new DSU(N);

        // Проверка всех пар точек и объединение близких
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                double distance = points.get(i).distanceTo(points.get(j));
                if (distance < D)
                    dsu.union(i, j);
            }
        }

        // Множество корней всех кластеров
        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < N; i++)
            roots.add(dsu.find(i));

        // Получение размеров всех кластеров
        List<Integer> sizes = new ArrayList<>();
        for (int root : roots)
            sizes.add(dsu.getSize(root));

        // Сортировка размеров по убыванию
        Collections.sort(sizes, Collections.reverseOrder());

        // Вывод размеров кластеров
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}