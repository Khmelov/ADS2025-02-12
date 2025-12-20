package by.it.group451001.kazakov.lesson13;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GraphC {

    // Внутренний класс для хэш-таблицы "строка -> целое число"
    private static class StrIntMap {
        String[] keys;    // Массив ключей
        int[] vals;       // Массив значений
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

        // Функция хэширования строки (FNV-1a)
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
            size = 0;

            // Перемещаем все элементы в новую таблицу
            for (int i = 0; i < ok.length; i++) {
                if (ou[i]) {
                    put(ok[i], ov[i]);
                }
            }
        }

        // Получить значение по ключу или добавить новый элемент
        int getOrPut(String k) {
            // Проверяем необходимость перехэширования
            if ((size << 1) >= keys.length) rehash();

            int m = keys.length;
            int h = hash(k) & (m - 1);  // Вычисляем индекс

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

        // Получить массив всех ключей
        String[] keysArray() {
            String[] res = new String[size];
            int p = 0;
            // Собираем все использованные ключи в массив
            for (int i = 0; i < keys.length; i++) {
                if (used[i]) res[p++] = keys[i];
            }
            return res;
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

    // Сортировка строк пузырьком
    private static void sortStrings(String[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[j].compareTo(a[i]) < 0) {
                    String t = a[i];
                    a[i] = a[j];
                    a[j] = t;
                }
    }

    // Сортировка целых чисел пузырьком
    private static void sortInts(int[] a) {
        for (int i = 0; i < a.length; i++)
            for (int j = i + 1; j < a.length; j++)
                if (a[j] < a[i]) {
                    int t = a[i];
                    a[i] = a[j];
                    a[j] = t;
                }
    }

    public static void main(String[] args) throws IOException {
        // Чтение входных данных
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = br.readLine();

        // Проверка на пустой ввод
        if (line == null || line.trim().isEmpty()) return;

        // Разбиваем строку на части по запятым
        String[] parts = line.split(",");
        String[] L = new String[parts.length];  // Левые части ребер
        String[] R = new String[parts.length];  // Правые части ребер
        int m = 0;  // Счетчик действительных ребер

        // Создаем хэш-таблицу для сопоставления имен вершин с числовыми индексами
        StrIntMap map = new StrIntMap(Math.max(8, parts.length * 2));

        // Обрабатываем каждую часть входной строки
        for (String raw : parts) {
            String s = raw.trim();
            if (s.isEmpty()) continue;

            // Ищем разделитель "->"
            int p = s.indexOf("->");
            if (p < 0) continue;

            // Извлекаем исходную и целевую вершины
            String u = s.substring(0, p).trim();
            String v = s.substring(p + 2).trim();

            if (u.isEmpty() || v.isEmpty()) continue;

            // Сохраняем ребро и регистрируем вершины
            L[m] = u;
            R[m] = v;
            map.getOrPut(u);
            map.getOrPut(v);
            m++;
        }

        int n = map.size;  // Количество уникальных вершин
        if (n == 0) return;

        // Создаем массив меток вершин (обратное отображение индекс -> имя)
        String[] labels = new String[n];
        {
            for (int i = 0, got = 0; i < map.keys.length; i++) {
                if (map.used[i]) {
                    labels[map.vals[i]] = map.keys[i];
                    got++;
                    if (got == n) break;
                }
            }
        }

        // Подсчет исходящих и входящих степеней
        int[] outDeg = new int[n];
        int[] inDeg = new int[n];
        EdgeSet seen = new EdgeSet(Math.max(8, m * 2));

        // Первый проход: подсчет степеней
        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) outDeg[u]++;
        }

        // Построение прямого и обратного графов
        int[][] g = new int[n][];      // Прямой граф (оригинальные направления)
        int[][] gr = new int[n][];     // Обратный граф (обратные направления)
        int[] cur = new int[n];

        // Выделяем память для прямого графа
        for (int i = 0; i < n; i++) {
            g[i] = new int[outDeg[i]];
        }

        // Второй проход: построение прямого графа и подсчет входящих степеней для обратного
        seen = new EdgeSet(Math.max(8, m * 2));
        int[] inDegTmp = new int[n];  // Временный массив для входящих степеней обратного графа

        for (int i = 0; i < m; i++) {
            int u = map.getOrPut(L[i]);
            int v = map.getOrPut(R[i]);
            if (seen.add(u, v)) {
                g[u][cur[u]++] = v;    // Добавляем в прямой граф
                inDegTmp[v]++;         // Увеличиваем счетчик входящих для обратного
            }
        }

        // Строим обратный граф
        gr = new int[n][];
        for (int i = 0; i < n; i++) {
            gr[i] = new int[inDegTmp[i]];
        }
        int[] curR = new int[n];
        for (int u = 0; u < n; u++) {
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                gr[v][curR[v]++] = u;  // Добавляем обратное ребро
            }
        }

        // Алгоритм Косарайю: первый проход (прямой обход)
        boolean[] used = new boolean[n];
        int[] order = new int[n];      // Порядок выхода из DFS
        int ordSz = 0;

        // Нерекурсивный DFS для прямого графа
        for (int s = 0; s < n; s++) {
            if (used[s]) continue;

            int[] stV = new int[n * 2 + 5];  // Стек вершин
            int[] stI = new int[n * 2 + 5];  // Стек индексов
            int top = 0;

            stV[top] = s;
            stI[top] = 0;
            top++;
            used[s] = true;

            while (top > 0) {
                int v = stV[top - 1];
                int i = stI[top - 1];

                // Если обработали всех соседей
                if (i == g[v].length) {
                    order[ordSz++] = v;  // Добавляем в порядок выхода
                    top--;
                    continue;
                }

                int to = g[v][i];
                stI[top - 1] = i + 1;   // Увеличиваем индекс

                if (!used[to]) {
                    used[to] = true;
                    stV[top] = to;
                    stI[top] = 0;
                    top++;
                }
            }
        }

        // Алгоритм Косарайю: второй проход (обратный обход)
        int[] compId = new int[n];     // ID компоненты сильной связности для каждой вершины
        for (int i = 0; i < n; i++) compId[i] = -1;
        int comps = 0;                 // Счетчик компонент

        // Обрабатываем вершины в порядке убывания времени выхода
        for (int it = n - 1; it >= 0; it--) {
            int v0 = order[it];
            if (compId[v0] != -1) continue;  // Уже назначена компонента

            // Нерекурсивный DFS по обратному графу
            int[] st = new int[n];     // Стек вершин
            int sp = 0;                // Указатель стека
            st[sp++] = v0;
            compId[v0] = comps;

            int[] compBuf = new int[n];  // Буфер для вершин текущей компоненты
            int cb = 0;
            compBuf[cb++] = v0;

            int[] iter = new int[n];   // Массив итераторов для каждой позиции стека

            while (sp > 0) {
                int v = st[sp - 1];
                int i = iter[sp - 1];  // Текущий индекс в списке смежности

                if (i == gr[v].length) {
                    sp--;  // Закончили обработку вершины
                    continue;
                }

                int u = gr[v][i];
                iter[sp - 1] = i + 1;  // Переходим к следующему соседу

                if (compId[u] == -1) {
                    compId[u] = comps;
                    st[sp++] = u;
                    compBuf[cb++] = u;
                }
            }

            // Сохраняем найденную компоненту
            if (compList == null) {
                compList = new int[4][];
                compSizeCap = 4;
            }
            if (comps >= compSizeCap) {
                // Увеличиваем емкость массива компонент
                int newCap = compSizeCap << 1;
                int[][] nlist = new int[newCap][];
                for (int i2 = 0; i2 < compSizeCap; i2++) {
                    nlist[i2] = compList[i2];
                }
                compList = nlist;
                compSizeCap = newCap;
            }

            // Копируем вершины компоненты в отдельный массив
            int[] arr = new int[cb];
            for (int t = 0; t < cb; t++) {
                arr[t] = compBuf[t];
            }
            compList[comps] = arr;

            comps++;
        }

        // Обрезаем массив компонент до фактического размера
        int[][] compListLocal = compListTrim(compList, comps);

        // Строим конденсацию графа (граф компонент сильной связности)
        int[] cOutDeg = new int[comps];  // Исходящие степени компонент
        EdgeSet seenCE = new EdgeSet(Math.max(8, m * 2));

        for (int u = 0; u < n; u++) {
            int cu = compId[u];  // Компонента вершины u
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                int cv = compId[v];  // Компонента вершины v

                // Добавляем ребро между разными компонентами
                if (cu != cv && seenCE.add(cu, cv)) {
                    cOutDeg[cu]++;
                    inDeg[cv]++;
                }
            }
        }

        // Строим граф конденсации
        int[][] cg = new int[comps][];  // Граф компонент
        int[] ccur = new int[comps];
        for (int i = 0; i < comps; i++) {
            cg[i] = new int[cOutDeg[i]];
        }

        seenCE = new EdgeSet(Math.max(8, m * 2));
        for (int u = 0; u < n; u++) {
            int cu = compId[u];
            for (int k = 0; k < g[u].length; k++) {
                int v = g[u][k];
                int cv = compId[v];
                if (cu != cv && seenCE.add(cu, cv)) {
                    cg[cu][ccur[cu]++] = cv;
                }
            }
        }

        // Топологическая сортировка конденсации
        int[] inDegC = new int[comps];
        for (int i = 0; i < comps; i++) {
            inDegC[i] = inDeg[i];
        }

        int[] topoC = new int[comps];  // Топологический порядок компонент
        int produced = 0;
        boolean[] usedC = new boolean[comps];

        // Алгоритм Кана для топологической сортировки
        for (int it = 0; it < comps; it++) {
            int pick = -1;
            // Ищем компоненту с нулевой входящей степенью
            for (int i = 0; i < comps; i++) {
                if (!usedC[i] && inDegC[i] == 0) {
                    pick = i;
                    break;
                }
            }
            if (pick == -1) break;

            topoC[produced++] = pick;
            usedC[pick] = true;

            // Уменьшаем входящие степени соседей
            for (int k = 0; k < cg[pick].length; k++) {
                inDegC[cg[pick][k]]--;
            }
        }

        // Формируем выходную строку
        StringBuilder out = new StringBuilder();
        for (int ti = 0; ti < produced; ti++) {
            int cid = topoC[ti];
            int[] verts = compListLocal[cid];

            // Получаем метки вершин компоненты
            String[] compLabels = new String[verts.length];
            for (int i = 0; i < verts.length; i++) {
                compLabels[i] = labels[verts[i]];
            }

            // Сортируем метки в лексикографическом порядке
            sortStrings(compLabels);

            // Добавляем отсортированные метки в результат
            for (String s : compLabels) {
                out.append(s);
            }

            // Добавляем перевод строки между компонентами
            if (ti + 1 < produced) {
                out.append('\n');
            }
        }

        System.out.print(out.toString());
    }

    // Статические переменные для хранения компонент
    private static int[][] compList;
    private static int compSizeCap;

    // Обрезка массива компонент до нужного размера
    private static int[][] compListTrim(int[][] a, int n) {
        int[][] res = new int[n][];
        for (int i = 0; i < n; i++) {
            res[i] = a[i];
        }
        return res;
    }
}