package by.bsuir.dsa.csv2025.gr410902.Андала;

import java.io.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Solution {

    public static List<Integer> possibleK(int[] A, int[] B) {
        int n = A.length;
        int[] current = Arrays.copyOf(A, n);

        // minPos[i] — самая правая позиция минимального элемента в суффиксе A[i..n-1]
        int[] minPos = new int[n];
        int minVal = Integer.MAX_VALUE;
        int minIdx = n; // начальное значение — за пределами массива

        for (int i = n - 1; i >= 0; i--) {
            if (A[i] < minVal) {
                minVal = A[i];
                minIdx = i;
            }
            minPos[i] = minIdx;
        }

        List<Integer> result = new ArrayList<>();
        int swaps = 0;

        for (int i = 0; i < n; i++) {
            int j = minPos[i];

            // Делаем обмен только если нужно
            if (i != j) {
                int temp = current[i];
                current[i] = current[j];
                current[j] = temp;
                swaps++;
            }

            // Проверяем совпадение ТОЛЬКО ПОСЛЕ i-го шага (включая "пропущенные" шаги)
            if (Arrays.equals(current, B)) {
                result.add(swaps);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] partsA = sc.nextLine().trim().split("\\s+");
        int[] A = new int[partsA.length];
        for (int i = 0; i < A.length; i++) A[i] = Integer.parseInt(partsA[i]);

        String[] partsB = sc.nextLine().trim().split("\\s+");
        int[] B = new int[partsB.length];
        for (int i = 0; i < B.length; i++) B[i] = Integer.parseInt(partsB[i]);

        List<Integer> ks = possibleK(A, B);

        if (ks.isEmpty()) {
            System.out.println();
        } else {
            for (int i = 0; i < ks.size(); i++) {
                if (i > 0) System.out.print(" ");
                System.out.print(ks.get(i));
            }
            System.out.println();
        }
    }

    // ====================== ТЕСТЫ ======================

    private String runMain(String input) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try {
            main(new String[0]);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
        return baos.toString().replace("\r\n", "\n").trim();
    }

    @Test public void test0() { assertEquals("1", runMain("5 3 4 1 2\n1 3 4 5 2")); }
    @Test public void test1() { assertEquals("1", runMain("4 2 1 3\n1 2 4 3")); }
    @Test public void test2() { assertEquals("1", runMain("6 5 4 3 2 1\n1 5 4 3 2 6")); }
    @Test public void test3() { assertEquals("0 0 0 0", runMain("1 2 3 4\n1 2 3 4")); }
    @Test public void test4() { assertEquals("1 1 1 1", runMain("2 1 3 4\n1 2 3 4")); }
    @Test public void test5() { assertEquals("1 1", runMain("3 1 4 2\n1 3 4 2")); }
    @Test public void test6() { assertEquals("1", runMain("4 2 5 1 3\n1 2 5 4 3")); }
    @Test public void test7() { assertEquals("1 1 1", runMain("1 3 2 4\n1 2 3 4")); }
    @Test public void test8() { assertEquals("1", runMain("4 3 2 1\n1 3 2 4")); }
    @Test public void test9() { assertEquals("1", runMain("7 5 3 1 6 4 2\n1 5 3 7 6 4 2")); }
}