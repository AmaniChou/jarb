$(document).ready(function() {

	$.ajax('/posts', {
	    type : 'GET',
	    success : function(data) {
		    $('.posts tbody').empty();
		    $.each(data, function(index, post) {
			    appendPost(post);
		    });
	    }
	});

	var appendPost = function(post) {
		var newPost = '<tr>';
		newPost += '<td>' + post.title + '</td>';
		newPost += '<td class="author">' + post.author + '</td>';
		newPost += '<td class="date">' + post.postedOn + '</td>';
		newPost += '<td class="message">' + post.message + '</td>';
		newPost += '</tr>';
		$('.posts tbody').append(newPost);
	};

	var createForm = $('#create-post form');
	createForm.constraints('/posts/constraints');
	createForm.validate(); // Init validator

	createForm.submit(function(e) {
		var post = getNewPost();

		// Perform validation checks before posting
		var validator = createForm.validate();
		if (validator.form()) {
			// Send post to server
			$.ajax('/posts', {
			    type : 'POST',
			    contentType : 'application/json; charset=utf-8',
			    dataType : 'json',
			    data : JSON.stringify(post),
			    success : function(data) {
				    $('#status').text(data.message);

				    if (data.success) {
					    appendPost(data.post);

					    // Clear creation form
					    $(':input', '#create-post').not(':button, :submit, :reset, :hidden').removeAttr('checked').removeAttr('selected').val('');

					    validator.resetForm();
				    }
			    }
			});
		}
		return false;
	});

	function getNewPost() {
		var post = {};
		$.each(createForm.serializeArray(), function(index, property) {
			post[property.name] = property.value;
		});
		return post;
	}

});