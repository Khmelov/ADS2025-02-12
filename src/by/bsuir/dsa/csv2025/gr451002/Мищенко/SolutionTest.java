package by.bsuir.dsa.csv2025.gr451002.Мищенко;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class SolutionTest {

    @Test
    public void testPrefixFunctionBasicExample() {
        int[] pi = Solution.prefixFunction("abacabad");
        assertEquals(0, pi[0]);
        assertEquals(0, pi[1]);
        assertEquals(1, pi[2]);
        assertEquals(0, pi[3]);
        assertEquals(1, pi[4]);
        assertEquals(2, pi[5]);
        assertEquals(3, pi[6]);
        //assertEquals(1, pi[7]);
    }

    @Test
    public void testSingleOccurrence() {
        List<Integer> positions = Solution.findOccurrences("PASSPORT BLOCKED",
                "subject said PASSPORT BLOCKED once");
        assertEquals(Collections.singletonList(13), positions);
    }

    @Test
    public void testMultipleOccurrencesIncludingOverlap() {
        List<Integer> positions = Solution.findOccurrences("SOS",
                "SOSOS call log forwarded");
        assertEquals(Arrays.asList(0, 2), positions);
    }

    @Test
    public void testNoOccurrences() {
        List<Integer> positions = Solution.findOccurrences("PASSPORT BLOCKED",
                "status green, no issues reported");
        assertEquals(Collections.emptyList(), positions);
    }

    @Test
    public void testPatternLongerThanText() {
        List<Integer> positions = Solution.findOccurrences("PASSPORT", "id");
        assertEquals(Collections.emptyList(), positions);
    }


}