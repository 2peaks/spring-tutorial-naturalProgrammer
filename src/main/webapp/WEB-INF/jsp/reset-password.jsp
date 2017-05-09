<%@include file="includes/header.jsp" %>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">Reset password?</h3>
	</div>
	
	<div class="panel-body">
		<form:form modelAttribute="resetPasswordForm" role="form">
			
			<form:errors /> <!-- if error on the form itself -->
			
			<div class="form-group">
				<form:label path="password">Type new password</form:label>
				<form:password path="password" class="form-control" plassholder="Password" />
				<form:errors cssClass="error" path="password" />
			</div>
			
			<div class="form-group">
				<form:label path="retypePassword">Retype new password</form:label>
				<form:password path="retypePassword" class="form-control" plassholder="Retype Password" />
				<form:errors cssClass="error" path="retypePassword" />
			</div>
			
			<button type="submit" class="btn btn-default">Submit</button>
		</form:form>
	</div>
</div>
	
<%@include file="includes/footer.jsp" %>