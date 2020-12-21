
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/***
 * 
 * Cette classe calcule les metriques voulues du code Java associe
 * @author William Bach (20127144) && Daniel El-Masri (20096261)  
 *
 */
public class CodeAnalyzer 
{
    CSVManager CSVproducer; //produit les files .csv 
    FileManager manager; //manager qui gere l'extraction des classes
    String path; //chemin absolu du folder contenant les files a analyser
    String[][] classes; //2d array avec chaque ligne de chaque classe
    String[] classNames; //noms des classes associees 
    String[] absolutePaths; //chemins absolus 

    /**
     * Constructeur. Enregistre les attributs
     * @param path: chemin absolu du folder avec les files
     */
    public CodeAnalyzer(String path)                                            
    {
        this.CSVproducer = new CSVManager();
        this.manager = new FileManager(path);
        this.path = path;
        this.classes = getClasses(path); getMethodes();                          
        this.classNames = getClassNames(path);
        this.absolutePaths = getAbsolutePaths();

    }

    /**
     * Recupere les lignes de chaque classe grace a FileManager
     * @param path
     * @return 2d array avec les lignes de chaque classe
     */
    public String[][] getClasses(String path) 
    {
        return this.manager.getClassesArray();
    }

    /**
     * Recupere les noms de chaque classe grace a FileManager
     * @param path
     * @return array de Strings avec les noms de chaque classe
     */
    public String[] getClassNames(String path) 
    {
        return this.manager.getClassNamesArray();
    }

    /**
     * Cette fonction annote la position des methodes d'une classe directement
     * dans l'attribut 'classes'
     */
    public void getMethodes()
    {
        //on parcourt l'attribut this.classes
        for (var i=0; i < this.classes.length; i++){
            for (var j=0; j < this.classes[i].length; j++) 
            {
                //si on trouve une déclaration de méthode dans la ligne lue
                if((this.classes[i][j].contains("void")      ||
                    this.classes[i][j].contains("public")    ||
                    this.classes[i][j].contains("protected") ||
                    this.classes[i][j].contains("private")) 
                    &&
                   (this.classes[i][j].endsWith(") {")       ||
                    this.classes[i][j].endsWith("){")        ||
                    this.classes[i][j].endsWith(")"))) 
                {
                    //on sauvegarde l'index de la declaration
                    int idx = j;

                    //on retrouve le nom de la methode
                    String[] declarationTokens = this.classes[i][j].split(" ");    
                    String name = "";
                    for (String token : declarationTokens) 
                        //le nom de la methode est colle a la parenthese gauche
                        if (token.contains("(")) name = token.trim().substring(0, token.indexOf("("));

                    //on retrouve les types des arguments de la methode
                    int firstParIdx = this.classes[i][j].indexOf("(");
                    int sndParIdx   = this.classes[i][j].indexOf(")");
                    //split le contenu entre parentheses de la declaration
                    String[] args = this.classes[i][j].substring(firstParIdx+1, sndParIdx).split(",");
                    for (String arg_i : args) {
                        //isole les arguments de leur type
                        String[] typeArg_i = arg_i.split(" ");
                        if (!(typeArg_i[0].isBlank()))
                            name += "_" + typeArg_i[0].trim();
                    }
                                          
                    //on ajoute dans l'array de la classe un identificateur (le nom de la methode)
                    //no javadoc
                    if (this.classes[i][j-1].isBlank()) 
                        this.classes[i][j-1] = "METHOD= " + name;
                    //w/ javadoc
                    else {
                        while (!(this.classes[i][j-1].isBlank())) 
                            j--;
                        this.classes[i][j-1] = "METHOD= " + name;
                    }

                    j = idx+1;
                }
            }
        }
    }

    public String[][] getClassMethods(String[] classe) 
    {
        ArrayList<String[]> methods = new ArrayList<>();

        for (var i = 0; i < classe.length; i++) {
            //methode trouvee
            if (classe[i].contains("METHOD=")) {
                int startIdx = i+1;
                while (!(i >= classe.length -1) && !(classe[i+1].contains("METHOD="))) 
                    ++i;
                //extraction des lignes de la methode
                String[] methodLines = new String[i - startIdx];
                methodLines = Arrays.copyOfRange(classe, startIdx, i+1);
                methods.add(methodLines);
            }
        }

        String[][] result = new String[methods.size()][];
        for (var i = 0; i < methods.size(); i++) {
            result[i] = methods.get(i);
        }
        return result;
    }

    /**
     * Cherche les chemins absolus de chaque .java file dans le folder 
     * @return array de Strings
     */
    public String[] getAbsolutePaths() 
    {
        return this.manager.getAbsolutePaths();
    }

    /**
     * Produit les files CSV pour sauvegarder la data selon le type de data produit
     */
    public void produceData()
    {

        String[] results = this.produceClassesData();
        this.produceCSV(results, true);

        String[] lines = this.produceMethodsData();
        this.produceCSV(lines, false);
    }
    
    /***
     * This method produces or modifies a CSV file to add computed information into
     * @param lines, the array of information computed
     * @param doesFileExist, the boolean to inform csv manager class which csv file to update
     */
    public void produceCSV(String[] lines, boolean isClass)
    {
    	this.CSVproducer.updateCSVFile(lines, isClass);
    }
    
    /***
     * This method computes the LOC, CLOC and DC information for all the 
     * classes in the specified directory and produces report.
     * @return an array of strings containing the computed information
     */
    public String[] produceClassesData()
    {
        String[] results = new String[this.classes.length+1];
        results[0] = "chemin ," + "class ," + "classe_LOC ," + "classe_CLOC ," + "classe_DC ," + "WMC ," + "classe_BC \n";
        for (var i=0; i < this.classes.length; i++) {
            results[i+1] = this.absolutePaths[i] + "," + this.classNames[i] + "," +
                classe_LOC(this.classes[i]) + "," + classe_CLOC(this.classes[i]) +
                "," + classe_DC(this.classes[i]) + "," + WMC(this.classes[i]) + "," +
                classe_BC(this.classes[i]) + "\n";
        }
        return results;
    }
    
    /***
     * This method computes the LOC, CLOC and DC information for all the methods in the specified directory and produces report.
     * @return lines, an array of strings containing the computed information
     */
    public String[] produceMethodsData()
    {
        //stocke les lignes à produire
        ArrayList<String> results = new ArrayList<String>();
        // Add the title line
        results.add("chemin ," + "classe ," + "methode ," + "methode_LOC ," + "methode_CLOC ," + "methode_DC ," + "CC ," + "methode_BC ," + "\n");
        for (var i=0; i < this.classes.length; i++) {
            for (var j=0; j < this.classes[i].length; j++) 
            {
                //methode trouvee
                if (this.classes[i][j].contains("METHOD=")) {
                    int startIdx = j+1;
                    while (!(j >= this.classes[i].length -1) && 
                        !(this.classes[i][j+1].contains("METHOD="))) 
                        ++j;
                    //extraction des lignes de la methode
                    String[] methodLines = new String[j - startIdx];
                    methodLines = Arrays.copyOfRange(this.classes[i], startIdx, j+1);
                    //production du rapport 
                    results.add(this.absolutePaths[i] + "," + this.classNames[i] +
                        "," + this.classes[i][startIdx-1].substring(8) + 
                        "," + methode_LOC(methodLines) + "," + methode_CLOC(methodLines) +
                        "," + methode_DC(methodLines) + "," + CC(methodLines) + "," + 
                        methode_BC(methodLines) + "\n");
                    
                }
            }
        }
        //conversion de l'ArrayList des resultats en array de Strings
        String[] lines = new String[results.size()];
        for (var i=0; i < results.size(); i++) {
            lines[i] = results.get(i);
        }

        return lines;
    }




    /**
     * Calcule le nombre de lignes de code non-vides dans une classe
     * @param allLines les lignes de la classe
     * @return counter, the number of non-empty lines of codes in class
     */
    public int classe_LOC(String[] allLines)
    {
        int counter = 0;

        for (var i=0; i < allLines.length; i++) 
        {
            if (allLines[i].isBlank() || allLines[i].contains("METHOD=")) 
                continue;
            else ++counter; 
        }

        return counter;
    }

    /**
     * Calcule le nombre de lignes de codes non-vides dans une methode
     * @param allLines les lignes de la methode
     * @return counter, the number of non-empty lines of codes in method
     */
    public int methode_LOC(String[] allLines)
    {
        return classe_LOC(allLines);
    }

    /**
     * Calcule le nombre de lignes-commentaires dans une classe
     * @param allLines
     * @return counter, the number of lines of with comments codes in class
     */
    public int classe_CLOC(String[] allLines)
    {
        int counter = 0;

        for (var i=0; i < allLines.length; i++) 
        {
            if (allLines[i].contains("/*")) {
                while (!(allLines[i].contains("*/")) && (i < allLines.length -1)) {
                    ++counter;
                    ++i;
                }
                if (allLines[i].contains("*/")) ++counter;
            } 
            else if (allLines[i].contains("//")) {
                ++counter;
            }
        }

        return counter;
    }

    /**
     * Calcule le nombre de lignes-commentaires dans une methode
     * @param allLines
     * @return counter, the number of lines of with comments codes in method
     */
    public int methode_CLOC(String[] allLines)
    {
        return classe_CLOC(allLines);
    }

    /**
     * Calcule la densite de commentaires d'une classe
     * @param alLines
     * @return
     */
    public float classe_DC(String[] allLines)
    {
        int cloc = classe_CLOC(allLines);
        int loc  = classe_LOC(allLines);

        if (loc == 0) return 0;
        return ((float) cloc / loc);
    }

    /**
     * Calcule la densite de commentaires dans une methode
     * @param alLines
     * @return float car valeur non-entiere (pourcentage)
     */
    public float methode_DC(String[] allLines)
    {
        int cloc = methode_CLOC(allLines);
        int loc  = methode_LOC(allLines);

        if (loc == 0) return 0;
        return ((float) cloc / loc);
    }

    /**
     * Mesure la complexite cyclomatique de McCabe d'une methode
     * @param method la methode a analyser
     * @return sa complexite (int)
     */
    public int CC(String[] method) 
    {
        int predicats_counter = 0;

        String line = "";
        for (var i=0; i < method.length; i++)
        {
            line = method[i];
            if (line.contains("while") || line.contains("if") || line.contains("else") ||
                line.contains("for") || (line.contains("case ") && line.contains(":"))) 
            {
                predicats_counter++;
            }
        }

        return predicats_counter;
    }

    /**
     * Mesure la somme des complexites des methodes d'une classe
     * @param classe la classe a analyser
     * @return la somme des complexites de ses methodes (int)
     */
    public int WMC(String[] classe) 
    {
        String[][] methodsArray = getClassMethods(classe);
        int complexityCounter = 0;

        for (var i =0; i < methodsArray.length; i++)
            complexityCounter += CC(methodsArray[i]);

        return complexityCounter;
    }

    /**
     * Mesure le degre selon lequel une classe est bien commentee
     * @param classe
     * @return
     */
    public float classe_BC(String[] classe)
    {
        float classe_dc  = classe_DC(classe);
        int   classe_wmc = WMC(classe);

        if (classe_wmc == 0) return 0;
        return ((float) classe_dc / classe_wmc);
    }

    /**
     * Mesure le degre selon lequel une methode est bien commentee
     * @param method
     * @return
     */
    public float methode_BC(String[] method) 
    {
        float methode_dc = methode_DC(method);
        int   methode_cc = CC(method);

        if (methode_cc == 0) return 0;
        return ((float) methode_dc / methode_cc);
    }



    /***
     * Main method
     * @param args, array of argument containing the path of the files to analyze
     */
    public static void main(String[] args) 
    {
        try {
			String folderToAnalyze = args[0];
            CodeAnalyzer analyzer = new CodeAnalyzer(folderToAnalyze);
            analyzer.produceData();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }

}
