package by.bsuir.dsa.csv2025.gr451001.Колосун;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution {
    static class Client {
        long b;
        long c;
        int id;

        public Client(long b, long c, int id) {
            this.b = b;
            this.c = c;
            this.id = id;
        }
    }

    public static void main(String[] args) {
        FastScanner fs = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int t = fs.nextInt();
        while (t-- > 0) {
            solve(fs, out);
        }

        out.close();
    }

    private static void solve(FastScanner fs, PrintWriter out) {
        int n = fs.nextInt();
        int m = fs.nextInt();

        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = fs.nextLong();
        }

        long[] b = new long[m];
        for (int i = 0; i < m; i++) {
            b[i] = fs.nextLong();
        }

        long[] c = new long[m];
        for (int i = 0; i < m; i++) {
            c[i] = fs.nextLong();
        }

        ArrayList<Client> stage1Client = new ArrayList<>();
        ArrayList<Client> stage2Client = new ArrayList<>();

        for (int i = 0; i < m; i++) {
            if (c[i] > 0) {
                stage1Client.add(new Client(b[i], c[i], i));
            } else {
                stage2Client.add(new Client(b[i], c[i], i));
            }
        }

        Collections.sort(stage1Client, Comparator.comparingLong(mon -> mon.b));

        PriorityQueue<Long> pq = new PriorityQueue<>();
        for (long val : a) {
            pq.add(val);
        }

        ArrayList<Long> swordsForStage2 = new ArrayList<>();
        int count = 0;

        for (Client mon : stage1Client) {
            while (!pq.isEmpty() && pq.peek() < mon.b) {
                swordsForStage2.add(pq.poll());
            }

            if (pq.isEmpty()) {
                break;
            }

            long sword = pq.poll();
            count++;

            long newSwordVal = Math.max(sword, mon.c);
            pq.add(newSwordVal);
        }

        while (!pq.isEmpty()) {
            swordsForStage2.add(pq.poll());
        }

        Collections.sort(swordsForStage2);
        Collections.sort(stage2Client, Comparator.comparingLong(mon -> mon.b));

        int swordIdx = 0;
        int monIdx = 0;
        int nSwords = swordsForStage2.size();
        int nClients = stage2Client.size();

        while (swordIdx < nSwords && monIdx < nClients) {
            long swordVal = swordsForStage2.get(swordIdx);
            long monsterReq = stage2Client.get(monIdx).b;

            if (swordVal >= monsterReq) {
                count++;
                monIdx++;
                swordIdx++;
            } else {
                swordIdx++;
            }
        }

        out.println(count);
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        public FastScanner() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
    }



    public void runTest(String input, String expectedOutput) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos));

            Solution.main(new String[0]);

            String actual = baos.toString().replace("\r\n", "\n");
            String expected = expectedOutput.replace("\r\n", "\n");

            assertEquals(expected, actual);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void testGroup1_BasicUpgrade() throws Exception {
        runTest("4\n3 2\n5 10 20\n6 12\n15 25\n3 2\n10 20 30\n15 25\n15 25\n3 1\n1 5 10\n4\n100\n4 2\n1 1 10 10\n2 8\n3 9\n",
                "2\n2\n1\n2\n");
    }

    @Test
    public void testGroup2_CriticalDiscard() throws Exception {
        runTest("2\n"+
                "2 3\n" +
                "10 5\n" +
                "8 4 12\n" +
                "15 0 1\n" +
                "3 4\n" +
                "2 10 20\n" +
                "15 5 25 4\n" +
                "5 0 1 0\n","3\n" +
                "3\n");
    }

    @Test
    public void testGroup3_ExactMatch() throws Exception {
        runTest("3\n 4 3\n" +
                        "1 5 10 20\n" +
                        "15 2 7\n" +
                        "0 0 0\n" +
                        "3 3\n" +
                        "5 10 20\n" +
                        "8 30 4\n" +
                        "50 0 15\n" +
                        "4 3\n" +
                        "5 10 15 25\n" +
                        "40 5 10\n" +
                        "10 0 0",
                "3\n" +
                        "3\n" +
                        "2\n");
    }

    @Test
    public void testGroup4_InstantPowerhouse() throws Exception {
        runTest("4\n4 3\n1 10 50 100\n1 5 90\n1000 0 0\n4 3\n10 1 50 100\n5 1 90\n0 1000 0\n5 3\n1 10 20 30 40\n5 35 1\n100 100 0\n4 3\n1 2 3 4\n1 2 5\n10 10 0\n",
                "3\n3\n3\n3\n");
    }

    @Test
    public void testGroup5_Stage1Failure() throws Exception {
        runTest("2\n2 3\n" +
                        "10 20\n" +
                        "10 18 1\n" +
                        "5 0 0\n" +
                        "4 5\n" +
                        "10 50 100 5\n" +
                        "1 5 20 90 10\n" +
                        "1 0 0 0 1\n","3\n" +
                "5\n");
    }

    @Test
    public void testGroup6_Stage2SimpleConsumption() throws Exception {
        runTest("2\n3 3\n" +
                        "10 20 30\n" +
                        "15 5 25\n" +
                        "12 0 1\n" +
                        "5 5\n" +
                        "1 2 3 10 100\n" +
                        "50 1 2 3 4\n" +
                        "50 0 0 0 0\n", "3\n" +
                        "5\n");
    }

    @Test
    public void testGroup7_OptimalS2Match() throws Exception {
        runTest("4\n5 4\n1 3 5 7 9\n2 4 6 8\n0 0 0 0\n6 6\n1 2 3 4 5 6\n1 2 3 4 5 6\n0 0 0 0 0 0\n5 5\n10 20 30 40 50\n15 25 35 45 55\n0 0 0 0 0\n5 3\n1 5 10 20 30\n15 2 5\n0 0 0\n",
                "4\n6\n4\n3\n");
    }

    @Test
    public void testGroup8_S2Inefficiency() throws Exception {
        runTest("3\n4 4\n" +
                        "5 10 15 20\n" +
                        "6 11 16 21\n" +
                        "7 12 17 1\n" +
                        "2 3\n" +
                        "100 1000\n" +
                        "500 999999999 150\n" +
                        "2000000000 0 0\n" +
                        "4 3\n" +
                        "10 20 30 40\n" +
                        "15 25 35\n" +
                        "25 35 1\n", "3\n" +
                "2\n" +
                "3\n");
    }

    @Test
    public void testGroup9_FullCycle() throws Exception {
        runTest("2\n4 4\n" +
                        "1 10 50 100\n" +
                        "1 5 20 90\n" +
                        "1000 0 0 0\n" +
                        "2 2\n" +
                        "1 10\n" +
                        "1 5\n" +
                        "100 0\n",
                "4\n" +
                        "2\n");
    }

    @Test
    public void testGroup10_LargeNumbers() throws Exception {
        runTest("3\n3 2\n" +
                        "5 10 15\n" +
                        "12 18\n" +
                        "1 0\n" +
                        "5 5\n" +
                        "2 12 20 35 50\n" +
                        "10 30 15 5 1\n" +
                        "10 0 40 0 50\n" +
                        "10 10\n" +
                        "1 2 3 4 5 6 7 8 9 10\n" +
                        "2 4 6 8 10 1 3 5 7 9\n" +
                        "2 4 6 8 10 0 0 0 0 0\n", "1\n" +
                "5\n" +
                "10\n");
    }
}