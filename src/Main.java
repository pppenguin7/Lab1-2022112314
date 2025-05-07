import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Graph graph = new Graph();
        graph.buildGraphFromFile("D:\\university\\软件工程\\lab1\\Easy Test.txt");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== 图功能菜单 ===");
            System.out.println("1. 显示有向图");
            System.out.println("2. 查询桥接词");
            System.out.println("3. 生成新文本");
            System.out.println("4. 计算最短路径");
            System.out.println("5. 计算 PageRank");
            System.out.println("6. 随机游走");
            System.out.println("7. 可视化图形");
            System.out.println("0. 退出");
            System.out.print("请选择功能：");

            int choice = scanner.nextInt();
            scanner.nextLine();  // 消耗换行符

            switch (choice) {
                case 1 -> graph.showDirectedGraph();
                case 2 -> {
                    System.out.print("输入word1: ");
                    String w1 = scanner.nextLine();
                    System.out.print("输入word2: ");
                    String w2 = scanner.nextLine();
                    System.out.println(graph.queryBridgeWords(w1, w2));
                }
                case 3 -> {
                    System.out.print("输入句子：");
                    String input = scanner.nextLine();
                    System.out.println("生成新文本：");
                    System.out.println(graph.generateNewText(input));
                }
                case 4 -> {
                    System.out.print("起点：");
                    String start = scanner.nextLine();
                    System.out.print("终点：");
                    String end = scanner.nextLine();
                    System.out.println(graph.calcShortestPath(start, end));
                }
                case 5 -> {
                    System.out.print("输入单词：");
                    String word = scanner.nextLine();
                    System.out.println("PageRank：" + graph.calPageRank(word));
                }
                case 6 -> System.out.println(graph.randomWalk());
                case 0 -> {
                    System.out.println("退出程序");
                    return;
                }
                default -> System.out.println("无效选项");
            }
        }
    }
}
