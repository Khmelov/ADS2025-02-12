package by.it.group451003.galuzo.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class B_EditDist {

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        // Получаем длины обеих строк
        int m = one.length();
        int n = two.length();

        // Создаем матрицу для хранения расстояний
        int[][] dp = new int[m + 1][n + 1];

        // Инициализация базовых случаев:
        // Для преобразования в пустую строку нужно удалить все символы
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        // Для преобразования из пустой строки нужно вставить все символы
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Заполняем матрицу итеративно
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Если символы совпадают, берем значение по диагонали
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Если символы разные, выбираем минимальное из трех вариантов:
                    // 1. Вставка (значение слева + 1)
                    // 2. Удаление (значение сверху + 1)
                    // 3. Замена (значение по диагонали + 1)
                    dp[i][j] = 1 + Math.min(Math.min(
                                    dp[i][j - 1],   // Вставка
                                    dp[i - 1][j]),  // Удаление
                            dp[i - 1][j - 1] // Замена
                    );
                }
            }
        }

        // Результат находится в правом нижнем углу матрицы
        int result = dp[m][n];
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_EditDist.class.getResourceAsStream("dataABC.txt");
        B_EditDist instance = new B_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}