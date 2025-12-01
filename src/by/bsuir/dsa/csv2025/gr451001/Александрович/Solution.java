package by.bsuir.dsa.csv2025.gr451001.Александрович;

import java.util.Scanner;

public class Solution {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    public static void main(String [] args)
    {
        Scanner scanner = new Scanner(System.in);
        length = scanner.nextInt();
        input = new String[length];
        for (int i = 0; i < length; i++){
            input[i] = scanner.next();
        }
        System.out.println(Solution(generateTree()));
    }

    static String[] input;
    static int length, i, j;
    static TreeNode prev;
    static TreeNode generateTree()
    {
        i = 0;
        j = 1;
        prev = null;
        if (length == 0) return null;
        TreeNode root = new TreeNode(Integer.parseInt(input[i++]));
        while (i < length)
        {
            recursive(root, 1);
            j++;
        }
        return root;
    }
    static void recursive(TreeNode node, int depth){
        if (i >= length) return;
        if (depth == j)
        {
            String nextVal = input[i];
            if (nextVal.equals("null")) node.left = null;
            else node.left = new TreeNode(Integer.parseInt(nextVal));
            if (++i >= length) return;
            nextVal = input[i];
            if (nextVal.equals("null")) node.right = null;
            else node.right = new TreeNode(Integer.parseInt(nextVal));
            i++;
        }
        else
        {
            if (node.left != null) recursive(node.left, depth+1);
            if (i >= length) return;
            if (node.right != null) recursive(node.right, depth+1);
        }
    }
    static boolean Solution(TreeNode root) {
        if (root == null) return true;
        if (root.left != null && !Solution(root.left)) return false;
        if (prev == null || prev.val < root.val) prev = root;
        else return false;
        return root.right == null || Solution(root.right);
    }
}
