package jumpygrof;

import jumpygrof.datastructure.Graph;
import jumpygrof.datastructure.LinkedList;

import javax.swing.*;
import jumpygrof.TextReader.TextReader;

public class Simulation extends JFrame {

    private Graph<Point, Integer> graph;
    private LinkedList<Point> pointList;
    private LinkedList<Kangaroo> kangarooList;
    private int hopCount = 0;
    private boolean foodRegeneration = true;
    private static final int FOOD_REGENERATION_INTERVAL = 25; // How many kangaroo hops before generate

    Simulation() {
        setup();
        addInput();
        start();
    }

    Simulation(boolean hasFoodRegeneration) {
        this();
        foodRegeneration = hasFoodRegeneration;
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
        TextReader reader = new TextReader();
        
        //Initialize points
        reader.read("src/jumpygrof/pointTest.txt"); //read point input text file: pointTest.txt
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
        reader.read("src/jumpygrof/kangarooTest.txt"); //read kangaroo input text file: kangarooTest.txt
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
            System.out.println("Kangaroo " + currentKangaroo.getID() + " is now at Point " + currentKangaroo.getCurrentPoint().getID() +
                    " with food amount of " + currentKangaroo.getCurrentFoodAmount());
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
}
