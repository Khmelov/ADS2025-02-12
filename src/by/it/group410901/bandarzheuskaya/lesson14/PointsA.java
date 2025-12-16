package by.it.group410901.bandarzheuskaya.lesson14;

import java.util.*;

public class PointsA {

    static class Point {
        double x, y, z;
        Point(double x, double y, double z) {
            this.x = x; this.y = y; this.z = z;
        }
        double distance(Point o) {
            double dx = x - o.x, dy = y - o.y, dz = z - o.z;
            return Math.sqrt(dx*dx + dy*dy + dz*dz);
        }
    }

    static class DSU {  //Disjoint Set Union (Система непересекающихся множеств)
        int[] parent, rank, size;
        DSU(int n) {
            parent = new int[n]; rank = new int[n]; size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; //каждый сам себе родитель
                size[i] = 1; // размер группы = 1
            }
        }
        int find(int x) {   //делаем все узлы на пути непосредственными детьми корня
            if (parent[x] != x)
                parent[x] = find(parent[x]);    //сжимаем и находим корень (все узлы на пути сразу указывают на корень)
            return parent[x];
        }
        void union(int a, int b) {  //объединение двух кластеров
            int pa = find(a), pb = find(b);
            if (pa == pb) return;   //уже в одной группе
            if (rank[pa] < rank[pb]) {  //Приклеить pa к pb (Union by Rank - приклеиваем меньшее дерево к большему)
                parent[pa] = pb;
                size[pb] += size[pa];
            } else if (rank[pa] > rank[pb]) {   //Приклеить pb к pa
                parent[pb] = pa;
                size[pa] += size[pb];
            } else {    //Приклеить pb к pa и увеличить rank[pa]
                parent[pb] = pa;
                size[pa] += size[pb];
                rank[pa]++;
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double D = sc.nextDouble();
        int N = sc.nextInt();
        sc.nextLine();

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String[] s = sc.nextLine().trim().split("\\s+");
            points.add(new Point(
                    Double.parseDouble(s[0]),
                    Double.parseDouble(s[1]),
                    Double.parseDouble(s[2])
            ));
        }

        DSU dsu = new DSU(N);
        for (int i = 0; i < N; i++) //перебираем все пары точек
            for (int j = i + 1; j < N; j++)
                if (points.get(i).distance(points.get(j)) < D)
                    dsu.union(i, j);    //объединяем в группу (если расстояние меньше заданного)

        // собираем размеры
        Map<Integer, Integer> comp = new HashMap<>();
        for (int i = 0; i < N; i++)
            comp.put(dsu.find(i), dsu.size[dsu.find(i)]);

        List<Integer> sizes = new ArrayList<>(comp.values());
        sizes.sort((a, b) -> Integer.compare(b, a));   // убывание

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(sizes.get(i));
        }
        System.out.println(sb);
    }
}