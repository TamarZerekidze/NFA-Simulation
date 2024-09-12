# Regular Expression to NFA Simulation

## Overview

This project involves creating a program that will check whether a given text matches a provided regular expression using a Non-deterministic Finite Automaton (NFA). The task is divided into two main parts:

1. **Conversion**: Convert a given regular expression into an equivalent NFA (Non-deterministic Finite Automaton).
2. **Simulation**: Simulate the NFA to determine if the input string is accepted.

## Part 1: Regular Expression to NFA

### Task

Program converts a given regular expression into a specific type of NFA. The conversion should adhere to the following constraints:
- The NFA must have no more than one additional state beyond the length of the regular expression.
- The NFA should not contain ε-transitions (transitions that occur without consuming any input). Remove ε-transitions by making all states that transition via ε also transitions directly.

### Input Format

- **Standard Input**: A single line containing the regular expression. Allowed symbols are lowercase Latin letters, digits, `|` (for union), `*` (for Kleene star), and `()` (for grouping).
    - Example: `(ab*c(0|1)*)*`

### Output Format

- **Standard Output**: The description of the NFA in the following format:
    1. The first line contains three integers: `n`, `a`, `t`:
        - `n`: Number of states
        - `a`: Number of accepting states
        - `t`: Number of transitions
    2. The second line contains `a` integers representing the indices of the accepting states.
    3. For each state, print a line that starts with an integer `ki` representing the number of transitions from that state, followed by `ki` pairs of characters and state indices.

  Example:

input:
- (a|b)*(c|())

output:
- 2 2 3
- 0 1
- 3 a 0 b 0 c 1
- 0


### Program Name

- Name the program `build.java`

## Part 2: NFA Simulation

### Task

Program that simulates the NFA and determines if the input string reaches any accepting states at each position.

### Input Format

- **First Line**: The input string to be checked.
- **Subsequent Lines**: The output format of the NFA from Part 1.

### Output Format

- **Standard Output**: A string of the same length as the input string where each position indicates whether the NFA was in an accepting state after reading the first `i` characters. Use 'Y' for accepting states and 'N' for non-accepting states.

Example:

input:
- aababacab
- 2 2 3
- 0 1
- 3 a 0 b 0 c 1
- 0

output:
- YYYYYYYNN

### Program Name

- Name the program `run.java`

## Notes

- Solution does not exceed O((N + n) · n) operations, where `N` is the length of the input string and `n` is the number of states in the NFA.
