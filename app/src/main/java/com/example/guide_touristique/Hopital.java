package com.example.guide_touristique;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

public class Hopital  {
    private int hopitalid;
    private String hopitaladress;
    private double hopitallatitude;
    private double hopitallongitude;
    private String hopitalname;
    private float hopitalrating;
    private String hopitaltel;
    private String hopitaltype;
    private String imageurl;
    private int idville;
    private double distance;
    private String duree;
    public Hopital() {
    }



    public Hopital(int hopitalid, String hopitaladress, double hopitallatitude, double hopitallongitude, String hopitalname, float hopitalrating, String hopitaltel, String hopitaltype, String imageurl, int idville) {
        this.hopitalid = hopitalid;
        this.hopitaladress = hopitaladress;
        this.hopitallatitude = hopitallatitude;
        this.hopitallongitude = hopitallongitude;
        this.hopitalname = hopitalname;
        this.hopitalrating = hopitalrating;
        this.hopitaltel = hopitaltel;
        this.hopitaltype = hopitaltype;
        this.imageurl = imageurl;
        this.idville = idville;
        this.distance=0;
        this.duree="";
    }




    public static Comparator<Hopital> distanceComparator = new Comparator<Hopital>() {

        public int compare(Hopital s1, Hopital s2) {
            double distance1 = s1.getDistance();
            double distance2= s2.getDistance();

            //ascending order
            return (int) (distance1-distance2);
            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};

    public int getHopitalid() {
        return hopitalid;
    }

    public void setHopitalid(int hopitalid) {
        this.hopitalid = hopitalid;
    }

    public String getHopitaladress() {
        return hopitaladress;
    }

    public void setHopitaladress(String hopitaladress) {
        this.hopitaladress = hopitaladress;
    }

    public double getHopitallatitude() {
        return hopitallatitude;
    }

    public void setHopitallatitude(double hopitallatitude) {
        this.hopitallatitude = hopitallatitude;
    }

    public double getHopitallongitude() {
        return hopitallongitude;
    }

    public void setHopitallongitude(double hopitallongitude) {
        this.hopitallongitude = hopitallongitude;
    }

    public String getHopitalname() {
        return hopitalname;
    }

    public void setHopitalname(String hopitalname) {
        this.hopitalname = hopitalname;
    }

    public float getHopitalrating() {
        return hopitalrating;
    }

    public void setHopitalrating(float hopitalrating) {
        this.hopitalrating = hopitalrating;
    }

    public String getHopitaltel() {
        return hopitaltel;
    }

    public void setHopitaltel(String hopitaltel) {
        this.hopitaltel = hopitaltel;
    }

    public String getHopitaltype() {
        return hopitaltype;
    }

    public void setHopitaltype(String hopitaltype) {
        this.hopitaltype = hopitaltype;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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

