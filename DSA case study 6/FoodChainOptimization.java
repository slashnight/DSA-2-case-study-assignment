import java.util.*;

public class FoodChainOptimization {

    // ---------------------------------------------------
    // 1. ACTIVITY SELECTION (GREEDY)
    // ---------------------------------------------------

    static class Activity {
        int start, finish;

        Activity(int start, int finish) {
            this.start = start;
            this.finish = finish;
        }
    }

    static void activitySelection(Activity[] activities) {

        Arrays.sort(activities, Comparator.comparingInt(a -> a.finish));

        System.out.println("\nSelected Activities:");

        int lastFinish = activities[0].finish;
        System.out.println("(" + activities[0].start + ", "
                + activities[0].finish + ")");

        for (int i = 1; i < activities.length; i++) {
            if (activities[i].start >= lastFinish) {
                System.out.println("(" + activities[i].start + ", "
                        + activities[i].finish + ")");
                lastFinish = activities[i].finish;
            }
        }
    }

    // ---------------------------------------------------
    // 2. DIJKSTRA SHORTEST PATH (GREEDY)
    // ---------------------------------------------------

    static class Edge {
        int dest, weight;

        Edge(int dest, int weight) {
            this.dest = dest;
            this.weight = weight;
        }
    }

    static void dijkstra(List<List<Edge>> graph, int source) {

        int V = graph.size();

        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));

        dist[source] = 0;
        pq.offer(new int[] { source, 0 });

        while (!pq.isEmpty()) {

            int[] current = pq.poll();

            int u = current[0];

            for (Edge edge : graph.get(u)) {

                int v = edge.dest;
                int weight = edge.weight;

                if (dist[u] + weight < dist[v]) {

                    dist[v] = dist[u] + weight;

                    pq.offer(new int[] { v, dist[v] });
                }
            }
        }

        System.out.println("\nShortest Distances from Node " + source);

        for (int i = 0; i < V; i++) {
            System.out.println("To " + i + " = " + dist[i]);
        }
    }

    // ---------------------------------------------------
    // 3. 0/1 KNAPSACK (TABULATION DP)
    // ---------------------------------------------------

    static int knapsack(int[] wt, int[] val, int W) {

        int n = wt.length;

        int[][] dp = new int[n + 1][W + 1];

        for (int i = 1; i <= n; i++) {

            for (int w = 0; w <= W; w++) {

                if (wt[i - 1] <= w) {

                    dp[i][w] = Math.max(
                            val[i - 1] +
                                    dp[i - 1][w - wt[i - 1]],
                            dp[i - 1][w]);

                } else {

                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        return dp[n][W];
    }

    // ---------------------------------------------------
    // 4. LCS (TABULATION DP)
    // ---------------------------------------------------

    static String lcs(String A, String B) {

        int m = A.length();
        int n = B.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                if (A.charAt(i - 1) == B.charAt(j - 1)) {

                    dp[i][j] = dp[i - 1][j - 1] + 1;

                } else {

                    dp[i][j] = Math.max(dp[i - 1][j],
                            dp[i][j - 1]);
                }
            }
        }

        StringBuilder result = new StringBuilder();

        int i = m;
        int j = n;

        while (i > 0 && j > 0) {

            if (A.charAt(i - 1) == B.charAt(j - 1)) {

                result.append(A.charAt(i - 1));
                i--;
                j--;

            } else if (dp[i - 1][j] > dp[i][j - 1]) {

                i--;

            } else {

                j--;
            }
        }

        return result.reverse().toString();
    }

    // ---------------------------------------------------
    // 5. MATRIX CHAIN MULTIPLICATION (DP)
    // ---------------------------------------------------

    static int matrixChain(int[] p) {

        int n = p.length;

        int[][] dp = new int[n][n];

        for (int len = 2; len < n; len++) {

            for (int i = 1; i < n - len + 1; i++) {

                int j = i + len - 1;

                dp[i][j] = Integer.MAX_VALUE;

                for (int k = i; k < j; k++) {

                    int cost = dp[i][k]
                            + dp[k + 1][j]
                            + p[i - 1] * p[k] * p[j];

                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }

        return dp[1][n - 1];
    }

    // ---------------------------------------------------
    // MAIN METHOD
    // ---------------------------------------------------

    public static void main(String[] args) {

        System.out.println(
                "FOODCHAIN OPTIMIZATION SYSTEM");

        // -----------------------------------
        // Activity Scheduling
        // -----------------------------------

        Activity[] activities = {
                new Activity(1, 4),
                new Activity(3, 5),
                new Activity(0, 6),
                new Activity(5, 7),
                new Activity(8, 9),
                new Activity(5, 9)
        };

        activitySelection(activities);

        // -----------------------------------
        // Dijkstra
        // -----------------------------------

        int V = 6;

        List<List<Edge>> graph = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(0).add(new Edge(1, 4));
        graph.get(0).add(new Edge(2, 2));

        graph.get(1).add(new Edge(2, 1));
        graph.get(1).add(new Edge(3, 5));

        graph.get(2).add(new Edge(3, 8));
        graph.get(2).add(new Edge(4, 10));

        graph.get(3).add(new Edge(4, 2));
        graph.get(3).add(new Edge(5, 6));

        graph.get(4).add(new Edge(5, 3));

        dijkstra(graph, 0);

        // -----------------------------------
        // Knapsack
        // -----------------------------------

        int[] wt = { 2, 3, 4, 5, 1 };
        int[] val = { 60, 90, 120, 150, 30 };
        int capacity = 10;

        int maxProfit = knapsack(wt, val, capacity);

        System.out.println(
                "\nMaximum Profit = "
                        + maxProfit);

        // -----------------------------------
        // LCS
        // -----------------------------------

        String A = "PIZZABURGER";
        String B = "PASTABURGER";

        String lcsResult = lcs(A, B);

        System.out.println(
                "\nLongest Common Subsequence = "
                        + lcsResult);

        // -----------------------------------
        // Matrix Chain Multiplication
        // -----------------------------------

        int[] dimensions = { 10, 30, 5, 60, 15 };

        int minimumCost = matrixChain(dimensions);

        System.out.println(
                "\nMinimum Matrix Multiplication Cost = "
                        + minimumCost);
    }
}