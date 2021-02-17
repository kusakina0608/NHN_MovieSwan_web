const submitButtonOnClick = function () {
    const baseURL = "http://localhost:8080"
    fetch((baseURL + "/api/question/register"), {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({
            uid: document.getElementsByName("uid")[0].value,
            title: document.getElementsByName("title")[0].value,
            content: document.getElementsByName("content")[0].value
        })
    }).then((res) => {
        return res.json();
    }).then((data) => {
        console.log(data);
    });
};