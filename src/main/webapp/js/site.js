
document.addEventListener('DOMContentLoaded', function() {
    var elems = document.querySelectorAll('.sidenav');
    var instances = M.Sidenav.init(elems);

    var elems = document.querySelectorAll('.modal');
    var instances = M.Modal.init(elems);

    var selectElems = document.querySelectorAll('select');
    var instances = M.FormSelect.init(selectElems);

    // шукаємо кнопку реєстрації. якщо находим додаємо обробник
    const signUpButton = document.getElementById("signup-button");
    if(signUpButton){
        signUpButton.onclick = signupButtonClick;
    }

    // шукаємо кнопку додавання товару. якщо находим додаємо обробник
    const addProductButton = document.getElementById("add-product-button");
    if(addProductButton){
        addProductButton.onclick = addProductButtonClick;
    }

});

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


function validateName (nameInput, nameInputHelper) {
    var check = true;

    if (nameInput.value == "")
    {
        nameInput.className = "invalid";
        nameInputHelper.setAttribute('data-error', "Name can't be empty");
        check = false;
    }
    else if (/\d/.test(nameInput.value)) {
        nameInput.className = "invalid";
        nameInputHelper.setAttribute('data-error', "Name can't contain digits");
        check = false;

    }
    else if (/[^a-zа-яіЇє'ґ ]/i.test(nameInput.value)) {
        nameInput.className = "invalid";
        nameInputHelper.setAttribute('data-error', "Name can't contain special characters");
        check = false;
    }
    else {
        nameInput.className = "valid";
        nameInputHelper.setAttribute('data-success',"Accepted!")
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
