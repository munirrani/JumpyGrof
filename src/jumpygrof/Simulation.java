package jumpygrof;

import jumpygrof.datastructure.Graph;
import jumpygrof.datastructure.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.*;
import jumpygrof.TextReader.TextReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Simulation extends JFrame {

    private int hopCount = 0;
    private Graph<Point, Integer> graph;
    private LinkedList<Point> pointList;
    private LinkedList<Kangaroo> kangarooList;
    private TextReader reader = new TextReader();
    private boolean foodRegeneration = false;
    private static final int FOOD_REGENERATION_INTERVAL = 23; // How many kangaroo hops before generate

    //JFrame Stuff
    private NodePanel nodePanel = new NodePanel();
    private ArrayList<PointBox> nodeArrayList = new ArrayList<>();
    private BufferedImage background;

    Simulation() {
        simulationSetup();
        addInput();
        JFrameSetup();
        start();
    }

    Simulation(boolean hasFoodRegeneration) {
        this();
        foodRegeneration = hasFoodRegeneration;
    }

    private void simulationSetup() {
        graph = new Graph<>();
        pointList = new LinkedList();
        kangarooList = new LinkedList();
    }

    private void JFrameSetup() {
        try {
            background = ImageIO.read(new File("src/jumpygrof/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        initNodes();
        setSize(600, 600);
        setTitle("Jumpy Grof");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addInput() {
        //Initialize points
        reader.read("src/jumpygrof/pointTest.txt");
        while(!reader.getIdQueue().isEmpty()){
            String idHolder = (String) reader.getIdQueue().dequeue();
            int foodAvailableHolder = (int) reader.getFoodAvailableQueue().dequeue(), kangarooCapacityHolder = (int) reader.getKangarooCapacityQueue().dequeue();
            pointList.add(new Point(idHolder, foodAvailableHolder, kangarooCapacityHolder));
        }

        for (int i = 0; i < pointList.size(); i++) graph.addVertice(pointList.get(i));
        
        //Initialize edges
        while(!reader.getWeightQueue().isEmpty()){
            int from = (int) reader.getFromQueue().dequeue() - 1;
            int to = (int) reader.getToQueue().dequeue() - 1;
            int weight = (int) reader.getWeightQueue().dequeue();
            graph.addEdge(pointList.get(from), pointList.get(to), weight);
        }
        
        //Initialize kangaroos
        reader.read("src/jumpygrof/kangarooTest.txt");
        while(!reader.getGenderQueue().isEmpty()){
            boolean genderHolder = (boolean) reader.getGenderQueue().dequeue();
            int capacityHolder = reader.getCapacityQueue().dequeue();
            kangarooList.add(new Kangaroo(genderHolder, capacityHolder));
        }
        
        //Initialize kangaroo starting points
        for(int i = 0; i<kangarooList.size(); i++){
            int startingPointHolder = reader.getStartingPointQueue().dequeue() - 1;
            pointList.get(startingPointHolder).addKangaroo(kangarooList.get(i));
        }
    }

    private void start() {
        // Kangaroo starts picking up the available food into their pouches
        for (int i = 0; i < pointList.size(); i++) pointList.get(i).startPickupFood();
        printStatus();
        int count = 0;
        while (count != 50) {
            for (int i = 0; i < kangarooList.size(); i++) {
                Kangaroo currentKangaroo = kangarooList.get(i);
                if (currentKangaroo.isFemale() || currentKangaroo.isInAColony()) continue; // Only males allowed to hop and only in colony
                Point currentPoint = currentKangaroo.getCurrentPoint();
                Point nextPoint = whereToMove(currentKangaroo, currentPoint);
                if (nextPoint != null && canHop(currentKangaroo, nextPoint)) move(currentKangaroo, currentPoint, nextPoint);
            }
            count++;
        }
        printStatus();
        System.out.println();
        System.out.println("Total hop count : " + hopCount);
        printColony();
    }

    /*
    Determines next point for kangaroo to hop
    Returns null if kangaroo cannot move anywhere
     */
    private Point whereToMove(Kangaroo kangaroo, Point point) {
        LinkedList<Point> nodes = graph.getAdjascent(point);
        if (nodes.size() != 0) {
            int max = Integer.MIN_VALUE;
            Point to = null;
            for (int i = 0; i < nodes.size(); i++) {
                Point possiblePoint = nodes.get(i);
                if (possiblePoint.compareTo(point) == 0 || possiblePoint.isFull()) continue;
                int worth = getPointWorth(kangaroo, possiblePoint);
                if (worth > max) {
                    max = worth;
                    to = possiblePoint;
                }
            }
            if (max < 0) return null;
            return to;
        }
        return null;
    }

    private int getFoodNeededToHop(Kangaroo kangaroo, Point to) {
        int height = graph.getWeight(kangaroo.getCurrentPoint(), to);
        return height + (kangaroo.getCurrentFoodAmount()/2);
    }

    private boolean canHop(Kangaroo kangaroo, Point to) {
        if (to.isFull()) {
            return false;
        } else if (to.isAColony()) {
            return getFoodNeededToHop(kangaroo, to) + to.getCurrentCapacity() <= (kangaroo.getCurrentFoodAmount() + to.getCurrentFoodAmount());
        } else {
            return getFoodNeededToHop(kangaroo, to) <= (kangaroo.getCurrentFoodAmount() + to.getCurrentFoodAmount());
        }
    }

    private int getPossibleExtraFood(Kangaroo kangaroo, Point to) {
        return to.getCurrentFoodAmount() - getFoodNeededToHop(kangaroo, to);
    }

    private int getPointWorth(Kangaroo kangaroo, Point to) {
        int worth = 0;
        worth += getPossibleExtraFood(kangaroo, to);
        worth += to.getCurrentFemaleKangaroo();
        if (getPossibleExtraFood(kangaroo, to) < 0) return -1;
        return worth;
    }

    public void move(Kangaroo kangaroo, Point from, Point to) {
        hopCount++;
        System.out.println("Moving " + kangaroo.toString() + " from " + from.toString() + " to " + to.toString());
        int foodInPouch = kangaroo.getCurrentFoodAmount();
        int foodInPoint = to.getCurrentFoodAmount();
        int foodNeeded = getFoodNeededToHop(kangaroo, to);

        foodInPoint -= foodNeeded;
        if (foodInPoint < 0) {
            foodInPouch += foodInPoint; // Use up the remaining food needed in their pouch
            foodInPoint = 0;
        } else if (foodInPoint > 0) { // Kangaroo picks up the extra foods at destination point to its capacity
            int extraFood = getPossibleExtraFood(kangaroo, to);
            int capableTotalFood = foodInPouch + extraFood;
            int difference = 0;
            if (capableTotalFood > kangaroo.getCapacity()) { // If it exceeds what it can carry, sum it back to the difference
                difference = capableTotalFood - kangaroo.getCapacity();
                foodInPouch = kangaroo.getCapacity();
            } else {
                foodInPouch = capableTotalFood;
            }
            foodInPoint =  foodInPoint - extraFood + difference;
        }
        if (to.isAColony()) {
            foodInPouch -= to.getCurrentCapacity();
        }

        kangaroo.setCurrentFoodAmount(foodInPouch);
        to.setCurrentFoodAmount(foodInPoint);

        from.removeKangaroo(kangaroo);
        to.addKangaroo(kangaroo);

        System.out.println(to.toString() + " now has " + to.getCurrentFemaleKangaroo() + "F " +
                (to.getCurrentCapacity() - to.getCurrentFemaleKangaroo()) + "M");

        if(foodRegeneration && hopCount % FOOD_REGENERATION_INTERVAL == 0) {
            for (int i = 0; i < pointList.size(); i++) pointList.get(i).generateFood();
            System.out.println("Food has been regenerated");
        }
    }

    private void printStatus() {
        for (int i = 0; i < pointList.size(); i++) {
            Point currentPoint = pointList.get(i);
            System.out.println("Point " + currentPoint.getID() + " : " + currentPoint.getCurrentFoodAmount());
        }
        for (int i = 0; i < kangarooList.size(); i++) {
            Kangaroo currentKangaroo = kangarooList.get(i);
            if (!currentKangaroo.isFemale()) {
                System.out.println("Kangaroo(M) " + currentKangaroo.getID() + " is now at Point " + currentKangaroo.getCurrentPoint().getID() +
                        " with food amount of " + currentKangaroo.getCurrentFoodAmount());
            } else {
                System.out.println("Kangaroo(F) " + currentKangaroo.getID() + " is now at Point " + currentKangaroo.getCurrentPoint().getID() +
                        " with food amount of " + currentKangaroo.getCurrentFoodAmount());
            }
        }
    }

    private void printColony() {
        System.out.println("Total colonies: " + pointList.get(0).getTotalColony());
        for (int i = 0; i < pointList.size(); i++) {
            Point currentPoint = pointList.get(i);
            if (currentPoint.isAColony()) {
                System.out.print(currentPoint.toString());
                if (currentPoint.isFull()) System.out.print(" (Full)");
                System.out.println();
            }
        }
    }

    private void initNodes() {
        PointBox node1 = new PointBox(pointList.get(0).getID(), 410, 270);
        PointBox node2 = new PointBox(pointList.get(1).getID(),240, 290);
        PointBox node3 = new PointBox(pointList.get(2).getID(),340, 330);
        PointBox node4 = new PointBox(pointList.get(3).getID(),500, 260);
        PointBox node5 = new PointBox(pointList.get(4).getID(),420, 380);
        PointBox node6 = new PointBox(pointList.get(5).getID(),470, 450);
        PointBox node7 = new PointBox(pointList.get(6).getID(),520, 350);
        PointBox node8 = new PointBox(pointList.get(7).getID(),290, 360);
        PointBox node9 = new PointBox(pointList.get(8).getID(),180, 380);
        PointBox node10 = new PointBox(pointList.get(9).getID(),90, 340);
        PointBox node11 = new PointBox(pointList.get(10).getID(),70, 260);
        PointBox node12 = new PointBox(pointList.get(11).getID(),150, 240);
        PointBox node13 = new PointBox(pointList.get(12).getID(),200, 190);
        PointBox node14 = new PointBox(pointList.get(13).getID(),270, 230);
        PointBox node15 = new PointBox(pointList.get(14).getID(),250, 160);
        PointBox node16 = new PointBox(pointList.get(15).getID(),350, 220);
        PointBox node17 = new PointBox(pointList.get(16).getID(),360, 180);
        PointBox node18 = new PointBox(pointList.get(17).getID(),450, 180);
        PointBox node19 = new PointBox(pointList.get(18).getID(),445, 130);

        nodeArrayList.add(node1);
        nodeArrayList.add(node2);
        nodeArrayList.add(node3);
        nodeArrayList.add(node4);
        nodeArrayList.add(node5);
        nodeArrayList.add(node6);
        nodeArrayList.add(node7);
        nodeArrayList.add(node8);
        nodeArrayList.add(node9);
        nodeArrayList.add(node10);
        nodeArrayList.add(node11);
        nodeArrayList.add(node12);
        nodeArrayList.add(node13);
        nodeArrayList.add(node14);
        nodeArrayList.add(node15);
        nodeArrayList.add(node16);
        nodeArrayList.add(node17);
        nodeArrayList.add(node18);
        nodeArrayList.add(node19);

        for (PointBox node : nodeArrayList) {
            nodePanel.add(node);
        }

        add(nodePanel);
    }

    class NodePanel extends JPanel {

        int nodeSize = PointBox.NODE_SIZE;
        LinkedList<Integer> fromList = reader.getFromList();
        LinkedList<Integer> toList = reader.getToList();

        public NodePanel() {
            setLayout(null);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(background, 0,0, (int)getSize().getWidth(), (int)getSize().getHeight(), null);
            for (int i = 0; i < fromList.size(); i++) {
                int fromPoint = fromList.get(i) - 1;
                int toPoint = toList.get(i) - 1;
                g.drawLine(nodeArrayList.get(fromPoint).getX() + nodeSize/2, nodeArrayList.get(fromPoint).getY() + nodeSize/2,
                        nodeArrayList.get(toPoint).getX() + nodeSize/2, nodeArrayList.get(toPoint).getY() + nodeSize/2);
            }
        }
    }
}