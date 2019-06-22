package com.smookcreative.icare.Adapters.ILearn;

import com.smookcreative.icare.Adapters.CategoryObject;
import com.smookcreative.icare.Adapters.PlacesAdapter;
import com.smookcreative.icare.Adapters.PlacesObject;
import com.smookcreative.icare.ISearch.BookingObject;
import com.smookcreative.icare.MainActivity;

import java.util.ArrayList;

public class LoadAllCenters {

    public   ArrayList<ArrayList<BookingObject>> ALL_ISEARCH_DATAS =new ArrayList<>();
    private ArrayList<PlacesObject> centers=new ArrayList<>();
    private ArrayList<BookingObject> pharm = MainActivity.bookingList;
    private int count_type;


    public LoadAllCenters() {
        setCenters();
        setPharmacies();
        setPersonnel();

        setArraylist();
    }

    private void setCenters(){
        centers.add(new PlacesObject("Hopital Central","Messa", "Camp Sic",193, "201", "101", "https://www.smookcreative.com/I_Care/Booking_Pictures/1/1/index.jpg"));
        centers.add(new PlacesObject("Clinique Dilma","Omnisport","Face école publique",208, "31", "153", "https://www.smookcreative.com/I_Care/AppPictures/hospital.png"));
        centers.add(new PlacesObject("Centre Pasteur du Cameroun","Centre Ville","Messa",110, "93", "119", "https://www.smookcreative.com/I_Care/AppPictures/hospital.png"));
        centers.add(new PlacesObject("Hôpital Général","Ngousso","Ngousso",30, "11", "13", "https://www.smookcreative.com/I_Care/Booking_Pictures/1/2/index.jpg"));
        centers.add(new PlacesObject("Polyclinique SENDE","Essos","Titi garare",10, "221", "411", "https://www.smookcreative.com/I_Care/AppPictures/hospital.png"));
    }


    private void setPersonnel(){
       // centers.add(new PlacesObject("Zang Belinga Alphonse","Cardiologue",0,"https://www.smookcreative.com/I_Care/AppPictures/doc.jpg"));

    }

    private void setPharmacies(){

    }

    private void setArraylist(){
        ALL_ISEARCH_DATAS.add(pharm);
        //ALL_ISEARCH_DATAS.add(centers);
    }

    public ArrayList<ArrayList<BookingObject>> returnDatas(){
        return ALL_ISEARCH_DATAS;
    }







}
