<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Up and running with Spring Framework quickly</title>
<link href="/public/lib/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<link href="/public/css/styles.css" rel="stylesheet">
</head>
<body>
<div class="container">
	<nav class="navbar navbar-default">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="#">Brand</a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav">
	        <li class="active"><a href="#">Link <span class="sr-only">(current)</span></a></li>
	        <li><a href="#">Link</a></li>
	        <li class="dropdown">
	          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
	          <ul class="dropdown-menu">
	            <li><a href="#">Action</a></li>
	            <li><a href="#">Another action</a></li>
	            <li><a href="#">Something else here</a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="#">Separated link</a></li>
	            <li role="separator" class="divider"></li>
	            <li><a href="#">One more separated link</a></li>
	          </ul>
	        </li>
	      </ul>
	      <form class="navbar-form navbar-left">
	        <div class="form-group">
	          <input type="text" class="form-control" placeholder="Search">
	        </div>
	        <button type="submit" class="btn btn-default">Submit</button>
	      </form>
	      <ul class="nav navbar-nav navbar-right">
		    <sec:authorize access="isAnonymous()">
		      <li><a href="<c:url value="/signup" />"> <span class="glyphicon glyphicon-list-alt"></span> Sign Up</a></li>
		      <li><a href="<c:url value="/login" />"> Sign in <span class="glyphicon glyphicon-log-in"></span></a></li>
		    </sec:authorize>
		    
		    <sec:authorize access="isAuthenticated()">
		        <li class="dropdown">
		          <a href="#" class="dropdown-toggle" data-toggle="dropdown">
		          	<span class="glyphicon glyphicon-user"></span>
		          	<sec:authentication property="principal.user.name" /> <b class="caret"> </b>
		          </a>
		          <ul class="dropdown-menu">
		            <li><a href="/users/<sec:authentication property='principal.user.id' />"><span class="glyphicon glyphicon-user" ></span> Profile</a></li>
		            <li>
		            	<!-- spring logout is on POST. For consistence look, add <a> tag that submits the logout form. Just a UI trick-->
			        	<c:url var="logoutUrl" value="/logout" />
			        	<form:form id="logoutForm" action="${logoutUrl}" method="post">
			        	</form:form>
			        	<a href="#" onclick="document.getElementById('logoutForm').submit()"> <span class="glyphicon glyphicon-log-out"></span> Sign out</a>
		            </li>
		          </ul>
		        </li>
	        </sec:authorize>
	      </ul>
	    </div><!-- /.navbar-collapse -->
	  </div><!-- /.container-fluid -->
	</nav>
	
	<sec:authorize access="hasRole('ROLE_UNVERIFIED')">
		<div class="alert alert-warning alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			Your email id is unverified <a href="/users/resend-verification-mail">Click here</a> to get the verification mail again.
		</div>
	</sec:authorize>
	
	<c:if test="${not empty flashMessage}">
		<div class="alert alert-${flashKind} alert-dismissable">
			<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
			${flashMessage}
		</div>
	</c:if>