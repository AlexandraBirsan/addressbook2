/**
 * Created by birsan on 4/13/2016.
 */

function invokeCreateContact(data) {
    $.ajax({
        url: "addressbook/api/contacts",
        type: "POST",
        data: JSON.stringify(data),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $('#listTable').DataTable().ajax.reload();
            $('#ContactDiv').hide();
        },
        complete: function (xhr, textStatus) {
            $('#errors').text(xhr.responseText);
            $('#errorsDiv').show();
        }
    });
}

function invokeUpdateContact(data) {
    $.ajax({
        url: "addressbook/api/contacts",
        type: "PUT",
        data: JSON.stringify(data),
        processData: false,
        contentType: "application/json; charset=utf-8",
        success: function (response) {
            $('#ContactDiv').hide();
            reloadDataTable();
        },
        complete: function (xhr, textStatus) {
            $('#errors').text(xhr.responseText);
            $('#errorsDiv').show();
        }
    });
}

function invokeDeleteContact(object) {
    var id = object.getAttribute("data-id");
    $.ajax({
        url: "addressbook/api/contacts/" + id,
        type: "DELETE",
        success: function (response) {
            reloadDataTable();
        },
        complete: function (xhr, textStatus) {
            alert(xhr.responseText);
        }
    });

}