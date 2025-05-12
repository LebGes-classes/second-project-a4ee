package ru.itis.project.company.repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    public static List<String[]> readFile(String path){
        List<String[]> data = new ArrayList<>();
        //InputStream is = FileStorage.class.getClassLoader().getResourceAsStream(path);
        File file = new File(path);
        try
            (BufferedReader br = new BufferedReader(new FileReader(file))){
                String line;
                while ((line = br.readLine()) !=null){
                    data.add(line.split(";"));
                }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return data;
    }
    public static void writeFile(String path, List<String> lines){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))){
            for (String line : lines){
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
