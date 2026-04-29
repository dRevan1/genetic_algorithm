/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package classy;
import java.util.Random;

/**
 *
 * @author Lubo
 * 
 * Trieda <code>Alg</code> je predok všetkých tried, ktoré riešia úlohu 
 * <strong>umiestňovaciu úlohu p-mediánového typu</strong>.
 */
abstract public class Alg {

    /**
     * Maximálny počet umiestnených stredísk.
     */
    protected int p;
    
    /**
     * Počet vrcholov dopravnej siete
     */
    protected int pocVrch;

    protected int[] best_sol;
    protected int best_sol_val;
    protected Random rand;

    /**
     * Matica vzdialeností "každý s každým" (medzi každou dvojicou vrcholov).
     */
    protected int [][] D;

    public Alg(int p, int pocVrch, int[][] D, Random rand) {
        this.p = p;
        this.pocVrch = pocVrch;
        this.D = D;
        this.rand = rand;
        best_sol = new int[p];
        best_sol_val = Integer.MAX_VALUE;
    }

    /**
     * Metóda vyrieši umiestňovaciu úlohu p-mediánového typu.
     */
    abstract public void vyriesSa();
}
