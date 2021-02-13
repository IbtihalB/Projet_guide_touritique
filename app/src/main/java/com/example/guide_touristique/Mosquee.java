package com.example.guide_touristique;

import java.util.Comparator;

public class Mosquee {
    public static Comparator<? super Mosquee> distanceComparator= new Comparator<Mosquee>() {

        public int compare(Mosquee s1, Mosquee s2) {
            double distance1 = s1.getDistance();
            double distance2= s2.getDistance();

            //ascending order
            return (int) (distance1-distance2);
            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };;
    private int mosqueeid;
    private String imageurl;
    private String mosqueeadress;
    private double mosqueelatitude;

    public Mosquee() {
    }

    private double mosqueelongitude;
    private String mosqueename;
    private int idville;
    private double distance;
    private String duree;


    public Mosquee(int mosqueeid, String imageurl, String mosqueeadress, double mosqueelatitude, double mosqueelongitude, String mosqueename, int idville) {
        this.mosqueeid = mosqueeid;
        this.imageurl = imageurl;
        this.mosqueeadress = mosqueeadress;
        this.mosqueelatitude = mosqueelatitude;
        this.mosqueelongitude = mosqueelongitude;
        this.mosqueename = mosqueename;
        this.idville = idville;
        this.distance=0;
        this.duree="";
    }

    public int getMosqueeid() {
        return mosqueeid;
    }

    public void setMosqueeid(int mosqueeid) {
        this.mosqueeid = mosqueeid;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getMosqueeadress() {
        return mosqueeadress;
    }

    public void setMosqueeadress(String mosqueeadress) {
        this.mosqueeadress = mosqueeadress;
    }

    public double getMosqueelatitude() {
        return mosqueelatitude;
    }

    public void setMosqueelatitude(double mosqueelatitude) {
        this.mosqueelatitude = mosqueelatitude;
    }

    public double getMosqueelongitude() {
        return mosqueelongitude;
    }

    public void setMosqueelongitude(double mosqueelongitude) {
        this.mosqueelongitude = mosqueelongitude;
    }

    public String getMosqueename() {
        return mosqueename;
    }

    public void setMosqueename(String mosqueename) {
        this.mosqueename = mosqueename;
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

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }
}




