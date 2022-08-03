const mealAjaxUrl = "meals/";

const mealCtx = {
    ajaxUrl: mealAjaxUrl
};

$(function () {
    makeMealsEditable(
        $("#mealsDatatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
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
        })
    );
});

function makeMealsEditable(datatableApi) {
    mealCtx.datatableApi = datatableApi;
    makeEditable(mealCtx, $("#mealDetailsForm"));
}

