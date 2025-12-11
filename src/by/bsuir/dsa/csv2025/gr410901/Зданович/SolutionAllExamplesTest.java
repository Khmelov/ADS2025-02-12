package by.bsuir.dsa.csv2025.gr410901.Зданович;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SolutionAllExamplesTest {

    private String runWithInput(String input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        java.io.InputStream oldIn = System.in;
        try {
            System.setOut(new PrintStream(out));
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Solution.main(new String[0]);
            return out.toString().replace("\r\n", "\n").trim();
        } finally {
            System.setOut(oldOut);
            System.setIn(oldIn);
        }
    }

    // Пример 1
    @Test
    public void testExample1() {
        String input = String.join("\n",
                "A-B(5), B-C(3), C-D(4), A-D(10)",
                "3",
                "MST",
                "CONNECTED A C",
                "CONNECTED A E"
        );
        String expected = String.join("\n",
                "12",
                "YES",
                "NO"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 2
    @Test
    public void testExample2() {
        String input = String.join("\n",
                "A-B(2), B-C(2), D-E(1)",
                "2",
                "MST",
                "CONNECTED A D"
        );
        String expected = String.join("\n",
                "NO MST",
                "NO"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 3
    @Test
    public void testExample3() {
        String input = String.join("\n",
                "A-B(10), B-C(10), C-D(10), D-E(10)",
                "2",
                "MST",
                "CONNECTED B E"
        );
        String expected = String.join("\n",
                "40",
                "YES"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 4
    @Test
    public void testExample4() {
        String input = String.join("\n",
                "A-B(3), B-C(4), C-A(5)",
                "3",
                "MST",
                "CONNECTED A B",
                "CONNECTED A D"
        );
        String expected = String.join("\n",
                "7",
                "YES",
                "NO"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 5
    @Test
    public void testExample5() {
        String input = String.join("\n",
                "X-Y(7), Y-Z(1), Z-W(2), X-W(100)",
                "3",
                "MST",
                "CONNECTED X W",
                "CONNECTED X Q"
        );
        String expected = String.join("\n",
                "10",
                "YES",
                "NO"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 6
    @Test
    public void testExample6() {
        String input = String.join("\n",
                "A-B(5), B-C(6), C-D(7), A-D(100), B-D(1)",
                "3",
                "MST",
                "CONNECTED A D",
                "CONNECTED C A"
        );
        String expected = String.join("\n",
                "12",
                "YES",
                "YES"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 7
    @Test
    public void testExample7() {
        String input = String.join("\n",
                "K-L(2), L-M(2), M-N(2), N-K(2), K-M(10)",
                "2",
                "MST",
                "CONNECTED L N"
        );
        String expected = String.join("\n",
                "6",
                "YES"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 8
    @Test
    public void testExample8() {
        String input = String.join("\n",
                "A-B(1), C-D(1), E-F(1)",
                "4",
                "CONNECTED A B",
                "CONNECTED A C",
                "MST",
                "CONNECTED E F"
        );
        String expected = String.join("\n",
                "YES",
                "NO",
                "NO MST",
                "YES"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 9
    @Test
    public void testExample9() {
        String input = String.join("\n",
                "S-T(4), T-U(8), U-V(3), V-W(9), S-W(20), T-V(1)",
                "3",
                "MST",
                "CONNECTED S V",
                "CONNECTED X Y"
        );
        String expected = String.join("\n",
                "17",
                "YES",
                "NO"
        );
        assertEquals(expected, runWithInput(input));
    }

    // Пример 10
    @Test
    public void testExample10() {
        String input = String.join("\n",
                "P-Q(3), Q-R(5), R-S(4), S-T(6), T-P(2), Q-S(7)",
                "4",
                "MST",
                "CONNECTED P R",
                "CONNECTED P X",
                "CONNECTED R T"
        );
        String expected = String.join("\n",
                "14",
                "YES",
                "NO",
                "YES"
        );
        assertEquals(expected, runWithInput(input));
    }
}