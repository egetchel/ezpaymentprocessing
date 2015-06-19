<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.ezpaymentprocessing.utils.PaymentProcessingConfigManager" %>
<%@ page import="java.util.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script src="//code.jquery.com/jquery-2.1.3.min.js"></script>
<title>Merchant Portal</title>
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
.upperCornersRound, .allCornersRound {
	border-top-left-radius: 0.5em;
	border-top-right-radius: 0.5em;
	-moz-border-radius-topleft: 0.5em;
	-moz-border-radius-topright: 0.5em;
	-webkit-border-top-left-radius: 0.5em;
	-webkit-border-top-right-radius: 0.5em;
}
.lowerCornersRound, .allCornersRound {
	border-bottom-left-radius: 0.5em;
	border-bottom-right-radius: 0.5em;
	-moz-border-radius-bottomleft: 0.5em;
	-moz-border-radius-bottomright: 0.5em;
	-webkit-border-bottom-left-radius: 0.5em;
	-webkit-border-bottom-right-radius: 0.5em;
}
</style>
</head>
<body>
<div class="wrapper">
<div class="black-border text-center upperCornersRound">
Merchant Portal Links
</div>
<div class="black-border lowerCornersRound">
<form id="purchaseForm">
<br/>
<table>

	<tr>
		<td>Available Merchants: </td>
		<td>
		
<%
// Because I don't want to fight JSTL tags when Static classes/methods are involved...

			Map <String,String> serverURLs = PaymentProcessingConfigManager.getMotenizationServerURLs();
			if (serverURLs != null)
			{
				for (String merchantId : serverURLs.keySet())
				{

					String url = serverURLs.get(merchantId);
					
					// Build a URL to the index.jsp page
					// need to handle both local (with port and context root):
					// http://localhost:8081/monetizationservice/rest/qualifyPromotion/
					// ... and OpenShift URLs
					// http://monetizationservice/rest/qualifyPromotion/
					
					// We want everything to the left of "rest"
					int endIndex = url.indexOf("rest");
					
					out.print("<a href=\"");
					out.print(url.substring(0, endIndex));
					out.print("\">");
					out.print(merchantId);
					out.print("</a><br>");
					
				}
			}

%>			
	
		</td>
	</tr>
	<tr><td><img src="images/Red_Hat_RGB_150px.jpg" align="right"/></td></tr>
</table>
</form>
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


</script>

</body>
</html>