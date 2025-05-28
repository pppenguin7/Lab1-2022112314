package graph;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class GraphTest {

    @Test
    public void testBridgeWords_Valid() throws IOException {
        Graph g = new Graph();
        g.buildGraphFromFile("D:\\university\\软件工程\\2022112314-Lab1-code\\Easy Test.txt");
        String result = g.queryBridgeWords("shared", "report");
        assertEquals("The bridge words from shared to report are: the", result);
    }

    @Test
    public void testBridgeWords_AnotherValid() throws IOException {
        Graph g = new Graph();
        g.buildGraphFromFile("D:\\university\\软件工程\\2022112314-Lab1-code\\Easy Test.txt");
        String result = g.queryBridgeWords("the", "");
        assertEquals("No word1 or word2 in the graph!", result);
    }

    @Test
    public void testBridgeWords_NoWord2() throws IOException {
        Graph g = new Graph();
        g.buildGraphFromFile("D:\\university\\软件工程\\2022112314-Lab1-code\\Easy Test.txt");
        String result = g.queryBridgeWords("the", "@");
        assertEquals("No word1 or word2 in the graph!", result);
    }

    @Test
    public void testBridgeWords_NoWord1() throws IOException {
        Graph g = new Graph();
        g.buildGraphFromFile("D:\\university\\软件工程\\2022112314-Lab1-code\\Easy Test.txt");
        String result = g.queryBridgeWords("xyz", "report");
        assertEquals("No word1 or word2 in the graph!", result);
    }

    @Test
    public void testBridgeWords_NoBridge() throws IOException {
        Graph g = new Graph();
        g.buildGraphFromFile("D:\\university\\软件工程\\2022112314-Lab1-code\\Easy Test.txt");
        String result = g.queryBridgeWords("shared", "with");
        assertEquals("No bridge words from shared to with!", result);
    }
}
