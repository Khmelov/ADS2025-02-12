package by.it.group451003.kharkevich.lesson14;

import java.util.*;

public class SitesB{

    public static class DSU{
        int[] parent = new int[0];  // Массив для хранения родительских элементов

        void initialize_arr(int n){
            parent = Arrays.copyOf(parent, n);
        }

        void make_set (int v) {
            parent[v] = v;  // Элемент становится родителем самого себя
        }

        int find_set(int v) {  // Поиск корневого элемента для v
            if (v == parent[v])  // Если v - корень своего множества
                return v;
            parent[v] = find_set(parent[v]);
            return parent[v];  // Возвращаем найденный корень
        }

        void union_sets(int a, int b) {
            a = find_set(a);  // Находим корень первого множества
            b = find_set(b);  // Находим корень второго множества
            if (a != b)
                parent[b] = a;  // Объединяем путем подвешивания b к a
        }
    }

    public static int dist(int[] f, int[]s){
        return (f[0] - s[0])*(f[0] - s[0]) + (f[1] - s[1])*(f[1] - s[1]) + (f[2] - s[2])*(f[2] - s[2]);
    }

    public static void main(String[] args) {
        String s;
        Scanner in = new Scanner(System.in);
        DSU myDSU = new DSU();
        Map<String, Integer> toInd = new HashMap<>();  // Словарь для сопоставления имен сайтов с индексами
        int size = 0;

        while(true){
            s = in.next();
            if(s.equals("end"))
                break;

            int temp = -1;  // Временная переменная для поиска позиции '+'
            while(s.charAt(++temp) != '+');  // Поиск индекса символа '+' в строке
            String f = s.substring(0, temp);  // Извлечение первого сайта (до '+')
            String d = s.substring(temp + 1, s.length());  // Извлечение второго сайта (после '+')

            if(!toInd.containsKey(f)) {  // Если первый сайт еще не встречался
                toInd.put(f, size++);  // Добавляем сайт в словарь с новым индексом
                myDSU.initialize_arr(size);  // Расширяем массив DSU до нового размера
                myDSU.make_set(size - 1);  // Создаем новое множество для сайта
            }

            if(!toInd.containsKey(d)) {  // Если второй сайт еще не встречался
                toInd.put(d, size++);  // Добавляем сайт в словарь с новым индексом
                myDSU.initialize_arr(size);  // Расширяем массив DSU до нового размера
                myDSU.make_set(size - 1);  // Создаем новое множество для сайта
            }

            myDSU.union_sets(toInd.get(f), toInd.get(d));  // Объединяем множества связанных сайтов
        }

        Map<Integer, Integer> cnt = new HashMap<>();  // Словарь для сопоставления корней с индексами в ans
        int free = 0;
        int[] ans = new int[size];  // Массив для хранения размеров кластеров

        for(int i = 0; i < size; ++i) {
            int p = myDSU.find_set(i);  // Находим корневой элемент для текущего сайта

            if(cnt.containsKey(p))  // Если кластер с таким корнем уже существует
                ans[cnt.get(p)]++;  // Увеличиваем счетчик размера кластера
            else {
                ans[free] = 1;  // Создаем новый кластер с размером 1
                cnt.put(p, free++);  // Запоминаем индекс кластера для данного корня
            }
        }

        System.out.println();

        ans = Arrays.copyOf(ans, cnt.size());

        Arrays.sort(ans);  // Сортировка размеров кластеров по возрастанию

        for(int i = ans.length - 1; i >= 0;) {
            System.out.print(ans[i--]);
            System.out.print(' ');
        }
    }
}