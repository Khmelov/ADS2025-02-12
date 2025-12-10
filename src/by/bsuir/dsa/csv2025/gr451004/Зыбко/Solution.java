package by.bsuir.dsa.csv2025.gr451004.Зыбко;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Solution {

    private static class Vertex {
        Vertex[] to = new Vertex[26];
        Vertex[] go = new Vertex[26];
        Vertex link;
        Vertex parent;
        int parentChar;
        boolean isTerminal;
        String word;

        Vertex(int pch, Vertex p) {
            parentChar = pch;
            parent = p;
            link = null;
        }
    }

    private Vertex root;

    public Solution() {
        root = new Vertex(-1, null);
    }

    public void addForbiddenWord(String word) {
        Vertex v = root;
        for (char c : word.toCharArray()) {
            int idx = c - 'a';
            if (v.to[idx] == null) {
                v.to[idx] = new Vertex(idx, v);
            }
            v = v.to[idx];
        }
        v.isTerminal = true;
        v.word = word;
    }

    private Vertex getLink(Vertex v) {
        if (v.link == null) {
            if (v == root || v.parent == root) {
                v.link = root;
            } else {
                v.link = go(getLink(v.parent), v.parentChar);
            }
        }
        return v.link;
    }

    private Vertex go(Vertex v, int c) {
        if (v.go[c] == null) {
            if (v.to[c] != null) {
                v.go[c] = v.to[c];
            } else if (v == root) {
                v.go[c] = root;
            } else {
                v.go[c] = go(getLink(v), c);
            }
        }
        return v.go[c];
    }

    public List<Match> findForbiddenWords(String text) {
        List<Match> matches = new ArrayList<>();
        Vertex current = root;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'a' || c > 'z') {
                continue;
            }

            int idx = c - 'a';
            current = go(current, idx);

            Vertex check = current;
            while (check != root) {
                if (check.isTerminal) {
                    matches.add(new Match(i - check.word.length() + 1, i, check.word));
                }
                check = getLink(check);
            }
        }

        return matches;
    }

    public String censorText(String text) {
        char[] chars = text.toCharArray();
        List<Match> matches = findForbiddenWords(text);

        for (Match match : matches) {
            for (int i = match.start; i <= match.end; i++) {
                chars[i] = '-';
            }
        }

        return new String(chars);
    }

    public static class Match {
        public final int start;
        public final int end;
        public final String word;

        public Match(int start, int end, String word) {
            this.start = start;
            this.end = end;
            this.word = word;
        }
    }

    public void buildAutomatonBFS() {
        Queue<Vertex> queue = new LinkedList<>();

        root.link = root;
        for (int i = 0; i < 26; i++) {
            if (root.to[i] != null) {
                root.to[i].link = root;
                queue.add(root.to[i]);
            } else {
                root.go[i] = root;
            }
        }

        while (!queue.isEmpty()) {
            Vertex v = queue.poll();

            for (int i = 0; i < 26; i++) {
                if (v.to[i] != null) {
                    Vertex child = v.to[i];

                    child.link = go(v.link, i);

                    queue.add(child);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution censor = new Solution();

        // Предопределенные запрещенные слова
        String[] forbiddenWords = {"protest", "rally", "opposition", "freedom",
                "democracy", "revolution", "strike",
                "kill", "police", "fight"};

        for (String word : forbiddenWords) {
            censor.addForbiddenWord(word);
        }

        censor.buildAutomatonBFS();

        String text = scanner.nextLine();

        String censoredText = censor.censorText(text);
        System.out.println(censoredText);

        scanner.close();
    }

    @Test
    public void testSingleForbiddenWord() {
        Solution censor = new Solution();
        censor.addForbiddenWord("protest");
        censor.buildAutomatonBFS();

        String text = "people started a protest";
        String censored = censor.censorText(text);
        assertEquals("people started a -------", censored);
    }

    @Test
    public void testMultipleForbiddenWords() {
        Solution censor = new Solution();
        censor.addForbiddenWord("protest");
        censor.addForbiddenWord("rally");
        censor.addForbiddenWord("freedom");
        censor.buildAutomatonBFS();

        String text = "protest and rally for freedom";
        String censored = censor.censorText(text);
        assertEquals("------- and ----- for -------", censored);
    }

    @Test
    public void testNoForbiddenWords() {
        Solution censor = new Solution();
        censor.addForbiddenWord("protest");
        censor.buildAutomatonBFS();

        String text = "hello world this is normal text";
        String censored = censor.censorText(text);
        assertEquals("hello world this is normal text", censored);
    }

    @Test
    public void testCaseSensitivity() {
        Solution censor = new Solution();
        censor.addForbiddenWord("protest");
        censor.buildAutomatonBFS();

        String text = "PROTEST Rally oppOsition";
        String censored = censor.censorText(text);
        assertEquals("PROTEST Rally oppOsition", censored);
    }

    @Test
    public void testEmptyText() {
        Solution censor = new Solution();
        censor.addForbiddenWord("protest");
        censor.buildAutomatonBFS();

        String text = "";
        String censored = censor.censorText(text);
        assertEquals("", censored);

        List<Match> matches = censor.findForbiddenWords(text);
        assertTrue("Should find no matches in empty text", matches.isEmpty());
    }
}