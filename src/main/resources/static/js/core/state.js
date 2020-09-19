var appState = (() => {
    'use strict';

    const state = {
        pageConfig: {
            dtoFields: {},
            urls: {},
            fragments: {},
            components: {},
            buttons: {}
        },
        form: {
            id: '',
            submittedData: ''
        },
        table: {
            id: '',
            options: {},
            activeRow: {}
        },
        modal: {
            id: '',
            data: {},
            callback: () => ''
        },
        toggle: {}
    };

    return {
        pageConfig,
        form,
        table,
        modal,
        toggle
    };

    function pageConfig(pageConfigState) {
        pageConfigState && Object.assign(state.pageConfig, pageConfigState);
        return state.pageConfig;
    }

    function form(formState) {
        formState && Object.assign(state.form, formState);
        return state.form;
    }

    function table(tableState) {
        tableState && Object.assign(state.table, tableState);
        return state.table;
    }

    function modal(modalState) {
        modalState && Object.assign(state.modal, modalState);
        return state.modal;
    }

    function toggle(toggleState) {
        toggleState && Object.assign(state.toggle, toggleState);
        return state.toggle;
    }
})();