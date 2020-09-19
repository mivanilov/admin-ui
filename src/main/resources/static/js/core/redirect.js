var appRedirect = (() => {
    'use strict';

    const redirectPaths = {
        login: '/access/login?logout',
        error: '/error'
    };
    Object.freeze(redirectPaths);

    return {
        redirectToLogin,
        redirectToError
    };

    function redirectToLogin() {
        _redirectTo(redirectPaths.login);
    }

    function redirectToError() {
        _redirectTo(redirectPaths.error);
    }

    function _redirectTo(path) {
        window.location.replace(path);
    }
})();