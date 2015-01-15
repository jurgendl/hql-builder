function anchorfactory(prefix,time) {
	console.log('anchorfactory['+prefix+']');
	$(prefix+'a[href*=#]').anchor({ transitionDuration : time });
}
$(document).ready(function() {
	anchorfactory('',1500);
});
