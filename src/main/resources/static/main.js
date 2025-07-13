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

        if (contentType && contentType.includes("text/plain")) {
            const data = xhr.responseText;
            if (data === 'Product saved') {
                notify(data.message, "success")
            } else {
                notify(data.message, "error")
            }
        }
    }
);