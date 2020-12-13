var appBaseController = (() => {
    'use strict';

    return {
        init,
        handleCallback
    };

    function init(pageConfig, tableOptions) {
        appState.pageConfig(pageConfig);
        appState.form({id: pageConfig.components.form});
        appState.table({id: pageConfig.components.table, options: tableOptions});
    }

    function handleCallback(responseFragmentId, actionMeta) {
        if (responseFragmentId === _pageConfig().fragments.page) {
            appTable.initTable();
            appForm.registerFormAjaxSubmit(actionMeta.responseActionMeta.page || actionMeta);
        } else if (responseFragmentId === _pageConfig().fragments.form) {
            appForm.registerFormAjaxSubmit(actionMeta.responseActionMeta.form || actionMeta);
        } else if (responseFragmentId === _pageConfig().fragments.table) {
            appTable.initTable();
        }
    }

    function _pageConfig() {
        return appState.pageConfig();
    }
})();