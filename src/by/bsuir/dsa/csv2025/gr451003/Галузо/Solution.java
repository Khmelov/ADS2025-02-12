package by.bsuir.dsa.csv2025.gr451003.Галузо;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    //он был в другом файле извините :) можно не минус балл ;)

    private static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = Solution.class.getResourceAsStream("dataA.txt");
        Solution instance = new Solution();
        String[] result = instance.searchInBST(stream);
        for (String res : result) {
            System.out.println(res);
        }
    }

    String[] searchInBST(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();

        TreeNode root = null;
        for (int i = 0; i < n; i++) {
            int value = scanner.nextInt();
            root = insert(root, value);
        }

        int k = scanner.nextInt();
        String[] result = new String[k];

        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            if (search(root, value)) {
                result[i] = "found";
            } else {
                result[i] = "not found";
            }
        }

        scanner.close();
        return result;
    }

    private TreeNode insert(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }

        if (val < root.val) {
            root.left = insert(root.left, val);
        } else if (val > root.val) {
            root.right = insert(root.right, val);
        }

        return root;
    }

    private boolean search(TreeNode root, int val) {
        if (root == null) {
            return false;
        }

        if (root.val == val) {
            return true;
        } else if (val < root.val) {
            return search(root.left, val);
        } else {
            return search(root.right, val);
        }
    }

    @Test
    public void checkA() throws Exception {
        testCase(1, new String[]{"found", "not found", "found", "found", "not found"});
        testCase(2, new String[]{"not found", "not found", "not found"});
        testCase(3, new String[]{"found", "not found", "not found"});
        testCase(4, new String[]{"found", "found", "found", "not found"});
        testCase(5, new String[]{"found", "found", "not found", "not found"});
        testCase(6, new String[]{"found", "found", "found", "not found", "found", "found"});
        testCase(7, new String[]{"found", "found", "not found", "found"});
        testCase(8, new String[]{"found", "found", "not found", "not found"});
        testCase(9, new String[]{"found", "found", "found", "found", "found", "found"});
        testCase(10, new String[]{"found", "found", "not found", "not found"});
    }

    private void testCase(int testNumber, String[] expected) throws Exception {
        String testData = getTestData(testNumber);
        InputStream inputStream = new java.io.ByteArrayInputStream(testData.getBytes());

        Solution instance = new Solution();
        String[] result = instance.searchInBST(inputStream);

        assertArrayEquals("Test " + testNumber + " failed", expected, result);
    }

    private String getTestData(int testNumber) {
        switch(testNumber) {
            case 1: return "7 5 3 7 2 4 6 8\n5 4 9 5 2 10";
            case 2: return "0\n3 1 2 3";
            case 3: return "1 10\n3 10 5 15";
            case 4: return "5 10 5 3 2 1\n4 10 5 1 15";
            case 5: return "5 5 6 7 8 9\n4 5 9 4 10";
            case 6: return "7 4 2 6 1 3 5 7\n6 1 4 7 8 3 5";
            case 7: return "5 5 4 3 2 1\n4 5 1 6 3";
            case 8: return "5 1 2 3 4 5\n4 1 5 0 6";
            case 9: return "5 8 3 10 1 6\n6 3 3 10 10 1 1";
            case 10: return "5 1000000000 500000000 1500000000 250000000 750000000\n4 500000000 1000000000 2000000000 0";
            default: return "";
        }
    }
}