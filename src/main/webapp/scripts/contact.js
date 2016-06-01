/**
 * Created by birsan on 4/13/2016.
 */
var AUTH_TOKEN = "AuthToken";
var INPUT_WITH_ADD_BUTTON = "<input type='button' onclick='addPhoneNumber()' value='+'/>"

function createOrUpdateContact() {
    var firstName = $('#firstName').val();
    var lastName = $('#lastName').val();
    var company = $('#company').val();
    var id = $('#id').val();
    var rawPhoneNumbers = getPhoneNumbers();
    var phoneNumbers = [];
    for (var i = 0; i < rawPhoneNumbers.length; i++) {
        phoneNumbers[i] = {"number": rawPhoneNumbers[i]}
    }
    var data = {
        "id": id, "firstName": firstName, "lastName": lastName,
        "company": company, "phoneNumbers": phoneNumbers
    };

    if (id === "") {
        invokeCreateContact(data);
    }
    else {
        invokeUpdateContact(data);
    }
}

function getPhoneNumbers() {
    var my_array = [];
    $(":input[class^=phoneNumber]").each(function (index, element) {
        my_array.push(element.value);
    });
    return my_array;
}

function loadContactData(object) {
    var company = object.getAttribute("data-company");
    var firstName = object.getAttribute("data-first-name");
    var lastName = object.getAttribute("data-last-name");
    var id = object.getAttribute("data-id");
    var phoneNumber = object.getAttribute("data-phone-number");
    $('#company').val(company);
    $('#firstName').val(firstName);
    $('#lastName').val(lastName);
    $('#id').val(id);
    if (id === null) {
        clearAllRowsFromTable("phoneNumTable");
        $('#phoneNumTableBody').append(
            "<tr><td><input type='text' class='phoneNumber' name='phoneNumber'/></td><td>" + INPUT_WITH_ADD_BUTTON + "</td></tr>"
        );
    } else {
        clearAllRowsFromTable("phoneNumTable");
        if (phoneNumber != null) {
            var phoneNumbers = phoneNumber.replace(/\s+/g, '')
            phoneNumbers = phoneNumbers.split(',');
            for (var i = 0; i < phoneNumbers.length; i++) {
                appendRowToTablePhoneNumbers("phoneNumTable", phoneNumbers[i]);
                if (i == 0) {
                    $("#phoneNumTableBody").children('tr:first').find('td:last').append(INPUT_WITH_ADD_BUTTON);
                }
            }
        }
    }
    $('#ContactDiv').show();
    $('errorsDiv').hide();
}



function loadDataTable() {
    $('#listTable').DataTable({
        "processing": true,
        "serverSide": false,
        "ajax":{
            url: "addressbook/api/contacts",
            headers: {  "AuthToken": getCookie(COOKIE_NAME)}
        },
        "columns": [
            {"data": "company"},
            {"data": "firstName"},
            {"data": "id"},
            {"data": "lastName"},
            {"data": "phoneNumber"},
            {
                "data": null,
                "render": function (object) {
                    return '<button data-company="' + object.company + '" data-first-name="' + object.firstName + '" data-last-name="' + object.lastName + '" data-id="' + object.id +
                        '"data-phone-number="' + object.phoneNumber + '" onclick="loadContactData(this)" class=' + "editButton" + '>' + 'Edit' + '</button>';
                }
            },
            {
                "data": null,
                "render": function (object) {
                    return '<button  data-id="' + object.id + '" onclick="invokeDeleteContact(this)">' + 'Delete' + '</button>';
                }
            }
        ],
        "order": [[0, "asc"], [2, "asc"]],
        "columnDefs": [
            {
                "targets": 'no-sort',
                "orderable": false
            },
            {
                "targets": 'sort',
                "orderable": true,
                "searchable": true
            },
            {
                "targets":[2],
                "visible": false,
                "searchable": false
            }],
        "pagingType": "simple"
    });
}

function addPhoneNumber() {
    appendRowToTablePhoneNumbers("phoneNumTable", "");
}

function appendRowToTablePhoneNumbers(table, text) {
    $('#' + table + '> tbody').append(
        "<tr>" +
        "<td>" +
        "<input type='text' class='phoneNumber' name='phoneNumber' value='" + text + "'/>" +
        "</td>" +
        "<td></td>" +
        "</tr>");
}

function reloadDataTable() {
    $('#listTable').DataTable().ajax.reload();
}

function gotThis() {
    $('#errors').text("");
    $('#errorsDiv').hide();
}

$(document).ready(function () {
    $('#visibleCreate').click(function showContact() {
        loadContactData(this)
    });
    loadDataTable();
});