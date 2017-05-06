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
import org.jgap.impl.CrossoverOperator;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.MutationOperator;



public class GeneticAlgorithm {
    
    //Algorithm variables
    private int generationLimit;
    private int populationSize;
    private MyFitnessFunction myFitnessFunction;
    private Genotype population;
    private IChromosome bestSolution;
    private Selection selection = Selection.Roulette;
    private int mutationChance;
    private int recombinationChance;
    private Configuration gaConf;
    private double maxFitness=-1;
    private int noImprovementGenerations=0;
    private int percentOfChangeOperatorChance=10000;
    private int stoppedGeneration=-1;
    private int mutChance=1000;
    private double recChance=0.8d;
    //Problem variables
    private int railLength,origrailLength=0;
    private static ArrayList<Integer> railSizes,railAmounts,origrailAmounts;
    private String fileToLoad;
    private long timePassed=0;
    private boolean elitismEnabled = false;
    
    //guideRailSelection variables
    private int index=0;
    private int lengthOfMyUsedRails=0;
    private int forcedTimesToBeUsed;
    public boolean guidedRailSelectionChecker=false;
    
    //fix the chromosome's fittness
    private IChromosome fixedChromosome;
    private int bestGeneration;
    private int evocounter;
    private double bestFitnessValue;
    
    public GeneticAlgorithm(String fileToLoad){
        
        this.fileToLoad = fileToLoad;
        railSizes = new ArrayList<>();
        railAmounts = new ArrayList<>();
        
        try {
            BufferedReader br = new BufferedReader((new FileReader(fileToLoad)));
            railLength=Integer.parseInt(br.readLine());
            origrailLength= railLength;
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
    
    //public void recoverValues(){
     //   railAmounts = (ArrayList<Integer>)origrailAmounts.clone();
    //    railLength = origrailLength;
        
    //}
    
    public int getBestGeneration() {
        return bestGeneration;
    }
    
    public int getStoppedGeneration() {
        return stoppedGeneration;
    }
    
    public void setMutChance(int mutChance) {
        this.mutChance = mutChance;
    }
    
    public void setRecChance(int recChance) {
        this.recChance = recChance;
    }
    
    public IChromosome getFixedChromosome() {
        return fixedChromosome;
    }
    
    public void setFixedChromosome(IChromosome fixedChromosome) {
        this.fixedChromosome = fixedChromosome;
    }
    
    
    public int getPercentOfChangeOperatorChance() {
        return percentOfChangeOperatorChance;
    }
    
    public void setPercentOfChangeOperatorChance(int percentOfChangeOperatorChance) {
        this.percentOfChangeOperatorChance = percentOfChangeOperatorChance;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getForcedTimesToBeUsed() {
        return forcedTimesToBeUsed;
    }
    
    public int getLengthOfMyUsedRails() {
        return lengthOfMyUsedRails;
    }
    
    public void setLengthOfMyUsedRails(int lengthOfMyUsedRails) {
        this.lengthOfMyUsedRails = lengthOfMyUsedRails;
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
        this.stoppedGeneration = generationLimit;
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
    
    public static void setRailAmounts(ArrayList<Integer> railAmounts) {
        GeneticAlgorithm.railAmounts = railAmounts;
    }
    
    public static void setRailAmountElement(int index, int value){
        railAmounts.set(index, railAmounts.get(index) + value);
    }
    
    public long getTimePassed() {
        return timePassed;
    }
    
    
    
    public String getFileToLoad() {
        return fileToLoad;
    }
    
    public void setFileToLoad(String fileToLoad) {
        this.fileToLoad = fileToLoad;
    }
    
    //forcing the algorithm to use the biggest rail x times(in order to speed up
    //the process whilst selecting the minimum connections between rails)
    public void guidedRailSelection() {
        guidedRailSelectionChecker=false;
        int max=0,maxAmounts=0,check;
        //finding the biggest rail and times that can be used
        for(int i=0;i<railSizes.size();i++){
            if(max<railSizes.get(i)){
                max=railSizes.get(i);
                maxAmounts=railAmounts.get(i);
                index=i;
            }
        }
        
       
        
        //check:how many times I could use the biggest rail
        //forcedTimesToBeUsed:the times I choose to use the rail(therefore
        //I remove from the available maxAmount. I leave the remaining amount
        //of rails for the algorithm to choose-always 2)
        railLength=origrailLength;
        System.out.println(railLength+" "+origrailLength);
        //origrailAmounts = (ArrayList<Integer>)railAmounts.clone();
        
        check=railLength/max;
        if(maxAmounts>=3){
            guidedRailSelectionChecker=true;
            forcedTimesToBeUsed=maxAmounts-2;
            if(forcedTimesToBeUsed<=check-2){
                railAmounts.set(index,maxAmounts-forcedTimesToBeUsed);
                lengthOfMyUsedRails=forcedTimesToBeUsed*max;
                railLength-=lengthOfMyUsedRails;
                System.out.println("_________!____________"+"\n"+"RailLength in guidedRailSelection() "+railLength+"\n"+"__________!_________");
            }
            else{
                forcedTimesToBeUsed=check-2;
                railAmounts.set(index,maxAmounts-forcedTimesToBeUsed);
                lengthOfMyUsedRails=forcedTimesToBeUsed*max;
                railLength-=lengthOfMyUsedRails;
                System.out.println("__________!___________"+"\n"+"RailLength in guidedRailSelection() "+railLength+"\n"+"__________!_________");
            }
            //origrailLength=railLength;
        }
        //return guidedRailSelectionChecker;
    }
    
    
    public void evolve() throws InvalidConfigurationException{
        evolve(recChance,mutChance);
    }
    
    
    public void evolve(double recRate, int mutRate) throws InvalidConfigurationException{
        timePassed = 0;
        stoppedGeneration = generationLimit;
        evocounter = 0;
        maxFitness = -1;
        
        if(gaConf==null){
            gaConf = new MyConfiguration(selection,recRate,mutRate);
            myFitnessFunction = new MyFitnessFunction(this.railLength);
            try {
                gaConf.setFitnessFunction(myFitnessFunction);
            } catch (InvalidConfigurationException ex) {
                Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(elitismEnabled)
                gaConf.setPreservFittestIndividual(true);
            Gene[] genes = new Gene[railAmounts.size()];
            
            
            for(int i=0;i<railAmounts.size();i++){
                genes[i] = new IntegerGene(gaConf,0,railAmounts.get(i));
            }
            IChromosome myChromosome = new Chromosome(gaConf,genes);
            gaConf.setSampleChromosome(myChromosome);
            gaConf.setPopulationSize(populationSize);
        }

        population = Genotype.randomInitialGenotype(gaConf);
        
        timePassed = System.currentTimeMillis();
        for(int i=0;i<generationLimit;i++){
            population.evolve();
            
            bestFitnessValue = population.getFittestChromosome().getFitnessValue();
            population.getPopulation().sortByFitness();
            
            //System.out.println(population.getFittestChromosome().getFitnessValue()+"****"+population.getPopulation().getChromosome(population.getPopulation().size()/2).getFitnessValue()+"****");
            if(asdfg(bestFitnessValue)){
                stoppedGeneration = i;
                break;
            }
            
        }
        
        
        timePassed = System.currentTimeMillis() - timePassed;
        bestSolution = population.getFittestChromosome();
        System.out.println("______________________"+"\n"+"RailLength in evolve[before fixation]:"+railLength+"\n"+"____________________");
        //fix The Chromosome if needed
        if(guidedRailSelectionChecker){
            fixChromosome(index,forcedTimesToBeUsed);
            
            railLength=(getChromosomeRailLength(bestSolution)+lengthOfMyUsedRails);
            //System.out.println("(2)railLength in evolve: "+railLength);
            System.out.println("______________________"+"\n"+"(1)RailLength in evolve[after fixation] :"+railLength+"\n"+"____________________");
            myFitnessFunction.setRailLength(railLength);
        }
        
    }
    
    public void fixChromosome(int index, int value) throws InvalidConfigurationException{
        fixedChromosome=(IChromosome)bestSolution.clone();
        int totalAmount=((Integer)fixedChromosome.getGene(index).getAllele()).intValue()+value;
        fixedChromosome.getGenes()[index] = new IntegerGene(gaConf,0,totalAmount);
        railAmounts.set(index, railAmounts.get(index)+value);
        fixedChromosome.getGene(index).setAllele(totalAmount);
        
        //fixedChromosome.setFitnessValue(myFitnessFunction.evaluate(fixedChromosome));
    }
    
    
    
    public double getFittestChromosomeFitness(){
        return bestSolution.getFitnessValue();
        
    }
    
    public int getChromosomeRailLength(IChromosome chromosome){
        int length = 0;
        for(int i=0;i<chromosome.getGenes().length;i++){
            length += railSizes.get(i)*((Integer)chromosome.getGene(i).getAllele()).intValue();
        }
        return length;
    }
    
    public int getChromosomeRailPoints(IChromosome chromosome){
        int points = 0;
        for(int i=0;i<chromosome.getGenes().length;i++){
            points+=((Integer)chromosome.getGene(i).getAllele()).intValue();
        }
        return --points;
    }
    
    public int[] getFittestChromosomeAsIntArray(){
        int[] genes = new int[bestSolution.getGenes().length];
        for(int i=0;i<genes.length;i++){
            genes[i] = ((Integer)bestSolution.getGene(i).getAllele()).intValue();
        }
        return genes;
    }
    
    public IChromosome getBestSolution() {
        return bestSolution;
    }
    
    private boolean asdfg(double fitness){
        noImprovementGenerations++;
        evocounter++;
        if(maxFitness<fitness){
            maxFitness = fitness;
            bestGeneration = evocounter;
            noImprovementGenerations=0;
        }
        
        if(evocounter>percentOfChangeOperatorChance+bestGeneration){
            return true;
        }
        return false;
    }
    
}
