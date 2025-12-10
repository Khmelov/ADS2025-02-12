package by.bsuir.dsa.csv2025.gr451001.Александрович;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SolutionTest {
    @Test
    public void Test1() {
        Solution.length = 1;
        Solution.input = new String[]{"5"};
        assertTrue(Solution.Solution(Solution.generateTree()));
    }

    @Test
    public void Test2() {
        Solution.length = 3;
        Solution.input = new String[]{"2", "1", "3"};
        assertTrue(Solution.Solution(Solution.generateTree()));
    }

    @Test
    public void Test3() {
        Solution.length = 7;
        Solution.input = new String[]{"5","1","6","null","null","3","7"};
        assertFalse(Solution.Solution(Solution.generateTree()));
    }
}