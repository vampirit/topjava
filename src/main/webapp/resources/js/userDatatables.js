var ajaxUrl = "ajax/admin/users/";
var datatableApi;

// $(document).ready(function () {
$(function () {
    var datatable = $("#datatable");
    datatableApi = datatable.DataTable({
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
    });
    makeEditable();

    datatable.find(':checkbox').on('click', function () {
        var line = $(this).closest('tr');
        var id = line.prop('id');
        $.ajax({
            type: 'POST',
            url: ajaxUrl + 'enabledUser',
            data: {'userId': id, 'enabled': $(this).prop('checked')},
            success: function () {
                line.toggleClass('disable');
            }
        });
        console.log(id + ' ' + $(this).prop('checked'));

    })
});

function updateTable(){
    $.get(ajaxUrl, insertDataInTable);
}