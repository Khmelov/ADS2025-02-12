package by.bsuir.dsa.csv2025.gr451001.Забелич;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int[] arr = new int[n];


        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }


        int minLeft = arr[0];
        int maxDiff = 0;

        for (int j = 1; j < n; j++) {
            maxDiff = Math.max(maxDiff, arr[j] - minLeft);
            minLeft = Math.min(minLeft, arr[j]);
        }


        System.out.println(maxDiff);
    }
}