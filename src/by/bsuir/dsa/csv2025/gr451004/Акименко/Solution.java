package by.bsuir.dsa.csv2025.gr451004.Акименко;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class Solution {

    // Основной метод приложения
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Слияние двух BST в сбалансированное дерево ===");

        // Ввод первого дерева
        System.out.println("Введите элементы первого BST через пробел:");
        int[] tree1 = readIntArray(scanner);

        // Ввод второго дерева
        System.out.println("Введите элементы второго BST через пробел:");
        int[] tree2 = readIntArray(scanner);

        // Построение деревьев
        TreeNode root1 = BSTMerge.buildBST(tree1);
        TreeNode root2 = BSTMerge.buildBST(tree2);

        // Слияние деревьев
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);

        // Вывод результата
        List<Integer> inOrderResult = BSTMerge.getInOrder(result);
        System.out.println("Результат слияния (in-order обход): " + inOrderResult);

        scanner.close();
    }

    // Метод для чтения массива чисел из консоли
    private static int[] readIntArray(Scanner scanner) {
        try {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return new int[0];
            }

            String[] parts = input.split("\\s+");
            int[] result = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                result[i] = Integer.parseInt(parts[i]);
            }
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите только целые числа, разделенные пробелами");
            return new int[0];
        }
    }

    @Test
    public void test1() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{2, 1, 3});
        TreeNode root2 = BSTMerge.buildBST(new int[]{5, 4});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test2() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{1, 2, 3});
        TreeNode root2 = BSTMerge.buildBST(new int[]{2, 3, 4});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 2, 2, 3, 3, 4);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test3() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{});
        TreeNode root2 = BSTMerge.buildBST(new int[]{5, 3, 7});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(3, 5, 7);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test4() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{10});
        TreeNode root2 = BSTMerge.buildBST(new int[]{20});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(10, 20);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test5() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{1, 3, 5, 7, 9});
        TreeNode root2 = BSTMerge.buildBST(new int[]{2, 4, 6, 8, 10});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test6() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{-5, -3, -1});
        TreeNode root2 = BSTMerge.buildBST(new int[]{-4, -2, 0});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(-5, -4, -3, -2, -1, 0);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test7() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{1, 1, 2, 2});
        TreeNode root2 = BSTMerge.buildBST(new int[]{2, 3, 3});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 1, 2, 2, 2, 3, 3);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test8() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{4, 2, 6, 1, 3});
        TreeNode root2 = BSTMerge.buildBST(new int[]{9, 7, 10});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 6, 7, 9, 10);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test9() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{1, 2, 3, 4, 5});
        TreeNode root2 = BSTMerge.buildBST(new int[]{10, 9, 8, 7});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 7, 8, 9, 10);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    @Test
    public void test10() {
        TreeNode root1 = BSTMerge.buildBST(new int[]{15, 8, 20, 5, 12, 18, 25});
        TreeNode root2 = BSTMerge.buildBST(new int[]{10, 6, 14, 4, 7});
        TreeNode result = BSTMerge.mergeTreesToBalanced(root1, root2);
        List<Integer> expected = Arrays.asList(4, 5, 6, 7, 8, 10, 12, 14, 15, 18, 20, 25);
        assertEquals(expected, BSTMerge.getInOrder(result));
    }

    // Внутренний класс для узла дерева
    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode() {
        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // Внутренний класс с логикой слияния BST
    public static class BSTMerge {

        public static TreeNode mergeTreesToBalanced(TreeNode root1, TreeNode root2) {
            List<Integer> values = new ArrayList<>();
            collectValues(root1, values);
            collectValues(root2, values);
            Collections.sort(values);
            return buildBalancedBST(values, 0, values.size() - 1);
        }

        private static void collectValues(TreeNode root, List<Integer> values) {
            if (root != null) {
                collectValues(root.left, values);
                values.add(root.val);
                collectValues(root.right, values);
            }
        }

        private static TreeNode buildBalancedBST(List<Integer> values, int start, int end) {
            if (start > end) return null;

            int mid = start + (end - start) / 2;
            TreeNode node = new TreeNode(values.get(mid));

            node.left = buildBalancedBST(values, start, mid - 1);
            node.right = buildBalancedBST(values, mid + 1, end);

            return node;
        }

        public static List<Integer> getInOrder(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            inOrderTraversal(root, result);
            return result;
        }

        private static void inOrderTraversal(TreeNode root, List<Integer> result) {
            if (root != null) {
                inOrderTraversal(root.left, result);
                result.add(root.val);
                inOrderTraversal(root.right, result);
            }
        }

        public static TreeNode buildBST(int[] elements) {
            if (elements.length == 0) {
                return null;
            }

            TreeNode root = null;
            for (int element : elements) {
                root = insertIntoBST(root, element);
            }
            return root;
        }

        private static TreeNode insertIntoBST(TreeNode root, int val) {
            if (root == null) {
                return new TreeNode(val);
            }

            if (val < root.val) {
                root.left = insertIntoBST(root.left, val);
            } else {
                root.right = insertIntoBST(root.right, val);
            }

            return root;
        }
    }

}