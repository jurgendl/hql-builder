function onSignIn(googleUser) {
	// Useful data for your client-side scripts:
	var profile = googleUser.getBasicProfile();
	// Don't send this directly to your server!
	console.log("ID: " + profile.getId());
	console.log("Name: " + profile.getName());
	console.log("Image URL: " + profile.getImageUrl());
	console.log("Email: " + profile.getEmail());
	// The ID token you need to pass to your backend:
	console.log("ID Token: " + googleUser.getAuthResponse().id_token);
};