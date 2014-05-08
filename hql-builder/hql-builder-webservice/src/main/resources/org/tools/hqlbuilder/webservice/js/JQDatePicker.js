function initJQDatepicker(inputId, countryIsoCode, dateFormat, calendarIcon) {
	var localizedArray = $.datepicker.regional[countryIsoCode];
	localizedArray['buttonImage'] = calendarIcon;
	localizedArray['dateFormat'] = dateFormat;
	initCalendar(localizedArray);
	$("#" + inputId).datepicker(localizedArray);
};
function initJQDatepicker(inputId, countryIsoCode, dateFormat) {
	var localizedArray = $.datepicker.regional[countryIsoCode];
	localizedArray['dateFormat'] = dateFormat;
	initCalendar(localizedArray);
	$("#" + inputId).datepicker(localizedArray);
};
function initCalendar(localizedArray){
	localizedArray['changeMonth']= true;
	localizedArray['changeYear']= true;
	localizedArray['showOn'] = 'button';
	localizedArray['buttonImageOnly'] = true;
};
