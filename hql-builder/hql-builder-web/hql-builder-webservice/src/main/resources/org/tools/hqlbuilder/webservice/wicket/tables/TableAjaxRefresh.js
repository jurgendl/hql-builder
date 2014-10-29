/* http://kyleschaeffer.com/development/the-perfect-jquery-ajax-request/ */
/* http://stackoverflow.com/questions/377644/jquery-ajax-error-handling-show-custom-exception-messages */
/* http://code.tutsplus.com/tutorials/5-ways-to-make-ajax-calls-with-jquery--net-6289 */
/* http://www.javabeat.net/jquery-ajaxsetup-example/ */
/* http://www.javabeat.net/jquery-getjson-example/ */
$(document).ready(function() {
	$.each( tableAjaxRefresh, function( tableId, tableAjaxRefreshMeta ) {
		console.log(tableId+'::'+tableAjaxRefreshMeta['type']+'::'+tableAjaxRefreshMeta['url']+'::'+tableAjaxRefreshMeta['oidProperty']);
		var intervalID = self.setInterval( function() {
			$.ajax({
				type : tableAjaxRefreshMeta['type']
				,
				url : tableAjaxRefreshMeta['url']
				,
				dataType : 'json'
				,
				data : {
					time : $.now()
				}
				,
				beforeSend : function() {
					// nothing
				}
				,
				success : function(response) {
					$.each(response, function(rowIdx, record) {
						var oid = record[tableAjaxRefreshMeta['oidProperty']];
						var htmlRecNode = $('tr[data-id='+oid+']');
						$.each(tableAjaxRefreshMeta['config'], function(property, propertyConfigMap) {
							var value = record[property];
							if(value) {
								var col = propertyConfigMap['idx'];
								var dataNode = $(htmlRecNode.children()[col]).children('div');
								var currentValue = dataNode.html();
								if(currentValue!=value) {
									console.log(oid+':('+property+'='+col+'):'+currentValue+'>'+value);
									dataNode.empty().append(value);
									if(propertyConfigMap['data']){
										dataNode.attr('data-'+property, value);
										console.log(oid+':'+'data-'+property+'='+value);
									}
								}
							}
						});
					});
				}
				,
				error : function(xhr, ajaxOptions, thrownError) {
					window.clearInterval(intervalID);
					console.log(xhr.status+' - '+xhr.responseText+' - '+thrownError);
				}
			});
		}, tableAjaxRefreshMeta['refresh'] * 1000);
	});
});
