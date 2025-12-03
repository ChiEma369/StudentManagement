package com.gm.student_management;

public class Diem {
    private Sinhvien sv;
    private String mon;
    private String namhoc;
    private double diem;

    public Diem(Sinhvien sv, String mon, String lop, double diem) {
        this.sv = sv;
        this.mon = mon;
        this.namhoc = namhoc;
        this.diem = diem;
    }
    public Sinhvien getSv() {return sv;}
    public void setSv(Sinhvien sv) {this.sv = sv;}

    public String getMon() {return mon;}
    public void setMon(String m) {mon = m;}

    public String getNam() {return namhoc;}
    public void setNam(String l) {namhoc = l;}

    public double getDiem() {return diem;}
    public void setDiem(double d) {diem = d;}

    public double getGPA(){
        if(diem >= 8.5) return 4.0;
        else if(diem >= 8.0) return 3.5;
        else if(diem >= 7.0) return 3.0;
        else if(diem >= 6.5) return 2.5;
        else if(diem >= 5.5) return 2;
        else if(diem >= 5) return 1.5;
        else return 1.0;
    }
}
