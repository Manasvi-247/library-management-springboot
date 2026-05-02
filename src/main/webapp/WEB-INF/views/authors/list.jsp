<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="authors" scope="request"/>
<c:set var="title" value="Authors — Inkwell" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>

<div class="page-header">
    <div>
        <h1>Authors</h1>
        <p class="subtitle">Every Inkwell book is anchored to one of these writers.</p>
    </div>
    <a class="btn btn-primary" href="<c:url value='/authors/new'/>">+ New Author</a>
</div>

<div class="table-wrap">
    <c:choose>
        <c:when test="${empty authors}">
            <div class="empty">No authors yet — start by adding one.</div>
        </c:when>
        <c:otherwise>
            <table class="data">
                <thead>
                <tr>
                    <th style="width:70px;">ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Country</th>
                    <th class="no-wrap">Birth Year</th>
                    <th class="no-wrap text-right">Books</th>
                    <th class="text-right">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="a" items="${authors}">
                    <tr>
                        <td data-label="ID"><span class="row-id">#${a.id}</span></td>
                        <td data-label="Name"><strong>${a.name}</strong></td>
                        <td data-label="Email" class="muted">${a.email}</td>
                        <td data-label="Country"><span class="pill cyan">${a.country}</span></td>
                        <td data-label="Birth Year">${a.birthYear}</td>
                        <td data-label="Books" class="text-right">
                            <span class="pill amber">${fn:length(a.books)}</span>
                        </td>
                        <td data-label="Actions" class="row-actions">
                            <a class="btn btn-sm" href="<c:url value='/authors/${a.id}/edit'/>">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../fragments/footer.jsp"/>
