package io.helidon.examples.quickstart.mp;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import io.helidon.common.http.SetCookie;
import io.helidon.microprofile.cors.CrossOrigin;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.logging.Logger;


@Path("/user")
@ApplicationScoped
public class UserRegistration {
	
	 private final DataSource dataSource;
	 private static final JsonBuilderFactory JSON = Json.createBuilderFactory(Collections.emptyMap());
	 private static final Logger LOGGER = Logger.getLogger(UserRegistration.class.getName());
	 @Inject
	  public UserRegistration(@Default final DataSource dataSource) throws SQLException {
	    super();
	    this.dataSource = Objects.requireNonNull(dataSource);
	    this.dataSource.setLoginTimeout(2000000);
	  }
	 
	 
	 	@Path("/openregister")
	    @GET
	    @Produces(MediaType.TEXT_HTML)
	 	@CrossOrigin(value = {"http://150.136.116.225:30996/","http://150.136.116.225:30998/"},
	       allowMethods = {HttpMethod.POST,HttpMethod.GET})
	    public  Response validateSession(@QueryParam("trackId") String trackId,@QueryParam("userId") String userId) throws URISyntaxException, SQLException {
	    	
	 		 LOGGER.info("DBOperations:validateSession"+"   Form Param : trackId===>"+trackId+"<=======userId===>"+userId);
	 		URI ui = null;
	 		 String checkSql = "select LOGIN_TIME, LOGIN_STATUS from USER_SSO where SESSION_ID = ? and USER_ID = ?";
	 		Connection connection = null;
	 		 try {
				 
				LOGGER.info(userId);
				connection = this.dataSource.getConnection();
				PreparedStatement st = connection.prepareStatement(checkSql);
				int t = Integer.parseInt(userId);
				st.setString(1, trackId);
				st.setInt(2, t);
					 
				ResultSet rs = st.executeQuery();
				
				Timestamp ts = null;
				String loginStatus = null;
				while(rs.next()) {
					ts = rs.getTimestamp("LOGIN_TIME");
					loginStatus = rs.getString("LOGIN_STATUS");
				}
				LOGGER.info("Time Stamp       =======>  "+ts+"   and Login status ===========>"+loginStatus);
				if(calculateTimeDiff(ts) > 20) {
					 ui = new URI("http://150.136.116.225:30998/timeout.html");
				} else {
					 ui = new URI("http://150.136.116.225:30998/register.html");
				}
			 }catch(Exception e) {
				 e.printStackTrace();
			 }finally {
				 connection.close();
			 }
	 		LOGGER.info("UI Get ----->"+ui);
	 		//NewCookie[] sesIdCok = SetCookie.create("regValidId",trackId);
			 return Response.seeOther(ui)
					 .header("Access-Control-Allow-Origin","*")
					 .build();
	    }
	 	
	 	public long calculateTimeDiff(Timestamp ts) {
	 		
	 		SimpleDateFormat fm = new SimpleDateFormat("E MMM dd hh:mm:ss Z yyyy");
	 	     long now = System.currentTimeMillis();
	 	     
	 	     Timestamp ps = new Timestamp(now);
	 	      
	 	     Date loginTime = new Date(ts.getTime());
	 	     Date currentTime = new Date(ps.getTime());
	 	     
	 	    try {
	 	    	loginTime = fm.parse(loginTime.toString());
	 	       currentTime = fm.parse(currentTime.toString());
	 	    } catch (Exception e) {
	 	        e.printStackTrace();
	 	    }    
	 	    
	 	    LOGGER.info("loginTime : "+loginTime);
	 	    LOGGER.info("currentTime : "+currentTime);
	 	      
	 	      long diff = currentTime.getTime() - loginTime.getTime();
	 	      long diffMinutes = diff / (60 * 1000);
	 	      LOGGER.info("Time in minutes: " + diffMinutes + " minutes.");   

	 		return diffMinutes;
	 	}
	 
	 
	  
	    @Path("/register")
	    @POST
	    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	    @Produces(MediaType.APPLICATION_JSON)
	    public JsonObject registerUser(@FormParam("email") String email,@FormParam("firstName") String firstName,@FormParam("lastName") String lastName,@FormParam("pwd") String password,@FormParam("phone") String phone) throws SQLException {
	       
	    	LOGGER.info(email+firstName+lastName);
	    	Timestamp lt = new Timestamp(0);
	    	String insertTableSQL = "INSERT INTO IKM_USER (USER_ID, FIRSTNAME, LASTNAME, EMAIL, PHONE, USER_PASSWORD, CREATED_DATE) VALUES (USER_SEQ.nextVal, ?, ?, ?, ?, ?, ?)";
	    	LOGGER.info("DBOperations:registerUser");
	    	int status = 0;
	    	Connection connection =null;
	    	//PreparedStatement stmt = null;
	    	try {
	    		
	    		connection = this.dataSource.getConnection();
	    		PreparedStatement stmt = connection.prepareStatement(insertTableSQL);
	    		stmt.setString(1, firstName);
	    		stmt.setString(2, lastName);
				stmt.setString(3, email);
				stmt.setString(4, phone);
				stmt.setString(5, password);
				stmt.setTimestamp(6, lt);
				status = stmt.executeUpdate();
	    	} catch(Exception e) {
	    		e.printStackTrace();
	    	}finally {
	    		connection.close();
	    	}
			if(status == 0) {
				return JSON.createObjectBuilder().add("InsertStatus", status).build();
				
			} else {
				return JSON.createObjectBuilder().add("InsertStatus", status).build();
			}
			
			/*final Response returnValue = Response.ok()
				      .entity(JSON.toString())
				      .build(); */
			
	    }
	 
	 

		
		
	
	

}
