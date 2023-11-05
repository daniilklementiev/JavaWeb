document.addEventListener('DOMContentLoaded', function() {
    // db.jsp
    const callMeButton = document.getElementById('db-call-me-button');
    if (callMeButton) {
        callMeButton.addEventListener('click', callMeButtonClick);
    }
    const createButton = document.getElementById('db-create-button');
    if (createButton) {
        createButton.addEventListener('click', createButtonClick);
    }

    const getAllButton = document.getElementById('db-get-all-button');
    if (getAllButton) {
        getAllButton.addEventListener('click', getAllButtonClick);
    }

    const getAllWoutDeletedButton = document.getElementById('db-get-all-wout-deleted-button');
    if (getAllWoutDeletedButton) {
        getAllWoutDeletedButton.addEventListener('click', getAllWoutDeletedButtonClick);
    }
});

function getAllWoutDeletedButtonClick() {
    fetch(window.location.href, {
        method: 'UNLINK'
    }).then(r => r.json()).then(showCalls);
}

function createButtonClick() {
    fetch(window.location.href, {
        method: 'POST'
    }).then(r => r.json()).then(j=> {
        console.log(j)
    });
}

function callMeButtonClick() {
    const nameInput = document.getElementById('db-call-me-name');
    if(!nameInput) {
        throw "nameInput not found";
    }
    if(!nameInput.value) {
        M.toast({html: 'Name is required'});
        return;
    }
    const phoneInput = document.getElementById('db-call-me-phone');
    if(!phoneInput) throw "phoneInput not found";
    if(!phoneInput.value) {
        M.toast({html: 'Phone is required'});
        return;
    }

    fetch(window.location.href, {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            name: nameInput.value,
            phone: phoneInput.value
        })
    }).then(r => r.json()).then(j=>{
        // Заявка принята - уведомление
        M.toast({html: 'Your order is accepted'});
    });

}

function showCalls(json) {
    const container = document.getElementById('db-get-all-container');
    if(!container) {
        throw "calls not found";
    }

    const table = document.createElement('table');
    table.classList.add('striped');
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    const th1 = document.createElement('th');
    th1.innerText = 'id';
    const th2 = document.createElement('th');
    th2.innerText = 'name';
    const th3 = document.createElement('th');
    th3.innerText = 'phone';
    const th4 = document.createElement('th');
    th4.innerText = 'Call moment';
    const th5 = document.createElement('th');
    th5.innerText = 'Delete?';
    th5.classList.add('center-align');
    tr.appendChild(th1);
    tr.appendChild(th2);
    tr.appendChild(th3);
    tr.appendChild(th4);
    tr.appendChild(th5);
    thead.appendChild(tr);
    table.appendChild(thead);
    const tbody = document.createElement('tbody');
    json.forEach(call => {
        const tr = document.createElement('tr');
        const td1 = document.createElement('td');
        td1.innerText = call.id;
        const td2 = document.createElement('td');
        td2.innerText = call.name;
        const td3 = document.createElement('td');
        td3.innerText = call.phone;
        const td4 = document.createElement('td');
        if(typeof call.call_moment == 'undefined' || call.call_moment == null) {
            const btn = document.createElement('button');
            btn.classList.add('btn');
            btn.classList.add('waves-effect');
            btn.classList.add('waves-light');
            btn.classList.add('deep-purple');
            btn.setAttribute('data-call-id', call.id);
            btn.addEventListener('click', makeCallClick);
            btn.innerText = 'Call';

            td4.appendChild(btn);
        }
        else {
           td4.appendChild(document.createTextNode(call.call_moment));
        }

        const td5 = document.createElement('td');
        td5.classList.add('center-align');
        const btn = document.createElement('button');
        btn.classList.add('btn');
        btn.classList.add('waves-effect');
        btn.classList.add('waves-light');
        btn.classList.add('red');
        btn.setAttribute('data-call-id', call.id);
        btn.addEventListener('click', deleteCallClick);
        btn.innerText = 'X';

        td5.appendChild(btn);
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
        tr.appendChild(td4);
        tr.appendChild(td5);
        tbody.appendChild(tr);
    });
    table.appendChild(tbody);
    container.innerHTML = '';
    container.appendChild(table);
}

function makeCallClick(e) {
    const callId = e.target.getAttribute('data-call-id');
    if(confirm("Make call to order " + callId)){
        fetch(window.location.href + '?call-id=' + callId, {
            method: 'CALL'
        }).then(r => r.json()).then(j=>{
            if( typeof j.callMoment != 'undefined') {
                alert('Call made for order ' + callId);
            }
            else {
                e.target.parentNode.innerHTML = j.call_moment;
            }
        });
    }
}

function getAllButtonClick() {
    fetch(window.location.href, {
        method: 'LINK'
    }).then(r => r.json()).then(showCalls);
}

function deleteCallClick(e) {
    const callId = e.target.getAttribute('data-call-id');
    if(confirm("Delete call with ID:  " + callId + "?")){
        fetch(window.location.href + '?call-id=' + callId, {
            method: 'DELETE'
        }).then(r => {
            if(r.status === 204) {
                e.target.parentNode.parentNode.remove();
            }
            else {
                alert('Error deleting call with ID: ' + callId);
            }
        });
    }
}