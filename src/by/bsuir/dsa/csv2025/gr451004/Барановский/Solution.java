package by.bsuir.dsa.csv2025.gr451004.Барановский;

import java.util.*;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int n = scanner.nextInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        int[] result = compressCoordinates(arr);
        
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i]);
            if (i < result.length - 1) {
                System.out.print(" ");
            }
        }
    }
    
    public static int[] compressCoordinates(int[] arr) {
        if (arr.length == 0) {
            return new int[0];
        }
        
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        
        int[] unique = Arrays.stream(sorted).distinct().toArray();
        
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            int index = Arrays.binarySearch(unique, arr[i]);
            result[i] = index;
        }
        
        return result;
    }
}