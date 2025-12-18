package by.it.group451002.koltsov.lesson15;

import javax.xml.crypto.dsig.spec.XSLTTransformParameterSpec;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SourceScannerC {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src";
        File srcFolder = new File(src);
        List<fileInfo> fileInfoList = new ArrayList<>();
        fileInfoC.getListOfFilePaths(srcFolder, fileInfoList);
        fileInfoList.sort(new fileInfo(""));

        // На этом этапе у нас есть список элементов, содержащих относительный папке src путь, их текст и размер
        int count = 0;

        int[][] matrix = new int[fileInfoC.count][fileInfoC.count];

        for (int i = 0; i < fileInfoList.size(); i++) {
            for (int j = i + 1; j < fileInfoList.size(); j++) {
                String text1 = fileInfoList.get(i).fileText;
                String text2 = fileInfoList.get(j).fileText;
                if (Math.abs(fileInfoList.get(i).fileText.length() - fileInfoList.get(j).fileText.length()) > 10 || text1.isEmpty() || text2.isEmpty())
                    continue;

                if (matrix[fileInfoList.get(i).ID - 1][fileInfoList.get(j).ID - 1] != 0)
                    continue;

                if (C_EditDist.getDistanceEdinting(text1, text2) <= 10) {
                    count++;
                    System.out.println(fileInfoList.get(i).filePath + " Копия: " + fileInfoList.get(j).filePath + " Общее количество копий: " + count);
                    matrix[fileInfoList.get(i).ID - 1][fileInfoList.get(j).ID - 1] = 2;
                    matrix[fileInfoList.get(j).ID - 1][fileInfoList.get(i).ID - 1] = 2;
                }
                else
                {
                    matrix[fileInfoList.get(i).ID - 1][fileInfoList.get(j).ID - 1] = 1;
                    matrix[fileInfoList.get(j).ID - 1][fileInfoList.get(i).ID - 1] = 1;
                }
            }
        }
    }
}
