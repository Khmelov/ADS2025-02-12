package by.it.group451002.koltsov.lesson15;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class fileInfoB implements Comparator<fileInfo> {
    String filePath;
    String fileText;
    Integer size;
    fileInfoB(String filePath) {
        this.filePath = filePath;
    }

    static void getListOfFilePaths(File folder, List<fileInfo> list) {
        List<fileInfo> fileInfos = new ArrayList<>();

        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getListOfFilePaths(fileEntry, list);
            } else {
                int dotIndex = fileEntry.getPath().lastIndexOf(".");
                if (dotIndex >= 0) {
                    if (!fileEntry.getPath().substring(dotIndex + 1).equals("java"))
                        continue;
                }
                File file = new File(fileEntry.getPath());
                String relative = new File(System.getProperty("user.dir") + File.separator + "src").toURI().relativize(file.toURI()).getPath().replace("/", "\\");

                fileInfo fi = new fileInfo(relative);
                try {
                    StringBuilder fileText = new StringBuilder();
                    Scanner scan = new Scanner(file);
                    boolean isTestFile = false;
                    while (scan.hasNextLine()) {
                        String line = scan.nextLine();

                        if (!line.isEmpty() && !line.contains("package") && !line.contains("import")) {
                            fileText.append(line);
                            fileText.append("\n");
                        }
                        if (line.contains("@Test") || line.contains("org.junit.Test")) {
                            isTestFile = true;
                            break;
                        }
                    }

                    if (!isTestFile) {
                        fileText = new StringBuilder(fileText.toString().replaceAll("[^:]//.*|/\\\\*((?!=*/)(?s:.))+\\\\*/", ""));

                        for (int i = 0; i < fileText.length(); i++)
                            if (fileText.charAt(i) >= 33)
                                break;
                            else
                            {
                                fileText.deleteCharAt(i);
                                i--;
                            }

                        for (int i = fileText.length() - 1; i >= 0; i--)
                            if (fileText.charAt(i) >= 33)
                                break;
                            else
                                fileText.deleteCharAt(i);

                        fi.fileText = fileText.toString();
                        fi.size = 2 * fileText.length();
                        list.add(fi);
                    }
                }
                catch(FileNotFoundException e)  {
                    System.out.print("Надеюсь это сообщение никогда не выведется");
                }
            }
        }
    }

    @Override
    public int compare(fileInfo o1, fileInfo o2) {
        if (o1.fileText.length() > o2.fileText.length())
            return 1;
        else if (o1.fileText.length() < o2.fileText.length())
            return -1;
        else
            return o1.fileText.compareTo(o2.fileText);
    }
}
