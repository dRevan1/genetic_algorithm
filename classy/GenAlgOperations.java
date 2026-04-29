package classy;

import java.util.Random;

public class GenAlgOperations {

    //-------------------------------------------------------------------------------------------------------------
    // kríženie 2 riešení - first a second sol, zapíše sa do matice offsprings s rozmermi 2 * p (p -> p z p-mediánu)
    //--------------------------------------------------------------------------------------------------------------
    public static void Cross(int[] first_sol, int[] second_sol, int[][] offsprings, Random rand) {
        for (int i = 0; i < first_sol.length; i++) {
            int mask_number = rand.nextInt(2);   // číslo masky
            offsprings[0][i] = (mask_number == 1) ? first_sol[i] : second_sol[i];  // priradenie podľa masky - 1 zostane hodnota, 0 sa vyberie z druhého rodiča
            offsprings[1][i] = (mask_number == 1) ? second_sol[i] : first_sol[i];
        }
    }
    //------------------------
    // mutácia daného riešenia
    //------------------------
    public static void Mutate(int[] sol, Random rand) {
        if (rand.nextDouble() < 0.5) {
            int index =  rand.nextInt(sol.length);
            sol[index] = (1 -  sol[index]);
        }
    }
}