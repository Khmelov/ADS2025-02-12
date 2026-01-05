package by.it.group410902.varava.lesson14;

import java.util.*;

public class SitesB {

    // Класс для реализации системы непересекающихся множеств (Disjoint Set Union)
    static class DSU {
        private Map<String, String> parent; // Хранит родительский элемент для каждого узла
        private Map<String, Integer> rank;  // Хранит ранг (высоту) дерева для оптимизации

        public DSU() {
            parent = new HashMap<>();
            rank = new HashMap<>();
        }

        // Метод поиска корневого элемента с оптимизацией пути
        public String find(String x) {
            // Если элемента еще нет в системе, добавляем его как корень самого себя
            if (!parent.containsKey(x)) {
                parent.put(x, x);
                rank.put(x, 0);
                return x;
            }

            // Рекурсивно находим корень и делаем сжатие пути
            if (!parent.get(x).equals(x)) {
                parent.put(x, find(parent.get(x))); // сжатие пути - делаем родителем корень
            }
            return parent.get(x);
        }

        // Метод объединения двух множеств
        public void union(String x, String y) {
            String rootX = find(x); // Находим корень первого элемента
            String rootY = find(y); // Находим корень второго элемента

            // Если корни разные - объединяем множества
            if (!rootX.equals(rootY)) {
                // Объединение по рангу (эвристика по высоте дерева)
                if (rank.get(rootX) < rank.get(rootY)) {
                    // Присоединяем меньшее дерево к большему
                    parent.put(rootX, rootY);
                } else if (rank.get(rootX) > rank.get(rootY)) {
                    // Присоединяем меньшее дерево к большему
                    parent.put(rootY, rootX);
                } else {
                    // Если ранги равны, выбираем произвольно и увеличиваем ранг
                    parent.put(rootY, rootX);
                    rank.put(rootX, rank.get(rootX) + 1);
                }
            }
        }

        // Метод для получения всех сайтов в системе
        public Set<String> getAllSites() {
            return parent.keySet();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DSU dsu = new DSU(); // Создаем систему непересекающихся множеств

        // Читаем входные данные построчно до ключевого слова "end"
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Условие выхода из цикла ввода
            if (line.equals("end")) {
                break;
            }

            // Разбиваем строку на два сайта по разделителю "+"
            // Пример: "site1+site2" -> ["site1", "site2"]
            String[] sites = line.split("\\+");
            if (sites.length == 2) {
                String site1 = sites[0];
                String site2 = sites[1];

                // Объединяем сайты в одно множество (связь ненаправленная)
                dsu.union(site1, site2);
            }
        }

        scanner.close(); // Закрываем Scanner для освобождения ресурсов

        // Подсчитываем размеры кластеров (компонент связности)
        Map<String, Integer> clusterSizes = new HashMap<>();

        // Для каждого сайта находим корневой элемент и увеличиваем счетчик размера кластера
        for (String site : dsu.getAllSites()) {
            String root = dsu.find(site); // Находим корень кластера
            // Увеличиваем счетчик для данного корня (кластера)
            clusterSizes.put(root, clusterSizes.getOrDefault(root, 0) + 1);
        }

        // Собираем размеры всех кластеров в список
        List<Integer> sizes = new ArrayList<>(clusterSizes.values());

        // Сортируем размеры кластеров по УБЫВАНИЮ (требование теста)
        Collections.sort(sizes, Collections.reverseOrder());

        // Выводим результат: размеры кластеров через пробел
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
    }
}