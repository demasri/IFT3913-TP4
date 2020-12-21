import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/***
 * FileManager handles the search and extraction of all .java files in
 * the folder to analyze.
 * @author William Bach (20127144)
 *
 */
public class FileManager {
    String path; //absolute path of the folder to analyze

    /**
     * Constructor
     * @param path attribute 
     */
    public FileManager(String path)
    {
        this.path = path;
    }

    /**
     * Sends to CodeAnalyzer every line of every .java file in the attribute 
     * folder
     * @return 2D Strings array that contains the line arrays of every class 
     * 
     * SOURCE: https://stackabuse.com/java-list-files-in-a-directory/
     */
    public String[][] getClassesArray()
    {
        //get the .java File objects 
        File[] classes = listFiles(this.path);
        
//        // Remove the useless files in classes array to keep only the .java files in folder
//        List<File> classList = new ArrayList<File>();
//        for(File s : classes) {
//            if(s != null && s.length() > 0) {
//               classList.add(s);
//            }
//         }
//        classes = classList.toArray(new File[classList.size()]);
        
        //store the lines of every file found
        ArrayList<ArrayList<String>> classesArray = new ArrayList<ArrayList<String>>();
        //extract every line of every File
        for (var i=0; i<classes.length; i++) 
        {
            try { 
            	//on utilise le module 'Files' pour extraire les lignes en une seule fois
                List<String> allLines = Files.readAllLines(
                    Paths.get(classes[i].getAbsolutePath()));
                //convert List of lines to ArrayList for convenience
                ArrayList<String> allLinesArrayList = new ArrayList<String>(allLines);
                //add to the @return array
                classesArray.add(allLinesArrayList);
            } 
            catch (IOException e) {
                System.out.println("Something went wrong (getClassesArray)");
                System.exit(1);
            }
        }

        //convert ArrayList to a String array for the main class
        String[][] finalArray = new String[classesArray.size()][];
        for (var i=0; i<classesArray.size(); i++)
        {
            ArrayList<String> fileContent = classesArray.get(i);
            finalArray[i] = fileContent.toArray(new String[fileContent.size()]);
        }
        return finalArray;

    }

    /**
     * Sends to CodeAnalyzer the name of all the classes to be analyzed
     * @return String array of names
     */
    public String[] getClassNamesArray() 
    {
        File[] classes = listFiles(this.path); 
        // Create the classNames array with only the .java files in folder
        String[] classNames = new String[classes.length];

        for (var i=0; i<classNames.length; i++)
        {
            classNames[i] = classes[i].getName().replace(".java", "");
        } 

        return classNames;
    }

    /**
     * Renvoie tous les chemins absolus des files .java dans le folder
     * @return array de Strings
     */
    public String[] getAbsolutePaths() 
    {
        File[] files = listFiles(this.path);
        String[] paths = new String[files.length];
        for (var i=0; i < files.length; i++) {
            paths[i] = files[i].getAbsolutePath();
        }

        return paths;
    }

    /**
     * Searches the specified folder  
     * @param startDir folder
     */
    private File[] listFiles(String startDir) 
    {

        //first we extract the files without looking at them
        File[] files = null;
        try {
            //extracts files from up to 20 inner folders
            files = Files.walk(Paths.get(startDir), 20)
            .filter(Files::isRegularFile) //no filter
            .map(Path::toFile)
            .toArray(File[]::new);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
            System.exit(1);
        }
    
        //then extraction of files .java 
        File[] javaFiles = new File[files.length];
        int counter = 0;
        for (var i=0; i<files.length; i++)
        {
            if (files[i].getName().endsWith(".java")) {
                javaFiles[counter] = files[i];
                counter++;
            }
        }
        
        // Remove the useless files in classes array to keep only the .java files in folder
        List<File> javaFilesList = new ArrayList<File>();
        for(File s : javaFiles) {
            if(s != null && s.length() > 0) {
               javaFilesList.add(s);
            }
         }
        javaFiles = javaFilesList.toArray(new File[javaFilesList.size()]);
        
        return javaFiles;
        
    } 
}
    
