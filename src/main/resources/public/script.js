$(document).ready(function() {
    $("#submitButton").click(function(e) {
	e.preventDefault();
	
	/*var user = {
        id: "",
        email: $("#email").val(),
        firstName: $("#firstName").val(),
        lastName: $("#lastName").val(),
        phone: $("#phone").val(),
        pwd: $("#pwd").val()
    };*/
 if(getCookie1("sessionUUid") != null && getCookie1("userId") != null && getCookie1("sessionUUid") != "" && getCookie1("userId") != ""){
	
        // using serialize function of jQuery to get all values of form
        var serializedData = $("#registrationForm").serialize();
 		var combined = "email=" + $("#email").val() + "&" + "pwd=" + $("#pwd").val()+"&" + "firstName=" + $("#firstName").val()+"&" + "lastName=" + $("#lastName").val()+"&" + "phone=" + $("#phone").val();
		    	
        // Variable to hold request
        var request;
        // Fire off the request to process_registration_form.php
        request = $.ajax({
            url: "/user/register",
            type: "POST",
            data: combined
           // data: JSON.stringify(serializedData)
        });
 
        // Callback handler that will be called on success
        request.done(function(jqXHR, textStatus, response) {
            // you will get response from your php page (what you echo or print)
             // show successfully for submit message
            console.log("at done"+jqXHR.responseText);
            location.href = "/regSuccess.html";
        });
 
        // Callback handler that will be called on failure
        request.fail(function(jqXHR, textStatus, errorThrown) {
            // Log the error to the console
            // show error
            $("#result").html('There is some error while submit');
            console.error(
                "The following error occurred: " +
                textStatus, errorThrown
            );
        });
 } else {
	
	alert("Invalid Session - Kindly Login");
	window.location.href = "http://152.70.192.169:8080/login.html";
}
       // return false;
        
        function getCookie1(cname) {
			let name = cname + "=";
			let decodedCookie = decodeURIComponent(document.cookie);
			let ca = decodedCookie.split(';');
			for (let i = 0; i < ca.length; i++) {
				let c = ca[i];
				while (c.charAt(0) == ' ') {
					c = c.substring(1);
				}
				if (c.indexOf(name) == 0) {
					return c.substring(name.length, c.length);
				}
			}
			return "";
		}

 
    });
});