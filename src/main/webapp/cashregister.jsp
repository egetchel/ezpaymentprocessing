<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.ezpaymentprocessing.utils.PaymentProcessingConfigManager" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
<title>Ye Olde Cash Register</title>
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
Ye Olde Cash Register
</div>
<div class="black-border">
<form id="purchaseForm">
		<input type="hidden" name="mobileNumber" value="">
		<input type="hidden" name="merchantId" value="">

<br/>
<table>
	<tr>
		<td>Amount:</td>
		<td>	
			<input type="text" name="amount" id="amount"/>
		</td>
	 </tr>
	 <tr>
	 	<td>&nbsp;</td>
	 	<td>
	 		<button type="submit" id="submitButtonRest" onclick="javascript:submitPost();">Purchase (POST)</button>
		</td>
	</tr>
</table>
</form>
</div>
<div class="black-border">
<div id="result">&nbsp;</div>
</div>		

</div>
<script type="text/javascript"> 

function submitPost(restEndpoint) 
{
	var frm = $("#purchaseForm");
	var data = JSON.stringify(frm.serializeObject());
	$( "#result" ).html("");
	$.ajax({
		  url:"<%=PaymentProcessingConfigManager.getPaymentProcessingURL()%>",
		  type:"POST",
		  data:data,
		  contentType:"application/json; charset=utf-8",
		  dataType:"json",
		  success: function(response)
		  {
			  //alert(response);
			  var approved = response.approved;
			  var message  = response.message;
			  var consoleMessage;
			  if (approved) {
				consoleMessage = "Your purchase for $"+ $( "#amount").val() +" was approved";
			  }
			  else
			  {
				  consoleMessage = message;
			  }
 
			  $( "#result" ).html( consoleMessage );

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

$(document).keypress(function(event){

	if(event.keyCode == 13) 
	{
    	submitPost();
        return false;
		
    }
    }
);
</script>

</body>
</html>