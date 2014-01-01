document.addEventListener('DOMContentLoaded', function () {
	chrome.tabs.query({
		currentWindow: true,
		active: true },
		function (tabs) {
			chrome.runtime.sendMessage({
				video_url: tabs[0].url},
				function(response) {
					console.log("message sent");
					window.close();
				});
			});
		});
