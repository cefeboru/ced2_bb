<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ page import="blackboard.platform.context.ContextManagerFactory" %>
<%@ page import="blackboard.platform.context.ContextManager" %>
<%@page import="blackboard.data.course.Course"%>

<%@page import="org.tempuri.CED_WebService"%>
<%@taglib uri="/bbData" prefix="bbData" %>

<bbData:context id="ctx">
	
	<p><%
		CED_WebService wb = new CED_WebService();
		String token = "";
		token = wb.getToken();
		out.println("Token: " + token);
	%></p>
	<p><% try{
		ContextManagerFactory.getInstance().setContext(request);
		String user = ContextManagerFactory.getInstance().getContext().getCourse().toString();
		out.print(user);
	}catch(Exception ex){
		out.print(ex);
	} %></p>
	
</bbData:context>