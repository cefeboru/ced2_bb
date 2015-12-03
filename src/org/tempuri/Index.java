package org.tempuri;

//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;

import  javax.servlet.http.HttpServletRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import blackboard.db.BbDatabase;
import blackboard.db.ConnectionManager;
import blackboard.db.ConnectionNotAvailableException;
import blackboard.platform.context.Context;
import blackboard.platform.context.ContextManagerFactory;

import java.util.Date;

public class Index {
	private WSAsistenciaSoapProxy cedProxy;
	private Context ctx;
	private String courseID;
	private String studentRut = "15543201";
	private String token;
	private String modulo;
	//public String mensajeWS;
	private Connection conn = null;
	private ConnectionManager cManager = null; 
	private PreparedStatement selectQuery = null;
	ResultSet rSet= null;
	
	
	public static void main(String [] args){

	}
	// ---- End Main ----
	
	public Index(HttpServletRequest request){
		//Set the BB Context
		ctx = ContextManagerFactory.getInstance().setContext(request);
		//Instance the WS Class
		cedProxy = new WSAsistenciaSoapProxy();
		
		courseID = ctx.getCourse().getCourseId();
		//studentRut = ctx.getUser().getBatchUid();
		
		//Open the DB connection
		try{
			cManager = BbDatabase.getDefaultInstance().getConnectionManager();
		    conn = cManager.getConnection();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		modulo = getModulo();
		token = getToken();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Date now = new Date();
		String fechaAsistencia = sdf.format(now);
		try {
			Response r = cedProxy.registrarAsistencia(modulo, studentRut, fechaAsistencia, token);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		

	}
	
	public String getModulo(){
		String modulo = "";
		
		try{
		    selectQuery = conn.prepareStatement(
		    		"select * from lnoh_modulos_estudiantes where rut_id=" + studentRut + " and curso_id='" + courseID +"';",
		    		ResultSet.TYPE_FORWARD_ONLY, 
		    		ResultSet.CONCUR_READ_ONLY);
		    rSet = selectQuery.executeQuery();
		    
		    if(rSet.next()){
		    	String seccion;
		    	String anio;
		    	String semestre;
		    	seccion = rSet.getString("seccion");
		    	anio = rSet.getString("anio");
		    	semestre = rSet.getString("semestre");
		    	modulo = courseID + "-" + seccion + "-" + anio + "-" + semestre;
		    } else {
		    	//TODO Que hacer cuando no encuentre data;
		    	return "No se encontro el modulo en la BD";
		    }
		} catch(SQLException e){
			e.printStackTrace();
		}
    	return modulo;
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
