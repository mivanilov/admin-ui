var appConst = (() => {
    'use strict';

    const constants = {
        http: {
            get: 'get',
            post: 'post',
            put: 'put',
            patch: 'patch',
            delete: 'delete'
        }
    };
    Object.freeze(constants);

    return constants;
})();