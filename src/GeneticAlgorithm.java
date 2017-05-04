/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author shadowalker
 */

import org.jgap.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jgap.impl.IntegerGene;



public class GeneticAlgorithm {
    
    //Algorithm variables
    private int generationLimit;
    private int populationSize;
    private FitnessFunction myFitnessFunction;
    private Genotype population;
    private IChromosome bestSolution;
    private Selection selection = Selection.Roulette;
    private int mutationChance;
    private int recombinationChance;
    
    //Problem variables
    private int railLength;
    private static ArrayList<Integer> railSizes,railAmounts;
    private String fileToLoad;
    
    private boolean elitismEnabled = false;
    
    
    
    public GeneticAlgorithm(String fileToLoad){
        
        this.fileToLoad = fileToLoad;
        railSizes = new ArrayList<>();
        railAmounts = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader((new FileReader(fileToLoad)));
            railLength=Integer.parseInt(br.readLine());
            String railValueLine = br.readLine();
            String[] railValues;
            while(!railValueLine.equals("0 0")){
                railValues = railValueLine.split(" ");
                railSizes.add(Integer.parseInt(railValues[0]));
                railAmounts.add(Integer.parseInt(railValues[1]));
                railValueLine = br.readLine();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isElitismEnabled() {
        return elitismEnabled;
    }
    
    public void setElitismEnabled(boolean elitismEnabled) {
        this.elitismEnabled = elitismEnabled;
    }
    
    public Selection getSelection() {
        return selection;
    }
    
    public void setSelection(Selection selection) {
        this.selection = selection;
    }
    
    public int getMutationChance() {
        return mutationChance;
    }
    
    public void setMutationChance(int mutationChance) {
        this.mutationChance = mutationChance;
    }
    
    public int getRecombinationChance() {
        return recombinationChance;
    }
    
    public void setRecombinationChance(int recombinationChance) {
        this.recombinationChance = recombinationChance;
    }
    
    public int getPopulationSize() {
        return populationSize;
    }
    
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }
    
    public int getGenerationLimit() {
        return generationLimit;
    }
    
    public void setGenerationLimit(int generationLimit) {
        this.generationLimit = generationLimit;
    }
    
    public int getRailLength() {
        return railLength;
    }
    
    public void setRailLength(int railLength) {
        this.railLength = railLength;
    }
    
    public static ArrayList<Integer> getRailSizes() {
        return railSizes;
    }
    
    public static int getRailSize(int index){
        return railSizes.get(index);
    }
    
    public static ArrayList<Integer> getRailAmounts() {
        return railAmounts;
    }
    
    public String getFileToLoad() {
        return fileToLoad;
    }
    
    public void setFileToLoad(String fileToLoad) {
        this.fileToLoad = fileToLoad;
    }
    
    public void guideRailSelection() {
            
            //for(int i=0;i<railSizes.size();i++){
            //    max
            //}
            //railSizes
        }
    
    
    
    
    
    public void evolve(){
        Configuration gaConf = new MyConfiguration(selection);
        
        myFitnessFunction = new MyFitnessFunction(this.railLength);
        try {
            gaConf.setFitnessFunction(myFitnessFunction);
        } catch (InvalidConfigurationException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(elitismEnabled)
            gaConf.setPreservFittestIndividual(true);
        
        Gene[] genes = new Gene[railAmounts.size()];
        
        try{
            for(int i=0;i<railAmounts.size();i++){
                genes[i] = new IntegerGene(gaConf,0,railAmounts.get(i));
            }
            IChromosome myChromosome = new Chromosome(gaConf,genes);
            gaConf.setSampleChromosome(myChromosome);
            
            gaConf.setPopulationSize(populationSize);
            
            population = Genotype.randomInitialGenotype(gaConf);
            
            for(int i=0;i<generationLimit;i++){
                population.evolve();
            }
            bestSolution = population.getFittestChromosome();
        }
        catch(InvalidConfigurationException ex){
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public double getFittestChromosomeFitness(){
        return bestSolution.getFitnessValue();
    }
    
    public int getFittestChromosomeRailLength(){
        int length = 0;
        for(int i=0;i<bestSolution.getGenes().length;i++){
            length += railSizes.get(i)*((Integer)bestSolution.getGene(i).getAllele()).intValue();
        }
        return length;
    }
    
    public int getFittestChromosomeRailPoints(){
        int points = 0;
        for(int i=0;i<bestSolution.getGenes().length;i++){
            points+=((Integer)bestSolution.getGene(i).getAllele()).intValue();
        }
        return --points;
    }
    
    public int[] getFittestChromosome(){
        int[] genes = new int[bestSolution.getGenes().length];
        for(int i=0;i<genes.length;i++){
            genes[i] = ((Integer)bestSolution.getGene(i).getAllele()).intValue();
        }
        return genes;
    }

}