/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author c38-hughes
 */
public class Individual {

    private Random r = new Random();

    private int genes[];

    private int ruleLength;

    private int p;

    private int fitness;

    public Individual(int p, int ruleLength) {
        this.p = p;
        this.ruleLength = ruleLength;
        genes = new int[p * ruleLength];

        for (int i = 0; i < genes.length; i++) {
//            int maxRand = isOutputBit(i) ? 2 : 3;
//            int randValue = r.nextInt(maxRand);
            int randValue = r.nextInt(2);
            genes[i] = randValue;
        }
        this.ruleLength = ruleLength;
    }

    public Individual(int[] genes, int ruleLength) {
        this.ruleLength = ruleLength;
        this.p = genes.length / ruleLength;
        this.genes = genes;
    }

    public Individual(String genes, int ruleLength, int p) {
        this.ruleLength = ruleLength;
        this.p = p;
        this.genes = new int[genes.length()];

        for (int i = 0; i < genes.length(); i++) {
            this.genes[i] = Character.getNumericValue(genes.charAt(i));
        }
    }

    public void calculateFitness(Individual dataSet) {
        int totalFitness = 0;
        List<String> data = dataSet.getRuleList();
        Iterator it = data.iterator();
        List<String> population = getRuleList();

        while (it.hasNext()) {
            String rule = (String) it.next();
            for (String s : population) {
                if (checkInput(rule, s)) {
                    if (checkOutput(rule, s)) {
                        totalFitness++;
                    }
                    break;
                }
            }
        }

        setFitness(totalFitness);
    }

    private boolean checkInput(String r1, String r2) {
        for (int i = 0; i < ruleLength - 1; i++) {
            if (r2.charAt(i) != 2 && r1.charAt(i) != r2.charAt(i)) {
                return false;
            }
        }

        return true;
    }
    
    private boolean checkOutput(String r1, String r2) {
        return r1.charAt(ruleLength - 1) == r2.charAt(ruleLength - 1);
    }

    public List<String> getRuleList() {
        return getGenes(printGenes());
    }

    public ArrayList<String> getGenes(String geneString) {
        ArrayList<String> genes = new ArrayList<>();
        int startIndex = 0;
        int endIndex = ruleLength;

        for (int i = 0; i <= geneString.length() / ruleLength; i++) {
            if (endIndex > geneString.length()) {
                break;
            }

            genes.add(geneString.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex += ruleLength;
        }

        return genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    public int[] getGenes() {
        return genes;
    }

    public void setGenes(int[] genes) {
        this.genes = genes;
    }

    public int getRuleLength() {
        return ruleLength;
    }

    public void setRuleLength(int ruleLength) {
        this.ruleLength = ruleLength;
    }

    public int getP() {
        return p;
    }

    public void setP(int p) {
        this.p = p;
    }

    public String printGenes() {
        String genes = "";

        for (int i : getGenes()) {
            genes += i;
        }

        return genes;
    }

    public boolean isOutputBit(int i) {
        return i != 0 && i % (ruleLength - 1) == 0;
    }

    public Individual copy() {
        return new Individual(printGenes(), getRuleLength(), getP());
    }

    @Override
    public String toString() {
        return "Individual{" + "genes=" + Arrays.toString(getGenes()) + ", fitness=" + fitness + '}';
    }
}
