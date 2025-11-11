package com.gm.student_management;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.*;

public class DiemCsv {
    public static void saveCsv(String file, List<Diem> list) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(Diem d : list){
            bw.write(d.getSv().getMasv() + "," + d.getMon() + "," +d.getDiem());
            bw.newLine();
        }
        bw.close();
    }
    public static List<Diem> loadCsv(String file) throws IOException {
        List<Diem> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            Sinhvien sv = findSVbyMasv(data[0]);
            if(sv != null){
                Diem d = new Diem(sv, data[1], Double.parseDouble(data[2]));
                list.add(d);
            }
        }
        br.close();
        return list;
    }
    private static Sinhvien findSVbyMasv(String masv){
        for(Sinhvien sv: Mainapp.getSinhvien()){
            if(sv.getMasv().equals(masv)) {return sv;}
        }
        return null;
    }
}
