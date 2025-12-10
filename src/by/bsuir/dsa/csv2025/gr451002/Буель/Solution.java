package by.bsuir.dsa.csv2025.gr451002.Буель;

import org.junit.Test;

import java.util.Scanner;

import static org.junit.Assert.*;

public class Solution<E extends Comparable<E>> {

    private static class Node<E> {
        E data;
        Node<E> left;
        Node<E> right;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> root;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution bst = new Solution();

        String[] input = scanner.nextLine().split(" ");

        for (String str : input) {
            bst.add(Integer.valueOf(str));
        }
        int index = scanner.nextInt();
        System.out.println(bst.search(index));
    }

    // ========================= Add =============================
    public boolean add(E value) {
        if (root == null) {
            root = new Node<>(value);
            return true;
        }

        Node<E> currentNode = root;
        boolean inputted = false;
        while (!inputted) {
            while (currentNode.data.compareTo(value) > 0) {
                if (currentNode.left == null) {
                    currentNode.left = new Node<>(value);
                    inputted = true;
                }
                currentNode = currentNode.left;
            }

            while (currentNode.data.compareTo(value) < 0) {
                if (currentNode.right == null) {
                    currentNode.right = new Node<>(value);
                    inputted = true;
                }
                currentNode = currentNode.right;
            }

            if (!inputted && currentNode.data.equals(value)) {
                inputted = true;
                return false;
            }
        }
        return true;
    }

    // ==================== K-th smallest =========================
    private int index;
    private E findKth(Node<E> node, int k) {
        if (node == null) {
            return null;
        }

        E left = findKth(node.left, k);
        if (left != null) {
            return left;
        }

        if (index == k) {
            return node.data;
        }
        index++;

        return findKth(node.right, k);
    }

    public E search(int k) {
        index = 1;
        return findKth(root, k);
    }

    @Test
    public void checkUserTreeA() {
        var userBST = new Solution();
        assertTrue(userBST.add(2));
        assertTrue(userBST.add(1));
        assertTrue(userBST.add(3));
        assertFalse(userBST.add(1));
        assertTrue(userBST.add(5));
        assertEquals(3, userBST.search(3));
    }

    @Test
    public void checkUserTreeB() {
        var userBST = new Solution();
        assertTrue(userBST.add(2));
        assertEquals(2, userBST.search(1));
        assertTrue(userBST.add(1));
        assertFalse(userBST.add(1));
        assertEquals(2, userBST.search(2));
        assertTrue(userBST.add(10));
        assertFalse(userBST.add(10));
        assertEquals(10, userBST.search(3));
    }

    @Test
    public void checkUserTreeC() {
        var userBST = new Solution();
        assertTrue(userBST.add(7));
        assertTrue(userBST.add(4));
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(2));
        assertTrue(userBST.add(6));
        assertTrue(userBST.add(9));
        assertTrue(userBST.add(14));
        assertTrue(userBST.add(5));
        assertEquals(11, userBST.search(7));
        assertEquals(14, userBST.search(8));
        assertEquals(2, userBST.search(1));
    }

    @Test
    public void checkUserTreeD() {
        var userBST = new Solution();
        assertTrue(userBST.add(7));
        assertTrue(userBST.add(4));
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(14));
        assertTrue(userBST.add(5));
        assertEquals(11, userBST.search(4));
        assertEquals(14, userBST.search(5));
        assertEquals(4, userBST.search(1));
    }

    @Test
    public void checkUserTreeE() {
        var userBST = new Solution();
        assertTrue(userBST.add(7));
        assertTrue(userBST.add(4));
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(9));
        assertTrue(userBST.add(5));
        assertEquals(4, userBST.search(1));
        assertEquals(11, userBST.search(5));
        assertEquals(9, userBST.search(4));
    }

    @Test
    public void checkUserTreeF() {
        var userBST = new Solution();
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(2));
        assertTrue(userBST.add(6));
        assertTrue(userBST.add(14));
        assertTrue(userBST.add(5));
        assertEquals(11, userBST.search(4));
        assertEquals(14, userBST.search(5));
        assertEquals(2, userBST.search(1));
    }

    @Test
    public void checkUserTreeG() {
        var userBST = new Solution();
        assertTrue(userBST.add(7));
        assertTrue(userBST.add(4));
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(9));
        assertTrue(userBST.add(14));
        assertTrue(userBST.add(5));
        assertEquals(11, userBST.search(5));
        assertEquals(14, userBST.search(6));
        assertEquals(4, userBST.search(1));
    }

    @Test
    public void checkUserTreeH() {
        var userBST = new Solution();
        assertTrue(userBST.add(1000));
        assertEquals(1000, userBST.search(1));
        assertTrue(userBST.add(1));
        assertFalse(userBST.add(1));
        assertEquals(1, userBST.search(1));
        assertTrue(userBST.add(10));
        assertFalse(userBST.add(10));
        assertEquals(1000, userBST.search(3));
    }

    @Test
    public void checkUserTreeI() {
        var userBST = new Solution();
        assertTrue(userBST.add(7));
        assertTrue(userBST.add(4));
        assertTrue(userBST.add(11));
        assertTrue(userBST.add(2));
        assertTrue(userBST.add(6));
        assertTrue(userBST.add(9));
        assertTrue(userBST.add(14));
        assertTrue(userBST.add(5));
        assertEquals(11, userBST.search(7));
        assertEquals(14, userBST.search(8));
        assertEquals(2, userBST.search(1));
    }

    @Test
    public void checkUserTreeJ() {
        var userBST = new Solution();
        assertTrue(userBST.add(-1));
        assertEquals(-1, userBST.search(1));
        assertTrue(userBST.add(12));
        assertTrue(userBST.add(1));
        assertEquals(1, userBST.search(2));
        assertFalse(userBST.add(1));
        assertEquals(12, userBST.search(3));
    }
}