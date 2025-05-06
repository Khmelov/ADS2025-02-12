package by.it.group451002.shandr.lesson01;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555L;  // большое число
        int m = 1000;     // делитель
        System.out.printf("fasterC(%d, %d)=%d \n\t time=%d ms\n\n", n, m, fibo.fasterC(n, m), fibo.time());
    }
    //lf
    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n <= 1) {
            return n;
        }

        // Находим длину последовательности Пизано для числа m
        int pisanoLength = getPisanoPeriod(m);

        // Сокращаем n по длине Пизано
        n = n % pisanoLength;

        // Теперь считаем n-е число Фибоначчи по модулю m
        return fibModulo(n, m);
    }

    // Метод для нахождения длины последовательности Пизано
    private int getPisanoPeriod(int m) {
        long prev = 0, curr = 1;
        for (int i = 0; i < m * 6; i++) { // Длина Пизано не превышает 6*m
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
            if (prev == 0 && curr == 1) {
                return i + 1; // Длина цикла
            }
        }
        return m; // Вряд ли понадобится, но добавим для надежности
    }

    // Метод для вычисления n-го числа Фибоначчи по модулю m
    private long fibModulo(long n, int m) {
        if (n <= 1) {
            return n;
        }
        long prev = 0, curr = 1;
        for (long i = 2; i <= n; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }
        return curr;
    }
}

