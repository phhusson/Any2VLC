function retrieve_url(video_url, cb) {
	$.ajax({
		url: 'http://youtube-dl.appspot.com/api/',
		type: 'GET',
		data: {
			url: video_url,
		},
		dataType: 'json'
	}).success(function(data) {
		//Hum...
		if(data.videos == undefined ||
			data.videos[0] === undefined)
			return;

		var video_url = data.videos[0].url;
		var description = data.videos[0].description;
		var title = data.videos[0].title;
		console.log(data);

		cb(video_url, title);
	});
}
