package com.gm.student_management;

public class Diem {
    private Sinhvien sv;
    private String mon;
    private double diem;

    public Diem(Sinhvien sv, String mon, double diem) {
        this.sv = sv;
        this.mon = mon;
        this.diem = diem;
    }
    public Sinhvien getSv() {return sv;}

    public String getMon() {return mon;}
    public void setMon(String m) {mon = m;}

    public double getDiem() {return diem;}
    public void setDiem(double d) {diem = d;}

}
