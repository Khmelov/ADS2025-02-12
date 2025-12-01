package by.bsuir.dsa.csv2025.gr410901.Скачкова;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int m = sc.nextInt();

        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            int a = sc.nextInt(), b = sc.nextInt();
            segments[i] = new Segment(a, b);
        }

        int[] points = new int[m];
        for (int i = 0; i < m; i++) {
            points[i] = sc.nextInt();
        }

        hybridQuickSort(segments, 0, n - 1);

        for (int p : points) {
            int cnt = 0;
            for (Segment s : segments) {
                if (s.l <= p && p <= s.r) cnt++;
            }
            System.out.print(cnt + " ");
        }
    }

    private static void hybridQuickSort(Segment[] a, int l, int r) {
        while (l < r) {
            if (r - l + 1 < 17) {
                insertionSort(a, l, r);
                break;
            }
            int[] p = partition3(a, l, r);
            if (p[0] - l < r - p[1]) {
                hybridQuickSort(a, l, p[0] - 1);
                l = p[1] + 1;
            } else {
                hybridQuickSort(a, p[1] + 1, r);
                r = p[0] - 1;
            }
        }
    }

    private static int[] partition3(Segment[] a, int l, int r) {
        int m = l + (r - l) / 2;
        if (a[m].compareTo(a[l]) < 0) swap(a, l, m);
        if (a[r].compareTo(a[l]) < 0) swap(a, l, r);
        if (a[m].compareTo(a[r]) < 0) swap(a, m, r);
        swap(a, m, r - 1);
        Segment pivot = a[r - 1];

        int lt = l, gt = r - 1, i = l;
        while (i <= gt) {
            int cmp = a[i].compareTo(pivot);
            if (cmp < 0) swap(a, lt++, i++);
            else if (cmp > 0) swap(a, i, gt--);
            else i++;
        }
        swap(a, lt, r - 1);
        return new int[]{lt, gt + 1};
    }

    private static void insertionSort(Segment[] a, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            Segment key = a[i];
            int j = i - 1;
            while (j >= l && a[j].compareTo(key) > 0) a[j + 1] = a[j--];
            a[j + 1] = key;
        }
    }

    private static void swap(Segment[] a, int i, int j) {
        Segment t = a[i]; a[i] = a[j]; a[j] = t;
    }

    private static class Segment implements Comparable<Segment> {
        int l, r;
        Segment(int a, int b) { l = Math.min(a, b); r = Math.max(a, b); }
        public int compareTo(Segment o) {
            int cmp = Integer.compare(l, o.l);
            return cmp != 0 ? cmp : Integer.compare(r, o.r);
        }
    }


    private static String runWithInput(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        try {
            main(new String[0]);
            return baos.toString().trim();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }


    @Test public void test01() { assertEquals("1 0 0", runWithInput("2 3\n0 5\n7 10\n1 6 11")); }
    @Test public void test02() { assertEquals("1 0 0", runWithInput("1 3\n0 10\n5 15 20")); }
    @Test public void test03() { assertEquals("3 3",   runWithInput("3 2\n1 5\n2 6\n3 7\n3 5")); }
    @Test public void test04() { assertEquals("0 0",   runWithInput("2 2\n0 5\n10 15\n6 20")); }
    @Test public void test05() { assertEquals("1 2",   runWithInput("2 2\n0 10\n5 15\n3 7")); }
    @Test public void test06() { assertEquals("0 0",   runWithInput("0 2\n5 10")); }
    @Test public void test07() { assertEquals("1",     runWithInput("1 1\n5 15\n10")); }
    @Test public void test08() { assertEquals("1 2 2", runWithInput("3 3\n0 5\n5 10\n10 15\n0 5 10")); }
    @Test public void test09() { assertEquals("1 2 2 0", runWithInput("2 4\n0 10\n5 15\n1 5 10 20")); }
    @Test public void test10() { assertEquals("2 2",   runWithInput("3 2\n10 1\n0 5\n8 12\n4 9")); }
}