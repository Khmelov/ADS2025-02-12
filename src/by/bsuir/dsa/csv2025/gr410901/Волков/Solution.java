package by.bsuir.dsa.csv2025.gr410901.Волков;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
}

class TaskTest {
    

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
        
    @org.junit.Test
    public void checkC_Basic() throws Exception {

        String input = readResource("dataC.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        

        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            

            Solution.main(new String[0]);
            

            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertEquals("C basic test failed: expected penalty 0", 0, penalty);
        } finally {

            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @org.junit.Test
    public void checkC_Medium() throws Exception {

        String input = readResource("dataC2.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            Solution.main(new String[0]);
            
            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertTrue("C medium test failed: penalty should be non-negative", penalty >= 0);
            org.junit.Assert.assertTrue("C medium test failed: penalty seems too large", penalty < 1000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @org.junit.Test
    public void checkC_Complex() throws Exception {

        String input = readResource("dataC3.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            Solution.main(new String[0]);
            
            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertTrue("C complex test failed: penalty should be non-negative", penalty >= 0);
            org.junit.Assert.assertTrue("C complex test failed: penalty seems too large", penalty < 2000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @org.junit.Test
    public void checkC_SingleTask() throws Exception {

        String input = readResource("dataC4.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            Solution.main(new String[0]);
            
            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertEquals("C single task test failed: expected penalty 0", 0, penalty);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @org.junit.Test
    public void checkC_TwoTasks() throws Exception {

        String input = readResource("dataC5.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            Solution.main(new String[0]);
            
            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertTrue("C two tasks test failed: penalty should be non-negative", penalty >= 0);
            org.junit.Assert.assertTrue("C two tasks test failed: penalty should be reasonable", penalty < 2000);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }

    @org.junit.Test(timeout = 5000)
    public void checkC_Performance() throws Exception {

        String input = readResource("dataC3.txt");
        org.junit.Assert.assertNotNull("Test data file not found", input);
        
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        
        try {
            System.setIn(inputStream);
            System.setOut(new PrintStream(outputStream));
            
            long startTime = System.currentTimeMillis();
            Solution.main(new String[0]);
            long endTime = System.currentTimeMillis();
            
            String output = outputStream.toString();
            String[] lines = output.trim().split("\n");
            long penalty = Long.parseLong(lines[0]);
            

            org.junit.Assert.assertTrue("C performance test failed: solution took too long", (endTime - startTime) < 1000);
            org.junit.Assert.assertTrue("C performance test failed: result should be valid", penalty >= 0);
        } finally {
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
    }
}

