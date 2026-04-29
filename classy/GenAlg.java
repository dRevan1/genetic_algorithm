package classy;

import java.util.Collections;
import java.util.Random;

public class GenAlg extends Alg {
    private int[][] population;
    private int population_size;
    private int centres;
    private int n ;   // počet behov GA bez zmeny najlepšie nájdeného riešenia

    public GenAlg(int p, int centres, int population_size, int pocVrch, int[][] D, int n, Random rand) {
        super(p, pocVrch, D, rand);
        this.population_size = population_size;
        this.centres = centres;
        this.n = n;
        population = new int[population_size][centres];
    }
    //---------------------------------------------
    // naplní maticu "population" vstupnou populáciou
    //---------------------------------------------
    private void CreateInitialPopulation() {
        int i = 0;

        while (i < population_size) {
            int center_count = 0;
            for (int j = 0; j < centres; j++) {
                population[i][j] = rand.nextInt(2);
                center_count += population[i][j];
            }
            if (center_count <= centres && center_count > 0) {
                KeepBestSolution(population[i]);
                i++;
            }
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // metóda spustí genetický algoritmus, musí byť zavolaná až PO metóde "CreateInitialPopulation", ktorá vytvorí vstupnú populáciu
    //------------------------------------------------------------------------------------------------------------------------------
    private void RunGeneticAlg() {
        int t = 0;
        int[][] offsprings = new int[2][centres];
        int[] first_parent = new int[centres];
        int[] second_parent = new int[centres];

        while (t <= n) {
            int new_population_index = 0;
            int first_index = rand.nextInt(population_size);
            int second_index = rand.nextInt(population_size);
            CopySolution(population[first_index], first_parent);
            CopySolution(population[second_index], second_parent);

            GenAlgOperations.Cross(first_parent, second_parent, offsprings, rand);
            GenAlgOperations.Mutate(offsprings[0], rand);
            GenAlgOperations.Mutate(offsprings[1], rand);
            t++;

            if (IsFeasible(offsprings[0])) {
                if (KeepBestSolution(offsprings[0])) {
                    t = 0;
                }
                new_population_index++;
            }
            if (IsFeasible(offsprings[1])) {
                if (KeepBestSolution(offsprings[1])) {
                    t = 0;
                }
                new_population_index++;
            }


        }
    }
    //------------------------------------
    // skopíruje riešenie z "from" do "to"
    //------------------------------------
    private void CopySolution(int[] from, int[] to) {
        for (int i = 0; i < centres; i++) {
            to[i] = from[i];
        }
    }
    //-----------------------------------------------------------------------------------------
    // skontroluje, či nové riešenie má lepšiu účelovú funkciu, ako najlepšie, potom ho nahradí
    //-----------------------------------------------------------------------------------------
    private boolean KeepBestSolution(int[] new_sol) {
        int sol_val = GetTargetFunctionValue(new_sol);
        if (sol_val < best_sol_val) {
            CopySolution(new_sol, best_sol);
            best_sol_val = sol_val;

            return true;
        }
        return false;
    }
    //-----------------------------------------------
    // vráti hodnotu účelovej funkcie daného riešenia
    //-----------------------------------------------
    private int GetTargetFunctionValue(int[] solution) {
        int target_value = 0, center_count = 0, index = 0;
        int[] center_indices = new int[p]; // vytvorí sa zoznam indexov umiestnených stredísk

        for (int i = 0; i < centres; i++) {
            if  (solution[i] == 1) {
                center_indices[index] = i;
                center_count++;
                index++;
            }
        }

        for (int i = 0; i < pocVrch; i++) {
            int shortest_path = Integer.MAX_VALUE;
            for (int j = 0; j < center_count; j++) {
                int center_index = center_indices[j];
                if (D[i][center_index] < shortest_path) {
                    shortest_path = D[i][center_index];
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
        for (int i = 0 ; i < centres; i++) {
            center_count += sol[i];
        }

        return center_count <= p && center_count > 0;  // kontrola, či je počet umiestnených stredísk medzi 1 a p
    }

    @Override
    public void vyriesSa() {
        CreateInitialPopulation();
        RunGeneticAlg();
    }
}