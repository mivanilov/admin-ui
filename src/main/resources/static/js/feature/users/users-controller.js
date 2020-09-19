var usersController = (() => {
    'use strict';

    return Object.assign({}, appCrudController, {init});

    function init(pageConfig) {
        appCrudController.init(pageConfig, _tableOptions(pageConfig));
    }

    function _tableOptions(pageConfig) {
        return Object.assign(
            appTable.eagerTableOptions(),
            {
                columns: [
                    {
                        field: undefined,
                        searchable: false,
                        formatter: appTable.tableRowActionsFormatter,
                        events: appTable.tableRowActionEvents()
                    },
                    {
                        field: pageConfig.dtoFields.email,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.name,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.role,
                        sortable: true
                    }
                ]
            }
        );
    }
})();
