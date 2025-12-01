package by.it.group410902.gribach.lesson14;


import java.util.*;
import java.util.stream.Collectors;
public class SitesB {
    public static void main(String[] args) {
        // Список для хранения кластеров сайтов
        List<Set<String>> dsu = new ArrayList<>();
        // Множество для хранения всех уникальных ссылок
        Set<String> links = new HashSet<>();

        try (Scanner scanner = new Scanner(System.in)) {

            String line;
            while (!(line = scanner.nextLine()).equals("end")) {
                links.add(line);
                String[] sites = line.split("\\+");
                // Создаем множество сайтов и добавляем в структуру DSU
                Set<String> set = new HashSet<>(Arrays.asList(sites));
                dsu.add(set);
            }
        }

        for (int i = 0; i < dsu.size(); i++) {
            // Ищем, с каким кластером можно объединить текущий
            for (Set<String> set : dsu) {
                boolean union = false;
                label:
                if (dsu.get(i) != set) {
                    for (String site1 : dsu.get(i)) {
                        for (String site2 : set) {
                            if (!site1.equals(site2) && checkLink(links, site1, site2)) {
                                union = true;
                                break label;
                            }
                        }
                    }
                }
                if (union) {
                    dsu.get(i).addAll(set);
                    set.clear();
                    i = 0;
                }
            }
        }

        dsu.removeIf(Set::isEmpty);
        // Подсчитываем размер каждого кластера и сортируем их по убыванию
        String output = dsu.stream()
                .map(Set::size)
                .sorted((n, m) -> m - n)
                .map(String::valueOf)
                .collect(Collectors.joining(" "))
                .trim();

        System.out.println(output);
    }

    // Проверка на наличие связи между сайтами
    private static boolean checkLink(Set<String> links, String site1, String site2) {
        return links.contains(String.format("%s+%s", site1, site2)) ||
                links.contains(String.format("%s+%s", site2, site1));
    }
}
