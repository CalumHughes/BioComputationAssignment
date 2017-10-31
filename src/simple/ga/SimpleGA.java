/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;


/**
 *
 * @author c38-hughes
 */
public class SimpleGA {

    private static final int generations = 100;

    private static final int N = 100;

    private static final int P = 100;

    private static double mutationRate;

    private static Population population;

    private static Population offspring;

    private static Helper helper;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int generation = 0;
        mutationRate = 1.0f / N;

        population = new Population(P, N);
        helper = new Helper(P, N, mutationRate);
        
        population.calculateTotalFitnessOfPopulation();

        population.printGeneration(generation);
        
        while (generation != generations) {
            generation++;
            Individual best = population.getHighestFitnessIndividual();
            offspring = helper.tournamentSelection(population);
            offspring = helper.singlePointCrossover(offspring);
            offspring = helper.bitwiseMutation(offspring);
            population = offspring;
            population.replaceWorstIndividual(best);
            offspring.printGeneration(generation);
            offspring = new Population();
            
        }

        //printPopulation(population);
    }

  
}
