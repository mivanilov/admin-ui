var appUtil = (() => {
    'use strict';

    return {
        showElement,
        hideElement
    };

    function showElement(elementId) {
        $(`#${elementId}`).removeClass('d-none');
    }

    function hideElement(elementId) {
        $(`#${elementId}`).addClass('d-none');
    }
})();