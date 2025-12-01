package by.bsuir.dsa.csv2025.gr451001.Супрунович;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        Solution bstSearch = new Solution();
        Scanner scanner = new Scanner(System.in);

        String[] treeValues = scanner.nextLine().split(" ");
        int target = scanner.nextInt();
        int tolerance = scanner.nextInt();
        scanner.close();

        TreeNode root = bstSearch.buildBST(treeValues);

        Integer result = bstSearch.findInTolerance(root, target, tolerance);

        if (result != null) {
            System.out.println(result);
        } else {
            System.out.println("NO");
        }
    }

    @Test
    public void testCase1() {
        TreeNode root = buildBST(new String[]{"5", "3", "7", "2", "4", "6", "8"});
        Integer result = findInTolerance(root, 3, 1);
        assertEquals(Integer.valueOf(3), result);
    }

    @Test
    public void testCase2() {
        TreeNode root = buildBST(new String[]{"5", "3", "7", "2", "4", "6", "8"});
        Integer result = findInTolerance(root, 9, 0);
        assertNull(result);
    }

    @Test
    public void testCase3() {
        TreeNode root = buildBST(new String[]{"10"});
        Integer result = findInTolerance(root, 10, 0);
        assertEquals(Integer.valueOf(10), result);
    }

    @Test
    public void testCase4() {
        TreeNode root = buildBST(new String[]{"8", "4", "12", "2", "6", "10", "14"});
        Integer result = findInTolerance(root, 7, 2);
        assertEquals(Integer.valueOf(6), result);
    }

    @Test
    public void testCase5() {
        TreeNode root = buildBST(new String[]{"1", "2", "3", "4", "5"});
        Integer result = findInTolerance(root, 0, 1);
        assertEquals(Integer.valueOf(1), result);
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    private TreeNode buildBST(String[] values) {
        if (values.length == 0 || values[0].isEmpty()) {
            return null;
        }

        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        for (int i = 1; i < values.length; i++) {
            if (!values[i].isEmpty()) {
                insertIntoBST(root, Integer.parseInt(values[i]));
            }
        }
        return root;
    }

    private void insertIntoBST(TreeNode root, int val) {
        TreeNode current = root;
        while (true) {
            if (val <= current.val) {
                if (current.left == null) {
                    current.left = new TreeNode(val);
                    return;
                } else {
                    current = current.left;
                }
            } else {
                if (current.right == null) {
                    current.right = new TreeNode(val);
                    return;
                } else {
                    current = current.right;
                }
            }
        }
    }

    private Integer findInTolerance(TreeNode root, int target, int tolerance) {
        if (root == null) {
            return null;
        }

        TreeNode current = root;
        Integer closest = null;
        int minDiff = Integer.MAX_VALUE;

        while (current != null) {
            int diff = Math.abs(current.val - target);

            if (diff <= tolerance && (diff < minDiff || (diff == minDiff && current.val < closest))) {
                minDiff = diff;
                closest = current.val;
            }

            if (target < current.val) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return (minDiff <= tolerance) ? closest : null;
    }
}