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
	private String id_curso;
	private String rut_estudiante = "15543201";
	private String token;
	private String modulo;
	//public String mensajeWS;
	private Connection conn = null;
	private ConnectionManager cManager = null; 
	private PreparedStatement queryString = null;
	ResultSet rSet= null;
	
	public String Index(HttpServletRequest request){
		//Set the BB Context
		ctx = ContextManagerFactory.getInstance().setContext(request);
		//Instance the WS Class
		cedProxy = new WSAsistenciaSoapProxy();
		
		id_curso = ctx.getCourse().getCourseId();
		//rut_estudiante = ctx.getUser().getUuid();
		
		//Open the DB connection
		try{
			cManager = BbDatabase.getDefaultInstance().getConnectionManager();
		    conn = cManager.getConnection();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//get the course module from the DB
		modulo = getModulo();
		
		//Call the WS method loginMoodle to get the Token
		token = getToken();
		
		//Format the date to be sent to the WS
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		Date now = new Date();
		String fechaAsistencia = sdf.format(now);
		//Call the WS method registrarAsistencia 
		try {
			Response r = cedProxy.registrarAsistencia(modulo, rut_estudiante, fechaAsistencia, token);
			if(!(r.getCodigo() == 0 || r.getCodigo() == 10)){
				//TODO send mail to users
			}
			queryString = conn.prepareStatement(
		    		"insert into lnoh_ced_response(rut_estudiante,id_curso,fecha_asistencia,fecha_ws,codigo) VALUES " 
		    				+ "(" + rut_estudiante + ",'" + id_curso.toString() + "'," + fechaAsistencia + "," + System.currentTimeMillis()/1000L + "",
		    		ResultSet.TYPE_FORWARD_ONLY, 
		    		ResultSet.CONCUR_READ_ONLY);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	public String getModulo(){
		String modulo = "";
		
		try{
		    queryString = conn.prepareStatement(
		    		"select * from lnoh_modulos_estudiantes where rut_id=" + rut_estudiante + " and curso_id='" + id_curso +"';",
		    		ResultSet.TYPE_FORWARD_ONLY, 
		    		ResultSet.CONCUR_READ_ONLY);
		    rSet = queryString.executeQuery();
		    
		    if(rSet.next()){
		    	String seccion;
		    	String anio;
		    	String semestre;
		    	seccion = rSet.getString("seccion");
		    	anio = rSet.getString("anio");
		    	semestre = rSet.getString("semestre");
		    	modulo = id_curso + "-" + seccion + "-" + anio + "-" + semestre;
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
			return id_curso;
		}catch(Exception ex){
			return ex.getMessage();
		}
		
	}
}
