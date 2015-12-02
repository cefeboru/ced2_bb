<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<%@ page import="blackboard.platform.context.ContextManagerFactory" %>
<%@ page import="blackboard.platform.context.ContextManager" %>
<%@ page import="org.tempuri.Index" %>


<%@taglib uri="/bbData" prefix="bbData" %>

<bbData:context id="ctx">
	<%
	Index index = new Index(request); 
	out.print("<p>" + index.getCourseID() + "</p>");
	%>
</bbData:context>