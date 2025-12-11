package by.bsuir.dsa.csv2025.gr451003.Аврусевич;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import static org.junit.Assert.*;

public class Solution {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    static class Order {
        int id;
        int arrivalTime;
        int priority;
        int processingTime;

        Order(int id, int arrivalTime, int priority, int processingTime) {
            this.id = id;
            this.arrivalTime = arrivalTime;
            this.priority = priority;
            this.processingTime = processingTime;
        }
    }

    static class Result {
        final List<Integer> processingOrder;
        final int totalProcessingTime;
        final double averageWaitingTime;

        Result(List<Integer> processingOrder, int totalProcessingTime, double averageWaitingTime) {
            this.processingOrder = processingOrder;
            this.totalProcessingTime = totalProcessingTime;
            this.averageWaitingTime = averageWaitingTime;
        }
    }

    public static Result optimizeWarehouse(int[][] ordersArray) {
        int n = ordersArray.length;
        if (n == 0) {
            return new Result(new ArrayList<>(), 0, 0.0);
        }

        Order[] orders = new Order[n];
        int totalProcessingTime = 0;

        for (int i = 0; i < n; i++) {
            orders[i] = new Order(i, ordersArray[i][0], ordersArray[i][1], ordersArray[i][2]);
            totalProcessingTime += ordersArray[i][2];
        }

        Arrays.sort(orders, Comparator.comparingInt(a -> a.arrivalTime));

        PriorityQueue<Order> pq = new PriorityQueue<>((a, b) -> {
            if (a.priority != b.priority)
                return a.priority - b.priority;

            return a.arrivalTime - b.arrivalTime;
        });

        int currentTime = 0;
        int totalWaitingTime = 0;
        int index = 0;
        List<Integer> processingOrder = new ArrayList<>(n);

        while (index < n || !pq.isEmpty()) {
           while (index < n && orders[index].arrivalTime <= currentTime) {
                pq.offer(orders[index]);
                index++;
            }

            if (pq.isEmpty()) {
                currentTime = orders[index].arrivalTime;
                continue;
            }

            Order currentOrder = pq.poll();
            processingOrder.add(currentOrder.id);

            int waitingTime = currentTime - currentOrder.arrivalTime;
            if (waitingTime < 0) {
                waitingTime = 0;
            }
            totalWaitingTime += waitingTime;

            currentTime = Math.max(currentTime, currentOrder.arrivalTime) + currentOrder.processingTime;
        }

        double averageWaitingTime = n > 0 ? (double) totalWaitingTime / n : 0.0;

        return new Result(processingOrder, totalProcessingTime, averageWaitingTime);
    }

    private static void processInputAndOutput(Scanner scanner) {
        int n = scanner.nextInt();
        int[][] ordersArray = new int[n][3];
        for (int i = 0; i < n; i++) {
            ordersArray[i][0] = scanner.nextInt();
            ordersArray[i][1] = scanner.nextInt();
            ordersArray[i][2] = scanner.nextInt();
        }

        Result result = optimizeWarehouse(ordersArray);

        String output = result.processingOrder.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(" ")) +
                " " + result.totalProcessingTime +
                " " + String.format("%.2f", result.averageWaitingTime);

        System.out.print(output);
        scanner.close();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        processInputAndOutput(scanner);
    }

    public static void runProgram(String input) {
        Scanner scanner = new Scanner(input);
        processInputAndOutput(scanner);
    }

    private void assertResult(String expected, int[][] orders) {
        assertEquals(expected, outContent.toString().trim());
        assertTrue(isValidResult(orders, outContent.toString().trim()));
    }

    @Test
    public void test1() {
        String input = "4\n0 2 5\n2 1 3\n4 3 2\n6 1 4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 3 2 14 3.25", new int[][]{{0,2,5}, {2,1,3}, {4,3,2}, {6,1,4}});
    }

    @Test
    public void test2() {
        String input = "3\n0 1 10\n0 2 5\n0 3 3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 2 18 8.33", new int[][]{{0,1,10}, {0,2,5}, {0,3,3}});
    }

    @Test
    public void test3() {
        String input = "1\n5 2 8\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 8 0.00", new int[][]{{5,2,8}});
    }

    @Test
    public void test4() {
        String input = "3\n0 2 6\n1 1 2\n2 3 4\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 2 12 3.67", new int[][]{{0,2,6}, {1,1,2}, {2,3,4}});
    }

    @Test
    public void test5() {
        String input = "6\n0 2 5\n2 1 3\n4 3 2\n6 1 4\n8 2 2\n10 1 1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 3 5 4 2 17 3.83", new int[][]{{0,2,5}, {2,1,3}, {4,3,2}, {6,1,4}, {8,2,2}, {10,1,1}});
    }

    @Test
    public void test6() {
        String input = "5\n0 3 4\n2 1 2\n3 2 3\n5 1 1\n7 3 2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 3 2 4 12 2.00", new int[][]{{0,3,4}, {2,1,2}, {3,2,3}, {5,1,1}, {7,3,2}});
    }

    @Test
    public void test7() {
        String input = "5\n0 1 3\n0 2 5\n0 1 2\n0 3 4\n0 2 1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 2 1 4 3 15 5.80", new int[][]{{0,1,3}, {0,2,5}, {0,1,2}, {0,3,4}, {0,2,1}});
    }

    @Test
    public void test8() {
        String input = "6\n0 2 3\n1 1 2\n2 3 1\n3 1 4\n5 2 2\n6 3 3\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 3 4 2 5 15 3.83", new int[][]{{0,2,3}, {1,1,2}, {2,3,1}, {3,1,4}, {5,2,2}, {6,3,3}});
    }

    @Test
    public void test9() {
        String input = "4\n0 2 2\n5 1 3\n10 3 1\n15 1 2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 2 3 8 0.00", new int[][]{{0,2,2}, {5,1,3}, {10,3,1}, {15,1,2}});
    }

    @Test
    public void test10() {
        String input = "6\n0 3 4\n1 1 2\n2 1 1\n3 2 3\n4 1 2\n5 3 1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        runProgram(input);
        assertResult("0 1 2 4 3 5 13 3.83", new int[][]{{0,3,4}, {1,1,2}, {2,1,1}, {3,2,3}, {4,1,2}, {5,3,1}});
    }

    private static boolean isValidResult(int[][] orders, String output) {
        String[] parts = output.split(" ");
        if (parts.length < 2) return false;

        int orderCount = orders.length;
        if (orderCount == 0) {
            return parts.length == 2 && "0".equals(parts[0]) && "0.00".equals(parts[1]);
        }

        int idCount = parts.length - 2;
        return orderCount == idCount;
    }
}