<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="" scope="request"/>
<c:set var="title"  value="Error — Inkwell" scope="request"/>
<jsp:include page="fragments/header.jsp"/>

<div class="card error-card">
    <div style="font-size:42px;">⚠</div>
    <h2>${title != null ? title : 'Something went wrong'}</h2>
    <p class="muted">${message}</p>
    <c:if test="${not empty detail}">
        <div class="detail">${detail}</div>
    </c:if>
    <div style="margin-top:24px;">
        <a class="btn btn-primary" href="<c:url value='/'/>">Back to dashboard</a>
        <a class="btn btn-ghost" href="javascript:history.back()">Go back</a>
    </div>
</div>

<jsp:include page="fragments/footer.jsp"/>
