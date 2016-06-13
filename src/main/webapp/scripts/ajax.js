/**
 * Created by birsan on 4/13/2016.
 */
function invokeCreateContact(data) {
    $.ajax({
        url: "addressbook/api/contacts",
        type: "POST",
        headers: {"AuthToken": getCookie(COOKIE_NAME)},
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
        headers: { "AuthToken": getCookie(COOKIE_NAME)},
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
        headers: { "AuthToken": getCookie(COOKIE_NAME)},
        success: function (response) {
            reloadDataTable();
        },
        complete: function (xhr, textStatus) {
            alert(xhr.responseText);
        }
    });
}

function exportFile(){
        $.ajax({
            url:"addressbook/api/contacts/file",
            type:'GET',
            headers: { "AuthToken": getCookie(COOKIE_NAME)},
            processData:false,
            success:function resolve(result) {
                //$("#export").attr('href',result);
                //window.location.href =result;
                $('#export').attr('href',"data:application/octet-stream;base64," + result).show();
            },
            error:function (){
                alert(GENERIC_ERROR_MESSAGE);
            }
        });
}

function hideDownloadLink(){
    $('#export').hide();
}

$(document).ready(function func(){
    $("#visibleCreate").click(function(){
        $('#export').hide();
    });
    $("#submit").click(function(){
        $('#export').hide();
    });
    $(".editButton").click(function(){
        $('#export').hide();
    });
    $(".deleteButton").click(function(){
        $('#export').hide();
    });
})
;
