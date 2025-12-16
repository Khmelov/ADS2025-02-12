package by.bsuir.dsa.csv2025.gr410901.Мещеряков;

import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class Solution {

    static class Group {
        boolean isWildcard = false;
        boolean isSet = false;
        Set<Character> allowed = null;
        Character fixed = null;
    }

    public List<Integer> findMatches(String s, String p) {
        List<Group> groups = parsePattern(p);
        int n = s.length();
        int m = groups.size();
        List<Integer> ans = new ArrayList<>();
        if (m > n) return ans;

        int[] need = new int[26];
        boolean[] isAnagramPos = new boolean[m];
        for (int i = 0; i < m; i++) {
            Group g = groups.get(i);
            if (!g.isWildcard && !g.isSet && g.fixed != null) {
                need[g.fixed - 'a']++;
                isAnagramPos[i] = true;
            }
        }

        for (int left = 0; left + m <= n; left++) {
            boolean ok = true;
            int[] have = new int[26];

            for (int i = 0; i < m; i++) {
                char c = s.charAt(left + i);
                Group g = groups.get(i);

                if (g.isWildcard) continue;
                if (g.isSet) {
                    if (!g.allowed.contains(c)) {
                        ok = false;
                        break;
                    }
                } else if (g.fixed != null) {
                    have[c - 'a']++;
                }
            }

            if (!ok) continue;

            if (Arrays.equals(need, have)) {
                ans.add(left);
            }
        }

        return ans;
    }

    private List<Group> parsePattern(String p) {
        List<Group> res = new ArrayList<>();
        int i = 0;
        while (i < p.length()) {
            char c = p.charAt(i);
            if (c == '?') {
                Group g = new Group();
                g.isWildcard = true;
                res.add(g);
                i++;
            } else if (c == '{') {
                int j = i + 1;
                while (j < p.length() && p.charAt(j) != '}') j++;
                if (j >= p.length()) throw new IllegalArgumentException("Malformed pattern");
                String content = p.substring(i + 1, j);
                Group g = new Group();
                g.isSet = true;
                g.allowed = new HashSet<>();
                for (char x : content.toCharArray()) g.allowed.add(x);
                res.add(g);
                i = j + 1;
            } else {
                Group g = new Group();
                g.fixed = c;
                res.add(g);
                i++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) return;
        String s = sc.nextLine().trim();
        if (!sc.hasNextLine()) return;
        String p = sc.nextLine().trim();

        Solution solver = new Solution();
        List<Integer> res = solver.findMatches(s, p);

        for (int i = 0; i < res.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(res.get(i));
        }
        System.out.println();
    }

    public static class Tests {

        @Test
        public void test1() {
            var solver = new Solution();
            assertTrue(solver.findMatches("bacbadc", "a?{bc}b").isEmpty());
        }

        @Test
        public void test2() {
            var solver = new Solution();
            // Only index 3 matches under strict semantics
            assertEquals(List.of(3), solver.findMatches("abdcab", "abc"));
        }

        @Test
        public void test3() {
            var solver = new Solution();
            assertEquals(List.of(0,1,2,3,4), solver.findMatches("abcdef", "??"));
        }

        @Test
        public void test4() {
            var solver = new Solution();
            assertEquals(List.of(0,1,3), solver.findMatches("abcabc", "{ab}{bc}{ca}"));
        }

        @Test
        public void test5() {
            var solver = new Solution();
            assertTrue(solver.findMatches("aaaaa", "b?c").isEmpty());
        }

        @Test
        public void test6() {
            var solver = new Solution();
            assertEquals(List.of(0,1,2), solver.findMatches("abcde", "{abc}{bcd}{cde}"));
        }

        @Test
        public void test7() {
            var solver = new Solution();
            assertTrue(solver.findMatches("abc", "abcd").isEmpty());
        }

        @Test
        public void test8() {
            var solver = new Solution();
            assertEquals(List.of(0,6), solver.findMatches("cbaebabacd", "abc"));
        }

        @Test
        public void test9() {
            var solver = new Solution();
            assertTrue(solver.findMatches("bbaccab", "b?{ac}a").isEmpty());
        }

        @Test
        public void test10() {
            var solver = new Solution();
            assertEquals(List.of(0,1,2,3), solver.findMatches("aaaaaa", "a?a"));
        }
    }
}
