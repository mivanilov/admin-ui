var appCrudController = (() => {
    'use strict';

    return {
        init,
        editRecord,
        cancelEditRecord,
        deleteRecord,
        confirmDeleteRecord,
        createCallback,
        editCallback,
        saveEditCallback,
        cancelEditCallback,
        deleteCallback
    };

    function init(pageConfig, tableOptions) {
        appBaseController.init(pageConfig, _mergeTableOptions(tableOptions));
        appTable.initTable();
        appForm.registerFormAjaxSubmit(_createMeta());
    }

    function editRecord(row, index) {
        appForm.ajaxSubmit(_pageConfig().urls.edit, appForm.buildAjaxSubmitData(row), _editMeta(index));
    }

    function cancelEditRecord() {
        appForm.ajaxSubmit(_pageConfig().urls.cancelEdit, null, _cancelEditMeta());
    }

    function deleteRecord(row, index) {
        appModal.show(_pageConfig().components.confirmationModal, {row, index}, confirmDeleteRecord);
    }

    function confirmDeleteRecord({row, index}) {
        appForm.ajaxSubmit(_pageConfig().urls.delete, appForm.buildAjaxSubmitData(row), _deleteMeta(index));
    }

    function createCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);
    }

    function editCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);

        if (responseFragmentId === _pageConfig().fragments.form) {
            appUtil.hideElement(_pageConfig().fragments.table);
        }
    }

    function saveEditCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);

        if (responseFragmentId === _pageConfig().fragments.page) {
            appUtil.showElement(_pageConfig().fragments.table);
        }
    }

    function cancelEditCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);

        appUtil.showElement(_pageConfig().fragments.table);
    }

    function deleteCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);
    }

    function _pageConfig() {
        return appState.pageConfig();
    }

    function _mergeTableOptions(tableOptions) {
        return Object.assign(
            {
                classes: 'table table-hover',
                rowAttributes: appTable.tableRowAttributes
            },
            tableOptions
        );
    }

    function _createMeta() {
        return {
            submitMethod: appConst.http.post,
            submitButtonId: _pageConfig().buttons.create,
            responseCallback: appCrudController.createCallback,
            responseActionMeta: {
                page: undefined,
                form: undefined
            }
        };
    }

    function _editMeta(index) {
        return {
            submitMethod: appConst.http.put,
            submitButtonId: `${_pageConfig().buttons.edit}${index}`,
            responseCallback: appCrudController.editCallback,
            responseActionMeta: {
                page: _createMeta(),
                form: _saveEditMeta()
            }
        };
    }

    function _saveEditMeta() {
        return {
            submitMethod: appConst.http.patch,
            submitButtonId: _pageConfig().buttons.saveEdit,
            responseCallback: appCrudController.saveEditCallback,
            responseActionMeta: {
                page: _createMeta(),
                form: undefined
            }
        };
    }

    function _cancelEditMeta() {
        return {
            submitMethod: appConst.http.get,
            submitButtonId: _pageConfig().buttons.cancelEdit,
            responseCallback: appCrudController.cancelEditCallback,
            responseActionMeta: {
                page: _createMeta(),
                form: _createMeta()
            }
        };
    }

    function _deleteMeta(index) {
        return {
            submitMethod: appConst.http.delete,
            submitButtonId: `${_pageConfig().buttons.delete}${index}`,
            responseCallback: appCrudController.deleteCallback,
            responseActionMeta: {
                page: _createMeta(),
                form: _createMeta()
            }
        };
    }
})();