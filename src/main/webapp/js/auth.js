document.addEventListener('DOMContentLoaded', () => {
    M.Modal.init(document.querySelectorAll('.modal'), {
        opacity: 0.6,
        inDuration: 200,
        outDuration: 200,
        onOpenStart: onModalOpens
    });

    const authModalSignInButton = document.getElementById('auth-modal-sign-in-button');
    if (authModalSignInButton) {
        authModalSignInButton.addEventListener('click', signInButtonClick);
    }

    const spaContainer = document.getElementById('spa-container');
    if(spaContainer) {
        const token = window.localStorage.getItem('token');
        if(token) {
            spaContainer.innerText = token;
        }
        else {
            spaContainer.innerText = 'No token';
        }
    }

    const spaLogoutButton = document.getElementById('spa-btn-logout');
    if(spaLogoutButton) {
        spaLogoutButton.addEventListener('click', spaLogoutClick);
    }
    const spaGetInfoButton = document.getElementById('spa-btn-get-info');
    if(spaGetInfoButton) {
        spaGetInfoButton.addEventListener('click', spaGetInfoClick);
    }

});

function spaLogoutClick() {
    window.localStorage.removeItem('token');
    window.location.reload();
}

function spaGetInfoClick() {
    const container = document.getElementById('spa-container');
    if(!container) {
        throw "#spa-container not found";
    }
    fetch(`${getAppContext()}/uk/tpl/template1.html`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token'),
        }
    } ).then(r=>r.text()).then(t=>{
        container.innerHTML = t;
    });
    fetch(`${getAppContext()}/uk/tpl/NP.png`, {
        method: 'GET',
        headers: {
            'Authorization': 'Bearer ' + window.localStorage.getItem('token'),
        }
    } ).then(r=>r.blob()).then(b=>{
        const blobUrl = URL.createObjectURL(b);
        console.log(blobUrl);
        container.innerHTML += `<img src="${blobUrl}">`;
    });
}

function onModalOpens() {
    const {authLogin, authPassword, authMessage} = getModalElements();

    authLogin.value = '';
    authPassword.value = '';
    authMessage.innerText = '';
}

function getModalElements() {
    const authLogin = document.getElementById('auth-login');
    if (!authLogin) {
        throw '#auth-login not found'
    }
    const authPassword = document.getElementById('auth-password');
    if (!authPassword) {
        throw '#auth-password not found'
    }
    const authMessage = document.getElementById('auth-message-container');
    if (!authMessage) {
        throw '#auth-message-container not found'
    }
    return {
        authLogin,
        authPassword,
        authMessage
    }
}

function signInButtonClick() {
    const {authLogin, authPassword, authMessage} = getModalElements();

    const login = authLogin.value;


    if(login.length === 0 ) {
        authMessage.innerText = 'Login must be filled';
        return;
    }
    const password = authPassword.value;
    if(password.length === 0 ) {
        authMessage.innerText = 'Password must be filled';
        return;
    }

    const url = `${getAppContext()}/auth?login=${login}&password=${password}`;
    fetch(url).then(r=> {
        if(r.status === 202) {
            r.json().then(encodedToken => {
                // проверяем целостность путем декодирования и кодирования
                try {
                    const token = JSON.parse(atob(encodedToken));
                    if(typeof token.jti === 'undefined') {
                        authMessage.innerText = 'Bad token received';
                    }
                    else {
                        window.localStorage.setItem('token', encodedToken);
                        window.location.reload();
                    }
                }catch (e) {
                    authMessage.innerText = 'Bad data received';
                    return;
                }
                console.log(encodedToken);
            });
        }
        else {
            authMessage.innerText = 'Authorization failed';
        }
    });
}

function getAppContext() {
    const isContextPresent = false;
    return window.location.pathname.substring(0, window.location.pathname.indexOf('/', 2));
}
