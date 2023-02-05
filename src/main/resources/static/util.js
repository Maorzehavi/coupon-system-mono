function handelSuccess(res) {
    document.getElementById("successMsg").innerHTML = res;
    document.getElementById("errorMsg").innerHTML = null;
}

function handleError(error) {
    console.error(error.message, error);
    document.getElementById("errorMsg").innerHTML = error.message;
    document.getElementById("successMsg").innerHTML = null;
}

//   function test(){
//     let url = "http://localhost:8080/api/v1/auth/test";
//     let xhr = new XMLHttpRequest();
//     xhr.open("GET", url);
//     xhr.setRequestHeader("Content-Type", "application/json");
//     xhr.onload = function () {
//         if (xhr.status === 200) {
//             handelSuccess(xhr.response);
//         } else {
//             handleError(xhr.response);
//         }
//         }
//     xhr.send();

//   } 
function test() {
    let url = "http://localhost:8080/test";
    fetch(url, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    })
        .then((res) => {
            if (res.status === 200) {
                return res.json();
            } else {
                throw new Error(res.statusText);
            }
        }
        )
        .then((res) => handelSuccess(res))
        .catch((error) => handleError(error));

}

