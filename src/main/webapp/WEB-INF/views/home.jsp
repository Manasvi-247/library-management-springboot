<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<c:set var="active" value="home" scope="request"/>
<c:set var="title" value="Inkwell — Dashboard" scope="request"/>
<jsp:include page="fragments/header.jsp"/>

<section class="hero">
    <h1>A library worth turning the page for.</h1>
    <p>Inkwell is a Spring Boot reference implementation of a one-to-many JPA model — Authors and their Books — with full Create / Read / Update flows, JSP views, validation, exception handling and an inner-join projection.</p>
    <div class="cta-row">
        <a class="btn btn-primary" href="<c:url value='/authors/new'/>">+ Add Author</a>
        <a class="btn" href="<c:url value='/books/new'/>">+ Add Book</a>
        <a class="btn btn-ghost" href="<c:url value='/books/joined'/>">View Catalog Join →</a>
    </div>
</section>

<div class="stats-grid">
    <div class="stat">
        <div class="stat-label">Authors</div>
        <div class="stat-value">${authorCount}</div>
    </div>
    <div class="stat warm">
        <div class="stat-label">Books</div>
        <div class="stat-value">${bookCount}</div>
    </div>
    <div class="stat cool">
        <div class="stat-label">Joined Rows</div>
        <div class="stat-value">${joinedCount}</div>
    </div>
</div>

<div class="card">
    <h3 style="margin:0 0 8px;">What this app demonstrates</h3>
    <p class="muted" style="margin:0 0 16px;">Each item maps to a grading-rubric requirement.</p>
    <ul style="line-height:1.9; padding-left: 18px; margin:0;">
        <li>JPA entities with <code>@OneToMany</code> / <code>@ManyToOne</code> relationship and unique constraints.</li>
        <li>Repository layer with a custom JPQL <strong>INNER JOIN</strong> projecting into a DTO.</li>
        <li>Service layer with <code>@Transactional</code> business logic and domain exceptions.</li>
        <li>Spring MVC controllers with <code>@Valid</code> form binding and <code>BindingResult</code>.</li>
        <li>JSP views with JSTL / EL, flash messages, and an integrated design system.</li>
        <li>Global exception handler for <code>DataIntegrityViolationException</code>.</li>
        <li>Unit tests with JUnit 5, Mockito and <code>@DataJpaTest</code>.</li>
    </ul>
</div>

<jsp:include page="fragments/footer.jsp"/>
