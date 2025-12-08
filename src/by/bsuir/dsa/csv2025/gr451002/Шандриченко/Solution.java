package by.bsuir.dsa.csv2025.gr451002.Шандриченко;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        scanner.close();
        
        int period = findSolution(s);
        System.out.println(period);
    }
    
    public static int findSolution(String s) {
        int n = s.length();
        if (n == 0) return 0;
        
        int[] pi = computePrefixFunction(s);
        int candidate = n - pi[n - 1];
        
        return (n % candidate == 0) ? candidate : n;
    }
    
    public static int[] computePrefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];
        
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            
            pi[i] = j;
        }
        
        return pi;
    }
}
