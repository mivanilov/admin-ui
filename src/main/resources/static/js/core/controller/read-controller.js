var appReadController = (() => {
    'use strict';

    return {
        init,
        loadCallback
    };

    function init(pageConfig, tableOptions) {
        appBaseController.init(pageConfig, _mergeTableOptions(tableOptions));
        appForm.registerFormAjaxSubmit(_loadMeta());
    }

    function loadCallback(responseFragmentId, actionMeta) {
        appBaseController.handleCallback(responseFragmentId, actionMeta);
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
            submitMethod: appConst.http.post,
            submitButtonId: _pageConfig().buttons.load,
            responseCallback: appReadController.loadCallback,
            responseActionMeta: {
                page: undefined,
                form: undefined
            }
        };
    }
})();