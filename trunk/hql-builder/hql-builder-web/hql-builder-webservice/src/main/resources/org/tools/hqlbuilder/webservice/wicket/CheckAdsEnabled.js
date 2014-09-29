if($("#check\\.ads img").css('display') == "none") {
	$('#check\\.ads').append($('<div>')
		.attr('class', 'ui-state-highlight ui-corner-all')
		.attr('style', 'margin:14px;padding-left:20px;padding-right:20px;z-index:250')
		.html('<p>Ad block is installed and active. Please support us by disabling it</p>'));
};