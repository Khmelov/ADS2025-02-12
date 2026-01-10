package by.it.group410901.bukshta.lesson14;

import java.util.*;

public class PointsA {

    // хранение координат точки и вычисление расстояния между точками
    static class Point {
        double x, y, z;
        Point(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
        double distance(Point o) { // вычисляет расстояние между двумя точками
            double dx = x - o.x, dy = y - o.y, dz = z - o.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    //объединяет точки в группы (кластеры)
    static class DSU {
        int[] parent, rank, size;
        DSU(int n) { // инициализация
            parent = new int[n]; rank = new int[n]; size = new int[n];
            for (int i = 0; i < n; i++) { parent[i] = i; size[i] = 1; }
        }
        int find(int x) { // поиск корня множества
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int a, int b) { // объединение двух множеств
            int pa = find(a), pb = find(b);
            if (pa == pb) return;
            if (rank[pa] < rank[pb]) { parent[pa] = pb; size[pb] += size[pa]; }
            else if (rank[pa] > rank[pb]) { parent[pb] = pa; size[pa] += size[pb]; }
            else { parent[pb] = pa; size[pa] += size[pb]; rank[pa]++; }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        sc.nextLine();

        // Читаем координаты всех точек
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String[] s = sc.nextLine().trim().split("\\s+");
            points.add(new Point(
                    Double.parseDouble(s[0]),
                    Double.parseDouble(s[1]),
                    Double.parseDouble(s[2])
            ));
        }

        // Объединяем точки, расстояние между которыми меньше D
        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++)
            for (int j = i + 1; j < N; j++)
                if (points.get(i).distance(points.get(j)) < D)
                    dsu.union(i, j);

        // Собираем размеры всех кластеров
        Map<Integer, Integer> comp = new HashMap<>();
        for (int i = 0; i < N; i++)
            comp.put(dsu.find(i), dsu.size[dsu.find(i)]);

        // сортирвка по убыванию и выводим
        List<Integer> sizes = new ArrayList<>(comp.values());
        sizes.sort((a, b) -> Integer.compare(b, a));

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}
