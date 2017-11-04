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

    private static final String FILE_NAME = "res/data1.txt";

    private static final int GENS = 100;

    private static int ruleLength;

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

        helper = new Helper(P);
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        ruleLength = helper.getRuleLength();
        mutationRate = 1.0f / (helper.getRuleLength() * helper.getP());
        helper.setMutationRate(mutationRate);

        population = new Population(P, ruleLength, dataSet);

        population.printGeneration(generation);

        while (generation != GENS) {
            generation++;
            best = population.getHighestFitnessIndividual();
            offspring = helper.tournamentSelection(population);
            offspring = helper.singlePointCrossover(offspring);
            offspring = helper.bitwiseMutation(offspring);
            offspring.replaceWorstIndividual(best);
            
            population = offspring.copy();
            population.printGeneration(generation);
            offspring = new Population(dataSet);
        }
    }

}
