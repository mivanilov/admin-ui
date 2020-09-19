var appToggle = (() => {
    'use strict';

    const spinners = {
        button: '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>'
    };
    Object.freeze(spinners);

    return {
        toggleButton
    };

    function toggleButton(buttonId, enable) {
        enable ? _enableElement(buttonId) : _disableElement(buttonId, spinners.button);
    }

    function _enableElement(elementId) {
        const element = $(`#${elementId}`);

        element.prop('disabled', false);
        element.html(appState.toggle()[elementId]);
    }

    function _disableElement(elementId, spinner) {
        const element = $(`#${elementId}`);

        appState.toggle({[elementId]: element.html()});

        element.prop('disabled', true);
        element.html(`${spinner} ${element.html()}`);
    }
})();