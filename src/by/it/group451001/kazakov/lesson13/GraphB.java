package by.it.group451001.kazakov.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphB {

    // Внутренний класс для реализации хэш-таблицы "строка -> целое число"
    private static class StrIntMap {
        String[] keys;    // Массив ключей (строк)
        int[] vals;       // Массив значений (целых чисел)
        boolean[] used;   // Флаги занятости ячеек
        int size;         // Текущее количество элементов

        // Конструктор с начальной емкостью
        StrIntMap(int cap) {
            // Находим ближайшую степень двойки, большую чем cap * 2
            int n = 1;
            while (n < cap * 2) n <<= 1;
            keys = new String[n];
            vals = new int[n];
            used = new boolean[n];
        }

        // Функция хэширования строки (вариант FNV-1a)
        private int hash(String s) {
            int h = 1469598101;  // Начальное значение FNV
            for (int i = 0; i < s.length(); i++) {
                h ^= s.charAt(i);     // XOR с текущим символом
                h *= 16777619;        // Умножение на FNV prime
            }
            return h & 0x7fffffff;    // Гарантируем неотрицательное число
        }

        // Перехэширование при заполнении таблицы
        private void rehash() {
            // Сохраняем старые массивы
            String[] ok = keys;
            int[] ov = vals;
            boolean[] ou = used;

            // Создаем новые массивы в два раза больше
            keys = new String[ok.length << 1];
            vals = new int[keys.length];
            used = new boolean[keys.length];

            int oldSize = size;
            size = 0;

            // Перемещаем все элементы в новую таблицу
            for (int i = 0; i < ok.length; i++) {
                if (ou[i]) {
                    put(ok[i], ov[i]);
                }
            }
            size = oldSize;
        }

        // Получить значение по ключу или добавить новый элемент
        int getOrPut(String k) {
            // Проверяем необходимость перехэширования
            if ((size << 1) >= keys.length) rehash();

            int m = keys.length;
            int h = hash(k) & (m - 1);  // Вычисляем индекс (m - степень двойки)

            // Линейное пробирование для разрешения коллизий
            while (used[h]) {
                if (keys[h].equals(k)) return vals[h];  // Ключ найден
                h = (h + 1) & (m - 1);                 // Переход к следующей ячейке
            }

            // Добавление нового ключа
            used[h] = true;
            keys[h] = k;
            vals[h] = size;  // Присваиваем следующий доступный индекс
            return size++;    // Возвращаем индекс и увеличиваем счетчик
        }

        // Явное добавление пары ключ-значение
        void put(String k, int v) {
            if ((size << 1) >= keys.length) rehash();

            int m = keys.length;
            int h = hash(k) & (m - 1);

            while (used[h]) {
                if (keys[h].equals(k)) {
                    vals[h] = v;  // Обновляем существующее значение
                    return;
                }
                h = (h + 1) & (m - 1);
            }

            used[h] = true;
            keys[h] = k;
            vals[h] = v;
            size++;
        }
    }

    // Внутренний класс для хранения множества ребер
    private static class EdgeSet {
        long[] data;     // Массив для хранения ребер (u << 32 | v)
        boolean[] used;  // Флаги занятости ячеек

        EdgeSet(int cap) {
            // Находим ближайшую степень двойки
            int n = 1;
            while (n < cap * 2) n <<= 1;
            data = new long[n];
            used = new boolean[n];
        }

        // Функция хэширования 64-битного числа
        private int hash64(long x) {
            x ^= (x >>> 33);
            x *= 0xff51afd7ed558ccdL;
            x ^= (x >>> 33);
            x *= 0xc4ceb9fe1a85ec53L;
            x ^= (x >>> 33);
            return (int)(x ^ (x >>> 32)) & 0x7fffffff;
        }

        // Добавление ребра (u, v) в множество
        boolean add(int u, int v) {
            // Упаковываем две 32-битные вершины в одно 64-битное число
            long key = (((long)u) << 32) ^ (v & 0xffffffffL);

            int m = data.length;
            int h = hash64(key) & (m - 1);

            // Проверяем наличие ребра
            while (used[h]) {
                if (data[h] == key) return false;  // Ребро уже существует
                h = (h + 1) & (m - 1);
            }

            // Добавляем новое ребро
            used[h] = true;
            data[h] = key;
            return true;
        }
    }

    public static void main(String[] args) throws IOException {
        // Чтение входных данных
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        // Проверка на пустой ввод
        if (line == null || line.trim().isEmpty()) {
            System.out.println("no");
            return;
        }

        // Разбиваем строку на части по запятым
        String[] parts = line.split(",");
        String[] L = new String[parts.length];  // Левые части ребер (исходные вершины)
        String[] R = new String[parts.length];  // Правые части ребер (целевые вершины)
        int m = 0;  // Счетчик действительных ребер

        // Создаем хэш-таблицу для сопоставления имен вершин с числовыми индексами
        StrIntMap map = new StrIntMap(Math.max(8, parts.length * 2));

        // Обрабатываем каждую часть входной строки
        for (String raw : parts) {
            String s = raw.trim();
            if (s.isEmpty()) continue;  // Пропускаем пустые строки

            // Ищем разделитель "->"
            int p = s.indexOf("->");
            if (p < 0) continue;  // Пропускаем некорректные записи

            // Извлекаем исходную и целевую вершины
            String u = s.substring(0, p).trim();
            String v = s.substring(p + 2).trim();

            if (u.isEmpty() || v.isEmpty()) continue;  // Пропускаем пустые вершины

            // Сохраняем ребро и регистрируем вершины в хэш-таблице
            L[m] = u;
            R[m] = v;
            map.getOrPut(u);
            map.getOrPut(v);
            m++;
        }

        int n = map.size;  // Количество уникальных вершин
        if (n == 0) {
            System.out.println("no");
            return;
        }

        // Массив для подсчета исходящих степеней вершин
        int[] outDeg = new int[n];
        // Множество для отслеживания уникальных ребер
        EdgeSet seen = new EdgeSet(Math.max(8, m * 2));

        // Первый проход: подсчет исходящих степеней и удаление дубликатов
        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) outDeg[u]++;  // Увеличиваем счетчик только для новых ребер
        }

        // Создаем списки смежности на основе подсчитанных степеней
        int[][] adj = new int[n][];
        int[] cur = new int[n];  // Текущие позиции для добавления в списки смежности

        for (int i = 0; i < n; i++) {
            adj[i] = new int[outDeg[i]];  // Выделяем массив нужного размера
        }

        // Второй проход: построение списков смежности
        seen = new EdgeSet(Math.max(8, m * 2));  // Новое множество для отслеживания добавленных ребер
        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) {
                adj[u][cur[u]++] = v;  // Добавляем ребро в список смежности
            }
        }

        // Массив цветов вершин для DFS: 0 - белый (не посещена), 1 - серый (в обработке), 2 - черный (обработана)
        byte[] color = new byte[n];
        boolean hasCycle = false;  // Флаг обнаружения цикла

        // Поиск в глубину для каждой непосещенной вершины
        for (int s = 0; s < n && !hasCycle; s++) {
            if (color[s] != 0) continue;  // Пропускаем уже посещенные вершины

            // Реализация DFS с использованием стека (нерекурсивная)
            int[] stackV = new int[n * 2 + 5];  // Стек для вершин
            int[] stackI = new int[n * 2 + 5];  // Стек для индексов смежных вершин
            int top = 0;

            // Добавляем стартовую вершину в стек
            stackV[top] = s;
            stackI[top] = 0;
            top++;
            color[s] = 1;  // Помечаем как "в обработке"

            // Обработка стека
            while (top > 0 && !hasCycle) {
                int v = stackV[top - 1];  // Текущая вершина
                int i = stackI[top - 1];  // Текущий индекс в списке смежности

                // Если обработали всех соседей
                if (i == adj[v].length) {
                    color[v] = 2;  // Помечаем как полностью обработанную
                    top--;          // Убираем из стека
                    continue;
                }

                int to = adj[v][i];      // Следующий сосед
                stackI[top - 1] = i + 1; // Увеличиваем индекс для следующего вызова

                if (color[to] == 0) {
                    // Новая непосещенная вершина
                    color[to] = 1;
                    stackV[top] = to;
                    stackI[top] = 0;
                    top++;
                } else if (color[to] == 1) {
                    // Обнаружен цикл (вершина уже в обработке)
                    hasCycle = true;
                }
            }
        }

        // Вывод результата
        System.out.println(hasCycle ? "yes" : "no");
    }
}