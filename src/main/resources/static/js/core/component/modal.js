var appModal = (() => {
    'use strict';

    return {
        show,
        confirm
    };

    function show(id, data, callback) {
        appState.modal({id, data, callback});
        _modal(id).modal('show');
    }

    function confirm() {
        const modalState = appState.modal();
        modalState.callback(modalState.data);
        _modal(modalState.id).modal('hide');
    }

    function _modal(modalId) {
        return $(`#${modalId}`);
    }
})();