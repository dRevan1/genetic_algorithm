package classy;


/**
 * Write a description of class PrQueue here. The class describes a prior queue
 * with the assocoated method of insertion and withdrawing.
 *
 * @author Jaroslav Janacek
 * @version 010112
 */
public class PrQueue {
    // instance variables 

    private int h; // Head of the queue
    private int p; // End of the prior queue
    private int e; // End of the ordinary queue
    private boolean em; // True, if the queue is empty
    private int[] Q; // The queue
    private int m; // Maximum length of queue

    /**
     * Constructor for objects of class PrQueue The input parameter n is the
     * maximum length of the queue
     */
    public PrQueue(int n) {
        // initialise instance variables
        Q = new int[n + 1];
        m = n;
    } // End of PrQueue

    /**
     * The method empties the total queue
     */
    public void EmptyTQ() {
        h = m;
        p = h;
        e = h;
        Q[h] = h;
        for (int i = 0; i < h; i++) {
            Q[i] = -1;
        }
        em = true;
    } // End of InsertToTQ

    /**
     * The method inserts item s into the prior queue
     */
    public void InsertToPQ(int s) {
        if (s < m && Q[s] < 0) {
            if (p == e) {
                e = s;
            }
            Q[s] = Q[p];
            Q[p] = s;
            p = s;
            em = false;
        }
    } // End of InsertToPQ

    /**
     * The method inserts item s into the ordinary queue
     */
    public void InsertToOQ(int s) {
        if (s < m && Q[s] < 0) {
            Q[s] = h;
            Q[e] = s;
            e = s;
            em = false;
        }
    } // End of InsertToOQ

    /**
     * The method witdraws the first item from the queue, if the queue is empty,
     * it returns -1
     */
    public int GetFromPQ() {
        if (em) {
            return -1;
        } else {
            int a = Q[h];
            Q[h] = Q[a];
            Q[a] = -1;
            if (p == a) {
                p = h;
            }
            if (e == a) {
                e = h;
            }
            if (Q[h] == h) {
                em = true; // The last item has been withdrawn 
            }
            return a;
        }
    } // End of InsertToPQ

    public boolean IsEmpty() {
        return em;
    } //End of IsEmpty
}
