
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

});


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
                console.log(j);

               /* document.getElementById("sign-up-form").innerHTML =
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
