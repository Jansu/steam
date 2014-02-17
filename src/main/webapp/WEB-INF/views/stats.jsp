<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>
<jsp:include page="template/head.jsp"/>
	<div>
		<div id="gameCount">
		</div>
		<div id="achievCount">
		</div>
        <div class="achievsByDayOfWeek">
        	<h2>achievsByDayOfWeek:</h2>
        </div>
        <div class="achievsByHourOfDay">
        	<h2>achievsByHourOfDay:</h2>
        </div>
        <div id="calendar" class="">
        </div>
	</div>
<jsp:include page="template/footer.jsp"/>