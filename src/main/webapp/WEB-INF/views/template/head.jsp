<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <title>Home</title>
    <link href="/web/resources/css/styles.css" rel="stylesheet" type="text/css" />
    <link href="/web/resources/css/charts.css" rel="stylesheet" type="text/css" />
    <link href="/web/resources/css/colorbrewer.css" rel="stylesheet" type="text/css" />
    <link href="/web/resources/css/ui-darkness/jquery-ui-1.10.3.custom.css" rel="stylesheet" type="text/css" />
    
<!--     <link href="resources/css/print.css" media="print" rel="stylesheet" type="text/css" /> -->

	<script type="text/javascript" src="/web/resources/js/jquery-1.9.1.js"></script>
	<script type="text/javascript" src="/web/resources/js/jquery-ui-1.10.3.custom.js"></script>
    <script type="text/javascript" src="/web/resources/js/jquery-sortChildren.js"></script>
    <script type="text/javascript" src="/web/resources/js/shortcut.js"></script>
    <script type="text/javascript" src="/web/resources/js/d3.v3.js" charset="utf-8"></script>
    <script type="text/javascript" src="/web/resources/js/stats.js"></script>
    <script type="text/javascript" src="/web/resources/js/my.js"></script>
</head>
<body>
    <div class="head">
        <div class="navTitel flyout">
	        <h1 class="">
	            <a title="Toggle Layout" href="/web/games">Games</a>
	        </h1>
	        <ul class="outflyerContainer">
	        	<li class="outflyer hide"><a title="Easiest Games" href="/web/games/easiest">Easiest Games</a></li>
	        	<li class="outflyer hide"><a title="Easiest Games Overall" href="/web/games/easiestOverall">Easiest Games Overall</a></li>
	        </ul>
	    </div>
        <div class="navTitel flyout">
	        <h1 class="">
	            <a title="Stats" href="/web/stats">Stats</a>
	        </h1>
        </div>
        <div class="navTitel flyout">
	        <h1 class="">
	            <a title="Achievements" href="/web/timeline/0">Achievements</a>
	        </h1>
	        <ul class="outflyerContainer">
	            <li class="outflyer hide"><a title="Achievement Timeline" href="/web/timeline/0">Achievement Timeline</a></li>
	        	<li class="outflyer hide"><a title="Easiest locked Achievements" href="/web/achievements/easiest/0">Easiest locked Achievements</a></li>
	        	<li class="outflyer hide"><a title="Hardest unlocked Achievements" href="/web/achievements/hardest/0">Hardest unlocked Achievements</a></li>
	        </ul>
        </div>
        <div class="navTitel flyout">
	        <h1 class="">
	        	<a title="Update recently played Games" href="/web/update/recent">Update</a>
	        </h1>
	        <ul class="outflyerContainer">
	        	<li class="outflyer hide"><a title="Update recently played Games" href="/web/update/recent">Update recently played Games</a></li>
	        	<li class="outflyer hide"><a title="Update Everything" href="/web/update/all">Update Everything</a></li>
	        	<li class="outflyer hide"><a title="Update all Metascores" href="/web/update/allMetaScores">Update all Metascores</a></li>
	        	<li class="outflyer hide"><a title="Update all to beat Times" href="/web/update/allHowLongToBeat">Update all to beat Times</a></li>
	        </ul>
        </div>
        <c:if test="${not empty sorterOptions}">
			<div class="filterContainer">
				<div class="filter">
					<label for="hideBeaten">Hide Beaten</label><input id="hideBeaten" name="hideBeaten" class="control" type="checkbox" value="beaten" />
				</div>
				<div class="search">
					<label for="searchBox">Search:</label><input id="searchBox" name="searchBox" type="text" />
				</div>
				<div class="sorter flyout">
					<span class="sorterTitle">Sort by </span>
					<span id="currentSorting">Nothing</span> 
			        <ul class="sortOptionContainer outflyerContainer">
			        <c:forEach var="option" varStatus="status" begin="0" end="${fn:length(sorterOptions)-1}" step="2">
						<li class="outflyer hide sortOption sortOption_${sorterOptions[status.index]}"><a href="javascript:sort('${sorterOptions[status.index]}')">${sorterOptions[status.index+1]}</a></li>
			        </c:forEach>
			        </ul>
				</div>
		    </div>
        </c:if>
    </div>
    <div class="content">