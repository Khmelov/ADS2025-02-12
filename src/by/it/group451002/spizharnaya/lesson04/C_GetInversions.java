package by.it.group451002.spizharnaya.lesson04;

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
        InputStream stream = C_GetInversions.class.getResourceAsStream("dataC.txt");
        C_GetInversions instance = new C_GetInversions();
        //long startTime = System.currentTimeMillis();
        int result = instance.calc(stream);
        //long finishTime = System.currentTimeMillis();
        System.out.print(result);
    }

    int calc(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!
        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }
        inverse = 0;
        //!!!!!!!!!!!!!!!!!!!!!!!!     тут ваше решение   !!!!!!!!!!!!!!!!!!!!!!!!

        MergeSort(a,0,n-1);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return inverse;
    }

    private static int inverse;

    private static void MergeSort(int[] arr, int left, int right){
        if (left < right) {
            int mid = (left + right) / 2;
            MergeSort(arr, left, mid);
            MergeSort(arr, mid + 1, right);
            Merge(arr,left,mid,right);
        }
    }

    private static void Merge(int[] arr, int left, int mid, int right){
        int ind1 = left;
        int ind2 = mid+1;
        //временный массив для слияния двух частей в него, в конце будет переписан в arr
        int[] newarr = new int[right-left+1];
        int indnew = 0;
        while ((ind1<=mid)&&(ind2<=right)){    //пока есть элементы в обеих объединяемых частях
            if (arr[ind1]<=arr[ind2]){          //берем меньший из двух частей
                newarr[indnew]=arr[ind1];
                ind1++;
            }
            else{
                //каждый раз, когда элемент из правой половины меньше элемента из левой половины
                //счетчик инверсий увеличивается на количество оставшихся элементов левой части
                inverse = inverse + mid-ind1+1;
                newarr[indnew]=arr[ind2];
                ind2++;
            }
            indnew++;
        }
        //теперь сравнивать не надо, добавляем все оставшиеся в одной из частей элементы
        while(ind1<=mid){   //если остались элементы в левой части
            newarr[indnew]=arr[ind1];
            ind1++; indnew++;
        }
        while(ind2<=right){   //если остались элементы в правой части
            newarr[indnew]=arr[ind2];
            ind2++; indnew++;
        }
        //записываем отсортированный массив в тот который нужно было сортировать
        for(int i=0;i<newarr.length;i++){
            arr[i+left]=newarr[i];
        }
    }
}
