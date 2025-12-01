package by.bsuir.dsa.csv2025.gr410901.Гетманчук;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    // Основной метод программы
    public static void main(String[] args) throws Exception {
        // Определение пути к файлу конфигурации
        Path config = Paths.get("data.txt");

        // Чтение параметров конфигурации в Map
        Map<String, String> kv = readConfig(config);

        // Получение текста для кодирования
        String text = kv.getOrDefault("text", "");

        // Получение пути к файлу с тестами
        String testsFile = kv.get("testsFile");

        // Флаги вывода кодов и сводки
        boolean showCodes = Boolean.parseBoolean(kv.getOrDefault("showCodes", "true"));
        boolean summary = Boolean.parseBoolean(kv.getOrDefault("summary", "true"));

        // Обработка тестов из файла если указан testsFile
        if (testsFile != null && !testsFile.isBlank()) {
            Path tf = Paths.get(testsFile);

            // Чтение всех строк файла тестов
            List<String> lines = Files.readAllLines(tf);

            // Обработка каждой строки теста
            for (String line : lines) {
                String s = parseCaseLine(line);

                Result r = encode(s);

                String decoded = decode(r.bits, r.tree);

                // Вывод сводки если включена
                if (summary) {
                    System.out.println(s);
                    System.out.println(r.bits.length());
                    System.out.println(decoded.equals(s));
                }

                if (showCodes) System.out.println(r.codes);
            }
        } else {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String s = br.readLine();
            if (s == null || s.isBlank()) s = text;
            Result r = encode(s);
            String decoded = decode(r.bits, r.tree);
            System.out.println(String.format("%s %s", decoded, r.bits));
        }
    }

    // Чтение конфигурационного файла в Map ключ-значение
    static Map<String, String> readConfig(Path p) throws IOException {
        Map<String, String> kv = new HashMap<>();

        // Если файл не существует возвращаем пустую карту
        if (!Files.exists(p)) return kv;

        // Чтение всех строк и разделение на ключ и значение
        for (String line : Files.readAllLines(p)) {
            int i = line.indexOf('=');
            if (i > 0) {
                String k = line.substring(0, i).trim();
                String v = line.substring(i + 1).trim();
                kv.put(k, v);
            }
        }
        return kv;
    }

    // Парсинг строки теста, удаление префикса вида "1) "
    static String parseCaseLine(String line) {
        int i = line.indexOf(") ");
        if (i >= 0) return line.substring(i + 2).trim();
        return line.trim();
    }

    // Узел дерева Хаффмана: хранит символ, частоту и ссылки на потомков
    public static final class Node {
        // Символ в листе (для внутренних узлов может быть 0)
        public final char ch;
        // Суммарная частота появления символов в поддереве
        public final int freq;
        // Левый потомок (бит '0')
        public final Node left;
        // Правый потомок (бит '1')
        public final Node right;
        // Минимальный символ в поддереве (для устойчивой сортировки при равных частотах)
        public final char minChar;

        public Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
            this.minChar = computeMinChar();
        }

        // Проверка что узел является листом (нет потомков)
        public boolean isLeaf() { return left == null && right == null; }

        // Вычисление минимального символа в поддереве для стабилизации порядка
        private char computeMinChar() {
            if (left == null && right == null) return ch;
            char a = left != null ? left.minChar : Character.MAX_VALUE;
            char b = right != null ? right.minChar : Character.MAX_VALUE;
            return a < b ? a : b;
        }
    }

    // Результат кодирования: битовая строка, таблица кодов и корень дерева
    public static final class Result {
        public final String bits;
        public final Map<Character, String> codes;
        public final Node tree;

        public Result(String bits, Map<Character, String> codes, Node tree) {
            this.bits = bits;
            this.codes = codes;
            this.tree = tree;
        }
    }

    // Кодирование строки методом Хаффмана: строим частоты, дерево, коды и битовую строку
    public static Result encode(String s) {
        if (s == null) s = "";

        Map<Character, Integer> freq = new LinkedHashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }

        // Пустая строка — ничего кодировать, возвращаем пустой результат
        if (freq.isEmpty()) return new Result("", Collections.emptyMap(), null);

        // Очередь с приоритетом по частоте; при равенстве — по минимальному символу
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator
                .comparingInt((Node n) -> n.freq)
                .thenComparing(n -> n.minChar));

        for (Map.Entry<Character, Integer> e : freq.entrySet()) {
            pq.add(new Node(e.getKey(), e.getValue(), null, null));
        }

        // Особый случай: единственный уникальный символ — код "0" и соответствующая длина
        if (pq.size() == 1) {
            Node only = pq.poll();
            Map<Character, String> codes = new HashMap<>();
            codes.put(only.ch, "0");
            StringBuilder bits = new StringBuilder();
            for (int i = 0; i < s.length(); i++) bits.append('0');
            return new Result(bits.toString(), codes, only);
        }

        // Строим дерево: извлекаем два наименьших узла и объединяем их в родителя
        while (pq.size() > 1) {
            Node a = pq.poll();
            Node b = pq.poll();
            Node parent = new Node((char)0, a.freq + b.freq, a, b);
            pq.add(parent);
        }

        // Корень дерева Хаффмана
        Node root = pq.poll();

        // Формируем таблицу кодов символов по дереву
        Map<Character, String> codes = buildCodes(root);

        // Собираем битовую строку, заменяя каждый символ его кодом
        StringBuilder bits = new StringBuilder();
        for (int i = 0; i < s.length(); i++) bits.append(codes.get(s.charAt(i)));

        return new Result(bits.toString(), codes, root);
    }

    // Декодирование битовой строки по дереву Хаффмана
    public static String decode(String bits, Node tree) {
        if (bits == null || bits.isEmpty()) return "";
        if (tree == null) return "";

        // Если дерево состоит из одного листа — повторяем символ столько раз, сколько бит
        if (tree.isLeaf()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bits.length(); i++) sb.append(tree.ch);
            return sb.toString();
        }

        // Пошаговый обход по битам: '0' — влево, '1' — вправо; на листе добавляем символ
        StringBuilder out = new StringBuilder();
        Node cur = tree;
        for (int i = 0; i < bits.length(); i++) {
            char b = bits.charAt(i);
            cur = (b == '0') ? cur.left : cur.right;
            if (cur.isLeaf()) {
                out.append(cur.ch);
                cur = tree;
            }
        }
        return out.toString();
    }

    // Проверка префиксности набора кодов: ни один код не является префиксом другого
    public static boolean isPrefixFree(Map<Character, String> codes) {
        List<String> all = new ArrayList<>(codes.values());
        for (int i = 0; i < all.size(); i++) {
            for (int j = 0; j < all.size(); j++) {
                if (i == j) continue;
                String a = all.get(i);
                String b = all.get(j);
                if (b.startsWith(a)) return false;
            }
        }
        return true;
    }

    // Возвращает длину битовой строки (null безопасно считается нулевой)
    public static int bitLength(String bits) {
        return bits == null ? 0 : bits.length();
    }

    // Обход дерева Хаффмана (итеративный DFS) для построения таблицы кодов
    private static Map<Character, String> buildCodes(Node root) {
        Map<Character, String> codes = new HashMap<>();
        Deque<Object[]> stack = new ArrayDeque<>();
        stack.push(new Object[]{root, ""});
        while (!stack.isEmpty()) {
            Object[] it = stack.pop();
            Node n = (Node) it[0];
            String p = (String) it[1];
            if (n.isLeaf()) {
                // Для единственного символа код может быть пустым — заменяем на "0"
                codes.put(n.ch, p.isEmpty() ? "0" : p);
            } else {
                // Правый потомок получает бит '1', левый — бит '0'
                if (n.right != null) stack.push(new Object[]{n.right, p + '1'});
                if (n.left != null) stack.push(new Object[]{n.left, p + '0'});
            }
        }
        return codes;
    }

}


