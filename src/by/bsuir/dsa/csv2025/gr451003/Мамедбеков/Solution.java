package by.bsuir.dsa.csv2025.gr451003.Мамедбеков;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long left = 1L;
        long right = 1000000000L;
        int queryCount = 0;

        try {
            while (left <= right) {
                long mid = left + (right - left) / 2;
                queryCount++;

                if (queryCount > 30) {
                    System.err.println("Превышено максимальное количество запросов!");
                    break;
                }

                System.out.println("? " + mid);
                System.out.flush();

                String response = scanner.nextLine().trim();

                if (response.equals("=")) {
                    System.out.println("! " + mid);
                    return;
                } else if (response.equals("<")) {
                    right = mid - 1;
                } else if (response.equals(">")) {
                    left = mid + 1;
                } else {
                    System.err.println("Неверный формат ответа: " + response);
                    queryCount--;
                }
            }

            System.err.println("Число не найдено в диапазоне после 30 запросов!");
        } catch (Exception e) {
            System.err.println("Ошибка ввода: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}