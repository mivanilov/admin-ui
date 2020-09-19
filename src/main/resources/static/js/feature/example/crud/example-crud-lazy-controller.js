var exampleCrudLazyController = (() => {
    'use strict';

    return Object.assign({}, appCrudController, {init});

    function init(pageConfig) {
        appCrudController.init(pageConfig, _tableOptions(pageConfig));
    }

    function _tableOptions(pageConfig) {
        return Object.assign(
            appTable.lazyTableOptions(),
            {
                columns: [
                    {
                        field: pageConfig.dtoFields.id,
                        searchable: false,
                        formatter: appTable.tableRowActionsFormatter,
                        events: appTable.tableRowActionEvents()
                    },
                    {
                        field: pageConfig.dtoFields.name,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.visibility,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.createDate,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.exampleConfigTypeText,
                        sortable: true
                    },
                    {
                        field: pageConfig.dtoFields.active,
                        sortable: true
                    }
                ]
            }
        );
    }
})();
