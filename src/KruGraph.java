import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

public class KruGraph {
    private Vertex[] vertexArr;
    private ArrayList<MyEdge> edgeArr;
    private int vertexCount;
    private int edgeCount;

    //Implement the constructor for KruGraph
    //The format of the input file is the same as the format of the input file in Dijkstra
    public KruGraph(String graph_file)throws IOException{
        File file = new File(graph_file);
        Scanner sc = new Scanner(file);
        vertexCount = sc.nextInt();
        edgeCount = sc.nextInt();
        vertexArr = new Vertex[vertexCount + 1];
        edgeArr = new ArrayList<>(edgeCount);
        for(int i =0; i < vertexCount + 1; i ++){
            if(i != 0) {
                vertexArr[i] = new Vertex(i);
            }
        }
        for(int i = 0;i < edgeCount; i ++){
            int begin = sc.nextInt();
            int end = sc.nextInt();
            int weight = sc.nextInt();
            edgeArr.add(new MyEdge(begin, end,weight));
        }
    }

    //Could be a helper function
    private void addEgde(int from, int to, int weight){
        //insert into edgeArr and sort
    }


    //Implement Kruskal with weighted union find algorithm
    public PriorityQueue<MyEdge> kruskalMST(){
        PriorityQueue<MyEdge> pq = new PriorityQueue<>(edgeCount);
        for (int i = 0; i < edgeCount; i++) {
            pq.add(edgeArr.get(i));
        }

        PriorityQueue<MyEdge> A = new PriorityQueue<>();
        while (A.size() < vertexCount - 1 && pq.size() > 0) {
            MyEdge current = pq.poll();
            if (union(vertexArr[current.getS()], vertexArr[current.getD()])) {
                A.add(current);
            }
        }
        return A;
    }

    //Implement the recursion trick for the leaves to update the parent efficiently
    //Set it as static as always
    public static Vertex find(Vertex x){
        if (x.getParent() == x)
            return x;
        x.updateParent(find(x.getParent()));
        return x.getParent();
    }


    //This function should union two vertices when an edge is added to the MST
    //Return true when the edge can be picked in the MST
    //Otherwise return false
    //Set it as static as always
    public static boolean union(Vertex x, Vertex y){
        Vertex parentX = find(x);
        Vertex parentY = find(y);
        if (parentX.getVertexNumber() == parentY.getVertexNumber()) {
            return false;
        } else {
            if (parentX.getSize() > parentY.getSize()) {
                parentX.updateSize(parentX.getSize() + parentY.getSize());
                parentY.updateParent(parentX);
            } else {
                parentY.updateSize(parentY.getSize() + parentX.getSize());
                parentX.updateParent(parentY);
            }
        }
        return true;
    }

    //This is what we expect for the output format
    //The test cases will follow this format
    public static void printGraph(PriorityQueue<MyEdge> edgeList){
        int turn = edgeList.size();
        for (int i = 0; i < turn; i++) {
            MyEdge edge = edgeList.poll();
            int source = edge.getS();
            int dest = edge.getD();
            if(source > dest){
                int temp = source;
                source = dest;
                dest = temp;
            }
            System.out.println("from: " + source + " to: " + dest + " weight: " + edge.getWeight());
        }
    }

    public static void main(String[] args) throws IOException {
        KruGraph graph = new KruGraph("localtestk1.txt");
        printGraph(graph.kruskalMST());
    }

}
