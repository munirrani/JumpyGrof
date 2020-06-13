package jumpygrof;

public class Kangaroo implements Comparable<Kangaroo> {

    private static int totalKangarooNumber = 0;
    private String ID;
    private boolean isFemale;
    private int capacity;
    private int currentFoodAmount;
    private Point currentPoint;
    private boolean isInAColony = false;

    public Kangaroo(boolean isFemale, int capacity) {
        totalKangarooNumber++;
        ID = String.valueOf(totalKangarooNumber);
        this.isFemale = isFemale;
        this.capacity = capacity;
        currentFoodAmount = 0;
    }

    public int getTotalKangarooNumber() { return totalKangarooNumber; }
    public String getID() { return ID; }
    public boolean isFemale() { return isFemale; }
    public int getCapacity() { return capacity; }
    public int getCurrentFoodAmount() { return currentFoodAmount; }
    public void setCurrentFoodAmount(int amount) { currentFoodAmount = amount; }
    public Point getCurrentPoint() { return currentPoint; }
    public void setCurrentPoint(Point point) { currentPoint = point; }
    public boolean isInAColony(){ return isInAColony; }
    public void setInAColony(boolean state) { isInAColony = state; }

    @Override
    public int compareTo(Kangaroo kangaroo) {
        return kangaroo.getTotalKangarooNumber() - totalKangarooNumber;
    }

    public String toString() {
        return "Kangaroo " + ID;
    }
}
