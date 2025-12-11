package by.bsuir.dsa.csv2025.gr451004.Райчук;

class Solution{
    public static int[] buildLPS(String p) {
        int[] lps = new int[p.length()];
        int len = 0;
        int i = 1;
        while (i < p.length()) {
            if (p.charAt(i) == p.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i++] = 0;
                }
            }
        }
        return lps;
    }

    public static int countOccurrences(String s, String p) {
        if (p.length() == 0) return 0;
        int[] lps = buildLPS(p);
        int i = 0, j = 0;
        int count = 0;
        while (i < s.length()) {
            if (s.charAt(i) == p.charAt(j)) {
                i++;
                j++;
                if (j == p.length()) {
                    count++;
                    j = 0;
                }
            } else {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return count;
    }
}

