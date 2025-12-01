# Name

Find all substrings that meet the given criteria

# Task

Find all substrings that meet the given criteria

## Task

A non-empty string containing any characters in any sequence is given. It is necessary to find all substrings that meet the following requirements:

1. They consist of either only Cyrillic characters or only Latin characters
2. The minimum length of a substring is 3 characters
3. The maximum length of a substring is 15 characters
4. The substring begins with a capital character
5. The substring contains only one capital character

> **Remarks:** 
> - create an array with substrings;
> - output all substrings separated by spaces in the order they appear in the given string;

## Example

- Input: 76МАРтшщ778*впрвы+-*Dfsr65565dggd8778Hik
- Expected output: Ртщщ Dfsr Hik

# Theory

Theoretical information

## Searching for a substring

### Definition and features

**A substring** is a continuous sequence of characters within the original string.

 Classification of characters:
1. Cyrillic characters:
 > Capital: A-Z, Y
 > Lowercase: a-z, y
2. Latin characters:
 > Capital: A-Z
 > Lowercase: a-z
Algorithmic approaches:

 1. Full brute force - checking all possible substrings of length from 3 to 15

 2. Sliding window - efficient movement along the string with condition checking

 3. Finite state machines - sequential processing of characters with state tracking

 Key checks:

 1. Alphabet detection (Cyrillic/Latin)
 2. Character case checking
 3. Counting capital characters
 4. Substring length validation

Regular expressions:

For this task, you can use regular expressions to efficiently check conditions:
    [А-ЯЁ][а-яё]{2,14} - cirilic substrings

    [A-Z][a-z]{2,14} - latin substrings

