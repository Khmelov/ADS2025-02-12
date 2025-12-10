package by.it.group451001.volynets.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphC {
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
            size = 0;
            for (int i = 0; i < ok.length; i++) if (ou[i]) put(ok[i], ov[i]);
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

        String[] keysArray() {
            String[] res = new String[size];
            int p = 0;
            for (int i = 0; i < keys.length; i++) if (used[i]) res[p++] = keys[i];
            return res;
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

    private static void sortStrings(String[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[j].compareTo(a[i]) < 0) { String t = a[i]; a[i] = a[j]; a[j] = t; }
    }

    private static void sortInts(int[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[i]) { int t = a[i]; a[i] = a[j]; a[j] = t; }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();
        if (line == null || line.trim().isEmpty()) return;

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
        if (n == 0) return;

        String[] labels = new String[n];
        {
            for (int i = 0, got = 0; i < map.keys.length; i++) {
                if (map.used[i]) {
                    labels[map.vals[i]] = map.keys[i];
                    got++;
                    if (got == n) break;
                }
            }
        }

        int[] outDeg = new int[n];
        int[] inDeg = new int[n];
        EdgeSet seen = new EdgeSet(Math.max(8, m * 2));

        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) outDeg[u]++;
        }

        int[][] g = new int[n][];
        int[][] gr = new int[n][];
        int[] cur = new int[n];
        for (int i = 0; i < n; i++) g[i] = new int[outDeg[i]];

        seen = new EdgeSet(Math.max(8, m * 2));
        int[] inDegTmp = new int[n];
        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) {
                g[u][cur[u]++] = v;
                inDegTmp[v]++;
            }
        }
        gr = new int[n][];
        for (int i = 0; i < n; i++) gr[i] = new int[inDegTmp[i]];
        int[] curR = new int[n];
        for (int u = 0; u < n; u++) {
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                gr[v][curR[v]++] = u;
            }
        }

        boolean[] used = new boolean[n];
        int[] order = new int[n];
        int ordSz = 0;

        for (int s = 0; s < n; s++) {
            if (used[s]) continue;
            int[] stV = new int[n * 2 + 5];
            int[] stI = new int[n * 2 + 5];
            int top = 0;
            stV[top] = s; stI[top] = 0; top++;
            used[s] = true;
            while (top > 0) {
                int v = stV[top - 1];
                int i = stI[top - 1];
                if (i == g[v].length) {
                    order[ordSz++] = v;
                    top--;
                    continue;
                }
                int to = g[v][i];
                stI[top - 1] = i + 1;
                if (!used[to]) {
                    used[to] = true;
                    stV[top] = to; stI[top] = 0; top++;
                }
            }
        }

        int[] compId = new int[n];
        for (int i = 0; i < n; i++) compId[i] = -1;
        int comps = 0;

        for (int it = n - 1; it >= 0; it--) {
            int v0 = order[it];
            if (compId[v0] != -1) continue;

            int[] st = new int[n];
            int sp = 0;
            st[sp++] = v0;
            compId[v0] = comps;

            int[] compBuf = new int[n];
            int cb = 0;
            compBuf[cb++] = v0;

            int[] iter = new int[n];
            while (sp > 0) {
                int v = st[sp - 1];
                int i = iter[sp - 1];
                if (i == gr[v].length) {
                    sp--;
                    continue;
                }
                int u = gr[v][i];
                iter[sp - 1] = i + 1;
                if (compId[u] == -1) {
                    compId[u] = comps;
                    st[sp++] = u;
                    compBuf[cb++] = u;
                }
            }

            if (compList == null) {
                compList = new int[4][];
                compSizeCap = 4;
            }
            if (comps >= compSizeCap) {
                int newCap = compSizeCap << 1;
                int[][] nlist = new int[newCap][];
                for (int i2 = 0; i2 < compSizeCap; i2++) nlist[i2] = compList[i2];
                compList = nlist;
                compSizeCap = newCap;
            }
            int[] arr = new int[cb];
            for (int t = 0; t < cb; t++) arr[t] = compBuf[t];
            compList[comps] = arr;

            comps++;
        }

        int[][] compListLocal = compListTrim(compList, comps);
        int[] cOutDeg = new int[comps];
        EdgeSet seenCE = new EdgeSet(Math.max(8, m * 2));
        for (int u = 0; u < n; u++) {
            int cu = compId[u];
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                int cv = compId[v];
                if (cu != cv && seenCE.add(cu, cv)) {
                    cOutDeg[cu]++;
                    inDeg[cv]++;
                }
            }
        }
        int[][] cg = new int[comps][];
        int[] ccur = new int[comps];
        for (int i = 0; i < comps; i++) cg[i] = new int[cOutDeg[i]];
        seenCE = new EdgeSet(Math.max(8, m * 2));
        for (int u = 0; u < n; u++) {
            int cu = compId[u];
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                int cv = compId[v];
                if (cu != cv && seenCE.add(cu, cv)) {
                    cg[cu][ccur[cu]++] = cv;
                }
            }
        }

        int[] inDegC = new int[comps];
        for (int i = 0; i < comps; i++) inDegC[i] = inDeg[i];
        int[] topoC = new int[comps];
        int produced = 0;
        boolean[] usedC = new boolean[comps];

        for (int it = 0; it < comps; it++) {
            int pick = -1;
            for (int i = 0; i < comps; i++) {
                if (!usedC[i] && inDegC[i] == 0) { pick = i; break; }
            }
            if (pick == -1) break;
            topoC[produced++] = pick;
            usedC[pick] = true;
            for (int k = 0; k < cg[pick].length; k++) {
                inDegC[cg[pick][k]]--;
            }
        }

        StringBuilder out = new StringBuilder();
        for (int ti = 0; ti < produced; ti++) {
            int cid = topoC[ti];
            int[] verts = compListLocal[cid];
            String[] compLabels = new String[verts.length];
            for (int i = 0; i < verts.length; i++) compLabels[i] = labels[verts[i]];
            sortStrings(compLabels);
            for (String s : compLabels) out.append(s);
            if (ti + 1 < produced) out.append('\n');
        }

        System.out.print(out.toString());
    }

    private static int[][] compList;
    private static int compSizeCap;

    private static int[][] compListTrim(int[][] a, int n) {
        int[][] res = new int[n][];
        for (int i = 0; i < n; i++) res[i] = a[i];
        return res;
    }
}
