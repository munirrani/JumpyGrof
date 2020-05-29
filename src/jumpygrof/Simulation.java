package jumpygrof;

import jumpygrof.datastructure.Graph;
import jumpygrof.datastructure.LinkedList;

import javax.swing.*;

public class Simulation extends JFrame {

    private Graph<Point, Integer> graph;
    private LinkedList<Point> pointList;
    private LinkedList<Kangaroo> kangarooList;
    private int COLONY_MAX = 0;
    private boolean hasFormedColony = false;

    Simulation() {
        setup();
        addInput();
        start();
    }

    private void setup() {
        // JFrame
        setSize(600, 600);
        setTitle("Jumpy Grof");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        graph = new Graph<>();
        pointList = new LinkedList();
        kangarooList = new LinkedList();
    }

    private void addInput() {
        //Points and Edges
        pointList.add(new Point("1", 16, 10));
        pointList.add(new Point("2", 5, 3));
        pointList.add(new Point("3", 10, 8));
        pointList.add(new Point("4", 11, 6));

        for (int i = 0; i < pointList.size(); i++) graph.addVertice(pointList.get(i));

        graph.addEdge(pointList.get(0), pointList.get(1), 4);
        graph.addEdge(pointList.get(0), pointList.get(2), 1);
        graph.addEdge(pointList.get(0), pointList.get(3), 5);
        graph.addEdge(pointList.get(2), pointList.get(3), 3);


        // Kangaroos
        kangarooList.add(new Kangaroo(true, 6));
        kangarooList.add(new Kangaroo(false, 5));
        kangarooList.add(new Kangaroo(false, 3));
        kangarooList.add(new Kangaroo(true, 10));
        kangarooList.add(new Kangaroo(true, 6));

        pointList.get(0).addKangaroo(kangarooList.get(0));
        pointList.get(1).addKangaroo(kangarooList.get(1));
        pointList.get(3).addKangaroo(kangarooList.get(2));
        pointList.get(0).addKangaroo(kangarooList.get(3));
        pointList.get(2).addKangaroo(kangarooList.get(4));

        COLONY_MAX = 3;
    }

    private void start() {
        // Kangaroo starts picking up the available food into their pouches
        System.out.println("Current Food Available");
        for (int i = 0; i < pointList.size(); i++) {
            Point currentPoint = pointList.get(i);
            currentPoint.startPickupFood();
            System.out.println("Point " + currentPoint.getID() + " : " + currentPoint.getCurrentFoodAmount());
        }
        for (int i = 0; i < kangarooList.size(); i++) {
            Kangaroo currentKangaroo = kangarooList.get(i);
            System.out.println("Kangaroo " + currentKangaroo.getID() + " is now at Point " + currentKangaroo.getCurrentPoint().getID() +
                    " with food amount of " + currentKangaroo.getCurrentFoodAmount());
        }
        while (!hasFormedColony) {
            for (int i = 0; i < kangarooList.size(); i++) {
                Kangaroo currentKangaroo = kangarooList.get(i);
            }
            break;
        }
    }

    private Point whereToMove(Point point) {
        LinkedList<Point> nodes = graph.getAdjascent(point);
        if (nodes.size() != 0) {
            for (int i = 0; i < nodes.size(); i++) {

            }
        }
        return null;
    }

    public void move(Kangaroo kangaroo, Point from, Point to) {
        from.removeKangaroo(kangaroo);
        to.addKangaroo(kangaroo);
        if (to.getCurrentCapacity() == COLONY_MAX) {
            System.out.println("Point " + to.getID() + " got to form a colony!");
            hasFormedColony = true;
        }
    }
}
