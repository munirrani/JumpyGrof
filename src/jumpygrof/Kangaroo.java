package jumpygrof;

public class Kangaroo implements Comparable<Kangaroo> {

    private static int totalKangarooNumber = 0;
    private String ID;
    private boolean isMale;
    private int capacity;
    private int currentFoodAmount;
    private Point currentPoint;

    public Kangaroo(boolean isMale, int capacity) {
        totalKangarooNumber++;
        ID = String.valueOf(totalKangarooNumber);
        this.isMale = isMale;
        this.capacity = capacity;
        currentFoodAmount = 0;
    }

    public int getTotalKangarooNumber() { return totalKangarooNumber; }
    public String getID() { return ID; }
    public boolean isMale() { return isMale; }
    public int getCapacity() { return capacity; }
    public int getCurrentFoodAmount() { return currentFoodAmount; }
    public void setCurrentFoodAmount(int amount) { currentFoodAmount = amount; }
    public Point getCurrentPoint() { return currentPoint; }
    public void setCurrentPoint(Point point) { currentPoint = point; }

    @Override
    public int compareTo(Kangaroo kangaroo) {
        return kangaroo.getTotalKangarooNumber() - totalKangarooNumber;
    }
}
