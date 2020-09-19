var appForm = (() => {
    'use strict';

    const responseTypes = {
        success: 'success',
        error: 'error',
        unknown: 'unknown'
    };
    Object.freeze(responseTypes);

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
        const responseType = _resolveResponseType(response, actionMeta);

        if (responseType === responseTypes.unknown) {
            _handleUnknownResponse(response);
            return;
        }

        _replaceContent(responseType, response, actionMeta);
        _invokeCallback(responseType, actionMeta);
        _initForm(responseType, actionMeta);

        appToggle.toggleButton(actionMeta.submitButtonId, true);
    }

    function _resolveResponseType(response, actionMeta) {
        if (_isSuccessResponse(response, actionMeta.responsePlaceholderId.success)) {
            return responseTypes.success;
        } else if (_isErrorResponse(response, actionMeta.responsePlaceholderId.error)) {
            return responseTypes.error;
        }
        return responseTypes.unknown;
    }

    function _isSuccessResponse(response, responsePlaceholderId) {
        return _responseStartsWith(response, responsePlaceholderId, 'div');
    }

    function _isErrorResponse(response, responsePlaceholderId) {
        return responsePlaceholderId && _responseStartsWith(response, responsePlaceholderId, 'div');
    }

    function _responseStartsWith(response, responsePlaceholderId, elementName) {
        return response.startsWith(`<${elementName} id="${responsePlaceholderId}"`);
    }

    function _handleUnknownResponse(response) {
        response.includes('<div id="login"') === true ? appRedirect.redirectToLogin() : appRedirect.redirectToError();
    }

    function _replaceContent(responseType, response, actionMeta) {
        if (responseType === responseTypes.success) {
            $(`#${actionMeta.responsePlaceholderId.success}`).replaceWith(response);
        } else if (responseType === responseTypes.error) {
            $(`#${actionMeta.responsePlaceholderId.error}`).replaceWith(response);
        }
    }

    function _invokeCallback(responseType, actionMeta) {
        responseType === responseTypes.success && actionMeta.successCallback && actionMeta.successCallback();
    }

    function _initForm(responseType, actionMeta) {
        if (actionMeta.initForm) {
            if (responseType === responseTypes.success && actionMeta.successResActionMeta) {
                registerFormAjaxSubmit(actionMeta.successResActionMeta);
            } else {
                registerFormAjaxSubmit(actionMeta);
            }
        }
    }
})();