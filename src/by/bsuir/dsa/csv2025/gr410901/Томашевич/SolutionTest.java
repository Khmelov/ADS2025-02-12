package by.bsuir.dsa.csv2025.gr410901.Томашевич;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

// Класс с тестами
public class SolutionTest {

    @Test
    public void test1_BasicGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(1, 3, 4),
                new Solution.Edge(2, 3, 2),
                new Solution.Edge(2, 4, 5),
                new Solution.Edge(3, 4, 3)
        );
        long result = Solution.kruskalAlgorithm(4, edges);
        assertEquals(6, result);
    }

    @Test
    public void test2_NotConnectedGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 5)
        );
        long result = Solution.kruskalAlgorithm(3, edges);
        assertEquals(-1, result);
    }

    @Test
    public void test3_SingleVertex() {
        List<Solution.Edge> edges = new ArrayList<>();
        long result = Solution.kruskalAlgorithm(1, edges);
        assertEquals(0, result);
    }

    @Test
    public void test4_NegativeWeights() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, -5),
                new Solution.Edge(2, 3, 3),
                new Solution.Edge(1, 3, 1)
        );
        long result = Solution.kruskalAlgorithm(3, edges);
        assertEquals(-4, result);
    }

    @Test
    public void test5_CompleteGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(1, 3, 2),
                new Solution.Edge(1, 4, 3),
                new Solution.Edge(2, 3, 4),
                new Solution.Edge(2, 4, 5),
                new Solution.Edge(3, 4, 6)
        );
        long result = Solution.kruskalAlgorithm(4, edges);
        assertEquals(6, result);
    }

    @Test
    public void test6_LargeGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 10),
                new Solution.Edge(1, 3, 20),
                new Solution.Edge(2, 3, 30),
                new Solution.Edge(2, 4, 5),
                new Solution.Edge(3, 4, 15),
                new Solution.Edge(3, 5, 25),
                new Solution.Edge(4, 5, 8)
        );
        long result = Solution.kruskalAlgorithm(5, edges);
        assertEquals(38, result);
    }

    @Test
    public void test7_TwoVertices() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 100)
        );
        long result = Solution.kruskalAlgorithm(2, edges);
        assertEquals(100, result);
    }

    @Test
    public void test8_ThreeVerticesChain() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 5),
                new Solution.Edge(2, 3, 10)
        );
        long result = Solution.kruskalAlgorithm(3, edges);
        assertEquals(15, result);
    }

    @Test
    public void test9_CycleGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(2, 3, 2),
                new Solution.Edge(3, 4, 3),
                new Solution.Edge(1, 4, 4)
        );
        long result = Solution.kruskalAlgorithm(4, edges);
        assertEquals(6, result);
    }

    @Test
    public void test10_LinearGraph() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(2, 3, 1),
                new Solution.Edge(3, 4, 1),
                new Solution.Edge(4, 5, 1)
        );
        long result = Solution.kruskalAlgorithm(5, edges);
        assertEquals(4, result);
    }

    @Test
    public void test11_FormatResultConnected() {
        String result = Solution.formatResult(42);
        assertEquals("42", result);
    }

    @Test
    public void test12_FormatResultNotConnected() {
        String result = Solution.formatResult(-1);
        assertEquals("GRAPH IS NOT CONNECTED", result);
    }

    @Test
    public void test13_EmptyGraph() {
        List<Solution.Edge> edges = new ArrayList<>();
        long result = Solution.kruskalAlgorithm(0, edges);
        assertEquals(0, result);
    }

    @Test
    public void test14_SingleEdgeHighWeight() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1000000000)
        );
        long result = Solution.kruskalAlgorithm(2, edges);
        assertEquals(1000000000, result);
    }

    @Test
    public void test15_MultipleComponents() {
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(3, 4, 2)
        );
        long result = Solution.kruskalAlgorithm(4, edges);
        assertEquals(-1, result);
    }
}