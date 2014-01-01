var base = "http://"+localStorage["server"] + ":" + localStorage["port"] + localStorage["proxy"];

function add_password(xhr) {
	// generate base 64 string from username + password
	var base64 = btoa(":"+localStorage["pass"]);
	// set header
	xhr.setRequestHeader("Authorization", "Basic " + base64);
}

function vlc_send(type, url, name) {
	console.log("Send  " + type + " @" + url);
	$.ajax({
		data: {
			command: 'in_'+type,
			input: url,
			name: name,
		},
		url: base,
		method: 'GET',
		beforeSend : add_password,
	});
}

function vlc_add(video_url, title) {
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
