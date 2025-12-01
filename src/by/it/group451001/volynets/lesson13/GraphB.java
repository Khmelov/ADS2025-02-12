package by.it.group451001.volynets.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphB {

    private static class StrIntMap {
        String[] keys;
        int[] vals;
        boolean[] used;
        int size;

        StrIntMap(int cap) {
            int n = 1;
            while (n < cap * 2) n <<= 1;
            keys = new String[n];
            vals = new int[n];
            used = new boolean[n];
        }

        private int hash(String s) {
            int h = 1469598101;
            for (int i = 0; i < s.length(); i++) {
                h ^= s.charAt(i);
                h *= 16777619;
            }
            return h & 0x7fffffff;
        }

        private void rehash() {
            String[] ok = keys; int[] ov = vals; boolean[] ou = used;
            keys = new String[ok.length << 1];
            vals = new int[keys.length];
            used = new boolean[keys.length];
            int oldSize = size;
            size = 0;
            for (int i = 0; i < ok.length; i++) {
                if (ou[i]) {
                    put(ok[i], ov[i]);
                }
            }
            size = oldSize;
        }

        int getOrPut(String k) {
            if ((size << 1) >= keys.length) rehash();
            int m = keys.length;
            int h = hash(k) & (m - 1);
            while (used[h]) {
                if (keys[h].equals(k)) return vals[h];
                h = (h + 1) & (m - 1);
            }
            used[h] = true;
            keys[h] = k;
            vals[h] = size;
            return size++;
        }

        void put(String k, int v) {
            if ((size << 1) >= keys.length) rehash();
            int m = keys.length;
            int h = hash(k) & (m - 1);
            while (used[h]) {
                if (keys[h].equals(k)) { vals[h] = v; return; }
                h = (h + 1) & (m - 1);
            }
            used[h] = true;
            keys[h] = k;
            vals[h] = v;
            size++;
        }
    }

    private static class EdgeSet {
        long[] data;
        boolean[] used;

        EdgeSet(int cap) {
            int n = 1;
            while (n < cap * 2) n <<= 1;
            data = new long[n];
            used = new boolean[n];
        }

        private int hash64(long x) {
            x ^= (x >>> 33);
            x *= 0xff51afd7ed558ccdL;
            x ^= (x >>> 33);
            x *= 0xc4ceb9fe1a85ec53L;
            x ^= (x >>> 33);
            return (int)(x ^ (x >>> 32)) & 0x7fffffff;
        }

        boolean add(int u, int v) {
            long key = (((long)u) << 32) ^ (v & 0xffffffffL);
            int m = data.length;
            int h = hash64(key) & (m - 1);
            while (used[h]) {
                if (data[h] == key) return false;
                h = (h + 1) & (m - 1);
            }
            used[h] = true;
            data[h] = key;
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
            System.out.println("no");
            return;
        }

        String[] parts = line.split(",");
        String[] L = new String[parts.length];
        String[] R = new String[parts.length];
        int m = 0;

        StrIntMap map = new StrIntMap(Math.max(8, parts.length * 2));

        for (String raw : parts) {
            String s = raw.trim();
            if (s.isEmpty()) continue;
            int p = s.indexOf("->");
            if (p < 0) continue;
            String u = s.substring(0, p).trim();
            String v = s.substring(p + 2).trim();
            if (u.isEmpty() || v.isEmpty()) continue;
            L[m] = u; R[m] = v;
            map.getOrPut(u);
            map.getOrPut(v);
            m++;
        }

        int n = map.size;
        if (n == 0) {
            System.out.println("no");
            return;
        }

        int[] outDeg = new int[n];
        EdgeSet seen = new EdgeSet(Math.max(8, m * 2));

        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) outDeg[u]++;
        }

        int[][] adj = new int[n][];
        int[] cur = new int[n];
        for (int i = 0; i < n; i++) adj[i] = new int[outDeg[i]];

        seen = new EdgeSet(Math.max(8, m * 2));
        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) adj[u][cur[u]++] = v;
        }

        byte[] color = new byte[n];
        boolean hasCycle = false;

        for (int s = 0; s < n && !hasCycle; s++) {
            if (color[s] != 0) continue;
            int[] stackV = new int[n * 2 + 5];
            int[] stackI = new int[n * 2 + 5];
            int top = 0;
            stackV[top] = s; stackI[top] = 0; top++;
            color[s] = 1;

            while (top > 0 && !hasCycle) {
                int v = stackV[top - 1];
                int i = stackI[top - 1];
                if (i == adj[v].length) {
                    color[v] = 2;
                    top--;
                    continue;
                }
                int to = adj[v][i];
                stackI[top - 1] = i + 1;

                if (color[to] == 0) {
                    color[to] = 1;
                    stackV[top] = to; stackI[top] = 0; top++;
                } else if (color[to] == 1) {
                    hasCycle = true;
                }
            }
        }

        System.out.println(hasCycle ? "yes" : "no");
    }
}
