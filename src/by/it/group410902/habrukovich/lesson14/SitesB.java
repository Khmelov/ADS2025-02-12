package by.it.group410902.habrukovich.lesson14;

import java.util.*;

public class SitesB {

    // Класс DSU для объединения элементов в группы
    private static class DSU {
        int[] parent;
        int[] size;

        DSU(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i; // каждый элемент сам себе родитель
                size[i] = 1;   // начальный размер множества = 1
            }
        }

        // Метод поиска корня множества с компрессией пути
        int find(int x) {
            if (parent[x] != x)
                parent[x] = find(parent[x]); // путь к корню сжат
            return parent[x]; // возвращаем корень множества
        }

        // Метод объединения двух множеств
        void union(int a, int b) {
            int ra = find(a); // корень множества a
            int rb = find(b); // корень множества b
            if (ra == rb) return; // если уже в одном множестве, ничего не делаем

            // объединяем меньший размер в больший (union by size)
            if (size[ra] < size[rb]) {
                parent[ra] = rb;
                size[rb] += size[ra];
            } else {
                parent[rb] = ra;
                size[ra] += size[rb];
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Map для сопоставления строки  с уникальным ID
        Map<String, Integer> id = new HashMap<>();
        // Список пар сайтов, которые нужно объединить
        List<String[]> pairs = new ArrayList<>();
        int counter = 0; // счётчик уникальных сайтов

        // Чтение входных данных до слова "end"
        while (true) {
            String line = sc.nextLine().trim();
            if (line.equals("end")) break;

            String[] ab = line.split("\\+"); // строки формата "A+B"
            String a = ab[0];
            String b = ab[1];

            // присвоение уникального ID, если сайт новый
            if (!id.containsKey(a)) id.put(a, counter++);
            if (!id.containsKey(b)) id.put(b, counter++);

            // добавляем пару для объединения
            pairs.add(new String[]{a, b});
        }

        // создаём DSU на основе количества уникальных сайтов
        DSU dsu = new DSU(counter);


        for (String[] p : pairs) {
            dsu.union(id.get(p[0]), id.get(p[1]));
        }

        // Map для подсчёта размеров групп (корень -> количество элементов в группе)
        Map<Integer, Integer> rootCount = new HashMap<>();
        for (int i = 0; i < counter; i++) {
            int r = dsu.find(i); // находим корень множества
            rootCount.put(r, rootCount.getOrDefault(r, 0) + 1); // увеличиваем счётчик группы
        }


        List<Integer> res = new ArrayList<>(rootCount.values());


        res.sort((a, b) -> b - a);


        for (int i = 0; i < res.size(); i++) {
            System.out.print(res.get(i));
            if (i + 1 < res.size()) System.out.print(" ");
        }
    }
}
