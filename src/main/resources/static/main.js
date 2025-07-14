function notify(message, icon) {
    Swal.fire({
        position: "top-end",
        icon: icon,
        title: message,
        showConfirmButton: false,
        timer: 2000
    });
}

function confirm(message, yesBtn, noBtn, icon) {

}


document.body.addEventListener('htmx:afterRequest',
    function handleResponse(event) {
        // alert(event);
        const xhr = event.detail.xhr;   //raw XHR object
        const contentType = xhr.getResponseHeader("Content-Type");
        let textField = document.querySelectorAll("#product-submission-form > input[type=text]")
    // alert(textField.length)
        if (contentType && contentType.includes("text/plain")) {
            const data = xhr.responseText;
            if (data === 'Product saved') {
                notify(data, "success")
                for (let field of textField) {
                    field.value = ''
                }
            } else {
                notify(data, "error")
            }
        }
    }
);