<%@ taglib prefix="c"    uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="authors" scope="request"/>
<c:set var="title"  value="${mode == 'edit' ? 'Edit Author' : 'New Author'} — Inkwell" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>

<div class="page-header">
    <div>
        <h1>${mode == 'edit' ? 'Edit Author' : 'New Author'}</h1>
        <p class="subtitle">${mode == 'edit' ? 'Update existing author details. Email must remain unique.' : 'Add a new writer to your catalog. Email must be unique across the library.'}</p>
    </div>
    <a class="btn btn-ghost" href="<c:url value='/authors'/>">← Back to list</a>
</div>

<div class="card form-card">
    <c:choose>
        <c:when test="${mode == 'edit'}"><c:url var="action" value="/authors/${author.id}"/></c:when>
        <c:otherwise><c:url var="action" value="/authors"/></c:otherwise>
    </c:choose>
    <form:form modelAttribute="author" action="${action}" method="post" cssClass="form-grid">
        <div class="field">
            <label for="name">Full name</label>
            <form:input path="name" id="name" placeholder="e.g. Arundhati Roy"/>
            <form:errors path="name" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="email">Email</label>
            <form:input path="email" id="email" type="email" placeholder="author@example.com"/>
            <form:errors path="email" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="country">Country</label>
            <form:input path="country" id="country" placeholder="India"/>
            <form:errors path="country" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="birthYear">Birth year</label>
            <form:input path="birthYear" id="birthYear" type="number" placeholder="1947"/>
            <form:errors path="birthYear" element="span" cssClass="error"/>
        </div>
        <div class="form-actions">
            <a class="btn btn-ghost" href="<c:url value='/authors'/>">Cancel</a>
            <button class="btn btn-primary" type="submit">${mode == 'edit' ? 'Save changes' : 'Create author'}</button>
        </div>
    </form:form>
</div>

<jsp:include page="../fragments/footer.jsp"/>
