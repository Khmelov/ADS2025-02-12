package by.it.group451002.koltsov.lesson15;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SourceScannerB {
    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src";
        File srcFolder = new File(src);
        List<fileInfo> fileInfoList = new ArrayList<>();
        fileInfoB.getListOfFilePaths(srcFolder, fileInfoList);
        fileInfoList.sort(new fileInfo(""));
        for (fileInfo fi : fileInfoList)
            System.out.println(fi.filePath + " " + fi.size);
    }
}
