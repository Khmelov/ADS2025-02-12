package by.bsuir.dsa.csv2025.gr451003.Климинтионак;


import org.junit.Test;

import java.nio.charset.StandardCharsets;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;


import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    static final String EMPTY = "EMPTY";
    static final String DELETED = "DELETED";

    static int a, b, c, d;
    static int m;
    static String[] table;
    static int used;

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
        ProcessBuilder pb = new ProcessBuilder(
                "java",
                "-cp",
                "out/production/Hash",   // Путь к Solution.class
                "Solution"
        );

        pb.redirectErrorStream(true);
        Process p = pb.start();

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(p.getOutputStream(), StandardCharsets.UTF_8))) {
            writer.write(input);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = p.getInputStream()) {
            is.transferTo(baos);
        }

        p.waitFor();
        return baos.toString().replace("\r", "").trim();
    }


    private void assertRun(String input, String expected) throws Exception {
        String out = runProgram(input).trim();
        assertEquals(expected.trim(), out);
    }

    @Test
    public void test1() throws Exception {
        assertRun(
                "1 0 1 0 5\n" +
                        "7\n" +
                        "ADD 10\n" +
                        "ADD 20\n" +
                        "ADD 30\n" +
                        "FIND 20\n" +
                        "DEL 20\n" +
                        "FIND 20\n" +
                        "PRINT\n",

                "1\n" +
                        "-1\n" +
                        "0 10\n" +
                        "1 DELETED\n" +
                        "2 EMPTY\n" +
                        "3 30\n" +
                        "4 EMPTY"
        );
    }


    @Test
    public void test2() throws Exception {
        assertRun(
                "1 0 1 0 5\n" +
                        "5\n" +
                        "ADD 5\n" +
                        "ADD 15\n" +
                        "ADD 25\n" +
                        "PRINT\n" +
                        "FIND 15\n",

                "0 5\n" +
                        "1 EMPTY\n" +
                        "2 25\n" +
                        "3 EMPTY\n" +
                        "4 15\n" +
                        "4"
        );
    }


    @Test
    public void test3() throws Exception {
        assertRun(
                "2 1 1 0 7\n" +
                        "6\n" +
                        "ADD 14\n" +
                        "ADD 28\n" +
                        "DEL 14\n" +
                        "ADD 35\n" +
                        "PRINT\n" +
                        "FIND 14\n",

                "0 EMPTY\n" +
                        "1 35\n" +
                        "2 EMPTY\n" +
                        "3 EMPTY\n" +
                        "4 EMPTY\n" +
                        "5 EMPTY\n" +
                        "6 28\n" +
                        "-1"
        );
    }


    @Test
    public void test4() throws Exception {
        assertRun(
                "1 0 1 0 11\n" +
                        "6\n" +
                        "ADD 27\n" +
                        "ADD 16\n" +
                        "DEL 16\n" +
                        "DEL 100\n" +
                        "PRINT\n" +
                        "FIND 27\n",

                "0 EMPTY\n" +
                        "1 DELETED\n" +
                        "2 EMPTY\n" +
                        "3 EMPTY\n" +
                        "4 EMPTY\n" +
                        "5 27\n" +
                        "6 EMPTY\n" +
                        "7 EMPTY\n" +
                        "8 EMPTY\n" +
                        "9 EMPTY\n" +
                        "10 EMPTY\n" +
                        "5"
        );
    }


    @Test
    public void test5() throws Exception {
        assertRun(
                "3 1 2 1 5\n" +
                        "6\n" +
                        "ADD 4\n" +
                        "ADD 9\n" +
                        "ADD 14\n" +
                        "ADD 19\n" +
                        "PRINT\n" +
                        "FIND 14\n",

                "0 14\n" +
                        "1 19\n" +
                        "2 9\n" +
                        "3 4\n" +
                        "4 EMPTY\n" +
                        "0"
        );
    }


    @Test
    public void test6() throws Exception {
        assertRun(
                "1 1 1 1 5\n" +
                        "8\n" +
                        "ADD 1\n" +
                        "ADD 6\n" +
                        "DEL 6\n" +
                        "ADD 11\n" +
                        "ADD 16\n" +
                        "PRINT\n" +
                        "FIND 6\n" +
                        "FIND 16\n",

                "0 EMPTY\n" +
                        "1 DELETED\n" +
                        "2 1\n" +
                        "3 11\n" +
                        "4 16\n" +
                        "-1\n" +
                        "4"
        );
    }


    @Test
    public void test7() throws Exception {
        assertRun(
                "3 0 1 0 9\n" +
                        "10\n" +
                        "ADD 12\n" +
                        "ADD 21\n" +
                        "ADD 30\n" +
                        "DEL 21\n" +
                        "ADD 39\n" +
                        "ADD 48\n" +
                        "ADD 57\n" +
                        "PRINT\n" +
                        "FIND 48\n" +
                        "FIND 21\n",

                "0 12\n" +
                        "1 48\n" +
                        "2 57\n" +
                        "3 EMPTY\n" +
                        "4 EMPTY\n" +
                        "5 EMPTY\n" +
                        "6 DELETED\n" +
                        "7 30\n" +
                        "8 39\n" +
                        "1\n" +
                        "-1"
        );
    }


    @Test
    public void test8() throws Exception {
        assertRun(
                "5 0 3 0 7\n" +
                        "6\n" +
                        "ADD 7\n" +
                        "ADD 14\n" +
                        "ADD 21\n" +
                        "PRINT\n" +
                        "FIND 14\n" +
                        "FIND 100\n",

                "0 7\n" +
                        "1 14\n" +
                        "2 EMPTY\n" +
                        "3 EMPTY\n" +
                        "4 21\n" +
                        "5 EMPTY\n" +
                        "6 EMPTY\n" +
                        "1\n" +
                        "-1"
        );
    }


    @Test
    public void test9() throws Exception {
        assertRun(
                "2 0 0 0 7\n" +
                        "7\n" +
                        "ADD 3\n" +
                        "ADD 10\n" +
                        "ADD 17\n" +
                        "ADD 24\n" +
                        "PRINT\n" +
                        "FIND 24\n" +
                        "FIND 1000\n",

                "0 10\n" +
                        "1 17\n" +
                        "2 24\n" +
                        "3 EMPTY\n" +
                        "4 EMPTY\n" +
                        "5 EMPTY\n" +
                        "6 3\n" +
                        "2\n" +
                        "-1"
        );
    }


    @Test
    public void test10() throws Exception {
        assertRun(
                "1 0 1 0 7\n" +
                        "10\n" +
                        "ADD 7\n" +
                        "ADD 14\n" +
                        "ADD 21\n" +
                        "DEL 14\n" +
                        "ADD 28\n" +
                        "ADD 35\n" +
                        "DEL 7\n" +
                        "ADD 42\n" +
                        "PRINT\n" +
                        "FIND 42\n",

                "0 EMPTY\n" +
                        "1 35\n" +
                        "2 EMPTY\n" +
                        "3 EMPTY\n" +
                        "4 21\n" +
                        "5 EMPTY\n" +
                        "6 EMPTY\n" +
                        "7 EMPTY\n" +
                        "8 42\n" +
                        "9 EMPTY\n" +
                        "10 EMPTY\n" +
                        "11 28\n" +
                        "12 EMPTY\n" +
                        "13 EMPTY\n" +
                        "14 EMPTY\n" +
                        "15 EMPTY\n" +
                        "16 EMPTY\n" +
                        "8"
        );
    }
}

