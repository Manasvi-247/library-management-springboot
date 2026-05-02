<%@ taglib prefix="c"    uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="books" scope="request"/>
<c:set var="title"  value="${mode == 'edit' ? 'Edit Book' : 'New Book'} — Inkwell" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>

<div class="page-header">
    <div>
        <h1>${mode == 'edit' ? 'Edit Book' : 'New Book'}</h1>
        <p class="subtitle">${mode == 'edit' ? 'Update existing book details. Title must remain unique per author.' : 'Add a new title and link it to one of the existing authors.'}</p>
    </div>
    <a class="btn btn-ghost" href="<c:url value='/books'/>">← Back to list</a>
</div>

<div class="card form-card">
    <c:choose>
        <c:when test="${mode == 'edit'}"><c:url var="action" value="/books/${book.id}"/></c:when>
        <c:otherwise><c:url var="action" value="/books"/></c:otherwise>
    </c:choose>
    <form:form modelAttribute="book" action="${action}" method="post" cssClass="form-grid">
        <div class="field">
            <label for="title">Title</label>
            <form:input path="title" id="title" placeholder="e.g. The God of Small Things"/>
            <form:errors path="title" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="authorId">Author</label>
            <select name="authorId" id="authorId" required>
                <option value="">— Select author —</option>
                <c:forEach var="a" items="${authors}">
                    <option value="${a.id}" ${selectedAuthorId == a.id ? 'selected' : ''}>${a.name} (${a.country})</option>
                </c:forEach>
            </select>
            <form:errors path="author" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="genre">Genre</label>
            <form:input path="genre" id="genre" placeholder="Fiction, Fantasy, Literary…"/>
            <form:errors path="genre" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="publishedYear">Published year</label>
            <form:input path="publishedYear" id="publishedYear" type="number" placeholder="2003"/>
            <form:errors path="publishedYear" element="span" cssClass="error"/>
        </div>
        <div class="field">
            <label for="price">Price (₹)</label>
            <form:input path="price" id="price" type="number" step="0.01" placeholder="349.00"/>
            <form:errors path="price" element="span" cssClass="error"/>
        </div>
        <div class="form-actions">
            <a class="btn btn-ghost" href="<c:url value='/books'/>">Cancel</a>
            <button class="btn btn-primary" type="submit">${mode == 'edit' ? 'Save changes' : 'Create book'}</button>
        </div>
    </form:form>
</div>

<jsp:include page="../fragments/footer.jsp"/>
