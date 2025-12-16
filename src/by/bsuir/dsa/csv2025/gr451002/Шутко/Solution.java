package by.bsuir.dsa.csv2025.gr451002.Шутко;

import java.io.*;
import java.util.StringTokenizer;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;

public class Solution {
    private int[] tree;
    private int[] arr;
    private int n;

    public static void main(String[] args) throws IOException {
        new Solution().solve();
    }

    public void solve() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);

        // Read array
        n = Integer.parseInt(br.readLine());
        arr = new int[n];
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // Build segment tree
        buildTree();

        // Process queries
        int q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            st = new StringTokenizer(br.readLine());
            String type = st.nextToken();
            if (type.equals("MIN")) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                // Ensure indices are within bounds
                if (l < 0) l = 0;
                if (r >= n) r = n - 1;
                pw.println(query(1, 0, n - 1, l, r));
            } else if (type.equals("UPDATE")) {
                int idx = Integer.parseInt(st.nextToken());
                int val = Integer.parseInt(st.nextToken());
                // Ensure index is within bounds
                if (idx >= 0 && idx < n) {
                    update(1, 0, n - 1, idx, val);
                }
            }
        }

        pw.close();
        br.close();
    }

    private void buildTree() {
        if (n == 0) return;
        int size = 4 * n;
        tree = new int[size];
        // Initialize with MAX_VALUE to avoid issues with default 0 values
        for (int i = 0; i < size; i++) {
            tree[i] = Integer.MAX_VALUE;
        }
        build(1, 0, n - 1);
    }

    private void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
            tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
        }
    }

    private void update(int node, int start, int end, int idx, int val) {
        if (start == end) {
            arr[idx] = val;
            tree[node] = val;
        } else {
            int mid = (start + end) / 2;
            if (idx >= start && idx <= mid) {
                update(2 * node, start, mid, idx, val);
            } else {
                update(2 * node + 1, mid + 1, end, idx, val);
            }
            tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
        }
    }

    private int query(int node, int start, int end, int l, int r) {
        if (r < start || l > end) {
            return Integer.MAX_VALUE;
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        int leftMin = query(2 * node, start, mid, l, r);
        int rightMin = query(2 * node + 1, mid + 1, end, l, r);
        return Math.min(leftMin, rightMin);
    }

    public static class SolutionTest {

        @Test
        public void testBasicExample() throws IOException {
            String input = "5\n" +
                    "1 3 2 5 4\n" +
                    "6\n" +
                    "MIN 0 4\n" +
                    "MIN 1 3\n" +
                    "UPDATE 2 0\n" +
                    "MIN 0 4\n" +
                    "UPDATE 0 6\n" +
                    "MIN 0 2\n";

            String expectedOutput = "1\n2\n0\n0\n";
            String actualOutput = runSolution(input);
            assertEquals("Basic example test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testAllElementsSame() throws IOException {
            String input = "3\n" +
                    "5 5 5\n" +
                    "4\n" +
                    "MIN 0 2\n" +
                    "UPDATE 1 3\n" +
                    "MIN 0 2\n" +
                    "MIN 1 1\n";

            String expectedOutput = "5\n3\n3\n";
            String actualOutput = runSolution(input);
            assertEquals("All elements same test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testSingleElement() throws IOException {
            String input = "1\n" +
                    "10\n" +
                    "3\n" +
                    "MIN 0 0\n" +
                    "UPDATE 0 5\n" +
                    "MIN 0 0\n";

            String expectedOutput = "10\n5\n";
            String actualOutput = runSolution(input);
            assertEquals("Single element test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testNegativeNumbers() throws IOException {
            String input = "4\n" +
                    "-1 -5 -2 -8\n" +
                    "5\n" +
                    "MIN 0 3\n" +      // Должен вернуть -8 (минимум: -1, -5, -2, -8)
                    "UPDATE 1 10\n" +  // Меняем -5 на 10 → массив: [-1, 10, -2, -8]
                    "MIN 0 3\n" +      // Должен вернуть -8 (минимум: -1, 10, -2, -8)
                    "UPDATE 3 0\n" +   // Меняем -8 на 0 → массив: [-1, 10, -2, 0]
                    "MIN 2 3\n";       // Должен вернуть -2 (минимум: -2, 0)

            String expectedOutput = "-8\n-8\n-2\n";
            String actualOutput = runSolution(input);
            assertEquals("Negative numbers test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testLargeRange() throws IOException {
            String input = "6\n" +
                    "10 20 5 15 30 25\n" +
                    "4\n" +
                    "MIN 0 5\n" +
                    "MIN 2 4\n" +
                    "UPDATE 1 2\n" +
                    "MIN 0 3\n";

            String expectedOutput = "5\n5\n2\n";
            String actualOutput = runSolution(input);
            assertEquals("Large range test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testEdgeCaseMinimalArray() throws IOException {
            String input = "2\n" +
                    "1 2\n" +
                    "3\n" +
                    "MIN 0 1\n" +
                    "UPDATE 0 3\n" +
                    "MIN 0 1\n";

            String expectedOutput = "1\n2\n";
            String actualOutput = runSolution(input);
            assertEquals("Minimal array test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testMultipleUpdates() throws IOException {
            String input = "3\n" +
                    "1 2 3\n" +
                    "5\n" +
                    "MIN 0 2\n" +
                    "UPDATE 0 4\n" +
                    "UPDATE 1 0\n" +
                    "UPDATE 2 1\n" +
                    "MIN 0 2\n";

            String expectedOutput = "1\n0\n";
            String actualOutput = runSolution(input);
            assertEquals("Multiple updates test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testSingleElementQueries() throws IOException {
            String input = "3\n" +
                    "7 8 9\n" +
                    "4\n" +
                    "MIN 0 0\n" +
                    "MIN 1 1\n" +
                    "MIN 2 2\n" +
                    "MIN 0 2\n";

            String expectedOutput = "7\n8\n9\n7\n";
            String actualOutput = runSolution(input);
            assertEquals("Single element queries test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testUpdateAndImmediateQuery() throws IOException {
            String input = "2\n" +
                    "100 200\n" +
                    "3\n" +
                    "UPDATE 0 50\n" +
                    "MIN 0 1\n" +
                    "UPDATE 1 25\n";

            String expectedOutput = "50\n";
            String actualOutput = runSolution(input);
            assertEquals("Update and immediate query test failed", expectedOutput, actualOutput);
        }

        @Test
        public void testConsecutiveMinQueries() throws IOException {
            String input = "4\n" +
                    "3 1 4 2\n" +
                    "5\n" +
                    "MIN 0 3\n" +
                    "MIN 0 1\n" +
                    "MIN 1 2\n" +
                    "MIN 2 3\n" +
                    "MIN 0 0\n";

            String expectedOutput = "1\n1\n1\n2\n3\n";
            String actualOutput = runSolution(input);
            assertEquals("Consecutive MIN queries test failed", expectedOutput, actualOutput);
        }

        private String runSolution(String input) throws IOException {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PrintStream originalOut = System.out;
            InputStream originalIn = System.in;

            try {
                System.setIn(in);
                System.setOut(new PrintStream(out, true)); // autoflush true

                Solution solution = new Solution();
                solution.solve();

                return out.toString().replace("\r\n", "\n"); // normalize line endings
            } finally {
                System.setOut(originalOut);
                System.setIn(originalIn);
            }
        }
    }
}