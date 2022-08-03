const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const userCtx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeUsersEditable(
        $("#usersDatatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});

function makeUsersEditable(datatableApi) {
    userCtx.datatableApi = datatableApi;
    makeEditable(userCtx, $("#userDetailsForm"));
}