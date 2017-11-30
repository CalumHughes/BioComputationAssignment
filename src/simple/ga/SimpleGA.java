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

    private static final String FILE_NAME = "res/data3.txt";

    private static final int GENS = 1000;

    private static int ruleLength;

    private static final int RULE_COUNT = 10;

    private static final int P = 100;

    private static final double MUTATE_AMOUNT = 0.6;

    private static double mutateRate;

    private static List<Rule> trainingSet;

    public static List<Rule> testSet;

    private static Individual best;

    private static Population population;

    private static Population offspring;

    private static Helper helper;

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        helper = new Helper(P, RULE_COUNT, ruleLength);
        List<Rule> tempList = helper.getIndividualFromFile(FILE_NAME);
        trainingSet = tempList.subList(0, 1000);
        testSet = tempList.subList(1000, tempList.size());
        helper.setDataSet(trainingSet);

        ruleLength = helper.getRuleLength();
        ruleLength = (ruleLength - 1) * 2;
        ruleLength++;
        mutateRate = 1.5f / (ruleLength * RULE_COUNT);
        helper.setMutationRate(mutateRate);
        helper.setMutationAmount(MUTATE_AMOUNT);

        int generation = 0;
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList("Generation", "Average Fitness", "Best Fitness Training", "Fitness of best on testSet"));
        population = new Population(P, RULE_COUNT, ruleLength, trainingSet);

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
            offspring = new Population(trainingSet);
        }
        population.printIndividual(null);
        String b = String.valueOf(population.getHighestFitnessIndividual().getFitness());
        String fileName = GENS + "-" + P + "-" + RULE_COUNT + "-" + mutateRate + "-" + MUTATE_AMOUNT + "-" + b + ".csv";
        Writer w = new FileWriter(CSV_FILE_DIR + fileName);
        CSVUtils.writeLines(w, csvData);
    }
}