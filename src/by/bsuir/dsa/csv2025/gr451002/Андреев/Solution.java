package by.bsuir.dsa.csv2025.gr451002.Андреев;

import java.util.*;

/**
 * Решение задачи "Оптимальный набор проектов" с использованием дискретного метода Лагранжа
 * 
 * Автор: Андреев Александр
 * 
 * Задача: Выбрать набор проектов для максимизации прибыли при ограничениях:
 * - Бюджет: 1,000,000$
 * - Максимальное количество сотрудников: 50
 */
public class Solution {
    
    private static final int BUDGET = 1_000_000;
    private static final int MAX_MANPOWER = 50;
    
    /**
     * Класс для представления проекта
     */
    static class Project {
        int index;
        int cost;
        int profit;
        int manpower;
        double lagrangianValue; // Значение лагранжиана для сортировки
        
        Project(int index, int cost, int profit, int manpower) {
            this.index = index;
            this.cost = cost;
            this.profit = profit;
            this.manpower = manpower;
        }
    }
    
    /**
     * Решает задачу выбора проектов с использованием дискретного метода Лагранжа
     * 
     * @param costs массив стоимостей проектов
     * @param profits массив прибылей проектов
     * @param manpowers массив требуемых сотрудников для проектов
     * @return список индексов выбранных проектов
     */
    public static List<Integer> selectSolution(int[] costs, int[] profits, int[] manpowers) {
        int n = costs.length;
        
        // Создаем массив проектов
        Project[] projects = new Project[n];
        for (int i = 0; i < n; i++) {
            projects[i] = new Project(i, costs[i], profits[i], manpowers[i]);
        }
        
        // Параметры Лагранжа (множители)
        // Начинаем с начальных значений, основанных на средних соотношениях
        double lambda1 = 0.001; // Множитель для ограничения по бюджету
        double lambda2 = 100.0; // Множитель для ограничения по сотрудникам
        
        List<Integer> bestSolution = new ArrayList<>();
        int bestProfit = 0;
        
        // Итеративная оптимизация множителей Лагранжа
        for (int iteration = 0; iteration < 100; iteration++) {
            // Вычисляем значения лагранжиана для каждого проекта
            // L = profit - λ1*cost - λ2*manpower
            for (Project project : projects) {
                project.lagrangianValue = project.profit - lambda1 * project.cost - lambda2 * project.manpower;
            }
            
            // Сортируем проекты по убыванию значения лагранжиана
            Arrays.sort(projects, (a, b) -> Double.compare(b.lagrangianValue, a.lagrangianValue));
            
            // Жадный выбор проектов с учетом ограничений
            List<Integer> solution = new ArrayList<>();
            int totalCost = 0;
            int totalManpower = 0;
            int totalProfit = 0;
            
            for (Project project : projects) {
                if (totalCost + project.cost <= BUDGET && 
                    totalManpower + project.manpower <= MAX_MANPOWER) {
                    solution.add(project.index);
                    totalCost += project.cost;
                    totalManpower += project.manpower;
                    totalProfit += project.profit;
                }
            }
            
            // Обновляем лучшее решение
            if (totalProfit > bestProfit) {
                bestProfit = totalProfit;
                bestSolution = new ArrayList<>(solution);
            }
            
            // Обновляем множители Лагранжа на основе нарушения ограничений
            // (для следующей итерации, если нужно)
            if (iteration < 99) {
                // Простая адаптация: если решение хорошее, немного корректируем множители
                double costViolation = Math.max(0, totalCost - BUDGET * 0.95) / BUDGET;
                double manpowerViolation = Math.max(0, totalManpower - MAX_MANPOWER * 0.95) / MAX_MANPOWER;
                
                lambda1 += 0.0001 * costViolation;
                lambda2 += 10.0 * manpowerViolation;
            }
        }
        
        // Финальная проверка: пробуем улучшить решение локальным поиском
        return improveSolution(bestSolution, costs, profits, manpowers);
    }
    
    /**
     * Улучшает решение локальным поиском
     */
    private static List<Integer> improveSolution(List<Integer> solution, 
                                                   int[] costs, int[] profits, int[] manpowers) {
        List<Integer> improved = new ArrayList<>(solution);
        Set<Integer> selected = new HashSet<>(solution);
        Set<Integer> notSelected = new HashSet<>();
        
        for (int i = 0; i < costs.length; i++) {
            if (!selected.contains(i)) {
                notSelected.add(i);
            }
        }
        
        int currentCost = solution.stream().mapToInt(i -> costs[i]).sum();
        int currentManpower = solution.stream().mapToInt(i -> manpowers[i]).sum();
        int currentProfit = solution.stream().mapToInt(i -> profits[i]).sum();
        
        // Пробуем заменить проекты или добавить новые
        boolean improvedFlag = true;
        while (improvedFlag) {
            improvedFlag = false;
            
            // Пробуем добавить проекты
            for (Integer projectIdx : notSelected) {
                if (currentCost + costs[projectIdx] <= BUDGET && 
                    currentManpower + manpowers[projectIdx] <= MAX_MANPOWER) {
                    improved.add(projectIdx);
                    selected.add(projectIdx);
                    notSelected.remove(projectIdx);
                    currentCost += costs[projectIdx];
                    currentManpower += manpowers[projectIdx];
                    currentProfit += profits[projectIdx];
                    improvedFlag = true;
                    break;
                }
            }
            
            // Пробуем заменить проекты (удалить один, добавить другой)
            if (!improvedFlag) {
                for (Integer removeIdx : new ArrayList<>(selected)) {
                    for (Integer addIdx : notSelected) {
                        int newCost = currentCost - costs[removeIdx] + costs[addIdx];
                        int newManpower = currentManpower - manpowers[removeIdx] + manpowers[addIdx];
                        int newProfit = currentProfit - profits[removeIdx] + profits[addIdx];
                        
                        if (newCost <= BUDGET && newManpower <= MAX_MANPOWER && newProfit > currentProfit) {
                            improved.remove(removeIdx);
                            improved.add(addIdx);
                            selected.remove(removeIdx);
                            selected.add(addIdx);
                            notSelected.remove(addIdx);
                            notSelected.add(removeIdx);
                            currentCost = newCost;
                            currentManpower = newManpower;
                            currentProfit = newProfit;
                            improvedFlag = true;
                            break;
                        }
                    }
                    if (improvedFlag) break;
                }
            }
        }
        
        Collections.sort(improved);
        return improved;
    }
    
    /**
     * Вычисляет общую прибыль выбранных проектов
     */
    public static int calculateTotalProfit(List<Integer> selectedProjects, int[] profits) {
        return selectedProjects.stream().mapToInt(i -> profits[i]).sum();
    }
    
    /**
     * Вычисляет общую стоимость выбранных проектов
     */
    public static int calculateTotalCost(List<Integer> selectedProjects, int[] costs) {
        return selectedProjects.stream().mapToInt(i -> costs[i]).sum();
    }
    
    /**
     * Вычисляет общее количество сотрудников для выбранных проектов
     */
    public static int calculateTotalManpower(List<Integer> selectedProjects, int[] manpowers) {
        return selectedProjects.stream().mapToInt(i -> manpowers[i]).sum();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Читаем количество проектов
        int n = scanner.nextInt();
        
        int[] costs = new int[n];
        int[] profits = new int[n];
        int[] manpowers = new int[n];
        
        // Читаем данные о проектах
        for (int i = 0; i < n; i++) {
            costs[i] = scanner.nextInt();
            profits[i] = scanner.nextInt();
            manpowers[i] = scanner.nextInt();
        }
        
        // Решаем задачу
        List<Integer> selectedProjects = selectSolution(costs, profits, manpowers);
        
        // Выводим результаты
        System.out.println(selectedProjects.size());
        for (int idx : selectedProjects) {
            System.out.print(idx + " ");
        }
        System.out.println();
        System.out.println(calculateTotalProfit(selectedProjects, profits));
        System.out.println(calculateTotalCost(selectedProjects, costs));
        System.out.println(calculateTotalManpower(selectedProjects, manpowers));
        
        scanner.close();
    }
}
