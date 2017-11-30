/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author c38-hughes
 */
public class SimpleGA {

    private static final String CSV_FILE_DIR = "res/csv_results/";

    private static final String FILE_NAME = "res/data2.txt";
    
    public static boolean useGeneralization = true;

    private static final int GENS = 500;

    private static int ruleLength;

    private static final int RULE_COUNT = 5;

    private static final int P = 100;

    private static double mutateRate;

    private static Individual dataSet;

    private static Individual best;

    private static Population population;

    private static Population offspring;

    private static Helper helper;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int generation = 0;

        helper = new Helper(P, RULE_COUNT, ruleLength, useGeneralization);
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        ruleLength = helper.getRuleLength();
        mutateRate = 0.015;
       //mutateRate = 1.0f / (ruleLength * RULE_COUNT);
        helper.setMutationRate(mutateRate);

        for (int i = 0; i < 5; i++) {
            population = new Population(P, RULE_COUNT, ruleLength, dataSet, useGeneralization);

            List<List<String>> csvData = new ArrayList<>();
            csvData.add(Arrays.asList("Generation", "Average Fitness", "Best Fitness"));

            csvData.add(population.printGeneration(generation));
            while (generation != GENS) {
                generation++;

                best = population.getHighestFitnessIndividual();
                offspring = helper.tournamentSelection(population);
                offspring = helper.singlePointCrossover(offspring);
                offspring = helper.bitwiseMutation(offspring);
                population = offspring.copy();

                population.replaceWorstIndividual(best);

                csvData.add(population.printGeneration(generation));
                offspring = new Population(dataSet);
            }
            String b = String.valueOf(population.getHighestFitnessIndividual().getFitness());
            generation = 0;
            String fileName = GENS + "-" + P + "-" + RULE_COUNT + "-" + mutateRate + "-" + b + "-" + i + ".csv";
            Writer w = new FileWriter(CSV_FILE_DIR + fileName);
            CSVUtils.writeLines(w, csvData);
            population.printBest();
        }
    }
}
