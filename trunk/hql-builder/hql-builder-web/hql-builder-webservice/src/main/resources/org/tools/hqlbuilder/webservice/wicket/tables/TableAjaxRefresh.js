/* http://kyleschaeffer.com/development/the-perfect-jquery-ajax-request/ */
/* http://stackoverflow.com/questions/377644/jquery-ajax-error-handling-show-custom-exception-messages */
/* http://code.tutsplus.com/tutorials/5-ways-to-make-ajax-calls-with-jquery--net-6289 */
/* http://www.javabeat.net/jquery-ajaxsetup-example/ */
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
					var tableMap = tableAjaxRefreshMeta['struct'];
					var hasDataMap = tableAjaxRefreshMeta['data'];
					var oidProperty = tableAjaxRefreshMeta['oidProperty'];
					$.each(response, function(rowIdx, record) {
						var oid = record[oidProperty];
						var recSelector = 'tr[data-id='+oid+']';
						var htmlRecNode = $(recSelector);
						$.each(tableMap, function(property, col) {
							var data = record[property];
							if(data) {
								var dataNode = $(htmlRecNode.children()[col]).children('div');
								var currV = dataNode.html();
								console.log(oid+':('+property+'='+col+'):'+currV+'>'+data);
								dataNode.empty().append(data);
								if(hasDataMap[property]){
									dataNode.attr('data', data);
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
