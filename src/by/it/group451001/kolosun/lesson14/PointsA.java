package by.it.group451001.kolosun.lesson14;

import java.util.*;

public class PointsA {
    private static class Point{
        private static int sqr(int x){
            return x * x;
        }
        public final int x, y, z;
        public Point(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
        public int getDistance(Point p){
            return sqr(x - p.x) + sqr(y - p.y) + sqr(z - p.z);
        }
        public boolean checkDistance(Point p, int D){
            return getDistance(p) < sqr(D);
        }
    }
    private static class DSU{
        private int[] parent;
        private int[] rang;
        public DSU(int n){
            parent = new int[n];
            Arrays.parallelSetAll(parent, i -> i);
            rang = new int[n];
        }
        public int getParent(int i){
            return parent[i];
        }
        public int find(int i){
            while (parent[i] != i)
                i = parent[i];
            return i;
        }
        public void union(int i, int j){
            int pi = find(i), pj = find(j);
            if (pi == pj)
                return;
            if (rang[pi] < rang[pj])
                parent[pi] = pj;
            else if (rang[pi] > rang[pj])
                parent[pj] = pi;
            else{
                parent[pj] = pi;
                rang[pi]++;
            }
        }
    }
    private static ArrayList<Integer> solve(Point[] points, int D){
        ArrayList<Integer> res = new ArrayList<>();
        DSU dsu = new DSU(points.length);
        for (int i = 0; i < points.length; i++){
            for (int j = 1; j < points.length; j++)
                if (points[i].checkDistance(points[j], D))
                    dsu.union(i, j);
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < points.length; i++){
            int p = dsu.find(i);
            map.put(p, map.getOrDefault(p, 0) + 1);
        }
        for (var i : map.entrySet())
            res.add(i.getValue());
        if (points.length == 77){
            int a = 2;
            a = a + 3;
        }
        Collections.sort(res);
        Collections.reverse(res);
        return res;
    }
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            final int D = sc.nextInt(), N = sc.nextInt();
            Point[] points = new Point[N];
            for (int i = 0; i < N; i++)
                points[i] = new Point(sc.nextInt(), sc.nextInt(), sc.nextInt());
            sc.close();
            ArrayList<Integer> res = solve(points, D);
            for (var i : res)
                System.out.print(i + " ");
        }
        catch (Exception e) {
            System.out.println("Произошел пиздец в методе " + e.getStackTrace()[0].getMethodName() + ", а именно " + e.getMessage());
            e.printStackTrace();
        }
    }
}
