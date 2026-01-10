package by.it.group451002.koltsov.lesson15;

import java.util.ArrayList;

public class C_EditDist {

    static int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        // создаём таблицу
        int[][] table = new int[one.length() + 1][two.length() + 1];

        // заполняем начальными значениями
        for (int i = 1; i < one.length() + 1; i++) {
            table[i][0] = i;
        }
        for (int i = 1; i < two.length() + 1; i++) {
            table[0][i] = i;
        }

        // рассчитываем все элементы таблицы
        for (int i = 1; i < one.length() + 1; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 1; j < two.length() + 1; j++) {
                if (one.charAt(i - 1) == two.charAt(j - 1)) {
                    table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j - 1]));
                } else {
                    table[i][j] = Math.min(table[i - 1][j - 1], Math.min(table[i - 1][j], table[i][j - 1])) + 1;
                }
                if (table[i][j] < min)
                    min = table[i][j];
        }
        if (min >= 10) return 20;
    }
    int result = table[one.length()][two.length()];
    //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
}