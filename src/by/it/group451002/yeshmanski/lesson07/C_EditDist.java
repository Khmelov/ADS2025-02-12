package by.it.group451002.yeshmanski.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
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

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    #,#,

    Sample Input 2:
    short
    ports
    Sample Output 2:
    -s,~p,#,#,#,+s,

    Sample Input 3:
    distance
    editing
    Sample Output 2:
    +e,#,#,-s,#,~i,#,-c,~g,


    P.S. В литературе обычно действия редакционных предписаний обозначаются так:
    - D (англ. delete) — удалить,
    + I (англ. insert) — вставить,
    ~ R (replace) — заменить,
    # M (match) — совпадение.
*/


public class C_EditDist {

    // Основной метод для получения редакционного предписания
    String getDistanceEdinting(String one, String two) {

        // Передача строк в вспомогательный метод для вычисления редакционного предписания
        return strEditDist(one, two);
    }

    // Вспомогательный метод для вычисления редакционного предписания между двумя строками
    private static String strEditDist(String str1, String str2) {
        // Таблица для хранения минимального количества операций для каждой пары подстрок
        int[][] dist = new int[str1.length() + 1][str2.length() + 1];
        // Таблица для хранения операций (вставка, удаление, замена, копирование)
        String[][] inst = new String[str1.length() + 1][str2.length() + 1];

        // Заполнение первой строки таблицы: преобразование пустой строки в str2
        for (int i = 1; i <= str1.length(); i++) {
            dist[i][0] = i; // Все символы str1 должны быть удалены
            inst[i][0] = "," + str1.charAt(i - 1) + "-"; // Операция удаления
        }

        // Заполнение первого столбца таблицы: преобразование пустой строки в str1
        for (int i = 1; i <= str2.length(); i++) {
            dist[0][i] = i; // Все символы str2 должны быть добавлены
            inst[0][i] = "," + str2.charAt(i - 1) + "+"; // Операция вставки
        }

        // Основной цикл для заполнения таблиц dist и inst
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                // Определение стоимости замены (0, если символы совпадают, иначе 1)
                int cost = (str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1;

                // Определение минимальной стоимости операций: вставка, удаление, замена
                if (dist[i][j - 1] < dist[i - 1][j] && dist[i][j - 1] < dist[i - 1][j - 1] + cost) {
                    // Вставка символа str2[j-1] в строку str1
                    dist[i][j] = dist[i][j - 1] + 1;
                    inst[i][j] = "," + str2.charAt(j - 1) + "+";
                } else if (dist[i - 1][j] < dist[i - 1][j - 1] + cost) {
                    // Удаление символа str1[i-1]
                    dist[i][j] = dist[i - 1][j] + 1;
                    inst[i][j] = "," + str1.charAt(i - 1) + "-";
                } else {
                    // Замена символа str1[i-1] на str2[j-1] или копирование, если символы совпадают
                    dist[i][j] = dist[i - 1][j - 1] + cost;
                    inst[i][j] = (cost == 1) ? "," + str2.charAt(j - 1) + "~" : ",#";
                }
            }
        }

        // Строка для хранения редакционного предписания
        StringBuilder instruction = new StringBuilder("");
        int i = str1.length(), j = str2.length();

        // Построение последовательности операций, начиная с конца таблицы
        do {
            String someStr = inst[i][j]; // Текущая операция
            instruction.append(someStr); // Добавляем операцию в строку
            if (someStr.length() == 2 || someStr.charAt(2) == '~') {
                // Если операция замена или копирование, переходим по диагонали
                i--;
                j--;
            } else if (someStr.charAt(2) == '-') {
                // Если операция удаление, двигаемся вверх
                i--;
            } else {
                // Если операция вставка, двигаемся влево
                j--;
            }
        } while ((i != 0) || (j != 0)); // Повторяем, пока не дойдем до начала таблицы

        // Разворачиваем последовательность операций, чтобы получить корректный порядок
        return instruction.reverse().toString();
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(),scanner.nextLine()));
    }

}