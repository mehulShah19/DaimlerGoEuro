package com.daimler.peninsula.goeuro.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mehul on 12/09/16.
 */
public class LocationInformation {

        @SerializedName("_id")
        private int id;
        //public Object key;
        private String name;
        private String fullName;
        @SerializedName("iata_airport_code")
        
        private Object iataAirportCode;

        private String type;
        private String country;

        @SerializedName("geo_position")
        private GeoPosition geoPosition;
       // public Object locationId;
        private boolean inEurope;
        private int countryId;
        private String countryCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Object getIataAirportCode() {
        return iataAirportCode;
    }

    public void setIataAirportCode(Object iataAirportCode) {
        this.iataAirportCode = iataAirportCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GeoPosition getGeoPosition() {
        return geoPosition;
    }

    public void setGeoPosition(GeoPosition geoPosition) {
        this.geoPosition = geoPosition;
    }

    public boolean isInEurope() {
        return inEurope;
    }

    public void setInEurope(boolean inEurope) {
        this.inEurope = inEurope;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isCoreCountry() {
        return coreCountry;
    }

    public void setCoreCountry(boolean coreCountry) {
        this.coreCountry = coreCountry;
    }

    public boolean coreCountry;
//      public Object distance;
//      @SerializedName("names")
//
//        public Names names;
//        @SerializedName("alternativeNames")
//
//        public AlternativeNames alternativeNames;

}


