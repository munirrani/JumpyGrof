package jumpygrof.datastructure;

public class ListNode<T> {

    private T data;
    private ListNode listNode;

    public ListNode (T t, ListNode listNode) {
        data = t;
        this.listNode = listNode;
    }

    public T getData() { return data; }
    public void setLink(ListNode listNode) { this.listNode = listNode; }
    public ListNode getLink() { return listNode; }
    public String toString() {
        return data + " --> ";
    }
}
