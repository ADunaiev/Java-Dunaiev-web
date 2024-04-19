
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.sidenav');
    var instances = M.Sidenav.init(elems);

    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems);

    var selectElems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(selectElems);

    // шукаємо кнопку реєстрації. якщо находим додаємо обробник
    const signUpButton = document.getElementById("signup-button");
    if(signUpButton){signUpButton.onclick = signupButtonClick;}

    // шукаємо кнопку реєстрації. якщо находим додаємо обробник
    const authButton = document.getElementById("auth-button");
    if(authButton){authButton.onclick = authButtonClick;}

    // шукаємо кнопку додавання товару. якщо находим додаємо обробник
    const addProductButton = document.getElementById("add-product-button");
    if(addProductButton){
        addProductButton.onclick = addProductButtonClick;
    }
    checkAuth();

});

function serveCartButtons() {

    const userId = document.querySelector('[data-user-id]').getAttribute('data-user-id');
    for( let btn of document.querySelectorAll('[data-product]')) {
        btn.onclick = () => {
            let productId = btn.getAttribute('data-product');
            fetch(`/${getContext()}/shop-api?user-id=${userId}&product-id=${productId}`, {
                method: 'PUT'
            }).then(r => r.json()).then(j => {
                window.location.reload();
            });
        }
    }
}
function addProductButtonClick(e) {
    //console.log("product button clicked");

    const addProductForm = e.target.closest('form');
    if(! addProductForm) {
        throw "Addproduct form was not found";
    }

    const productNameInput = addProductForm.querySelector('input[name="product-name"]');
    if(!productNameInput) {throw "productNameInput not found";}
    const productNameHelper = productNameInput.parentNode.querySelector('.helper-text');
    if (!productNameHelper) throw "productNameInput '.helper-text' is not found";

    const productPriceInput = addProductForm.querySelector('input[name="product-price"]');
    if(!productPriceInput) {throw "productPriceInput not found";}
    const productPriceHelper = productPriceInput.parentNode.querySelector('.helper-text');
    if (!productPriceHelper) throw "productPriceInput '.helper-text' is not found";

    const productDescriptionTextarea = addProductForm.querySelector('textarea[name="product-description"]');
    if(!productDescriptionTextarea) {throw "productDescriptionTextarea not found";}

    const productImgInput = addProductForm.querySelector('input[name="product-img"]');
    if(!productImgInput) {throw "productImgInput not found";}

    /// Валідація даних
    if(
        validateName(productNameInput, productNameHelper)
    ){
        // формуємо дані для передачі на бекенд
        const formData = new FormData();
        formData.append("product-name", productNameInput.value);
        formData.append("product-price", productPriceInput.value);
        formData.append("product-description", productDescriptionTextarea.value);
        if (productImgInput.files.length >0){
            formData.append("product-img", productImgInput.files[0]);
        }

        // передаємо - формуємо запит

        fetch(window.location.href, {
            method: 'POST',
            body: formData
        })
            .then( r => r.json())
            .then( j => {

                console.log(j);

                /* document.getElementById("sign-up-form").innerHTML =
                     j['data']['message'];*/

            }) ;
    }

}

function signupButtonClick(e) {
    // console.log("signup button clicked");
    // шукаємо батьківський елемент кнопки (e.target)

    const signupForm = e.target.closest('form');
    if(! signupForm) {
        throw "Signup form was not found";
    }

    const nameInput = signupForm.querySelector('input[name="user-name"]');
    if(!nameInput) {throw "nameInput not found";}
    const nameHelper = nameInput.parentNode.querySelector('.helper-text');
    if (!nameHelper) throw "nameInput '.helper-text' is not found";

    const emailInput = signupForm.querySelector('input[name="user-email"]');
    if(!emailInput) {throw "emailInput not found";}
    const emailHelper = emailInput.parentNode.querySelector('.helper-text');
    if (!emailHelper) throw "emailInput '.helper-text' is not found";

    const passwordInput = signupForm.querySelector('input[name="user-password"]');
    if(!passwordInput) {throw "passwordInput not found";}
    const passwordHelper = passwordInput.parentNode.querySelector('.helper-text');
    if (!passwordHelper) throw "passwordInput '.helper-text' is not found";

    const repeatInput = signupForm.querySelector('input[name="user-repeat"]');
    if(!repeatInput) {throw "repeatInput not found";}
    const repeatPasswordHelper = repeatInput.parentNode.querySelector('.helper-text');
    if (!repeatPasswordHelper) throw "repeatInput '.helper-text' is not found";

    const avatarInput = signupForm.querySelector('input[name="user-avatar"]');
    if(!avatarInput) {throw "avatarInput not found";}

    const signupResult = document.getElementById('sign-up-result');
    if(!signupResult) {throw "Element 'sign-up-result' not found";}

    /// Валідація даних
    if(
        validateName(nameInput, nameHelper) &&
        validateEmail(emailInput, emailHelper) &&
        validatePassword(passwordInput, passwordHelper) &&
        validateRepeatPassword(repeatInput, repeatPasswordHelper, passwordInput)
    ){
        // формуємо дані для передачі на бекенд
        const formData = new FormData();
        formData.append("user-name", nameInput.value);
        formData.append("user-email", emailInput.value);
        formData.append("user-password", passwordInput.value);
        if (avatarInput.files.length >0){
            formData.append("user-avatar", avatarInput.files[0]);
        }

        // передаємо - формуємо запит

        fetch(window.location.href, {
            method: 'POST',
            body: formData
        })
            .then( r => r.json())
            .then( j => {

                if(j.meta.status == "success") {

                    signupResult.innerHTML = j.meta.message;
                    signupResult.style.background = "lightgreen";

                    setTimeout(function () {
                        window.location.href = '/' + (window.location.pathname.split('/')[1]) + '/';
                    }, 3000);
                }
                else {
                    signupResult.innerHTML = j.meta.message;
                    signupResult.style.background = "lightpink";

                    passwordInput.value = "";
                    repeatInput.value = "";
                }

               /*document.getElementById("sign-up-form").innerHTML =
                    j['data']['message'];*/

            }) ;
    }

}

function authButtonClick(e) {
    // console.log("signup button clicked");
    // шукаємо батьківський елемент кнопки (e.target)

    const authForm = e.target.closest('form');
    if(! authForm) {
        throw "Auth form was not found";
    }

    const emailInput = authForm.querySelector('input[name="sign-in-email"]');
    if(!emailInput) {throw "sign-in-email not found";}
    const emailHelper = emailInput.parentNode.querySelector('.helper-text');
    if (!emailHelper) throw "emailInput '.helper-text' is not found";

    const passwordInput = authForm.querySelector('input[name="sign-in-password"]');
    if(!passwordInput) {throw "sign-in-password not found";}
    const passwordHelper = passwordInput.parentNode.querySelector('.helper-text');
    if (!passwordHelper) throw "passwordInput '.helper-text' is not found";



    /// Валідація даних

    if(
        validateEmail(emailInput, emailHelper) &&
        validatePassword(passwordInput, passwordHelper)
    ){

        // передаємо - формуємо запит

        fetch(`/${getContext()}/auth?email=${emailInput.value}&password=${passwordInput.value}`, {
            method: 'GET'
        })
            .then( r => r.json())
            .then(j => {
                if( j.data == null || typeof j.data.token == "undefined") {
                    document.getElementById("modal-auth-message").innerText = "Authorization rejected!";
                }
                else {
                    // авторізація токенами передбачає їх збереження з метою
                    // подальшого використання. Для того, щоб токени були доступні після перезавантаження
                    // їх вміщують до постійного сховища браузера - localStorage
                    localStorage.setItem("auth-token", j.data.token);
                    window.location.reload();
                }
            });


    }

}

function getContext() {
    return window.location.pathname.split('/')[1];
}
function checkAuth() {
    // ... при завантаженні сторінки перевіряємо наявність даних автентифікації
    // у localStorage

    const authToken = localStorage.getItem("auth-token");
    if(authToken) {
        // перевіряємо токен на валіндність і одержуємо дані користувача
        fetch(`/${getContext()}/auth?token=${authToken}`, {
            method: 'POST'
        })
            .then( r => r.json())
            .then(j => {
                if (j.meta.status == "success") {
                    // замінити "кнопку входу" на аватарку користувача
                    document.querySelector('[data-auth="avatar"]').innerHTML = `<img data-user-id="${j.data.id}" title="${j.data.name}" class="nav-avatar" src="/${getContext()}/img/avatar/${j.data.avatar}" />`;
                    const product = document.querySelector('[data-auth="product"]');
                    if (product) {
                        fetch(`/${getContext()}/product.jsp`)
                            .then(r => r.text())
                            .then(t => {
                                product.innerHTML = t;
                                document.getElementById("add-product-button")
                                    .addEventListener("click", addProductClick);
                            });

                    }
                    serveCartButtons();
                }
            });
    }
}

function addProductClick(e) {
    const form = e.target.closest('form');

    const nameInput = form.querySelector("#product-name");
    const nameInputHelper = form.querySelector("#product-name-helper");


    const fileInput = form.querySelector("#product-img");

    const priceInput = form.querySelector("#product-price");
    const priceHelper = form.querySelector("#product-price-helper");

    const descriptionTextarea = form.querySelector("#product-description");
    const descriptionHelper = form.querySelector("#description-helper");

    // Валідація даних

    if (validateName(nameInput, nameInputHelper)
        && validatePrice(priceInput, priceHelper)
        && validateDescription(descriptionTextarea, descriptionHelper)

        ) {

        // Формуємо дані для передачі на сервер

        const formData = new FormData();
        formData.append("name", nameInput.value.trim());
        formData.append("price", Number(priceInput.value.trim()));
        formData.append("description", descriptionTextarea.value.trim());
        formData.append("image", fileInput.files[0]);
        formData.append("token", localStorage.getItem("auth-token"));

        fetch(`/${getContext()}/shop-api`, {
            method: 'POST',
            body: formData
        })
            .then(r => r.json())
            .then(j => {
                const addProductResult = document.getElementById("add-product-result");
                if(addProductResult) {
                    addProductResult.innerHTML = j.meta.message;
                    if(j.meta.status === "success") {

                        addProductResult.style.background = "lightgreen";
                    }
                    else {
                        addProductResult.style.background = "lightpink";
                    }
                }
            });
    }
}

function validateName (nameInput, nameInputHelper) {
    var check = true;

    if (nameInput.value == "")
    {
        nameInput.className = "invalid";
        nameInputHelper.setAttribute('data-error', "Name can't be empty");
        check = false;
    }
    else {
        nameInput.className = "valid";
        nameInputHelper.setAttribute('data-success',"Accepted!")
    }

    return check;
}

function validatePrice (priceInput, priceHelper) {
    var check = true;

    if (priceInput.value.trim() === "")
    {
        priceInput.className = "invalid";
        priceHelper.setAttribute('data-error', "Price can't be empty");
        check = false;
    }
    else if (Number(priceInput.value.trim()) <= 0) {
        priceInput.className = "invalid";
        priceHelper.setAttribute('data-error', "Price should be positive");
        check = false;
    }
    else {
        priceInput.className = "valid";
        priceHelper.setAttribute('data-success',"Accepted!")
    }

    return check;
}

function validateDescription (descriptionTextarea, descriptionHelper) {
    var check = true;

    if (descriptionTextarea.value === "")
    {
        descriptionTextarea.className = "invalid";
        descriptionHelper.innerHTML = "Description can't be empty";
        descriptionHelper.style.color = "red";
        check = false;
    }
    else if (descriptionTextarea.value.length <= 50) {
        descriptionTextarea.className = "invalid";
        descriptionHelper.innerHTML = "Description should contain more than 50 chars";
        descriptionHelper.style.color = "red";
        check = false;

    }
    else {
        descriptionTextarea.className = "valid";
        descriptionHelper.innerHTML = "Accepted!";
        descriptionHelper.style.color = "green";
    }

    return check;
}

function validateEmail (dataInput, dataInputHelper) {
    var check = true;

    if (dataInput.value == "")
    {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Email can't be empty!");
        check = false;

    }
    else if (/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/i.test(dataInput.value)) {
        dataInput.className = "valid";
        dataInputHelper.setAttribute('data-success', "Accepted!");
    }
    else {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Wrong email format!");
        check = false;
    }

    return check;
}

function validatePassword (dataInput, dataInputHelper) {
    var check = true;

    if (dataInput.value == "") {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Password could not be empty");
        check = false;
    }
    //  ^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#\-_)\^(0?&])[A-Za-z\d@$!\^)(0_%*#?
    else if (/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#\-_)\^(0?&])[A-Za-z\d@$!\^)(0_%*#?\-&]{8,}$/i.test(dataInput.value)) {
        dataInput.className = "valid";
        dataInputHelper.setAttribute('data-success', "Accepted!");
    }
    else {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Minimum eight characters, at least one letter, one number and one special character");
        check = false;
    }

    return check;
}

function validateRepeatPassword (dataInput, dataInputHelper, passwordInput) {
    var check = true;

    if (dataInput.value == "") {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Password repeat could not be empty");
        check = false;
    }
    else if(dataInput.value == passwordInput.value ){
        dataInput.className = "valid";
        dataInputHelper.setAttribute('data-success', "Accepted!");
    }
    else {
        dataInput.className = "invalid";
        dataInputHelper.setAttribute('data-error', "Passwords are not equal");
        check = false;
    }

    return check;
}

