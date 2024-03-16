
<%@ page contentType="text/html;charset=UTF-8" %>
<h1>Домашня сторінка</h1>
<p>
    Контроль хеш-сервісу: <%=request.getAttribute("hash")%>
</p>
<p>
    Контроль бази даних: <%=request.getAttribute("db")%>
</p>