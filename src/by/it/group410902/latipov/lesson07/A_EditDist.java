package by.it.group410902.latipov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class A_EditDist {

    private Integer[][] memo;

    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        memo = new Integer[one.length()][two.length()];
        return calculateDistance(one, two, one.length() - 1, two.length() - 1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
    }

    private int calculateDistance(String s1, String s2, int i, int j) {
        // Базовые случаи
        if (i < 0) return j + 1; // если первая строка пустая, нужно вставить все символы второй
        if (j < 0) return i + 1; // если вторая строка пустая, нужно удалить все символы первой

        // Проверяем, не вычисляли ли мы уже это состояние
        if (memo[i][j] != null) {
            return memo[i][j];
        }

        // Если символы совпадают, переходим к следующим
        if (s1.charAt(i) == s2.charAt(j)) {
            memo[i][j] = calculateDistance(s1, s2, i - 1, j - 1);
        } else {
            // Рассматриваем три операции:
            // 1. Удаление (из первой строки)
            int delete = calculateDistance(s1, s2, i - 1, j) + 1;
            // 2. Вставка (во вторую строку)
            int insert = calculateDistance(s1, s2, i, j - 1) + 1;
            // 3. Замена
            int replace = calculateDistance(s1, s2, i - 1, j - 1) + 1;

            // Выбираем минимальную стоимость
            memo[i][j] = Math.min(Math.min(delete, insert), replace);
        }

        return memo[i][j];
    }

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}