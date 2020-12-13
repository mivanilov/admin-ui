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
        _hide(modalState.id);
    }

    function _hide(modalId) {
        _modal(modalId).modal('hide');
        $('body').removeClass('modal-open');
        $('.modal-backdrop').remove();
    }

    function _modal(modalId) {
        return $(`#${modalId}`);
    }
})();