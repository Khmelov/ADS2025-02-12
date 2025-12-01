package by.bsuir.dsa.csv2025.gr451004.Матырко;

import java.util.*;

public class Solution {

    static class Node {
        int value;
        Node left;
        Node right;
        Node(int v) { value = v; }
    }

    static class BST {
        Node root;

        void add(int x) {
            root = insert(root, x);
        }

        private Node insert(Node node, int x) {
            if (node == null) return new Node(x);
            if (x < node.value) node.left = insert(node.left, x);
            else node.right = insert(node.right, x);
            return node;
        }

        boolean find(int x) {
            return search(root, x);
        }

        private boolean search(Node node, int x) {
            if (node == null) return false;
            if (x == node.value) return true;
            if (x < node.value) return search(node.left, x);
            return search(node.right, x);
        }

        void del(int x) {
            root = delete(root, x);
        }

        private Node delete(Node node, int x) {
            if (node == null) return null;

            if (x < node.value) node.left = delete(node.left, x);
            else if (x > node.value) node.right = delete(node.right, x);
            else {
                if (node.left == null && node.right == null) return null;
                if (node.left == null) return node.right;
                if (node.right == null) return node.left;

                Node min = findMinNode(node.right);
                node.value = min.value;
                node.right = delete(node.right, min.value);
            }
            return node;
        }

        private Node findMinNode(Node node) {
            while (node.left != null) node = node.left;
            return node;
        }

        int min() {
            if (root == null) throw new NoSuchElementException();
            Node cur = root;
            while (cur.left != null) cur = cur.left;
            return cur.value;
        }

        int max() {
            if (root == null) throw new NoSuchElementException();
            Node cur = root;
            while (cur.right != null) cur = cur.right;
            return cur.value;
        }

        int sum() {
            return sumRec(root);
        }

        private int sumRec(Node node) {
            if (node == null) return 0;
            return node.value + sumRec(node.left) + sumRec(node.right);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();
        BST tree = new BST();

        String[] tokens = line.split(" ");
        int i = 0;

        while (i < tokens.length) {
            String cmd = tokens[i];

            if (cmd.equals("add")) {
                int x = Integer.parseInt(tokens[i + 1]);
                tree.add(x);
                i += 2;
            } 
            else if (cmd.equals("del")) {
                int x = Integer.parseInt(tokens[i + 1]);
                tree.del(x);
                i += 2;
            }
            else if (cmd.equals("find")) {
                int x = Integer.parseInt(tokens[i + 1]);
                System.out.println(tree.find(x));
                i += 2;
            }
            else if (cmd.equals("min")) {
                try {
                    System.out.println(tree.min());
                } catch (Exception e) {
                    System.out.println("null");
                }
                i++;
            }
            else if (cmd.equals("max")) {
                try {
                    System.out.println(tree.max());
                } catch (Exception e) {
                    System.out.println("null");
                }
                i++;
            }
            else if (cmd.equals("sum")) {
                System.out.println(tree.sum());
                i++;
            } 
            else {
                i++; 
            }
        }
    }
}