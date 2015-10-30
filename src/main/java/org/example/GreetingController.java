package org.example;

//import domain.City;

import java.util.List;
import java.util.ArrayList;

import org.example.domain.City;
import org.example.domain.TickerHistory;
import org.example.repositories.CityRepository;
import org.example.repositories.TickerHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import org.example.Trip;

@Controller
public class GreetingController {


private boolean isLoggedIn=false;
private String accessToken;
private long expiresIn ;
private boolean DEBUG=true;
	
	@Autowired
  	CityRepository cityRepository ;
	
	@Autowired
  	TickerHistoryRepository tickerHistoryRepository ;
	
	
	@ModelAttribute("tickerHistoryList")
	public List<TickerHistory> getTickerHistoryList() {
	    return (List<TickerHistory>) this.tickerHistoryRepository.findAll();
	}

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }
    
    
    @RequestMapping("/")
    public String root(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @RequestMapping("/index")
    public String index(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

   /**
    *  For drawing the showing a trip.           
    *                                               
    * Outputs in map.html 
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    @RequestMapping("/map")
    public String map(@RequestParam(value="name", required=false, defaultValue="World") String name
                      , @RequestParam(value="startLon", required=false, defaultValue="World") String startLon
                      , @RequestParam(value="startLat", required=false, defaultValue="World") String startLat
                      , @RequestParam(value="endLon", required=false, defaultValue="World") String endLon
                      , @RequestParam(value="endLat", required=false, defaultValue="World") String endLat
                      , @RequestParam(value="path", required=false, defaultValue="World") String path
                      , Model model) {
        model.addAttribute("name", name);
        model.addAttribute("startLon", startLon);
        model.addAttribute("startLat", startLat);
        model.addAttribute("endLon", endLon);
        model.addAttribute("endLat", endLat);
        model.addAttribute("path", path);
        System.out.println("startLon ="  + startLon + ", startLat=" + startLat ) ;
        return "map";
    }

    /**
    *  For testing           
    *                                               
    * Outputs in test.html 
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    @RequestMapping("/test")
    public String test(@RequestParam(value="name", required=false, defaultValue="World") String name
                      , @RequestParam(value="startLon", required=false, defaultValue="World") String startLon
                      , @RequestParam(value="startLat", required=false, defaultValue="World") String startLat
                      , @RequestParam(value="endLon", required=false, defaultValue="World") String endLon
                      , @RequestParam(value="endLat", required=false, defaultValue="World") String endLat
                      , @RequestParam(value="path", required=false, defaultValue="World") String path
                      , Model model) {
        model.addAttribute("name", name);
        model.addAttribute("startLon", startLon);
        model.addAttribute("startLat", startLat);
        model.addAttribute("endLon", endLon);
        model.addAttribute("endLat", endLat);
        model.addAttribute("path", path);
        System.out.println("startLon ="  + startLon + ", startLat=" + startLat ) ;
        return "test";
   }
    
    @RequestMapping("/configure")
    public String configure(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "underConstruction";
    }
    
/* Commented Out
    @RequestMapping("/discover")
    public String discover(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "underConstruction";
    }
    
    @RequestMapping("/history")
    public String history(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        
        System.out.println("tickers found with findAll():");
        System.out.println("-------------------------------");
       
        try {
			for  ( TickerHistory tickerHistory : tickerHistoryRepository.findAll() )
			{ 
			  System.out.println("history: Recommendation for  " + tickerHistory.getName() + ", @ " + tickerHistory.getPrice() + ", is " +  tickerHistory.getRecommendation());
			  
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "historyresults";
    }
    
    @RequestMapping("/filterHistory")
    public String filterHistory(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "underConstruction";
    }
*/

    /**
    * After the user  Authorizes with Automatic API's, Automatic redirects here            
    *                                               
    * Use OAuth token and get vehicle and User Data 
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    @RequestMapping("/redirect")
    public String redirect(@RequestParam(value="code", required=false) String code
                               , @RequestParam(value="state", required=false) String state
                               ,Model model) {
	System.out.println("***  In redirect: code is" + code + ", state is " + state );

        try {
  

        OAuthClientRequest request = OAuthClientRequest
                .tokenLocation("https://accounts.automatic.com/oauth/access_token")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setClientId("b7e67af066fa3ae53ab7")
                .setClientSecret("910b0a8769b54bf3a8fce78a953d70c0d6b3540b")
                .setRedirectURI("http://iotwebapp.cfapps.io/redirect")
                .setCode(code)
                .buildBodyMessage();
 
            //create OAuth client that uses custom http client under the hood
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
 
            final OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request, "POST");
            accessToken = oAuthResponse.getAccessToken();
            expiresIn    = oAuthResponse.getExpiresIn();
            isLoggedIn=true;

	    System.out.println("***  In redirect: accessToken is" + accessToken + ",isLoggedIn=" + isLoggedIn);
  
            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://api.automatic.com/user/me/")
                .setAccessToken(accessToken).buildQueryMessage();
            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, "GET", OAuthResourceResponse.class);

	    System.out.println("***  In redirect 2 : resourceResponse  is" + resourceResponse );
            
            try {
            String userinfoJSON = resourceResponse.getBody();
	    final JSONObject obj    = new JSONObject(userinfoJSON);
           String id = obj.getString("id"); 
           String fname     = obj.getString("first_name");
           String lname     = obj.getString("last_name");
           String email     = obj.getString("email");
           String userName     = obj.getString("username");


           model.addAttribute("firstName", fname);
           model.addAttribute("lastName", lname);
           model.addAttribute("userName", userName);
           model.addAttribute("email", email);

           System.out.println("id ="  + id + ", obj=" + obj ) ;
           getVehicleInfo( model, oAuthClient, accessToken );
           getTripInfo( model, oAuthClient, accessToken );

           } catch (JSONException jsone) {
              System.out.println("*** json error: " + jsone.getMessage());
              System.out.println("*** json error description: " + jsone.toString());
           }

            } catch (OAuthSystemException e) {
              System.out.println("*** OAuth error: " + e.getMessage());
              System.out.println("*** OAuth error description: " + e.toString());
            } catch( OAuthProblemException oape) {
              System.out.println("*** OAuth error: " + oape.getMessage());
              System.out.println("*** OAuth error description: " + oape.toString());
            }
            
           
        return "userData";
    }
    
    /**
    * Called when a user clicks View Car Data button on the screen  
    * Sets up the OAuth request and calls the redirect URL          
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    @RequestMapping("/getCarData")
    public String getCarData(@RequestParam(value="ticker", required=false, defaultValue="EMC") String ticker, Model model) {
        
       try 
       {        

       String scope = "scope:user:profile scope:trip scope:location scope:vehicle:profile scope:vehicle:events";

       final int LENGTH = 8;
       StringBuffer sb = new StringBuffer();
       for (int x = 0; x < LENGTH; x++)
       {
          sb.append((char)((int)(Math.random()*26)+97));
       }
       System.out.println(sb.toString());

       OAuthClientRequest request = OAuthClientRequest
                                     .authorizationLocation("https://accounts.automatic.com/oauth/authorize")
                                     .setClientId("b7e67af066fa3ae53ab7")
                                     .setResponseType(ResponseType.CODE.toString())
                                     .setScope(scope)
                                     .buildQueryMessage();
        
        String redirectString= "redirect:" + request.getLocationUri() ;
        System.out.println("1. OAuth request  location uri is " + redirectString );
        /* Note - Returning here *** */
        return redirectString ;

       } catch (OAuthSystemException e) {
            System.out.println("OAuth error: " + e.getMessage());
            System.out.println("OAuth error description: " + e.toString());
        }
       
        /* Note: Will not get heref it works  because of redirect above */
        return "error" ;
    }
    


    /**
    *  Get Vehicle Info and add to the Model 
    *                                                               
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    public void getVehicleInfo( Model model, OAuthClient oAuthClient, String accessToken ) throws OAuthSystemException, OAuthProblemException {
           System.out.println("In getVehicleInfo ");
           OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://api.automatic.com/vehicle/")
                .setAccessToken(accessToken).buildQueryMessage();
           OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, "GET", OAuthResourceResponse.class);
	   System.out.println(" In getVehicleInfo  : resourceResponse  is" + resourceResponse );
            
            try {
            String vehicleinfoJSON = resourceResponse.getBody();
	    final JSONObject obj    = new JSONObject(vehicleinfoJSON);

           System.out.println("getVehicleInfo: obj= " + obj ) ;
           String results = obj.getString( "results") ;
           System.out.println("getVehicleInfo: results= " + results ) ;
           final JSONArray values = obj.getJSONArray("results") ;
           for (int i = 0; i < values.length(); i++) {
    
             JSONObject resultsObj = values.getJSONObject(i); 
       
             model.addAttribute("displayName", resultsObj.getString("display_name") );
             model.addAttribute("make", resultsObj.getString("make") );
             model.addAttribute("model", resultsObj.getString("model") );
             model.addAttribute("year", resultsObj.getString("year") );
             System.out.println("getVehicleInfo: make= " + resultsObj.getString("make") ) ;
           }

           } catch (JSONException jsone) {
              System.out.println("*** json error: " + jsone.getMessage());
              System.out.println("*** json error description: " + jsone.toString());
           }


    }
    
    /**
    *  Get Trip Info and add to the Model 
    *                                                               
    * @author   Sharath Sahadevan  
    * @version  1.0
    * @Since    Oct 2015                   
    */
    public void getTripInfo( Model model, OAuthClient oAuthClient, String accessToken ) throws OAuthSystemException, OAuthProblemException {
           String startLon, startLat;
           String endLon, endLat;
           String path;
           String id;

           System.out.println("In getTripInfo ");
           OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://api.automatic.com/trip/")
                .setAccessToken(accessToken).buildQueryMessage();
           OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, "GET", OAuthResourceResponse.class);
	   System.out.println(" In getTripInfo  : resourceResponse  is" + resourceResponse );
            
           try {
           String tripinfoJSON = resourceResponse.getBody();
           final JSONObject obj    = new JSONObject(tripinfoJSON);

           System.out.println("getTripInfo: obj= " + obj ) ;
           String results = obj.getString( "results") ;
           System.out.println("getTripInfo: results= " + results ) ;
           final JSONArray values = obj.getJSONArray("results") ;
           model.addAttribute( "numberOfTrips" , values.length() ) ;

           Trip trip = null ;
           ArrayList<Trip> tripList = new ArrayList<Trip>();
           for (int i = 0; i < values.length(); i++) {
    
             JSONObject resultsObj = values.getJSONObject(i); 
       
             System.out.println("getTripInfo: resultsObj= " + resultsObj ) ;
             final JSONObject startAddressObj = resultsObj.getJSONObject("start_address");
             final JSONObject endAddressObj = resultsObj.getJSONObject("end_address");
             // System.out.println("getTripInfo: startAddress= " + startAddressObj.getString("name") ) ;
        
             final JSONObject startLocationObj = resultsObj.getJSONObject("start_location");
             final JSONObject endLocationObj = resultsObj.getJSONObject("end_location");

             startLon=startLocationObj.getString("lon");
             startLat=startLocationObj.getString("lat");

             endLon=endLocationObj.getString("lon");
             endLat=endLocationObj.getString("lat");

             path=resultsObj.getString("path");
             id=resultsObj.getString("id");

             System.out.println("getTripInfo: startLocation= " + startLocationObj.getString("lon") ) ;
             System.out.println("getTripInfo: path= " + path ) ;
             trip = new Trip( startAddressObj.getString("name") 
                                , resultsObj.getString("started_at")
                                , endAddressObj.getString("name") 
                                , resultsObj.getString("ended_at")
                                ,startLon, startLat, endLon, endLat
                                ,path
                                ,id
                            ) ;


             tripList.add( trip);
          
             if ( DEBUG )
             {
                System.out.println("triplist size is " +  tripList.size() + ",trip.startAddess is " + trip.getStartAddress() );
                System.out.println("triplist path is " +   trip.getPath() );
             }

           }

           model.addAttribute("tripList", tripList ) ;
           if ( DEBUG )
           {
           	System.out.println("triplist size is " +  tripList.size() + ",tripList is " + tripList );
           }


           } catch (JSONException jsone) {
              System.out.println("*** json error: " + jsone.getMessage());
              System.out.println("*** json error description: " + jsone.toString());
           }


    }
    
}
