package by.bsuir.dsa.csv2025.gr410901.Заверач;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.assertEquals;

public class Solution {

    public static String solve(int n) {

        if (n < 5) {
            return "";
        }

        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false;
        isPrime[1] = false;

        for (int i = 2; i * i <= n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int p = 3; p + 2 <= n; p += 2) {
            if (isPrime[p] && isPrime[p + 2]) {
                result.append(p).append(" ").append(p + 2).append(" ");
            }
        }

        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextInt()) {
            return;
        }
        int n = sc.nextInt();
        System.out.print(solve(n));
    }

    @Before
    public void before() {}

    @After
    public void after() {}

    @Test public void test1()  { assertEquals("", solve(3)); }
    @Test public void test2()  { assertEquals("3 5", solve(5)); }
    @Test public void test3()  { assertEquals("3 5 5 7", solve(7)); }
    @Test public void test4()  { assertEquals("3 5 5 7", solve(10)); }
    @Test public void test5()  { assertEquals("3 5 5 7 11 13", solve(13)); }
    @Test public void test6()  { assertEquals("3 5 5 7 11 13", solve(17)); }
    @Test public void test7()  { assertEquals("3 5 5 7 11 13 17 19", solve(19)); }
    @Test public void test8()  { assertEquals("3 5 5 7 11 13 17 19", solve(30)); }
    @Test public void test9()  { assertEquals("3 5 5 7 11 13 17 19 29 31", solve(41)); }
    @Test public void test10() { assertEquals("3 5 5 7 11 13 17 19 29 31 41 43 59 61 71 73", solve(100)); }
}