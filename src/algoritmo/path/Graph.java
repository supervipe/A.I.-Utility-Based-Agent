package algoritmo.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Graph {
    private final int vertices_size;
    private final ArrayList<ArrayList<Integer>> adj;

    public Graph(int vertices_size) {
        this.vertices_size = vertices_size;

        // Adjacency list for storing which vertices are connected
        adj = new ArrayList<ArrayList<Integer>>(vertices_size);

        for (int i = 0; i < vertices_size; i++) {
            adj.add(new ArrayList<Integer>());
        }
    }

    public int mapPositions(int pos_x, int pos_y) {
        // pos_x = 29, pos_y = 29 -> 900
        // pos_x =  1, pos_y =  0 ->  30
        int count = 0;
        for (int y = 0; y < pos_y*30; y++) {
            count++;
        }
        for (int x = 0; x < pos_x; x++) {
            count++;
        }
        return count;
    }

    public int[][] mapPositionsToPosition(int num_v) {
        // 900 -> pos_x = 29, pos_y = 29
        // 30 -> pos_x = 0, pos_y = 1
        int pos_x = num_v%30;
        int pos_y = Math.abs(num_v/30);

        if (num_v%30 == 0) {
            pos_x = 30;
        }

        return new int[][]{{pos_x}, {pos_y}};
    }

    public void addEdge2(int pos_x, int pos_y, int pos_x2, int pos_y2) {
        int current_v = mapPositions(pos_x, pos_y);
        int linked_v = mapPositions(pos_x2, pos_y2);

        adj.get(current_v).add(linked_v);
        adj.get(linked_v).add(current_v);
    }

    public Boolean canRouteToBank(int pos_x, int pos_y) {
        int source_v = mapPositions(pos_x, pos_y);
        int destination_v = mapPositions(8, 8);

        int[] predecessor = new int[vertices_size];
        int[] distance = new int[vertices_size];

        return BFS(source_v, destination_v, vertices_size, predecessor, distance);
    }

    public LinkedList<Integer> shortestDistance(int pos_x, int pos_y, int pos_x2, int pos_y2) {
        int source_v = mapPositions(pos_x, pos_y);
        int destination_v = mapPositions(pos_x2, pos_y2);

        int[] predecessor = new int[vertices_size];
        int[] distance = new int[vertices_size];

        if (!BFS(source_v, destination_v, vertices_size, predecessor, distance)) {
            System.out.println("Given source and destination are not connected");
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = destination_v;
        path.add(crawl);
        while (predecessor[crawl] != -1) {
            path.add(predecessor[crawl]);
            crawl = predecessor[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + distance[destination_v]);

        // Print path
        System.out.println("Path is :");

        LinkedList<Integer> result = new LinkedList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
            result.add(mapPositionsToPosition(path.get(i))[0][0]);
            result.add(mapPositionsToPosition(path.get(i))[1][0]);
        }

        return result;
    }

    // function to form edge between two vertices
    // source and dest
    public void addEdge(int current_v, int linked_v) {
        adj.get(current_v).add(linked_v);
        adj.get(linked_v).add(current_v);
    }

    // function to print the shortest distance and path
    // between source vertex and destination vertex
    public void printShortestDistance(int source, int destination) {
        // predecessor[i] array stores predecessor of
        // i and distance array stores distance of i
        // from s
        int[] predecessor = new int[vertices_size];
        int[] distance = new int[vertices_size];

        if (!BFS(source, destination, vertices_size, predecessor, distance)) {
            System.out.println("Given source and destination" + "are not connected");
            return;
        }

        // LinkedList to store path
        LinkedList<Integer> path = new LinkedList<Integer>();
        int crawl = destination;
        path.add(crawl);
        while (predecessor[crawl] != -1) {
            path.add(predecessor[crawl]);
            crawl = predecessor[crawl];
        }

        // Print distance
        System.out.println("Shortest path length is: " + distance[destination]);

        // Print path
        System.out.println("Path is ::");
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print(path.get(i) + " ");
        }
    }

    // a modified version of BFS that stores predecessor
    // of each vertex in array pred
    // and its distance from source in array dist
    boolean BFS(int src, int dest, int v, int[] pred, int[] dist) {
        // a queue to maintain queue of vertices whose
        // adjacency list is to be scanned as per normal
        // BFS algorithm using LinkedList of Integer type
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // boolean array visited[] which stores the
        // information whether ith vertex is reached
        // at least once in the Breadth first search
        boolean[] visited = new boolean[v];

        // initially all vertices are unvisited
        // so v[i] for all i is false
        // and as no path is yet constructed
        // dist[i] for all i set to infinity
        for (int i = 0; i < v; i++) {
            visited[i] = false;
            dist[i] = Integer.MAX_VALUE;
            pred[i] = -1;
        }

        // now source is first to be visited and
        // distance from source to itself should be 0
        visited[src] = true;
        dist[src] = 0;
        queue.add(src);

        // bfs Algorithm
        while (!queue.isEmpty()) {
            int u = queue.remove();
            for (int i = 0; i < adj.get(u).size(); i++) {
                if (!visited[adj.get(u).get(i)]) {
                    visited[adj.get(u).get(i)] = true;
                    dist[adj.get(u).get(i)] = dist[u] + 1;
                    pred[adj.get(u).get(i)] = u;
                    queue.add(adj.get(u).get(i));

                    // stopping condition (when we find
                    // our destination)
                    if (adj.get(u).get(i) == dest) return true;
                }
            }
        }
        return false;
    }
}