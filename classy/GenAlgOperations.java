package classy;

import java.util.Random;

public class GenAlgOperations {

    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    // kríženie 2 riešení - first a second parent, zapíše sa do matice offsprings s rozmermi 2 * centres (centres -> celkový počet miest, kde sa dá umiestniť p stredísk)
    //-------------------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void Cross(int[] first_parent, int[] second_parent, int[][] offsprings, Random rand) {
        for (int i = 0; i < first_parent.length; i++) {
            int mask_number = rand.nextInt(2);   // číslo masky
            offsprings[0][i] = (mask_number == 1) ? first_parent[i] : second_parent[i];  // priradenie podľa masky - 1 zostane hodnota, 0 sa vyberie z druhého rodiča
            offsprings[1][i] = (mask_number == 1) ? second_parent[i] : first_parent[i];
        }
    }
    //------------------------
    // mutácia daného riešenia
    //------------------------
    public static void Mutate(int[] sol, Random rand) {
        if (rand.nextDouble() < 0.5) {
            int index =  rand.nextInt(sol.length);
            sol[index] = (1 -  sol[index]);  // hodnota 0/1 sa vymení
        }
    }
}