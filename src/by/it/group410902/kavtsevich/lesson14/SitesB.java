package by.it.group410902.kavtsevich.lesson14;

import java.util.*;

public class SitesB {

    // Класс для реализации системы непересекающихся множеств для строковых ключей
    static class DSU {
        Map<String, String> parent = new HashMap<>();  // хранит родительский элемент для каждого сайта
        Map<String, Integer> size = new HashMap<>();   // хранит размер компоненты для каждого корня

        // Метод поиска корня элемента с ленивой инициализацией
        String find(String x) {
            // Если сайт встречается впервые - инициализируем его
            if (!parent.containsKey(x)) {
                parent.put(x, x);      // изначально родитель - сам сайт
                size.put(x, 1);        // изначально размер компоненты = 1
            }
            // Сокращение пути: делаем родителем непосредственно корень
            if (!x.equals(parent.get(x))) {
                parent.put(x, find(parent.get(x))); // рекурсивно находим корень
            }
            return parent.get(x);
        }

        // Объединение двух множеств с эвристикой по размеру
        void union(String a, String b) {
            String rootA = find(a);  // находим корень первого сайта
            String rootB = find(b);  // находим корень второго сайта

            // Если уже в одном множестве - ничего не делаем
            if (rootA.equals(rootB)) return;

            // Получаем размеры компонент
            int sizeA = size.get(rootA);
            int sizeB = size.get(rootB);

            // Эвристика по размеру: присоединяем меньшее дерево к большему
            if (sizeA < sizeB) {
                parent.put(rootA, rootB);              // делаем rootB родителем rootA
                size.put(rootB, sizeA + sizeB);        // обновляем размер rootB
            } else {
                parent.put(rootB, rootA);              // делаем rootA родителем rootB
                size.put(rootA, sizeA + sizeB);        // обновляем размер rootA
            }
        }

        // Получить размеры всех кластеров
        List<Integer> getClusterSizes() {
            Set<String> roots = new HashSet<>();

            // Собираем уникальные корни всех компонент
            for (String site : parent.keySet()) {
                roots.add(find(site));  // используем find для получения актуального корня
            }

            // Собираем размеры всех компонент
            List<Integer> sizes = new ArrayList<>();
            for (String root : roots) {
                sizes.add(size.get(root));
            }

            // Сортировка по убыванию для соответствия формату вывода
            sizes.sort(Collections.reverseOrder());
            return sizes;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DSU dsu = new DSU();  // создаем DSU для работы со строками

        // Чтение входных данных до слова "end"
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;  // условие завершения ввода

            // Разбиваем строку по символу '+' для получения пары связанных сайтов
            String[] parts = line.split("\\+");
            if (parts.length == 2) {
                String siteA = parts[0];
                String siteB = parts[1];
                dsu.union(siteA, siteB);  // объединяем сайты в один кластер
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