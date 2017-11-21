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

    private static final String CSV_FILE_DIR = "res/csv_results/data3/";

    private static final String FILE_NAME = "res/data3.txt";

    private static final int GENS = 100;

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
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList("Generation", "Average Fitness", "Best Fitness"));
        helper = new Helper(P, RULE_COUNT, ruleLength);
        dataSet = helper.getIndividualFromFile(FILE_NAME);

        ruleLength = helper.getRuleLength();
        ruleLength = (ruleLength - 1) * 2;
        ruleLength++;
        mutateRate = 1.5f / (ruleLength * RULE_COUNT);
        helper.setMutationRate(mutateRate);
        helper.setMutationAmount(MUTATE_AMOUNT);
        population = new Population(P, RULE_COUNT, ruleLength, dataSet);

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
            w.flush();
        }
        population.printIndividual(null);
        String fileName = GENS + "-" + P + "-" + RULE_COUNT + "-" + mutateRate + "-" + b + "-" + 1 + ".csv";
        Writer w = new FileWriter(CSV_FILE_DIR + fileName);
        CSVUtils.writeLines(w, csvData);
    }

}
