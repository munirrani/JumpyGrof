package jumpygrof;

import jumpygrof.datastructure.Graph;
import jumpygrof.datastructure.LinkedList;

import javax.swing.*;
import jumpygrof.TextReader.TextReader;

public class Simulation extends JFrame {

    private Graph<Point, Integer> graph;
    private LinkedList<Point> pointList;
    private LinkedList<Kangaroo> kangarooList;

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
        TextReader reader = new TextReader();
        
        //Initialize points
        reader.read("pointTest.txt"); //read point input text file: pointTest.txt
        while(!reader.getIdQueue().isEmpty()){
            String idHolder = (String) reader.getIdQueue().dequeue();
            int foodAvailableHolder = (int) reader.getFoodAvailableQueue().dequeue(), kangarooCapacityHolder = (int) reader.getKangarooCapacityQueue().dequeue();
            pointList.add(new Point(idHolder, foodAvailableHolder, kangarooCapacityHolder));
        }
//        pointList.add(new Point("1", 20, 10));
//        pointList.add(new Point("2", 5, 3));
//        pointList.add(new Point("3", 10, 8));
//        pointList.add(new Point("4", 11, 6));

        for (int i = 0; i < pointList.size(); i++) graph.addVertice(pointList.get(i));
        
        //Initialize point edges
        while(!reader.getWeightQueue().isEmpty()){
            int from = (int) reader.getFromQueue().dequeue();
            int to = (int) reader.getToQueue().dequeue();
            int weight = (int) reader.getWeightQueue().dequeue();
            graph.addEdge(pointList.get(from), pointList.get(to), weight);
        }

//        graph.addEdge(pointList.get(0), pointList.get(1), 4);
//        graph.addEdge(pointList.get(0), pointList.get(2), 1);
//        graph.addEdge(pointList.get(0), pointList.get(3), 5);
//        graph.addEdge(pointList.get(2), pointList.get(3), 3);


        // Kangaroos
        
        //Initialize kangaroos
        reader.read("kangarooTest.txt"); //read kangaroo input text file: kangarooTest.txt
        while(!reader.getGenderQueue().isEmpty()){
            boolean genderHolder = (boolean) reader.getGenderQueue().dequeue();
            int capacityHolder = reader.getCapacityQueue().dequeue();
            kangarooList.add(new Kangaroo(genderHolder, capacityHolder));
        }
        
//        kangarooList.add(new Kangaroo(false, 6));
//        kangarooList.add(new Kangaroo(true, 5));
//        kangarooList.add(new Kangaroo(true, 3));
//        kangarooList.add(new Kangaroo(false, 10));
//        kangarooList.add(new Kangaroo(false, 6));
        
        //Initialize kangaroo starting points
        for(int i = 0; i<kangarooList.size(); i++){
            int startingPointHolder = reader.getStartingPointQueue().dequeue();
            pointList.get(startingPointHolder).addKangaroo(kangarooList.get(i));
        }

//        pointList.get(0).addKangaroo(kangarooList.get(0));
//        pointList.get(1).addKangaroo(kangarooList.get(1));
//        pointList.get(3).addKangaroo(kangarooList.get(2));
//        pointList.get(0).addKangaroo(kangarooList.get(3));
//        pointList.get(2).addKangaroo(kangarooList.get(4));
    }

    private void start() {
        // Kangaroo starts picking up the available food into their pouches
        for (int i = 0; i < pointList.size(); i++) pointList.get(i).startPickupFood();
        printStatus();
        int count = 0;
        while (count != 5) { // For testing
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
    }

    /*
    Determines next point for kangaroo to hop
    Returns null if kangaroo cannot move anywhere
     */
    private Point whereToMove(Kangaroo kangaroo, Point point) {
        LinkedList<Point> nodes = graph.getAdjascent(point);
        if (nodes.size() != 0) {
            int max = Integer.MIN_VALUE;
            Point to = null; // initialise variable
            for (int i = 0; i < nodes.size(); i++) {
                Point possiblePoint = nodes.get(i);
                if (possiblePoint.compareTo(point) == 0) continue; // No need to compare between same points
                int worth = getPointWorth(kangaroo, possiblePoint);
                System.out.println(kangaroo.toString() + " is considering " + possiblePoint.toString() + " with worth of " + worth);
                if (worth > max) {
                    max = worth;
                    to = possiblePoint;
                }
            }
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
        return worth;
    }

    public void move(Kangaroo kangaroo, Point from, Point to) {
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
            System.out.println(kangaroo.toString() + " is sharing " + to.getCurrentCapacity() +
                    " food to each kangaroo in " + to.toString());
            foodInPouch -= to.getCurrentCapacity();
        }

        kangaroo.setCurrentFoodAmount(foodInPouch);
        to.setCurrentFoodAmount(foodInPoint);

        from.removeKangaroo(kangaroo);
        to.addKangaroo(kangaroo);
    }

    private void printStatus() {
        for (int i = 0; i < pointList.size(); i++) {
            Point currentPoint = pointList.get(i);
            System.out.println("Point " + currentPoint.getID() + " : " + currentPoint.getCurrentFoodAmount());
        }
        for (int i = 0; i < kangarooList.size(); i++) {
            Kangaroo currentKangaroo = kangarooList.get(i);
            System.out.println("Kangaroo " + currentKangaroo.getID() + " is now at Point " + currentKangaroo.getCurrentPoint().getID() +
                    " with food amount of " + currentKangaroo.getCurrentFoodAmount());
        }
    }
}
