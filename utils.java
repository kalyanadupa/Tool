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
import org.apache.log4j.*;

/**
 *
 * @author aadupa
 */
public class utils {
    private static org.apache.log4j.Logger log = Logger.getLogger(utils.class);
    

    StringBuilder diffRemoved, diffAdded, diffModified;
    StringBuilder merged;



    public void utils() {
        merged = new StringBuilder();


    }
    
    public void reInitialize() {
        diffRemoved = new StringBuilder();
        diffAdded = new StringBuilder();
        diffModified = new StringBuilder();
    }

    public ArrayList<String> readFile(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        ArrayList<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        
        return lines;
    }

    /*
    compares two String arrays and writes the output merge into Merged List
     */
    void compareFiles(ArrayList<String> b310, ArrayList<String> n310, ArrayList<String> b311) {
        log.setLevel(Level.TRACE);
        reInitialize();
        int b310Index = 0;
        int n310Index = 0;
        int b311Index = 0;
        int[] indexes = null;
        int b310Length = b310.size();
        int n310Length = n310.size();
        int b311Length = b311.size();
        int stb310 = 0; // startb310 Index
        int stn310 = 0;
        int stb311 = 0;
        
        //The following loop runs for all the lines in both arrays simultaneously
        while ((b310Index < b310Length) && (n310Index < n310Length) && (b311Index < b311Length)) {
            // If the lines compared are same, it writes them in merged List
            if ( (b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))) && (b311[b311Index].replaceAll("\\s+", "").equals(b310[b310Index].replaceAll("\\s+", "")) ) ) {
                // 111
                log.trace("111:\t"+ b310[b310Index]);
                merged.append(b310[b310Index] + "\n");
                b310Index++;
                n310Index++;
                b311Index++;
            }
            else if ((b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))) && (!(b311[b311Index].replaceAll("\\s+", "").equals(b310[b310Index].replaceAll("\\s+", ""))))) {
                //it is either 110 or 001
                log.trace("\"110 or 001\" b310: "+b310[b310Index]);
                log.trace("\"110 or 001\" n310: "+n310[n310Index]);
                log.trace("\"110 or 001\" b311: "+b311[b311Index]);
                stb310 = b310Index;
                stn310 = n310Index;
                stb311 = b311Index;
                reInitialize();
                indexes = analyze(b310, b310Index, b311,b311Index);
                if (!diffAdded.toString().equals("")) {
                    merged.append(diffAdded);
                }
                b310Index = indexes[0];
                b311Index = indexes[1];
                n310Index = stn310 + (b310Index-stb310);
            }
            else if ((b310[b310Index].replaceAll("\\s+", "").equals(b311[b311Index].replaceAll("\\s+", ""))) && (!(b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))))) {
                //it is either 101 or 001
                log.trace("\"101 or 010\" b310: "+b310[b310Index]);
                log.trace("\"101 or 010\" n310: "+n310[n310Index]);
                log.trace("\"101 or 010\" b311: "+b311[b311Index]);                
                stb310 = b310Index;
                stn310 = n310Index;
                stb311 = b311Index;
                reInitialize();
                indexes = analyze(b310, b310Index, n310,n310Index);
                log.trace("Added:\t"+diffAdded.toString());
                log.trace("Removed:\t"+diffRemoved.toString());
                if (!diffAdded.toString().equals("")) {
                    merged.append(diffAdded);
                }
                b310Index = indexes[0];
                n310Index = indexes[1];
                b311Index = stb311 + (b310Index - stb310);
                log.trace("After 101 "+ b310[b310Index] + "//"+b310Index);
                log.trace("After 101 "+ n310[n310Index] + "//"+n310Index);
                log.trace("After 101 "+ b311[b311Index] + "//"+b311Index);
            }
            else if ((n310[n310Index].replaceAll("\\s+", "").equals(b311[b311Index].replaceAll("\\s+", ""))) && (!(b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))))) {
                //it is either 110 or 001
                log.trace("\"011 or 100\" b310: "+b310[b310Index]);
                log.trace("\"011 or 100\" n310: "+n310[n310Index]);
                log.trace("\"011 or 100\" b311: "+b311[b311Index]);                   
                stb310 = b310Index;
                stn310 = n310Index;
                stb311 = b311Index;
                reInitialize();
                indexes = analyze(b310, b310Index, n310,n310Index);
                if (!diffAdded.toString().equals("")) {
                    merged.append(diffAdded);
                }
                b310Index = indexes[0];
                n310Index = indexes[1];
                b311Index = stb311 + (n310Index - stn310);
                log.trace("After 011 "+ b310[b310Index] + "//"+b310Index);
                log.trace("After 011 "+ n310[n310Index] + "//"+n310Index);
                log.trace("After 011 "+ b311[b311Index] + "//"+b311Index);                
            }
            else{
                log.error(b310Index + " b310: "+b310[b310Index]);
                log.error(n310Index + "n310: "+n310[n310Index]);
                log.error(b311Index + "b311: "+b311[b311Index]);
                break;
            }
        }
        // When the baselineindex pointer is still not done but the customIndex pointer is done, it parses the rest of the file
        if (b310Index < b310Length) {
            for (int i = b310Index; i < b310Length; i++) {
                merged.append(b310[i] + "\n");
            }
        } else if (n310Index < n310Length) {
            for (int i = n310Index; i < n310Length; i++) {
                merged.append(n310[i] + "\n");
            }
        }
    }

    int[] analyze(String[] baselineFile, int baselineIndex, String[] customizedFile, int customizedIndex) {
        int tBaselineIndex = baselineIndex;
        int tCustomizedIndex = customizedIndex;
        boolean updated = false;
        for (int i = baselineIndex; i < baselineFile.length; i++) {
            if ((baselineFile[i].replaceAll("\\s+", "")).equals(customizedFile[customizedIndex].replaceAll("\\s+", ""))) {
                tBaselineIndex = i;

                for (int j = baselineIndex; j < i; j++) {
                    //merged.append(baselineFile[j] + "\n");
                    diffRemoved.append(baselineFile[j] + "\n");
                }
                //merged.append(baselineFile[i] + "\n");
                updated = true;
                break;
            }
        }

        if (tBaselineIndex == baselineIndex) {
            for (int i = customizedIndex; i < customizedFile.length; i++) {
                if ((customizedFile[i].replaceAll("\\s+", "")).equals(baselineFile[baselineIndex].replaceAll("\\s+", ""))) {
                    tCustomizedIndex = i;

                    for (int j = customizedIndex; j < i; j++) {
                        //merged.append(customizedFile[j] + "\n");
                        diffAdded.append(customizedFile[j] + "\n");
                    }
                    //merged.append(customizedFile[i] + "\n");
                    updated = true;
                    break;
                }

            }
        }
        if (!updated) {
            diffModified.append(customizedFile[customizedIndex] + "\n");
            diffModified.append(baselineFile[baselineIndex] + "\n");
            tBaselineIndex++;
            tCustomizedIndex++;
        }

        int[] updatedIndex = {tBaselineIndex, tCustomizedIndex};
        return updatedIndex;
    }

}
