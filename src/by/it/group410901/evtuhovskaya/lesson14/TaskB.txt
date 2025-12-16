Создайте класс SitesB с методом main.

Пусть у нас есть набор связанных через взаимные гиперссылки сайтов,
и мы хотим разбить их на кластеры в которых можно по ссылкам дойти
до любого сайта этого кластера.

Цель: объединение связанных сайтов в кластеры.

Для кластеризации используем структуру данных DSU,
причем эвристик должно быть две:
1. по рангу или размеру поддерева
2. по сокращению пути поддерева

С консоли вводится в каждой новой строке пара связанных сайтов.
Допускается связь сайта с самим собой. Направление ссылок для простоты не учитывается.
Пара объединяется символом "+", а весь ввод завершается строкой "end"
Нужно вывести на консоль число сайтов в полученных кластерах (в порядке возрастания).
Сложность DSU должна быть не выше, чем N log*(N), где log*(N) - итерированный логарифм.

Пример:

Ввод:
java.mobile+science.org
test.ru+application.info
hello.info+world.org
hello.mobile+java.mobile
course.mobile+computer.net
application.org+application.net
application.info+test.app
application.org+hello.mobile
course.io+test.io
computer.app+course.com
end

Вывод:
5 3 2 2 2 2

Объяснение:
Группа (5): [application.org, java.mobile, science.org, hello.mobile, application.net]
Группа (3): [test.app, application.info, test.ru]
Группа (2): [hello.info, world.org]
Группа (2): [computer.net, course.mobile]
Группа (2): [test.io, course.io]
Группа (2): [computer.app, course.com]


