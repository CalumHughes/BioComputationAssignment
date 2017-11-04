/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.util.List;

/**
 *
 * @author c38-hughes
 */
public class SimpleGA {

    private static final String FILE_NAME = "res/data1.txt";

    private static final int generations = 100;

    private static int n;

    private static final int P = 10;

    private static double mutationRate;

    private static Individual dataSet;
    
    private static Individual best;

    private static Population population;

    private static Population offspring;

    private static Helper helper;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int generation = 0;

        helper = new Helper();
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        n = helper.getN();
        mutationRate = 1.0f / n;
        helper.setMutationRate(mutationRate);

        population = new Population(P, n, dataSet);

        population.printGeneration(generation);

        while (generation != generations) {
            generation++;
            best = population.getHighestFitnessIndividual();
            offspring = helper.tournamentSelection(population);
            offspring = helper.singlePointCrossover(offspring);
            offspring.replaceWorstIndividual(best);
            offspring = helper.bitwiseMutation(offspring);
            offspring.replaceWorstIndividual(best);
            
            population = offspring.copy();
            population.replaceWorstIndividual(best);
            population.printGeneration(generation);
            offspring = new Population(dataSet);

        }
    }

}
