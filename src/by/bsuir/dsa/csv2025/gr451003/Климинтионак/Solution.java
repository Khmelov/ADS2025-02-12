package by.bsuir.dsa.csv2025.gr451003.Климинтионак;


import org.junit.Test;

import java.util.Arrays;

import java.io.*;


import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    static final String EMPTY = "EMPTY";
    static final String DELETED = "DELETED";


    static int a, b, c, d;
    static int m;
    static String[] table;
    static int used;

    static void reset() {
        table = null;
        used = 0;
        m = 0;
        a = b = c = d = 0;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        a = sc.nextInt();
        b = sc.nextInt();
        c = sc.nextInt();
        d = sc.nextInt();
        m = sc.nextInt();
        sc.nextLine();

        table = new String[m];
        Arrays.fill(table, EMPTY);

        int q = Integer.parseInt(sc.nextLine());

        while (q-- > 0) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");
            String cmd = parts[0];

            if (cmd.equals("ADD")) {
                add(parts[1]);
            } else if (cmd.equals("DEL")) {
                del(parts[1]);
            } else if (cmd.equals("FIND")) {
                System.out.println(find(parts[1]));
            } else if (cmd.equals("PRINT")) {
                printTable();
            }
        }
    }

    static int h1(int x) {
        int v = (a * x + b) % m;
        return v < 0 ? v + m : v;
    }

    static int h2(int x) {
        int v = 1 + ((c * x + d) % (m - 1));
        if (v < 1) v += (m - 1);
        return v;
    }

    static int find(String key) {
        int x = Integer.parseInt(key);

        int h1 = h1(x);
        int h2 = h2(x);

        for (int k = 0; k < m; k++) {
            int idx = (h1 + k * h2) % m;

            if (table[idx].equals(EMPTY)) return -1;
            if (table[idx].equals(key)) return idx;
        }
        return -1;
    }

    static void add(String key) {
        if (find(key) != -1) return;

        if (((double) used / m) >= 0.7) {
            rehash();
        }

        int x = Integer.parseInt(key);
        int h1 = h1(x);
        int h2 = h2(x);

        for (int k = 0; k < m; k++) {
            int idx = (h1 + k * h2) % m;

            if (table[idx].equals(EMPTY) || table[idx].equals(DELETED)) {
                table[idx] = key;
                used++;
                return;
            }
        }
    }

    static void del(String key) {
        int idx = find(key);
        if (idx != -1) {
            table[idx] = DELETED;
        }
    }

    static void rehash() {
        int oldM = m;
        String[] oldTable = table;

        m = nextPrime(m * 2);
        table = new String[m];
        Arrays.fill(table, EMPTY);
        used = 0;

        for (String val : oldTable) {
            if (!val.equals(EMPTY) && !val.equals(DELETED)) {
                add(val);
            }
        }
    }

    static int nextPrime(int x) {
        while (true) {
            if (isPrime(x)) return x;
            x++;
        }
    }

    static boolean isPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; i * i <= x; i++)
            if (x % i == 0) return false;
        return true;
    }

    static void printTable() {
        for (int i = 0; i < m; i++) {
            System.out.println(i + " " + table[i]);
        }
    }

    private String runProgram(String input) throws Exception {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        System.setIn(in);
        System.setOut(new PrintStream(out));

        Solution.main(new String[]{});

        System.setIn(oldIn);
        System.setOut(oldOut);

        return out.toString().replace("\r", "").trim();
    }

    private void assertRun(String input, String expected) throws Exception {
        String actual = runProgram(input);
        assertEquals(expected.trim(), actual.trim());
    }

    @Test
    public void task0() throws Exception {
        reset();
        assertRun(
                """
                        1 0 1 0 5
                        7
                        ADD 10
                        ADD 20
                        ADD 30
                        FIND 20
                        DEL 20
                        FIND 20
                        PRINT
                        """,

                """
                        1
                        -1
                        0 10
                        1 DELETED
                        2 EMPTY
                        3 30
                        4 EMPTY"""
        );
    }


    @Test
    public void test1() throws Exception {
        reset();
        assertRun(
                """
                        1 0 1 0 5
                        5
                        ADD 5
                        ADD 15
                        ADD 25
                        PRINT
                        FIND 15
                        """,

                """
                        0 5
                        1 EMPTY
                        2 25
                        3 EMPTY
                        4 15
                        4"""
        );
    }


    @Test
    public void test2() throws Exception {
        reset();
        assertRun(
                """
                        2 1 1 0 7
                        6
                        ADD 14
                        ADD 28
                        DEL 14
                        ADD 35
                        PRINT
                        FIND 14
                        """,

                """
                        0 EMPTY
                        1 35
                        2 EMPTY
                        3 EMPTY
                        4 EMPTY
                        5 EMPTY
                        6 28
                        -1"""
        );
    }


    @Test
    public void test3() throws Exception {
        reset();
        assertRun(
                """
                        1 0 1 0 11
                        6
                        ADD 27
                        ADD 16
                        DEL 16
                        DEL 100
                        PRINT
                        FIND 27
                        """,

                """
                        0 EMPTY
                        1 DELETED
                        2 EMPTY
                        3 EMPTY
                        4 EMPTY
                        5 27
                        6 EMPTY
                        7 EMPTY
                        8 EMPTY
                        9 EMPTY
                        10 EMPTY
                        5"""
        );
    }


    @Test
    public void test4() throws Exception {
        reset();
        assertRun(
                """
                        3 1 2 1 5
                        6
                        ADD 4
                        ADD 9
                        ADD 14
                        ADD 19
                        PRINT
                        FIND 14
                        """,

                """
                        0 14
                        1 19
                        2 9
                        3 4
                        4 EMPTY
                        0"""
        );
    }


    @Test
    public void test5() throws Exception {
        reset();
        assertRun(
                """
                        1 1 1 1 5
                        8
                        ADD 1
                        ADD 6
                        DEL 6
                        ADD 11
                        ADD 16
                        PRINT
                        FIND 6
                        FIND 16
                        """,

                """
                        0 EMPTY
                        1 DELETED
                        2 1
                        3 11
                        4 16
                        -1
                        4"""
        );
    }


    @Test
    public void test6() throws Exception {
        reset();
        assertRun(
                """
                        3 0 1 0 9
                        10
                        ADD 12
                        ADD 21
                        ADD 30
                        DEL 21
                        ADD 39
                        ADD 48
                        ADD 57
                        PRINT
                        FIND 48
                        FIND 21
                        """,

                """
                        0 12
                        1 48
                        2 57
                        3 EMPTY
                        4 EMPTY
                        5 EMPTY
                        6 DELETED
                        7 30
                        8 39
                        1
                        -1"""
        );
    }


    @Test
    public void test7() throws Exception {
        reset();
        assertRun(
                """
                        5 0 3 0 7
                        6
                        ADD 7
                        ADD 14
                        ADD 21
                        PRINT
                        FIND 14
                        FIND 100
                        """,

                """
                        0 7
                        1 14
                        2 EMPTY
                        3 EMPTY
                        4 21
                        5 EMPTY
                        6 EMPTY
                        1
                        -1"""
        );
    }


    @Test
    public void test8() throws Exception {
        reset();
        assertRun(
                """
                        2 0 0 0 7
                        7
                        ADD 3
                        ADD 10
                        ADD 17
                        ADD 24
                        PRINT
                        FIND 24
                        FIND 1000
                        """,

                """
                        0 10
                        1 17
                        2 24
                        3 EMPTY
                        4 EMPTY
                        5 EMPTY
                        6 3
                        2
                        -1"""
        );
    }


    @Test
    public void test9() throws Exception {
        reset();
        assertRun(
                """
                        1 0 1 0 7
                        10
                        ADD 7
                        ADD 14
                        ADD 21
                        DEL 14
                        ADD 28
                        ADD 35
                        DEL 7
                        ADD 42
                        PRINT
                        FIND 42
                        """,

                """
                        0 EMPTY
                        1 35
                        2 EMPTY
                        3 EMPTY
                        4 21
                        5 EMPTY
                        6 EMPTY
                        7 EMPTY
                        8 42
                        9 EMPTY
                        10 EMPTY
                        11 28
                        12 EMPTY
                        13 EMPTY
                        14 EMPTY
                        15 EMPTY
                        16 EMPTY
                        8"""
        );
    }
}

