package by.bsuir.dsa.csv2025.gr451001.Демидович;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {
    static class Block {
        char c;
        int count;

        public Block(char c, int count) {
            this.c = c;
            this.count = count;
        }
    }

    public static void main(String[] args) {
        FastScanner fs = new FastScanner();
        PrintWriter out = new PrintWriter(System.out);

        int t = fs.nextInt();
        while (t-- > 0) {
            String p = fs.next();
            String s = fs.next();
            solve(p, s, out);
        }

        out.close();
    }

    private static void solve(String p, String s, PrintWriter out) {
        List<Block> pBlocks = getRLE(p);
        List<Block> sBlocks = getRLE(s);

        if (pBlocks.size() != sBlocks.size()) {
            out.println("NO");
            return;
        }

        for (int i = 0; i < pBlocks.size(); i++) {
            Block bp = pBlocks.get(i);
            Block bs = sBlocks.get(i);

            if (bp.c != bs.c) {
                out.println("NO");
                return;
            }

            if (bs.count < bp.count || bs.count > 2 * bp.count) {
                out.println("NO");
                return;
            }
        }

        out.println("YES");
    }

    private static List<Block> getRLE(String str) {
        List<Block> blocks = new ArrayList<>();
        if (str.isEmpty()) return blocks;

        char currentChar = str.charAt(0);
        int currentCount = 1;

        for (int i = 1; i < str.length(); i++) {
            if (str.charAt(i) == currentChar) {
                currentCount++;
            } else {
                blocks.add(new Block(currentChar, currentCount));
                currentChar = str.charAt(i);
                currentCount = 1;
            }
        }
        blocks.add(new Block(currentChar, currentCount));
        return blocks;
    }

    static class FastScanner {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer("");

        String next() {
            while (!st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    private void runTest(String input, String expectedOutput) throws Exception {
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
    public void testGroup1_BaseCases() throws Exception {
        runTest("3\nL L\nR RR\nL LL\n",
                "YES\nYES\nYES\n");
    }

    @Test
    public void testGroup2_TooShort() throws Exception {
        runTest("2\nRRR R\nLL L\n",
                "NO\nNO\n");
    }

    @Test
    public void testGroup3_TooLong() throws Exception {
        runTest("3\nR RRR\nLL LLLLLLL\nRRRR RRRRRRRRR\n",
                "NO\nNO\nNO\n");
    }

    @Test
    public void testGroup4_WrongCharOrOrder() throws Exception {
        runTest("3\nR L\nLR RL\nL RR\n",
                "NO\nNO\nNO\n");
    }

    @Test
    public void testGroup5_ValidIntermediate() throws Exception {
        runTest("2\nLRR LLRRR\nRLL RRLLL\n",
                "YES\nYES\n");
    }

    @Test
    public void testGroup6_MaxExpansion() throws Exception {
        runTest("2\nLRLR LLRRLLRR\nLLRR LLLLRRRR\n",
                "YES\nYES\n");
    }

    @Test
    public void testGroup7_StructureFewerBlocks() throws Exception {
        runTest("2\nLRL LR\nLRLR LLRR\n",
                "NO\nNO\n");
    }

    @Test
    public void testGroup8_StructureMoreBlocks() throws Exception {
        runTest("2\nLR LRRL\nRL RLLR\n",
                "NO\nNO\n");
    }

    @Test
    public void testGroup9_LongBlocksBoundary() throws Exception {
        runTest("2\nRRR RRRRRR\nLLLL LLLLLLLL\n",
                "YES\nYES\n");
    }

    @Test
    public void testGroup10_MixedExpansionTypes() throws Exception {
        runTest("2\nLLLLR LLLLLLLLRR\nRRLLLR RRLLLRR\n",
                "YES\nYES\n");
    }
}

