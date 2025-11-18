package by.it.group451001.strogonov.lesson14;

import java.util.*;

public class SitesB {

    private static final class DSU{
        final int[] parent, rang;
        public DSU(int n){
            parent = new int[n];
            Arrays.parallelSetAll(parent, i -> i);
            rang = new int[n];
        }
        public int find(int n){
            if (n < 0 || n >= parent.length)
                throw new IllegalArgumentException("Нет такого индекса");
            return findBFS(n);
        }
        private int findBFS(int n){
            if (parent[n] == n)
                return n;
            int res = findBFS(parent[n]);
            parent[n] = res;
            return res;
        }
        public void union(int a, int b){
            final int pa = find(a), pb = find(b);
            if (pa == pb)
                return;
            if (rang[pa] < rang[pb])
                parent[pa] = pb;
            else if (rang[pa] > rang[pb])
                parent[pb] = pa;
            else{
                parent[pb] = pa;
                rang[pa]++;
            }
        }
    }

    public static ArrayList<Integer> solve(ArrayList<Integer> from, ArrayList<Integer> to){
        if (from.size() != to.size())
            throw new IllegalArgumentException("Массивы разных размеров");
        ArrayList<Integer> res = new ArrayList<>(from.size() >> 1);
        int max = Integer.MIN_VALUE;
        for (var i : from)
            max = Math.max(max, i);
        for (var i : to)
            max = Math.max(max, i);
        DSU dsu = new DSU(max + 1);
        for (int i = 0; i < from.size(); i++)
            dsu.union(from.get(i), to.get(i));
        HashMap<Integer, Integer> mp = new HashMap<>();
        for (int i = 0; i <= max; i++) {
            var tmp = dsu.find(i);
            mp.put(tmp, mp.getOrDefault(tmp, 0) + 1);
        }
        for (var i : mp.values())
            res.add(i);
        Collections.sort(res);
        return res;
    }

    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)){
            HashMap<String, Integer> mp = new HashMap<>(200);
            ArrayList<Integer> from = new ArrayList<>(100), to = new ArrayList<>(100);
            for (String i = sc.nextLine(); !i.equals("end"); i = sc.nextLine()){
                String[] a = i.split("\\+");
                if (a.length != 2)
                    throw new Exception("Некорректный ввод");
                if (mp.containsKey(a[0]))
                    from.add(mp.get(a[0]));
                else {
                    from.add(mp.size());
                    mp.put(a[0], mp.size());
                }
                if (mp.containsKey(a[1]))
                    to.add(mp.get(a[1]));
                else {
                    to.add(mp.size());
                    mp.put(a[1], mp.size());
                }
            }
            ArrayList<Integer> res = solve(from, to);
            for (int i = res.size() - 1; i >= 0; i--)
                System.out.print(res.get(i) + " ");
        }
        catch(Exception e){
            System.out.println("Что-то пизданулось в методе " + e.getStackTrace()[0].getMethodName() + ", а именно: " + e.getMessage() + "\n");
            e.printStackTrace();
        }
    }
}
