package by.bsuir.dsa.csv2025.gr410902.Дятко;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class Solution {
    static class Node {
        Node[] children = new Node[26];
        boolean isWord;
    }

    static class MyPrefixTree {
        private final Node root = new Node();

        public void insert(String word) {
            Node node = root;
            for (char c : word.toCharArray()) {
                int idx = c - 'a';
                if (node.children[idx] == null) {
                    node.children[idx] = new Node();
                }
                node = node.children[idx];
            }
            node.isWord = true;
        }

        public Node getRoot() {
            return root;
        }
    }

    public static int minWords(String s, List<String> dict) {
        MyPrefixTree trie = new MyPrefixTree();
        for (String word : dict) {
            trie.insert(word);
        }

        int n = s.length();
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            if (dp[i] == Integer.MAX_VALUE) continue;
            Node node = trie.getRoot();
            for (int j = i; j < n; j++) {
                int idx = s.charAt(j) - 'a';
                if (node.children[idx] == null) break;
                node = node.children[idx];
                if (node.isWord) {
                    dp[j + 1] = Math.min(dp[j + 1], dp[i] + 1);
                }
            }
        }

        return dp[n] == Integer.MAX_VALUE ? -1 : dp[n];
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = Integer.parseInt(sc.nextLine().trim());
        String[] words = sc.nextLine().trim().split("\\s+");
        List<String> dict = Arrays.asList(words);
        String s = sc.nextLine().trim();
        System.out.println(minWords(s, dict));
    }

    @Test(timeout = 300)
    public void testCases() throws Exception {
        assertEquals(2, minWords("aaab", Arrays.asList("a", "aa", "aaa", "b")));
        assertEquals(2, minWords("leetcode", Arrays.asList("leet", "code")));
        assertEquals(-1, minWords("xyz", Arrays.asList("x", "y")));
        assertEquals(2, minWords("applepie", Arrays.asList("apple", "pie", "app", "le")));
        assertEquals(3, minWords("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog")));
        assertEquals(2, minWords("banana", Arrays.asList("ban", "ana", "ba", "na")));
        assertEquals(3, minWords("pineapplepenapple", Arrays.asList("apple", "pen", "applepen", "pine", "pineapple")));
        assertEquals(2, minWords("hello", Arrays.asList("he", "hell", "o", "lo")));
        assertEquals(4, minWords("alphabetaepsilonkappa", Arrays.asList("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "eta", "theta", "iota", "kappa")));
        assertEquals(3, minWords("abcdefghij", Arrays.asList("ab", "abc", "abcd", "bc", "cd", "d", "cde", "efg", "fg", "gh", "hij")));
        assertEquals(2, minWords("internationalization", Arrays.asList("inter", "nation", "al", "ization", "international", "ization")));
        assertEquals(-1, minWords("abcdef", Arrays.asList("gh", "ij", "kl")));
        assertEquals(1, minWords("microservice", Arrays.asList("micro", "service", "microservice")));
        assertEquals(3, minWords("aaaaaaa", Arrays.asList("a", "aa", "aaa")));
        assertEquals(1, minWords("foobar", Arrays.asList("foo", "bar", "foobar")));
        assertEquals(4, minWords("onetwothreefourfive", Arrays.asList("one", "two", "three", "four", "five", "on", "etwo", "threefour")));
        assertEquals(0, minWords("", Arrays.asList("a", "b", "c")));
        assertEquals(1, minWords("zeta", Arrays.asList("alpha", "beta", "gamma", "delta", "epsilon", "zeta", "theta")));

    }
}
