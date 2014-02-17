<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<jsp:include page="template/head.jsp"/>
        <div class="timeline compact achievList searchable">
            <c:forEach var="day" items="${days}">
            	<c:set var="lastgame" value="${null}" />
            	<div class="day">
            		<div class="date golden">
		            	<h2><fmt:formatDate value="${day}" pattern="E dd.MM.yyyy" /></h2>
		            	<c:set var="count" value="${fn:length(data[day])}" />
		            	<c:set var="sum" value="${0}"/> 
		            	<c:forEach var="a" items="${data[day]}">
							<c:set var="sum" value="${a.globalUnlockPerc + sum}"/>
		            	</c:forEach>
		            	${count} Achievements earned - Avg. <fmt:formatNumber type="number" maxFractionDigits="1" value="${sum/count}"/>%
            		</div>
            		<div class="achievsContainer">
            			<c:forEach var="a" items="${data[day]}" varStatus="status">
            				<c:if test="${lastgame ne a.game}">
            					<c:if test="${lastgame ne null}">
            						</div><%-- closing .section div --%>
            					</c:if>
		            			<div class="section">
            					<c:set var="lastgame" value="${a.game}" />
			            		<div class="gameContainer">
			            			<my:game game="${a.game}" />
			            		</div>
            				</c:if>
            				<my:achievement achievement="${a}" />
            			</c:forEach>
						</div><%-- closing .section div --%>
            		</div>
            	</div>
            </c:forEach>
        </div>
<jsp:include page="template/footer.jsp"/>