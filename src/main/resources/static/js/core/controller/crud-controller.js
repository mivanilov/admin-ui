var appCrudController = (() => {
    'use strict';

    return {
        init,
        editRecord,
        cancelEditRecord,
        deleteRecord,
        confirmDeleteRecord
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
            initForm: true,
            submitMethod: appConst.http.post,
            submitButtonId: _pageConfig().buttons.create,
            responsePlaceholderId: {
                success: _pageConfig().fragments.page,
                error: _pageConfig().fragments.form
            },
            successCallback: _createCallback
        };
    }

    function _editMeta(index) {
        return {
            initForm: true,
            submitMethod: appConst.http.put,
            submitButtonId: `${_pageConfig().buttons.edit}${index}`,
            responsePlaceholderId: {
                success: _pageConfig().fragments.form
            },
            successCallback: _editCallback,
            successResActionMeta: _saveEditMeta()
        };
    }

    function _saveEditMeta() {
        return {
            initForm: true,
            submitMethod: appConst.http.patch,
            submitButtonId: _pageConfig().buttons.saveEdit,
            responsePlaceholderId: {
                success: _pageConfig().fragments.page,
                error: _pageConfig().fragments.form
            },
            successCallback: _saveEditCallback,
            successResActionMeta: _createMeta()
        };
    }

    function _cancelEditMeta() {
        return {
            initForm: true,
            submitMethod: appConst.http.get,
            submitButtonId: _pageConfig().buttons.cancelEdit,
            responsePlaceholderId: {
                success: _pageConfig().fragments.form
            },
            successCallback: _cancelEditCallback,
            successResActionMeta: _createMeta()
        };
    }

    function _deleteMeta(index) {
        return {
            initForm: false,
            submitMethod: appConst.http.delete,
            submitButtonId: `${_pageConfig().buttons.delete}${index}`,
            responsePlaceholderId: {
                success: _pageConfig().fragments.table
            },
            successCallback: _deleteCallback
        };
    }

    function _createCallback() {
        appTable.initTable();
    }

    function _editCallback() {
        appUtil.hideElement(_pageConfig().fragments.table);
    }

    function _saveEditCallback() {
        appTable.initTable();
        appUtil.showElement(_pageConfig().fragments.table);
    }

    function _cancelEditCallback() {
        appTable.initTable();
        appUtil.showElement(_pageConfig().fragments.table);
    }

    function _deleteCallback() {
        appTable.initTable();
    }
})();