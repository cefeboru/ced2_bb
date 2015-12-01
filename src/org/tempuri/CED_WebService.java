package org.tempuri;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import blackboard.db.BbDatabase;
import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;

public class CED_WebService {
	
	public static void main(String [] args){
		Connection conn = null;
		try{
			ConnectionManager cManager = null; 
		    PreparedStatement selectQuery = null;
		    
		    cManager = BbDatabase.getDefaultInstance().getConnectionManager();
		    conn = cManager.getConnection();
		    
		    selectQuery = conn.prepareStatement("QUERY", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			
		    ResultSet rSet = selectQuery.executeQuery();
		   
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getToken(){
		try{
			WSAsistenciaSoapProxy cedProxy = new WSAsistenciaSoapProxy();
			Response r = cedProxy.loginMoodle("Moodle", "ET33OI8994FAQ351P");
			System.out.println(r.getMensaje());
			return r.getMensaje();
		}catch(RemoteException e){
			e.printStackTrace();
			return "Error Generating Token";
		}
	}
}
