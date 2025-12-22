package by.bsuir.dsa.csv2025.gr410902.Плехова;//  Этот класс содержит main, который запускает JUnitCore.

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

////////////НУЖНО ЗАПУСТИТЬ ЭТОТ КЛАСС ДЛЯ ПРОВЕРКИ/////////////
public class SolutionTest {

    // main запускает тесты автоматически при запуске файла
    public static void main(String[] args) {
        System.out.println("Запуск тестов SolutionTest...");
        Result result = JUnitCore.runClasses(SolutionTest.class);

        for (Failure f : result.getFailures()) {
            System.out.println("FAIL: " + f.toString());
        }

        if (result.wasSuccessful()) {
            System.out.println("\nALL TESTS PASSED");
            System.exit(0);
        } else {
            System.out.println("\nTESTS FAILED (count = " + result.getFailureCount() + ")");
            System.exit(1);
        }
    }

    // Вспомогательная функция: запуск main() с подстановкой System.in/out
    private String runMain(String input) throws Exception {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            System.setIn(in);
            System.setOut(new PrintStream(out));

            Solution.main(new String[]{});

            return out.toString().trim();
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }

    @Test(timeout = 2000)
    public void test1() throws Exception {
        assertEquals("7", runMain("3\n1 5\n2 6\n8 10\n"));
    }

    @Test(timeout = 2000)
    public void test2() throws Exception {
        assertEquals("8", runMain("2\n0 4\n4 8\n"));
    }

    @Test(timeout = 2000)
    public void test3() throws Exception {
        assertEquals("15", runMain("4\n-5 0\n-2 3\n1 6\n5 10\n"));
    }

    @Test(timeout = 2000)
    public void test4() throws Exception {
        assertEquals("100", runMain("1\n0 100\n"));
    }

    @Test(timeout = 2000)
    public void test5() throws Exception {
        assertEquals("6", runMain("3\n1 3\n3 5\n5 7\n"));
    }

    @Test(timeout = 2000)
    public void test6() throws Exception {
        assertEquals("5", runMain("5\n1 2\n2 3\n3 4\n4 5\n5 6\n"));
    }

    @Test(timeout = 2000)
    public void test7() throws Exception {
        assertEquals("5", runMain("3\n0 5\n1 2\n3 4\n"));
    }

    @Test(timeout = 2000)
    public void test8() throws Exception {
        assertEquals("10", runMain("2\n-10 -5\n-7 0\n"));
    }

    @Test(timeout = 2000)
    public void test9() throws Exception {
        assertEquals("6", runMain("4\n1 3\n4 6\n2 5\n7 8\n"));
    }

    @Test(timeout = 2000)
    public void test10() throws Exception {
        assertEquals("6", runMain("6\n1 2\n3 4\n2 3\n5 6\n0 1\n4 5\n"));
    }
}
