<%@ page import="step.learning.dal.dto.ActionItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = (String) request.getContextPath();
    // Виличаємо дані передані контролеером
    ActionItem[] actionItems = (ActionItem[]) request.getAttribute("action");
%>
<h1>Акційні пропозиції</h1>
<div class="row">
    <% for(ActionItem item : actionItems) { %>
    <div class="col s12 m4">
        <h5 class="header"></h5>
        <div class="card horizontal">

            <div class="card-image">
                <img src="<%= context%>/img/product.png" alt="no-image">
            </div>
            <div class="card-stacked">
                <div class="card-content">
                    <span class="new badge red" data-badge-caption="%">- <%=item.getRebate()%></span>
                    <p>id: <%=item.getProductId()%></p>

                </div>
                <div class="card-action">
                    <a href="#">купити</a>
                </div>
            </div>
        </div>
    </div>
    <% } %>
</div>