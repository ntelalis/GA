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
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import org.jgap.Chromosome;
import org.jgap.InvalidConfigurationException;

public class Menu {
    public static void main(String[] args)throws IOException, InvalidConfigurationException{
        
        
        
        
        String path = Paths.get(".").toAbsolutePath().normalize().toString();
        if(System.getProperty("os.name").contains("indows"))
            path +="\\src\\sources\\test.txt";
        else
            path += "/src/sources/test.txt";
        
        
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
        
        
        ga.evolve();
        MyFitnessFunction mff = new MyFitnessFunction(ga.getRailLength());
        System.out.println();
        
        if(ga.guidedRailSelectionChecker){
            
            mff = new MyFitnessFunction(ga.getRailLength());
            System.out.println("Total Time="+ga.getTimePassed()+"ms");
            //System.out.println("Fitness="+ga.getBestSolution().getFitnessValue());
            //System.out.println("FitnessRecall="+ga.getFixedChromosome().getFitnessValue());
            System.out.println("Fitness="+mff.evaluate(ga.getFixedChromosome()));
            System.out.println("Total Length="+ga.getRailLength());
            System.out.println("Intersections="+(ga.getChromosomeRailPoints(ga.getFixedChromosome())));
            System.out.print("Chromosome=[ ");
            for(int i=0;i<ga.getFittestChromosomeAsIntArray().length;i++){
                if(i==ga.getIndex()){
                    System.out.print((ga.getFittestChromosomeAsIntArray()[i]+ga.getForcedTimesToBeUsed())+" ");
                }
                else{
                    System.out.print(ga.getFittestChromosomeAsIntArray()[i]+" ");
                }
            }
            System.out.println("]");
        }
        else{
            System.out.println("Total Time="+ga.getTimePassed()+"ms");
            System.out.println("Fitness="+ga.getFittestChromosomeFitness());
            //System.out.println("Fitness="+ga.getBestSolution().getFitnessValue());
            //System.out.println("FitnessReCalculate="+mff.evaluate(ga.getBestSolution()));
            System.out.println("Total Length="+ga.getChromosomeRailLength(ga.getBestSolution()));
            System.out.println("Intersections="+(ga.getChromosomeRailPoints(ga.getBestSolution())));
            System.out.print("Chromosome=[ ");
            for(int i=0;i<ga.getFittestChromosomeAsIntArray().length;i++){
                System.out.print(ga.getFittestChromosomeAsIntArray()[i]+" ");
            }
            System.out.println("]");
            
        }
        
        
    }
}

