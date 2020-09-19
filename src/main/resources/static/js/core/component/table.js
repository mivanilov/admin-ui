var appTable = (() => {
    'use strict';

    return {
        initTable,
        getTableOptions,
        eagerTableOptions,
        lazyTableOptions,
        ajaxLoadTableRows,
        tableRowAttributes,
        toggleTableRowActions,
        tableRowActionsFormatter,
        tableRowActionEvents
    };

    function initTable() {
        const {id, options} = _table();
        const table = $(`#${id}`);
        table && table.bootstrapTable(options);
    }

    function getTableOptions() {
        const table = $(`#${_table().id}`);
        return table && table.bootstrapTable('getOptions');
    }

    function eagerTableOptions() {
        return {
            search: true,
            searchOnEnterKey: true,
            showSearchButton: true,
            sortable: true,
            pagination: true,
            queryParamsType: '',
            ajax: 'appTable.ajaxLoadTableRows'
        };
    }

    function lazyTableOptions() {
        return {
            sortable: true,
            serverSort: false,
            pagination: true,
            sidePagination: 'server',
            queryParamsType: '',
            ajax: 'appTable.ajaxLoadTableRows'
        };
    }

    function ajaxLoadTableRows(ajaxBootstrapTable) {
        $.ajax({
            url: _pageConfig().urls.tableRows,
            type: appConst.http.post,
            data: `${$.param(ajaxBootstrapTable.data)}&${_form().submittedData}`,
            success: (response) => ajaxBootstrapTable.success(response),
            error: () => appRedirect.redirectToError()
        });
    }

    function tableRowAttributes(row, index) {
        return {
            onclick: `appTable.toggleTableRowActions('actions${index}', 'actionsIcon${index}')`
        };
    }

    function toggleTableRowActions(actionsId, actionsIconId) {
        if (_table().activeRow) {
            appUtil.hideElement(_table().activeRow.actionsId);
            appUtil.showElement(_table().activeRow.actionsIconId);
        }

        appUtil.hideElement(actionsIconId);
        appUtil.showElement(actionsId);

        _table({activeRow: {actionsId, actionsIconId}});
    }

    function tableRowActionsFormatter(value, row, index) {
        return [
            `<button class="btn btn-sm far fa-edit" id="actionsIcon${index}"></button>`,
            `<div class="d-none" id="actions${index}">`,
            `    <button class="editRecord btn btn-sm fas fa-pencil-alt"></button>`,
            `    <button class="deleteRecord btn btn-sm far fa-trash-alt"></button>`,
            '</div>'
        ].join('');
    }

    function tableRowActionEvents() {
        return {
            'click .editRecord': (event, value, row, index) => appCrudController.editRecord(row, index),
            'click .deleteRecord': (event, value, row, index) => appCrudController.deleteRecord(row, index)
        };
    }

    function _pageConfig() {
        return appState.pageConfig();
    }

    function _form() {
        return appState.form();
    }

    function _table(tableState) {
        return appState.table(tableState);
    }
})();