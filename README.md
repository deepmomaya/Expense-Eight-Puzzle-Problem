# Expense-Eight-Puzzle-Problem

This is a Expense 8 Puzzle Solver to tackle the Expense 8 puzzle problem, a variant of the well-known 8 puzzle problem. In the classic 8 puzzle, a 3x3 grid contains numbered tiles, with one tile space left empty. The player can slide tiles into the empty space, aiming to reach a specified goal configuration from a given initial state by performing a sequence of moves. In the Expense 8 puzzle, each tile has an associated cost value representing the expense incurred when moving that particular tile. Hence, the player must not only consider the goal state but also strive to minimize the cumulative cost of the moves made throughout the puzzle solving process.

### Features
- Users can specify the initial and goal states of the puzzle via input files.
- Provides an option to dump the search trace for analysis.
- Utilizes the Manhattan distance heuristic for A* search.
- Supports various search algorithms including Breadth-First Search (BFS), Uniform Cost Search (UCS), Depth-First Search (DFS), and A* Search.
