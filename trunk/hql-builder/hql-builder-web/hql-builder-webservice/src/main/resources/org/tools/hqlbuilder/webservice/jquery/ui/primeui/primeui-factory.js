function primeuifactory(prefix) {
	$(prefix+'.puiinputtext').puiinputtext();
	$(prefix+'.puiinputtextarea').puiinputtextarea();
	$(prefix+'.puidropdown').puidropdown();
	$(prefix+'.puiaccordion').puiaccordion();
	$(prefix+'.puipassword').puipassword();
	$(prefix+'.puispinner').puispinner();
	$(prefix+'.puilistbox').puilistbox();
	$(prefix+'.puiautocomplete').puiautocomplete();
	$(prefix+'.puibreadcrumb').puibreadcrumb();
	$(prefix+'.puibutton').puibutton();
	$(prefix+'.puicheckbox').puicheckbox();
	$(prefix+'.puifieldset').puifieldset();
	$(prefix+'.puigalleria').puigalleria();
	$(prefix+'.puilightbox').puilightbox();
	$(prefix+'.puimenu').puimenu();
	$(prefix+'.puimenubar').puimenubar();
	$(prefix+'.puicontextmenu').puicontextmenu();
	$(prefix+'.puislidemenu').puislidemenu();
	$(prefix+'.puitieredmenu').puitieredmenu();
	$(prefix+'.puipaginator').puipaginator();
	$(prefix+'.puipanel').puipanel();
	$(prefix+'.puipicklist').puipicklist();
	$(prefix+'.puiprogressbar').puiprogressbar();
	$(prefix+'.puiradiobutton').puiradiobutton();
	$(prefix+'.puisticky').puisticky();
	$(prefix+'.puitabview').puitabview();
	$(prefix+'.puigrowl').puigrowl();
	$(prefix+'.puinotifytop').puinotify({easing: 'easeInOutCirc', position: 'top'});
	$(prefix+'.puinotifybottom').puinotify({easing: 'easeInOutCirc', position: 'bottom'});
	$(prefix+'.puidropdownfiltered').puidropdown({ filter: true, filterMatchMode: 'contains'});	
}
$( document ).ready(function() {
	primeuifactory('');
});