var exampleReadLazyController = (() => {
    'use strict';

    return Object.assign({}, appReadController, {init});

    function init(pageConfig) {
        appReadController.init(pageConfig, _tableOptions(pageConfig));
    }

    function _tableOptions(pageConfig) {
        return Object.assign(
            appTable.lazyTableOptions(),
            {
                columns: [
                    {
                        field: pageConfig.dtoFields.id,
                        searchable: false
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
