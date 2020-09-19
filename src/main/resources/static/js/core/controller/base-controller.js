var appBaseController = (() => {
    'use strict';

    return {
        init
    };

    function init(pageConfig, tableOptions) {
        appState.pageConfig(pageConfig);
        appState.form({id: pageConfig.components.form});
        appState.table({id: pageConfig.components.table, options: tableOptions});
    }
})();