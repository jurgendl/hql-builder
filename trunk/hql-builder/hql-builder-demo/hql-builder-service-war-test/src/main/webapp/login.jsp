<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*,java.util.*" errorPage=""%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Login</title>
</head>
<body>
<p>Login</p>
<form name="f" action="${pageContext.request.contextPath}/j_spring_security_check" method="post">
<p><label for="j_username">Username</label> <input type="text" name="j_username" id="j_username" /></p>
<p><label for="j_password">Password</label> <input type="password" name="j_password" id="j_password" /> max 10 characters.</p>
<p><input name="login" type="submit" id="login" value="login" /><input type="hidden" name="client" value="register" /></p>
</form>
</body>
</html>