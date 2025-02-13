<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Travel Offer System - Lucene Testing</title>
</head>
<body>
<h1>Welcome to Travel Offer System</h1>
<p>Use the links below to test database and Lucene functionalities:</p>

<h2>Lucene Operations</h2>
<ul>
    <!-- Rebuild Lucene Index -->
    <li>
        <a href="${pageContext.request.contextPath}/lucene/rebuild">Rebuild Lucene Index</a>
    </li>

    <!-- Add Document to Lucene -->
    <li>
        <form action="${pageContext.request.contextPath}/lucene/add" method="post">
            Add Document to Lucene:
            <input type="number" name="id" placeholder="Document ID" required>
            <input type="text" name="content" placeholder="Document Content" required>
            <button type="submit">Add Document</button>
        </form>
    </li>

    <!-- Search Lucene -->
    <li>
        <form action="${pageContext.request.contextPath}/lucene/search" method="get">
            Search Lucene Index:
            <input type="text" name="query" placeholder="Search Query" required>
            <button type="submit">Search</button>
        </form>
    </li>

    <!-- Mixed Query -->
    <li>
        <form action="${pageContext.request.contextPath}/lucene/mixed" method="get">
            Execute Mixed Query:
            <input type="text" name="query" placeholder="SQL Query with Lucene Condition" required>
            <button type="submit">Execute</button>
        </form>
    </li>

    <!-- Add Text File to Row (Only Write File, No Index) -->
    <li>
        <form action="${pageContext.request.contextPath}/lucene/addTextFileOnly" method="post">
            Add Text File Only (No Indexing):
            <br/>Row Key (ID): <input type="number" name="id" required>
            <br/>Text Content: <textarea name="content" rows="3" cols="40"></textarea>
            <br/><button type="submit">Add Text File</button>
        </form>
    </li>

    <!-- Add Text File to Row and Immediately Index -->
    <li>
        <form action="${pageContext.request.contextPath}/lucene/addTextFileAndIndex" method="post">
            Add Text File & Rebuild Index:
            <br/>Row Key (ID): <input type="number" name="id" required>
            <br/>Text Content: <textarea name="content" rows="3" cols="40"></textarea>
            <br/><button type="submit">Add & Index</button>
        </form>
    </li>
</ul>

<h2>Database Operations</h2>
<ul>
    <!-- Island Operations -->
    <li><a href="${pageContext.request.contextPath}/api/ile">View All Islands</a></li>
    <li>
        <form action="${pageContext.request.contextPath}/api/ile" method="post">
            Add Island:
            <input type="text" name="name" placeholder="Island Name" required>
            <button type="submit">Add</button>
        </form>
    </li>

    <!-- Beach Operations -->
    <li><a href="${pageContext.request.contextPath}/api/plage">View All Beaches</a></li>
    <li>
        <form action="${pageContext.request.contextPath}/api/plage" method="post">
            Add Beach:
            <input type="text" name="name" placeholder="Beach Name" required>
            <button type="submit">Add</button>
        </form>
    </li>
</ul>
</body>
</html>