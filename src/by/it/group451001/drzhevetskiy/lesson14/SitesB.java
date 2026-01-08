package by.it.group451001.drzhevetskiy.lesson14;

import java.io.*;
import java.util.*;

public class SitesB {

    // Объединение множеств
    static class UF {
        private final int[] rep;
        private final int[] count;

        UF(int n) {
            rep = new int[n];
            count = new int[n];
            for (int i = 0; i < n; i++) {
                rep[i] = i;
                count[i] = 1;
            }
        }

        int leader(int v) {
            if (v == rep[v]) return v;
            rep[v] = leader(rep[v]);
            return rep[v];
        }

        void join(int x, int y) {
            int rx = leader(x);
            int ry = leader(y);
            if (rx == ry) return;

            if (count[rx] < count[ry]) {
                rep[rx] = ry;
                count[ry] += count[rx];
            } else {
                rep[ry] = rx;
                count[rx] += count[ry];
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        ArrayList<String[]> edges = new ArrayList<>();
        LinkedHashSet<String> allSites = new LinkedHashSet<>();

        // Чтение входных данных
        for (String row; (row = reader.readLine()) != null; ) {
            row = row.trim();
            if (row.isEmpty()) continue;
            if (row.equals("end")) break;

            int pos = row.indexOf('+');
            if (pos < 0) continue;

            String left = row.substring(0, pos);
            String right = row.substring(pos + 1);

            edges.add(new String[]{left, right});
            allSites.add(left);
            allSites.add(right);
        }

        if (allSites.isEmpty()) {
            System.out.println();
            return;
        }

        // Присваиваем индекс каждому сайту
        HashMap<String, Integer> number = new HashMap<>();
        int n = 0;
        for (String s : allSites) number.put(s, n++);

        UF uf = new UF(n);

        // Обрабатываем связи
        for (String[] e : edges) {
            uf.join(number.get(e[0]), number.get(e[1]));
        }

        // Подсчитываем размеры компонент
        HashMap<Integer, Integer> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int r = uf.leader(i);
            groups.put(r, groups.getOrDefault(r, 0) + 1);
        }

        // Подготовка вывода
        ArrayList<Integer> out = new ArrayList<>(groups.values());
        out.sort((a, b) -> b - a);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < out.size(); i++) {
            if (i > 0) sb.append(' ');
            sb.append(out.get(i));
        }

        System.out.println(sb);
    }
}
