package by.it.group410902.grigorev.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class C_EditDist {
    // Метод вычисляет расстояние редактирования между двумя строками и возвращает последовательность операций
    String getDistanceEdinting(String one, String two) {
        int m = one.length(); // Длина первой строки
        int n = two.length(); // Длина второй строки

        // Динамическая таблица для хранения минимального количества операций
        int[][] dp = new int[m + 1][n + 1];

        // Таблица для хранения самих операций редактирования ('+', '-', '~', '#')
        char[][] operations = new char[m + 1][n + 1];

        // Заполняем таблицу динамического программирования и таблицу операций
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0) {

                    dp[i][j] = j;
                    operations[i][j] = '+';
                } else if (j == 0) {

                    dp[i][j] = i;
                    operations[i][j] = '-';
                } else if (one.charAt(i - 1) == two.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1];
                    operations[i][j] = '#';
                } else {

                    int insert = dp[i][j - 1];
                    int delete = dp[i - 1][j];
                    int replace = dp[i - 1][j - 1];

                    // Определяем наиболее выгодную операцию
                    if (insert <= delete && insert <= replace) {
                        dp[i][j] = 1 + insert;
                        operations[i][j] = '+';
                    } else if (delete <= insert && delete <= replace) {
                        dp[i][j] = 1 + delete;
                        operations[i][j] = '-';
                    } else {
                        dp[i][j] = 1 + replace;
                        operations[i][j] = '~';
                    }
                }
            }
        }

        // Восстанавливаем последовательность операций
        StringBuilder sb = new StringBuilder();
        int i = m, j = n;
        while (i > 0 || j > 0) {
            char op = operations[i][j];
            switch (op) {
                case '#':
                    sb.insert(0, "#,"); // Символ совпадает, операция не требуется
                    i--;
                    j--;
                    break;
                case '+':
                    sb.insert(0, "+" + two.charAt(j - 1) + ","); // Вставка символа
                    j--;
                    break;
                case '-':
                    sb.insert(0, "-" + one.charAt(i - 1) + ","); // Удаление символа
                    i--;
                    break;
                case '~':
                    sb.insert(0, "~" + two.charAt(j - 1) + ","); // Замена символа
                    i--;
                    j--;
                    break;
            }
        }

        return sb.toString(); // Возвращаем строку операций редактирования
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataABC.txt"
        InputStream stream = C_EditDist.class.getResourceAsStream("dataABC.txt");
        C_EditDist instance = new C_EditDist();
        Scanner scanner = new Scanner(stream);

        // Выводим последовательность операций для трех пар строк
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
