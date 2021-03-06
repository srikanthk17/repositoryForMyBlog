<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>HelloWorld page</title>
    <!-- <script type="text/javascript" src="http://code.jquery.com/jquery-2.1.4.min.js"></script> -->
    <script type="text/javascript" src="js/jquery-2.1.4.min.js"></script>
    <script type="text/javascript">
    function beautify(tagName) {
		var val = $('textarea[name=' + tagName + ']').val();
		var jsonobj = JSON.parse(val);
		var x = JSON.stringify(jsonobj, null, 4); // Indented 4 spaces
		x = JSON.stringify(jsonobj, null, "\t");
		$('textarea[name=' + tagName + ']').val(x);
	}
    
    function serviceTest() {
		var url = $('input[name=url]').val();
		var username = $('input[name=username]').val();
		var password = $('input[name=password]').val();
		var method = $('input[name=requestMethod]:checked').val();
		var input = $('textarea[name=jsonInput]').val();
    	if(typeof method === "undefined") {
			alert('No service call (GET/POST/PUT/DELETE) is selected');
			return;
		}
		//{"branchBean":{"branchId":25,"parentId":0,"bankName":"Test Bank","branchName":"Branch1","ifscCode":"12345678","micrCode":"87654321","email1":"ashismo@gmail.com","phone1":"9830625559","startDate":"Dec 22, 2015 10:43:30 PM","createUser":"ashish","addresses":[{"addressId":30,"addressName":"Test Address3","addressLine1":"Naldighi17","pin":"712304","distId":1,"phoneNo1":"9830525559","startDate":"Dec 22, 2015 10:43:30 PM","createUser":"ashish"}]}}

		
		//var input = "data:{ input:" +  input + "}";
		//console.log("input " + input); 
		if(method == 'POST') {
			$.ajax({
				 url: url,
				 type: "POST",
				 data: input,
				 dataType: "json",
				 contentType: "application/json",
				 beforeSend: function(xhr) {
					 xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password))
				 }
			}).done(function (data) {
				 console.log("Response " + JSON.stringify(data));
				 $('textarea[name=jsonOutput]').val(JSON.stringify(data));
			})
		} else if(method == 'GET') {
			$.ajax({
				 url: url,
				 type: "GET",
				 dataType: "json",
				 contentType: "application/json",
				 beforeSend: function(xhr) {
					 xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password))
				 }
			}).done(function (data) {
				 console.log("Response " + JSON.stringify(data));
				 $('textarea[name=jsonOutput]').val(JSON.stringify(data));
			})
		} else if(method == 'DELETE') {
			$.ajax({
				 url: url,
				 type: "DELETE",
				 data: input,
				 dataType: "json",
				 contentType: "application/json",
				 beforeSend: function(xhr) {
					 xhr.setRequestHeader("Authorization", "Basic " + btoa(username + ":" + password))
				 }
			}).done(function (data) {
				 console.log("Response " + JSON.stringify(data));
				 $('textarea[name=jsonOutput]').val(JSON.stringify(data));
			})
		}
		
		
    }
    </script>
</head>
<body>
    <form:form action="restTester" method="post" name="inputBean" commandName="inputBean">
	    <table>
	    	<tr><td><input type="text"  name="url" size="100" value="${inputBean.url}"/></td></tr>
	    	<tr><td>
	    		<form:radiobutton path="requestMethod" value="GET" />GET 
	    		<form:radiobutton path="requestMethod" value="POST" />POST
	    		<form:radiobutton path="requestMethod" value="DELETE" />DELETE
	    		<form:radiobutton path="requestMethod" value="PUT" />PUT
			</td></tr>
			<tr>
				<td>Username: <input  type="text" name="username" value="ashish"/></td>
			</tr>
			<tr>
				<td>Password: <input  type="password" name="password" value="ashish"/></td>
			</tr>
			<tr>
				<td><textarea  name="jsonInput" cols="100" rows="10">${inputBean.jsonInput}</textarea></td>
				<td><input type="button" id="iButton" value="Beautify" onclick="beautify('jsonInput');"></td>
			</tr>
	    	<tr>
	    		<td><textarea  name="jsonOutput" cols="100" rows="10">${inputBean.jsonOutput}</textarea></td>
	    		<td><input type="button" id="oButton" value="Beautify" onclick="beautify('jsonOutput');"></td>
	    	</tr>
	    	<tr>
	    		<td><input type="submit" value="Submit"></td>
	    		<td><input type="button" value="AjaxTest" onclick="serviceTest();"></td>
	    	</tr>
	    	
	    </table>
    </form:form>
    
    <br><br>
    <a href="jsonTester">Json Tester</a> </br>
    <a href="restTester">Webservice Tester</a>
</body>
</html>
