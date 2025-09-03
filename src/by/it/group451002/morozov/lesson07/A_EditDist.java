package by.it.group451002.morozov.lesson07;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Задача на программирование: расстояние Левенштейна
    https://ru.wikipedia.org/wiki/Расстояние_Левенштейна
    http://planetcalc.ru/1721/

Дано:
    Две данных непустые строки длины не более 100, содержащие строчные буквы латинского алфавита.

Необходимо:
    Решить задачу МЕТОДАМИ ДИНАМИЧЕСКОГО ПРОГРАММИРОВАНИЯ
    Рекурсивно вычислить расстояние редактирования двух данных непустых строк

    Sample Input 1:
    ab
    ab
    Sample Output 1:
    0

    Sample Input 2:
    short
    ports
    Sample Output 2:
    3

    Sample Input 3:
    distance
    editing
    Sample Output 3:
    5

*/

public class A_EditDist {


    int getDistanceEdinting(String one, String two) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!     НАЧАЛО ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!

    	int len1 = one.length();
    	int len2 = two.length();
    	
    	// Создание таблицы
        int[][] distTable = new int[len1+1][len2+1];
        
        for (int i = 0; i < len1+1; i++) {
        	for (int j = 0; j < len2+1; j++) {
                distTable[i][j] = Integer.MAX_VALUE;
            }
        }
        

        EditDist(distTable, one, two, len1, len2);
        
        int result = distTable[len1][len2];

        //!!!!!!!!!!!!!!!!!!!!!!!!!     КОНЕЦ ЗАДАЧИ     !!!!!!!!!!!!!!!!!!!!!!!!!
        return result;
    }
    
    
    
    int EditDist(int[][] d, String s1, String s2, int i, int j) {
    	if (d[i][j] == Integer.MAX_VALUE) {
    		if (i == 0) {
    			d[i][j] = j;
    		} else if (j == 0) {
    			d[i][j] = i;
    		} else {
    			int ins = EditDist(d, s1, s2, i, j-1) + 1;
    			int del = EditDist(d, s1, s2, i-1, j) + 1;
    			int sub = EditDist(d, s1, s2, i-1, j-1)+(s1.charAt(i-1)!=s2.charAt(j-1) ? 1 : 0);
    			
    			// Находим минимум
    			int minTemp = ins < del ? ins : del;
    			d[i][j] = minTemp < sub ? minTemp : sub;
    		}
    	}
    	return d[i][j];
    }


    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_EditDist.class.getResourceAsStream("dataABC.txt");
        A_EditDist instance = new A_EditDist();
        Scanner scanner = new Scanner(stream);
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
        System.out.println(instance.getDistanceEdinting(scanner.nextLine(), scanner.nextLine()));
    }
}
