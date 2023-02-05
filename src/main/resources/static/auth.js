function login(email, password) {
    let userCredentials = new UserCredentials(email, password);
    let userCredentialsJSON = JSON.stringify(userCredentials);
    let url = "http://localhost:8080/api/v1/auth/login";
    fetch(url, {
        method: 'POST', headers: {
            'Content-Type': 'application/json'
        }, body: userCredentialsJSON
    })
        .then(response => {
            if (response.status === 200) {
                console.log(response);
            } else {
                return response.json().then(error => {
                    throw new Error(error);
                });
            }
        })
        .then(token => {
            console.log(token);
            sessionStorage.setItem("token", token);
            handelSuccess("Logged In");
        })
        .catch(error => {
            handleError(error);
        });
}

// function login(email, password) {
//     let userCredentials = new UserCredentials(email, password);
//     let userCredentialsJSON = JSON.stringify(userCredentials);
//     let url = "http://localhost:8080/api/v1/auth/login";
//    let xhr = new XMLHttpRequest();
//     xhr.open("POST", url);
//     xhr.setRequestHeader("Content-Type", "application/json");
//     xhr.onload = function () {
//         if (xhr.status === 200) {
//             let token = xhr.responseText;
//             sessionStorage.setItem("token", token);
//             handelSuccess("Logged In");
//         } else {
//             let error = JSON.parse(xhr.responseText);
//             console.log(error);
//             handleError(error);
//         }
//     }
//     xhr.send(userCredentialsJSON);

// }

function logout() {
    sessionStorage.removeItem("token");
    handelSuccess("Logged Out");
}