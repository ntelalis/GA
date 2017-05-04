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

public class Menu {
    public static void main(String[] args)throws IOException{
        String path = "C:\\Users\\shadowalker\\Desktop\\test.txt";
        
        GeneticAlgorithm ga = new GeneticAlgorithm(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
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
        
        System.out.print("Which selection method you want to use?(T / R): ");
        String selection;
        do{
            selection=br.readLine().toUpperCase();
            if(selection.equals("T"))
                ga.setSelection(Selection.Tournament);
            else if(selection.equals("R"))
                ga.setSelection(Selection.Roulette);
            else
                System.out.println("Give your choice again:");
        }while(!(selection.equals("T") || selection.equals("R")));
        
        System.out.print("Do you want to enable elitism?(Y / N): ");
        String elitism;
        
        do{
            elitism=br.readLine().toUpperCase();
            if(elitism.equals("Y"))
                ga.setElitismEnabled(true);
            else if(elitism.equals("N"))
                ga.setElitismEnabled(false);
            else
                System.out.print("Give your choice again:");
        }while(!(elitism.equals("Y") || elitism.equals("N")));
        
        
        ga.evolve();
        
        System.out.println("Fitness="+ga.getFittestChromosomeFitness());
        System.out.println("Total Length="+ga.getFittestChromosomeRailLength());
        System.out.println("Intersections="+ga.getFittestChromosomeRailPoints());
        System.out.print("Chromosome=[ ");
        for(int i=0;i<ga.getFittestChromosome().length;i++){
            System.out.print(ga.getFittestChromosome()[i]+" ");
        }
        System.out.println("]");
        
        
    }
}

