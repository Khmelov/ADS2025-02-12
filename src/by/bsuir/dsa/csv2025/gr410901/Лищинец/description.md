# Name

Coordinate compression

# Task

Coordinate Compression for Database Query Optimization

## Problem Description
In a geo-analytics system, there is a database that stores object coordinates on a map. Coordinates are represented as integers (e.g., meters from a certain origin), but their range is very large - from -10^9 to 10^9. However, the number of unique coordinates does not exceed 10^5.

To optimize database queries and reduce memory usage, you need to implement a coordinate compression algorithm.

## Task Statement
Implement a `CoordinateCompressor` class that performs coordinate compression and supports the following operations:

- **Add coordinates** - register coordinates for subsequent compression
- **Compress coordinate** - transform original coordinate to its compressed index
- **Restore coordinate** - get original coordinate from compressed index
- **Get range** - determine compressed coordinate boundaries for a given range of original coordinates

## Technical Requirements
- Coordinates are represented as integers (`long`)
- After adding all coordinates, call `compress()` method to perform compression
- Compressed indices should be in range [0, n-1], where n is the number of unique coordinates
- Coordinate order must be preserved: if coord1 < coord2, then compressed(coord1) < compressed(coord2)
- Compression/restoration methods should work in O(log n) time
- Class should be thread-safe for single-threaded usage

## Class Methods

### Constructors
- `CoordinateCompressor()` - creates empty compressor
- `CoordinateCompressor(Collection<Long> initialCoordinates)` - creates compressor with initial coordinates

### Main Methods
- `boolean addCoordinate(long coordinate)` - adds coordinate
- `void addCoordinates(long[] coordinates)` - adds array of coordinates
- `void compress()` - performs coordinate compression
- `int compressCoordinate(long coordinate)` - compresses coordinate
- `long restoreCoordinate(int compressedIndex)` - restores coordinate
- `int[] getCompressedRange(long from, long to)` - finds compressed range

### Utility Methods
- `int size()` - returns number of unique coordinates
- `boolean isCompressed()` - checks if compression was performed
- `void clear()` - clears the compressor

## Practical Applications
This algorithm is used in:
- Geographic information systems (GIS)
- Database index optimization
- Big data processing algorithms
- Computer graphics and game engines
- Analytics and data visualization systems

## Usage Example
```java
CoordinateCompressor compressor = new CoordinateCompressor();
compressor.addCoordinate(1000L);
compressor.addCoordinate(100L);
compressor.addCoordinate(10L);
compressor.compress();

int index = compressor.compressCoordinate(100L); // returns 1
long coord = compressor.restoreCoordinate(0);    // returns 10L
```

# Theory

Coordinate Compression

**Coordinate compression** is a technique used to reduce the range of values while preserving their relative order. Particularly useful when working with large numbers where the number of unique values is much smaller than their range.

## Basic Idea

Given an array of coordinates with large range:
```
Original coordinates: [1000, -5000000, 1000, 250000000, -5000000]
```

After compression:
```
Compressed indices: [1, 0, 1, 2, 0]
Unique values: [-5000000, 1000, 250000000]
```

## Algorithm

1. **Collect unique coordinates** — store all distinct values
2. **Sorting** — arrange values in ascending order
3. **Mapping** — assign each original value its index in the sorted array

## Complexity

- **Time**: O(n log n) for sorting, O(log n) for queries
- **Space**: O(n) for storing unique values

## Applications

- **Geographic information systems** — compressing GPS coordinates
- **Databases** — optimizing indexes for sparse data
- **Query processing** — range transformation
- **Computer graphics** — working with large coordinate spaces

## Key Properties

- **Order preservation**: if a < b, then compressed(a) < compressed(b)
- **Reversibility**: original value can be restored from index
- **Efficiency**: reduces memory usage for sparse data

## Mathematical Foundation

The technique relies on establishing a **bijective mapping** between the original large coordinate space and a compressed integer space [0, n-1], where order is preserved:

```
f: OriginalSpace → [0, n-1]
such that ∀ a, b: a < b ⇒ f(a) < f(b)
```

