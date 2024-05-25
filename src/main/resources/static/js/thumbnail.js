
	$(document).ready(function() {
		$('#fileImage').change(function(){
			showImageThumbnail(this);
		});
		
		$('#primaryImage').change(function(){
			showImageThumbnail(this);
		});
		
		$("input[name='extraImage']").each( function(index){
			$(this).change(function(){
				showExtraImageThumbnail(this, index);
			})
		});
		
		
	});
	
	function showImageThumbnail(fileInput) {
		file =fileInput.files[0];
		reader = new FileReader();
		
		reader.onload = function(e) {
			$('#thumbnail').attr('src', e.target.result);
		};
		
		reader.readAsDataURL(file);
	} 
	
		
	function showExtraImageThumbnail(fileInput, index) {
		file =fileInput.files[0];
		reader = new FileReader();
		
		reader.onload = function(e) {
			$('#thumbnail'+index).attr('src', e.target.result);
		};
		
		reader.readAsDataURL(file);
	} 
	