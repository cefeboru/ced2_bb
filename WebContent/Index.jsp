<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>

<%@ page import="blackboard.platform.context.ContextManagerFactory" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*" %>
<%@ page import="org.tempuri.Index" %>


<%@taglib uri="/bbData" prefix="bbData" %>

<bbData:context id="ctx">
	<% Index in = new Index(); %>
	<p> Respuesta del WS:<% out.print(in.Index(request)); %></p>
	<p>Course Id:<% out.print(in.getCourseID()); %></p>
	<p>Token: <% out.print(in.getToken()); %></p>
	<p>Modulo: <% out.print(in.getModulo()); %></p>
	
</bbData:context>