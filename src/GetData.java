/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Andy
 */

import java.net.URL;
import java.io.*;
import java.util.*;
import org.json.*;
import java.nio.charset.Charset;
import org.json.simple.parser.*;





public class GetData {
    
    private static String getJson(String from){
        //String link = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDCNY%22%2C%20%22USDEUR%22)&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
        //File myOutput = new File(location);
    
         try{
        URL url = new URL(from);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
       // BufferedWriter out = new BufferedWriter(new FileWriter(myOutput));
        
       // char[] cbuf= new char[50];
       String temp = "";
       String s = "";
       while((temp = in.readLine()) != null){           
             s += temp;
         }
         
        
       // System.out.println(s);        
        in.close();
       // out.close();
        return s;
        
         }catch(IOException e){
            e.printStackTrace();
         }
         return null;
    }
    public static String[] readCurrency(){
         //Read JSON File     
         String link = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDCNY%22%2C%20%22USDEUR%22%2C%22USDJPY%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
        
         String s = getJson(link);
         String createdDate = s.substring(31, 50);
         //System.out.println(createdDate);
         
          try{
              JSONObject jsonObj = new JSONObject(s);
              JSONObject query = jsonObj.getJSONObject("query");
              JSONObject results = query.getJSONObject("results");
              JSONArray rate = results.getJSONArray("rate");
              
              String[] arr = new String[rate.length()+1];
              
              arr[rate.length()] = createdDate;
              for(int i =0; i< rate.length();i++){
                  JSONObject curr = rate.getJSONObject(i);
                  String ex = curr.getString("Rate");
                  arr[i] = ex;
              }
              
              
              
              
              System.out.println("Reading JSON");
               return arr;
              
             /* int position = s.indexOf("USD/CNY");
              int position1 = s.indexOf("USD/EUR");
              int position2 = s.indexOf("USD/JPY");
              String cny = s.substring(position+17,position+23);
              String eur = s.substring(position1+17,position1+23);
              String jpy = s.substring(position2+17, position2+23);
              String create = s.substring(31, 50);
               */
              //System.out.println(cny);
             //System.out.println(eur);
             //System.out.println(jpy);
              //System.out.println(create);
              //System.out.println(s);
              
            /*  JSONObject jsonObject = (JSONObject) parser.parse(fileReader);
              JSONObject query = jsonObject.getJSONObject("query");
              JSONObject diagnostics = query.getJSONObject("diagnostics");
              JSONObject results = diagnostics.getJSONObject("results");
              JSONArray rate = results.getJSONArray("rate");
              String chg = (String)  rate.get(2);
              
             // System.out.println(chg);
              */
              }catch (Exception e){
                  e.printStackTrace();
              }
                return null;
         
    }
    
    public static String[] readWeather(){
        
        
        try{
            //Find IP Address
            String url = "http://checkip.amazonaws.com";
            String ip = getJson(url);
            
            //Get Location Based on IP
            String link = "http://api.db-ip.com/v2/5131eb4e1a17261c229234b74f2108da578c833b/" + ip;            
            String location = getJson(link);
            JSONObject jsonObj = new JSONObject(location); 
           String city = jsonObj.getString("city");  //Get City From Location
           String state = jsonObj.getString("stateProv");
           String city1 = city.replaceAll(" ", "%20");
            //Get woeid
            link = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20geo.places%20where%20text%3D%22"+ city1+"%20"+ state +"%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            String cityQuery = getJson(link);
            jsonObj = new JSONObject(cityQuery);            
            JSONObject query = jsonObj.getJSONObject("query");
            JSONObject results = query.getJSONObject("results");            
            JSONObject place = results.getJSONObject("place");
            System.out.print(place);
            String woeid = place.getString("woeid");
            //Get Weather 
            link = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%3D"+ woeid + "&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
            String weather = getJson(link);
            jsonObj = new JSONObject(weather);
            query = jsonObj.getJSONObject("query");
            results = query.getJSONObject("results");
            JSONObject channel = results.getJSONObject("channel");
            JSONObject item = channel.getJSONObject("item");
            //Get Current Weather
            JSONObject condition = item.getJSONObject("condition");
            String time = condition.getString("date");
            String temp = condition.getString("temp");
            String text = condition.getString("text");
            //Getting gif address
            String[] gifA = item.getString("description").split("\"");
            String gif = gifA[1];
            
            //System.out.println(temp);
            
            String[] arr = new String[5];
            arr[0] = gif; // icon address
            arr[1] = city; //city
            arr[2] = temp; //temp
            arr[3] = text; //text
            arr[4] = time; //time
            
            return arr;
            
        }catch (Exception e){
            e.printStackTrace();
        }
        
        return null;
    }    
    
    
    public static void main(String args[]){
        String link = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDCNY%22%2C%20%22USDEUR%22%2C%22USDJPY%22)&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=";
       // String location ="C:\\Users\\Andy\\Documents\\NetBeansProjects\\JavaApplication1\\currency1.json";
        //getJson(link,location);
        //readCurrency(link);
        readWeather();
    }
    
}
