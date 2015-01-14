function anchorfactory(prefix) {
	console.log('anchorfactory['+prefix+']');
    $(prefix+'a[href*=#]').anchor({
        transitionDuration : 1500
    });
}
$(document).ready(function() {
	anchorfactory('');
});
