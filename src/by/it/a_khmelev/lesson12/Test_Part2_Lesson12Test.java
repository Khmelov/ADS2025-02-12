package by.it.a_khmelev.lesson12;


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
public class Test_Part2_Lesson12Test extends HomeWork {

    private static final int RND_SEED = 123;
    private static final int INVOCATION_COUNT_PER_METHOD = 10;
    private static final int MAX_VALUE = 100;
    private final Random rnd = new Random(RND_SEED);
    private Map<Integer, String> eObject;
    private Map<Integer, String> aObject;

    private Map<Method, String> cache = new HashMap<>();

    @Test(timeout = 500 * INVOCATION_COUNT_PER_METHOD)
    public void testTaskA() throws Exception {
        String[] methods = """
                toString()
                put(Object,Object)
                remove(Object)
                get(Object)
                containsKey(Object)

                size()
                clear()
                isEmpty()
                """.split("\\s+");
        eObject = new TreeMap<>();
        randomCheck("MyAvlMap", methods);
    }

    @Test(timeout = 500 * INVOCATION_COUNT_PER_METHOD)
    public void testTaskB() throws Exception {
        String[] methods = """
                toString()
                put(Object,Object)
                remove(Object)
                get(Object)
                containsKey(Object)
                containsValue(Object)
                                
                size()
                clear()
                isEmpty()
                                
                headMap(Object)
                tailMap(Object)
                firstKey()
                lastKey()
                """.split("\\s+");
        eObject = new TreeMap<>();
        randomCheck("MyRbMap", methods);
    }

    @Test(timeout = 500 * INVOCATION_COUNT_PER_METHOD)
    public void testTaskC() throws Exception {
        String[] methods = """
                put(Object,Object)
                remove(Object)
                get(Object)
                containsKey(Object)
                containsValue(Object)
                                
                size()
                clear()
                isEmpty()
                                
                headMap(Object)
                tailMap(Object)
                firstKey()
                lastKey()
                                
                lowerKey(Object)
                floorKey(Object)
                ceilingKey(Object)
                higherKey(Object)
                """.split("\\s+");
        eObject = new TreeMap<>();
        randomCheck("MySplayMap", methods);
    }

    private void randomCheck(String aClassName, String... methods) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Set<String> methodNames = new TreeSet<>(Arrays.asList(methods));
        methodNames.removeIf(key -> key == null || key.isBlank());
        methodNames.forEach(System.out::println);
        Class<?> aClass = findClass(aClassName);
        checkStructure(aClass);
        System.out.printf("\nStart test methods in class %s%n", aClass);
        aObject = (Map<Integer, String>) aClass.getDeclaredConstructor().newInstance();

        Map<String, Method> methodsE = fill(eObject.getClass(), methodNames);
        Map<String, Method> methodsA = fill(aClass, methodsE);

        assertEquals("Not found methods for test in:\n" + methodsA.toString().replace("),", ")\n")
                , methodNames.size()
                , methodsA.size()
        );

        for (int testNumber = 0; testNumber < INVOCATION_COUNT_PER_METHOD * methodNames.size(); testNumber++) {
            int count = rnd.nextInt(INVOCATION_COUNT_PER_METHOD * 10);
            if (eObject.size() < 10) {
                for (int i = 0; i <= count; i++) {
                    Integer key = rnd.nextInt(MAX_VALUE) * (i + 1);
                    String value = "generate" + key;
                    eObject.put(key, value);
                    aObject.put(key, value);
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
            String nameAndParameters = getSignature(methodA).replace(")", "->" + Arrays.toString(parameters)) + ")";
            System.out.printf("Start %s%n", nameAndParameters);
            Object expected = methodE.invoke(eObject, parameters);
            Object actual = methodA.invoke(aObject, parameters);
            String eString = eObject.toString();
            String aString = aObject.toString();
            assertEquals("Error compare methods\n" + methodE + "\n" + methodA, expected, actual);
            System.out.printf("\tStop. Size actual=%d expected=%d%n", aObject.size(), eObject.size());
            int eChecksum = checkSum(eString);
            int aChecksum = checkSum(aString);
            assertEquals(("Erros state\n" +
                          "expectred check sum=%d for %s\n" +
                          "   actual check sum=%d for %s\n")
                    .formatted(eChecksum, eString, aChecksum, aString), eChecksum, aChecksum);
        }
        System.out.println("=".repeat(100) + "\nCOMPLETE: " + methodNames);
        System.out.println("expected: " + eObject);
        System.out.println("  actual: " + aObject);
    }

    private Object[] getRandomParams(Class<?>[] parameterTypes) {
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                Set<Number> collect = IntStream
                        .range(2, 2 + rnd.nextInt(eObject.size()))
                        .mapToObj(index -> randomInteger())
                        .collect(Collectors.toUnmodifiableSet());
                parameters[i] = collect;
            } else if (String.class.isAssignableFrom(parameterTypes[i])
                       || i == 1 //for put(Object,Object)
            ) {
                parameters[i] = "str" + randomInteger();
            } else if (Integer.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomInteger();
            } else if (int.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = getRandomIndex();
            } else if (Object.class.isAssignableFrom(parameterTypes[i])) {
                parameters[i] = randomInteger();
            } else {
                fail("unexpected type " + parameterTypes[i]);
            }
        }
        return parameters;
    }

    private Number randomInteger() {
        int i = getRandomIndex();
        if (rnd.nextBoolean()) {
            return i * eObject.size();
        }
        Iterator<Integer> iterator = eObject.keySet().iterator();
        while (i-- > 0) {
            iterator.next();
        }
        return iterator.next();
    }

    private int getRandomIndex() {
        return rnd.nextInt(eObject.size());
    }


    private void checkStructure(Class<?> aClass) {
        if (aClass.getPackageName().equals(this.getClass().getPackageName())) {
            assertEquals("Incorrect parent", Object.class, aClass.getSuperclass());
        }
        for (Field field : aClass.getDeclaredFields()) {
            checkFieldAsCollection(field);
            for (Field subField : field.getType().getDeclaredFields()) {
                checkFieldAsCollection(subField);
            }
        }
    }

    private void checkFieldAsCollection(Field field) {
        Class<?> type = field.getType();
        if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
            fail("Incorrect field: " + field);
        }
    }


    private Map<String, Method> fill(Class<?> c, Set<String> methodNames) {
        return Stream.of(c.getMethods(), c.getDeclaredMethods())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> methodNames.contains(getSignature(m).split(" ", 3)[1]))
                .filter(this::notComparable)
                .collect(Collectors.toMap(this::getSignature, m -> m));
    }

    private Map<String, Method> fill(Class<?> c, Map<String, Method> expectedMapMethods) {
        return Stream.of(c.getMethods(), c.getDeclaredMethods())
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> expectedMapMethods.containsKey(getSignature(m)))
                .filter(this::notComparable)
                .collect(Collectors.toMap(this::getSignature, m -> m));
    }


    private boolean notComparable(Method m) {
        return m.getReturnType() != Comparable.class &&
               Arrays.stream(m.getParameterTypes())
                       .noneMatch(p -> p == Comparable.class);
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
        return getSignatures(aClass.getMethods(), aClass.getDeclaredMethods());
    }

    public String getSignatures(Method[]... methods) {
        return Stream.of(methods)
                .flatMap(Arrays::stream)
                .distinct()
                .filter(m -> !Modifier.isStatic(m.getModifiers()))
                .map(this::getSignature)
                .collect(Collectors.joining("\n"));
    }

    private int checkSum(String someString) {
        return someString.chars().sum();
    }

}