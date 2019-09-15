<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <title>test</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-3.4.1.min.js"></script>
    <link rel="stylesheet" href="table-sortable.css">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
            integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
            crossorigin="anonymous">
    </script>
    <script src="table-sortable.js"></script>

</head>
<body>

<h1>Overlay of external binary data onto Reactome</h1>

Background: Various valuable datasets are available as sets of binary data
pairs of the form
Entity - Relationship - Protein. Examples are Drug-target or
Disease-diseaseGene relationships. We would like to develop
a mechanism to efficiently connect such data to Reactome, with minimal
development effort, largely using existing
mechanisms, namely search/overrepresentation analysis and interactor
overlay. Slides to explain the idea are
<br>
<br>

<div id="diseaseTable">
    <input type="search" id="search">
    <div id="pages">

    </div>
</div>
<table>
    <thead>
    <tr>
        <th scope="col">Disease Name</th>
        <th scope="col">Disease Class</th>
        <th scope="col">Number Of Genes</th>
        <th scope="col">Gene List</th>
        <th scope="col">Disease Id</th>
        <th scope="col">Check in PathwayBrowser</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="diseaseItem" items="${page.diseaseItems}">
        <tr>
            <td>${diseaseItem.diseaseName}</td>
            <td>${diseaseItem.diseaseClass}</td>
            <td>${diseaseItem.geneItems.size()}</td>
            <td>${diseaseItem.geneItems}</td>
            <td>${diseaseItem.diseaseId}</td>
<%--            <td onclick="analyze(${diseaseItem.geneItems})">check into pathway browser</td>--%>
            <td>check into pathway browser</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<%--<script type="text/javascript">--%>
<%--    function analyze(geneItems) {--%>
<%--        $.ajax({--%>
<%--            type: "POST",--%>
<%--            url: "<%=request.getContextPath()%>/test/analyze",--%>
<%--            data: JSON.stringify(geneItems),--%>
<%--            dataType: "json",--%>
<%--            contentType: 'application/json;charset=utf-8',--%>
<%--            success: function (data) {--%>
<%--                console.log(data)--%>
<%--            },--%>
<%--            error: function (res) {--%>
<%--                console.log(res)--%>
<%--            }--%>
<%--        });--%>
<%--    }--%>
<%--</script>--%>
</body>
</html>
