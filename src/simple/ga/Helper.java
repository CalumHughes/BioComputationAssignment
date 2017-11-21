/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
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
    private double mutationAmount;
    private List<Rule> dataSet;
    private Random r;

    public Helper(int p, int ruleCount, int ruleLength) {
        this.p = p;
        this.ruleCount = ruleCount;
        this.ruleLength = ruleLength;
        r = new Random();
    }

    public List<Rule> getIndividualFromFile(String fileName) {
        int rCount = 0;
        double[] genes = null;
        int index = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String currentLine = br.readLine();
            String[] splitLine = currentLine.split("\\s+");
            rCount = Integer.parseInt(splitLine[0]);
            ruleLength = Integer.parseInt(splitLine[3]) + 1;
            genes = new double[ruleLength * rCount];

            while ((currentLine = br.readLine()) != null) {
                splitLine = currentLine.split("\\s+");

                for (int i = 0; i < ruleLength; i++) {
                    genes[index] = Double.parseDouble(splitLine[i]);
                    index++;
                }
            }
        } catch (IOException e) {
            System.out.println("Exception reading file...");
            return null;
        }

        dataSet = new Individual(genes, ruleLength, rCount).getRuleList(false);
        return dataSet;
    }

    public Population tournamentSelection(Population population) {
        Population offspring = new Population(dataSet);
        int p = population.getPopulation().size();

        for (int i = 0; i < p; i++) {
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
        double[] mutatedGenes = new double[individual.getGenes().length];
        int index = 0;

        for (double g : individual.getGenes()) {
            Double rand = r.nextDouble();
            if (rand < mutationRate) {
                if (individual.isOutputBit(index)) {
                    mutatedGenes[index] = 1.0 - g;
                } else {
                    mutatedGenes[index] = individual.round(inputMutation(g));
                }
            } else {
                mutatedGenes[index] = g;
            }
            index++;
        }

        return new Individual(mutatedGenes, individual.getRuleLength(), individual.getRuleCount());
    }

    private double inputMutation(double gene) {
        Double result;
        
        if (r.nextInt(2) == 0) {
            double m = getMutationAmount();
            result = gene - m;
        } else {
            double m = getMutationAmount();
            result = gene + m;
        }

        if (result > 1.0) {
            result = 1.0;
        } else if (result < 0.0) {
            result = 0.0;
        }

        return result;
    }

    public Population singlePointCrossover(Population population) {
        Population mutatedPopulation = new Population(dataSet);
        int rLength = population.getPopulation().get(0).getRuleLength();

        for (int i = 0; i < p; i++) {
            Individual parent1 = population.getIndividual(i);
            Individual parent2 = population.getIndividual(i + 1);
            mutatedPopulation.getPopulation().addAll(performCrossover(r.nextInt(parent1.getGenes().length), parent1.getGenes(), parent2.getGenes(), rLength));
            i++;
        }

        mutatedPopulation.calculateTotalFitnessOfPopulation();
        return mutatedPopulation;
    }

    public List<Individual> performCrossover(int crossoverPoint, double genesP1[], double genesP2[], int rLength) {
        List<Individual> children = new ArrayList<>();

        double[] child1Genes = genesP1;
        double[] child2Genes = genesP2;

        for (int i = crossoverPoint + 1; i < child1Genes.length; i++) {
            double tempGene = child1Genes[i];
            child1Genes[i] = child2Genes[i];
            child2Genes[i] = tempGene;
        }

        children.add(new Individual(child1Genes, rLength, ruleCount));
        children.add(new Individual(child2Genes, rLength, ruleCount));
        return children;
    }

    private double getMutationAmount() {
        return 0 + (mutationAmount - 0) * r.nextDouble();
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

    public void setMutationAmount(double mutationAmount) {
        this.mutationAmount = mutationAmount;
    }
    
    public void setDataSet(List<Rule> dataSet) {
        this.dataSet = dataSet;
    }
}
