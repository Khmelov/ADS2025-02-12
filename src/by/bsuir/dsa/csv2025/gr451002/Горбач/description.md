# Name

Adaptive Package Sorting

# Task

You are working for a logistics company that specializes in urgent deliveries. Your task is to develop an algorithm for determining the delivery order of packages.
Implement the `DeliveryHandler` class that will process incoming packages. The class should contain the `addPackage` method, which takes data for a new package, inserts it at the correct position in the sorted list using the insertion sort principle, and returns the index where the package was inserted.

### Package Characteristics

Each package is characterized by:

* `id` (unique identifier).
* `priority` (delivery priority: 1 - highest, 5 - lowest).
* `distance` (distance to the destination in kilometers).
* `estimatedTime` (estimated delivery time in minutes).

### Sorting Requirements

The incoming stream of packages is not sorted. However, after adding each new package to the list, this list must be maintained sorted according to the following combined criteria:

1. **Primary criterion:** `priority` (in ascending order: 1 comes first).
2. **Secondary criterion:** If priorities are equal, sort by `distance` (in ascending order).
3. **Tertiary criterion:** If both priority and distance are equal, sort by `estimatedTime` (in ascending order).

The Insertion Sort algorithm is ideal for maintaining order in a constantly growing list. Your task is to implement a modified version of this algorithm.

### Method

```java
int addPackage(int id, int priority, double distance, int estimatedTime);
```

### Input Parameters

* `id`: Unique identifier of the package.
* `priority`: Priority level (1-5).
* `distance`: Distance in kilometers.
* `estimatedTime`: Time in minutes.

### Return Value

* The index (starting from 0) in the sorted list where the new package was inserted.

## Important Requirements

* **Do not use built-in sorting methods** (`Collections.sort`, `Arrays.sort`, etc.) inside the `addPackage` method. The algorithm should independently find the correct insertion position and shift elements, simulating the work of classic insertion sort, but for a single new element.
* Initially, the list is empty.
* It is guaranteed that `id` is unique for each call.

## Usage Example

```java
DeliveryHandler handler = new DeliveryHandler();

// Package 1: priority 2, distance 15.5 km, time 30 min
int index1 = handler.addPackage(1, 2, 15.5, 30);   // Returns 0

// Package 2: higher priority 1 - goes to the beginning
int index2 = handler.addPackage(2, 1, 10.0, 20);   // Returns 0

// Package 3: same priority 2, but shorter distance - goes before package 1
int index3 = handler.addPackage(3, 2, 5.0, 25);    // Returns 1

// Package 4: same priority 2 and distance 15.5, but shorter time - goes before package 1
int index4 = handler.addPackage(4, 2, 15.5, 25);   // Returns 2
```

## Expected Result

After executing the example, the list should be ordered as follows:

1. Package with `id=2`: `priority=1, distance=10.0, time=20`
2. Package with `id=3`: `priority=2, distance=5.0, time=25`
3. Package with `id=4`: `priority=2, distance=15.5, time=25`
4. Package with `id=1`: `priority=2, distance=15.5, time=30`

# Theory

## Insertion Sort

Insertion Sort is one of the simplest sorting algorithms, and it works perfectly for situations where data arrives gradually and needs to be maintained in a sorted state.

## How the Algorithm Works

The array is conceptually divided into a sorted left part and an unsorted right part. The algorithm sequentially takes each element from the unsorted part and inserts it into the correct position in the sorted part.

**Step-by-step description:**

1. Start with the second element (index 1), considering the first element already sorted
2. For the current element:
   - Save its value to a temporary variable
   - Go from right to left through the sorted part
   - Compare the current element with each element of the sorted part
   - If current element is LESS → shift that element to the right
   - If current element is GREATER or EQUAL → stop
   - Insert the saved element at the freed position

**Example with numbers:**

```
Original array: [5, 2, 8, 1, 9]

Step 1: [5 | 2, 8, 1, 9]           
        Sorted part: [5]

Step 2: [2, 5 | 8, 1, 9]           
        Take 2, insert before 5
        Sorted part: [2, 5]

Step 3: [2, 5, 8 | 1, 9]           
        Take 8, already in place
        Sorted part: [2, 5, 8]

Step 4: [1, 2, 5, 8 | 9]           
        Take 1, insert at the beginning
        Sorted part: [1, 2, 5, 8]

Step 5: [1, 2, 5, 8, 9 |]          
        Take 9, already in place
        Result: [1, 2, 5, 8, 9]
```

## Complexity

- **Best case:** O(n) — when the array is already sorted
- **Worst case:** O(n²) — when the array is sorted in reverse order
- **Average case:** O(n²)

## Advantages

- **Adaptivity:** Works well on partially sorted data
- **Stream processing:** Can process data as it arrives
- **Stability:** Preserves the relative order of equal elements
- **Simple implementation:** Easy to implement and understand
- **Efficiency for small data:** Faster than complex algorithms for small arrays

## Disadvantages

- Inefficient on large arrays
- Quadratic complexity in the worst case

