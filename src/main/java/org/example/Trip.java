package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trip {

    private String startAddress;
    private String startedAt;
    private String endAddress;
    private String endedAt;
    private String startLon;
    private String startLat;
    private String endLon;
    private String endLat;
    private String path;
    private String id;
    
        

    public Trip( String startAddressParm, String startedAtParm
                , String endAddressParm, String endedAtParm
                , String startLonParm, String startLatParm
                , String endLonParm, String endLatParm
                , String pathParm
                , String idParm
               ) 
    {
        startAddress = startAddressParm;
        startedAt = startedAtParm;
        endAddress = endAddressParm;
        endedAt = endedAtParm;
        startLon =startLonParm;
        startLat =startLatParm;
        endLon =endLonParm;
        endLat =endLatParm;
        path =pathParm;
        id = idParm;

        System.out.println("Trip: startAddress is " + startAddress );
    }

    public String getStartAddress() {
        return startAddress;
    }
    
    public String getStartedAt() {
        return startedAt;
    }
    
    public String getEndAddress() {
        return endAddress;
    }
    
    public String getEndedAt() {
        return endedAt;
    }

    public String getStartLon() {
        return startLon;
    }

    public String getStartLat() {
        return startLat;
    }

    public String getEndLon() {
        return endLon;
    }

    public String getEndLat() {
        return endLat;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

      
}
