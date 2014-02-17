var m = [29, 20, 20, 19], // top right bottom left margin
	w = 620 - m[1] - m[3], // width
	h = 90 - m[0] - m[2], // height
	z = 11; // cell size

var day = d3.time.format("%w"),
	week = d3.time.format("%U"),
	percent = d3.format(".1%"),
	myData,
	flightData,
	stockData,
	formatDate = d3.time.format("%Y-%m-%d"),
	formatNumber = d3.format(",d"),
	formatPercent = d3.format("+.1%");

$(document).ready(function() {
	d3.json("/web/ajax/stat/achievCountByDayOfWeek", function(json) {
		$.each(json, function(index, value) {$('.achievsByDayOfWeek').append('<div>'+value+'</div>');})
//		drawBars('#achievsByDayOfWeek', json);
	});

	d3.json("/web/ajax/stat/achievCountByHourOfDay", function(json) {
		$.each(json, function(index, value) {$('.achievsByHourOfDay').append('<div>'+value+'</div>');})
//		drawBars('#achievsByHourOfDay', json);
	});
	
	d3.json("/web/ajax/stat/achievCountByDay", function(json) {
		myData = json;
		flightData = d3.nest()
		.key(function(d) { 
				var newDate = new Date(d.date);
				return newDate.getFullYear(); 
		})
		.key(function(d) { return d.date; })
		.rollup(function(d) { return +d[0].count; })
		.map(json.days);
		
		display(flightData);
	});

	d3.json("/web/ajax/stat/gameCount", function(json) {
		drawPie('#gameCount', json);
	});

	d3.json("/web/ajax/stat/achievCount", function(json) {
		drawPie('#achievCount', json);
	});
});

function drawPie(selector, data) {
	var width = 600,
	    height = 500,
	    radius = Math.min(width, height) / 2;

	var color = d3.scale.ordinal()
	    .range(["#44AA44", "#4444AA", "#AA4444"]);

	var arc = d3.svg.arc()
	    .outerRadius(radius - 10)
	    .innerRadius(radius - 90);

	var pie = d3.layout.pie()
	    .sort(null)
	    .value(function(d) { return d.value; });

	var svg = d3.select(selector).append("svg")
	    .attr("width", width)
	    .attr("height", height)
	  .append("g")
	    .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");

	  data.forEach(function(d) {
		  d.value = +d.value;
	  });

	  var g = svg.selectAll(" .arc")
	      .data(pie(data))
	    .enter().append("g")
	      .attr("class", "arc");

	  g.append("path")
	      .attr("d", arc)
	      .style("fill", function(d) { return color(d.value); });

	  g.append("text")
	      .attr("transform", function(d) { return "translate(" + arc.centroid(d) + ")"; })
	      .attr("dy", ".5em")
	      .style("text-anchor", "middle")
	      .text(function(d) { console.log(d); return d.data.key + ': ' + d.value; });
}

function display(data) {

	var svg = d3.select("#calendar").selectAll(".year")
	    .data(d3.range(myData.firstYear, myData.lastYear+1))
	  .enter().append("div")
	    .attr("class", "year")
	    .style("width", w + m[1] + m[3] + "px")
	    .style("height", h + m[0] + m[2] + "px")
//	    .style("display", "inline-block")
	  .append("svg:svg")
	    .attr("width", w + m[1] + m[3])
	    .attr("height", h + m[0] + m[2])
	    .attr("class", "Greys")
	  .append("svg:g")
	    .attr("transform", "translate(" + (m[3] + (w - z * 53) / 2) + "," + (m[0] + (h - z * 7) / 2) + ")");

	svg.append("svg:text")
	    .attr("transform", "translate(-6," + z * 3.5 + ")rotate(-90)")
	    .attr("text-anchor", "middle")
	    .attr("class", "white")
	    .text(String);

	var rect = svg.selectAll("rect.day")
	    .data(function(d) { return d3.time.days(new Date(d, 0, 1), new Date(d + 1, 0, 1)); })
	  .enter().append("svg:rect")
	    .attr("class", "day")
	    .attr("width", z)
	    .attr("height", z)
	    .attr("x", function(d) { return week(d) * z; })
	    .attr("y", function(d) { return day(d) * z; });

	rect.append("svg:title");

	svg.selectAll("path.month")
	    .data(function(d) { return d3.time.months(new Date(d, 0, 1), new Date(d + 1, 0, 1)); })
	  .enter().append("svg:path")
	    .attr("class", "month")
	    .attr("d", monthPath);
	
	
	var formatValue = formatNumber,
	    color = d3.scale.quantize();

  svg.each(function(year) {
    color
        .domain(d3.values(data[year]))
        .range(d3.range(100));

    d3.select(this).selectAll("rect.day")
//        .attr("class", function(d) { 
//        	return "day q" + color(data[year][d.getTime()]) + "-9"; 
//        })
        .attr("style", function(d) { 
        	return "opacity:" + opacity(data[year][d.getTime()]) + ";"; 
        })
      .select("title")
        .text(function(d) { 
        	return formatDate(d) + ": " + formatValue(data[year][d.getTime()]); 
        });
  });
}

function monthPath(t0) {
  var t1 = new Date(t0.getFullYear(), t0.getMonth() + 1, 0),
      d0 = +day(t0), w0 = +week(t0),
      d1 = +day(t1), w1 = +week(t1);
  return "M" + (w0 + 1) * z + "," + d0 * z
      + "H" + w0 * z + "V" + 7 * z
      + "H" + w1 * z + "V" + (d1 + 1) * z
      + "H" + (w1 + 1) * z + "V" + 0
      + "H" + (w0 + 1) * z + "Z";
}

function opacity(value) {
	if (value > 29) {
		return 0.1;
	}
	if (value > 24) {
		return 0.2;
	}
	if (value > 19) {
		return 0.3;
	}
	if (value > 15) {
		return 0.4;
	}
	if (value > 11) {
		return 0.5;
	}
	if (value > 7) {
		return 0.6;
	}
	if (value > 3) {
		return 0.7;
	}
	if (value > 1) {
		return 0.8;
	}
	if (value > 0) {
		return 0.9;
	}
	return 1.0;
}

function drawBars(targetSelector, data) {
	 var x = d3.scale.linear()
     .domain([0, d3.max(data)])
     .range(["0px", "420px"]);
	 var y = d3.scale.ordinal()
	  .domain(data)
	  .rangeBands([0, 120]);
	var chart = d3.select(targetSelector).append("svg")
	.attr("class", "chart")
	.attr("width", 440)
	.attr("height", 140)
	.style("margin-left", "32px") // Tweak alignment…
	.append("g")
	.attr("transform", "translate(10,15)");
	
	chart.selectAll("line")
	.data(x.ticks(10))
	.enter().append("line")
	.attr("x1", x)
	.attr("x2", x)
	.attr("y1", 0)
	.attr("y2", 120)
	.style("stroke", "#ccc");
	
	chart.selectAll(".rule")
	.data(x.ticks(10))
	.enter().append("text")
	.attr("class", "rule")
	.attr("x", x)
	.attr("y", 0)
	.attr("dy", -3)
	.attr("text-anchor", "middle")
	.text(String);
	
	chart.selectAll("rect")
	.data(data)
	.enter().append("rect")
	.attr("y", y)
	.attr("width", x)
	.attr("height", y.rangeBand());
	
	chart.selectAll(".bar")
	.data(data)
	.enter().append("text")
	.attr("class", "bar")
	.attr("x", x)
	.attr("y", function(d) { return y(d) + y.rangeBand() / 2; })
	.attr("dx", -3)
	.attr("dy", ".35em")
	.attr("text-anchor", "end")
	.text(String);
	
	chart.append("line")
	.attr("y1", 0)
	.attr("y2", 120)
	.style("stroke", "#000");
}
