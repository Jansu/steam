<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>
<jsp:include page="template/head.jsp"/>
        <div class="achievList sortable searchable">
            <c:forEach var="a" items="${achievs}">
                <div title="${a.name}" class="achievementContainer"
                        data_name="${a.name}"
                >
                	<div class="gameContainer">
            			<my:game game="${a.game}" />
            		</div>
            		<my:achievement achievement="${a}" />
                </div>
            </c:forEach>
        </div>
<jsp:include page="template/footer.jsp"/>