package by.bsuir.dsa.csv2025.gr410901.Гаркуша;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SolutionTest.Test1.class,
        SolutionTest.Test2.class,
        SolutionTest.Test3.class,
        SolutionTest.Test4.class,
        SolutionTest.Test5.class,
        SolutionTest.Test6.class,
        SolutionTest.Test7.class,
        SolutionTest.Test8.class,
        SolutionTest.Test9.class,
        SolutionTest.Test10.class,
})
public class SolutionTest {

    public static class Test1 {
        @Test
        public void test() {
            testCase("A C\nA - B 5\nB - C 3", "A - B - C 8");
        }
    }

    public static class Test2 {
        @Test
        public void test() {
            testCase("A D\nA - B 4\nA - C 1\nB - D 1\nC - B 2\nC - D 5", "A - C - B - D 4");
        }
    }

    public static class Test3 {
        @Test
        public void test() {
            testCase("A B\nA - B 10\nA - C 2\nC - B 1\nB - D 3", "A - C - B 3");
        }
    }

    public static class Test4 {
        @Test
        public void test() {
            testCase("A D\nA - B 2\nB - C 3\nC - A 1\nC - D 4", "A - B - C - D 9");
        }
    }

    public static class Test5 {
        @Test
        public void test() {
            testCase("Start End\nStart - A 3\nStart - B 1\nA - End 2\nB - End 4\nB - A 1", "Start - B - A - End 4");
        }
    }

    public static class Test6 {
        @Test
        public void test() {
            testCase("X End\nX - Y 5\nY - Z 2\nA - B 10\nZ - End 1", "X - Y - Z - End 8");
        }
    }

    public static class Test7 {
        @Test
        public void test() {
            testCase("1 5\n1 - 2 10\n1 - 3 1\n2 - 4 1\n3 - 2 1\n3 - 4 20\n4 - 5 2", "1 - 3 - 2 - 4 - 5 5");
        }
    }

    public static class Test8 {
        @Test
        public void test() {
            testCase("A F\nA - B 2\nB - C 1\nC - D 3\nD - E 1\nE - F 2", "A - B - C - D - E - F 9");
        }
    }

    public static class Test9 {
        @Test
        public void test() {
            testCase("A F\nA - B 4\nA - C 2\nB - D 5\nC - D 1\nC - E 3\nD - F 2\nE - F 4", "A - C - D - F 5");
        }
    }

    public static class Test10 {
        @Test
        public void test() {
            testCase("Home Work\nHome - Shop 5\nHome - Park 2\nPark - Shop 1\nShop - Work 3\nPark - Work 6", "Home - Park - Shop - Work 6");
        }
    }

    private static void testCase(String input, String expectedOutput) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(out);

            InputStream oldIn = System.in;
            PrintStream oldOut = System.out;

            try {
                System.setIn(in);
                System.setOut(printStream);
                Solution.main(new String[]{});
            } finally {
                System.setIn(oldIn);
                System.setOut(oldOut);
            }

            String result = out.toString().trim();
            assertEquals(expectedOutput, result);
        } catch (Exception e) {
            fail("Тест завершился с ошибкой: " + e.getMessage());
        }
    }
}
