<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <title>test</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/table-sortable.js"></script>
    <script type="text/css" src="<%=request.getContextPath()%>/resources/js/table-sortable.js"></script>
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
    <h1>Disease-Gene binary overlay table</h1>
    <input id="search" type="search" class="form-control" placeholder="filter">
    <div id="pages">
        <table class="gs-table">

        </table>
    </div>
</div>
<script type="text/javascript">

</script>
</body>
</html>
