package by.it.group410901.volkov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class A_EditDist {
    private Map<String, Integer> memo = new HashMap<>();

    int getDistanceEdinting(String one, String two) {
        // Очищаем кэш перед новым вычислением
        memo.clear();
        return calculateDistance(one, two, one.length(), two.length());
    }

    private int calculateDistance(String one, String two, int i, int j) {
        // Если результат уже вычислен, возвращаем его из кэша
        String key = i + "," + j;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }

        int result;
        // Базовые случаи:
        // Если первая строка пустая, нужно вставить все символы второй строки
        if (i == 0) {
            result = j;
        }
        // Если вторая строка пустая, нужно удалить все символы первой строки
        else if (j == 0) {
            result = i;
        }
        // Если последние символы совпадают, переходим к следующим символам
        else if (one.charAt(i - 1) == two.charAt(j - 1)) {
            result = calculateDistance(one, two, i - 1, j - 1);
        }
        else {
            // Иначе выбираем минимальное из трех возможных операций:
            // 1. Удаление (удаляем символ из первой строки)
            int delete = calculateDistance(one, two, i - 1, j);
            // 2. Вставка (вставляем символ во вторую строку)
            int insert = calculateDistance(one, two, i, j - 1);
            // 3. Замена (заменяем символ в первой строке на символ из второй)
            int replace = calculateDistance(one, two, i - 1, j - 1);

            result = 1 + Math.min(Math.min(delete, insert), replace);
        }

        // Сохраняем результат в кэш перед возвратом
        memo.put(key, result);
        return result;
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