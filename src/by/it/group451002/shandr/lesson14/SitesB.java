package by.it.group451002.shandr.lesson14;

import java.util.*;
import java.util.stream.Collectors;

public class SitesB {
    public static void main(String[] args) {
        // Список для хранения кластеров сайтов (каждый кластер - множество сайтов)
        List<Set<String>> dsu = new ArrayList<>();

        // Множество для хранения всех уникальных ссылок из ввода
        Set<String> links = new HashSet<>();

        try (Scanner scanner = new Scanner(System.in)) {
            String line;

            // Чтение входных данных до слова "end"
            while (!(line = scanner.nextLine()).equals("end")) {
                // Добавляем ссылку в общее множество всех ссылок
                links.add(line);

                // Разделяем ссылку на два сайта
                String[] sites = line.split("\\+");

                // Создаем новый кластер из этих двух сайтов
                Set<String> set = new HashSet<>(Arrays.asList(sites));

                // Добавляем кластер в список кластеров
                dsu.add(set);
            }
        }

        // Алгоритм объединения кластеров
        for (int i = 0; i < dsu.size(); i++) {
            // Проходим по всем другим кластерам
            for (Set<String> set : dsu) {
                boolean union = false; // Флаг, нужно ли объединять кластеры

                // Метка для выхода из вложенных циклов
                label:
                // Проверяем, что это разные кластеры
                if (dsu.get(i) != set) {
                    // Перебираем все сайты в текущем кластере (dsu.get(i))
                    for (String site1 : dsu.get(i)) {
                        // Перебираем все сайты в проверяемом кластере (set)
                        for (String site2 : set) {
                            // Если сайты разные И существует связь между ними
                            if (!site1.equals(site2) && checkLink(links, site1, site2)) {
                                union = true; // Помечаем, что кластеры нужно объединить
                                break label;  // Выходим из всех вложенных циклов
                            }
                        }
                    }
                }

                // Если нашли связь между кластерами
                if (union) {
                    // Объединяем кластеры: добавляем все сайты из set в dsu.get(i)
                    dsu.get(i).addAll(set);

                    // Очищаем второй кластер (теперь он пустой)
                    set.clear();

                    // Начинаем алгоритм заново, так как изменилась структура
                    i = 0;
                }
            }
        }

        // Удаляем все пустые кластеры (остались от объединений)
        dsu.removeIf(Set::isEmpty);

        // Формируем результат:
        // 1. Берем размер каждого кластера
        // 2. Сортируем размеры по убыванию
        // 3. Преобразуем числа в строки
        // 4. Объединяем через пробел
        String output = dsu.stream()
                .map(Set::size)                      // Получаем размер каждого кластера
                .sorted((n, m) -> m - n)             // Сортируем по убыванию
                .map(String::valueOf)                // Преобразуем числа в строки
                .collect(Collectors.joining(" "))    // Собираем в строку через пробел
                .trim();                             // Убираем лишние пробелы по краям

        // Выводим результат
        System.out.println(output);
    }

    // Метод проверки наличия связи между двумя сайтами
    private static boolean checkLink(Set<String> links, String site1, String site2) {
        // Проверяем два варианта:
        // 1. site1+site2
        // 2. site2+site1 (обратный порядок)
        return links.contains(String.format("%s+%s", site1, site2)) ||
                links.contains(String.format("%s+%s", site2, site1));
    }
}