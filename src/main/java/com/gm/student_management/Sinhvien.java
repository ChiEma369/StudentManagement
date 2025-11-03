package com.gm.student_management;

import javafx.beans.property.SimpleStringProperty;

public class Sinhvien {
        private SimpleStringProperty masv, ten, birth, sex, lop;

        public Sinhvien(String masv, String ten, String birth, String sex, String lop) {
            this.masv = new SimpleStringProperty(masv);
            this.ten = new SimpleStringProperty(ten);
            this.birth = new SimpleStringProperty(birth);
            this.sex = new SimpleStringProperty(sex);
            this.lop = new SimpleStringProperty(lop);
        }

        public String getMasv() { return masv.get(); }
        public void setMasv(String m) {masv.set(m);}
        public SimpleStringProperty masvProperty() { return masv; }

        public String getTen() { return ten.get(); }
        public void setTen(String t) {ten.set(t);}
        public SimpleStringProperty tenProperty() { return ten; }

        public String getBirth() { return birth.get(); }
        public void setBirth(String b) {birth.set(b);}
        public SimpleStringProperty birthProperty() { return birth; }

        public String getSex() { return sex.get(); }
        public void setSex(String s) {sex.set(s);}
        public SimpleStringProperty sexProper() {return sex; }

        public String getLop() { return lop.get(); }
        public void setLop(String l) {lop.set(l);}
        public SimpleStringProperty lopProper() {return lop; }
    }


