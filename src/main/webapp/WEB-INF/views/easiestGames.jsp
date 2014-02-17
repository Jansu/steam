<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="false" %>
<jsp:include page="template/head.jsp"/>
        <div class="gamelist sortable">
            <c:forEach var="gameData" items="${games}">
            	<c:set var="g" value="${gameData[0]}"/>
            	<c:set var="achievPerc">
            		<fmt:formatNumber type="number" maxFractionDigits="0" value="${g.stats.achievPerc*100}" />
            	</c:set>
                <div title="${g.name}" id="${g.appId}" class="game ${g.stats.beaten or achievPerc == 100? 'beaten' : 'unbeaten'} ${not empty g.stats and g.stats.totalPlaytime != '0'? 'played' : 'unplayed'}"
                        data_name="${g.name}"
                        data_appId="${g.appId}"
						data_achievDone="${g.stats.achievDone}"
						data_achievPerc="${g.stats.achievPerc}"
						data_beaten="${g.stats.beaten}"
                        data_totalPlaytime="${g.stats.totalPlaytime}"
                        data_hoursPlayed="${g.stats.hoursPlayed}" 
                        data_playerCount="${g.stats.playerCount}"
                        data_hardestAchiev="${gameData[2]}"
                        data_avgAchiev="${gameData[1]}"
                        <c:if test="${not g.stats.beaten}">
	                   		style="background: linear-gradient(to right,  rgba(0,0,0,1) 0%,rgba(255,255,255,1) ${achievPerc}%, rgba(0,0,0,1) ${achievPerc}%, rgba(0,0,0,1) 100%);"
                        </c:if>
                >
                	<div class="gameTitle">
                		<a href="/web/game/${g.appId}">${g.name}</a>
                	</div>
                	<div class="gameTitle">
                		Avg: <fmt:formatNumber type="number" maxFractionDigits="1" value="${gameData[1]}" />% - Hardest: <fmt:formatNumber type="number" maxFractionDigits="1" value="${gameData[2]}" />%
                	</div>
                    <img title="${g.name}" alt="${g.name}" src="${g.logoUrl}" />
                    <div class="bottomBar">
                    	<div class="achievPercBar" style="width:${empty g.stats.achievPerc? '0' : g.stats.achievPerc*100}%;">
                    		<c:if test="${not empty g.stats.achievPerc and g.stats.achievPerc > 0.75}"><div class="hoursPlayed">${g.stats.totalPlaytimeFormatted}</div></c:if>
                   		</div>
                    	<c:if test="${not empty g.stats.achievPerc}">
	                    	<div class="achievStats">
		                    	${g.stats.achievDone} of ${fn:length(g.achievements)} - <fmt:formatNumber type="number" maxFractionDigits="0" value="${g.stats.achievPerc*100}" />%
	                    	</div>
                    	</c:if>
                    	<c:if test="${empty g.stats.achievPerc or (not empty g.stats.achievPerc and g.stats.achievPerc <= 0.75)}">
	                    	<div class="hoursPlayed">
		                    	${g.stats.totalPlaytimeFormatted}
	                    	</div>
                    	</c:if>
                    </div>
                    <div class="controls">
                    	<div class="control">
                    		<a class="" title="Play" href="steam://run/${g.appId}"><div class="button playButton"></div></a>
                    	</div>
	                    <div class="control">
        	            	<a class="" title="Steam Store" href="${g.shopUrl}"><div class="button shopButton"></div></a>
	                    </div>
	                    <div class="control">
    	                	<a class="" title="Update" href="javascript:update('${g.appId}')"><div class="button updateButton"></div></a>
	                    </div>
	                    <div class="control">
		                    <a class="" title="${g.stats.beaten? 'Unm' : 'M'}ark as beaten" href="javascript:toggleBeaten('${g.appId}')"><div class="button beatenButton"></div></a>
                    	</div>
                    </div>
                </div>
            </c:forEach>
        </div>
<jsp:include page="template/footer.jsp"/>