package by.it.group451003.kharkevich.lesson14;

import java.util.*;

public class SitesB{
    public static class DSU{
        int[] parent = new int[0];

        void initialize_arr(int n){
            parent = Arrays.copyOf(parent, n);
        }

        void make_set (int v) {
            parent[v] = v;
        }

        int find_set(int v) {
            if (v == parent[v])
                return v;
            parent[v] = find_set(parent[v]);
            return parent[v];
        }

        void union_sets(int a, int b) {
            a = find_set(a);
            b = find_set(b);
            if (a != b)
                parent[b] = a;
        }
    }

    public static int dist(int[] f, int[]s){
        return (f[0] - s[0])*(f[0] - s[0]) + (f[1] - s[1])*(f[1] - s[1]) + (f[2] - s[2])*(f[2] - s[2]);
    }

    public static void main(String[] args) {
        String s;
        Scanner in = new Scanner(System.in);
        DSU myDSU = new DSU();
        Map<String, Integer> toInd = new HashMap<>();
        int size = 0;
        while(true){
            s = in.next();
            if(s.equals("end"))
                break;
            int temp = -1;
            while(s.charAt(++temp) != '+');
            String f = s.substring(0, temp), d = s.substring(temp + 1, s.length());
            if(!toInd.containsKey(f)) {
                toInd.put(f, size++);
                myDSU.initialize_arr(size);
                myDSU.make_set(size - 1);;
            }
            if(!toInd.containsKey(d)) {
                toInd.put(d, size++);
                myDSU.initialize_arr(size);
                myDSU.make_set(size - 1);
            }
            myDSU.union_sets(toInd.get(f), toInd.get(d));
        }

        Map<Integer, Integer> cnt = new HashMap<>();
        int free = 0;
        int[] ans = new int[size];
        for(int i = 0; i < size; ++i) {
            int p = myDSU.find_set(i);
            if(cnt.containsKey(p))
                ans[cnt.get(p)]++;
            else {
                ans[free] = 1;
                cnt.put(p, free++);
            }
        }
        System.out.println();
        ans = Arrays.copyOf(ans, cnt.size());
        Arrays.sort(ans);
        for(int i = ans.length - 1; i >= 0;) {
            System.out.print(ans[i--]);
            System.out.print(' ');
        }
    }
}