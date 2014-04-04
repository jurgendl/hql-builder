<%@ page contentType="text/html; charset=utf-8" language="java"
	import="java.sql.*,java.util.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Login</title>
<script type="text/javascript">
var popup="Sorry, right-click is disabled."; 
			function noway(go) 
			{ 
				if(document.all) 
				{ 
					if (event.button == 2) 
					{ 
						alert(popup); 
						return false; 
					} 
				} 
				if (document.layers)
				{ 
					if (go.which == 3) 
					{ 
						alert(popup); 
						return false; 
					} 
				} 
			} 
			if (document.layers) 
			{ 
				document.captureEvents(Event.MOUSEDOWN); 
			} 
			document.onmousedown=noway;
			
function setBorder(element,val){
	element.style.border=val;
}

function setColor(element,val){
	element.style.backgroundColor =val;
}			

function validateName(name){
	for (var i = 0; i < name.length; i++){
		var ch = name.charAt(i);
	    if (!/\D/.test(ch)||/[a-z]/.test(ch)||ch=="."){
			bool=true;
			setBorder(document.f.j_username,'2px inset #EBE9ED')
			continue;
		}else{
		setBorder(document.f.j_username,'2px solid red')
		setColor(document.f.j_username,'pink');
		alert("Enter Valid username");
		return false;
		}
	}
	return bool;
}

function validate(){
	var validated= new Boolean();
	validated=false;
	validated=validateName(document.f.j_username.value);
	if(validated){
		document.f.submit();
	}
}

</script>
</head>

<body>
<p>Login</p>

<!-- Login Form: Contains two parameters for authenticating
	1. j_username : Username of the user trying to authenticate 
	2. j_password : Password of the user trying to authenticate-->

<form name='f'
	action='${pageContext.request.contextPath}/j_spring_security_check'
	method='post'>
<p><label>Username <input type="text" name="j_username"
	id="j_username" /> </label></p>
<p><label for="login_password">Password</label> <input
	type="password" name="j_password" id="j_password" /> max 10
characters.</p>
<p><input name="login" type="button" onclick="validate();"
	id="login" value="login" /> <input type="hidden" name="client"
	value="register" /></p>

</form>
</body>
</html>