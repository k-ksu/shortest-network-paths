// Ksenia Korchagina

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        int M = scanner.nextInt();

        Graph<Integer, Integer> graph = new Graph<>();

        for (int i = 0; i <= N; i++) {
            graph.addVertex();
        }

        List<List<Integer>> scanned = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            int source = scanner.nextInt();
            int destination = scanner.nextInt();
            int length = scanner.nextInt();
            int bandwidth = scanner.nextInt();
            List<Integer> thisOne = new ArrayList<>();
            thisOne.add(source);
            thisOne.add(destination);
            thisOne.add(length);
            thisOne.add(bandwidth);
            scanned.add(thisOne);
        }
        int start = scanner.nextInt();
        int finish = scanner.nextInt();
        int bandwidth = scanner.nextInt();

        for (List<Integer> list : scanned) {
            int source = list.get(0);
            int destination = list.get(1);
            int length = list.get(2);
            int currentBandwidth = list.get(3);

            if (currentBandwidth >= bandwidth) {
                graph.addEdge(source, destination, length, currentBandwidth);
            }
        }

        int[] shortestPathData = graph.KseniaKorchagina_sp(start, finish);
        int shortestPath = shortestPathData[0];
        int smallestBandwidthOnPath = shortestPathData[1];

        if (shortestPath == -1) {
            System.out.println("IMPOSSIBLE");
        } else {
            List<Integer> shortestPathVertices = graph.constructShortestPath(finish);
            System.out.println(shortestPathVertices.size() + " " + shortestPath + " " + smallestBandwidthOnPath);
            for (int vertex : shortestPathVertices) {
                System.out.print(vertex + " ");
            }
            System.out.println();
        }
    }
}

class Graph<V, E extends Comparable<E>> {

    static class Vertex<V> {
        V id;
        int distance;
        int bandwidth;

        public Vertex(V id, int distance, int bandwidth) {
            this.id = id;
            this.distance = distance;
            this.bandwidth = bandwidth;
        }
    }

    List<List<int[]>> adjacencyList;
    Map<Integer, Integer> numVerticesVisited;
    Map<Integer, Integer> predecessors;

    public Graph() {
        this.adjacencyList = new ArrayList<>();
        this.numVerticesVisited = new HashMap<>();
        this.predecessors = new HashMap<>();
    }

    public void addVertex() {
        this.adjacencyList.add(new ArrayList<>());
    }

    public void addEdge(int source, int destination, int length, int bandwidth) {
        this.adjacencyList.get(source).add(new int[]{destination, length, bandwidth});
    }

    public int[] KseniaKorchagina_sp(int source, int finish) {
        int V = adjacencyList.size();
        PriorityQueue<Vertex<Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(v -> v.distance));
        int[] distances = new int[V];
        int[] smallestBandwidth = new int[V];
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(smallestBandwidth, Integer.MAX_VALUE);
        distances[source] = 0;
        minHeap.offer(new Vertex<>(source, 0, 0));
        numVerticesVisited.put(source, 1);
        predecessors.put(source, -1);

        while (!minHeap.isEmpty()) {
            Vertex<Integer> current = minHeap.poll();
            int u = current.id;
            int distU = current.distance;

            if (u == finish) return new int[]{distU, smallestBandwidth[u]};

            if (distances[u] < distU) continue;

            for (int[] edge : adjacencyList.get(u)) {
                int v = edge[0];
                int weightUV = edge[1];
                int bandwidthUV = edge[2];

                if (distances[u] + weightUV < distances[v]) {
                    distances[v] = distances[u] + weightUV;
                    smallestBandwidth[v] = Math.min(smallestBandwidth[u], bandwidthUV);
                    minHeap.offer(new Vertex<>(v, distances[v], smallestBandwidth[v]));
                    numVerticesVisited.put(v, numVerticesVisited.getOrDefault(u, 0) + 1);
                    predecessors.put(v, u);
                }
            }
        }

        return new int[]{-1, -1};
    }

    public List<Integer> constructShortestPath(int finish) {
        List<Integer> shortestPath = new ArrayList<>();
        int currentVertex = finish;
        while (currentVertex != -1) {
            shortestPath.add(currentVertex);
            currentVertex = predecessors.get(currentVertex);
        }
        Collections.reverse(shortestPath);
        return shortestPath;
    }
}


// References:
// https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/