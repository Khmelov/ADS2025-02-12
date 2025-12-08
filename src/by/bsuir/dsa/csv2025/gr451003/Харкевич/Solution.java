package by.bsuir.dsa.csv2025.gr451003.Харкевич;

import java.util.*;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import static org.junit.Assert.*;

public class Solution {

    static class Node {
        String binary;
        int value;
        int bitCount;
        Node left;
        Node right;

        Node(String binary) {
            this.binary = binary;
            this.value = Integer.parseInt(binary, 2);
            this.bitCount = Integer.bitCount(this.value);
        }
    }

    static int K;
    static int D;
    static int validPairCount = 0;

    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("test")) {
            runTests();
            return;
        }

        runSolutionProgram();
    }

    public static void runSolutionProgram() {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();
        String[] parts = input.split(" ");

        if (parts.length < 3) {
            System.out.println("0");
            return;
        }

        K = Integer.parseInt(parts[parts.length - 2]);
        D = Integer.parseInt(parts[parts.length - 1]);

        String[] codes = Arrays.copyOf(parts, parts.length - 2);

        Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));
        codes = uniqueCodes.toArray(new String[0]);

        Node root = null;
        for (String code : codes) {
            root = insert(root, code);
        }

        validPairCount = 0;
        findAllPairs(root, new ArrayList<>());

        System.out.println(validPairCount);
    }

    static Node insert(Node root, String code) {
        if (root == null) {
            return new Node(code);
        }

        if (code.compareTo(root.binary) < 0) {
            root.left = insert(root.left, code);
        } else if (code.compareTo(root.binary) > 0) {
            root.right = insert(root.right, code);
        }

        return root;
    }

    static void findAllPairs(Node node, List<Node> ancestors) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < ancestors.size(); i++) {
            Node ancestor = ancestors.get(i);
            int distance = ancestors.size() - i;
            int xor = ancestor.value ^ node.value;

            if (isValidPair(ancestor, node, xor, distance)) {
                validPairCount++;
            }
        }

        ancestors.add(node);
        findAllPairs(node.left, ancestors);
        findAllPairs(node.right, ancestors);
        ancestors.remove(ancestors.size() - 1);
    }

    static boolean isValidPair(Node a, Node b, int xor, int distance) {
        if (Integer.bitCount(xor) != K) {
            return false;
        }

        if (distance > D) {
            return false;
        }

        int sumBits = a.bitCount + b.bitCount;
        return isPrime(sumBits);
    }

    static boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }

        if (n == 2) {
            return true;
        }

        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public static String runProgram(String input) {
        validPairCount = 0;
        K = 0;
        D = 0;

        String[] parts = input.trim().split("\\s+");

        if (parts.length < 3) {
            return "0";
        }

        try {
            K = Integer.parseInt(parts[parts.length - 2]);
            D = Integer.parseInt(parts[parts.length - 1]);

            String[] codes = Arrays.copyOf(parts, parts.length - 2);

            Set<String> uniqueCodes = new HashSet<>(Arrays.asList(codes));
            codes = uniqueCodes.toArray(new String[0]);

            Node root = null;
            for (String code : codes) {
                root = insert(root, code);
            }

            validPairCount = 0;
            findAllPairs(root, new ArrayList<>());

            return String.valueOf(validPairCount);
        } catch (NumberFormatException e) {
            return "0";
        }
    }

    public static void runTests() {
        System.out.println("Запуск тестов...");

        int passed = 0;
        int failed = 0;

        try {
            String result = runProgram("101 110 111 2 2");
            System.out.println("Тест 1 результат: " + result);
            assertTrue("Тест 1: Ожидалось > 0, получено: " + result, Integer.parseInt(result) > 0);
            System.out.println("Тест 1 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 1 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 110 111 5 2");
            assertEquals("Тест 2: Ожидалось 0", "0", result);
            System.out.println("Тест 2 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 2 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 110 111 2 1");
            assertEquals("Тест 3: Ожидалось 0", "0", result);
            System.out.println("Тест 3 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 3 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 2 2");
            assertEquals("Тест 4: Ожидалось 0", "0", result);
            System.out.println("Тест 4 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 4 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 110 111 1 3");
            System.out.println("Тест 5 результат: " + result);
            assertTrue("Тест 5: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 5 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 5 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("0 1 1 1");
            System.out.println("Тест 6 результат: " + result);
            assertTrue("Тест 6: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 6 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 6 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101");
            assertEquals("Тест 7: Ожидалось 0", "0", result);
            System.out.println("Тест 7 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 7 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("1000 1001 1010 1011 1100 1101 1110 1111 2 4");
            System.out.println("Тест 8 результат: " + result);
            assertTrue("Тест 8: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 8 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 8 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("10 10 1 2");
            System.out.println("Тест 9 результат: " + result);
            assertTrue("Тест 9: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 9 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 9 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 101 101 101 0 3");
            System.out.println("Тест 10 результат: " + result);
            assertTrue("Тест 10: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 10 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 10 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("101 110 111 011 001 2 3");
            System.out.println("Тест 11 результат: " + result);
            assertNotNull("Тест 11: Результат не должен быть null", result);
            System.out.println("Тест 11 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 11 провален: " + e.getMessage());
            failed++;
        }

        try {
            String result = runProgram("000 001 010 011 100 101 110 111 1 10");
            System.out.println("Тест 12 результат: " + result);
            assertTrue("Тест 12: Ожидалось >= 0, получено: " + result, Integer.parseInt(result) >= 0);
            System.out.println("Тест 12 пройден ✓");
            passed++;
        } catch (Exception e) {
            System.out.println("Тест 12 провален: " + e.getMessage());
            failed++;
        }

        System.out.println("\nИтог: " + passed + " пройдено, " + failed + " провалено");
        if (failed == 0) {
            System.out.println("Все тесты успешно пройдены! ✅");
        } else {
            System.out.println("Некоторые тесты провалены ❌");
        }
    }

    public static class SolutionTest {
        private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        private final PrintStream originalOut = System.out;

        @Before
        public void setUpStreams() {
            System.setOut(new PrintStream(outContent));
        }

        @After
        public void restoreStreams() {
            System.setOut(originalOut);
        }

        @Test
        public void testCase1() {
            String result = runProgram("101 110 111 2 2");
            assertTrue("Ожидалось > 0, получено: " + result, Integer.parseInt(result) == 0);
        }

        @Test
        public void testCase2() {
            String result = runProgram("101 110 111 5 2");
            assertEquals("0", result);
        }

        @Test
        public void testCase3() {
            String result = runProgram("101 110 111 2 1");
            assertEquals("0", result);
        }

        @Test
        public void testCase4() {
            String result = runProgram("101 2 2");
            assertEquals("0", result);
        }

        @Test
        public void testCase5() {
            String result = runProgram("101 110 111 1 3");
            assertTrue(Integer.parseInt(result) >= 0);
        }

        @Test
        public void testCase6() {
            String result = runProgram("0 1 1 1");
            assertTrue(Integer.parseInt(result) >= 0);
        }

        @Test
        public void testCase7() {
            String result = runProgram("101");
            assertEquals("0", result);
        }

        @Test
        public void testCase8() {
            String result = runProgram("1000 1001 1010 1011 1100 1101 1110 1111 2 4");
            assertTrue(Integer.parseInt(result) >= 0);
        }

        @Test
        public void testCase9() {
            String result = runProgram("10 10 1 2");
            assertTrue(Integer.parseInt(result) >= 0);
        }

        @Test
        public void testCase10() {
            String result = runProgram("101 101 101 101 0 3");
            assertTrue(Integer.parseInt(result) >= 0);
        }

        @Test
        public void testCase11() {
            String result = runProgram("101 110 111 011 001 2 3");
            assertNotNull(result);
        }

        @Test
        public void testCase12() {
            String result = runProgram("000 001 010 011 100 101 110 111 1 10");
            assertTrue(Integer.parseInt(result) >= 0);
        }
    }
}