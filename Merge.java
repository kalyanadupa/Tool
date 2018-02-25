package merge;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileNotFoundException;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author aadupa
 */
public class Merge {

    public static void main(String[] args) throws FileNotFoundException {
        BasicConfigurator.configure();
        utils u = new utils();
        ArrayList<String> baseline310= new ArrayList<String>();
        ArrayList<String> nyc310= new ArrayList<String>();
        ArrayList<String> baseline311= new ArrayList<String>();
        baseline310 = u.readFile("baseline310.java");
        nyc310 = u.readFile("nyc310.java");
        baseline311 = u.readFile("baseline311.java");
        u.utils();
        u.compareFiles(baseline310, nyc310, baseline311);
        System.out.println(u.merged.toString());
    }
}