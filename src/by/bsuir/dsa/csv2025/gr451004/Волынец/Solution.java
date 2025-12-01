package by.bsuir.dsa.csv2025.gr451004.Волынец;

import java.util.*;

public class Solution {
    static int asciiHash(String s) {
        int sum = 0;
        for (char c : s.toCharArray()) sum += c;
        return sum & 0xFFFF;
    }

    static int fnv1aHash(String s) {
        int hash = 0x811C9DC5;
        int prime = 0x01000193;
        for (char c : s.toCharArray()) {
            hash ^= c;
            hash *= prime;
        }
        return hash & 0xFFFF;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String s1 = sc.nextLine();
        String s2 = sc.nextLine();

        int h1a = asciiHash(s1), h2a = asciiHash(s2);
        int h1b = fnv1aHash(s1), h2b = fnv1aHash(s2);

        System.out.println("ASCII hash1: " + h1a);
        System.out.println("ASCII hash2: " + h2a);
        System.out.println("Collision ASCII: " + (h1a == h2a ? "YES" : "NO"));

        System.out.println("FNV1a hash1: " + h1b);
        System.out.println("FNV1a hash2: " + h2b);
        System.out.println("Collision FNV1a: " + (h1b == h2b ? "YES" : "NO"));
    }
}