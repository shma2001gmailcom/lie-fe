/**
 * Created by misha on 1/9/16.
 */
var validator = {
    messageData: [],
    isMonomial: function (summand) {
        var pattern = /^\[(\[.*]|[a-z]),\s*(\[.*]|[a-z])]$/;
        var groups = pattern.exec(summand);
        return !pattern.test(summand) ? /^[a-z]$/.test(summand) :
        validator.isMonomial(groups[1]) && validator.isMonomial(groups[2]);
    },

    isPolynomial: function (input) {
        var monomials = input.replace(/[0-9\(\)\*; ]/g, '').split(/\+|-/);
        var message = '';
        for (var i = 1; i < monomials.length; ++i) {
            if (!validator.isMonomial(monomials[i].trim())) {
                message = message == '' ? i : message + ', ' + i;
            }
        }
        validator.messageData[input] = message;
    },

    checkInputs: function () {
        var message = '';
        var input = $('input[id=value]');
        input.each(function () {
            validator.isPolynomial($(this).val());
        });
        message = 'Please, check terms:<p>';
        input.each(function (index) {
            if (validator.messageData[$(this).val()]) {
                message += ' term #' + validator.messageData[$(this).val()] + ' at input #' + (index + 1) + '<p>';
            }
        });
        return message;
    }
};

$(document).bind('ready', function () {
    $('form').bind('submit', function (event) {
        var message = validator.checkInputs();
        if (message.indexOf('#') > -1) {
            $('.error-message').html(message);
            event.preventDefault();
            return false;
        }
    })
});


