var appForm = (() => {
    'use strict';

    return {
        registerFormAjaxSubmit,
        ajaxSubmit,
        buildAjaxSubmitData
    };

    function registerFormAjaxSubmit(actionMeta) {
        const form = $(`#${_form().id}`);

        form && form.on('submit', (event) => {
            event.preventDefault();
            ajaxSubmit(form.attr('action'), form.serialize(), actionMeta);
        });
    }

    function ajaxSubmit(url, data, actionMeta) {
        _form({submittedData: data});
        _table(_getTableOptions());

        appToggle.toggleButton(actionMeta.submitButtonId, false);

        $.ajax({
            url,
            type: actionMeta.submitMethod,
            data,
            success: (response) => _handleAjaxResponse(response, actionMeta),
            error: () => appRedirect.redirectToError()
        });
    }

    function buildAjaxSubmitData(row) {
        return Object
            .keys(row)
            .reduce((data, key) => {
                data.push(`${key}=${row[key]}`);
                return data;
            }, [])
            .join('&');
    }

    function _pageConfig() {
        return appState.pageConfig();
    }

    function _form(formState) {
        return appState.form(formState);
    }

    function _table(tableState) {
        return appState.table(tableState);
    }

    function _getTableOptions() {
        const {pageNumber, pageSize} = appTable.getTableOptions();
        return Object.assign(_table().options, {pageNumber, pageSize});
    }

    function _handleAjaxResponse(response, actionMeta) {
        const responseFragmentId = resolveResponseFragmentId(response);

        if (!responseFragmentId) {
            _handleUnknownResponse(response);
            return;
        }

        _replaceContent(responseFragmentId, response);
        _invokeCallback(responseFragmentId, actionMeta);

        appToggle.toggleButton(actionMeta.submitButtonId, true);
    }

    function resolveResponseFragmentId(response) {
        const responseMatch = response.match('^<div id="([a-zA-Z0-9]+)"');
        const extractedFragmentId = responseMatch && responseMatch[1];
        return [_pageConfig().fragments.page, _pageConfig().fragments.form, _pageConfig().fragments.table]
            .find(fragmentId => fragmentId === extractedFragmentId);
    }

    function _handleUnknownResponse(response) {
        response.includes('<div id="login"') === true ? appRedirect.redirectToLogin() : appRedirect.redirectToError();
    }

    function _replaceContent(responseFragmentId, response) {
        $(`#${responseFragmentId}`).replaceWith(response);
    }

    function _invokeCallback(responseFragmentId, actionMeta) {
        actionMeta.responseCallback && actionMeta.responseCallback(responseFragmentId, actionMeta);
    }
})();