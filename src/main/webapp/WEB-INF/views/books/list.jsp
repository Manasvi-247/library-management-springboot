<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="books" scope="request"/>
<c:set var="title"  value="Books — Inkwell" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>

<div class="page-header">
    <div>
        <h1>Books</h1>
        <p class="subtitle">Every title is linked to a single author via a foreign key.</p>
    </div>
    <a class="btn btn-primary" href="<c:url value='/books/new'/>">+ New Book</a>
</div>

<div class="table-wrap">
    <c:choose>
        <c:when test="${empty books}">
            <div class="empty">No books yet — start by adding one.</div>
        </c:when>
        <c:otherwise>
            <table class="data">
                <thead>
                <tr>
                    <th style="width:70px;">ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th class="no-wrap">Year</th>
                    <th class="text-right no-wrap">Price</th>
                    <th class="text-right">Actions</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="b" items="${books}">
                    <tr>
                        <td data-label="ID"><span class="row-id">#${b.id}</span></td>
                        <td data-label="Title"><strong>${b.title}</strong></td>
                        <td data-label="Author">${b.author.name}</td>
                        <td data-label="Genre">
                            <c:choose>
                                <c:when test="${b.genre == 'Fantasy'}">     <span class="pill pink">${b.genre}</span></c:when>
                                <c:when test="${b.genre == 'Literary'}">    <span class="pill cyan">${b.genre}</span></c:when>
                                <c:when test="${b.genre == 'Non-Fiction'}"> <span class="pill green">${b.genre}</span></c:when>
                                <c:when test="${b.genre == 'Dystopian'}">   <span class="pill amber">${b.genre}</span></c:when>
                                <c:otherwise>                               <span class="pill">${b.genre}</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td data-label="Year">${b.publishedYear}</td>
                        <td data-label="Price" class="text-right no-wrap">
                            ₹<fmt:formatNumber value="${b.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        </td>
                        <td data-label="Actions" class="row-actions">
                            <a class="btn btn-sm" href="<c:url value='/books/${b.id}/edit'/>">Edit</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../fragments/footer.jsp"/>
