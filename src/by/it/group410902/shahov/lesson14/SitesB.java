package by.it.group410902.shahov.lesson14;

import java.util.*;

public class SitesB {
    // Класс для системы непересекающихся множеств (Disjoint Set Union) с строковыми ключами
    static class DSU {
        Map<String, String> parent = new HashMap<>(); // хранит родительский элемент для каждого сайта
        Map<String, Integer> size = new HashMap<>();  // хранит размер компоненты для каждого корня

        // Находит корень элемента с применением сжатия путей
        String find(String x) {
            // Если элемент встречается впервые, инициализируем его
            if (!parent.containsKey(x)) {
                parent.put(x, x);    // изначально родитель - сам элемент
                size.put(x, 1);      // размер компоненты = 1
            }
            // Рекурсивно находим корень и применяем сжатие пути
            if (!x.equals(parent.get(x))) {
                parent.put(x, find(parent.get(x)));
            }
            return parent.get(x);
        }

        // Объединяет два множества, содержащие элементы a и b
        void elementsUnion(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            // Если уже в одном множестве, ничего не делаем
            if (rootA.equals(rootB)) return;

            int sizeA = size.get(rootA);
            int sizeB = size.get(rootB);

            // Эвристика объединения по размеру: меньшее дерево присоединяется к большему
            if (sizeA < sizeB) {
                parent.put(rootA, rootB);
                size.put(rootB, sizeA + sizeB);
            } else {
                parent.put(rootB, rootA);
                size.put(rootA, sizeA + sizeB);
            }
        }

        // Возвращает список размеров всех кластеров, отсортированный по убыванию
        List<Integer> getClusterSizes() {
            Set<String> roots = new HashSet<>();

            // Собираем уникальные корни всех компонент
            for (String site : parent.keySet()) {
                roots.add(find(site));
            }

            // Собираем размеры всех компонент
            List<Integer> sizes = new ArrayList<>();
            for (String root : roots) {
                sizes.add(size.get(root));
            }

            // Сортируем по убыванию размера
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();

        // Чтение входных данных до ключевого слова "end"
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            // Разбиваем строку по символу '+' для получения пары связанных сайтов
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String siteA = parts[0];
                String siteB = parts[1];
                // Объединяем сайты в один кластер
                dsu.elementsUnion(siteA, siteB);
            }
        }

        // Получаем и выводим результат
        List<Integer> result = dsu.getClusterSizes();
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i));
            if (i < result.size() - 1) System.out.print(" ");
        }
        System.out.println();
    }
}
