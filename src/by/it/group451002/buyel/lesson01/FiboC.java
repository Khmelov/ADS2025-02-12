package by.it.group451002.buyel.lesson01;

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

        // Инициализация переменных для циклов
        int Prev = 0, Curr = 1, Amount = 0, Next;

        // Рассчитывает период Пизано (Amount)
        do{
            Next = (Curr + Prev) % m;
            Prev = Curr;
            Curr = Next;
            Amount++;
        } while (!(Prev == 0 && Curr == 1));

        // Наименьший номер числа с эквивалентным остатком
        long Num = n % Amount;

        if (Num == 0) return 0;
        if (Num == 1) return 1;

        // Рассчитывает это число
        for (long i = 1; i < Num; i++){
            Next = (Curr + Prev) % m;
            Prev = Curr;
            Curr = Next;
        }

        return Curr % m;
    }
}
