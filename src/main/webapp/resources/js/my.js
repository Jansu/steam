$(document).ready(function() {
//	alert("doc ready start");
	$(".game").hover(
	  function () {
		  if (isGridView()) {
			  $(this).find(".controls").show();
		  }
	  },
	  function () {
		  if (isGridView()) {
			  $(this).find(".controls").hide();
		  }
	  }
	);

	$(".flyout").hover(
	  function () {
		  $(this).find(".outflyer").show();
	  },
	  function () {
		  $(this).find(".outflyer").hide();
	  }
	);
	
	$("#searchBox").bind("keyup", function() {
        var searchString = $(this).val();
        if (searchString.length > 0) {
            $(".searchable").children().each(function() {
                var item = $(this).attr("data_name");
                (item.toLowerCase().indexOf(searchString.toLowerCase()) != -1)? $(this).show() : $(this).hide();
            });
        }
        else {
            $(".searchable").children().show();
        }        
    });
	
	$('.filterContainer .control').change(function() {
		var filter = $(this).val();
		if ($(this).is(":checked")) {
			$(".filterable." + filter).hide();
		} else {
			$(".filterable." + filter).show();
		}
	});

	shortcut.add("Ctrl+F",function() {
		$('#searchBox').focus();
	});
	
//	alert("doc ready end");
});

function isGridView() {
	return !$('body').hasClass('listLayout');
}

function sort(criteria) {
	var sortOrder = $.sortChildren.reverse;
	if ($('.sortOption_' + criteria).hasClass('reverse')) {
		sortOrder = null;
	}
	$('.sortOption_' + criteria).toggleClass('reverse');
	
	$('.sortable').sortChildren(function(ele) {
		var value = $(ele).attr('data_' + criteria);
		var asInt = parseInt(value);
		return asInt? asInt : value;
	}, sortOrder);
	$('#currentSorting').text($('.sortOption_' + criteria).text());
}

function toggleBeaten(appid) {
	$.getJSON("/web/ajax/toggleBeaten/" + appid, function(data) {
		$('#'+appid).toggleClass('beaten', data);
	});
}

function update(appid) {
	$.getJSON("/web/update/" + appid, function(data) {
		// TODO update data saved in DOM
	});
}

function toggleLayout() {
	$("body").toggleClass("listLayout");
}