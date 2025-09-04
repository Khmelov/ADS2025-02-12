package by.it.group451003.burshtyn.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Рассчитать число инверсий одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо посчитать число пар индексов 1<=i<j<=n, для которых A[i]>A[j].

Sample Input:
5
2 3 9 2 9
Sample Output:
2
*/
public class C_GetInversions {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        int result = instance.calc(stream);
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        Scanner scanner = new Scanner(stream);
        // Чтение входа
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        // Вспомогательный массив для слияния
        int[] tmp = new int[n];
        // Запускаем модифицированный merge sort для подсчёта инверсий
        long invCount = mergeSortCount(a, tmp, 0, n - 1);
        return (int) invCount;
    }

    /**
     * Рекурсивная сортировка слиянием с подсчётом инверсий.
     * @return количество инверсий в a[left..right]
     */
    private long mergeSortCount(int[] a, int[] tmp, int left, int right) {
        if (left >= right) {
            return 0;
        }
        int mid = left + (right - left) / 2;
        long count = 0;
        // Инверсии в левой половине
        count += mergeSortCount(a, tmp, left, mid);
        // Инверсии в правой половине
        count += mergeSortCount(a, tmp, mid + 1, right);
        // Инверсии между половинами при слиянии
        count += mergeAndCount(a, tmp, left, mid, right);
        return count;
    }

    /**
     * Сливает a[left..mid] и a[mid+1..right], подсчитывая перекрёстные инверсии:
     * когда элемент из правой половины идёт раньше элемента из левой.
     */
    private long mergeAndCount(int[] a, int[] tmp, int left, int mid, int right) {
        int i = left;      // указатель левой половины
        int j = mid + 1;   // указатель правой половины
        int k = left;      // указатель для tmp
        long inversions = 0;

        while (i <= mid && j <= right) {
            if (a[i] <= a[j]) {
                tmp[k++] = a[i++];
            } else {
                // Все оставшиеся элементы a[i..mid] будут > a[j],
                // поэтому для каждого из них считается инверсия с a[j].
                inversions += (mid - i + 1);
                tmp[k++] = a[j++];
            }
        }
        // Копируем оставшиеся элементы
        while (i <= mid) {
            tmp[k++] = a[i++];
        }
        while (j <= right) {
            tmp[k++] = a[j++];
        }
        // Переносим назад в a
        for (int p = left; p <= right; p++) {
            a[p] = tmp[p];
        }
        return inversions;
    }
}
