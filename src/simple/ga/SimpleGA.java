/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author c38-hughes
 */
public class SimpleGA {

    private static final String FILE_NAME = "res/data3.txt";
    
    private static final String CSV_FILE_DIR = "res/csv_results/";

    private static final int GENS = 1000;

    private static int ruleLength;

    private static final int RULE_COUNT = 10;

    private static final int P = 100;

    private static final double MUTATE_AMOUNT = 0.5;

    private static double mutateRate;

    private static List<Rule> dataSet;

    private static Individual best;

    private static Population population;

    private static Population offspring;

    private static Helper helper;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        int generation = 0;
        
        helper = new Helper(P, RULE_COUNT, ruleLength);
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        ruleLength = helper.getRuleLength();
        ruleLength = (ruleLength - 1) * 2;
        ruleLength++;
        mutateRate = 1.5f / (ruleLength * RULE_COUNT);
        helper.setMutationRate(mutateRate);
        helper.setMutationAmount(MUTATE_AMOUNT);
        population = new Population(P, RULE_COUNT, ruleLength, dataSet);

        Random r = new Random();
        String fileName = GENS + "-" + P + "-" + RULE_COUNT + "-" + MUTATE_AMOUNT + "-" + mutateRate + "_" + r.nextInt(1001) + ".csv";
        Writer w = new FileWriter(CSV_FILE_DIR + fileName);
        CSVUtils.writeLine(w, Arrays.asList("Generation", "Average Fitness", "Best Fitness"));
        
        population.printGeneration(generation, w);
        while (generation != GENS) {
            generation++;
            best = population.getHighestFitnessIndividual();
            offspring = helper.tournamentSelection(population);
            offspring = helper.singlePointCrossover(offspring);
            offspring = helper.bitwiseMutation(offspring);
            population = offspring.copy();
            population.replaceWorstIndividual(best);
            population.printGeneration(generation, w);
            offspring = new Population(dataSet);
            w.flush();
        }

        w.close();
        population.printIndividual(null);
    }

}
