package by.it.group410902.varava.lesson13;

import java.util.ArrayList;
import java.util.Scanner;

public class GraphB {
    // Создание перечисления для цветов вершин (состояний при обходе)
    // white - не посещена, gray - в процессе обработки, black - обработана
    private static enum color {white, gray, black};

    // Метод для увеличения размера массива списков

    private static void resizeArray(ArrayList<ArrayList<Integer>> arr, int len){ // Добавляет новые пустые списки пока размер не станет равен len

        for (int i = 0; i <= len - arr.size(); i++) // Цикл добавляет нужное количество новых списков
            arr.add(new ArrayList<>());
    }

    // Глобальная переменная для хранения результата поиска циклов
    private static Boolean res; // true - цикл найден, false - циклов нет

    // Рекурсивный метод обхода графа
    // m - матрица смежности (граф), f - массив цветов вершин, cur - текущая вершина
    private static void dfs(ArrayList<ArrayList<Integer>> m, ArrayList<color> f, int cur){

        if (res)
            return;
        // Помечаем текущую вершину как "в процессе обработки" (серая)
        f.set(cur, color.gray);
        // Перебираем всех соседей текущей вершины
        for (var i : m.get(cur)){
            // Если сосед не посещен (белый), обрабатываем его
            if (f.get(i).equals(color.white))
                dfs(m, f, i);
            else
                // Если сосед серый то нашли цикл
                if (f.get(i).equals(color.gray))
                    res = true;
        }
        // Помечаем текущую вершину как полностью обработанную (черная)
        f.set(cur, color.black);
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            int i = 0;
            i += 2;
        }

        String s;// Объявление переменной для хранения входной строки
        {

            Scanner in = new Scanner(System.in);    // Создание сканера для чтения ввода
            s = in.nextLine();// Чтение строки из консоли
            in.close();
        }
        // Создание матрицы смежности для хранения графа
        // Каждый элемент - список соседей соответствующей вершины
        var m = new ArrayList<ArrayList<Integer>>();
        // Цикл для парсинга входной строки
        for (int i = 0; i < s.length();){
            // Создание строителя строк для первой вершины ребра
            StringBuilder sb_n1 = new StringBuilder();
            // Чтение цифр первой вершины (пока символы от '0' до '9')
            while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9')
                sb_n1.append(s.charAt(i++));
            // Проверка: если строка закончилась или следующий символ - запятая
            // Это случай изолированной вершины (без исходящих ребер)
            if (i >= s.length() || s.charAt(i) == ','){
                // Преобразование строки в число - номер вершины
                int n1 = Integer.parseInt(sb_n1.toString());
                // Проверка необходимости увеличения размера матрицы
                if (m.size() <= n1)
                    resizeArray(m, n1 + 1);
                // Пропуск запятой и пробела (i += 2)
                i += 2;
            }
            else {// Иначе - это ребро между двумя вершинами
                i += 4;// Пропуск символов " -> "
                // Создание строителя строк для второй вершины ребра
                StringBuilder sb_n2 = new StringBuilder();
                // Чтение цифр второй вершины
                while (i < s.length() && s.charAt(i) >= '0' && s.charAt(i) <= '9')
                    sb_n2.append(s.charAt(i++));
                i += 2;// Пропуск запятой и пробела
                // Преобразование обеих вершин в числа
                int n1 = Integer.parseInt(sb_n1.toString()), n2 = Integer.parseInt(sb_n2.toString());
                if (m.size() <= Math.max(n1, n2))// Проверка необходимости увеличения размера матрицы
                    resizeArray(m, Math.max(n1, n2) + 1);
                m.get(n1).add(n2);// Добавление ребра в граф: из n1 в n2
            }
        }
        // Создание массива цветов для всех вершин
        var f = new ArrayList<color>();
        // Инициализация всех вершин как непосещенных (белых)
        for (int i = 0; i < m.size(); i++)
            f.add(color.white);
        // Инициализация результата - циклов пока нет
        res = false;
        // Обход всех вершин графа
        for (int i = 0; i < f.size() && !res; i++)
            // Если вершина не посещена, запускаем из нее DFS
            if (f.get(i).equals(color.white))
                dfs(m, f, i);
        System.out.println(res ? "yes" : "no"); // Вывод результата: "yes" если есть циклы, "no" если нет
    }
}