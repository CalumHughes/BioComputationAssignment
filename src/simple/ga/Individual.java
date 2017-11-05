/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author c38-hughes
 */
public class Individual {

    private Random r = new Random();

    private double genes[];

    private int ruleLength;

    private int ruleCount;

    private int fitness;
    
    public Individual(int ruleCount, int ruleLength) {
        this.ruleCount = ruleCount;
        this.ruleLength = ruleLength;
        genes = new double[ruleCount * ruleLength];

        for (int i = 0; i < genes.length; i++) {
            
            if (isOutputBit(i)) {
                genes[i] = r.nextInt(2);
            } else {
                genes[i] = r.nextDouble();
            }
            
            genes[i] = round(genes[i]);
            System.out.println("Bit: " + (i + 1) + " - " + genes[i]);
        }
    }

    public Individual(double[] genes, int ruleLength, int ruleCount) {
        this.ruleLength = ruleLength;
        this.ruleCount = ruleCount;
        this.genes = genes;
    }

    public void calculateFitness(Individual dataSet) {
        int totalFitness = 0;
        List<Rule> data = dataSet.getRuleList();
        List<Rule> population = this.getRuleList();

        for (Rule d : data) {
            for (Rule r : population) {
                if (checkInput(d.getInput(), r.getInput())) {
                    if (d.getOutput() == r.getOutput()) {
                        fitness++;
                    }
                    break;
                }
            }
        }

        setFitness(totalFitness);
    }

    private boolean checkInput(double[] in1, double[] in2) {
        int index = 0;
        for (double d : in1) {
            if (isInRange(in2[index], in2[index + 1], d)) {
                return false;
            }
            index += 2;
        }
        return true;
    }

    private boolean isInRange(double b1, double b2, double input) {
        if (b1 > b2) {
            double temp = b2;
            b2 = b1;
            b1 = temp;
        }

        if (input > b1 && input < b2) {
            return true;
        }

        return false;
    }

    public List<Rule> getRuleList() {
        double[] genes = getGenes();
        int startIndex = 0;
        double[] rule = new double[ruleLength];

        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < ruleCount; i++) {
            for (int j = 0; j < ruleLength; i++) {
                rule[j] = genes[j + startIndex];
            }
            rules.add(new Rule(rule));
            startIndex += ruleLength;
        }
        return rules;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public double[] getGenes() {
        return genes;
    }

    public void setGenes(double[] genes) {
        this.genes = genes;
    }

    public int getRuleLength() {
        return ruleLength;
    }

    public void setRuleLength(int ruleLength) {
        this.ruleLength = ruleLength;
    }

    public int getRuleCount() {
        return ruleCount;
    }

//    public String printGenes() {
//        String genes = "";
//
//        for (double i : getGenes()) {
//            genes += i + " - ";
//        }
//
//        return genes;
//    }
    public boolean isOutputBit(int i) {
        if (i == 0) {
            return false;
        }
        if (i < ruleLength) {
            return i % (ruleLength - 1) == 0;
        }
        return (i + 1) % ruleLength == 0;
    }

    public Individual copy() {
        Individual i = new Individual(getGenes(), getRuleLength(), getRuleCount());
        i.setFitness(getFitness());
        return i;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\nIndividual Fitness = " + fitness);
        s.append("\nRules:\n");
        for (Rule rule : getRuleList()) {
            s.append(rule.toString() + "\n");
        }
        return s.toString();
    }

    public double round(double value) {
        DecimalFormat df = new DecimalFormat("#.######");
        return Double.parseDouble(df.format(value));
    }
}
