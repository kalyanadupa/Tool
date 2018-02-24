package merge;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aadupa
 */
public class Merge {

    public static void main(String[] args) throws FileNotFoundException {
        utils u = new utils();
        String[] baseline310 = u.readFile("baseline310.java");
        String[] nyc310 = u.readFile("nyc310.java");
        String[] baseline311 = u.readFile("baseline311.java");
        u.utils();
        u.compareFiles(baseline310, nyc310, baseline311);
        System.out.println(u.merged.toString());
    }
}