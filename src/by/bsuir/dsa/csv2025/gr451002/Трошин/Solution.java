package by.bsuir.dsa.csv2025.gr451002.Трошин;

public class Solution {
    public static void main(String[] args) {
        String str = "SAPABAP";
        int numSubStrs = countSolution(str);

        System.out.println("В строке \"" + str + "\" всего " + numSubStrs + " палиндромных подстрок ");
    }

    public static int countSolution(String str) {
        int strLen = str.length();
        if (strLen == 0) return 0;

        boolean[][] arrValidSubStrs = new boolean[strLen][strLen];

        // Подстроки длиной 1
        for (int i = 0; i < strLen; i++) {
            arrValidSubStrs[i][i] = true;
        }

        // Подстроки длиной 2
        for (int i = 0; i < strLen - 1; i++) {
            arrValidSubStrs[i][i + 1] = (str.charAt(i) == str.charAt(i + 1));
        }

        // Подстроки длиной от 3 до n
        for (int curSubStrLen = 3; curSubStrLen <= strLen; curSubStrLen++) {
            for (int leftChar = 0; leftChar <= strLen - curSubStrLen; leftChar++) {
                int rightChar = leftChar + curSubStrLen - 1;
                arrValidSubStrs[leftChar][rightChar] =
                        (str.charAt(leftChar) == str.charAt(rightChar)) && arrValidSubStrs[leftChar + 1][rightChar - 1];
            }
        }

        // Подсчет всех палиндромных подстрок
        int numValidSubStrs = 0;
        for (int l = 0; l < strLen; l++) {
            for (int r = l; r < strLen; r++) {
                if (arrValidSubStrs[l][r]) {
                    numValidSubStrs++;
                }
            }
        }

        return numValidSubStrs;
    }

}
