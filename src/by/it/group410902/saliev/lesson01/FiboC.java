package by.it.group410902.saliev.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации

        // 1. Найдём период Пизано — длину повторяющейся последовательности остатков
        int[] pisano = new int[m * 6]; // максимум длины периода — 6*m
        pisano[0] = 0;
        pisano[1] = 1;

        int period = 0;
        for (int i = 2; i < pisano.length; i++) {
            pisano[i] = (pisano[i - 1] + pisano[i - 2]) % m;
            if (pisano[i - 1] == 0 && pisano[i] == 1) {
                period = i - 1;
                break;
            }
        }

        // 2. Теперь находим остаток от деления n на длину периода
        int index = (int)(n % period);

        // 3. Возвращаем нужное значение Фибоначчи по модулю m
        return pisano[index];
    }
}
