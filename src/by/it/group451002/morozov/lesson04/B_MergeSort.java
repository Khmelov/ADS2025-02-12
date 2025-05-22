package by.it.group451002.morozov.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Реализуйте сортировку слиянием для одномерного массива.
Сложность алгоритма должна быть не хуже, чем O(n log n)

Первая строка содержит число 1<=n<=10000,
вторая - массив A[1…n], содержащий натуральные числа, не превосходящие 10E9.
Необходимо отсортировать полученный массив.

Sample Input:
5
2 3 9 2 9
Sample Output:
2 2 3 9 9
*/
public class B_MergeSort {

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = B_MergeSort.class.getResourceAsStream("dataB.txt");
        B_MergeSort instance = new B_MergeSort();
        //long startTime = System.currentTimeMillis();
        int[] result = instance.getMergeSort(stream);
        //long finishTime = System.currentTimeMillis();
        for (int index : result) {
            System.out.print(index + " ");
        }
    }
    
    // процедура для слияния упорядоченных массивов
    int[] merge(int[] a1, int a2[]) {
    	int[] res = new int[a1.length+a2.length];
    	//int minLen = a1.length < a2.length ? a1.length : a2.length;
    	int f1 = 0;
    	int f2 = 0;
    	int i = 0;
    	while ((f1 != a1.length) && (f2 != a2.length)) {
    		if (a1[f1] < a2[f2]) {
    			res[i] = a1[f1];
    			f1++;
    		} else {
    			res[i] = a2[f2];
    			f2++;
    		}
    		i++;
    	}
    	if (f1 == a1.length) {
    		
    		// "выталкиваем" элементы из a2
    		for (int j = f2; j < a2.length; j++) {
    			res[i] = a2[j];
    			i++;
    		}
    	} else {
    		// "выталкиваем" элементы из a1
    		for (int j = f1; j < a1.length; j++) {
    			res[i] = a1[j];
    			i++;
    		}
    	}
    	return res;
    }

    
    // рекурсивная процедура для сортировки слиянием
    int[] mergeSort(int[] arr, int l, int r) {
    	if (l < r) {
    		int c = (l+r)/2;
    		return merge(mergeSort(arr, l, c), mergeSort(arr, c+1, r));
    	}
    	return new int[]{arr[l]};
    }

    int[] getMergeSort(InputStream stream) throws FileNotFoundException {
        //подготовка к чтению данных
        Scanner scanner = new Scanner(stream);
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

        //размер массива
        int n = scanner.nextInt();
        //сам массив
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
            System.out.println(a[i]);
        }
        

        // тут ваше решение (реализуйте сортировку слиянием)
        // https://ru.wikipedia.org/wiki/Сортировка_слиянием
        
        a = mergeSort(a, 0, n-1);

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return a;
    }


}