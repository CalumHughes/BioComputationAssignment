/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.ga;

import java.util.ArrayList;
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

    private int n;
    
    private int p;

    private int fitness;

    public Individual(int p, int n) {
        this.p = p;
        this.n = n;
        
        genes = new int[p * n];

        for (int i = 0; i < genes.length; i++) {
            int randValue = r.nextInt(2);
            genes[i] = randValue;
        }
        this.n = n;
    }

    public Individual(int[] genes, int n) {
        this.n = n;
        this.p = genes.length / n;
        this.genes = genes;
    }

    public Individual(String genes, int n, int p) {
        this.n = n;
        this.p = p;
        this.genes = new int[genes.length()];

        for (int i = 0; i < genes.length(); i++) {
            this.genes[i] = Character.getNumericValue(genes.charAt(i));
        }
    }

    public void mutateGenes(double mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            Double d = r.nextDouble();
            if (d < mutationRate) {
                genes[i] = 1 - i;
            }
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
                if (checkRule(rule, s)) {
                    totalFitness++;
                    break;
                }
            }
        }

        setFitness(totalFitness);
    }
    
    private boolean checkRule(String r1, String r2) {
        for(int i = 0; i < n; i++) {
            if(r1.charAt(i) != r2.charAt(i)) {
                return false;
            }
        }
        
        return true;
    }

    public List<String> getRuleList() {
        return getGenes(printGenes());
    }

    public ArrayList<String> getGenes(String geneString) {
        ArrayList<String> genes = new ArrayList<>();
        int startIndex = 0;
        int endIndex = n;
        

        for (int i = 0; i <= geneString.length() / n; i++) {
            if (endIndex > geneString.length()) {
                break;
            }

            genes.add(geneString.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex += n;
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

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
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

    public Individual copy() {
        return new Individual(printGenes(), getN(), getP());
    }

    @Override
    public String toString() {
        return "Individual{" + "genes=" + getGenes() + ", fitness=" + fitness + '}';
    }
}
