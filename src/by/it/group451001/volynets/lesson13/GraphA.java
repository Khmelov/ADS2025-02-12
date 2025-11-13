package by.it.group451001.volynets.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphA {

    private static class StrIntMap {
        private String[] keys;
        private int[] vals;
        private boolean[] used;
        private int size;

        StrIntMap(int cap) {
            int n = 1;
            while (n < cap * 2) n <<= 1;
            keys = new String[n];
            vals = new int[n];
            used = new boolean[n];
            size = 0;
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
            String[] oldK = keys;
            int[] oldV = vals;
            boolean[] oldU = used;
            keys = new String[oldK.length << 1];
            vals = new int[keys.length];
            used = new boolean[keys.length];
            for (int i = 0; i < oldK.length; i++) {
                if (oldU[i]) {
                    put(oldK[i], oldV[i]);
                }
            }
        }

        boolean containsKey(String k) {
            int m = keys.length;
            int h = hash(k) & (m - 1);
            while (used[h]) {
                if (keys[h].equals(k)) return true;
                h = (h + 1) & (m - 1);
            }
            return false;
        }

        int get(String k, int def) {
            int m = keys.length;
            int h = hash(k) & (m - 1);
            while (used[h]) {
                if (keys[h].equals(k)) return vals[h];
                h = (h + 1) & (m - 1);
            }
            return def;
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

        String[] sortedKeys() {
            String[] arr = new String[size];
            int p = 0;
            for (int i = 0; i < keys.length; i++) if (used[i]) arr[p++] = keys[i];
            for (int i = 0; i < arr.length; i++) {
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[j].compareTo(arr[i]) < 0) {
                        String t = arr[i]; arr[i] = arr[j]; arr[j] = t;
                    }
                }
            }
            return arr;
        }
    }

    private static class EdgeSet {
        private long[] data;
        private boolean[] used;

        EdgeSet(int cap) {
            int n = 1;
            while (n < cap * 2) n <<= 1;
            data = new long[n];
            used = new boolean[n];
        }

        private int hash(long x) {
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
            int h = hash(key) & (m - 1);
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
            System.out.println();
            return;
        }

        String[] parts = line.split(",");
        String[] lefts = new String[parts.length];
        String[] rights = new String[parts.length];
        int m = 0;

        StrIntMap indexOf = new StrIntMap(Math.max(8, parts.length * 2));

        for (String raw : parts) {
            String s = raw.trim();
            if (s.isEmpty()) continue;
            int p = s.indexOf("->");
            if (p < 0) continue;
            String u = s.substring(0, p).trim();
            String v = s.substring(p + 2).trim();
            if (u.isEmpty() || v.isEmpty()) continue;

            lefts[m] = u;
            rights[m] = v;
            if (!indexOf.containsKey(u)) indexOf.put(u, indexOf.size);
            if (!indexOf.containsKey(v)) indexOf.put(v, indexOf.size);
            m++;
        }

        String[] labels = indexOf.sortedKeys();
        int n = labels.length;
        StrIntMap lexIndex = new StrIntMap(n * 2 + 1);
        for (int i = 0; i < n; i++) {
            lexIndex.put(labels[i], i);
        }

        int[] outDeg = new int[n];
        int[] inDeg = new int[n];
        EdgeSet seen = new EdgeSet(Math.max(8, m * 2));

        for (int i = 0; i < m; i++) {
            int u = lexIndex.get(lefts[i], -1);
            int v = lexIndex.get(rights[i], -1);
            if (u < 0 || v < 0) continue;
            if (seen.add(u, v)) {
                outDeg[u]++;
                inDeg[v]++;
            }
        }

        int[][] adj = new int[n][];
        int[] cur = new int[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new int[outDeg[i]];
            cur[i] = 0;
        }

        seen = new EdgeSet(Math.max(8, m * 2));
        for (int i = 0; i < m; i++) {
            int u = lexIndex.get(lefts[i], -1);
            int v = lexIndex.get(rights[i], -1);
            if (u < 0 || v < 0) continue;
            if (seen.add(u, v)) {
                adj[u][cur[u]++] = v;
            }
        }

        boolean[] used = new boolean[n];
        StringBuilder out = new StringBuilder();
        int produced = 0;

        for (int it = 0; it < n; it++) {
            int pick = -1;
            for (int i = 0; i < n; i++) {
                if (!used[i] && inDeg[i] == 0) { pick = i; break; }
            }
            if (pick == -1) break;

            if (produced > 0) out.append(' ');
            out.append(labels[pick]);
            produced++;
            used[pick] = true;

            for (int k = 0; k < adj[pick].length; k++) {
                int v = adj[pick][k];
                inDeg[v]--;
            }
        }

        System.out.println(out.toString());
    }
}
