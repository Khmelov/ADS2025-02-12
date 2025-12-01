package by.bsuir.dsa.csv2025.gr451003.Каминский;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.*;

public class tests {

    @Test
    public void testTwoComponents() {
        int n = 6;
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 5),
                new Solution.Edge(2, 3, 7),
                new Solution.Edge(4, 5, 2),
                new Solution.Edge(5, 6, 3)
        );

        Solution.TestResult result = Solution.calculateMST(n, edges);
        assertEquals(2, result.componentCount);
        assertEquals(17, result.totalWeight);
    }

    @Test
    public void testSingleComponent() {
        int n = 4;
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 3),
                new Solution.Edge(2, 3, 4),
                new Solution.Edge(3, 1, 5),
                new Solution.Edge(3, 4, 1),
                new Solution.Edge(2, 4, 6),
                new Solution.Edge(1, 4, 2)
        );

        Solution.TestResult result = Solution.calculateMST(n, edges);
        assertEquals(1, result.componentCount);
        assertEquals(6, result.totalWeight);
    }

    @Test
    public void testIsolatedVertices() {
        int n = 3;
        List<Solution.Edge> edges = Arrays.asList();

        Solution.TestResult result = Solution.calculateMST(n, edges);
        assertEquals(3, result.componentCount);
        assertEquals(0, result.totalWeight);
    }

    @Test
    public void testSingleEdge() {
        int n = 2;
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 10)
        );

        Solution.TestResult result = Solution.calculateMST(n, edges);
        assertEquals(1, result.componentCount);
        assertEquals(10, result.totalWeight);
    }

    @Test
    public void testComplexGraph() {
        int n = 7;
        List<Solution.Edge> edges = Arrays.asList(
                new Solution.Edge(1, 2, 1),
                new Solution.Edge(2, 3, 2),
                new Solution.Edge(4, 5, 3),
                new Solution.Edge(5, 6, 4),
                new Solution.Edge(6, 4, 5),
                new Solution.Edge(7, 7, 0)
        );

        Solution.TestResult result = Solution.calculateMST(n, edges);
        assertEquals(3, result.componentCount);
        assertEquals(10, result.totalWeight);
    }
}