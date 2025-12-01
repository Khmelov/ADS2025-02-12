package by.bsuir.dsa.csv2025.gr410902.Сулимов;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static int findMaxClique(boolean[][] graph) {
        if (graph.length == 0) {
            return 0;
        }
        return new MaxCliqueFinder().find(graph);
    }

    private static class MaxCliqueFinder {
        private int maxCliqueSize = 0;

        public int find(boolean[][] graph) {
            maxCliqueSize = 0;
            int n = graph.length;

            for (int i = 0; i < n; i++) {
                Set<Integer> currentClique = new HashSet<>();
                currentClique.add(i);
                Set<Integer> candidates = new HashSet<>();

                for (int j = i + 1; j < n; j++) {
                    if (isConnectedToAll(graph, currentClique, j)) {
                        candidates.add(j);
                    }
                }

                dfs(currentClique, candidates, graph);
            }
            return maxCliqueSize;
        }

        private void dfs(Set<Integer> currentClique, Set<Integer> candidates, boolean[][] graph) {
            if (currentClique.size() > maxCliqueSize) {
                maxCliqueSize = currentClique.size();
            }

            for (int candidate : new ArrayList<>(candidates)) {
                if (isConnectedToAll(graph, currentClique, candidate)) {
                    currentClique.add(candidate);
                    Set<Integer> newCandidates = new HashSet<>();

                    for (int nextCandidate : candidates) {
                        if (nextCandidate > candidate && isConnectedToAll(graph, currentClique, nextCandidate)) {
                            newCandidates.add(nextCandidate);
                        }
                    }

                    dfs(currentClique, newCandidates, graph);
                    currentClique.remove(candidate);
                }
            }
        }

        private boolean isConnectedToAll(boolean[][] graph, Set<Integer> clique, int vertex) {
            for (int v : clique) {
                if (!graph[v][vertex]) {
                    return false;
                }
            }
            return true;
        }
    }
    private static boolean[][] createGraph(String input) {
        String[] parts = input.split("\\s+");
        int n = Integer.parseInt(parts[0]);
        boolean[][] graph = new boolean[n][n];
        int index = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = Integer.parseInt(parts[index++]);
                graph[i][j] = (value == 1);
            }
        }
        return graph;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim();

        if (input.isEmpty()) {
            System.out.println("0");
            return;
        }

        String[] parts = input.split("\\s+");
        int n = Integer.parseInt(parts[0]);

        if (n == 0) {
            System.out.println("0");
            return;
        }

        boolean[][] graph = new boolean[n][n];
        int index = 1;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = Integer.parseInt(parts[index++]);
                graph[i][j] = (value == 1);
            }
        }

        int maxCliqueSize = findMaxClique(graph);
        System.out.println(maxCliqueSize);

        scanner.close();
    }


    @Test
    public void testAllGraphCases() {
        Object[][] testCases = {
                {"0", 0},
                {"1 0", 1},
                {"2 0 0 0 0", 1},
                {"2 0 1 1 0", 2},
                {"3 0 1 1 1 0 1 1 1 0", 3},
                {"4 0 1 1 1 1 0 0 0 1 0 0 0 1 0 0 0", 2},
                {"4 0 1 1 1 1 0 1 1 1 1 0 1 1 1 1 0", 4},
                {"4 0 1 1 0 1 0 1 1 1 1 0 0 0 1 0 0", 3},
                {"3 0 0 0 0 0 0 0 0 0", 1},
                {"3 0 1 0 1 0 1 0 1 0", 2},
                {"5 0 1 1 1 1 1 0 1 1 1 1 1 0 1 1 1 1 1 0 1 1 1 1 1 0", 5}
        };

        for (Object[] testCase : testCases) {
            String input = (String) testCase[0];
            int expected = (Integer) testCase[1];

            boolean[][] graph = createGraph(input);
            int actual = findMaxClique(graph);

            assertEquals( "failed : (input: " + input + ")", expected, actual);
        }
    }
}
