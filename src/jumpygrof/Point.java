package jumpygrof;

import jumpygrof.datastructure.LinkedList;

public class Point implements Comparable<Point> {

    private static final int COLONY_MAX = 5;
    private static int totalColony = 0;

    private String ID;
    private int foodAvailable;
    private int kangarooCapacity;
    private LinkedList<Kangaroo> kangarooList;
    private int currentFoodAmount;
    private boolean isAColony = false;

    public Point(String ID, int foodAvailable, int kangarooCapacity) {
        this.ID = ID;
        this.foodAvailable = foodAvailable;
        this.kangarooCapacity = kangarooCapacity;
        kangarooList = new LinkedList();
        currentFoodAmount = foodAvailable;
    }

    public String getID() { return ID; }
    public int getCurrentCapacity() { return kangarooList.size(); }
    public void addKangaroo(Kangaroo kangaroo) {
        kangaroo.setCurrentPoint(this);
        kangarooList.add(kangaroo);
        if (kangarooList.size() == COLONY_MAX) {
            System.out.println(toString() + " got to form a colony!");
            totalColony++;
            isAColony = true;
            for (int i = 0; i < kangarooList.size(); i++) {
                Kangaroo currentKangaroo = kangarooList.get(i);
                currentKangaroo.setInAColony(true);
            }
        } else if (isFull()) {
            System.out.println(toString() + " is full!");
        }
    }

    public void removeKangaroo(Kangaroo kangaroo) {
        kangaroo.setCurrentPoint(null);
        kangarooList.remove(kangaroo);
    }
    public int getCurrentFoodAmount() { return currentFoodAmount; }
    public void setCurrentFoodAmount(int amount) { currentFoodAmount = amount; }

    public void startPickupFood() {
        if (foodAvailable != 0) {
            int index;
            for (index = 0; index < kangarooList.size(); index++) {
                Kangaroo currentKangaroo = kangarooList.get(index);
                currentKangaroo.setCurrentFoodAmount(currentKangaroo.getCapacity());
                currentFoodAmount -= currentKangaroo.getCapacity();
            }
        }
    }

    public int getCurrentFemaleKangaroo() {
        int count = 0;
        for (int i = 0; i < kangarooList.size(); i++) {
            Kangaroo currentKangaroo = kangarooList.get(i);
            if (currentKangaroo.isFemale()) count++;
        }
        return count;
    }

    public boolean isAColony() { return isAColony; }
    public boolean isFull() { return kangarooList.size() == kangarooCapacity; }

    public int getTotalColony() { return totalColony; }

    @Override
    public int compareTo(Point o) { // Compare each Point by ID name
        return o.getID().compareTo(ID);
    }

    public String toString() {
        return "Point " + ID;
    }
}
