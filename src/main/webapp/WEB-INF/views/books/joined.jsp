<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="joined" scope="request"/>
<c:set var="title"  value="Catalog Join — Inkwell" scope="request"/>
<jsp:include page="../fragments/header.jsp"/>

<div class="page-header">
    <div>
        <h1>Catalog Join</h1>
        <p class="subtitle">
            Result of an <strong>INNER JOIN</strong> between <code>Book</code> and <code>Author</code>,
            projected through a JPQL constructor expression into <code>AuthorBookView</code>.
        </p>
    </div>
</div>

<div class="card" style="margin-bottom: 22px;">
    <div class="filter-bar">
        <form action="<c:url value='/books/joined'/>" method="get" style="display:flex; gap:10px;">
            <div class="field" style="margin:0;">
                <input name="genre" placeholder="Filter by genre (e.g. Fiction)" value="${filterGenre}"/>
            </div>
            <button class="btn btn-primary btn-sm" type="submit">Apply</button>
            <c:if test="${not empty filterGenre}">
                <a class="btn btn-ghost btn-sm" href="<c:url value='/books/joined'/>">Clear</a>
            </c:if>
        </form>
    </div>

    <pre style="margin:0; padding:14px 16px; background:rgba(0,0,0,0.30); border:1px solid var(--border); border-radius:12px; font-size:12.5px; color:#cdbfff; overflow-x:auto;">
SELECT new com.bits.library.dto.AuthorBookView(
    a.name, a.country, b.title, b.genre, b.publishedYear, b.price)
FROM   Book b
INNER JOIN b.author a
<c:if test="${not empty filterGenre}">WHERE  LOWER(b.genre) = LOWER('${filterGenre}')
</c:if>ORDER BY a.name ASC, b.publishedYear DESC</pre>
</div>

<div class="table-wrap">
    <c:choose>
        <c:when test="${empty rows}">
            <div class="empty">No matching rows.</div>
        </c:when>
        <c:otherwise>
            <table class="data">
                <thead>
                <tr>
                    <th>Author × Book</th>
                    <th>Country</th>
                    <th>Genre</th>
                    <th class="no-wrap">Year</th>
                    <th class="text-right no-wrap">Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${rows}">
                    <tr>
                        <td data-label="Author × Book">
                            <div class="join-pair">
                                <span>${r.authorName}</span>
                                <span class="arrow">›</span>
                                <span class="muted" style="font-weight:500;">${r.bookTitle}</span>
                            </div>
                        </td>
                        <td data-label="Country"><span class="pill cyan">${r.authorCountry}</span></td>
                        <td data-label="Genre"><span class="pill">${r.genre}</span></td>
                        <td data-label="Year">${r.publishedYear}</td>
                        <td data-label="Price" class="text-right no-wrap">
                            ₹<fmt:formatNumber value="${r.price}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>

<jsp:include page="../fragments/footer.jsp"/>
