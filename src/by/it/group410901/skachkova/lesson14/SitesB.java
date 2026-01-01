package by.it.group410901.skachkova.lesson14;

import java.util.*;

public class SitesB {

    private static class DSU
    {
        private final int[] parent;
        private final int[] rank;
        private final int[] size;

        DSU(int n)
        {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
                size[i] = 1;
            }
        }

        // Найти главного (корень) с сжатием пути
        int find(int x)
        {
            if (parent[x] != x)
            {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        // объединение двух сайтов
        void union(int a, int b)
        {
            a = find(a);
            b = find(b);
            if (a == b) return;

            // объединение по рангу
            if (rank[a] < rank[b])
            {
                int t = a; a = b; b = t;// меняем местами, чтобы a был "старше"
            }
            parent[b] = a;//меньшую группу присоединяем к большей
            size[a] += size[b];//обновляем размер группы
            if (rank[a] == rank[b])//если ранги были равны, то увеличиваем их
            {
                rank[a]++;
            }
        }

        int getSize(int x)
        {
            return size[find(x)];
        }
    }

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);

        // название сайта → его номер (id)
        Map<String, Integer> siteToId = new HashMap<>();
        List<String> idToSite = new ArrayList<>();

        // Список всех пар "объединений" (рёбер)
        List<String[]> edges = new ArrayList<>();
        while (sc.hasNextLine())
        {
            String line = sc.nextLine().trim();// читаем строку и убираем пробелы
            if (line.equals("end")) break;// конец ввода

            String[] parts = line.split("\\+");// разбиваем по знаку "+"
            if (parts.length != 2) continue;// если не два сайта — пропускаем

            String s1 = parts[0];
            String s2 = parts[1];

            // Если сайт ещё не встречался — даём ему новый номер
            siteToId.putIfAbsent(s1, siteToId.size());
            siteToId.putIfAbsent(s2, siteToId.size());

            // Сохраняем пару (ребро) для последующего объединения
            edges.add(new String[]{s1, s2});
        }

        int n = siteToId.size();// общее количество уникальных сайтов
        if (n == 0)
        {
            System.out.println();
            return;
        }

        DSU dsu = new DSU(n); //создаём DSU для всех сайтов

        // Объединяем все связанные + сайты
        for (String[] edge : edges)
        {
            int id1 = siteToId.get(edge[0]);//номер 1-го сайта
            int id2 = siteToId.get(edge[1]);//номер 2-го сайта
            dsu.union(id1, id2);//объединение всех в одну группу
        }

        // Собираем размеры всех кластеров (групп)
        Map<Integer, Integer> rootToSize = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i); //поиск главного
            rootToSize.put(root, dsu.getSize(i));// сохраняем размер его группы
        }

        List<Integer> sizes = new ArrayList<>(rootToSize.values());

        //  СОРТИРУЕМ ПО УБЫВАНИЮ
        sizes.sort((a, b) -> Integer.compare(b, a));

        // Вывод
        for (int i = 0; i < sizes.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(sizes.get(i));
        }
        System.out.println();
    }
}