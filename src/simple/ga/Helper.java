/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author calum
 */
public class Helper {

    private int p;
    private int ruleCount;
    private int ruleLength;
    private double mutationRate;
    private Individual dataSet;
    private Random r;

    public Helper(int p, int ruleCount, int ruleLength) {
        this.p = p;
        this.ruleCount = ruleCount;
        this.ruleLength = ruleLength;
        r = new Random();
    }

    public Individual getIndividualFromFile(String fileName) {
        int rCount = 0;
        String genes = "";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String currentLine;
            String[] splitLine;
            while ((currentLine = br.readLine()) != null) {
                splitLine = currentLine.split("\\s+");

                if (splitLine.length > 2) {
                    rCount = Integer.parseInt(splitLine[0]);
                    ruleLength = Integer.parseInt(splitLine[3]) + 1;
                } else {
                    genes = genes.concat(splitLine[0].concat(splitLine[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception reading file...");
            return null;
        }

        dataSet = new Individual(genes, ruleLength, rCount, 0);
        return dataSet;
    }

    public Population tournamentSelection(Population population) {
        Population offspring = new Population(dataSet);
        int p = population.getPopulation().size();

        for (int i = 1; i <= p; i++) {
            Individual parent1 = population.getIndividual(r.nextInt(p));
            Individual parent2 = population.getIndividual(r.nextInt(p));

            if (parent1.getFitness() >= parent2.getFitness()) {
                offspring.addIndividual(parent1.copy());
            } else {
                offspring.addIndividual(parent2.copy());
            }
        }

        offspring.calculateTotalFitnessOfPopulation();
        return offspring;
    }

    public Population bitwiseMutation(Population population) {
        Population mutatedOffspring = new Population(dataSet);

        for (Individual i : population.getPopulation()) {
            mutatedOffspring.addIndividual(mutateIndividual(i));
        }

        mutatedOffspring.calculateTotalFitnessOfPopulation();
        return mutatedOffspring;
    }

    public Individual mutateIndividual(Individual individual) {
        int[] mutatedGenes = new int[individual.getGenes().length];
        int index = 0;

        for (int i = 0; i < individual.getGenes().length; i++) {
            Double d = r.nextDouble();
            int gene = individual.getGenes()[i];
            if (d < mutationRate) {
                if (individual.isOutputBit(i)) {
                    mutatedGenes[index] = 1 - gene;
                } else {
                    mutatedGenes[index] = inputMutation(gene);
                }
            } else {
                mutatedGenes[index] = gene;
            }
            index++;
        }

        return new Individual(mutatedGenes, ruleLength, individual.getRuleCount());
    }

    private int inputMutation(int gene) {
        Double op = 0.5;
        switch (gene) {
            case 0:
                return r.nextDouble() <= op ? 1 : 2;
            case 1:
                return r.nextDouble() <= op ? 0 : 2;
            case 2:
                return r.nextDouble() <= op ? 1 : 0;
        }
        return -1;
    }

    public Population singlePointCrossover(Population population) {
        Population mutatedPopulation = new Population(dataSet);

        for (int i = 0; i < p; i++) {
            Individual parent1 = population.getIndividual(i);
            Individual parent2 = population.getIndividual(i + 1);
            mutatedPopulation.getPopulation().addAll(performCrossover(r.nextInt(ruleCount * ruleLength), (ruleCount * ruleLength), parent1.getGenes(), parent2.getGenes()));
            i++;
        }

        mutatedPopulation.calculateTotalFitnessOfPopulation();
        return mutatedPopulation;
    }

    public Population multiPointCrossover(Population population) {
        Population mutatedPopulation = new Population(dataSet);

        for (int i = 0; i < p; i++) {
            Individual parent1 = population.getIndividual(i);
            Individual parent2 = population.getIndividual(i + 1);
            mutatedPopulation.getPopulation().addAll(performCrossover(r.nextInt(ruleCount * ruleLength), -1, parent1.getGenes(), parent2.getGenes()));
            i++;
        }

        mutatedPopulation.calculateTotalFitnessOfPopulation();
        return mutatedPopulation;
    }

    public List<Individual> performCrossover(int crossoverPoint, int endCrossoverPoint, int genesP1[], int genesP2[]) {

        if(endCrossoverPoint == -1) {
            while (endCrossoverPoint < crossoverPoint) {
                endCrossoverPoint = r.nextInt(ruleLength * ruleCount);
            }
        }
        List<Individual> children = new ArrayList<>();

        int[] child1Genes = genesP1;
        int[] child2Genes = genesP2;

        for (int i = crossoverPoint; i < endCrossoverPoint; i++) {
            int tempGene = child1Genes[i];
            child1Genes[i] = child2Genes[i];
            child2Genes[i] = tempGene;
        }

        children.add(new Individual(child1Genes, ruleLength, ruleCount));
        children.add(new Individual(child2Genes, ruleLength, ruleCount));
        return children;
    }

    public int getP() {
        return p;
    }

    public int getRuleLength() {
        return ruleLength;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }
}
