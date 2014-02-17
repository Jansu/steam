<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@tag description="Achievement template" pageEncoding="UTF-8"%>
<%@tag import="de.druz.web.persistance.model.Achievement" %>
<%@attribute name="achievement" required="true" type="de.druz.web.persistance.model.Achievement"%>

<c:set var="a" value="${achievement}"/>

					<div class="achievement filterable ${a.unlocked? 'unlocked beaten' : 'locked'}"
							data_name="${a.name}"
					>
	                    <img title="${a.name} - ${a.description}" alt="${a.name} - ${a.description}" src="${a.unlocked? a.iconClosedUrl : a.iconOpenUrl}" />
	                    <div class="achievementData">
	                    	<div class="globalPercFiller" style="width: <fmt:formatNumber type="number" maxFractionDigits="0" value="${a.globalUnlockPerc}"/>%"></div>
		                	<div class="title">
		                		${a.name}
		                	</div>
		                	<div class="globalPerc">
	                    		<fmt:formatNumber type="number" maxFractionDigits="1" value="${a.globalUnlockPerc}"/>%
	                    	</div>
		                	<div class="desc">
		                		${a.description}
		                	</div>
	                    </div>
	                    <c:if test="${a.unlocked}">
		                    <div class="unlockedTime">
		                    	Unlocked: <fmt:formatDate value="${a.timestamp}" pattern="dd.MM.yyyy HH:mm:ss" />
		                    </div>
	                    </c:if>
                    </div>