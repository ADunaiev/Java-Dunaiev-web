<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = (String) request.getContextPath();
    // Виличаємо дані передані контролеером
    CartItem[] cartItems = (CartItem[]) request.getAttribute("cart");
%>
<h1>Ваш кошик</h1>
<div class="row">
    <% for(CartItem item : cartItems) { %>
    <div class="col s12 m4">
        <h5 class="header"></h5>
        <div class="card horizontal">
            <div class="card-image">
                <img src="<%= context%>/img/no_image.jpg" alt="no-image">
            </div>
            <div class="card-stacked">
                <div class="card-content">
                    <p>id: <%=item.getProductId()%></p>
                    <p>count: <%=item.getCount()%></p>
                </div>
                <div class="card-action">
                    <a href="#">видалити з кошику</a>
                </div>
            </div>
        </div>
    </div>
    <% } %>
</div>
<%-- Відображаємо --%>

