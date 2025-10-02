package by.it.group410902.andala.lesson01;

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

    // Метод для быстрого вычисления F(n) % m с использованием Пизано-периода
    long fasterC(long n, int m) {
        // Массив для хранения значений Пизано-последовательности
        // Максимально возможная длина Пизано-периода — 6*m
        int[] pisano = new int[m * 6];

        // Начальные значения последовательности Фибоначчи
        pisano[0] = 0;
        pisano[1] = 1;

        // Переменная для хранения длины найденного периода
        int period = 0;

        // Строим последовательность Фибоначчи по модулю m, пока не найдём начало периода (0, 1)
        for (int i = 2; i < pisano.length; i++) {
            // Формула Фибоначчи по модулю m
            pisano[i] = (pisano[i - 1] + pisano[i - 2]) % m;

            // Если встретили начало нового периода (0, 1), сохраняем длину периода
            if (pisano[i - 1] == 0 && pisano[i] == 1) {
                period = i - 1; // длина периода — всё до начала нового цикла
                break;
            }
        }

        // Остаток от деления n на длину периода
        // Поскольку последовательность периодична, достаточно вычислить F(n % period)
        int remainder = (int)(n % period);

        // Возвращаем соответствующее значение из Пизано-последовательности
        return pisano[remainder];
    }

}
