package by.bsuir.dsa.csv2025.gr451002.Вишневский;

import org.junit.Test;
import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void testEmptyPosition() {
        assertFalse(Solution.isWinningKnim(new int[]{}, 1));
        assertFalse(Solution.isWinningKnim(null, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidK() {
        Solution.isWinningKnim(new int[]{1, 2, 3}, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePile() {
        Solution.isWinningKnim(new int[]{1, -3, 4}, 2);
    }

    @Test
    public void testClassicNim() {
        assertFalse(Solution.isWinningKnim(new int[]{1, 1}, 1));  
        assertTrue(Solution.isWinningKnim(new int[]{1, 2}, 1));   
        assertFalse(Solution.isWinningKnim(new int[]{5, 5}, 1));
        assertTrue(Solution.isWinningKnim(new int[]{7, 7, 1}, 1));
    }

    @Test
    public void testKnimK2() {
        assertFalse(Solution.isWinningKnim(new int[]{1,1,1}, 2));
        assertTrue(Solution.isWinningKnim(new int[]{1,1}, 2));
        assertTrue(Solution.isWinningKnim(new int[]{2,2}, 2));
    }

    @Test
    public void testComputeModBits() {
        int[] modBits = Solution.computeModBits(new int[]{1, 1, 1}, 2);
        assertEquals(1, modBits.length);
        assertEquals(0, modBits[0]);
    }
}
