import java.util.*;

public class FoodChainRoutingSystem {

    static final int INF = 999999;

    static class Edge {
        int src, dest, weight;

        Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }
    }

    static class Node implements Comparable<Node> {
        int vertex, distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        public int compareTo(Node other) {
            return this.distance - other.distance;
        }
    }

    // Dijkstra Algorithm
    static void dijkstra(List<List<Node>> graph, int source) {
        int V = graph.size();

        int[] dist = new int[V];
        Arrays.fill(dist, INF);

        PriorityQueue<Node> pq = new PriorityQueue<>();

        dist[source] = 0;
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {

            Node current = pq.poll();

            int u = current.vertex;

            for (Node neighbor : graph.get(u)) {

                int v = neighbor.vertex;
                int weight = neighbor.distance;

                if (dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.add(new Node(v, dist[v]));
                }
            }
        }

        System.out.println("\nDijkstra Shortest Paths:");
        for (int i = 0; i < V; i++) {
            System.out.println("Location " + i +
                    " Distance = " + dist[i]);
        }
    }

    // Bellman-Ford Algorithm
    static void bellmanFord(List<Edge> edges, int V, int source) {

        int[] dist = new int[V];
        Arrays.fill(dist, INF);

        dist[source] = 0;

        for (int i = 1; i < V; i++) {

            for (Edge edge : edges) {

                if (dist[edge.src] != INF &&
                        dist[edge.src] + edge.weight < dist[edge.dest]) {

                    dist[edge.dest] = dist[edge.src] + edge.weight;
                }
            }
        }

        boolean negativeCycle = false;

        for (Edge edge : edges) {

            if (dist[edge.src] != INF &&
                    dist[edge.src] + edge.weight < dist[edge.dest]) {

                negativeCycle = true;
                break;
            }
        }

        if (negativeCycle) {
            System.out.println("\nNegative Weight Cycle Detected!");
            return;
        }

        System.out.println("\nBellman-Ford Shortest Paths:");

        for (int i = 0; i < V; i++) {
            System.out.println("Location " + i +
                    " Distance = " + dist[i]);
        }
    }

    // Floyd-Warshall Algorithm
    static void floydWarshall(int[][] graph, int V) {

        int[][] dist = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = graph[i][j];
            }
        }

        for (int k = 0; k < V; k++) {

            for (int i = 0; i < V; i++) {

                for (int j = 0; j < V; j++) {

                    if (dist[i][k] != INF &&
                            dist[k][j] != INF &&
                            dist[i][k] + dist[k][j] < dist[i][j]) {

                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        System.out.println("\nFloyd-Warshall Distance Matrix:");

        for (int i = 0; i < V; i++) {

            for (int j = 0; j < V; j++) {

                if (dist[i][j] == INF)
                    System.out.print("INF ");
                else
                    System.out.print(dist[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {

        int V = 6;

        /*
         * 0 = Restaurant
         * 1 = Hub A
         * 2 = Hub B
         * 3 = Warehouse
         * 4 = Dispatch Center
         * 5 = Customer
         */

        List<List<Node>> graph = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(new Node(1, 4));
        graph.get(0).add(new Node(2, 2));

        graph.get(1).add(new Node(3, 5));
        graph.get(1).add(new Node(4, 10));

        graph.get(2).add(new Node(1, 1));
        graph.get(2).add(new Node(4, 8));

        graph.get(3).add(new Node(5, 3));

        graph.get(4).add(new Node(5, 2));

        dijkstra(graph, 0);

        List<Edge> edges = new ArrayList<>();

        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 2));
        edges.add(new Edge(2, 1, 1));
        edges.add(new Edge(1, 3, 5));
        edges.add(new Edge(1, 4, 10));
        edges.add(new Edge(2, 4, 8));
        edges.add(new Edge(3, 5, 3));
        edges.add(new Edge(4, 5, 2));

        bellmanFord(edges, V, 0);

        int[][] matrix = {
                { 0, 4, 2, INF, INF, INF },
                { INF, 0, INF, 5, 10, INF },
                { INF, 1, 0, INF, 8, INF },
                { INF, INF, INF, 0, INF, 3 },
                { INF, INF, INF, INF, 0, 2 },
                { INF, INF, INF, INF, INF, 0 }
        };

        floydWarshall(matrix, V);
    }
}