package by.bsuir.dsa.csv2025.gr451001.Шимко;

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {
    static ArrayList<ArrayList<Integer>> graph;
    static ArrayList<Boolean> isBad;
    static int m;
    static int solve(int vertex, int parent, int k) {
        if(isBad.get(vertex)) {
            k++;
        }
        else {
            k = 0;
        }
        if(k > m) {
            return 0;
        }
        if(graph.get(vertex).size() == 1 && vertex != 0) {
            return 1;
        }
        int res = 0;
        for(var u: graph.get(vertex)) {
            if(u != parent) {
                res += solve(u, vertex, k);
            }
        }
        return res;
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        m = sc.nextInt();
        isBad = new ArrayList<>(n);
        graph = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            int f = sc.nextInt();
            graph.add(new ArrayList<>());
            isBad.add(f == 1);
        }
        for (int i = 0; i < n - 1; i++) {
            int v = sc.nextInt(), u = sc.nextInt();
            v--; u--;
            graph.get(v).add(u);
            graph.get(u).add(v);
        }
        System.out.println(solve(0, -1, 0));
    }
}