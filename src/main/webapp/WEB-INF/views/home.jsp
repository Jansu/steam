<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ page session="false" %>

<jsp:include page="template/head.jsp"/>
        <div class="gamelist sortable searchable">
            <c:forEach var="g" items="${games}">
            	<my:game game="${g}" />
            </c:forEach>
        </div>
<jsp:include page="template/footer.jsp"/>