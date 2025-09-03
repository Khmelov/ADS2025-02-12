package by.it.group451002.andreev.lesson06;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Arrays;

/**
 * Класс C_LongNotUpSubSeq реализует поиск наибольшей невозрастающей подпоследовательности (LNDS)
 * с использованием динамического программирования и двоичного поиска.
 */
public class C_LongNotUpSubSeq {

    public static void main(String[] args) throws FileNotFoundException {
        // Загружаем входные данные из файла "dataC.txt"
        InputStream stream = C_LongNotUpSubSeq.class.getResourceAsStream("dataC.txt");
        C_LongNotUpSubSeq instance = new C_LongNotUpSubSeq();

        // Выполняем поиск длины наибольшей невозрастающей подпоследовательности и выводим результат
        instance.getNotUpSeqSize(stream);
    }

    /**
     * Метод getNotUpSeqSize выполняет поиск наибольшей невозрастающей подпоследовательности.
     * @param stream входной поток данных
     * @return длина наибольшей невозрастающей подпоследовательности
     * @throws FileNotFoundException если файл не найден
     */
    int getNotUpSeqSize(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);

        // Читаем размер массива
        int n = scanner.nextInt();
        int[] a = new int[n];

        // Заполняем массив входными данными
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Массив tailIndices хранит индексы последних элементов возможных подпоследовательностей
        int[] tailIndices = new int[n];

        // Массив prev используется для восстановления последовательности
        int[] prev = new int[n];

        int length = 1; // Длина наибольшей невозрастающей подпоследовательности
        Arrays.fill(prev, -1); // Изначально нет предыдущих элементов

        tailIndices[0] = 0; // Начинаем с первого элемента

        for (int i = 1; i < n; i++) {
            if (a[i] <= a[tailIndices[length - 1]]) {
                // Если текущий элемент a[i] может быть продолжением текущей последовательности
                prev[i] = tailIndices[length - 1]; // Запоминаем предшественника
                tailIndices[length++] = i; // Увеличиваем длину последовательности
            } else {
                // Двоичный поиск первого места, где a[i] < a[tailIndices[mid]]
                int left = 0, right = length - 1;
                while (left <= right) {
                    int mid = (left + right) / 2;
                    if (a[tailIndices[mid]] >= a[i]) {
                        left = mid + 1;
                    } else {
                        right = mid - 1;
                    }
                }
                // Обновляем tailIndices[left] новым индексом
                tailIndices[left] = i;
                if (left > 0) {
                    prev[i] = tailIndices[left - 1]; // Запоминаем предшественника
                }
            }
        }

        // Восстанавливаем последовательность индексов
        int[] resultIndices = new int[length];
        int idx = tailIndices[length - 1];
        for (int i = length - 1; i >= 0; i--) {
            resultIndices[i] = idx + 1; // Переводим индексацию в формат "с 1"
            idx = prev[idx];
        }

        // Вывод результата: сначала длина подпоследовательности, затем её индексы
        System.out.println(length);
        for (int i = 0; i < length; i++) {
            System.out.print(resultIndices[i] + " ");
        }

        return length;
    }
}
