package classy;

import java.util.Collections;
import java.util.Random;

public class GenAlg extends Alg {
    private int[][] population;
    private int population_size;
    private int new_population_size;
    private int centres;
    private int n ;   // počet výmen populácie GA bez zmeny najlepšie nájdeného riešenia
    private int q;  // veľkosť podmnožiny pre turnajovú selekciu

    public GenAlg(int p, int centres, int pocVrch, int[][] D, int n, int population_size, int new_population_size, int q, Random rand) {
        super(p, pocVrch, D, rand);
        this.centres = centres;
        this.population_size = population_size;
        this.new_population_size = new_population_size;
        this.n = n;
        this.q = q;
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
    //---------------------------------------------------------------------------------------------------------------------------
    // vykoná turnajovú selekciu 2 rodičov - pre rodiča na indexe 0 a 1 prejde q rôznych riešení a vyberia to s najväčším Fitness
    //---------------------------------------------------------------------------------------------------------------------------
    private void TournamentSelection(int[][] parents) {
        for (int i = 0; i < parents.length; i++) {
            int best_index = 0;  // index najlepšieho riešenia podľa fitness pre daného rodčia
            int best_fitness = 0; // hodnota fitness pre riešenie s indexom best_index
            int[] q_solutions = new int[population_size];  // vektor výberu riešení - 1 ak bolo už vybrané, aby v q množine neboli rovnaké riešenia
            int picked_solutions = 0;  // počet vybraných riešení

            while (picked_solutions < q) {
                int new_solution = rand.nextInt(population_size);

                if (q_solutions[new_solution] == 0) {  // ak ešte nie je v q, tak sa nastaví na 1 a zvýši picked_solutions, potom sa skontroluje, či nie je lepšie ako doteraz best
                    q_solutions[new_solution] = 1;
                    picked_solutions++;
                    int new_fitness = EvaluateFitness(population[new_solution]);

                    if (new_fitness > best_fitness) {
                        best_fitness = new_fitness;
                        best_index = new_solution;
                    }
                }
            }

            CopySolution(population[best_index], parents[i]);   // nakoniec sa skopíruje najlepšie riešenie podľa Fitness z q do rodiča na indexe i
        }
    }
    //------------------------------------------------------------------------------------------------------------------------------
    // metóda spustí genetický algoritmus, musí byť zavolaná až PO metóde "CreateInitialPopulation", ktorá vytvorí vstupnú populáciu
    //------------------------------------------------------------------------------------------------------------------------------
    private void RunGeneticAlg() {
        int t = 0;
        int[][] offsprings = new int[2][centres];
        int[][] parents = new int[2][centres];

        while (t < n) {
            t++;  // t sa aktualizuje ešte pred vytváraním novej populácie - ak sa v procese vytvárania aktualizuje best solution -> t je 0
            for (int i = 0; i < new_population_size; i++) {  // po mutácii sa riešenia opravujú, takže sa potomok vždy pridá do kandidátov do novej populácie - môžeme použiť for loop
                TournamentSelection(parents);  // turnajový výber 2 rodičov
                GenAlgOperations.Cross(parents[0], parents[1], offsprings, rand);  // kríženie - vytvorenie 2 nových potomkov
                GenAlgOperations.Mutate(offsprings[0], rand); // mutácia potomkov
                GenAlgOperations.Mutate(offsprings[1], rand);
                for (int j = 0; j < offsprings[0].length; j++) {   // tu sa skontroluje prípustnosť potomkov a prípadne sa opravia - ďalej budú vždy prípustní
                    if (!IsFeasible(offsprings[j])) {
                        FixUnfeasibleSolution(offsprings[j]);
                    }
                    if (KeepBestSolution(offsprings[j])) {
                        t = 0;
                    }
                }


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
    //-------------------------------
    // vráti Fitness hodnotu riešenia
    //-------------------------------
    private int EvaluateFitness(int[] sol) {
        return GetTargetFunctionValue(sol);
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
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    // opraví neprípustné riešenie - ak je to nulový vektor, tak nejaký prvok tá na 1, ak je 1 viac ako p, zníži počet stredísk náhodným výberom, kým nebude rovný p
    //--------------------------------------------------------------------------------------------------------------------------------------------------------------
    private void FixUnfeasibleSolution(int[] sol) {
        int center_count = 0;

        for (int i = 0; i < centres; i++) {
            center_count += sol[i];
        }
        if (center_count == 0) {
            sol[rand.nextInt(centres)] = 1;
        }
        else if (center_count > p) {
            while (center_count > p) {
                int index = rand.nextInt(centres);
                if (sol[index] == 1) {
                    sol[index] = 0;
                    center_count--;
                }
            }
        }
    }

    @Override
    public void vyriesSa() {
        CreateInitialPopulation();
        RunGeneticAlg();
    }
}