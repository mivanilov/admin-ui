package org.mi.adminui.web.core.configuration.constant;

public class AppRoutes {
    public static final String HOME = "/";
    public static final String ERROR = "/error";

    public static final String ACCESS_LOGIN = "/access/login";
    public static final String ACCESS_LOGOUT = "/access/logout";
    public static final String ACCESS_DENIED = "/access/denied";
    public static final String ACCESS_LOGIN_SUCCESS = "/access/login-success";

    public static final String USERS = "/users";
    public static final String USERS_CREATE = "/users/create";
    public static final String USERS_DELETE = "/users/delete";
    public static final String USERS_EDIT = "/users/edit";
    public static final String USERS_EDIT_SAVE = "/users/edit/save";
    public static final String USERS_EDIT_CANCEL = "/users/edit/cancel";
    public static final String USERS_LOAD = "/users/load";

    public static final String EXAMPLE_CRUD_EAGER = "/example/crud/eager";
    public static final String EXAMPLE_CRUD_EAGER_CREATE = "/example/crud/eager/create";
    public static final String EXAMPLE_CRUD_EAGER_DELETE = "/example/crud/eager/delete";
    public static final String EXAMPLE_CRUD_EAGER_EDIT = "/example/crud/eager/edit";
    public static final String EXAMPLE_CRUD_EAGER_EDIT_SAVE = "/example/crud/eager/edit/save";
    public static final String EXAMPLE_CRUD_EAGER_EDIT_CANCEL = "/example/crud/eager/edit/cancel";
    public static final String EXAMPLE_CRUD_EAGER_LOAD_ROWS = "/example/crud/eager/rows";

    public static final String EXAMPLE_CRUD_LAZY = "/example/crud/lazy";
    public static final String EXAMPLE_CRUD_LAZY_CREATE = "/example/crud/lazy/create";
    public static final String EXAMPLE_CRUD_LAZY_DELETE = "/example/crud/lazy/delete";
    public static final String EXAMPLE_CRUD_LAZY_EDIT = "/example/crud/lazy/edit";
    public static final String EXAMPLE_CRUD_LAZY_EDIT_SAVE = "/example/crud/lazy/edit/save";
    public static final String EXAMPLE_CRUD_LAZY_EDIT_CANCEL = "/example/crud/lazy/edit/cancel";
    public static final String EXAMPLE_CRUD_LAZY_LOAD_ROWS_PAGE = "/example/crud/lazy/rows-page";

    public static final String EXAMPLE_READ_EAGER = "/example/read/eager";
    public static final String EXAMPLE_READ_EAGER_LOAD = "/example/read/eager/load";
    public static final String EXAMPLE_READ_EAGER_LOAD_ROWS = "/example/read/eager/rows";

    public static final String EXAMPLE_READ_LAZY = "/example/read/lazy";
    public static final String EXAMPLE_READ_LAZY_LOAD = "/example/read/lazy/load";
    public static final String EXAMPLE_READ_LAZY_LOAD_ROWS_PAGE = "/example/read/lazy/load/rows-page";

    private AppRoutes() {
    }
}
