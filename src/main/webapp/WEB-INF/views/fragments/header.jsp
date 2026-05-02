<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${title != null ? title : 'Inkwell — Library Management'}</title>
    <link rel="preconnect" href="https://fonts.googleapis.com"/>
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="<c:url value='/resources/css/styles.css'/>"/>
</head>
<body>
<nav class="navbar">
    <a class="brand" href="<c:url value='/'/>">
        <span class="logo">i</span>
        <span>Inkwell</span>
    </a>
    <ul class="nav-links">
        <li><a href="<c:url value='/'/>"          class="${active == 'home' ? 'active' : ''}">Dashboard</a></li>
        <li><a href="<c:url value='/authors'/>"   class="${active == 'authors' ? 'active' : ''}">Authors</a></li>
        <li><a href="<c:url value='/books'/>"     class="${active == 'books' ? 'active' : ''}">Books</a></li>
        <li><a href="<c:url value='/books/joined'/>" class="${active == 'joined' ? 'active' : ''}">Catalog Join</a></li>
    </ul>
</nav>
<main class="app-shell">
    <c:if test="${not empty flashSuccess}">
        <div class="alert success"><span class="dot"></span><span>${flashSuccess}</span></div>
    </c:if>
    <c:if test="${not empty flashError}">
        <div class="alert error"><span class="dot"></span><span>${flashError}</span></div>
    </c:if>
