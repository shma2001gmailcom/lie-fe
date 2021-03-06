/**
 * author: misha
 * date: 1/9/16
 * time: 1:43 PM
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
        var monomials = input.replace(/[0-9()*; ]/g, '').split(/\+|-/);
        var message = '';
        for (var i = 1; i < monomials.length; ++i) {
            if (!validator.isMonomial(monomials[i].trim())) {
                message = message == '' ? i : message + ', ' + i;
            }
        }
        validator.messageData[input] = message;
    },

    isEndomorphism: function(input) {
        var pattern = /^\((.*;)+(.*)\)$/;
        return !pattern.test(input);
    },

    checkInputs: function () {
        var input = $('input[id=value]');
        input.each(function () {
            validator.isPolynomial($(this).val());
        });
        validator.isEndomorphism($('.endo'));
        var message = 'Please, check terms:<p>';
        input.each(function (index) {
            if (validator.messageData[$(this).val()]) {
                message += ' term #' + validator.messageData[$(this).val()] + ' at input #' + (index + 1) + '<p>';
                validator.switchClass($(this), 'text-field', 'contains-error');
            }
        });
        return message;
    },

    switchClass: function(input, oldClass, newClass) {
        input.removeClass(oldClass);
        input.addClass(newClass);

    }
};

$(document).bind('ready', function () {
    var input = $('input[name=value]');
    input.bind('switchClass', function(oldClass, newClass) {
        $(this).removeClass(oldClass).addClass(newClass);
    });
    $('form').bind('submit', function (event) {
        var message = validator.checkInputs();
        if (message.indexOf('#') > -1) {
            var error = $('.error-message');
            error.html(message);
            error.show();
            event.preventDefault();
            return false;
        }
    });
    input.bind('click', function() {
        $('.error-message').hide();
        validator.switchClass($(this), 'contains-error', 'text-field');
    });
});


