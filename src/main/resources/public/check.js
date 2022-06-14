function checkCokkies(){
	
	console.log("Entered Check Cokkies In User Registration");
	
	console.log("UserId Cokkiee : "+getCookie1("userId"));
	console.log("sessionUUid Cokkiee : "+getCookie1("sessionUUid"));
	
	if(getCookie1("sessionUUid") != null && getCookie1("userId") != null && getCookie1("sessionUUid") != "" && getCookie1("userId") != ""){
		
		//var track = "?trackId=" + getCookie1("sessionUUid")+"&userId="+getCookie1("userId");
		//window.location.href = "http://150.136.116.225:30998/user/openregister"+track;
		
	} else {
		alert("Invalid Session - Kindly Login");
		window.location.href = "http://150.136.116.225:30996/login.html";
	}
}

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

function getUserList() {
	
	
	if(getCookie1("sessionUUid") != null && getCookie1("userId") != null && getCookie1("sessionUUid") != "" && getCookie1("userId") != ""){
		
		var track = "?trackId=" + getCookie1("sessionUUid")+"&userId="+getCookie1("userId");
		window.location.href = "http://152.70.192.169:8183/user/openUserList"+track;
		
	} else {
		alert("Invalid Session - Kindly Login");
		window.location.href = "http://150.136.116.225:30996/login.html";
	}

}
