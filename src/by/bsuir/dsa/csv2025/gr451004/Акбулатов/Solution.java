package by.bsuir.dsa.csv2025.gr451004.Акбулатов;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.assertEquals;


public class Solution {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = br.readLine();

        int result = Solution2.maxSumBST(input);

        System.out.print(result);
    }

    @Test
    public void test1() {
        assertEquals(20, Solution2.maxSumBST("1 4 3 2 4 2 5 null null null null null null 4 6"));
    }

    @Test
    public void test2() {
        assertEquals(2, Solution2.maxSumBST("4 3 null 1 2"));
    }

    @Test
    public void test3() {
        assertEquals(0, Solution2.maxSumBST("-4 -2 -5"));
    }

    @Test
    public void test4() {
        assertEquals(6, Solution2.maxSumBST("2 1 3"));
    }

    @Test
    public void test5() {
        assertEquals(7, Solution2.maxSumBST("5 4 8 3 null 6 3"));
    }

    @Test
    public void test6() {
        assertEquals(14, Solution2.maxSumBST("10 5 15 1 8 null 7"));
    }

    @Test
    public void test7() {
        assertEquals(25, Solution2.maxSumBST("1 null 10 -5 20"));
    }

    @Test
    public void test8() {
        assertEquals(9, Solution2.maxSumBST("8 9 8"));
    }

    @Test
    public void test9() {
        assertEquals(14, Solution2.maxSumBST("4 8 null 6 1 9 null -5 4 null null null -3 null 10"));
    }

    @Test
    public void test10() {
        assertEquals(0, Solution2.maxSumBST("0 null -1"));
    }

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    static class Solution2 {
        static int maxSum;
        static Map<TreeNode, NodeInfo> map = new HashMap<>();

        public static int maxSumBST(String data) {
            maxSum = 0;
            map.clear();

            maxSumBSTRecursive(buildTree(data));

            return Math.max(maxSum, 0);
        }

        private static TreeNode buildTree(String data) {
            if (data == null) return null;
            data = data.trim();
            if (data.isEmpty()) return null;

            String[] tokens = data.split("\\s+");
            if (tokens.length == 0 || tokens[0].equals("null")) return null;

            TreeNode root = new TreeNode(Integer.parseInt(tokens[0]));
            Queue<TreeNode> q = new LinkedList<>();
            q.add(root);

            int i = 1;
            while (!q.isEmpty() && i < tokens.length) {
                TreeNode curr = q.poll();

                if (!tokens[i].equals("null")) {
                    curr.left = new TreeNode(Integer.parseInt(tokens[i]));
                    q.add(curr.left);
                }
                i++;

                if (i < tokens.length) {
                    if (!tokens[i].equals("null")) {
                        curr.right = new TreeNode(Integer.parseInt(tokens[i]));
                        q.add(curr.right);
                    }
                    i++;
                }
            }

            return root;
        }

        private static void maxSumBSTRecursive(TreeNode root) {
            if (root == null) return;

            maxSumBSTRecursive(root.left);
            maxSumBSTRecursive(root.right);

            NodeInfo info = new NodeInfo(root.val, true, root.val, root.val);

            if (root.left != null) {
                NodeInfo leftInfo = map.get(root.left);
                if (leftInfo == null || !leftInfo.inBST) {
                    info.inBST = false;
                    map.put(root, info);
                    return;
                }

                if (leftInfo.max >= root.val) {
                    info.inBST = false;
                    map.put(root, info);
                    return;
                }

                info.sum += leftInfo.sum;
                info.min = leftInfo.min;
            }

            if (root.right != null) {
                NodeInfo rightInfo = map.get(root.right);
                if (rightInfo == null || !rightInfo.inBST) {
                    info.inBST = false;
                    map.put(root, info);
                    return;
                }

                if (rightInfo.min <= root.val) {
                    info.inBST = false;
                    map.put(root, info);
                    return;
                }

                info.sum += rightInfo.sum;
                info.max = rightInfo.max;
            }

            if (info.sum > maxSum) maxSum = info.sum;

            map.put(root, info);
        }

        private static class NodeInfo {
            int sum;
            boolean inBST;
            int min;
            int max;

            NodeInfo(int sum, boolean inBST, int min, int max) {
                this.sum = sum;
                this.inBST = inBST;
                this.min = min;
                this.max = max;
            }
        }
    }
}