 $(function() {
		/* For zebra striping */
		$("table tr:nth-child(odd)").addClass("odd");
		$("table tr:nth-child(even)").addClass("even");
		/* For cell text alignment */
		$("table td:first-child, table th:first-child").addClass("first");
		/* For removing the last border */
		$("table td:last-child, table th:last-child").addClass("last");
});