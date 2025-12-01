package by.it.group410902.andala.lesson14;

import java.util.*;
import java.util.stream.Collectors;

public class SitesB {
    public static void main(String[] args) {
        // Список множеств связанных сайтов
        List<Set<String>> dsu = new ArrayList<>();
        // Все связи между сайтами
        Set<String> links = new HashSet<>();

        // Чтение связей до команды "end"
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (!(line = scanner.nextLine()).equals("end")) {
                links.add(line);
                String[] sites = line.split("\\+");
                // Создаем множество из двух связанных сайтов
                Set<String> set = new HashSet<>(Arrays.asList(sites));
                dsu.add(set);
            }
        }

        // Объединяем множества связанных сайтов
        for (int i = 0; i < dsu.size(); i++) {
            for (Set<String> set : dsu) {
                boolean needUnion = false;
                // Пропускаем сравнение множества с самим собой
                if (dsu.get(i) != set) {
                    // Проверяем есть ли связь между сайтами из разных множеств
                    for (String site1 : dsu.get(i)) {
                        for (String site2 : set) {
                            if (checkLink(links, site1, site2)) {
                                needUnion = true;
                                break;
                            }
                        }
                        if (needUnion) break;
                    }
                }
                // Объединяем множества и начинаем проверку заново
                if (needUnion) {
                    dsu.get(i).addAll(set);
                    set.clear();
                    i = 0; // начинаем с начала
                }
            }
        }

        // Удаляем пустые множества
        dsu.removeIf(Set::isEmpty);

        // Сортируем размеры множеств по убыванию и выводим
        String output = dsu.stream()
                .map(Set::size)
                .sorted((n, m) -> m - n)
                .map(String::valueOf)
                .collect(Collectors.joining(" "));

        System.out.println(output);
    }

    // Проверяем есть ли связь между двумя сайтами
    private static boolean checkLink(Set<String> links, String site1, String site2) {
        return links.contains(site1 + "+" + site2) ||
                links.contains(site2 + "+" + site1);
    }
}