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

    private static final String FILE_NAME = "res/data3.txt";

    private static final int GENS = 1000;

    private static int ruleLength;

    private static final int RULE_COUNT = 5;

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

        helper = new Helper(P, RULE_COUNT, ruleLength);
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        ruleLength = helper.getRuleLength();
        ruleLength = (ruleLength - 1) * 2;
        ruleLength++;
        
        mutationRate = 1.0f / (ruleLength * RULE_COUNT);
        helper.setMutationRate(mutationRate);
        population = new Population(P, RULE_COUNT, ruleLength, dataSet);

        population.printGeneration(generation);
        while (generation != GENS) {
            generation++;

            best = population.getHighestFitnessIndividual();
//            System.out.println(best.toString());
            offspring = helper.tournamentSelection(population);
            offspring = helper.singlePointCrossover(offspring);
            offspring = helper.bitwiseMutation(offspring);
            population = offspring.copy();

            population.replaceWorstIndividual(best);

            population.printGeneration(generation);
            offspring = new Population(dataSet);
        }

        population.printBest();
    }

}
