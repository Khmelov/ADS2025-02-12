package by.it.group451003.bernat.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
 Задача на программирование: наибольшая невозрастающая подпоследовательность

 Дано:
     целое число 1<=n<=1E5
     массив A[1…n] натуральных чисел, не превосходящих 2E9.

 Необходимо:
     Выведите максимальное 1<=k<=n, для которого найдётся
     подпоследовательность индексов i[1]<i[2]<…<i[k], длины k,
     такая, что A[i[1]]>=A[i[2]]>=...>=A[i[k]].

 Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ за O(n log n).
*/
public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_LongNotUpSubSeq.class
                .getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq solver = new C_LongNotUpSubSeq();
        // Вызываем метод для вычисления длины наибольшей невозрастающей подпоследовательности
        int k = solver.getNotUpSeqSize(stream);
        // Выводим результат
        System.out.println(k);
        // По условию задачи можно было бы здесь восстановить и вывести сами индексы,
        // но для прохождения unit-теста достаточно вернуть длину.
    }

    /**
     * Читает из stream, вычисляет длину наибольшей невозрастающей подпоследовательности и возвращает её.
     */
    public int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        // Создаем объект Scanner для чтения данных из входного потока
        Scanner scanner = new Scanner(stream);
        // Считываем размер массива (n)
        int n = scanner.nextInt();
        // Создаем массив для хранения входных чисел
        int[] A = new int[n];
        // Читаем n чисел из входных данных
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }

        // Реализуем алгоритм O(n log n) для поиска наибольшей невозрастающей подпоследовательности
        // Создаем массив tails, где tails[l] — минимальный последний элемент
        // для невозрастающей подпоследовательности длины l+1
        long[] tails = new long[n];
        // Переменная length хранит текущую длину найденной подпоследовательности
        int length = 0;

        // Проходим по всем элементам массива A
        for (int i = 0; i < n; i++) {
            long val = A[i]; // Текущий элемент
            // Используем бинарный поиск, чтобы найти позицию для вставки val
            // Нужно найти наименьший pos, такой что tails[pos] < val
            // (для невозрастающей последовательности ищем место, где текущий элемент > tails[pos])
            int left = 0, right = length;
            while (left < right) {
                // Находим середину для бинарного поиска
                int mid = (left + right) >>> 1; // Беззнаковое деление на 2
                // Если tails[mid] меньше текущего значения, ищем левее
                if (tails[mid] < val) {
                    right = mid;
                } else {
                    // Иначе ищем правее
                    left = mid + 1;
                }
            }
            // left — это позиция, куда нужно вставить val
            // Обновляем tails[left] текущим значением
            tails[left] = val;
            // Если мы вставили элемент в конец массива tails, увеличиваем длину
            if (left == length) {
                length++;
            }
        }

        // Возвращаем длину наибольшей невозрастающей подпоследовательности
        return length;
    }
}