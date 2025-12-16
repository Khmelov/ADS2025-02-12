package by.bsuir.dsa.csv2025.gr410902.Ковальчук;



import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class Solution {
    private static class Node {
        int key;
        int priority;
        int count;
        long sum;
        int size;
        Node left, right;

        Node(int key, int priority) {
            this.key = key;
            this.priority = priority;
            this.count = 1;
            this.sum = (long) key;
            this.size = 1;
        }
    }

    private Node root;
    private int nextPriority = 0;

    private void update(Node node) {
        if (node != null) {
            node.size = getSize(node.left) + getSize(node.right) + node.count;
            node.sum = getSum(node.left) + getSum(node.right) + ((long) node.key) * node.count;
        }
    }

    private int getSize(Node node) {
        return node == null ? 0 : node.size;
    }

    private long getSum(Node node) {
        return node == null ? 0L : node.sum;
    }

    private Node[] split(Node node, long key) {
        if (node == null) return new Node[]{null, null};

        if ((long)node.key <= key) {
            Node[] result = split(node.right, key);
            node.right = result[0];
            update(node);
            return new Node[]{node, result[1]};
        } else {
            Node[] result = split(node.left, key);
            node.left = result[1];
            update(node);
            return new Node[]{result[0], node};
        }
    }

    public void insert(int key) {
        Node[] parts = split(root, key);
        Node[] parts2 = split(parts[0], key - 1);

        if (parts2[1] != null && parts2[1].key == key) {
            parts2[1].count++;
            update(parts2[1]);
            root = merge(merge(parts2[0], parts2[1]), parts[1]);
        } else {
            int priority = generatePriority(key);
            Node newNode = new Node(key, priority);
            root = merge(merge(parts2[0], newNode), parts[1]);
        }
    }

    private int generatePriority(int key) {
        int priority = (key * 31 + nextPriority++) & Integer.MAX_VALUE;
        return priority;
    }

    public void erase(int key) {
        Node[] parts = split(root, key);
        Node[] parts2 = split(parts[0], key - 1);

        if (parts2[1] != null && parts2[1].key == key) {
            if (parts2[1].count > 1) {
                parts2[1].count--;
                update(parts2[1]);
                root = merge(merge(parts2[0], parts2[1]), parts[1]);
            } else {
                root = merge(parts2[0], parts[1]);
            }
        } else {
            root = merge(merge(parts2[0], parts2[1]), parts[1]);
        }
    }

    public int count(int key) {
        return count(root, key);
    }

    private int count(Node node, int key) {
        if (node == null) return 0;
        if (node.key == key) return node.count;
        if (key < node.key) return count(node.left, key);
        return count(node.right, key);
    }

    public long sum(int L, int R) {
        Node[] parts1 = split(root, (long)R);
        Node[] parts2 = split(parts1[0], (long)L - 1);
        long result = getSum(parts2[1]);
        root = merge(merge(parts2[0], parts2[1]), parts1[1]);
        return result;
    }

    public String kth(int k) {
        if (k <= 0 || k > getSize(root)) {
            return "none";
        }

        Node current = root;
        while (current != null) {
            int leftSize = getSize(current.left);

            if (k <= leftSize) {
                current = current.left;
            } else if (k <= leftSize + current.count) {
                return String.valueOf(current.key);
            } else {
                k -= leftSize + current.count;
                current = current.right;
            }
        }
        return "none";
    }

    public String next(int x) {
        Node[] parts = split(root, x);
        if (parts[1] == null) {
            root = merge(parts[0], parts[1]);
            return "none";
        }

        Node current = parts[1];
        while (current.left != null) {
            current = current.left;
        }
        int result = current.key;
        root = merge(parts[0], parts[1]);
        return String.valueOf(result);
    }

    public String prev(int x) {
        Node[] parts = split(root, x - 1);
        if (parts[0] == null) {
            root = merge(parts[0], parts[1]);
            return "none";
        }

        Node current = parts[0];
        while (current.right != null) {
            current = current.right;
        }
        int result = current.key;
        root = merge(parts[0], parts[1]);
        return String.valueOf(result);
    }

    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;

        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            update(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            update(right);
            return right;
        }
    }
    public static String executeOperations(String input) {
        Solution tree = new Solution();
        Scanner scanner = new Scanner(input);
        List<String> results = new ArrayList<>();

        int n = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            int command = scanner.nextInt();

            switch (command) {
                case 1:
                    int x1 = scanner.nextInt();
                    tree.insert(x1);
                    break;

                case 2:
                    int x2 = scanner.nextInt();
                    tree.erase(x2);
                    break;

                case 3:
                    int x3 = scanner.nextInt();
                    results.add(String.valueOf(tree.count(x3)));
                    break;

                case 4:
                    int L = scanner.nextInt();
                    int R = scanner.nextInt();
                    long sumResult = tree.sum(L, R);
                    results.add(String.valueOf(sumResult));
                    break;

                case 5:
                    int k = scanner.nextInt();
                    results.add(tree.kth(k));
                    break;

                case 6:
                    int x4 = scanner.nextInt();
                    results.add(tree.next(x4));
                    break;

                case 7:
                    int x5 = scanner.nextInt();
                    results.add(tree.prev(x5));
                    break;
            }
        }
        scanner.close();
        return String.join(" ", results);
    }
    public static void main(String[] args) {
        Solution tree = new Solution();
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        List<String> results = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int command = scanner.nextInt();

            switch (command) {
                case 1:
                    int x1 = scanner.nextInt();
                    tree.insert(x1);
                    break;

                case 2:
                    int x2 = scanner.nextInt();
                    tree.erase(x2);
                    break;

                case 3:
                    int x3 = scanner.nextInt();
                    results.add(String.valueOf(tree.count(x3)));
                    break;

                case 4:
                    int L = scanner.nextInt();
                    int R = scanner.nextInt();
                    long sumResult = tree.sum(L, R);
                    results.add(String.valueOf(sumResult));
                    break;

                case 5:
                    int k = scanner.nextInt();
                    results.add(tree.kth(k));
                    break;

                case 6:
                    int x4 = scanner.nextInt();
                    results.add(tree.next(x4));
                    break;

                case 7:
                    int x5 = scanner.nextInt();
                    results.add(tree.prev(x5));
                    break;
            }
        }
        System.out.println(String.join(" ", results));
        scanner.close();
    }
    @Test
    public void runAllTests() {
        System.out.println(" Запуск всех тестов для Cartesian Tree");
        System.out.println("========================================");

        TestCase[] testCases = {
                new TestCase("test1_basicInsertCount",
                        """
                        7
                        1 5
                        1 3
                        1 5
                        3 5
                        3 3
                        3 1
                        3 6
                        """, "2 1 0 0"),

                new TestCase("test2_sumAndErase",
                        """
                        10
                        1 10
                        1 5
                        1 5
                        1 -2
                        4 -10 10
                        2 5
                        3 5
                        4 0 10
                        2 5
                        3 5
                        """, "18 1 15 0"),

                new TestCase("test3_kth_basic_and_out_of_range",
                        """
                        8
                        1 2
                        1 2
                        1 3
                        1 1
                        5 1
                        5 3
                        5 4
                        5 5
                        """, "1 2 3 none"),

                new TestCase("test4_next_prev_edgeCases",
                        """
                        9
                        6 0
                        7 0
                        1 5
                        1 10
                        6 5
                        7 5
                        7 11
                        6 10
                        6 4
                        """, "none none 10 none 10 none 5"),

                new TestCase("test5_combination_many_ops",
                        """
                        12
                        1 1
                        1 3
                        1 5
                        1 7
                        4 2 6
                        2 3
                        4 2 6
                        1 5
                        3 5
                        5 2
                        2 1
                        5 1
                        """, "8 5 2 5 5"),

                new TestCase("test6_largeKeysAndNegative",
                        """
                        8
                        1 2147483647
                        1 -2147483648
                        4 -2147483648 2147483647
                        3 2147483647
                        3 -2147483648
                        6 -2147483648
                        7 2147483647
                        5 2
                        """, "-1 1 1 2147483647 -2147483648 2147483647"),

                new TestCase("test7_sum_empty_and_no_elements_in_range",
                        """
                        6
                        4 0 10
                        1 1
                        1 2
                        4 3 4
                        2 1
                        4 0 2
                        """, "0 0 2"),

                new TestCase("test8_repeated_erase_until_empty",
                        """
                        9
                        1 4
                        1 4
                        1 4
                        3 4
                        2 4
                        3 4
                        2 4
                        2 4
                        3 4
                        """, "3 2 0"),

                new TestCase("test9_kth_after_mixed_operations",
                        """
                        11
                        1 5
                        1 1
                        1 9
                        1 3
                        2 1
                        1 2
                        5 1
                        5 2
                        5 3
                        5 4
                        5 5
                        """, "2 3 5 9 none"),

                new TestCase("test10_next_prev_with_duplicates",
                        """
                        10
                        1 5
                        1 5
                        1 5
                        1 7
                        6 5
                        7 5
                        2 5
                        2 5
                        3 5
                        6 5
                        """, "7 none 1 7")
        };

        int passed = 0;
        int failed = 0;

        for (TestCase testCase : testCases) {
            System.out.printf("\n Тест: %s%n", testCase.name);
            System.out.printf("   Входные данные: %s%n", testCase.input.replace("\n", " ").trim());
            System.out.printf("   Ожидаемый результат: %s%n", testCase.expected);

            try {
                String result = executeOperations(testCase.input);
                System.out.printf("   Полученный результат: %s%n", result);

                if (testCase.expected.equals(result)) {
                    System.out.println("    ПРОЙДЕН");
                    passed++;
                } else {
                    System.out.println("    ПРОВАЛЕН");
                    System.out.printf("      Ожидалось: %s%n", testCase.expected);
                    System.out.printf("      Получено:  %s%n", result);
                    failed++;
                }
            } catch (Exception e) {
                System.out.printf("    ОШИБКА: %s%n", e.getMessage());
                failed++;
            }
        }

        System.out.println("\n========================================");
        System.out.printf(" ИТОГ:%n");
        System.out.printf("   Всего тестов: %d%n", testCases.length);
        System.out.printf("    Пройдено: %d%n", passed);
        System.out.printf("    Провалено: %d%n", failed);

        if (failed == 0) {
            System.out.println(" Все тесты успешно пройдены!");
        } else {
            fail("Некоторые тесты провалились");
        }
    }
    private static class TestCase {
        String name;
        String input;
        String expected;

        TestCase(String name, String input, String expected) {
            this.name = name;
            this.input = input;
            this.expected = expected;
        }
    }
}