
function actionShowPicture(image){
	$("#imgPicture").attr('src', '/images/loading.gif');
	preloadImage(image.midImage, function(){ $("#imgPicture").attr('src', image.midImage) });
	
	$("#tfPictureName").html(image.name);
	$("#btnDownloadPicture").attr('href', image.downloadLink);
	$("#dialogPicture").modal('show');
	
	actionRefreshPictureLink(document.getElementById("btnPriorPicture"), image.priorId);
	actionRefreshPictureLink(document.getElementById("btnNextPicture"), image.nextId);
}

function preloadImage(imgSrc, callback) {
	var objImagePreloader = new Image();

	objImagePreloader.src = imgSrc;
	if (objImagePreloader.complete) {
		callback();
		objImagePreloader.onload = function() {
		};
	} else {
		objImagePreloader.onload = function() {
			callback();
			objImagePreloader.onload = function() {
			};
		}
	}
}

function actionRefreshPictureLink(btn, id){
	visibility = id === "null" ? "hidden" : "visible";
	btn.style.visibility=visibility;
	btn.onclick = function() { 
			btn.blur();
			actionGoToPicture(id); 
		}
}

function actionGoToPicture(id){
	$("#imgPicture").attr('src', '/images/loading.gif');
	$.ajax({
		type : "GET"
		,contentType : "application/json"
		,url : "/rest/picture/" + id
		,success : function(data) {
			actionShowPicture(data);
		}
		, error : function(e) {
			console.log("ERROR: ", e);
		}
	});
	
}