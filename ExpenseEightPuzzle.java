import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ExpenseEightPuzzle {
    private int[][] state; // Current state of the puzzle
    private int cost; // Cost to reach this state
    private int depth; // Depth of this state in the search tree
    private ExpenseEightPuzzle parent; // Parent state
    private String action; // Action taken to reach this state
    private static int nodesPopped = 0;
    private static int nodesExpanded = 0;
    private static int nodesGenerated = 0;
    private static int maxFringeSize = 0;

    // Constructor
    public ExpenseEightPuzzle(int[][] state, int cost, int depth, ExpenseEightPuzzle parent, String action) {
        this.state = state;
        this.cost = cost;
        this.depth = depth;
        this.parent = parent;
        this.action = action;
        nodesGenerated++;
    }

    // Getter methods
    public int[][] getState() {
        return state;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    public ExpenseEightPuzzle getParent() {
        return parent;
    }

    public String getAction() {
        return action;
    }

    // Method to expand a node
    public static List<ExpenseEightPuzzle> expandNode(ExpenseEightPuzzle node) {
        List<ExpenseEightPuzzle> successors = new ArrayList<>();
        int[][] currentState = node.getState();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] == 0) {
                    // Move Up
                    if (i > 0) {
                        int[][] newState = Arrays.stream(currentState).map(int[]::clone).toArray(int[][]::new);
                        newState[i][j] = newState[i - 1][j];
                        newState[i - 1][j] = 0;
                        successors.add(new ExpenseEightPuzzle(newState, node.getCost() + newState[i][j], node.getDepth() + 1, node, "Up"));
                    }
                    // Move Left
                    if (j > 0) {
                        int[][] newState = Arrays.stream(currentState).map(int[]::clone).toArray(int[][]::new);
                        newState[i][j] = newState[i][j - 1];
                        newState[i][j - 1] = 0;
                        successors.add(new ExpenseEightPuzzle(newState, node.getCost() + newState[i][j], node.getDepth() + 1, node, "Left"));
                    }
                    // Move Down
                    if (i < 2) {
                        int[][] newState = Arrays.stream(currentState).map(int[]::clone).toArray(int[][]::new);
                        newState[i][j] = newState[i + 1][j];
                        newState[i + 1][j] = 0;
                        successors.add(new ExpenseEightPuzzle(newState, node.getCost() + newState[i][j], node.getDepth() + 1, node, "Down"));
                    }
                    // Move Right
                    if (j < 2) {
                        int[][] newState = Arrays.stream(currentState).map(int[]::clone).toArray(int[][]::new);
                        newState[i][j] = newState[i][j + 1];
                        newState[i][j + 1] = 0;
                        successors.add(new ExpenseEightPuzzle(newState, node.getCost() + newState[i][j], node.getDepth() + 1, node, "Right"));
                    }
                    return successors;
                }
            }
        }
        return successors;
    }

    // Method to calculate the Manhattan distance heuristic
    private int calculateHeuristic() {
        int heuristic = 0;
        int[][] goalState = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } }; // Goal state
        int n = 3;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int value = state[i][j];
                if (value != 0) {
                    int targetI = (value - 1) / n;
                    int targetJ = (value - 1) % n;
                    heuristic += Math.abs(i - targetI) + Math.abs(j - targetJ);
                }
            }
        }
        return heuristic;
    }

    // Method to get the heuristic value
    public int getHeu() {
        return calculateHeuristic();
    }

    // Breadth-First Search algorithm
    public static void bfs(int[][] startState, int[][] goalState, boolean dumpFlag) {
        Queue<ExpenseEightPuzzle> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        ExpenseEightPuzzle startNode = new ExpenseEightPuzzle(startState, 0, 0, null, null);
        queue.add(startNode);
        while (!queue.isEmpty()) {
            maxFringeSize = Math.max(maxFringeSize, queue.size());
            ExpenseEightPuzzle currentNode = queue.poll();
            nodesPopped++;
            if (Arrays.deepEquals(currentNode.getState(), goalState)) {
                printOutput(nodesPopped, nodesExpanded, nodesGenerated, maxFringeSize, currentNode);
                return;
            }
            if (!visited.contains(Arrays.deepToString(currentNode.getState()))) {
                nodesExpanded++;
                for (ExpenseEightPuzzle successor : expandNode(currentNode)) {
                    queue.add(successor);
                }
                visited.add(Arrays.deepToString(currentNode.getState()));
                if (dumpFlag) {
                    writeTrace(currentNode, queue, visited);
                }
            }
        }
    }

    // Uniform Cost Search algorithm
    public static void ucs(int[][] startState, int[][] goalState, boolean dumpFlag) {
        PriorityQueue<ExpenseEightPuzzle> queue = new PriorityQueue<>(Comparator.comparingInt(ExpenseEightPuzzle::getCost));
        Set<String> visited = new HashSet<>();
        ExpenseEightPuzzle startNode = new ExpenseEightPuzzle(startState, 0, 0, null, null);
        queue.add(startNode);
        while (!queue.isEmpty()) {
            maxFringeSize = Math.max(maxFringeSize, queue.size());
            ExpenseEightPuzzle currentNode = queue.poll();
            nodesPopped++;
            if (Arrays.deepEquals(currentNode.getState(), goalState)) {
                printOutput(nodesPopped, nodesExpanded, nodesGenerated, maxFringeSize, currentNode);
                return;
            }
            if (!visited.contains(Arrays.deepToString(currentNode.getState()))) {
                nodesExpanded++;
                for (ExpenseEightPuzzle successor : expandNode(currentNode)) {
                    queue.add(successor);
                }
                visited.add(Arrays.deepToString(currentNode.getState()));
                if (dumpFlag) {
                    writeTrace(currentNode, queue, visited);
                }
            }
        }
    }

    // Depth-First Search algorithm
    public static void dfs(int[][] startState, int[][] goalState, boolean dumpFlag) {
        Deque<ExpenseEightPuzzle> stack = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        ExpenseEightPuzzle startNode = new ExpenseEightPuzzle(startState, 0, 0, null, null);
        stack.push(startNode);
        while (!stack.isEmpty()) {
            maxFringeSize = Math.max(maxFringeSize, stack.size());
            ExpenseEightPuzzle currentNode = stack.pop();
            nodesPopped++;
            if (Arrays.deepEquals(currentNode.getState(), goalState)) {
                printOutput(nodesPopped, nodesExpanded, nodesGenerated, maxFringeSize, currentNode);
                return;
            }
            if (!visited.contains(Arrays.deepToString(currentNode.getState()))) {
                nodesExpanded++;
                for (ExpenseEightPuzzle successor : expandNode(currentNode)) {
                    stack.push(successor);
                }
                visited.add(Arrays.deepToString(currentNode.getState()));
                if (dumpFlag) {
                    writeTrace(currentNode, stack, visited);
                }
            }
        }
    }

    // A* Search algorithm
    public static void aStarSearch(int[][] startState, int[][] goalState, boolean dumpFlag) {
        PriorityQueue<ExpenseEightPuzzle> queue = new PriorityQueue<>(Comparator.comparingInt(a -> a.getCost() + a.getHeu()));
        Set<String> visited = new HashSet<>();
        ExpenseEightPuzzle startNode = new ExpenseEightPuzzle(startState, 0, 0, null, null);
        queue.add(startNode);
        while (!queue.isEmpty()) {
            maxFringeSize = Math.max(maxFringeSize, queue.size());
            ExpenseEightPuzzle currentNode = queue.poll();
            nodesPopped++;
            if (Arrays.deepEquals(currentNode.getState(), goalState)) {
                printOutput(nodesPopped, nodesExpanded, nodesGenerated, maxFringeSize, currentNode);
                return;
            }
            if (!visited.contains(Arrays.deepToString(currentNode.getState()))) {
                nodesExpanded++;
                for (ExpenseEightPuzzle successor : expandNode(currentNode)) {
                    queue.add(successor);
                }
                visited.add(Arrays.deepToString(currentNode.getState()));
                if (dumpFlag) {
                    writeTrace(currentNode, queue, visited);
                }
            }
        }
    }

    // Method to print output
    public static void printOutput(int nodesPopped, int nodesExpanded, int nodesGenerated, int maxFringeSize, ExpenseEightPuzzle goalNode) {
        System.out.println("Nodes Popped: " + nodesPopped);
        System.out.println("Nodes Expanded: " + nodesExpanded);
        System.out.println("Nodes Generated: " + nodesGenerated);
        System.out.println("Max Fringe Size: " + maxFringeSize);
        System.out.println("Solution Found at depth " + goalNode.getDepth() + " with cost of " + goalNode.getCost() + ".");
        System.out.println("Steps:");
        List<String> steps = new ArrayList<>();
        while (goalNode.getParent() != null) {
            steps.add("Move " + goalNode.getAction() + " " + goalNode.getCost());
            goalNode = goalNode.getParent();
        }
        Collections.reverse(steps);
        for (String step : steps) {
            System.out.println(step);
        }
    }

    // Method to write trace
    public static void writeTrace(ExpenseEightPuzzle currentNode, Collection<ExpenseEightPuzzle> fringe, Set<String> closedSet) {
        try (FileWriter traceWriter = new FileWriter("trace.txt")) {
            traceWriter.write("State: " + Arrays.deepToString(currentNode.getState()) + ", action: " + currentNode.getAction() + ", depth: " + currentNode.getDepth() + ", g(n): " + currentNode.getCost() + ", h(n): " + currentNode.getHeu() + "\n");
            traceWriter.write("Fringe: " + fringe + "\n");
            traceWriter.write("Closed: " + closedSet + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Main method
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java ExpenseEightPuzzle <start-file> <goal-file> [<method>] [<dump-flag>]");
            return;
        }
        int[][] startState = readStateFromFile(args[0]);
        int[][] goalState = readStateFromFile(args[1]);
        String method = "aStar"; // Default method
        boolean dumpFlag = false;
        if (args.length > 2) {
            method = args[2];
        }
        if (args.length > 3 && args[3].equalsIgnoreCase("true")) {
            dumpFlag = true;
        }
        switch (method.toLowerCase()) {
            case "bfs":
                bfs(startState, goalState, dumpFlag);
                break;
            case "ucs":
                ucs(startState, goalState, dumpFlag);
                break;
            case "dfs":
                dfs(startState, goalState, dumpFlag);
                break;
            case "astar":
                aStarSearch(startState, goalState, dumpFlag);
                break;
            default:
                System.out.println("Invalid search method: " + method);
                break;
        }
    }

    // Method to read state from file
    private static int[][] readStateFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int[][] state = new int[3][3];
            String line;
            int row = 0;
            while ((line = reader.readLine()) != null) {
                if (line.equals("END OF FILE")) {
                    break;
                }
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length != 3) {
                    System.err.println("Invalid input file format: " + fileName);
                    System.exit(1);
                }
                for (int col = 0; col < 3; col++) {
                    state[row][col] = Integer.parseInt(tokens[col]);
                }
                row++;
            }
            if (row != 3) {
                System.err.println("Invalid input file format: " + fileName);
                System.exit(1);
            }
            return state;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading input file: " + fileName);
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
