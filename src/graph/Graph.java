package graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {
    private final Map<String, Map<String, Integer>> adjacencyList = new HashMap<>();
    private final Random rand;  // 将 Random 作为类成员变量

    // 默认构造函数，使用系统时间种子
    public Graph() {
        this.rand = new Random();
    }

    // 可传入种子的构造函数，方便测试随机性
    public Graph(long seed) {
        this.rand = new Random(seed);
    }

    public void buildGraphFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append(" ");
        }
        reader.close();

        String[] words = content.toString().toLowerCase().replaceAll("[^a-z\\s]", "").split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {
            String from = words[i];
            String to = words[i + 1];
            adjacencyList.putIfAbsent(from, new HashMap<>());
            Map<String, Integer> edges = adjacencyList.get(from);
            edges.put(to, edges.getOrDefault(to, 0) + 1);
        }
    }

    public void showDirectedGraph() {
        System.out.println("Directed Graph:");
        for (String from : adjacencyList.keySet()) {
            for (String to : adjacencyList.get(from).keySet()) {
                System.out.println(from + " -> " + to + " [Weight=" + adjacencyList.get(from).get(to) + "]");
            }
        }
    }

    public String queryBridgeWords(String word1, String word2) {
        if (!adjacencyList.containsKey(word1) || !adjacencyList.containsKey(word2)) {
            return "No word1 or word2 in the graph!";
        }

        Set<String> bridgeWords = new HashSet<>();
        Map<String, Integer> word1Edges = adjacencyList.get(word1);
        for (String intermediate : word1Edges.keySet()) {
            Map<String, Integer> nextEdges = adjacencyList.get(intermediate);
            if (nextEdges != null && nextEdges.containsKey(word2)) {
                bridgeWords.add(intermediate);
            }
        }

        if (bridgeWords.isEmpty()) {
            return "No bridge words from " + word1 + " to " + word2 + "!";
        }

        return "The bridge words from " + word1 + " to " + word2 + " are: " + String.join(", ", bridgeWords);
    }

    public String generateNewText(String inputText) {
        String[] words = inputText.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length - 1; i++) {
            result.append(words[i]).append(" ");
            String word1 = words[i];
            String word2 = words[i + 1];
            Set<String> bridgeWords = new HashSet<>();

            Map<String, Integer> edges = adjacencyList.get(word1);
            if (edges != null) {
                for (String inter : edges.keySet()) {
                    Map<String, Integer> nextEdges = adjacencyList.get(inter);
                    if (nextEdges != null && nextEdges.containsKey(word2)) {
                        bridgeWords.add(inter);
                    }
                }
            }

            if (!bridgeWords.isEmpty()) {
                String[] bridges = bridgeWords.toArray(new String[0]);
                result.append(bridges[rand.nextInt(bridges.length)]).append(" ");
            }
        }
        result.append(words[words.length - 1]);
        return result.toString();
    }

    public String calcShortestPath(String start, String end) {
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return "Word not found!";
        }

        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingInt(dist::get));

        for (String node : getAllNodes()) {
            dist.put(node, Integer.MAX_VALUE);
        }
        dist.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            for (Map.Entry<String, Integer> neighbor : adjacencyList.getOrDefault(current, Map.of()).entrySet()) {
                int alt = dist.get(current) + neighbor.getValue();
                if (alt < dist.getOrDefault(neighbor.getKey(), Integer.MAX_VALUE)) {
                    dist.put(neighbor.getKey(), alt);
                    prev.put(neighbor.getKey(), current);
                    queue.add(neighbor.getKey());
                }
            }
        }

        if (!prev.containsKey(end)) return "No path found";

        LinkedList<String> path = new LinkedList<>();
        for (String at = end; at != null; at = prev.get(at)) {
            path.addFirst(at);
        }

        return "Shortest path: " + String.join(" -> ", path);
    }

    public double calPageRank(String target) {
        final double d = 0.85;
        final int maxIterations = 100;
        final double tolerance = 1e-6;

        Map<String, Double> ranks = new HashMap<>();
        Set<String> nodes = getAllNodes();

        int N = nodes.size();
        for (String node : nodes) {
            ranks.put(node, 1.0 / N);
        }

        for (int i = 0; i < maxIterations; i++) {
            Map<String, Double> newRanks = new HashMap<>();
            for (String node : nodes) {
                double rankSum = 0.0;
                for (String from : nodes) {
                    Map<String, Integer> edges = adjacencyList.get(from);
                    if (edges != null && edges.containsKey(node)) {
                        int outDegree = edges.size();
                        rankSum += ranks.get(from) / outDegree;
                    }
                }
                newRanks.put(node, (1 - d) / N + d * rankSum);
            }

            double delta = 0;
            for (String node : nodes) {
                delta += Math.abs(newRanks.get(node) - ranks.get(node));
            }

            ranks = newRanks;
            if (delta < tolerance) break;
        }

        return ranks.getOrDefault(target, 0.0);
    }

    public String randomWalk() {
        List<String> path = new ArrayList<>();
        List<String> nodes = new ArrayList<>(adjacencyList.keySet());
        if (nodes.isEmpty()) return "Empty graph";

        String current = nodes.get(rand.nextInt(nodes.size()));
        path.add(current);

        Set<String> visited = new HashSet<>();
        visited.add(current);

        while (adjacencyList.containsKey(current) && !adjacencyList.get(current).isEmpty()) {
            Map<String, Integer> neighbors = adjacencyList.get(current);
            List<String> nextNodes = new ArrayList<>(neighbors.keySet());
            current = nextNodes.get(rand.nextInt(nextNodes.size()));
            path.add(current);
            if (visited.contains(current)) break;
            visited.add(current);
        }

        return "Random walk path:\n" + String.join(" -> ", path);
    }

    public Map<String, Map<String, Integer>> getAdjacencyList() {
        return adjacencyList;
    }

    public Set<String> getAllNodes() {
        Set<String> allNodes = new HashSet<>(adjacencyList.keySet());
        for (Map<String, Integer> edges : adjacencyList.values()) {
            allNodes.addAll(edges.keySet());
        }
        return allNodes;
    }

    public int getNodeCount() {
        return getAllNodes().size();
    }
}
