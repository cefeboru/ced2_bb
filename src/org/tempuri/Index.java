package org.tempuri;

//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import  javax.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.db.BbDatabase;
import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

public class Index {
	private WSAsistenciaSoapProxy cedProxy;
	private Context ctx;
	private String courseID;
	private String studentRut;
	private String token;
	
	
	public static void main(String [] args){
		Connection conn = null;
		try{
			ConnectionManager cManager = null; 
		    PreparedStatement selectQuery = null;
		    cManager = BbDatabase.getDefaultInstance().getConnectionManager();
		    conn = cManager.getConnection();
		    selectQuery = conn.prepareStatement("QUERY", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		    ResultSet rSet = selectQuery.executeQuery();
		    rSet.getString(0);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ConnectionNotAvailableException e) {
			e.printStackTrace();
		}
	}
	// ---- End Main ----
	
	public Index(HttpServletRequest request){
		ctx = ContextManagerFactory.getInstance().setContext(request);
		cedProxy = new WSAsistenciaSoapProxy();
		
		courseID = ctx.getCourse().getCourseId();
		studentRut = ctx.getUser().getBatchUid();
	}
	
	public void registrarAsistencia(HttpServletRequest request){
		
	}
	
	private void setTokenFromWS(){
		try{
			Response r = cedProxy.loginMoodle("Moodle", "ET33OI8994FAQ351P");
			System.out.println(r.getMensaje());
			token =  r.getMensaje();
		}catch(RemoteException e){
			e.printStackTrace();
			token = "Error Generating Token";
		}
	}
	
	public String getToken(){
		setTokenFromWS();
		return token;
	}

	public String getCourseID() {
		try{
			return courseID;
		}catch(Exception ex){
			return ex.getMessage();
		}
		
	}
}
