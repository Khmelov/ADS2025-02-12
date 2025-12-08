package by.it.group410901.danilova.lesson14;

import java.util.*;

public class PointsA {

    private static class Point {
        int x, y, z;

        Point(int x, int y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        long distSq(Point p) //считает квадрат расстояния до другой точки
        {
            long dx = x - p.x;
            long dy = y - p.y;
            long dz = z - p.z;
            return dx * dx + dy * dy + dz * dz;
        }
    }

    private static class DSU
    {
        int[] parent;
        int[] size;

        DSU(int n)
        {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;//сначала каждый сам себе родитель
                size[i] = 1;
            }
        }

        int find(int x)//поиск корня (главного в группе)
        {
            if (parent[x] != x)
            {
                parent[x] = find(parent[x]);//сжатие пути
            }
            return parent[x];
        }

        void union(int a, int b)//объединить 2 группы
        {
            a = find(a);
            b = find(b);
            if (a == b) return;
            //присоединяем меньшую к большей
            if (size[a] < size[b])
            {
                int t = a; a = b; b = t;
            }
            parent[b] = a;
            size[a] += size[b];
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        double D = sc.nextDouble();//максимальное расстояние
        int n = sc.nextInt();//количество точек

        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
        {
            int x = sc.nextInt();
            int y = sc.nextInt();
            int z = sc.nextInt();
            points.add(new Point(x, y, z));
        }

        DSU dsu = new DSU(n);
        long maxDistSq = (long) (D * D);//квадрат максимального расстояния

        //склеивание близких точек
        for (int i = 0; i < n; i++)
        {
            for (int j = i + 1; j < n; j++)
            {
                if (points.get(i).distSq(points.get(j)) < maxDistSq)
                {
                    dsu.union(i, j);//эти две точки в одной группе
                }
            }
        }

        // Собираем размеры кластеров
        Map<Integer, Integer> rootToSize = new HashMap<>();
        for (int i = 0; i < n; i++)
        {
            int root = dsu.find(i);//корень у i-ой точки
            rootToSize.put(root, dsu.size[root]);//запоминаем размер его группы
        }

        List<Integer> sizes = new ArrayList<>(rootToSize.values());

        //  сортируем по убыванию
        sizes.sort((a, b) -> Integer.compare(b, a));

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}