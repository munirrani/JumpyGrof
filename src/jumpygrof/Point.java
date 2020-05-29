package jumpygrof;

import jumpygrof.datastructure.LinkedList;

public class Point implements Comparable<Point> {

    private String ID;
    private int foodAvailable;
    private int kangarooCapacity;
    private LinkedList<Kangaroo> kangarooList;
    private int currentFoodAmount;

    public Point(String ID, int foodAvailable, int kangarooCapacity) {
        this.ID = ID;
        this.foodAvailable = foodAvailable;
        this.kangarooCapacity = kangarooCapacity;
        kangarooList = new LinkedList();
        currentFoodAmount = foodAvailable;
    }

    public String getID() { return ID; }
    public int getFoodAvailable() { return foodAvailable; }
    public int getCurrentCapacity() { return kangarooList.size(); }
    public void addKangaroo(Kangaroo kangaroo) {
        kangaroo.setCurrentPoint(this);
        kangarooList.add(kangaroo);
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

    @Override
    public int compareTo(Point o) { // Compare each Point by ID name
        return o.getID().compareTo(ID);
    }
}
