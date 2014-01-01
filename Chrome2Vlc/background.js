function genericOnClick(info, tab) {
  console.log("Click on " + info.linkUrl);
  retrieve_url(info.linkUrl, vlc_add);
}

var id = chrome.contextMenus.create({
	"title": "Ajouter à la liste de lecture vlc",
	"contexts": ["link"],
	"onclick": genericOnClick
});

chrome.runtime.onMessage.addListener(
	function(request, sender, sendResponse) {
		console.log(sender.tab ?
			"from a content script:" + sender.tab.url :
			"from the extension");
		console.log("Got msg "+JSON.stringify(request));

		retrieve_url(request.video_url, vlc_add);
		sendResponse({started: true});
	});
