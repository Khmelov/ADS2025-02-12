package by.bsuir.dsa.csv2025.gr451001.Руссу;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.*;

public class Solution {
    static class Node {
        int val;
        Node left, right;
        Node(int v) { val = v; }
    }

    static int idx;

    public static Node parseTree(String s) {
        idx = 0;
        return parse(s);
    }

    private static Node parse(String s) {
        // читаем число
        int sign = 1;
        if (s.charAt(idx) == '-') {
            sign = -1;
            idx++;
        }
        int num = 0;
        while (idx < s.length() && Character.isDigit(s.charAt(idx))) {
            num = num * 10 + (s.charAt(idx) - '0');
            idx++;
        }
        Node root = new Node(sign * num);

        // если дальше [, значит есть поддеревья
        if (idx < s.length() && s.charAt(idx) == '[') {
            idx++; // '['
            // левое поддерево
            if (idx < s.length() && s.charAt(idx) != ',' && s.charAt(idx) != ']') {
                root.left = parse(s);
            }
            // если есть запятая, значит есть правое поддерево
            if (idx < s.length() && s.charAt(idx) == ',') {
                idx++;
                if (idx < s.length() && s.charAt(idx) != ']') {
                    root.right = parse(s);
                }
            }
            if (idx < s.length() && s.charAt(idx) == ']') {
                idx++; // ']'
            }
        }
        return root;
    }

    public static int countPaths(Node root, int target) {
        return dfs(root, target, 0);
    }

    private static int dfs(Node node, int target, int sum) {
        if (node == null) return 0;
        sum += node.val;
        if (node.left == null && node.right == null) {
            return sum == target ? 1 : 0;
        }
        return dfs(node.left, target, sum) + dfs(node.right, target, sum);
    }


    public class SolutionTest {

        private int run(String inputK, String tree) {
            int K = Integer.parseInt(inputK.trim());
            Node root = Solution.parseTree(tree.trim());
            return Solution.countPaths(root, K);
        }

        @Test
        public void test1() { assertEquals(2, run("6", "3[2[1],3]")); }
        @Test public void test2() { assertEquals(1, run("3", "3")); }
        @Test public void test3() { assertEquals(0, run("4", "2[1[0],3[4,5]]")); }
        @Test public void test4() { assertEquals(2, run("21", "11[6[3[1],4],8[5,7]]")); }
        @Test public void test5() { assertEquals(1, run("20", "9[5[2[4],3],11[6,9]]")); }
        @Test public void test6() { assertEquals(2, run("15", "7[4[2[2],3[1]],5[6,4[4]],8[7,0]]")); }
        @Test public void test7() { assertEquals(2, run("20", "10[5[2[3],5],7[6,4[3]]]")); }
        @Test public void test8() { assertEquals(5, run("0", "0[0[0,0],0[0[0,0],0]]")); }
        @Test public void test9() { assertEquals(2, run("5", "0[3[2],2[3]]")); }
        @Test public void test10(){ assertEquals(2, run("22", "11[6[4[1],5],7[8,3[4]]]")); }

    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int K = Integer.parseInt(sc.nextLine().trim());
        String treeStr = sc.nextLine().trim();
        Node root = parseTree(treeStr);
        int result = countPaths(root, K);
        System.out.println(result);
    }
}