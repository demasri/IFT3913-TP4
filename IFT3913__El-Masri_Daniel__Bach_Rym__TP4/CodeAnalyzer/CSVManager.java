import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/***
 * Produit les fichiers CSV 
 * @author William Bach (20127144) && Daniel El-Masri (20096261)
 *
 */
public class CSVManager {
    private String PATH_classes = "CSV/classes.csv";
    private String PATH_methods = "CSV/methods.csv";
    private String DIRECTORY_PATH = "CSV";

    /**
     * Remplit les fichiers CSV avec l'information fournie 
     * @param lines array avec toutes les lignes CSV
     * @param isClassReport true si on update classes.csv
     */
    public void updateCSVFile(String[] lines, boolean isClassReport) 
    {
        //recuperation du chemin du fichier csv
        String PATH;
        
        // selection du fichier dans lequel on ecrit
        if (isClassReport)
        {
        	PATH = this.PATH_classes;
        }
        else
        {
        	PATH = this.PATH_methods;
        }

        File csvFile = new File(PATH);
        
        // If parent directory and file already exists
        if(csvFile.exists())
        {
            try {
                FileWriter csvWriter = new FileWriter(PATH, false);
                int i = 0;
                while (i < lines.length) {
                    csvWriter.append(lines[i]);
                    csvWriter.flush();
                    i++;
                }
                csvWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Something went wrong. Can't update CSV files.");
            }
        }
        else
        {
        	File parentDirectory = new File(DIRECTORY_PATH);
      
        	// If parent directory exists but not csv file
        	if(parentDirectory.exists())
        	{
            	try {
            		// Create the file if the file doesnt exist
    				csvFile.createNewFile();
    				
    		        try {
    		            FileWriter csvWriter = new FileWriter(PATH, false);
    		            int i = 0;
    		            while (i < lines.length) {
    		                csvWriter.append(lines[i]);
    		                csvWriter.flush();
    		                i++;
    		            }
    		            csvWriter.close();

    		        } catch (IOException e) {
    		            e.printStackTrace();
    		            System.out.println("Something went wrong. Can't update CSV files.");
    		        }
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}
        	// If the parent directory and the csv file dont exist
        	else
        	{
        		// Creates the parent directory
        		parentDirectory.mkdir();
        		
            	try {
            		// Create the file if the file doesnt exist
    				csvFile.createNewFile();
    				
    		        try {
    		            FileWriter csvWriter = new FileWriter(PATH, false);
    		            int i = 0;
    		            while (i < lines.length) {
    		                csvWriter.append(lines[i]);
    		                csvWriter.flush();
    		                i++;
    		            }
    		            csvWriter.close();

    		        } catch (IOException e) {
    		            e.printStackTrace();
    		            System.out.println("Something went wrong. Can't update CSV files.");
    		        }
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
        	}

        	
        }
    }
    
    /***
     * Private Methods
     */
    
    private void createDirectory()
    {
    	
    }
}
