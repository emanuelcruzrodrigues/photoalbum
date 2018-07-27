function actionShowPicture(id, name, imageSrc){
	$("#tfPictureName").html(name);
	$("#imgPicture").attr('src', imageSrc);
	$("#dialogPicture").modal('show');
	actionRefreshPrior(id);
	actionRefreshNext(id);
}

function actionRefreshPrior(id){
	document.getElementById("btnPriorPicture").onclick = function() { alert(id); }
}

function actionRefreshNext(id){
	document.getElementById("btnNextPicture").onclick = function() { alert(id); }
}