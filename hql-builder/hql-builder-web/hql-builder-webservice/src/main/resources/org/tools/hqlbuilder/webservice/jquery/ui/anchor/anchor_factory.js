function anchorfactory(prefix,time) {
	console.log('anchorfactory['+prefix+']');
	$(prefix+'a[href*=#]').anchor({ transitionDuration : time });
}
function anchorfactory(prefix) {
	anchorfactory(prefix,1500);
}
function anchorfactory() {
	anchorfactory('');
}
function anchorfactory(time) {
	anchorfactory('',time);
}
$(document).ready(function() {
	anchorfactory('');
});
