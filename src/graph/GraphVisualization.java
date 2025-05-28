package graph;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphVisualization extends JPanel {
    private final Graph graph;
    private final Map<String, Point> nodePositions;
    private static final int NODE_RADIUS = 20;

    public GraphVisualization(Graph graph) {
        this.graph = graph;
        this.nodePositions = new HashMap<>();
        calculateNodePositions();
        setPreferredSize(new Dimension(800, 600)); // 设置画布大小
    }

    /**
     * 计算节点位置（圆形布局）
     */
    private void calculateNodePositions() {
        int centerX = 400, centerY = 300;
        int radius = Math.min(300, 200 + graph.getNodeCount() * 10); // 动态调整半径
        double angle = 2 * Math.PI / graph.getNodeCount();

        int i = 0;
        for (String node : graph.getAllNodes()) {
            double theta = i * angle;
            int x = centerX + (int) (radius * Math.cos(theta));
            int y = centerY + (int) (radius * Math.sin(theta));
            nodePositions.put(node, new Point(x, y));
            i++;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制边
        g2.setColor(Color.BLUE);
        for (Map.Entry<String, Map<String, Integer>> entry : graph.getAdjacencyList().entrySet()) {
            String source = entry.getKey();
            Point p1 = nodePositions.get(source);

            for (Map.Entry<String, Integer> edge : entry.getValue().entrySet()) {
                String target = edge.getKey();
                Point p2 = nodePositions.get(target);

                // 绘制带箭头的边
                drawArrowLine(g2, p1.x, p1.y, p2.x, p2.y, 15, 5);

                // 绘制权重
                int weight = edge.getValue();
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g2.drawString(String.valueOf(weight), midX, midY);
            }
        }

        // 绘制节点
        g2.setColor(new Color(70, 130, 180)); // 深蓝色节点
        for (Map.Entry<String, Point> entry : nodePositions.entrySet()) {
            Point p = entry.getValue();
            String node = entry.getKey();

            g2.fill(new Ellipse2D.Double(
                    p.x - NODE_RADIUS, p.y - NODE_RADIUS,
                    NODE_RADIUS * 2, NODE_RADIUS * 2));

            // 绘制节点标签（居中）
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(node);
            g2.setColor(Color.WHITE);
            g2.drawString(node, p.x - textWidth / 2, p.y + fm.getAscent() / 2 - 2);
            g2.setColor(new Color(70, 130, 180));
        }
    }

    /**
     * 绘制带箭头的线
     */
    private void drawArrowLine(Graphics2D g2, int x1, int y1, int x2, int y2, int arrowWidth, int arrowHeight) {
        g2.drawLine(x1, y1, x2, y2);

        // 计算箭头角度
        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        double sin = Math.sin(angle), cos = Math.cos(angle);

        // 箭头基点（略微缩短以避免覆盖节点）
        int baseX = (int) (x2 - NODE_RADIUS * cos);
        int baseY = (int) (y2 - NODE_RADIUS * sin);

        // 箭头两翼的点
        int x3 = (int) (baseX - arrowHeight * cos + arrowWidth * sin);
        int y3 = (int) (baseY - arrowHeight * sin - arrowWidth * cos);
        int x4 = (int) (baseX - arrowHeight * cos - arrowWidth * sin);
        int y4 = (int) (baseY - arrowHeight * sin + arrowWidth * cos);

        // 填充箭头
        g2.fillPolygon(new int[]{baseX, x3, x4}, new int[]{baseY, y3, y4}, 3);
    }

    /**
     * 显示图形窗口
     */
    public static void showGraph(Graph graph) {
        JFrame frame = new JFrame("有向图可视化");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(new GraphVisualization(graph));
        frame.pack();
        frame.setLocationRelativeTo(null); // 居中窗口
        frame.setVisible(true);
    }
    // 在GraphVisualization类中添加
    public void saveToImage(String filename) {
        BufferedImage image = new BufferedImage(
                getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        paint(g2);
        g2.dispose();
        try {
            ImageIO.write(image, "PNG", new File(filename));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "保存失败: " + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

}