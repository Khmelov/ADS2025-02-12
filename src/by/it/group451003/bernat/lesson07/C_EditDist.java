package by.it.group451003.bernat.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Итерационно вычислить алгоритм преобразования двух данных непустых строк
    Вывести через запятую редакционное предписание в формате:
     операция("+" вставка, "-" удаление, "~" замена, "#" копирование)
     символ замены или вставки
*/
public class C_EditDist {

    // Метод для вычисления редакционного предписания для преобразования строки one в строку two
    String getDistanceEdinting(String one, String two) {
        // Получаем длины строк
        int m = one.length();
        int n = two.length();
        // Создаем таблицу динамического программирования размером (m+1)x(n+1)
        // dp[i][j] хранит минимальное количество операций для преобразования
        // первых i символов строки one в первые j символов строки two
        int[][] dp = new int[m + 1][n + 1];

        // Инициализируем первую строку и первый столбец
        // dp[i][0] — стоимость удаления i символов из первой строки
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // dp[0][j] — стоимость вставки j символов из второй строки
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполняем таблицу динамического программирования
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Определяем стоимость замены: 0, если символы совпадают, 1 — если различаются
                int cost = one.charAt(i - 1) == two.charAt(j - 1) ? 0 : 1;
                // Выбираем минимальную стоимость из трех операций:
                // 1. Удаление: стоимость предыдущего состояния + 1
                // 2. Вставка: стоимость предыдущего состояния + 1
                // 3. Замена (или копирование, если символы совпадают)
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1,    // удаление
                                dp[i][j - 1] + 1),   // вставка
                        dp[i - 1][j - 1] + cost       // замена или совпадение
                );
            }
        }

        // Восстанавливаем последовательность операций
        int i = m, j = n;
        // Используем LinkedList для хранения операций в порядке от конца к началу
        LinkedList<String> ops = new LinkedList<>();
        // Пока не достигли начала хотя бы одной строки
        while (i > 0 || j > 0) {
            // Случай 1: символы совпадают, это копирование
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1]
                    && one.charAt(i - 1) == two.charAt(j - 1)) {
                ops.addFirst("#"); // Операция копирования
                i--;
                j--;
            }
            // Случай 2: замена символа
            else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.addFirst("~" + two.charAt(j - 1)); // Операция замены с указанием символа
                i--;
                j--;
            }
            // Случай 3: вставка символа из второй строки
            else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                ops.addFirst("+" + two.charAt(j - 1)); // Операция вставки с указанием символа
                j--;
            }
            // Случай 4: удаление символа из первой строки
            else {
                ops.addFirst("-" + one.charAt(i - 1)); // Операция удаления с указанием символа
                i--;
            }
        }

        // Формируем итоговую строку с операциями, разделенными запятыми
        StringBuilder sb = new StringBuilder();
        for (String op : ops) {
            sb.append(op).append(",");
        }
        // Возвращаем строку с редакционным предписанием
        return sb.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataABC.txt"
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        // Создаем Scanner для чтения строк из файла
        Scanner scanner = new Scanner(stream);
        // Читаем и обрабатываем три пары строк, выводя редакционное предписание для каждой
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}