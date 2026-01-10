package by.it.group451004.kaberdin.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<n, для которых A[i]>A[j].

    (Такая пара элементов называется инверсией массива.
    Количество инверсий в массиве является в некотором смысле
    его мерой неупорядоченности: например, в упорядоченном по неубыванию
    массиве инверсий нет вообще, а в массиве, упорядоченном по убыванию,
    инверсию образуют каждые (т.е. любые) два элемента.
    )

Sample Input:
5
2 3 9 2 9
Sample Output:
2

Головоломка (т.е. не обязательно).
Попробуйте обеспечить скорость лучше, чем O(n log n) за счет многопоточности.
Докажите рост производительности замерами времени.
Большой тестовый массив можно прочитать свой или сгенерировать его программно.
*/

public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        // InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        InputStream stream = C_GetInversions.class.getResourceAsStream("test_array_100k.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream); // Вычисляем количество инверсий
        System.out.print(result); // Выводим результат
    }

    static long result = 0; // Глобальная переменная для подсчёта инверсий

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt(); // Считываем размер массива
        int[] a = new int[n]; // Создаём массив
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt(); // Заполняем массив числами
        }
        long startTime = System.currentTimeMillis(); // Засекаем время начала
        // Однопоточная версия
        mergeSort2(a, 0, n - 1);
        // Многопоточная версия
        // mergeSort(a, 0, n - 1);
        long finishTime = System.currentTimeMillis(); // Засекаем время окончания
        // Выводим информацию о времени выполнения
        System.out.printf("StartTime=%d\nFinishTime=%d\nDelta=%d\n", startTime, finishTime, finishTime - startTime);
        System.out.print(result); // Выводим количество инверсий
        return (int) result;
    }

    void mergeSort2(int[] a, int minA, int maxA)  {
        // Если в подмассиве 1 или 2 элемента, сортируем их вручную
        if (maxA - minA < 2) {
            if (a[maxA] < a[minA]) {
                // Меняем элементы местами и увеличиваем счётчик инверсий
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
                result++;
            }
        } else {
            // Рекурсивно сортируем левую и правую половины
            int mid = (maxA + minA) / 2;
            mergeSort2(a, minA, mid);
            mergeSort2(a, mid + 1, maxA);
            // Сливаем отсортированные половины
            mergeSubarrays(a, minA, mid, maxA);
        }
    }

    static int threadCount = 0; // Счётчик потоков

    // Многопоточная версия сортировки слиянием
    void mergeSort(int[] a, int minA, int maxA) throws InterruptedException {
        if (maxA - minA < 2) {
            // Обработка маленьких подмассивов
            if (a[maxA] < a[minA]) {
                int temp = a[minA];
                a[minA] = a[maxA];
                a[maxA] = temp;
                result++;
            }
        } else {
            int mid = (maxA + minA) / 2;

            // Если потоков меньше 17, создаём новые потоки
            if (threadCount < 17) {
                // Левый подмассив в отдельном потоке
                Thread left = new Thread(() -> {
                    try {
                        mergeSort(a, minA, mid);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                // Правый подмассив в отдельном потоке
                Thread right = new Thread(() -> {
                    try {
                        mergeSort(a, mid + 1, maxA);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
                threadCount++;
                left.start();
                threadCount++;
                right.start();

                left.join(); // Ожидаем завершения левого потока
                threadCount--;
                right.join(); // Ожидаем завершения правого потока
                threadCount--;
            } else {
                // Если потоков слишком много, работаем в текущем потоке
                mergeSort(a, minA, mid);
                mergeSort(a, mid + 1, maxA);
            }
            // Сливаем отсортированные подмассивы
            mergeSubarrays(a, minA, mid, maxA);
        }
    }

    // Функция для слияния двух отсортированных подмассивов с подсчётом инверсий
    void mergeSubarrays(int[] source, int minA, int maxA, int maxB) {
        int ai = minA; // Указатель на начало левого подмассива
        int bi = maxA + 1; // Указатель на начало правого подмассива
        int length = maxB - minA + 1; // Длина результирующего массива
        int[] buf = new int[length]; // Временный буфер для слияния
        int index = 0; // Индекс в буфере

        // Пока есть элементы в обоих подмассивах
        while (ai <= maxA && bi <= maxB) {
            if (source[ai] <= source[bi]) {
                // Элемент из левого подмассива меньше - копируем его
                buf[index++] = source[ai++];
            } else {
                // Элемент из правого подмассива меньше - копируем его
                buf[index++] = source[bi++];
                // Все оставшиеся элементы в левом подмассиве образуют инверсии
                result += (maxA - ai + 1);
            }
        }
        // Копируем оставшиеся элементы из левого подмассива
        while (ai <= maxA) {
            buf[index++] = source[ai++];
        }
        // Копируем оставшиеся элементы из правого подмассива
        while (bi <= maxB) {
            buf[index++] = source[bi++];
        }
        // Копируем отсортированные данные из буфера обратно в исходный массив
        System.arraycopy(buf, 0, source, minA, length);
    }
}