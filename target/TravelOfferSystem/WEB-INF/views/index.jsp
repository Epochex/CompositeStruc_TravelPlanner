<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Travel Offer System</title>
</head>
<body>
<h1>Welcome to Travel Offer System</h1>
<p>Please select an operation below:</p>
<ul>
    <!-- Lucene Operations -->
    <li><a href="${pageContext.request.contextPath}/lucene/rebuild">Rebuild Lucene Index</a></li>
    <li>
        <form action="${pageContext.request.contextPath}/lucene/add" method="post">
            Add Lucene Document:
            <input type="text" name="id" placeholder="Document ID" required>
            <input type="text" name="content" placeholder="Document Content" required>
            <button type="submit">Add</button>
        </form>
    </li>
    <li>
        <form action="${pageContext.request.contextPath}/lucene/search" method="get">
            Search Lucene Document:
            <input type="text" name="query" placeholder="Search Query" required>
            <button type="submit">Search</button>
        </form>
    </li>

    <!-- Database Operations -->
    <li><a href="${pageContext.request.contextPath}/query/ile">View All Islands</a></li>
    <li>
        <form action="${pageContext.request.contextPath}/query/ile" method="post">
            Add Island:
            <input type="text" name="id" placeholder="Island ID" required>
            <input type="text" name="name" placeholder="Island Name" required>
            <button type="submit">Add</button>
        </form>
    </li>
    <li><a href="${pageContext.request.contextPath}/query/plage">View All Beaches</a></li>
    <li>
        <form action="${pageContext.request.contextPath}/query/plage" method="post">
            Add Beach:
            <input type="text" name="id" placeholder="Beach ID" required>
            <input type="text" name="name" placeholder="Beach Name" required>
            <button type="submit">Add</button>
        </form>
    </li>
</ul>
</body>
</html>
