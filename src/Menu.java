/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/**
 *
 * @author shadowalker
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;
import org.jgap.Chromosome;
import org.jgap.InvalidConfigurationException;

public class Menu {
    public static void main(String[] args)throws IOException, InvalidConfigurationException, InterruptedException{
        
        
        DecimalFormat df = new DecimalFormat("0.000");
        
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        String path2 = Paths.get(".").toAbsolutePath().normalize().toString();
        if(System.getProperty("os.name").contains("indows")){
            path +="\\src\\sources\\test.txt";
            path2 +="\\src\\sources\\results.txt";
        }
        else{
            path += "/src/sources/test.txt";
            path2 += "/src/sources/results.txt";
        }
        
        
        FileWriter fw = new FileWriter(path2);
        BufferedWriter bw = new BufferedWriter(fw);
        
        GeneticAlgorithm ga = new GeneticAlgorithm(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        //enabling the guidedRailSelection()
        //boolean check1=false;
        String selection;
        System.out.println("Do you want to use Adamidis' knowledge?? xD");
        do{
            System.out.print("type (Y / N)[N]: ");
            selection=br.readLine().toUpperCase();
            if(selection.equals("Y"))
                ga.guidedRailSelection();
            else if(selection.equals("N"))
                break;
        }while(!(selection.equals("Y") || selection.equals("N") || selection.equals("")));
        
        
        
        System.out.print("Set the generation Limit(1000): ");
        String check=br.readLine();
        if(check.equals(""))
            ga.setGenerationLimit(1000);
        else{
            int generationLimit = Integer.parseInt(check);
            ga.setGenerationLimit(generationLimit);
        }
        
        
        System.out.print("Set the population size(50): ");
        check=br.readLine();
        if(check.equals(""))
            ga.setPopulationSize(50);
        else{
            int populationSize=Integer.parseInt(check);
            ga.setPopulationSize(populationSize);
        }
        
        System.out.print("Which selection method you want to use?(T / R)[R]: ");
        //String selection;
        do{
            selection=br.readLine().toUpperCase();
            if(selection.equals("T"))
                ga.setSelection(Selection.Tournament);
            else if(selection.equals("R"))
                ga.setSelection(Selection.Roulette);
            else
                ga.setSelection(Selection.Roulette);
            //System.out.println("Give your choice again:");
        }while(!(selection.equals("T") || selection.equals("R") || selection.equals("")));
        //}while(!(selection.equals("T") || selection.equals("R")));
        
        System.out.print("Do you want to enable elitism?(Y / N)[Y]: ");
        String elitism;
        
        do{
            elitism=br.readLine().toUpperCase();
            if(elitism.equals("Y"))
                ga.setElitismEnabled(true);
            else if(elitism.equals("N"))
                ga.setElitismEnabled(false);
            else
                ga.setElitismEnabled(true);
            //System.out.print("Give your choice again:");
        }while(!(elitism.equals("Y") || elitism.equals("N") || elitism.equals("")));
        //}while(!(elitism.equals("Y") || elitism.equals("N")));
        
        System.out.print("Give Recombination Chance[0.8]: ");
        double recChance;
        String choice = br.readLine();
        try{
            recChance = Integer.parseInt(choice);
        }
        catch(NumberFormatException e){
            recChance = 0.8d;
        }
        
        System.out.print("Give Mutation Chance 1/x[500]: ");
        int mutChance;
        choice = br.readLine();
        try{
            mutChance = Integer.parseInt(choice);
        }
        catch(NumberFormatException e){
            mutChance = 500;
        }
        for(int i=0;i<1000;i++){
            //TimeUnit.SECONDS.sleep(1);
            ga.evolve(recChance,mutChance);
            
            MyFitnessFunction mff = new MyFitnessFunction(ga.getRailLength());
            String evolutionRunString="";
            
            System.out.println();
            System.out.println("Total Time="+ga.getTimePassed()+"ms");
            evolutionRunString+=ga.getTimePassed()+",";
            System.out.println("Stopped at generation="+ga.getStoppedGeneration());
            evolutionRunString+=ga.getStoppedGeneration()+",";
            System.out.println("Best Solution found at generation="+(ga.getBestGeneration()));
            evolutionRunString+=ga.getBestGeneration()+",";
            if(ga.guidedRailSelectionChecker){
                mff = new MyFitnessFunction(ga.getRailLength());
                
                //System.out.println("Fitness="+ga.getBestSolution().getFitnessValue());
                //System.out.println("FitnessRecall="+ga.getFixedChromosome().getFitnessValue());
                System.out.println("Fitness="+df.format(mff.evaluate(ga.getFixedChromosome())));
                evolutionRunString+=df.format(mff.evaluate(ga.getFixedChromosome()))+",";
                System.out.println("Total Length="+ga.getRailLength());
                evolutionRunString+=ga.getRailLength()+",";
                System.out.println("Intersections="+(ga.getChromosomeRailPoints(ga.getFixedChromosome())));
                evolutionRunString+=ga.getChromosomeRailPoints(ga.getFixedChromosome())+",";
                System.out.print("Chromosome=[ ");
                for(int j=0;j<ga.getFittestChromosomeAsIntArray().length;j++){
                    if(j==ga.getIndex()){
                        System.out.print((ga.getFittestChromosomeAsIntArray()[j]+ga.getForcedTimesToBeUsed())+" ");
                        evolutionRunString+=(ga.getFittestChromosomeAsIntArray()[j]+ga.getForcedTimesToBeUsed())+" ";
                    }
                    else{
                        System.out.print(ga.getFittestChromosomeAsIntArray()[j]+" ");
                        evolutionRunString+=ga.getFittestChromosomeAsIntArray()[j]+" ";
                    }
                }
                System.out.println("]");
                //ga.recoverValues();
                ga.guidedRailSelection();
            }
            else{
                System.out.println("Fitness="+df.format(ga.getFittestChromosomeFitness()));
                evolutionRunString+=df.format(ga.getFittestChromosomeFitness())+",";
                //System.out.println("Fitness="+ga.getBestSolution().getFitnessValue());
                //System.out.println("FitnessReCalculate="+mff.evaluate(ga.getBestSolution()));
                System.out.println("Total Length="+ga.getChromosomeRailLength(ga.getBestSolution()));
                evolutionRunString+=ga.getChromosomeRailLength(ga.getBestSolution())+",";
                System.out.println("Intersections="+(ga.getChromosomeRailPoints(ga.getBestSolution())));
                evolutionRunString+=ga.getChromosomeRailPoints(ga.getBestSolution())+",";
                System.out.print("Chromosome=[ ");
                for(int j=0;j<ga.getFittestChromosomeAsIntArray().length;j++){
                    System.out.print(ga.getFittestChromosomeAsIntArray()[j]+" ");
                    evolutionRunString+=ga.getFittestChromosomeAsIntArray()[j]+" ";
                }
                System.out.println("]");
                
            }
            evolutionRunString+="\n";
            bw.write(evolutionRunString);
            
        }
        
        bw.close();
    }
}

