package by.bsuir.dsa.csv2025.gr410901.Данилова;

import java.util.*;

public class Solution {

    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        Solution instance = new Solution();
        int[][] result = instance.analyzeSales(consoleScanner);
        for (int[] counts : result) {
            for (int count : counts) {
                System.out.print(count + " ");
            }
            System.out.println();
        }
    }

    int[][] analyzeSales(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Order[] orders = new Order[n];
        int[] queries = new int[m];
        int[][] result = new int[m][];

        for (int i = 0; i < n; i++) {
            orders[i] = new Order(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            queries[i] = scanner.nextInt();
        }

        Arrays.sort(orders);

        Map<Integer, int[]> bounds = getCategoryBounds(orders);

        for (int i = 0; i < m; i++) {
            result[i] = processQuery(orders, bounds, queries[i]);
        }

        return result;
    }

    private Map<Integer, int[]> getCategoryBounds(Order[] orders) {
        Map<Integer, int[]> bounds = new TreeMap<>();
        if (orders.length == 0) return bounds;

        int start = 0;
        int currentCat = orders[0].category;

        for (int i = 1; i < orders.length; i++) {
            if (orders[i].category != currentCat) {
                bounds.put(currentCat, new int[]{start, i - 1});
                start = i;
                currentCat = orders[i].category;
            }
        }
        bounds.put(currentCat, new int[]{start, orders.length - 1});

        return bounds;
    }

    private int[] processQuery(Order[] orders, Map<Integer, int[]> bounds, int amount) {
        int[] counts = new int[bounds.size()];
        int idx = 0;

        for (int[] range : bounds.values()) {
            counts[idx++] = countInRange(orders, range[0], range[1], amount);
        }

        return counts;
    }

    private int countInRange(Order[] orders, int start, int end, int amount) {
        int firstIndex = findFirstAbove(orders, start, end, amount);
        if (firstIndex == -1) {
            return 0;
        }
        return end - firstIndex + 1;
    }

    private int findFirstAbove(Order[] orders, int left, int right, int amount) {
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (orders[mid].amount > amount) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    private class Order implements Comparable<Order> {
        int id, amount, category;

        Order(int id, int amount, int category) {
            this.id = id;
            this.amount = amount;
            this.category = category;
        }

        @Override
        public int compareTo(Order o) {
            if (this.category != o.category) {
                return Integer.compare(this.category, o.category);
            }
            return Integer.compare(this.amount, o.amount);
        }
    }
}

