
function add_password(xhr) {
	// generate base 64 string from username + password
	var base64 = btoa(":"+localStorage["pass"]);
	// set header
	xhr.setRequestHeader("Authorization", "Basic " + base64);
}

function vlc_send(type, url, name) {
	var base = "http://"+localStorage["server"] + ":" + localStorage["port"] + localStorage["proxy"];
	console.log("Send  " + type + " @" + url);
	var req_uri = base + "?command=in_"+type;
	req_uri = req_uri + "&input="+encodeURIComponent(url);
	req_uri = req_uri + "&name="+encodeURIComponent(name);
	$.ajax({
		url: req_uri,
		method: 'GET',
		beforeSend : add_password,
	});
}

function vlc_add(video_url, title) {
	var base = "http://"+localStorage["server"] + ":" + localStorage["port"] + localStorage["proxy"];
	console.log("Sending " + video_url);
	$.ajax({
		url: base,
		dataType: "xml",
		beforeSend : add_password,
	}).success(function(data) {
		console.log(data);
		var state = $(data).find('state').text();
		console.log("state = " + state);
		if(state == "stopped") {
			vlc_send('play', video_url, title);
		} else {
			vlc_send('enqueue', video_url, title);
		}
	}).fail(function(a,b,c) {
		vlc_send('enqueue', video_url, title);
	});
}
