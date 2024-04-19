<%@ page import="step.learning.dal.dto.CartItem" %>
<%@ page import="step.learning.models.CartPageModel" %>
<%@ page import="step.learning.dal.dto.Product" %>
<%@ page import="step.learning.dal.dao.ProductDao" %>
<%@ page import="java.util.Objects" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String context = (String) request.getContextPath();
    // Виличаємо дані передані контролеером
   //  CartItem[] cartItems = (CartItem[]) request.getAttribute("cart");

    CartPageModel model = (CartPageModel) request.getAttribute("model");

%>
<div class="row">
    <div class="col s8">
        <h1>Товари</h1>
        <div class="row">
        <% for(Product product : model.getProducts()) { %>

                <div class="col s4">
                    <div class="card medium">
                        <div class="card-image">
                            <img src="<%=context%>/img/products/<%= product.getImage() == null ?  "no_image.jpg" : product.getImage() %>" alt="Image"/>

                            <a data-product="<%= product.getId()%>" class="product-cart-btn btn-floating halfway-fab waves-effect waves-light red"><i class="material-icons">shopping_cart</i></a>
                        </div>
                        <div class="card-content">
                            <span class="card-title"><%= product.getName()%></span>
                            <p><%= product.getDescription()%></p>
                        </div>
                    </div>
                </div>

        <% } %>
        </div>
    </div>
    <div class="col s4">
        <h1>Ваш кошик</h1>
        <ul class="collection">
            <% for(CartItem item : model.getCartItems()) {
                for (Product productItem : model.getProducts()) {
                    if (item.getProductId().equals(productItem.getId())) {%>
                        <li class="collection-item avatar">
                            <img src="<%=context%>/img/products/<%=productItem.getImage() != null ? productItem.getImage() : "no_image.jpg"%>" alt="" class="circle">
                            <%=productItem.getName()%>
                            <span class="badge"><%=item.getCount()%></span>
                            <p><%= productItem.getDescription()%></p>
                        </li>
            <% }}} %>
        </ul>
    </div>
</div>


<%-- Відображаємо --%>

