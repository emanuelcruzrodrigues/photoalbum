$(document).ready(function() {
	
	$("#dialogLogin").on('hide.bs.modal', function(e) {
		e.preventDefault();
	});
	
	$("#dialogLogin").modal('show');
});