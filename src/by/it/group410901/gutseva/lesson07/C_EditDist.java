package by.it.group410901.gutseva.lesson07;

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

    String getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Длины входных строк
        int m = one.length();
        int n = two.length();

        // Матрица dp для хранения минимального количества операций редактирования
        // dp[i][j] - расстояние между подстроками one[0..i-1] и two[0..j-1]
        int[][] dp = new int[m+1][n+1];

        // Матрица operations для хранения последней операции при переходе
        // operations[i][j] содержит:
        // '#' - символы совпадают (копирование)
        // '-' - удаление символа из первой строки
        // '+' - вставка символа из второй строки
        // '~' - замена символа первой строки на символ второй
        char[][] operations = new char[m+1][n+1];

        // Инициализация базовых случаев:
        // Преобразование строки длины i в пустую строку требует i удалений
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;  // Стоимость - i операций удаления
            operations[i][0] = '-';  // Помечаем как удаления
        }

        // Преобразование пустой строки в строку длины j требует j вставок
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;  // Стоимость - j операций вставки
            operations[0][j] = '+';  // Помечаем как вставки
        }

        // Заполнение матрицы dp и operations:
        // Для каждой пары подстрок one[0..i-1] и two[0..j-1]
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если текущие символы совпадают
                if (one.charAt(i-1) == two.charAt(j-1)) {
                    // Берем значение из диагонали без увеличения стоимости
                    dp[i][j] = dp[i-1][j-1];
                    operations[i][j] = '#';  // Операция - копирование
                }
                else {
                    // Вычисляем стоимости трех возможных операций:
                    int delete = dp[i-1][j];    // Удаление символа из one
                    int insert = dp[i][j-1];    // Вставка символа из two
                    int replace = dp[i-1][j-1]; // Замена символа

                    // Выбираем операцию с минимальной стоимостью
                    if (delete <= insert && delete <= replace) {
                        dp[i][j] = delete + 1;  // Удаление +1 к стоимости
                        operations[i][j] = '-';  // Помечаем как удаление
                    }
                    else if (insert <= delete && insert <= replace) {
                        dp[i][j] = insert + 1;  // Вставка +1 к стоимости
                        operations[i][j] = '+';  // Помечаем как вставку
                    }
                    else {
                        dp[i][j] = replace + 1; // Замена +1 к стоимости
                        operations[i][j] = '~'; // Помечаем как замену
                    }
                }
            }
        }

        // Восстановление последовательности операций
        // Восстановление последовательности операций:
        // Начинаем с конца строк (правый нижний угол матрицы)
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;

        while (i > 0 || j > 0) {
            char op = operations[i][j];

            // В зависимости от операции:
            switch (op) {
                case '#':  // Копирование (символы совпадают)
                    sb.insert(0, "#,");  // Добавляем операцию
                    i--; j--;  // Перемещаемся по диагонали
                    break;

                case '-':  // Удаление символа из one
                    sb.insert(0, "-" + one.charAt(i-1) + ",");  // Формат: -<символ>
                    i--;  // Перемещаемся вверх
                    break;

                case '+':  // Вставка символа из two
                    sb.insert(0, "+" + two.charAt(j-1) + ",");  // Формат: +<символ>
                    j--;  // Перемещаемся влево
                    break;

                case '~':  // Замена символа
                    sb.insert(0, "~" + two.charAt(j-1) + ",");  // Формат: ~<новый символ>
                    i--; j--;  // Перемещаемся по диагонали
                    break;
            }
        }

        // Получаем итоговую строку с операциями
        String result = sb.toString();
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
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