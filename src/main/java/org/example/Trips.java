package org.example;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trips {

    private String startAddress;
    private String startedAt;
    private String endAddress;
    private String endedAt;
        

    public Trips( String startAddress, String startedAt, String endAddress, String endedAt ) 
    {
        startAddress = startAddress;
        startedAt = startedAt;
        endAddress = endAddress;
        endedAt = endedAt;
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

      
}
