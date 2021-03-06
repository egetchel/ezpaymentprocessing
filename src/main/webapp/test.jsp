<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.ezpaymentprocessing.utils.PaymentProcessingConfigManager" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
<title>REST Endpoint Test Application</title>
<style>
body 
{
	font-family: Arial;
	font-size: 1em;
}
input, textarea, select
{
	font-family: Arial;
	font-size: 1em;

}
.black-border
{
	 border: 1px solid black;
	 padding: 5px 5px 5px 5px;
	 
}
.center
{
	align:center;
}
.text-center
{
	text-align:center;
}
.wrapper
{
	width:600px;
}
</style>
</head>
<body>
<div class="wrapper">
<div class="black-border text-center">
REST Endpoint Test Application
</div>
<div class="black-border">
<form id="purchaseForm">
<br/>
<table>
	<tr>
		<td>Amount:</td>
		<td>	
			<select name="amount" id="amount">
	  		<option value="10">$10</option>
	  		<option value="20">$20</option>
  			<option value="30">$30</option>
			</select>
		</td>
	 </tr>
	<tr>
		<td>Mobile:</td>
		<td><input type="text" name="mobileNumber" id="mobileNumber" value="5556667777"></td>
	</tr>
	<tr>
		<td>Selected Endpoint: </td>
		<td>
		<select name="merchantId" onchange="javascript:changeEndpoint();">
<%
// Because I don't want to fight JSTL tags when Static classes/methods are involved...
			Map <String,String> serverURLs = PaymentProcessingConfigManager.getMotenizationServerURLs();
			if (serverURLs != null)
			{
				for (String merchantId : serverURLs.keySet())
				{
					out.print("<option value=\"");
					out.print(merchantId);
					out.print("\">");
					out.print(merchantId);
					out.print("</option>");
				}
			}

%>			
		</select>
		
		</td>
	</tr>
	 <tr>
	 	<td>&nbsp;</td>
	 	<td>
	 	<%
	 	if (serverURLs != null && serverURLs.size() > 0)
	 	{
	 	%>
	 		<button type="button" id="submitButtonRest" onclick="javascript:submitPost('<%=PaymentProcessingConfigManager.getPaymentProcessingURL()%>');">Purchase (POST)</button>
	 	<%
	 	}
	 	else
	 	{
	 	%> No Registered Services. Please spin up remote infrastructure...
	 	<%
	 	}
	 	%>
	 	
		</td>
	</tr>
</table>
</form>
</div>
<div class="black-border">
<table>
	<tr>
		<td>Available Endpoints:
		</td>
		<td> 
		</td>
	<tr>
		<td>Endpoint:</td>
		<td><div id="endpoint"></div></td>
	</tr>
	<tr>
		<td>Request Data:</td>
		<td><div id="requestData"></div></td>
	</tr>
	<tr>
		<td>Response Data:</td>
		<td><div id="result"></div></td>
	</tr>
</table>
</div>		

</div>
</div>
<script type="text/javascript"> 
function changeEndpoint()
{
	var selectedText = $( "#myselect option:selected" ).text();
	
}
function submitPost(restEndpoint) 
{
	var frm = $("#purchaseForm");
	var data = JSON.stringify(frm.serializeObject());
	$( "#result" ).html("");
	$("#endpoint").html(restEndpoint);
	$("#requestData").html( data );
	$.ajax({
		  url:restEndpoint,
		  type:"POST",
		  data:data,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  success: function(response)
		  {
			  //alert(response);
			  $( "#result" ).html( JSON.stringify(response) );
		  }
		});

}

$.fn.serializeObject = function()
{
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

//Note, this sends with Content-Type of application/x-www-form-urlencoded; charset=UTF-8
//Which causes JAX-RS to throw an error.
// I have left it in for research purposes
function submitRest_Old()
{
	var frm = $("#purchaseForm");
	var data = JSON.stringify(frm.serializeObject());
	$.post('<%=request.getContextPath()%>/rest/purchase', data , function(response) {
		// Do something with the request
		alert("hello");
		}, 'json');
}

</script>

</body>
</html>