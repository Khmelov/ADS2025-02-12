# Name

Special order

# Task

Given an array of integers `nums`, sort the array in a **special order** that satisfies the following conditions:

1. **All even numbers** must be positioned **before** all odd numbers.
2. **Among the even numbers**, the sorting should be in **descending order** (from largest to smallest).
3. **Among the odd numbers**, the sorting should be in **ascending order** (from smallest to largest).

# Theory


### 1. Custom Sorting in Java
In Java, custom sorting uses:
- **Comparator** interface
- **Lambda expressions** (Java 8+)
- **Arrays.sort()** or **Collections.sort()**

### 2. Comparator
**Comparator** - a functional interface that defines the order of two elements:
- Returns a **negative** number if the first element should come before the second
- Returns a **positive** number if the second element should come before the first
- Returns **0** if the elements are equal

---

##  Key Solution Ideas

### Strategy : Single Comparator with Lambda

```java
Arrays.sort(nums, (a, b) -> {
    boolean aEven = (a % 2 == 0);
    boolean bEven = (b % 2 == 0);
    
    if (aEven && bEven) {
        return b - a; // even numbers in descending order
    } else if (!aEven && !bEven) {
        return a - b; // odd numbers in ascending order
    } else if (aEven) {
        return -1;    // even before odd
    } else {
        return 1;     // odd after even
    }
});

