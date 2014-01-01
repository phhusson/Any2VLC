// Saves options to localStorage.
function save_options() {
	var args = [ "server", "port", "proxy", "pass"];
	for(var i in args) {
		console.log(args[i]);
		console.log(document.getElementById(args[i]));
		localStorage[args[i]] = document.getElementById(args[i]).value;
	}

	// Update status to let user know options were saved.
	var status = document.getElementById("status");
	status.innerHTML = "Options Saved.";
	setTimeout(function() {
		status.innerHTML = "";
	}, 750);
}

// Restores select box state to saved value from localStorage.
function restore_options() {
	var args = [ "server", "port", "proxy", "pass"];
	for(var i in args) {
		if(localStorage[args[i]] === undefined)
			continue;
		document.getElementById(args[i]).value = localStorage[args[i]];
	}
}
document.addEventListener('DOMContentLoaded', restore_options);
document.querySelector('#save').addEventListener('click', save_options);
