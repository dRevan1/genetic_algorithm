/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classy;

import java.io.FileNotFoundException;

/**
 *
 * @author Lubo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        Sit281211 sit = new Sit281211("nodes.txt", "edges.txt");
        int tnn = sit.GetTnn(); // Počet vrcholov dopravnej siete
        int [][] D = new int [tnn][tnn]; // Matica vzdialeností
        int [] R = new int [tnn], C = new int [tnn];
        int i;
        for(i=0; i<tnn; i++) {
            R[i] = sit.GetNn(i);
            C[i] = sit.GetNn(i);
        }
        sit.GetMatrixI(tnn, tnn, R, C, D);
        // Matica vzdialeností D je naplnená.
        
        
        
        
        // Pokračovať v implementácií GA.
        
        System.out.println("Cv8...");                
    }
}
