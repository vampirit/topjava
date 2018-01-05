var ajaxUrl = "ajax/meals/";
var datatableApi;

$(function () {
   datatableApi = $("#datatable").DataTable({
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
       "order": [
           [
               0, "asc"
           ]
       ]
   });
   makeEditable();

   $('#filterForm').submit(function(){
       updateTable();
       return false;
   });
});

function updateTable(){
    var filterData = $('#filterForm');
    $.ajax({
        type:"POST",
        url: ajaxUrl + 'filter',
        data: filterData.serialize(),
        success: insertDataInTable
    })
}

function resetFilter() {
    $('#filterForm').find(':input').val("");
    updateTable();
}