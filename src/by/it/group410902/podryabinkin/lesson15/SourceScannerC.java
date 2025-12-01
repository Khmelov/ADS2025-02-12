package by.it.group410902.podryabinkin.lesson15;

import by.it.group410902.podryabinkin.lesson07.C_EditDist;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;
import java.nio.ByteBuffer;


public class SourceScannerC {



    public static class Ffiles {
        public int size;
        public String path;
        public Boolean hasBeenChecked = false;

        public Ffiles(int size, String path, String Content) {
            this.size = size;
            this.path = path;
            FullContent = Content;

        }
        public String FullContent = "";
    }


    public static void main(String[] args) throws IOException {

        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        Path srcPath = Paths.get(src);

        List<Ffiles> results = new ArrayList<>();

        Files.walk(srcPath).filter(p -> p.toString().endsWith(".java")).forEach(path -> {
            String text = safeReadFile(path);

            if (text == null) return;
            if (text.contains("@Test") || text.contains("org.junit.Test")) return;

            String processed = removeComments(text);

            processed = processText(processed);

            Path relative = srcPath.relativize(path);
            int size = processed.getBytes(StandardCharsets.UTF_8).length;

            results.add(new Ffiles(size, relative.toString(), processed));
        });


        results.sort((a, b) -> {
            if (a.FullContent.length() != b.FullContent.length()) return Integer.compare(a.FullContent.length(), b.FullContent.length());
            return a.path.compareTo(b.path);
        });
        int total_count = 0;
        StringBuilder out = new StringBuilder();

        long startTime;
        startTime = System.currentTimeMillis();


        for(int i = 0; i < results.size(); i++){
            if(System.currentTimeMillis() - startTime > 20000){
                System.out.println(out);
                return;
            }
            int count = 0;
            int size1 = results.get(i).FullContent.length();
            String str1 = results.get(i).FullContent;

            if(str1 == null) continue;
            if(str1 == "") continue;
            if(str1.length() < 11) continue;
            if(results.get(i).hasBeenChecked) continue;
            for(int k = i + 1; k < results.size(); k++){
                if(results.get(k).hasBeenChecked) continue;
                if(Math.abs(results.get(k).FullContent.length() - size1) <=  10){
                    String str2 = results.get(k).FullContent;
                    if(str2 == null) continue;
                    if(str2.length() < 8) continue;
                    C_EditDist CED = new C_EditDist();
                    //int dist = levenshtein(str1, str2) ;
                    int dist = CED.getDistanceEdintingCount(str1, str2);

                    if( dist <= 10){
                        //if(count == 0) System.out.println("\nИсходный: " + results.get(i).path);
                        total_count++;
                        if(count == 0){
                            out.append("\n\nИсходный: " + results.get(i).path + "\t");
                            //out.append( results.get(i).FullContent + "\n");
                        }
                        //System.out.println( "Копия " + results.get(k).path + "\t" + dist);
                        out.append( "\nКопия " + results.get(k).path + "\t" + dist + "||" + total_count + "||" + str2.length() + "\t");
                        //out.append(results.get(k).FullContent + "\n");

                        count++;
                        results.get(k).hasBeenChecked = true;


                    }
                }
                else break;
            }

        }
        if(!out.isEmpty()) System.out.println(out);
    }

    // MalformedInputException не волнует, у нас всё в битах
    private static String safeReadFile(Path path) {
        try {
            byte[] bytes = Files.readAllBytes(path);

            // 1) Сначала пытаемся строго UTF-8
            try {
                CharsetDecoder dec = StandardCharsets.UTF_8
                        .newDecoder()
                        .onMalformedInput(CodingErrorAction.REPORT)
                        .onUnmappableCharacter(CodingErrorAction.REPORT);

                return dec.decode(ByteBuffer.wrap(bytes)).toString();
            }
            catch (CharacterCodingException utf8err) {
                // 2) Если UTF-8 не подходит — пробуем CP1251
                try {
                    CharsetDecoder dec1251 = Charset.forName("windows-1251")
                            .newDecoder()
                            .onMalformedInput(CodingErrorAction.REPORT)
                            .onUnmappableCharacter(CodingErrorAction.REPORT);

                    return dec1251.decode(ByteBuffer.wrap(bytes)).toString();
                }
                catch (CharacterCodingException bad1251) {
                    // 3) Файл повреждён или экзотическая кодировка
                    return null;
                }
            }

        } catch (IOException e) {
            return null;
        }
    }


    private static String processText(String text) {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new StringReader(text))) {
            String line;
            while ((line = br.readLine()) != null) {

                String trimmed = line.trim();

                if (trimmed.startsWith("package ")) continue;
                if (trimmed.startsWith("import ")) continue;


                //if (trimmed.isEmpty()) continue;

                sb.append(line).append("\n");
            }
        } catch (IOException ignored) {}

        // trim по символам < 33
        return OnlySpaces(sb.toString());
    }

    public static String OnlySpaces(String s) {
        StringBuilder out = new StringBuilder();
        int start = 0;
        int end = s.length() - 1;

        while (start <= end) {
            char c = s.charAt(start);

            if (c < 33) {
                // Пропускаем все подряд идущие символы <33
                while (start <= end && s.charAt(start) < 33) {
                    start++;
                }
                // Добавляем один пробел вместо всех пропущенных символов
                out.append(' ');
            } else {
                out.append(c);
                start++;
            }
        }

        return out.toString().trim();
    }

    private static String removeComments(String s) {
        StringBuilder out = new StringBuilder();
        int n = s.length();
        int i = 0;

        boolean inString = false;
        boolean inChar = false;
        boolean inBlockComment = false;
        boolean inLineComment = false;

        while (i < n) {
            char c = s.charAt(i);
            //ОСОБОЕ
            if (inString) {
                out.append(c);
                if (c == '"' && s.charAt(i - 1) != '\\') {
                    inString = false;
                }
                i++;
                continue;
            }

            if (inChar) {
                out.append(c);
                if (c == '\'' && s.charAt(i - 1) != '\\') {
                    inChar = false;
                }
                i++;
                continue;
            }

            if (inLineComment) {
                if (c == '\n') {
                    out.append('\n');
                    inLineComment = false;
                }
                i++;
                continue;
            }
            //НАЧАЛА
            if (inBlockComment) {
                if (c == '*' && i + 1 < n && s.charAt(i + 1) == '/') {
                    inBlockComment = false;
                    i += 2;
                } else {
                    i++;
                }
                continue;
            }

            if (c == '"') {
                inString = true;
                out.append(c);
                i++;
                continue;
            }
            if (c == '\'') {
                inChar = true;
                out.append(c);
                i++;
                continue;
            }

            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '/') {
                inLineComment = true;
                i += 2;
                continue;
            }
            if (c == '/' && i + 1 < n && s.charAt(i + 1) == '*') {
                inBlockComment = true;
                i += 2;
                continue;
            }

            out.append(c);
            i++;
        }

        return out.toString();
    }



    /** Возвращает точное расстояние Левенштейна между a и b. */
    public static int levenshtein(String a, String b) {
        if (a == null) a = "";
        if (b == null) b = "";
        int n = a.length();
        int m = b.length();
        if (n == 0) return m;
        if (m == 0) return n;

        // гарантим, что m <= n чтобы использовать меньше памяти
        if (m > n) {
            String tmp = a; a = b; b = tmp;
            int t = n; n = m; m = t;
        }

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];

        for (int j = 0; j <= m; j++) prev[j] = j;

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            char ca = a.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                int cost = (ca == b.charAt(j - 1)) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            // swap
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }




}
