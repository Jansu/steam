<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>
<jsp:include page="template/head.jsp"/>
        <div class="gameDetail">
			<my:game game="${game}"></my:game>
            <div class="achievList sortable searchable">
	            <c:forEach var="a" items="${game.achievements}">
					<my:achievement achievement="${a}"></my:achievement>
	            </c:forEach>
            </div>
        </div>
<jsp:include page="template/footer.jsp"/>