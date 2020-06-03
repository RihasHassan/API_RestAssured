package MovieApi;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import ExcelUtility.ExcelUtilities;
import ExcelUtility.WriteExcel;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.*;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LatestMovie {
	
	 @Test(priority=1)
		public void Status()
		{
		    Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
			int status=res.getStatusCode();
			Assert.assertEquals(status, 200);//Checking status code
		}
	 
	
	 @Test(priority=2)
		public void releaseDate() throws ParseException
		{
		 
		 Calendar c = Calendar.getInstance();
         Date today=c.getTime();
         //SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

        //String todaysdate = format1.format(today); 
         //System.out.println("Todays Date: " + today);
         
		 Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
         JsonPath jsonPathEvaluator = res.jsonPath();
         List<String> releasedate=new ArrayList<String>();
         releasedate=  jsonPathEvaluator.getList("upcomingMovieData.releaseDate");
         
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
         for(String date:releasedate)
         {  
        	 Assert.assertFalse(!date.contains("Null|null"),"Release date Contains Null Value");
        	 
        	 //Checking for relaseDate is before current date 
        	 
        	 Date dateSpecified =dateFormat.parse(date);
        	 
        	 Assert.assertFalse(dateSpecified.before(today),"Release date is before todays date...!!!");
        	  
         }
         
      }
	 
	 @Test(priority=3)
		public void movieTitle()
		{
		    Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
			 String responseBody=res.getBody().asString();

		    JSONObject jsonObject=new JSONObject(responseBody);
		    List<String> list=new ArrayList<String>();
		    JSONArray jsonArray=jsonObject.getJSONArray("upcomingMovieData");
		      for(int i=0;i<jsonArray.length();i++)
		      {
		    	  list.add(jsonArray.getJSONObject(i).getString("moviePosterUrl"));
		    	 //Checking for movie poster url only contain .jpg formate image files
                 
		    	Assert.assertFalse(!jsonArray.getJSONObject(i).getString("moviePosterUrl").endsWith(".jpg"));
		    	     
		    	 
		      }
		    
       }
	 
	
	 
	 @Test(priority=4)
	 public void paytmMovieCode()
		{
		    Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
			 String responseBody=res.getBody().asString();

		    JSONObject jsonObject=new JSONObject(responseBody);
		    List<String> list=new ArrayList<String>();
		    
		    JSONArray jsonArray=jsonObject.getJSONArray("upcomingMovieData");
		      for(int i=0;i<jsonArray.length();i++)
		      {   
		    	  
		    	  String code1=jsonArray.getJSONObject(i).getString("paytmMovieCode");
		    	  for(int j = i + 1; j < jsonArray.length(); j++)
		    	  
		    	  {  
		    		  String code2=jsonArray.getJSONObject(j).getString("paytmMovieCode");
		    		    Assert.assertFalse(code1.equals(code2), "PayTm code is not unique");
                         //Checking for whether paytmMovie code is unique or not 
		    				  //if(code1.equals(code2))
		    				  //{
		 		   		    	// System.out.println("PayTm code is not unique"+code2);

		   		    	     //}
		    				  
		    	  }  
             }
		    	
		    	 
          }
	 
	 
	 @Test(priority=5)
		public void ExcelFileGenration() throws IOException
		{
		 String xlpath= "./Excelfiles/Test.xlsx";
		 String sheetname="MovieNames";
		 WriteExcel excel=new WriteExcel(xlpath,sheetname);
		 Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
         JsonPath jsonPathEvaluator = res.jsonPath();
         List<String> listmovienames=new ArrayList<String>();

           Integer check=0;
			List<String> movie = jsonPathEvaluator.getList("upcomingMovieData.provider_moviename");

				List<Integer> releaseDate = jsonPathEvaluator.getList("upcomingMovieData.isContentAvailable");
				
				for (int i= 0;i<releaseDate.size();i++)
				{
					Integer content=releaseDate.get(i);
					   if(content==check)
					   {
						   
						       int j=i;
						       listmovienames.add(movie.get(j));
							   
					   }
						   
				} 
				excel.setCellData(listmovienames);
		}    
	 
	 
	 @Test(priority=6)
		public void LanguageFormat()
		{
		 
		 Response res=RestAssured.get("https://apiproxy.paytm.com/v2/movies/upcoming");
      JsonPath jsonPathEvaluator = res.jsonPath();

			List<String> languageFormat = jsonPathEvaluator.getList("upcomingMovieData.language");
			List<String> data = new ArrayList<String>();
		 
	        
			for (int i= 0;i<data.size();i++)
			{   
				 
				String content=languageFormat.get(i);
				//Checking for ,movies have more than one language format 
				if(content.contains(","))
			        //System.out.println("more than one language format: " );
  
	        	 Assert.assertTrue(content.contains(","),"Movies have more than one language format");
			}
				
				
		}    
    }
	 
	 
	 

