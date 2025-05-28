package graph;
import org.junit.Test;
import static org.junit.Assert.*;
public class GraphRandomWalkTest {
    @Test
    public void testRandomWalkEmptyGraph() {
        Graph g = new Graph();
        String result = g.randomWalk();
        assertEquals("Empty graph", result);
    }
    @Test
    public void testRandomWalkSingleNode() {
        Graph g = new Graph();
        // 手动构建图，添加一个节点无边
        g.getAdjacencyList().put("node1", new java.util.HashMap<>());
        String result = g.randomWalk();
        assertTrue(result.startsWith("Random walk path:"));
        assertTrue(result.contains("node1"));
    }
    @Test
    public void testRandomWalkMultipleNodes() {
        Graph g = new Graph();
        // 构造一个简单有向图 node1 -> node2 -> node3
        g.getAdjacencyList().put("node1", new java.util.HashMap<>());
        g.getAdjacencyList().get("node1").put("node2", 1);
        g.getAdjacencyList().put("node2", new java.util.HashMap<>());
        g.getAdjacencyList().get("node2").put("node3", 1);
        g.getAdjacencyList().put("node3", new java.util.HashMap<>());

        String result = g.randomWalk();
        assertTrue(result.startsWith("Random walk path:"));
        // 路径至少包含一个节点名字
        assertTrue(result.contains("node1") || result.contains("node2") || result.contains("node3"));
    }
}
