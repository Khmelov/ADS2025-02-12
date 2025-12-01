# Name

Finding Unique Words Using a Hash Table

# Task

You are given a string consisting of words separated by spaces. Your task is to determine how many unique words appear in the string.

Use a hash table (or any hashing-based data structure) to efficiently detect duplicate words.

Input format:
A single line of text.

Output format:
A single integer — the number of unique words.

Example:
Input:
orange apple apple pear orange
Output:
3

# Theory

Hashing is a technique used to transform data (such as a string) into a fixed-size numeric value called a hash.
A data structure that uses hashing — the hash table — allows very fast operations such as searching, inserting, and checking if an element exists.

Core idea:

a hash function computes the hash of a key;

the hash determines the index where the element is stored;

this usually enables O(1) access time.

Hash tables are commonly used to check whether elements are unique or to count their occurrences.

