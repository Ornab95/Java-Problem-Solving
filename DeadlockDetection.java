import java.util.*;

public class DeadlockDetection {
    static class Graph {
        private final int numVertices;
        private final List<List<Integer>> adjList;

        public Graph(int numVertices) {
            this.numVertices = numVertices;
            adjList = new ArrayList<>(numVertices);
            for (int i = 0; i < numVertices; i++) {
                adjList.add(new ArrayList<>());
            }
        }

        public void addEdge(int src, int dest) {
            adjList.get(src).add(dest);
        }

        public boolean detectCycles() {
            boolean[] visited = new boolean[numVertices];
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i] && detectCyclesUtil(i, visited)) {
                    return true;
                }
            }
            return false;
        }

 private boolean detectCyclesUtil(int node, boolean[] visited) {
    Queue<Integer> queue = new LinkedList<>();
    Map<Integer, Integer> parent = new HashMap<>();

    queue.add(node);
    visited[node] = true;
    parent.put(node, -1);

    while (!queue.isEmpty()) {
        int current = queue.poll();

        for (int neighbor : adjList.get(current)) {
            if (!visited[neighbor]) {
                visited[neighbor] = true;
                queue.add(neighbor);
                parent.put(neighbor, current);
            } else if (parent.get(current) != null && parent.get(current) != neighbor) {
                // We've found a cycle
                System.out.println("Deadlock Detected Among nodes: " + getPath(parent, current, neighbor));
                return true;
            }
        }
    }
    return false;
}


        private List<Integer> getPath(Map<Integer, Integer> parent, int start, int end) {
            List<Integer> path = new ArrayList<>();
            int current = end;
            while (current != start) {
                path.add(current);
                current = parent.get(current);
            }
            path.add(start);
            Collections.reverse(path);
            return path;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of nodes: ");
        int numNodes = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Node names: ");
        String nodeNames = scanner.nextLine();

        Graph graph = new Graph(numNodes);
        Map<Character, Integer> nodeIndexMap = new HashMap<>();
        for (int i = 0; i < nodeNames.length(); i++) {
            nodeIndexMap.put(nodeNames.charAt(i), i);
        }

        System.out.println("Enter edges (type 'done' to finish):");
        while (true) {
            String edge = scanner.nextLine();
            if (edge.equals("done")) {
                break;
            }
            String[] nodes = edge.split(" to ");
            int src = nodeIndexMap.get(nodes[0].charAt(0));
            int dest = nodeIndexMap.get(nodes[1].charAt(0));
            graph.addEdge(src, dest);
        }

        if (!graph.detectCycles()) {
            System.out.println("No Deadlocks Detected");
        }

        scanner.close();
    }
}
