$(document).ready(function () {
    var error = $('#error');
    if (error.html()) {
        error.attr('class', 'error-message');
    }
});