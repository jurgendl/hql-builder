$(function() {
	/* For zebra striping */
	$("table tr:nth-child(odd)").addClass("odd");
	$("table tr:nth-child(even)").addClass("even");
	/* For cell text alignment */
	$("table td:first-child, table th:first-child").addClass("first");
	/* For removing the last border */
	$("table td:last-child, table th:last-child").addClass("last");
});

$(function() {
	var top = $('#topbarfloating').offset().top - parseFloat($('#topbarfloating').css('marginTop').replace(/auto/, 100));
	$(window).scroll(function(event) {
		// what the y position of the scroll is
		var y = $(this).scrollTop();
		// whether that's below the form
		if (y >= top) {
			// if so, ad the fixed class
			$('#topbarfloating').addClass('fixed');
		} else {
			// otherwise remove it
			$('#topbarfloating').removeClass('fixed');
		}
	});
});

$(function() {
	var bottom = $('#bottombarfloating').offset().bottom - parseFloat($('#bottombarfloating').css('marginBottom').replace(/auto/, 100));
	$(window).scroll(function(event) {
		// what the y position of the scroll is
		var y = $(this).scrollBottom();
		// whether that's below the form
		if (y >= bottom) {
			// if so, ad the fixed class
			$('#bottombarfloating').addClass('fixed');
		} else {
			// otherwise remove it
			$('#bottombarfloating').removeClass('fixed');
		}
	});
});