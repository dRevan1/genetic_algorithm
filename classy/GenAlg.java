package classy;

import java.util.Random;

public class GenAlg extends Alg {
    private int[][] population;
    private int population_size;
    private int n = 100;   // počet behov GA bez zmeny najlepšie nájdeného riešenia

    public GenAlg(int p, int pocVrch, int[][] D) {
        super(p, pocVrch, D);
        population_size = 10 * p;
        population = new int[population_size][p];
    }
    //---------------------------------------------
    // naplní maticu "population" vstupnou populáciou
    //---------------------------------------------
    private void CreateInitialPopulation() {
        Random rand = new Random();
        rand.setSeed(42);
        int i = 0;

        while (i < population_size) {
            boolean feasible = false;
            for (int j = 0; j < p; j++) {
                population[i][j] = rand.nextInt(2);
                feasible = (population[i][j] == 1);
            }
            if (feasible) {
                i++;
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // metóda spustí genetický algoritmus, musí byť zavolaná až PO metóde "CreateInitialPopulation", ktorá vytvorí vstupnú populáciu
    //------------------------------------------------------------------------------------------------------------------------------
    private void RunGeneticAlg() {
        Random rand = new Random();
        rand.setSeed(42);
        int t = 0;
        int[][] offsprings = new int[2][p];
        int[] first_parent = new int[p];
        int[] second_parent = new int[p];

        while (t <= n) {
            int first_index = rand.nextInt(population_size);
            int second_index = rand.nextInt(population_size);
            CopySolution(population[first_index], first_parent);
            CopySolution(population[second_index], second_parent);

            GenAlgOperations.Cross(first_parent, second_parent, offsprings);
            GenAlgOperations.Mutate(offsprings[0]);
            GenAlgOperations.Mutate(offsprings[1]);

            int first_offspring_val = GetTargetFunctionValue(offsprings[0]);
            int second_offspring_val = GetTargetFunctionValue(offsprings[1]);

            if (first_offspring_val < best_sol_val) {
                CopySolution(offsprings[0], best_sol);
                best_sol_val = first_offspring_val;
                t = 0;
            }
            if (second_offspring_val < best_sol_val) {
                CopySolution(offsprings[1], best_sol);
                best_sol_val = second_offspring_val;
                t = 0;
            }

            t++;
        }
    }
    //------------------------------------
    // skopíruje riešenie z "from" do "to"
    //------------------------------------
    private void CopySolution(int[] from, int[] to) {
        for (int i = 0; i < p; i++) {
            to[i] = from[i];
        }

    }
    //-----------------------------------------------
    // vráti hodnotu účelovej funkcie daného riešenia
    //-----------------------------------------------
    private int GetTargetFunctionValue(int[] solution) {
        int target_value = 0;
        for (int i = 0; i < pocVrch; i++) {
            int shortest_path = Integer.MAX_VALUE;
            for  (int j = 0; j < p; j++) {
                if (solution[j] == i && D[i][j] < shortest_path) {
                    shortest_path = D[i][j];
                }
            }
            target_value += shortest_path;
        }

        return target_value;
    }
    //----------------------------------------------------
    // kontroluje prípustnosť riešenia -> true = prípustné
    //----------------------------------------------------
    private boolean IsFeasible(int[] sol) {
        int center_count = 0;
        for (int i = 0 ; i < p ; i++) {
            if (sol[i] == 1) {
                center_count++;
            }
        }

        return center_count <= p && center_count != 0;  // kontrola, či je počet umiestnených stredísk medzi 1 a p
    }

    @Override
    public void vyriesSa() {

    }
}