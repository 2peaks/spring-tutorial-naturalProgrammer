<%@include file="includes/header.jsp" %>

<div class="panel panel-default">
	<div class="panel-heading">
		<h3 class="panel-title">Please signup</h3>
	</div>
	
	<div class="panel-body">
		<!-- if you don't provide 'action' and 'method', spring add action url to the current page and method to post -->
		<form:form modelAttribute="signupForm" role="form">
		
			<form:errors /> <!-- if error on the form itself -->
			<div class="form-group">
				<form:label path="email">Email address</form:label>
				<form:input path="email" type="email" class="form-control" placeholder="Enter your email address" />
				<form:errors cssClass="error" path="email"/>
				<p class="help-block">Enter a unique email address. It will also be your login id</p>
			</div>
			
			<div class="form-group">
				<form:label path="name">Name</form:label>
				<form:input path="name" class="form-control" placeholder="Enter your name" />
				<form:errors cssClass="error" path="name"/>
				<p class="help-block">Enter your display name</p>
			</div>
			
			<div class="form-group">
				<form:label path="password">Password</form:label>
				<form:password path="password" class="form-control" placeholder="Enter your password" />
				<form:errors cssClass="error" path="password"/>
			</div>
			<button type="submit" class="btn btn-default">Submit</button>
		</form:form>
	</div>
</div>
	
<%@include file="includes/footer.jsp" %>