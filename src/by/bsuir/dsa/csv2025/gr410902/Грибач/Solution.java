package by.bsuir.dsa.csv2025.gr410902.Грибач;


import java.io.*;
import java.util.*;

public class Solution {

    static class Query {
        int l, r, t, idx;
        Query(int l, int r, int t, int idx) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.idx = idx;
        }
    }

    static class Update {
        int pos;
        int before, after;
        Update(int pos, int before, int after) {
            this.pos = pos;
            this.before = before;
            this.after = after;
        }
    }

    static int BLOCK;
    static int[] arr;
    static int[] freq;
    static int distinct = 0;

    static void add(int x) {
        if (freq[x] == 0) distinct++;
        freq[x]++;
    }

    static void remove(int x) {
        freq[x]--;
        if (freq[x] == 0) distinct--;
    }

    public static void main(String[] args) throws Exception {
        FastScanner fs = new FastScanner(System.in);

        int n = fs.nextInt();
        int q = fs.nextInt();

        if (n <= 0 || q <= 0) {
            return;
        }

        arr = new int[n + 1];
        List<Integer> all = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            arr[i] = fs.nextInt();
            all.add(arr[i]);
        }

        List<Query> queries = new ArrayList<>();
        List<Update> updates = new ArrayList<>();

        for (int i = 0; i < q; i++) {
            int type = fs.nextInt();
            if (type == 1) {
                int l = fs.nextInt();
                int r = fs.nextInt();
                queries.add(new Query(l, r, updates.size(), queries.size()));
            } else {
                int pos = fs.nextInt();
                int v = fs.nextInt();
                all.add(v);
                updates.add(new Update(pos, 0, v)); 
            }
        }

        // Сжимаем координаты
        Collections.sort(all);
        all = new ArrayList<>(new LinkedHashSet<>(all)); 

        for (int i = 1; i <= n; i++) {
            arr[i] = Collections.binarySearch(all, arr[i]);
        }

        for (Update u : updates) {
            u.after = Collections.binarySearch(all, u.after);
        }

        // Настраиваем Mo’s
        BLOCK = (int) Math.pow(n, 2.0 / 3.0);
        freq = new int[all.size() + 5];

        queries.sort((a, b) -> {
            int ab = a.l / BLOCK;
            int bb = b.l / BLOCK;
            if (ab != bb) return ab - bb;
            int ar = a.r / BLOCK;
            int br = b.r / BLOCK;
            if (ar != br) return ar - br;
            return a.t - b.t;
        });

        int[] ans = new int[queries.size()];

        int L = 1, R = 0, T = 0;
        int[] current = arr.clone();

        for (Query qr : queries) {
            while (T < qr.t) {
                Update u = updates.get(T);
                if (L <= u.pos && u.pos <= R) {
                    remove(current[u.pos]);
                    add(u.after);
                }
                current[u.pos] = u.after;
                T++;
            }
            while (T > qr.t) {
                T--;
                Update u = updates.get(T);
                if (L <= u.pos && u.pos <= R) {
                    remove(current[u.pos]);
                    add(u.before);
                }
                current[u.pos] = u.before;
            }
            while (R < qr.r) add(current[++R]);
            while (L > qr.l) add(current[--L]);
            while (R > qr.r) remove(current[R--]);
            while (L < qr.l) remove(current[L++]);

            ans[qr.idx] = distinct;
        }

        StringBuilder sb = new StringBuilder();
        for (int x : ans) sb.append(x).append("\n");
        System.out.print(sb);
    }

    // Быстрый сканер
    static class FastScanner {
        private final InputStream in;
        private final byte[] buffer = new byte[1 << 16];
        private int ptr = 0, len = 0;
        FastScanner(InputStream is) { in = is; }

        private int read() throws IOException {
            if (ptr >= len) {
                len = in.read(buffer);
                ptr = 0;
                if (len <= 0) return -1;
            }
            return buffer[ptr++];
        }

        int nextInt() throws IOException {
            int c;
            while ((c = read()) <= ' ') {
                if (c == -1) return -1;
            }
            int sign = 1;
            if (c == '-') { sign = -1; c = read(); }
            int val = c - '0';
            while ((c = read()) > ' ') val = val * 10 + (c - '0');
            return val * sign;
        }
    }
}
