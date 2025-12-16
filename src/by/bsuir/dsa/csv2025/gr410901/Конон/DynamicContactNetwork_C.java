package by.bsuir.dsa.csv2025.gr410901.Конон;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;

public class DynamicContactNetwork_C {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    private static class HistoryEntry {
        final int parentRoot;
        final int childRoot;
        final int oldParentSize;

        HistoryEntry(int parentRoot, int childRoot, int oldParentSize) {
            this.parentRoot = parentRoot;
            this.childRoot = childRoot;
            this.oldParentSize = oldParentSize;
        }
    }

    static class DSU {
        private final int[] parent;
        private final int[] size;
        private final Stack<HistoryEntry> history;

        DSU(int n) {
            parent = new int[n + 1];
            size = new int[n + 1];
            history = new Stack<>();
            for (int i = 1; i <= n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int i) {
            while (parent[i] != i) {
                i = parent[i];
            }
            return i;
        }

        public void union(int i, int j) {
            int rootI = find(i);
            int rootJ = find(j);

            if (rootI != rootJ) {
                if (size[rootI] < size[rootJ]) {
                    int temp = rootI;
                    rootI = rootJ;
                    rootJ = temp;
                }
                history.push(new HistoryEntry(rootI, rootJ, size[rootI]));
                parent[rootJ] = rootI;
                size[rootI] += size[rootJ];
            }
        }

        public void undo() {
            if (!history.isEmpty()) {
                HistoryEntry lastChange = history.pop();

                //восстанавление родителя и размер
                parent[lastChange.childRoot] = lastChange.childRoot; //возвразение корня
                size[lastChange.parentRoot] = lastChange.oldParentSize; //возвращение размера
            }
        }

        public int getSize(int i) {
            return size[find(i)];
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer st = new StringTokenizer(reader.readLine());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        DSU dsu = new DSU(n);

        for (int k = 0; k < m; k++) {
            st = new StringTokenizer(reader.readLine());
            String command = st.nextToken();

            switch (command) {
                case "contact":
                    dsu.union(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
                    break;
                case "size":
                    writer.println(dsu.getSize(Integer.parseInt(st.nextToken())));
                    break;
                case "undo":
                    dsu.undo();
                    break;
            }
        }
        writer.flush();
    }

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    private List<String> runMainAndCaptureOutput(String input) throws Exception {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        DynamicContactNetwork_C.main(new String[0]);

        String output = outContent.toString().trim();
        if (output.isEmpty()) return List.of();

        return List.of(output.split(System.lineSeparator()));
    }

    @Test
    public void testProvidedExample() throws Exception {
        String input = "10 12\n" +
                "contact 1 2\n" +
                "contact 3 4\n" +
                "size 1\n" +
                "contact 1 3\n" +
                "size 4\n" +
                "undo\n" +
                "size 4\n" +
                "contact 5 6\n" +
                "contact 1 5\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n";

        List<String> expectedOutput = List.of("2", "4", "2", "4", "2");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testUndoEdgeCases() throws Exception {
        String input = "5 7\n" +
                "undo\n" +
                "size 1\n" +
                "contact 1 2\n" +
                "undo\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n";

        List<String> expectedOutput = List.of("1", "1", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testChainedContacts() throws Exception {
        String input = "5 8\n" +
                "contact 1 2\n" +
                "contact 2 3\n" +
                "contact 3 4\n" +
                "size 1\n" +
                "size 4\n" +
                "undo\n" +
                "size 1\n" +
                "size 4\n";

        List<String> expectedOutput = List.of("4", "4", "3", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testStarShapedContacts() throws Exception {
        String input = "5 7\n" +
                "contact 1 2\n" +
                "contact 1 3\n" +
                "contact 1 4\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n" +
                "size 4\n";

        List<String> expectedOutput = List.of("4", "3", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testRepeatedContactsInSameGroup() throws Exception {
        String input = "5 6\n" +
                "contact 1 2\n" +
                "contact 3 4\n" +
                "contact 1 3\n" +
                "size 1\n" +
                "contact 2 4\n" +
                "size 1\n";

        List<String> expectedOutput = List.of("4", "4");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testFullMergeAndFullRollback() throws Exception {
        String input = "4 10\n" +
                "contact 1 2\n" +
                "contact 3 4\n" +
                "contact 1 3\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n";

        List<String> expectedOutput = List.of("4", "2", "2", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testComplexSequence() throws Exception {
        String input = "10 10\n" +
                "contact 1 2\n" +
                "contact 3 4\n" +
                "undo\n" +
                "contact 5 6\n" +
                "size 1\n" +
                "contact 1 5\n" +
                "size 1\n" +
                "undo\n" +
                "size 1\n" +
                "size 5\n";

        List<String> expectedOutput = List.of("2", "4", "2", "2");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testWithLargeIds() throws Exception {
        String input = "200000 5\n" +
                "contact 1 200000\n" +
                "size 1\n" +
                "size 200000\n" +
                "undo\n" +
                "size 1\n";

        List<String> expectedOutput = List.of("2", "2", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testHistoryIsUnaffectedByNonContactCommands() throws Exception {
        String input = "5 8\n" +
                "contact 1 2\n" +
                "contact 3 4\n" +
                "size 1\n" +
                "undo\n" +
                "size 3\n" +
                "undo\n" +
                "size 1\n" +
                "undo\n";

        List<String> expectedOutput = List.of("2", "1", "1");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }

    @Test
    public void testPerformanceOnLargeInput() throws Exception {
        String input = "100 20\n" +
                "contact 1 2\n" +
                "contact 2 3\n" +
                "contact 3 4\n" +
                "contact 4 5\n" +
                "contact 5 6\n" +
                "contact 6 7\n" +
                "contact 7 8\n" +
                "contact 8 9\n" +
                "contact 9 10\n" +
                "contact 10 11\n" +
                "size 1\n" +
                "size 1\n" +
                "size 1\n" +
                "size 1\n" +
                "size 1\n" +
                "undo\n" +
                "undo\n" +
                "undo\n" +
                "undo\n" +
                "undo\n";

        List<String> expectedOutput = List.of("11", "11", "11", "11", "11");
        assertEquals(expectedOutput, runMainAndCaptureOutput(input));
    }
}