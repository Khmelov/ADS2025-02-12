package by.bsuir.dsa.csv2025.gr410902.Грибач;

import java.util.*;
import org.junit.*;
import java.io.*;
import static org.junit.Assert.assertEquals;

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

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (!sc.hasNextInt()) return;
        int n = sc.nextInt();
        int q = sc.nextInt();

        arr = new int[n + 1];
        List<Integer> all = new ArrayList<>();

        for (int i = 1; i <= n; i++) {
            arr[i] = sc.nextInt();
            all.add(arr[i]);
        }

        List<Query> queries = new ArrayList<>();
        List<Update> updates = new ArrayList<>();

        // временное хранение before-значений
        int[] current = arr.clone();

        for (int i = 0; i < q; i++) {
            int type = sc.nextInt();
            if (type == 1) {
                int l = sc.nextInt();
                int r = sc.nextInt();
                queries.add(new Query(l, r, updates.size(), queries.size()));
            } else {
                int pos = sc.nextInt();
                int val = sc.nextInt();
                all.add(val);
                updates.add(new Update(pos, current[pos], val));
                current[pos] = val;
            }
        }

        // coordinate compression
        Collections.sort(all);
        all = new ArrayList<>(new LinkedHashSet<>(all));

        for (int i = 1; i <= n; i++)
            arr[i] = Collections.binarySearch(all, arr[i]);

        for (Update u : updates)
            u.after = Collections.binarySearch(all, u.after);

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
        int[] cur = arr.clone();

        for (Query qr : queries) {
            while (T < qr.t) {
                Update u = updates.get(T);
                if (L <= u.pos && u.pos <= R) {
                    remove(cur[u.pos]);
                    add(u.after);
                }
                cur[u.pos] = u.after;
                T++;
            }
            while (T > qr.t) {
                T--;
                Update u = updates.get(T);
                if (L <= u.pos && u.pos <= R) {
                    remove(cur[u.pos]);
                    add(u.before);
                }
                cur[u.pos] = u.before;
            }
            while (R < qr.r) add(cur[++R]);
            while (L > qr.l) add(cur[--L]);
            while (R > qr.r) remove(cur[R--]);
            while (L < qr.l) remove(cur[L++]);

            ans[qr.idx] = distinct;
        }

        StringBuilder sb = new StringBuilder();
        for (int num : ans) sb.append(num).append("\n");
        System.out.print(sb);
    }

    // =============================
    //            TESTS
    // =============================

    private String run(String input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(new PrintStream(out));

        main(new String[]{});
        return out.toString().replace("\r\n", "\n").trim();
    }

    @Test
    public void test1() {
        String in = "3 3\n1 2 1\n1 1 3\n2 2 3\n1 1 3";
        String out = "2\n2";
        assertEquals(out, run(in));
    }

    @Test
    public void test2() {
        String in =
                "4 4\n" +
                        "1 1 1 1\n" +
                        "1 1 4\n" +
                        "2 3 2\n" +
                        "1 2 3\n" +
                        "2 1 2\n" +
                        "1 1 4";
        String out = "1\n2";
        assertEquals(out, run(in));
    }

    @Test
    public void test3() {
        String in =
                "5 5\n" +
                        "5 4 3 2 1\n" +
                        "1 1 5\n" +
                        "2 5 5\n" +
                        "1 1 5\n" +
                        "2 1 1\n" +
                        "1 1 3";
        String out = "4\n5\n3";
        assertEquals(out, run(in));
    }

    @Test
    public void test4() {
        String in =
                "6 4\n" +
                        "1 2 3 2 1 3\n" +
                        "1 1 6\n" +
                        "2 3 1\n" +
                        "1 1 6\n" +
                        "1 2 5";
        String out = "4\n3\n2";
        assertEquals(out, run(in));
    }

    @Test
    public void test5() {
        String in =
                "7 5\n" +
                        "1 2 3 4 3 2 1\n" +
                        "1 1 7\n" +
                        "2 4 1\n" +
                        "1 1 7\n" +
                        "2 7 5\n" +
                        "1 3 7";
        String out = "4\n3\n4";
        assertEquals(out, run(in));
    }

    @Test
    public void test6() {
        String in =
                "5 4\n" +
                        "1 1 1 2 2\n" +
                        "1 1 5\n" +
                        "2 5 3\n" +
                        "1 4 5\n" +
                        "1 2 3";
        String out = "2\n2\n1";
        assertEquals(out, run(in));
    }

    @Test
    public void test7() {
        String in =
                "4 4\n" +
                        "1 2 2 1\n" +
                        "1 1 4\n" +
                        "2 2 3\n" +
                        "2 3 1\n" +
                        "1 1 4";
        String out = "2\n2";
        assertEquals(out, run(in));
    }

    @Test
    public void test8() {
        String in =
                "6 5\n" +
                        "1 2 3 3 2 1\n" +
                        "1 1 6\n" +
                        "2 5 4\n" +
                        "1 3 6\n" +
                        "2 1 3\n" +
                        "1 1 3";
        String out = "3\n3\n2";
        assertEquals(out, run(in));
    }

    @Test
    public void test9() {
        String in =
                "5 7\n" +
                        "1 2 1 3 2\n" +
                        "1 1 5\n" +
                        "2 3 5\n" +
                        "1 1 5\n" +
                        "2 1 2\n" +
                        "1 1 3\n" +
                        "2 5 1\n" +
                        "1 1 5";
        String out = "2\n3\n2\n4";
        assertEquals(out, run(in));
    }

    @Test
    public void test10() {
        String in =
                "8 7\n" +
                        "5 5 5 5 5 5 5 5\n" +
                        "2 4 3\n" +
                        "2 1 2\n" +
                        "2 8 3\n" +
                        "1 1 8\n" +
                        "2 5 10\n" +
                        "1 1 8\n" +
                        "1 3 6";
        String out = "3\n4\n3";
        assertEquals(out, run(in));
    }
}
