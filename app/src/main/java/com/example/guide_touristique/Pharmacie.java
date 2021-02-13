package com.example.guide_touristique;

import java.util.Comparator;

public class Pharmacie {
    private int pharmacieid;
    private String imageurl;
    private String pharmacieadress;
    private double pharmacielatitude;
    private double pharmacielongitude;
    private String duree;

    public int getPharmacieid() {
        return pharmacieid;
    }

    public Pharmacie() {
    }

    public Pharmacie(int pharmacieid, String imageurl, String pharmacieadress, double pharmacielatitude, double pharmacielongitude, String pharmaciename, String pharmacietel, int idville) {
        this.pharmacieid = pharmacieid;
        this.imageurl = imageurl;
        this.pharmacieadress = pharmacieadress;
        this.pharmacielatitude = pharmacielatitude;
        this.pharmacielongitude = pharmacielongitude;
        this.pharmaciename = pharmaciename;
        this.pharmacietel = pharmacietel;
        this.idville = idville;
        this.distance=0;
        this.duree="";
    }

    public void setPharmacieid(int pharmacieid) {
        this.pharmacieid = pharmacieid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPharmacieadress() {
        return pharmacieadress;
    }

    public void setPharmacieadress(String pharmacieadress) {
        this.pharmacieadress = pharmacieadress;
    }

    public double getPharmacielatitude() {
        return pharmacielatitude;
    }

    public void setPharmacielatitude(double pharmacielatitude) {
        this.pharmacielatitude = pharmacielatitude;
    }

    public double getPharmacielongitude() {
        return pharmacielongitude;
    }

    public void setPharmacielongitude(double pharmacielongitude) {
        this.pharmacielongitude = pharmacielongitude;
    }

    public String getPharmaciename() {
        return pharmaciename;
    }

    public void setPharmaciename(String pharmaciename) {
        this.pharmaciename = pharmaciename;
    }

    public String getPharmacietel() {
        return pharmacietel;
    }

    public void setPharmacietel(String pharmacietel) {
        this.pharmacietel = pharmacietel;
    }

    public int getIdville() {
        return idville;
    }

    public void setIdville(int idville) {
        this.idville = idville;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    private String pharmaciename;
    private String pharmacietel;
    private int idville;
    private double distance;
    public static Comparator<? super Pharmacie> distanceComparator= new Comparator<Pharmacie>() {

        public int compare(Pharmacie s1, Pharmacie s2) {
            double distance1 = s1.getDistance();
            double distance2= s2.getDistance();

            //ascending order
            return (int) (distance1-distance2);
            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };


    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}
