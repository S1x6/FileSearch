import search.FileSearch;
import search.FileSubstringSearch;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        if (args.length < 1 || args[0].equals("-h")) {
            printUsage();
            return;
        }
        if (args.length < 3) {
            printUsage();
            return;
        }

        String lookUpString = args[1];
        File file = new File(args[2]);
        if (!file.exists()) {
            System.out.println("File not found: " + file.getAbsolutePath());
            printUsage();
        }
        if (args[0].equals("--data")) {
            startDataSearch(lookUpString, file);
        } else if (args[0].equals("--name")) {
            startNameSearch(lookUpString, file);
        }
    }

    private static void startNameSearch(String find, File file) {
        FileSearch fileSearch = new FileSearch(file);
        fileSearch.find(find);
    }

    private static void startDataSearch(String find, File file) {
        FileSubstringSearch fileSearch = new FileSubstringSearch(file);
        fileSearch.find(find);
    }

    private static void printUsage() {
        System.out.println("usage:\njava -jar ./search.jar --name name_to_find dir_name\n\tor\njava -jar ./search.jar --data substring file_name");
    }
}
