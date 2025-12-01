package by.bsuir.dsa.csv2025.gr410901.Курач;

import java.util.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

class Solution {
    int val;
    Solution left;
    Solution right;
    
    Solution(int x) {
        val = x;
    }
}

public class BinarySearchTreeSearch {
    
    // Функция поиска в BST (Задание 1)
    public static boolean find(Solution root, int x) {
        if (root == null) {
            return false;
        }
        if (root.val == x) {
            return true;
        }
        if (x < root.val) {
            return find(root.left, x);
        } else {
            return find(root.right, x);
        }
    }
    
    // Вспомогательная функция для создания тестового дерева
    public static Solution buildTestTree() {
        /*
            Дерево:
                8
               / \
              3   10
             / \    \
            1   6    14
               / \   /
              4   7 13
        */
        Solution root = new Solution(8);
        
        root.left = new Solution(3);
        root.right = new Solution(10);
        
        root.left.left = new Solution(1);
        root.left.right = new Solution(6);
        
        root.left.right.left = new Solution(4);
        root.left.right.right = new Solution(7);
        
        root.right.right = new Solution(14);
        root.right.right.left = new Solution(13);
        
        return root;
    }



    // Основной метод для консольного ввода (сохраняем оригинальную функциональность)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Solution root = buildTestTree();

        // Читаем значение для поиска
        if (scanner.hasNextInt()) {
            int target = scanner.nextInt();
            boolean result = find(root, target);
            System.out.println(result);
        }

        scanner.close();
    }
}