<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
// This is a test page that allows debugging of various transport methods

// The endpoints need to be progromatically calculated depending on if we are running in OSE or locally
String paymentContextPath;
String promotionContextPath;
System.out.println("context path: " + request.getContextPath());
// Look for the two most common local host addresses.  Note, check the bind
// address if this does not submit locally as the machine may be using IPV6 or 
// a different address
if (request.getContextPath().startsWith("/"))
{
	paymentContextPath = "/ezpaymentprocessing/rest/";
	promotionContextPath = "/merchantservices/rest/"; 
}
else
{
	paymentContextPath = "http://ezpaymentprocessing-egetchel.rhcloud.com/rest/";
	promotionContextPath = "http://merchantservices-egetchel.rhcloud.com/rest/";
}


//contextPath="/merchantservices/rest/processPromotion";
//contextPath="/ezpaymentprocessing/rest/purchase";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
<title>REST Test App</title>
</head>
<body>
<div style="border:1px solid black">

<form id="purchaseForm">
	<input type="hidden" name="merchantId" id="merchantId" value="1">
	<select name="amount" id="amount">
  		<option value="10">$10</option>
  		<option value="20">$20</option>
  		<option value="30">$30</option>
	</select> 
	<button type="button" id="submitButtonRest" onclick="javascript:submitPost('<%=promotionContextPath%>processPromotion');">POST - Promotion</button>
	<button type="button" id="submitButtonRest" onclick="javascript:submitPost('<%=paymentContextPath%>purchase');">POST - Purchase</button>
</form>
Request:<div id="debug"></div>
<br><br> 
Response:<br>
<div id="result"></div>
<br>

</div>
<script type="text/javascript"> 

function submitPost(restEndpoint) 
{
	var frm = $("#purchaseForm");
	var data = JSON.stringify(frm.serializeObject());
	
	$("#debug").html('Endpoint: '+ restEndpoint + '<br>Data: ' + data );
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