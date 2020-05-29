package jumpygrof.datastructure;

public class LinkedList<T extends Comparable<T>> {

    private ListNode head;

    public LinkedList() {
        head = null;
    }

    public int size() {
        int count = 0;
        ListNode currentNode = head;
        while (currentNode != null) {
            currentNode = currentNode.getLink();
            count++;
        }
        return count;
    }

    public void showList() {
        ListNode currentNode = head;
        if (head == null) {
            System.out.println("List is empty");
            return;
        }
        while (currentNode != null) {
            System.out.print(currentNode.toString());
            currentNode = currentNode.getLink();
        }
        System.out.println();
    }

    public void add(T t) {
        ListNode newNode = new ListNode(t, null);
        ListNode currentNode = head;
        if (head == null) {
            head = newNode;
        } else {
            while (currentNode.getLink() != null) {
                currentNode = currentNode.getLink();
            }
            currentNode.setLink(newNode);
        }
    }

    public T get(int index) {
        if (index >= size() || index < 0 || head == null) {
            return  null;
        } else {
            int count = 0;
            ListNode currentNode = head;
            while (currentNode.getLink() != null && count != index) {
                currentNode = currentNode.getLink();
                count++;
            }
            return (T) currentNode.getData();
        }
    }

    public void remove(T t) {
        ListNode currentNode = head;
        if (t.compareTo((T)currentNode.getData()) == 0) {
            head = head.getLink();
        } else {
            ListNode previousNode = currentNode;
            currentNode = currentNode.getLink();
            while (currentNode != null) {
                if (t.compareTo((T)currentNode.getData()) == 0) {
                    previousNode.setLink(currentNode.getLink());
                    break;
                } else {
                    previousNode = currentNode;
                    currentNode = currentNode.getLink();
                }
            }
        }
    }
}

