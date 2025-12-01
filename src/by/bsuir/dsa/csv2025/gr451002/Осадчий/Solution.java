package by.bsuir.dsa.csv2025.gr451002.Осадчий;

import java.util.Arrays;
import java.util.Scanner;

public class Solution {
    
    public static boolean isWinningPosition(int[] piles) {
        return calculateNimSum(piles) != 0;
    }
    
    public static int calculateNimSum(int[] piles) {
        int result = 0;
        for (int pile : piles) {
            result ^= pile;
        }
        return result;
    }
    
    public static int[] findWinningMove(int[] piles) {
        int nimSum = calculateNimSum(piles);
        
        if (nimSum == 0) {
            return null;
        }
        
        for (int i = 0; i < piles.length; i++) {
            int newSize = piles[i] ^ nimSum;
            if (newSize < piles[i]) {
                return new int[]{i, newSize};
            }
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Введите размеры кучек через пробел:");
        String input = scanner.nextLine().trim();
        
        String[] parts = input.split("\\s+");
        int[] piles = new int[parts.length];
        
        for (int i = 0; i < parts.length; i++) {
            piles[i] = Integer.parseInt(parts[i]);
        }
        
        int nimSum = calculateNimSum(piles);
        boolean isWinning = isWinningPosition(piles);
        int[] move = findWinningMove(piles);
        
        System.out.println(nimSum);
        System.out.println(isWinning);
        if (move != null) {
            System.out.println(move[0] + " " + move[1]);
        } else {
            System.out.println("no move");
        }
        
        scanner.close();
    }
}