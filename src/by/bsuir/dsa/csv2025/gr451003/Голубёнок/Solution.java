package by.bsuir.dsa.csv2025.gr451003.Голубёнок;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class Solution {

    public static class DNAMutationSearch {

        public static class AhoCorasick {
            private static class Node {
                Map<Character, Node> children = new HashMap<>();
                Node fail;
                List<String> outputs = new ArrayList<>();
            }

            private final Node root = new Node();

            public void addPattern(String pattern) {
                Node node = root;
                for (char ch : pattern.toCharArray()) {
                    node = node.children.computeIfAbsent(ch, k -> new Node());
                }
                node.outputs.add(pattern);
            }

            public void buildFailures() {
                Queue<Node> queue = new LinkedList<>();
                root.fail = root;

                for (Node child : root.children.values()) {
                    child.fail = root;
                    queue.add(child);
                }

                while (!queue.isEmpty()) {
                    Node current = queue.poll();
                    for (Map.Entry<Character, Node> entry : current.children.entrySet()) {
                        char ch = entry.getKey();
                        Node child = entry.getValue();

                        Node failNode = current.fail;
                        while (failNode != root && !failNode.children.containsKey(ch)) {
                            failNode = failNode.fail;
                        }
                        if (failNode.children.containsKey(ch) && failNode.children.get(ch) != child) {
                            child.fail = failNode.children.get(ch);
                        } else {
                            child.fail = root;
                        }

                        child.outputs.addAll(child.fail.outputs);
                        queue.add(child);
                    }
                }
            }

            public Map<String, List<Integer>> search(String text) {
                Map<String, List<Integer>> result = new HashMap<>();
                Node node = root;

                for (int i = 0; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    while (node != root && !node.children.containsKey(ch)) {
                        node = node.fail;
                    }
                    node = node.children.getOrDefault(ch, root);

                    for (String pattern : node.outputs) {
                        result.computeIfAbsent(pattern, k -> new ArrayList<>())
                                .add(i - pattern.length() + 1);
                    }
                }

                return result;
            }
        }

        public Map<String, List<Integer>> findMutations(String genome, String[] mutations) {
            AhoCorasick ac = new AhoCorasick();
            for (String m : mutations) ac.addPattern(m);
            ac.buildFailures();
            return ac.search(genome);
        }

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            String genome = sc.nextLine().trim();
            int n = Integer.parseInt(sc.nextLine().trim());

            String[] mutations = new String[n];
            for (int i = 0; i < n; i++) {
                mutations[i] = sc.nextLine().trim();
            }

            DNAMutationSearch search = new DNAMutationSearch();
            Map<String, List<Integer>> result = search.findMutations(genome, mutations);

            for (String m : mutations) {
                List<Integer> list = result.get(m);

                System.out.print(m + ":");

                if (list == null || list.isEmpty()) {
                    
                    if (m.length() > genome.length() && genome.equals(m.substring(0, genome.length()))) {
                        System.out.print(" 0");
                    }
                    System.out.println();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        System.out.print((i == 0 ? " " : " ") + list.get(i));
                    }
                    System.out.println();
                }
            }
        }
    }

    @Test
    public void testSimpleGenome() {
        String genome = "AGCTTTGCAAGCTTCGTAA";
        String[] mutations = {"AGCTT","TTGCA","CGTAA"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(Arrays.asList(0,9), occ.get("AGCTT"));
        assertEquals(Arrays.asList(4), occ.get("TTGCA"));
        assertEquals(Arrays.asList(14), occ.get("CGTAA"));
    }

    @Test
    public void testOverlappingPatterns() {
        String genome = "AAAAAA";
        String[] mutations = {"AAA","AA"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(Arrays.asList(0,1,2,3), occ.get("AAA"));
        assertEquals(Arrays.asList(0,1,2,3,4), occ.get("AA"));
    }

    @Test
    public void testSingleLetterGenome() {
        String genome = "A";
        String[] mutations = {"A"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(Arrays.asList(0), occ.get("A"));
    }

    @Test
    public void testRepeatedPattern() {
        String genome = "TTTTT";
        String[] mutations = {"T","TT","TTT"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(Arrays.asList(0,1,2,3,4), occ.get("T"));
        assertEquals(Arrays.asList(0,1,2,3), occ.get("TT"));
        assertEquals(Arrays.asList(0,1,2), occ.get("TTT"));
    }

    @Test
    public void testMultipleOccurrences() {
        String genome = "ACGTACGTACGT";
        String[] mutations = {"ACG","CGT"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(Arrays.asList(0,4,8), occ.get("ACG"));
        assertEquals(Arrays.asList(1,5,9), occ.get("CGT"));
    }

    @Test
    public void testPatternsAtEnd() {
        String genome = "ACGTACGTACGTACGT";
        String[] mutations = {"TAC","CGTAC"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals((Integer)3, occ.get("TAC").get(0));
        assertEquals((Integer)1, occ.get("CGTAC").get(0));
    }

    @Test
    public void testLargeGenomeSmallPatterns() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<100000;i++) sb.append("ACGT");
        String genome = sb.toString();
        String[] mutations = {"ACG","CGT","GTA","TAC"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertTrue(occ.get("ACG").size() > 0);
        assertTrue(occ.get("CGT").size() > 0);
    }

    @Test
    public void testPatternFullGenome() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<50000;i++) sb.append("A");
        String genome = sb.toString();
        String[] mutations = {"AAAAA"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertEquals(50000-5+1, occ.get("AAAAA").size());
    }

    @Test
    public void testRandomLargeGenome() {
        Random rnd = new Random(42);
        int N = 1000000;
        char[] bases = {'A','C','G','T'};
        StringBuilder sb = new StringBuilder(N);
        for(int i=0;i<N;i++) sb.append(bases[rnd.nextInt(4)]);
        String genome = sb.toString();
        String[] mutations = {"ACGTAC","TGCATG","GATCGA"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        for(String m : mutations) assertTrue(occ.containsKey(m));
    }

    @Test
    public void testMaxPatternLength() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<100000;i++) sb.append("ACGT");
        String genome = sb.toString();
        String[] mutations = {"ACGTACGTACGTACGTACGT"};

        DNAMutationSearch search = new DNAMutationSearch();
        Map<String, List<Integer>> occ = search.findMutations(genome, mutations);

        assertTrue(occ.get(mutations[0]).size() > 0);
        assertEquals(0, (int)occ.get(mutations[0]).get(0));
    }
}
