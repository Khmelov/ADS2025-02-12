package by.bsuir.dsa.csv2025.gr451003.Гонцаревич;

import java.util.*;
import java.util.stream.Collectors;

class Solution {
    public int[] sortByDistance(int[] nums, Object target) {
        double mean = calculateMean(nums);
        int[] targetArray = parseTarget(target);
        
        Integer[] result = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            result[i] = nums[i];
        }
        
        Arrays.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                int diffA = calculateMinDifference(a, targetArray);
                int diffB = calculateMinDifference(b, targetArray);
                
                if (diffA != diffB) {
                    return Integer.compare(diffA, diffB);
                }
                
                double distToMeanA = Math.abs(a - mean);
                double distToMeanB = Math.abs(b - mean);
                
                if (Math.abs(distToMeanA - distToMeanB) > 1e-9) {
                    return Double.compare(distToMeanA, distToMeanB);
                }
                
                return Integer.compare(a, b);
            }
        });
        
        int[] primitiveResult = new int[result.length];
        for (int i = 0; i < result.length; i++) {
            primitiveResult[i] = result[i];
        }
        
        return primitiveResult;
    }
    
    private double calculateMean(int[] nums) {
        long sum = 0;
        for (int num : nums) {
            sum += num;
        }
        return (double) sum / nums.length;
    }
    
    private int[] parseTarget(Object target) {
        if (target instanceof Integer) {
            return new int[]{(Integer) target};
        } else if (target instanceof int[]) {
            return (int[]) target;
        } else {
            throw new IllegalArgumentException("Target must be Integer or int[]");
        }
    }
    
    private int calculateMinDifference(int number, int[] targetArray) {
        int minDiff = Integer.MAX_VALUE;
        for (int t : targetArray) {
            int diff = Math.abs(number - t);
            if (diff < minDiff) {
                minDiff = diff;
            }
        }
        return minDiff;
    }
    
    public static void main(String[] args) {
        Solution solution = new Solution();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Тестирование сортировки по удаленности ===");
        System.out.println("Выберите режим:");
        System.out.println("1 - Автоматические тесты");
        System.out.println("2 - Ручной ввод");
        System.out.print("Ваш выбор: ");
        
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            runAutomatedTests(solution);
        } else {
            runManualInput(solution, scanner);
        }
        
        scanner.close();
    }
    
    private static void runAutomatedTests(Solution solution) {
        System.out.println("\n=== Запуск 10 автоматических тестов ===");
        
        // Тест 1
        int[] nums1 = {1, 5, 9, 3, 7};
        int target1 = 4;
        int[] result1 = solution.sortByDistance(nums1, target1);
        System.out.println("Тест 1:");
        System.out.println("Ввод: nums = " + arrayToString(nums1) + ", target = " + target1);
        System.out.println("Вывод: " + arrayToString(result1));
        System.out.println("Ожидаемый: [5, 3, 7, 1, 9]");
        System.out.println();
        
        // Тест 2
        int[] nums2 = {10, 2, 8, 15, 12};
        int[] target2 = {5, 13};
        int[] result2 = solution.sortByDistance(nums2, target2);
        System.out.println("Тест 2:");
        System.out.println("Ввод: nums = " + arrayToString(nums2) + ", target = " + arrayToString(target2));
        System.out.println("Вывод: " + arrayToString(result2));
        System.out.println("Ожидаемый: [8, 12, 10, 15, 2]");
        System.out.println();
        
        // Тест 3
        int[] nums3 = {0, -5, 10, -10, 5};
        int target3 = 0;
        int[] result3 = solution.sortByDistance(nums3, target3);
        System.out.println("Тест 3:");
        System.out.println("Ввод: nums = " + arrayToString(nums3) + ", target = " + target3);
        System.out.println("Вывод: " + arrayToString(result3));
        System.out.println("Ожидаемый: [0, -5, 5, -10, 10]");
        System.out.println();
        
        // Тест 4
        int[] nums4 = {1, 1, 1, 2, 2};
        int target4 = 1;
        int[] result4 = solution.sortByDistance(nums4, target4);
        System.out.println("Тест 4:");
        System.out.println("Ввод: nums = " + arrayToString(nums4) + ", target = " + target4);
        System.out.println("Вывод: " + arrayToString(result4));
        System.out.println("Ожидаемый: [1, 1, 1, 2, 2]");
        System.out.println();
        
        // Тест 5
        int[] nums5 = {100, 50, 75, 25, 0};
        int[] target5 = {60, 80};
        int[] result5 = solution.sortByDistance(nums5, target5);
        System.out.println("Тест 5:");
        System.out.println("Ввод: nums = " + arrayToString(nums5) + ", target = " + arrayToString(target5));
        System.out.println("Вывод: " + arrayToString(result5));
        System.out.println("Ожидаемый: [75, 50, 100, 25, 0]");
        System.out.println();
        
        // Тест 6
        int[] nums6 = {3, 1, 4, 1, 5, 9};
        int target6 = 6;
        int[] result6 = solution.sortByDistance(nums6, target6);
        System.out.println("Тест 6:");
        System.out.println("Ввод: nums = " + arrayToString(nums6) + ", target = " + target6);
        System.out.println("Вывод: " + arrayToString(result6));
        System.out.println("Ожидаемый: [5, 4, 3, 9, 1, 1]");
        System.out.println();
        
        // Тест 7
        int[] nums7 = {-1, -2, -3, 1, 2, 3};
        int target7 = 0;
        int[] result7 = solution.sortByDistance(nums7, target7);
        System.out.println("Тест 7:");
        System.out.println("Ввод: nums = " + arrayToString(nums7) + ", target = " + target7);
        System.out.println("Вывод: " + arrayToString(result7));
        System.out.println("Ожидаемый: [-1, 1, -2, 2, -3, 3]");
        System.out.println();
        
        // Тест 8
        int[] nums8 = {7, 7, 7, 8, 8, 9};
        int[] target8 = {7, 10};
        int[] result8 = solution.sortByDistance(nums8, target8);
        System.out.println("Тест 8:");
        System.out.println("Ввод: nums = " + arrayToString(nums8) + ", target = " + arrayToString(target8));
        System.out.println("Вывод: " + arrayToString(result8));
        System.out.println("Ожидаемый: [7, 7, 7, 8, 8, 9]");
        System.out.println();
        
        // Тест 9
        int[] nums9 = {15, 20, 25, 30, 35};
        int target9 = 22;
        int[] result9 = solution.sortByDistance(nums9, target9);
        System.out.println("Тест 9:");
        System.out.println("Ввод: nums = " + arrayToString(nums9) + ", target = " + target9);
        System.out.println("Вывод: " + arrayToString(result9));
        System.out.println("Ожидаемый: [20, 25, 15, 30, 35]");
        System.out.println();
        
        // Тест 10
        int[] nums10 = {1};
        int target10 = 100;
        int[] result10 = solution.sortByDistance(nums10, target10);
        System.out.println("Тест 10:");
        System.out.println("Ввод: nums = " + arrayToString(nums10) + ", target = " + target10);
        System.out.println("Вывод: " + arrayToString(result10));
        System.out.println("Ожидаемый: [1]");
        System.out.println();
        
        System.out.println("=== Все тесты завершены ===");
    }
    
    private static void runManualInput(Solution solution, Scanner scanner) {
        System.out.println("\n=== Ручной ввод ===");
        
        System.out.print("Введите числа через пробел: ");
        scanner.nextLine(); // consume newline
        String numsInput = scanner.nextLine();
        int[] nums = Arrays.stream(numsInput.split(" "))
                          .mapToInt(Integer::parseInt)
                          .toArray();
        
        System.out.print("Target (число или числа через пробел): ");
        String targetInput = scanner.nextLine();
        Object target;
        
        if (targetInput.contains(" ")) {
            int[] targetArray = Arrays.stream(targetInput.split(" "))
                                    .mapToInt(Integer::parseInt)
                                    .toArray();
            target = targetArray;
        } else {
            target = Integer.parseInt(targetInput);
        }
        
        int[] result = solution.sortByDistance(nums, target);
        
        System.out.println("\nРезультат:");
        System.out.println("Входные данные: nums = " + arrayToString(nums) + ", target = " + targetToString(target));
        System.out.println("Отсортированный массив: " + arrayToString(result));
    }
    
    private static String arrayToString(int[] arr) {
        return Arrays.stream(arr)
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
    }
    
    private static String targetToString(Object target) {
        if (target instanceof Integer) {
            return target.toString();
        } else {
            return arrayToString((int[]) target);
        }
    }
}