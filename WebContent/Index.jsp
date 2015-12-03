<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8" %>

<%@ page import="blackboard.platform.context.ContextManagerFactory" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*" %>
<%@ page import="org.tempuri.Index" %>


<%@taglib uri="/bbData" prefix="bbData" %>

<bbData:context id="ctx">
	<p><%
		Index in = new Index(request);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		out.print(sdf.format(now));
		
	%></p>
</bbData:context>