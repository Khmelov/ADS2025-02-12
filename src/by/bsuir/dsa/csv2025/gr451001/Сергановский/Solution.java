package by.bsuir.dsa.csv2025.gr451001.Сергановский;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Solution {

    public static void main(String[] args) {
        if (args != null && args.length > 0 && "test".equalsIgnoreCase(args[0])) {
            int failures = MainSelfTest.runAll();
            if (failures == 0) {
                System.out.println("OK: all tests passed");
                System.exit(0);
            } else {
                System.out.println("FAIL: failed tests = " + failures);
                System.exit(1);
            }
            return;
        }

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < m; i++) {
            int u = sc.nextInt() - 1;
            int v = sc.nextInt() - 1;
            graph.get(u).add(v);
        }

        List<Integer> dfsResult = topoSortDFS(n, graph);
        if (dfsResult != null) {
            for (int v : dfsResult) System.out.print((v + 1) + " ");
            System.out.println();
        }

        List<Integer> kahnResult = topoSortKahn(n, graph);
        if (kahnResult != null) {
            for (int v : kahnResult) System.out.print((v + 1) + " ");
            System.out.println();
        }
    }

    static List<Integer> topoSortDFS(int n, List<List<Integer>> graph) {
        boolean[] visited = new boolean[n];
        boolean[] onStack = new boolean[n];
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (!dfs(i, graph, visited, onStack, result)) {
                    System.out.println("Topological sort is impossible: graph contains a cycle.");
                    return null;
                }
            }
        }
        Collections.reverse(result);
        return result;
    }

    static boolean dfs(int node, List<List<Integer>> graph, boolean[] visited, boolean[] onStack, List<Integer> result) {
        visited[node] = true;
        onStack[node] = true;

        for (int nxt : graph.get(node)) {
            if (!visited[nxt]) {
                if (!dfs(nxt, graph, visited, onStack, result)) return false;
            } else if (onStack[nxt]) {
                return false;
            }
        }

        onStack[node] = false;
        result.add(node);
        return true;
    }

    static List<Integer> topoSortKahn(int n, List<List<Integer>> graph) {
        int[] indeg = new int[n];
        for (List<Integer> edges : graph)
            for (int v : edges) indeg[v]++;

        Queue<Integer> q = new LinkedList<>();
        for (int i = 0; i < n; i++)
            if (indeg[i] == 0) q.add(i);

        List<Integer> res = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            res.add(u);
            for (int v : graph.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) q.add(v);
            }
        }

        if (res.size() != n) {
            System.out.println("Topological sort is impossible: graph contains a cycle.");
            return null;
        }
        return res;
    }

    // -------------------- BUILT-IN TESTS --------------------------
    static class MainSelfTest {

        static int runAll() {
            int fails = 0;

            fails += run("""
                3 2
                1 2
                2 3
                """).include("1 2 3") ? 0 : 1;

            fails += run("""
                3 3
                1 2
                2 3
                3 1
                """).include("impossible") ? 0 : 1;

            fails += run("""
                3 0
                """).include("1") ? 0 : 1;

            fails += run("""
                4 1
                4 1
                """).include("4") ? 0 : 1;

            fails += run("""
                5 4
                1 3
                1 4
                3 5
                4 5
                """).include("1") ? 0 : 1;

            fails += run("""
                2 1
                1 2
                """).include("1 2") ? 0 : 1;

            fails += run("""
                2 1
                2 1
                """).include("2 1") ? 0 : 1;

            fails += run("""
                6 3
                1 4
                2 4
                3 5
                """).include("4") ? 0 : 1;

            fails += run("""
                1 0
                """).include("1") ? 0 : 1;

            fails += run("""
                4 2
                1 2
                3 4
                """).include("2") ? 0 : 1;

            return fails;
        }

        static Asserter run(String input) {
            return new Asserter(input);
        }

        static class Asserter {
            private final String input;
            private String stdout = "";
            private String stderr = "";
            private int exitCode = -1;
            private boolean executed = false;

            Asserter(String input) {
                this.input = input;
            }

            boolean include(String expected) {
                ensureExecuted();
                boolean ok = stdout.replaceAll("\\s+", " ").contains(expected);

                if (!ok) {
                    System.out.println("\nTEST FAILED");
                    System.out.println("Expected to find: " + expected);
                    System.out.println("stdout:\n" + stdout);
                }
                return ok;
            }

            private void ensureExecuted() {
                if (executed) return;

                List<String> cmd = new ArrayList<>();
                cmd.add(resolveJavaBin());
                cmd.add("-Dfile.encoding=UTF-8");
                cmd.add("-cp");
                cmd.add(resolveClassPath());
                cmd.add(Solution.class.getName());

                ProcessBuilder pb = new ProcessBuilder(cmd);

                try {
                    Process proc = pb.start();

                    try (OutputStream os = proc.getOutputStream()) {
                        os.write(input.getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    }

                    stdout = readFully(proc.getInputStream());
                    stderr = readFully(proc.getErrorStream());

                    proc.waitFor(5, TimeUnit.SECONDS);

                    exitCode = proc.exitValue();
                    executed = true;

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private static String readFully(InputStream is) throws IOException {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }

            private static String resolveJavaBin() {
                String home = System.getProperty("java.home");
                Path p = Path.of(home, "bin", isWindows() ? "java.exe" : "java");
                if (Files.exists(p)) return p.toString();
                return "java";
            }

            private static String resolveClassPath() {
                return System.getProperty("java.class.path");
            }

            private static boolean isWindows() {
                return System.getProperty("os.name").toLowerCase().contains("win");
            }
        }
    }
}
