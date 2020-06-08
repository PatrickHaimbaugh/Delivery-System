import java.io.File;
import java.io.IOException;
import java.util.*;

public class DijGraph {
    static int MAXWEIGHT = 10000000;//The weight of edge will not exceed this number
    private Node[] nodeArr;//The vertices set in the graph
    private int nodeCount;//number of total vertices
    private int edgeCount;//number of total edges

    //Two option for the DijGraph constructor
    //Option 0 is used to build graph with for part 1: implementation for Dijkstra
    //Option 1 is used to build graph with for part 2: simple application of Dijkstra
    public DijGraph(String graph_file, int option)throws IOException{
        if (option == 0){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            edgeCount = sc.nextInt();
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                if(i != 0) {
                    nodeArr[i] = new Node(i);
                }
            }
            for(int i = 0;i < edgeCount; i ++){
                int begin = sc.nextInt();
                int end = sc.nextInt();
                int weight = sc.nextInt();
                nodeArr[begin].addEdge(end, weight);
                nodeArr[end].addEdge(begin,weight);
            }
        }
        else if (option == 1){
            File file = new File(graph_file);
            Scanner sc = new Scanner(file);
            nodeCount = sc.nextInt();
            edgeCount = sc.nextInt();
            nodeArr = new Node[nodeCount + 1];
            for(int i =0; i < nodeCount + 1; i ++){
                if(i != 0){
                    nodeArr[i]= new Node(i, sc.next());
                }
            }
            for(int i = 0;i < edgeCount; i ++){
                String begin = sc.next();
                String end = sc.next();
                int weight = sc.nextInt();
                Node beginNode = findByName(begin);
                Node endNode = findByName(end);
                beginNode.addEdge(endNode.getNodeNumber(), weight);
                endNode.addEdge(beginNode.getNodeNumber(),weight);
            }
        }

    }

    //Finding the single source shortest distances by implementing dijkstra.
    //Using min heap to find the next smallest target
    public  Dist[] dijkstra( int source){
        Dist[] result = new Dist[nodeCount +1];
        Dist[] Q = new Dist[nodeCount];
        int count = nodeCount;
        for (int i = 1; i < nodeCount + 1; i++) {
            if (i != source) {
                result[i] = new Dist(i, MAXWEIGHT);
            } else {
                result[i] = new Dist(i, 0);
            }
            insert(Q, result[i], i - 1);
        }
        while (count != 0) {
            Dist u = extractMin(Q,count);
            count--;
            int nodeNum = u.getNodeNumber();
            //edgeweight from sourse to that number
            HashMap<Integer,Integer> adj = nodeArr[nodeNum].getEdges();
            //edges.containsKey()
            for (int i = 0; i < count; i++) {
                int temp = Q[i].getNodeNumber();
                if (adj.containsKey(temp)) {
                    int alt = u.getDist() + adj.get(temp);
                    if (alt < result[temp].getDist()) {
                        result[temp].updateDist(alt);
                        Q[i].updateDist(alt);
                        insert(Q, Q[i], i);
                    }
                }
            }
        }

        return result;
    }

    //Find the vertex by the location name
    public Node findByName(String name){
        for (int x =1; x < nodeCount + 1; x++){
            if(nodeArr[x].getLocation().equals(name)){
                return nodeArr[x];
            }
        }
        return null;
    }

    //Implement insertion in min heap
    //first insert the element to the end of the heap
    //then swim up the element if necessary
    //Set it as static as always
    public static void insert(Dist [] arr, Dist value, int index){
        //TODO
        arr[index] = value;
        while (true) {
            if (index == 0) {
                break;
            }
            int parent = (index + 1) / 2;
            parent -= 1;
            if (arr[parent].compareTo(arr[index]) == 1) {
                swap(arr, parent, index);
            }
            index--;
        }
    }

    public static void swap(Dist []arr, int index1, int index2){
        Dist temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    //Extract the minimum element in the min heap
    //replace the last element with the root
    //then do minheapify
    //Set it as static as always
    public static Dist extractMin (Dist[] arr, int size){
        //TODO
        Dist min = arr[0];
        swap(arr, 0, size - 1);
        arr[size- 1] = null;
        minheapify(arr, size - 1);
        return min;
    }

    public static void minheapify(Dist[] arr, int size) {
        int current = 1;
        int child = 2;
        while ((child - 1 < size && arr[current - 1].compareTo(arr[child - 1]) == 1) || (child < size && arr[current - 1].compareTo(arr[child]) == 1)) {
            if ((child < size && arr[child - 1].compareTo(arr[child]) != 1) || child >= size) {
                swap(arr, current- 1, child - 1);
                current = current * 2;
                child = current * 2;
            } else {
                swap(arr, current - 1, child);
                current = current * 2 + 1;
                child = current * 2;
            }
        }
    }

    //This will print the shortest distance result
    //The output format will be what we expect to pass the test cases
    public static void printResult(Dist[] result, int source){
        for(int x = 1;  x < result.length; x++){
            if(x != source){
                System.out.println(result[x].getNodeNumber() + " " +result[x].getDist());
            }
        }
    }

    public static void main(String[] args)throws IOException {
        DijGraph graph = new DijGraph("localtest1.txt", 0);
        Dist[] result  = graph.dijkstra(7);
        printResult(result, 7);
    }
}
