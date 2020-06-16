/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jumpygrof.TextReader;

/**
 *
 * @author Asus
 */
import jumpygrof.Queue.Queue;
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

    private String path;

    private Queue idQueue = new Queue();
    private Queue foodAvailableQueue = new Queue();
    private Queue kangarooCapacityQueue = new Queue();
    private Queue fromQueue = new Queue();
    private Queue toQueue = new Queue();
    private Queue weightQueue = new Queue();

    private Queue genderQueue = new Queue();
    private Queue<Integer> capacityQueue = new Queue();
    private Queue<Integer> startingPointQueue = new Queue();

    public TextReader(){
    }

    public void read(String path){
        this.path = path;
        titleReader(path);
    }

    private void titleReader(String path){
        String title;
        try {
            Scanner titleRead = new Scanner(new FileInputStream(path));
            title = titleRead.nextLine();
            if(title.equalsIgnoreCase("Points"))
                pointReader(path);

            else if(title.equalsIgnoreCase("Kangaroo")) 
                    kangarooReader(path);

            titleRead.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void pointReader(String path){



        String temp, title;
        int point = 0, count = 0;

        try {
            Scanner pointRead = new Scanner(new FileInputStream(path));
            title = pointRead.next();
            if(title.equalsIgnoreCase("Points")){
                while(pointRead.hasNextLine()){
                    if(count == 0){
                        temp = pointRead.next();
                        idQueue.enqueue(temp);
                        count++;
                    }

                    else if(count == 1){
                        temp = pointRead.next();
                        foodAvailableQueue.enqueue(Integer.parseInt(temp));
                        count++;
                    }

                    else if(count == 2){
                        temp = pointRead.next();
                        kangarooCapacityQueue.enqueue(Integer.parseInt(temp));
                        count++;
                    }

                    else if(count == 3){
                        temp = pointRead.next();
                        if(Integer.parseInt(temp)>0){
                            int paths = Integer.parseInt(temp);
                            while(paths>0){
                                fromQueue.enqueue(point);
                                toQueue.enqueue(Integer.parseInt(pointRead.next()));
                                weightQueue.enqueue(Integer.parseInt(pointRead.next()));
                                paths--;
                            }
                        }
                        point++;
                        count++;
                    }

                    else if(count == 4){
                        count = 0;
                    }

                }
            }
            pointRead.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void kangarooReader(String path){



        String temp, title;
        int count = 0;

        try {
            Scanner kangarooRead = new Scanner(new FileInputStream(path));
            title = kangarooRead.next();
            if(title.equalsIgnoreCase("Kangaroo")){
                while(kangarooRead.hasNextLine()){

                    if(count == 0){
                        temp = kangarooRead.next();
                        if(temp.charAt(0) == 'm'){
                                genderQueue.enqueue(false);
                        }
                            else if(temp.charAt(0) == 'f'){
                                genderQueue.enqueue(true);
                            }
                        count++;
                    }

                    else if(count == 1){
                        temp = kangarooRead.next();
                        capacityQueue.enqueue(Integer.parseInt(temp));
                        count++;
                    }

                    else if(count == 2){
                        temp = kangarooRead.next();
                        startingPointQueue.enqueue(Integer.parseInt(temp));
                        count++;
                    }

                    else if(count == 3){
                        count = 0;
                    }

                }
            }
            kangarooRead.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextReader.class.getName()).log(Level.SEVERE, null, ex);
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


}
