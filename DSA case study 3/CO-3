import java.util.*;

class Edge implements Comparable<Edge> {
    int src, dest, weight;

    Edge(int src, int dest, int weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public int compareTo(Edge other) {
        return this.weight - other.weight;
    }
}

class Pair {
    int vertex, weight;

    Pair(int vertex, int weight) {
        this.vertex = vertex;
        this.weight = weight;
    }
}

class FoodChainNetwork {

    private int V;
    private List<List<Pair>> adjList;
    private List<Edge> edges;

    FoodChainNetwork(int V) {
        this.V = V;
        adjList = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }

        edges = new ArrayList<>();
    }

    void addRoad(int src, int dest, int distance) {
        adjList.get(src).add(new Pair(dest, distance));
        adjList.get(dest).add(new Pair(src, distance));

        edges.add(new Edge(src, dest, distance));
    }

    // BFS
    void BFS(int start) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        visited[start] = true;
        queue.add(start);

        System.out.println("\nBFS Traversal:");

        while (!queue.isEmpty()) {
            int node = queue.poll();

            System.out.print(node + " ");

            for (Pair neighbor : adjList.get(node)) {
                if (!visited[neighbor.vertex]) {
                    visited[neighbor.vertex] = true;
                    queue.add(neighbor.vertex);
                }
            }
        }
        System.out.println();
    }

    // DFS
    void DFS(int start) {
        boolean[] visited = new boolean[V];

        System.out.println("\nDFS Traversal:");
        dfsUtil(start, visited);
        System.out.println();
    }

    private void dfsUtil(int node, boolean[] visited) {
        visited[node] = true;

        System.out.print(node + " ");

        for (Pair neighbor : adjList.get(node)) {
            if (!visited[neighbor.vertex]) {
                dfsUtil(neighbor.vertex, visited);
            }
        }
    }

    // Connected Components
    void connectedComponents() {
        boolean[] visited = new boolean[V];
        int count = 0;

        System.out.println("\nConnected Components:");

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                count++;

                System.out.print("Cluster " + count + ": ");
                dfsUtil(i, visited);
                System.out.println();
            }
        }

        System.out.println("Total Clusters = " + count);
    }

    // Kruskal MST
    void kruskalMST() {

        Collections.sort(edges);

        int[] parent = new int[V];

        for (int i = 0; i < V; i++)
            parent[i] = i;

        int totalCost = 0;

        System.out.println("\nKruskal MST:");

        for (Edge edge : edges) {

            int root1 = find(parent, edge.src);
            int root2 = find(parent, edge.dest);

            if (root1 != root2) {

                System.out.println(
                        edge.src + " -- " +
                        edge.dest + " = " +
                        edge.weight + " km");

                totalCost += edge.weight;

                parent[root1] = root2;
            }
        }

        System.out.println("Total MST Cost = " + totalCost + " km");
    }

    private int find(int[] parent, int node) {
        if (parent[node] == node)
            return node;

        return parent[node] = find(parent, parent[node]);
    }

    // Prim MST
    void primMST() {

        boolean[] visited = new boolean[V];

        PriorityQueue<Edge> pq =
                new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        visited[0] = true;

        for (Pair p : adjList.get(0)) {
            pq.add(new Edge(0, p.vertex, p.weight));
        }

        int totalCost = 0;

        System.out.println("\nPrim MST:");

        while (!pq.isEmpty()) {

            Edge current = pq.poll();

            if (visited[current.dest])
                continue;

            visited[current.dest] = true;

            System.out.println(
                    current.src + " -- " +
                    current.dest + " = " +
                    current.weight + " km");

            totalCost += current.weight;

            for (Pair neighbor : adjList.get(current.dest)) {
                if (!visited[neighbor.vertex]) {
                    pq.add(new Edge(
                            current.dest,
                            neighbor.vertex,
                            neighbor.weight));
                }
            }
        }

        System.out.println("Total MST Cost = " + totalCost + " km");
    }
}

public class Main {

    public static void main(String[] args) {

        System.out.println("----------------------------------------");
        System.out.println(" FOODCHAIN DELIVERY NETWORK MANAGEMENT ");
        System.out.println("----------------------------------------");

        System.out.println("\nLocation Mapping:");
        System.out.println("0 - Central Warehouse");
        System.out.println("1 - Restaurant A");
        System.out.println("2 - Restaurant B");
        System.out.println("3 - Delivery Hub X");
        System.out.println("4 - Delivery Hub Y");
        System.out.println("5 - Restaurant C");

        FoodChainNetwork network =
                new FoodChainNetwork(6);

        network.addRoad(0, 1, 4);
        network.addRoad(0, 2, 3);
        network.addRoad(1, 3, 2);
        network.addRoad(2, 3, 5);
        network.addRoad(2, 4, 6);
        network.addRoad(3, 4, 1);
        network.addRoad(4, 5, 7);
        network.addRoad(3, 5, 8);

        network.BFS(0);

        network.DFS(0);

        network.connectedComponents();

        network.kruskalMST();

        network.primMST();
    }
}
