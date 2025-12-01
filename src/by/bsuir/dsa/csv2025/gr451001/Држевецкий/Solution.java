package by.bsuir.dsa.csv2025.gr451001.Држевецкий;

import java.util.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Solution {

    public static boolean canSplit(int[] a, int k, long maxSeg) {
        long currentSum = 0;
        int segments = 1;

        for (int x : a) {
            if (x > maxSeg) return false;

            if (currentSum + x <= maxSeg) {
                currentSum += x;
            } else {
                segments++;
                currentSum = x;
            }
        }
        return segments <= k;
    }

    public static long minimizeMaxSegmentSum(int[] a, int k) {
        long left = Arrays.stream(a).max().getAsInt();
        long right = Arrays.stream(a).sum();
        long ans = right;

        while (left <= right) {
            long mid = (left + right) / 2;

            if (canSplit(a, k, mid)) {
                ans = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();  // количество элементов
        int k = sc.nextInt();  // количество сегментов

        int[] a = new int[n]; // сами элементы
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        System.out.println(minimizeMaxSegmentSum(a, k));
    }

    @Test(timeout = 2000)
    public void check_A() throws Exception {
        long res = minimizeMaxSegmentSum(new int[]{1, 2, 3}, 3);
        boolean ok = Long.toString(res).equals("3");
        assertTrue("failed", ok);
    }

    @Test(timeout = 2000)
    public void check_B() throws Exception {
        long res = minimizeMaxSegmentSum(new int[]{7, 2, 5, 10, 8}, 2);
        boolean ok = Long.toString(res).equals("18");
        assertTrue("failed", ok);
    }

    @Test(timeout = 2000)
    public void check_C() throws Exception {
        long res = minimizeMaxSegmentSum(new int[]{1, 2, 3, 4, 5, 6, 7, 8}, 4);
        boolean ok = Long.toString(res).equals("11");
        assertTrue("failed", ok);
    }

    @Test(timeout = 2000)
    public void check_D() throws Exception {
        long res = minimizeMaxSegmentSum(new int[]{9, 1, 8, 2, 7}, 3);
        boolean ok = Long.toString(res).equals("9");
        assertTrue("failed", ok);
    }

    @Test(timeout = 2000)
    public void check_E() throws Exception {
        long res = minimizeMaxSegmentSum(new int[]{5, 5, 5, 5, 5, 5}, 3);
        boolean ok = Long.toString(res).equals("10");
        assertTrue("failed", ok);
    }
}