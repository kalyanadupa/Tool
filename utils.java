package merge;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author aadupa
 */
public class utils {

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

    public String[] readFile(String fileName) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        List<String> lines = new ArrayList<String>();
        while (sc.hasNextLine()) {
            lines.add(sc.nextLine());
        }

        String[] arr = lines.toArray(new String[0]);
        return arr;
    }

    /*
    compares two String arrays and writes the output merge into Merged List
     */
    void compareFiles(String[] b310, String[] n310, String[] b311) {
        reInitialize();
        int b310Index = 0;
        int n310Index = 0;
        int b311Index = 0;
        int[] indexes = null;
        int b310Length = b310.length;
        int n310Length = n310.length;
        int b311Length = b311.length;

        //The following loop runs for all the lines in both arrays simultaneously
        while ((b310Index < b310Length) && (n310Index < n310Length) && (b311Index < b311Length)) {
            // If the lines compared are same, it writes them in merged List
            if ( (b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))) && (b311[b311Index].replaceAll("\\s+", "").equals(b310[b310Index].replaceAll("\\s+", "")) ) ) {
                merged.append(b310[b310Index] + "\n");
                b310Index++;
                n310Index++;
                b311Index++;
            }
            else if ((b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))) && (!(b311[b311Index].replaceAll("\\s+", "").equals(b310[b310Index].replaceAll("\\s+", ""))))) {
                reInitialize();
                int startb310Index = b310Index;
                int startn310Index = n310Index;
                while (b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", ""))) {
                    b310Index++;
                    n310Index++;
                    if((b310Index < b310.length) || (n310Index < n310.length)){
                        break;
                    }
                }
                indexes = analyze(b310, startb310Index, b311, b311Index,b310Index+1);
                n310Index = startn310Index + (indexes[0] - startb310Index);
                b310Index = indexes[0];
                b311Index = indexes[1];
                if(!diffAdded.toString().equals("")){
                    merged.append(diffAdded);
                }
                else if(!diffModified.toString().equals("")){
                        merged.append(diffModified);
                    }
            }
            else {
                reInitialize();
                if(!(b310[b310Index].replaceAll("\\s+", "").equals(n310[n310Index].replaceAll("\\s+", "")))){
                    indexes = analyze(b310, b310Index, n310, n310Index,b310.length);
                    b310Index = indexes[0];
                    n310Index = indexes[1];
                    if(!diffAdded.toString().equals("")){
                        merged.append(diffAdded);
                    }
                    else if(!diffModified.toString().equals("")){
                        merged.append(diffModified);
                    }
                }
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

    int[] analyze(String[] baselineFile, int baselineIndex, String[] customizedFile, int customizedIndex, int end) {
        int tBaselineIndex = baselineIndex;
        int tCustomizedIndex = customizedIndex;
        boolean updated = false;
        for (int i = baselineIndex; i < end; i++) {
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
