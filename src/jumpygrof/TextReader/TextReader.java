/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof.TextReader;

import jumpygrof.datastructure.LinkedList;
import jumpygrof.datastructure.Queue;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author USER
 */
public class TextReader {
    
    private Queue idQueue = new Queue();
    private Queue foodAvailableQueue = new Queue();
    private Queue kangarooCapacityQueue = new Queue();
    private Queue fromQueue = new Queue();
    private Queue toQueue = new Queue();
    private Queue weightQueue = new Queue();
    
    private Queue genderQueue = new Queue();
    private Queue<Integer> capacityQueue = new Queue();
    private Queue<Integer> startingPointQueue = new Queue();
    private LinkedList<Integer> fromArray = new LinkedList();
    private LinkedList<Integer> toArray = new LinkedList();

    public void read(String path){
        if (path.contains("pointTest.txt")) {
            pointReader(path);
        } else if (path.contains("kangarooTest.txt")){
            kangarooReader(path);
        }
    }

    private void pointReader(String path) {
        try {
            Scanner scanner = new Scanner(new FileInputStream(path));
            String currentPoint = "";
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 3) { // Read as new points
                    idQueue.enqueue(parts[0]);
                    foodAvailableQueue.enqueue(Integer.parseInt(parts[1]));
                    kangarooCapacityQueue.enqueue(Integer.parseInt(parts[2]));
                    currentPoint = parts[0];
                } else if (parts.length == 2) { // Read as edges
                    fromQueue.enqueue(Integer.parseInt(currentPoint));
                    fromArray.add(Integer.parseInt(currentPoint));
                    toQueue.enqueue(Integer.parseInt(parts[0]));
                    toArray.add(Integer.parseInt(parts[0]));
                    weightQueue.enqueue(Integer.parseInt(parts[1]));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Logger.getLogger(TextReader.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void kangarooReader(String path)  {
        try {
            Scanner scanner = new Scanner(new FileInputStream(path));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    genderQueue.enqueue(parts[0].contentEquals("f"));
                    capacityQueue.enqueue(Integer.parseInt(parts[1]));
                    startingPointQueue.enqueue(Integer.parseInt(parts[2]));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            Logger.getLogger(TextReader.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Queue getIdQueue() {
        return idQueue;
    }

    public Queue getFoodAvailableQueue() {
        return foodAvailableQueue;
    }

    public Queue getKangarooCapacityQueue() {
        return kangarooCapacityQueue;
    }

    public Queue getFromQueue() {
        return fromQueue;
    }

    public Queue getToQueue() {
        return toQueue;
    }

    public Queue getWeightQueue() {
        return weightQueue;
    }

    public Queue getGenderQueue() {
        return genderQueue;
    }

    public Queue<Integer> getCapacityQueue() {
        return capacityQueue;
    }

    public Queue<Integer> getStartingPointQueue() {
        return startingPointQueue;
    }

    public LinkedList<Integer> getFromList() {
        return fromArray;
    }

    public LinkedList<Integer> getToList() {
        return toArray;
    }
}
