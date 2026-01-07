package by.bsuir.dsa.csv2025.gr410901.Волков;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;

@SuppressWarnings("all")
public class Solution {

    static class Task {
        int duration;
        int deadline;
        int penalty;
        int index;

        Task(int duration, int deadline, int penalty, int index) {
            this.duration = duration;
            this.deadline = deadline;
            this.penalty = penalty;
            this.index = index;
        }
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        Solution instance = new Solution();

        instance.scheduleTasks(scanner, System.out);
    }

    void scheduleTasks(Scanner scanner, PrintStream out) {

        int n = scanner.nextInt();
        

        Task[] tasks = new Task[n];
        

        for (int i = 0; i < n; i++) {
            int duration = scanner.nextInt();
            int deadline = scanner.nextInt();
            int penalty = scanner.nextInt();
            tasks[i] = new Task(duration, deadline, penalty, i + 1);
        }

        int maxMask = (1 << n) - 1;
        long[] dp = new long[maxMask + 1];
        int[] parent = new int[maxMask + 1];
        

        for (int mask = 1; mask <= maxMask; mask++) {
            dp[mask] = Long.MAX_VALUE;
            parent[mask] = -1;
        }

        int[] finishTime = new int[maxMask + 1];
        finishTime[0] = 0;

        for (int mask = 1; mask <= maxMask; mask++) {

            int totalTime = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    totalTime += tasks[i].duration;
                }
            }
            finishTime[mask] = totalTime;

            for (int i = 0; i < n; i++) {

                if ((mask & (1 << i)) == 0) {
                    continue;
                }

                int prevMask = mask ^ (1 << i);
                

                int completionTime = finishTime[prevMask] + tasks[i].duration;
                

                long penalty = 0;
                if (completionTime > tasks[i].deadline) {
                    penalty = (long) tasks[i].penalty * (completionTime - tasks[i].deadline);
                }

                if (dp[prevMask] != Long.MAX_VALUE) {
                    long newPenalty = dp[prevMask] + penalty;
                    if (newPenalty < dp[mask]) {
                        dp[mask] = newPenalty;
                        parent[mask] = i;
                    } else if (newPenalty == dp[mask]) {

                        if (parent[mask] == -1 || i > parent[mask]) {
                            parent[mask] = i;
                        }
                    }
                }
            }
        }

        List<Integer> order = new ArrayList<>();
        int currentMask = maxMask;
        

        while (currentMask != 0) {
            int lastTask = parent[currentMask];
            if (lastTask == -1) {

                int bestTask = -1;
                for (int i = 0; i < n; i++) {
                    if ((currentMask & (1 << i)) == 0) {
                        continue;
                    }
                    int prevMask = currentMask ^ (1 << i);
                    if (dp[prevMask] == Long.MAX_VALUE) {
                        continue;
                    }
                    int completionTime = finishTime[prevMask] + tasks[i].duration;
                    long penalty = 0;
                    if (completionTime > tasks[i].deadline) {
                        penalty = (long) tasks[i].penalty * (completionTime - tasks[i].deadline);
                    }
                    if (dp[prevMask] + penalty == dp[currentMask]) {
                        if (bestTask == -1 || tasks[i].index < tasks[bestTask].index) {
                            bestTask = i;
                        }
                    }
                }
                lastTask = bestTask;
            }
            if (lastTask != -1) {
                order.add(tasks[lastTask].index);
                currentMask = currentMask ^ (1 << lastTask);
            } else {
                break;
            }
        }
        

        List<Integer> result = new ArrayList<>();
        for (int i = order.size() - 1; i >= 0; i--) {
            result.add(order.get(i));
        }

        out.println(dp[maxMask]);
        for (int i = 0; i < result.size(); i++) {
            out.print(result.get(i));
            if (i < result.size() - 1) {
                out.print(" ");
            }
        }
        out.println();

    }

    private String readResource(String filename) {
        try {
            InputStream inputStream = Solution.class.getResourceAsStream(filename);
            if (inputStream == null) {
                return null;
            }
            Scanner scanner = new Scanner(inputStream);
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine()).append("\n");
            }
            scanner.close();
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void checkC_Basic() throws Exception {
        String input = readResource("in0.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            main(new String[0]);
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertEquals("C basic test failed: expected penalty 0", 0, penalty);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test
    public void checkC_Medium() throws Exception {
        String input = readResource("in1.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            main(new String[0]);
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertTrue("C medium test failed: penalty should be non-negative", penalty >= 0);
            assertTrue("C medium test failed: penalty seems too large", penalty < 1000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test
    public void checkC_Complex() throws Exception {
        String input = readResource("in4.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            main(new String[0]);
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertTrue("C complex test failed: penalty should be non-negative", penalty >= 0);
            assertTrue("C complex test failed: penalty seems too large", penalty < 2000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test
    public void checkC_SingleTask() throws Exception {
        String input = readResource("in2.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            main(new String[0]);
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertEquals("C single task test failed: expected penalty 0", 0, penalty);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test
    public void checkC_TwoTasks() throws Exception {
        String input = readResource("in3.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            main(new String[0]);
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertTrue("C two tasks test failed: penalty should be non-negative", penalty >= 0);
            assertTrue("C two tasks test failed: penalty should be reasonable", penalty < 2000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @Test(timeout = 5000)
    public void checkC_Performance() throws Exception {
        String input = readResource("in6.txt");
        assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(in);
            System.setOut(new PrintStream(out));
            
            long startTime = System.currentTimeMillis();
            main(new String[0]);
            long endTime = System.currentTimeMillis();
            
            String output = out.toString();
            String[] lines = output.trim().split("\\R");
            long penalty = Long.parseLong(lines[0]);
            
            assertTrue("C performance test failed: solution took too long", (endTime - startTime) < 1000);
            assertTrue("C performance test failed: result should be valid", penalty >= 0);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}

