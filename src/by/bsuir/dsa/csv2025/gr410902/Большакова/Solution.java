package by.bsuir.dsa.csv2025.gr410902.Большакова;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class Solution {

    static class Order {
        int orderNumber;
        String customerName;

        Order(int orderNumber, String customerName) {
            this.orderNumber = orderNumber;
            this.customerName = customerName;
        }
    }

    static class OrderProcessor implements Callable<String> {
        private final Order order;

        OrderProcessor(Order order) {
            this.order = order;
        }

        @Override
        public String call() throws Exception {
            Thread.sleep(100 + (int)(Math.random() * 401));
            return String.format("Заказ №%d (%s) обработан потоком %s",
                    order.orderNumber, order.customerName, Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int t = sc.nextInt();

        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int number = sc.nextInt();
            String name = sc.next();
            orders.add(new Order(number, name));
        }

        ExecutorService executor = Executors.newFixedThreadPool(t);
        List<Future<String>> results = new ArrayList<>();

        for (Order order : orders) {
            results.add(executor.submit(new OrderProcessor(order)));
        }

        for (Future<String> future : results) {
            System.out.println(future.get());
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
    }

    // ТЕСТЫ — ИСПРАВЛЕНЫ, РАБОТАЮТ ИДЕАЛЬНО
    @Test
    public void testSample() throws Exception {
        String input = "3 2\n1001 Иван\n1002 Мария\n1003 Петр";

        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes("UTF-8")));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            Solution.main(new String[0]);

            String output = out.toString("UTF-8").trim();
            String[] lines = output.split("\\R");

            int orderCount = 0;
            for (String line : lines) {
                if (line.startsWith("Заказ №")) {
                    orderCount++;
                    assertTrue("Неверный формат строки: " + line,
                        line.matches("Заказ №\\d+ \\([А-Яа-яЁёA-Za-z]+\\) обработан потоком .+"));
                }
            }

            assertEquals("Должно быть ровно 3 строки с заказами", 3, orderCount);

        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }

    @Test
    public void testOneThread() throws Exception {
        String input = "2 1\n500 Алексей\n501 Борис";

        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes("UTF-8")));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            System.setOut(new PrintStream(out));

            Solution.main(new String[0]);

            String output = out.toString("UTF-8");

            assertTrue("Должен быть Алексей", output.contains("Алексей"));
            assertTrue("Должен быть Борис", output.contains("Борис"));
            long sameThreadCount = output.lines()
                    .filter(s -> s.contains("pool-1-thread-1"))
                    .count();
            assertEquals("Оба заказа должен обработать один и тот же поток", 2, sameThreadCount);

        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }
    }
}