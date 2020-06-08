import java.util.*;


public class Delivery {
    private DijGraph westLafayette;//The graph
    private Node restaurant;//The vertex that the driver start
    private Node[] customer;//The vertices that the driver need to pass through
    private double slope;//Tip percentage function slope
    private double intercept;//Tip percentage function intercept
    private double [] order;//The order amount from each customer
    public Delivery (DijGraph graph,Node restaurant, Node[] customer, double slope, double intercept, double[] order){
        this.westLafayette = graph;
        this.restaurant = restaurant;
        this.customer = customer;
        this.slope = slope;
        this.intercept  = intercept;
        this.order = order;
    }

    //Finding the best path that the driver can earn most tips
    //Each time the driver only picks up three orders
    //Picking up N orders and find the maximum tips will be NP-hard
    public double bestPath(){
        double max = 0.0;
        int dist1, dist2, dist3;
        double tip1, tip2, tip3;
        tip1 = 0;
        tip2 = 0;
        tip3 = 0;
        Dist[] first, second, third;
        first = westLafayette.dijkstra(restaurant.getNodeNumber());
        for (int i = 0; i < 3; i++) {
            dist1 = first[customer[i].getNodeNumber()].getDist();
            second = westLafayette.dijkstra(customer[i].getNodeNumber());
            tip1 = (slope * dist1 + intercept) * .01 * order[i];
            for (int j = 0; j < 3; j++) {
                if (j == i) {
                    continue;
                }
                dist2 = second[customer[j].getNodeNumber()].getDist() + dist1;
                tip2 = (slope * dist2 + intercept) * .01 * order[j];
                third = westLafayette.dijkstra(customer[j].getNodeNumber());
                for (int k = 0; k < 3; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    dist3 = third[customer[k].getNodeNumber()].getDist() + dist2;
                    tip3 = (slope * dist3 + intercept) * .01 * order[k];
                    if (max < tip1 + tip2 + tip3) {
                        max = tip1 + tip2 + tip3;
                    }
                }
            }
        }

        return max;
    }

}


//            for (int z = 0; z < 3; z++) {
//                if (customer[z].getNodeNumber() == first[i].getNodeNumber()) {
//                    tip1 = order[z] *( (dist1 * slope + intercept) * .01);
//                }
//            }
//tip1 = order[first[i].getNodeNumber() % 3] *( (dist1 * slope + intercept) * .01);