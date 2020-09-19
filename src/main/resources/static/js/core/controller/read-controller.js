var appReadController = (() => {
    'use strict';

    return {
        init
    };

    function init(pageConfig, tableOptions) {
        appBaseController.init(pageConfig, _mergeTableOptions(tableOptions));
        appForm.registerFormAjaxSubmit(_loadMeta());
    }

    function _mergeTableOptions(tableOptions) {
        return Object.assign(
            {
                classes: 'table table-hover'
            },
            tableOptions
        );
    }

    function _pageConfig() {
        return appState.pageConfig();
    }

    function _loadMeta() {
        return {
            initForm: false,
            submitMethod: appConst.http.post,
            submitButtonId: _pageConfig().buttons.load,
            responsePlaceholderId: {
                success: _pageConfig().fragments.table,
                error: _pageConfig().fragments.form
            },
            successCallback: _loadCallback
        };
    }

    function _loadCallback() {
        appTable.initTable();
    }
})();