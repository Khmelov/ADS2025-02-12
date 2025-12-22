package by.it.a_khmelev.lesson09;


import by.it.HomeWork;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings("all")

//поставьте курсор на следующую строку и нажмите Ctrl+Shift+F10
//для корректной сборки теста добавьте библиотеку init.jar в проект (она находится в корне)
public class Test_Part2_Lesson09Test extends HomeWork {

    private static final int RND_SEED = 123;
    public static final int INVOCATION_COUNT_PER_METHOD = 10;
    public static final int MAX_VALUE = 100;
    Random rnd = new Random(RND_SEED);
    private List<Integer> eObject;
    private List<Integer> aObject;

    private Map<Method, String> cache = new HashMap<>();

    @Test(timeout = 5000)
    public void testTaskA() throws Exception {
        String[] methods = """
                toString()
                add(Object)
                remove(int)
                size()
                """.split("\\s+");
        randomCheck("ListA", methods);
    }

    @Test(timeout = 5000)
    public void testTaskB() throws Exception {
        String[] methods = """
                toString()
                add(Object)
                remove(int)
                size()
                                
                remove(Object)
                add(int,Object)
                remove(Object)
                set(int,Object)
                isEmpty()
                clear()
                indexOf(Object)
                get(int)
                contains(Object)
                lastIndexOf(Object)
                """.split("\\s+");
        randomCheck("ListB", methods);
    }

    @Test(timeout = 5000)
    public void testTaskC() throws Exception {
        String[] methods = """
                toString()
                add(Object)
                remove(int)
                size()
                                
                remove(Object)
                add(int,Object)
                remove(Object)
                set(int,Object)
                isEmpty()
                clear()
                indexOf(Object)
                get(int)
                contains(Object)
                lastIndexOf(Object)
                                
                containsAll(Collection)
                addAll(Collection)
                addAll(int,Collection)                
                removeAll(Collection)
                retainAll(Collection)
                retainAll(Collection)
                """.split("\\s+");
        randomCheck("ListC", methods);
    }

    private void randomCheck(String className, String... methods) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        TreeSet<String> methodNames = new TreeSet<>(Arrays.asList(methods));
        Class<?> aClass = findClass(className);
        checkStructure(aClass);
        System.out.printf("\nStart test methods in class %s%n",aClass);
        eObject = ArrayList.class.getDeclaredConstructor().newInstance();
        aObject = (List<Integer>) aClass.getDeclaredConstructor().newInstance();

        Map<String, Method> methodsA = fill(aClass, methodNames);
        Map<String, Method> methodsE = fill(eObject.getClass(), methodNames);

        assertEquals("Not found methods for test in:\n" + getSignatures(ArrayList.class), methodNames.size(), methodsA.size());

        for (int testNumber = 0; testNumber < INVOCATION_COUNT_PER_METHOD * methodNames.size(); testNumber++) {
            int count = rnd.nextInt(INVOCATION_COUNT_PER_METHOD * 10);
            if (eObject.size() < 10) {
                for (int i = 0; i <= count; i++) {
                    Integer value = rnd.nextInt(MAX_VALUE);
                    aObject.add(value + i * value);
                    eObject.add(value + i * value);
                }
                System.out.printf("%n==Add %d random values. %n", count);
            }
            int mIndex = rnd.nextInt(methodsA.size());
            Method methodE = null;
            Method methodA = null;
            int i = 0;
            for (Map.Entry<String, Method> entry : methodsA.entrySet()) {
                if (mIndex == i++) {
                    methodA = entry.getValue();
                    methodE = methodsE.get(entry.getKey());
                    break;
                }
            }
            int params = methodE.getParameterCount();
            Object[] parameters = getRandomParams(methodA.getParameterTypes());
            System.out.printf("Start %s. Parameters=%s%n", getSignature(methodA), Arrays.toString(parameters));
            Object expected = methodE.invoke(eObject, parameters);
            Object actual = methodA.invoke(aObject, parameters);
            String eString = eObject.toString();
            String aString = aObject.toString();
            assertEquals("Error compare methods\n" + methodE + "\n" + methodA, expected, actual);
            assertEquals("Erros state after\n" + methodE + "\n" + methodA, eString, aString);
            System.out.printf("Size actual=%d expected=%d%n", aObject.size(), eObject.size());
        }
        System.out.println("=".repeat(100) + "\n COMPLETE: " + methodNames);
        System.out.println("expected: " + eObject);
        System.out.println("  actual: " + aObject);
    }

    private Object[] getRandomParams(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                Set<Integer> collect = IntStream
                        .range(2, 2 + rnd.nextInt(eObject.size()))
                        .mapToObj(index -> randomObject())
                        .collect(Collectors.toUnmodifiableSet());
                parameters[i] = collect;
            } else if (Integer.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomObject();
            } else if (int.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = getRandomIndex();
            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomObject();
            } else {
                fail("unexpected type " + parameterTypes[i]);
            }
        }
        return parameters;
    }

    private Integer randomObject() {
        int i = getRandomIndex();
        return rnd.nextBoolean() ? eObject.get(i) : i * eObject.size();
    }

    private int getRandomIndex() {
        return rnd.nextInt(eObject.size());
    }


    private void checkStructure(Class<?> aClass) {
        if (aClass.getSimpleName().startsWith("List")) {
            assertEquals("Incorrect parent", Object.class, aClass.getSuperclass());
        }
        for (Field field : aClass.getDeclaredFields()) {
            checkField(field);
            for (Field subField : field.getType().getDeclaredFields()) {
                checkField(subField);
            }
        }
    }

    private void checkField(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            fail("Incorrect field: " + field);
        }
    }


    private Map<String, Method> fill(Class<?> c, TreeSet<String> methodNames) {
        return Stream.of(c.getMethods(), c.getDeclaredMethods())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> methodNames.contains(getSignature(m).split(" ", 3)[1]))
                .filter(this::notComparable)
                .collect(Collectors.toMap(this::getSignature, m -> m));
    }


    private boolean notComparable(Method m) {
        return m.getReturnType() != Comparable.class &&
               0 == Arrays.stream(m.getParameterTypes())
                       .filter(p -> p == Comparable.class)
                       .count();
    }

    private String getSignature(Method method) {
        return cache.computeIfAbsent(method, m -> {
            Class<?>[] parameterTypes = method.getParameterTypes();
            StringJoiner out = new StringJoiner(
                    ",",
                    method.getReturnType().getSimpleName() + " " + method.getName() + "(",
                    ")"
            );
            for (int i = 0, parameterTypesLength = parameterTypes.length; i < parameterTypesLength; i++) {
                out.add(parameterTypes[i].getSimpleName());
            }
            return out.toString();
        });
    }


    public String getSignatures(Class<?> aClass) {
        return Stream.of(aClass.getMethods(), aClass.getDeclaredMethods())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .map(this::getSignature)
                .collect(Collectors.joining("\n"));
    }
}