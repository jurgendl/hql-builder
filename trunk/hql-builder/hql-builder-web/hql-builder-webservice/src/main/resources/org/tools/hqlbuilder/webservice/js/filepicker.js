function clearFileInput(compid) {
	var comp = $('#'+compid);
	comp.removeAttr('aria-invalid');
	comp.attr('class', 'filestyle');
	comp.removeAttr('value');
	var x = comp.clone();
	comp.replaceWith( x );
	alert(comp.id);
	alert(comp.parent().id);
	alert(comp.parent().find(".error").id);
	alert(comp.parent().find(".error").text());
	comp.parent().find(".error").remove();
	//$( "#"+compid ).rules('remove');
	//$( "#"+formid ).validate();
	//$( "#"+compid ).rules('add', { accept: "application/pdf" });
}
