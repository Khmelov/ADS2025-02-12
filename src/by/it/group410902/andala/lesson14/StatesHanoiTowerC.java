package by.it.group410902.andala.lesson14;

import java.util.Arrays;
import java.util.Scanner;

public class StatesHanoiTowerC {

    // Рекурсивное решение Ханойской башни
    static void rekr(Integer[] s, Integer h, Integer[] ans, Integer fr, Integer to){
        if(h == 1){
            s[to]++;
            s[fr]--;
            // Находим максимальную высоту
            int maxHeight = Math.max(s[0], Math.max(s[1], s[2]));
            ans[maxHeight]++;
        } else {
            // Временный стержень
            int temp = 3 - fr - to;

            // Перемещаем n-1 дисков на временный
            rekr(s, h - 1, ans, fr, temp);

            // Перемещаем самый большой диск
            s[to]++;
            s[fr]--;

            // Записываем состояние
            int maxHeight = Math.max(s[0], Math.max(s[1], s[2]));
            ans[maxHeight]++;

            // Перемещаем n-1 дисков на целевой
            rekr(s, h - 1, ans, temp, to);
        }
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Integer n = in.nextInt(); // количество дисков

        // ans[i] - сколько раз встречалась высота i
        Integer[] ans = new Integer[n + 1];
        // s - диски на стержнях A, B, C
        Integer[] s = new Integer[3];

        // Начальное состояние
        s[0] = n; s[1] = 0; s[2] = 0;

        // Инициализация массива
        for(int i = 0; i <= n; ++i)
            ans[i] = 0;

        // Запуск алгоритма
        rekr(s, n, ans, 0, 2);

        // Сортировка и вывод
        Arrays.sort(ans);
        for(int i = 0; i < ans.length; ++i)
            if(ans[i] > 0) {
                System.out.print(ans[i] + " ");
            }
    }
}