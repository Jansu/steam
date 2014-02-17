<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@tag description="Game template" pageEncoding="UTF-8"%>
<%@tag import="de.druz.web.persistance.model.Game" %>
<%@attribute name="game" required="true" type="de.druz.web.persistance.model.Game"%>

<c:set var="g" value="${game}"/>

            	<c:set var="achievPerc">
            		<fmt:formatNumber type="number" maxFractionDigits="0" value="${g.stats.achievPerc*100}" />
            	</c:set>
                <div title="${g.name}" id="${g.appId}" class="game filterable ${g.stats.beaten or achievPerc == 100? 'beaten' : 'unbeaten'} ${not empty g.stats and g.stats.totalPlaytime != '0'? 'played' : 'unplayed'}"
                        data_name="${g.name}"
                        data_appId="${g.appId}"
                        data_achievCount="${fn:length(g.achievements)}"
						data_achievDone="${g.stats.achievDone}"
                        data_achievLeft="${fn:length(g.achievements)-g.stats.achievDone}"
						data_achievPerc="${g.stats.achievPerc}"
                        data_hasAchiev="${not empty g.achievements}"
						data_beaten="${g.stats.beaten}"
                        data_totalPlaytime="${g.stats.totalPlaytime}"
                        data_hoursPlayed="${g.stats.hoursPlayed}"
                        data_minutesToBeat="${g.stats.minutesToBeat}"
                        data_minutesToBeatRemaining="${g.stats.minutesToBeat-g.stats.totalPlaytime}"
                        data_minutesToBeatCompletely="${g.stats.minutesToBeatCompletely}"
                        data_minutesToBeatCompletelyRemaining="${g.stats.minutesToBeatCompletely-g.stats.totalPlaytime}"
                        data_metaScore="${g.stats.metaScore}"
                        data_userMetaScore="${g.stats.userMetaScore*10}"  
                        data_playerCount="${g.stats.playerCount}"
                        <c:if test="${not g.stats.beaten}">
	                   		style="background: linear-gradient(to right,  rgba(0,0,0,1) 0%,rgba(255,255,255,1) ${achievPerc}%, rgba(0,0,0,1) ${achievPerc}%, rgba(0,0,0,1) 100%);"
                        </c:if>
<%--                     		border-color: rgb(<fmt:formatNumber type="number" maxFractionDigits="0" value="${105*g.stats.achievPerc}"/>, --%>
<%--                     		<fmt:formatNumber type="number" maxFractionDigits="0" value="${100+155*g.stats.achievPerc}"/>,<fmt:formatNumber type="number" maxFractionDigits="0" value="${105*g.stats.achievPerc}"/>)" --%>
                >
	            	<a href="/web/game/${g.appId}">
	                	<div class="gameTitle">
	                		${g.name}
	                	</div>
    	                <img title="${g.name}" alt="${g.name}" src="${g.logoUrl}" />
    	            </a>
                    <div class="bottomBar">
                    	<div class="achievPercBar" style="width:${empty g.stats.achievPerc? '0' : g.stats.achievPerc*100}%;">
                    		<c:if test="${not empty g.stats.achievPerc and g.stats.achievPerc > 0.75}">
                    			<div class="hoursPlayed">
                    				<c:if test="${not empty g.stats.hoursPlayed and g.stats.hoursPlayed ne '0'}">${g.stats.hoursPlayed}h/</c:if>${g.stats.totalPlaytimeFormatted}
                   				</div>
                    		</c:if>
                   		</div>
                    	<c:if test="${not empty g.stats.achievPerc}">
	                    	<div class="achievStats">
		                    	${g.stats.achievDone} of ${fn:length(g.achievements)} - <fmt:formatNumber type="number" maxFractionDigits="0" value="${g.stats.achievPerc*100}" />%
	                    	</div>
                    	</c:if>
                    	<c:if test="${empty g.stats.achievPerc or (not empty g.stats.achievPerc and g.stats.achievPerc <= 0.75)}">
	                    	<div class="hoursPlayed">
                   				<c:if test="${not empty g.stats.hoursPlayed and g.stats.hoursPlayed ne '0'}">${g.stats.hoursPlayed}h/</c:if>${g.stats.totalPlaytimeFormatted}
	                    	</div>
                    	</c:if>
                    </div>
                    <div class="bottomBar">
                    	<c:if test="${not empty g.stats.metaScore or not empty g.stats.userMetaScore}">
	                    	<div class="achievStats">
		                    	<a href="${g.stats.metacriticUrl}"><c:if test="${not empty g.stats.metaScore}">${g.stats.metaScore} of 100</c:if> 
		                    	<c:if test="${not empty g.stats.userMetaScore}"><c:if test="${not empty g.stats.metaScore}">- </c:if>${g.stats.userMetaScore}</c:if></a>
							</div>
                    	</c:if>
                    	<div class="hoursPlayed">
	                    	<a href="${g.stats.howLongToBeatUrl}"><fmt:formatNumber type="number" maxFractionDigits="1" value="${g.stats.minutesToBeat/60}" />h to beat</a>
                    	</div>  
                    </div>
                    <div class="controls">
                    	<div class="control">
                    		<a class="button playButton" title="Play" href="steam://run/${g.appId}"></a>
                    	</div>
	                    <div class="control">
        	            	<a class="button shopButton" title="Steam Store" href="${g.shopUrl}"></a>
	                    </div>
	                    <div class="control">
    	                	<a class="button updateButton" title="Update" href="javascript:update('${g.appId}')"></a>
	                    </div>
	                    <div class="control">
		                    <a class="button beatenButton" title="${g.stats.beaten? 'Unm' : 'M'}ark as beaten" href="javascript:toggleBeaten('${g.appId}')"></a>
                    	</div>
                    </div>
                </div>