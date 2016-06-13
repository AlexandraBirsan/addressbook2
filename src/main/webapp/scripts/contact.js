/**
 * Created by birsan on 4/13/2016.
 */
var AUTH_TOKEN = "AuthToken";
var INPUT_WITH_ADD_BUTTON = "<input type='button' onclick='addPhoneNumber()' value='+'/>"
var currentEncodedPhoto;
var currentContentType;

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
        "company": company, "phoneNumbers": phoneNumbers,
        "photo": currentEncodedPhoto, "contentType": currentContentType
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
    var photo = object.getAttribute("data-photo");
    var contentType = object.getAttribute("data-content-type");
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
        $('#previewContactPhoto').attr("src", "data:" + contentType + ";base64," + photo);
        $('#previewContactPhoto').show();
    }
    $('#ContactDiv').show();
    $('errorsDiv').hide();
}


function loadDataTable() {
    $('#listTable').DataTable({
        "processing": true,
        "serverSide": false,
        "ajax": {
            url: "addressbook/api/contacts",
            headers: {"AuthToken": getCookie(COOKIE_NAME)}
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
                    if (object && object.contentType) {
                        return "<img src='data:" + object.contentType + ";base64," + object.photo + "' height='80' alt='image'>";
                    } else {
                        return "<img src='' height='60' alt='image'>";
                    }

                }
            },
            {"data": "contentType"},
            {
                "data": null,
                "render": function (object) {
                    return '<button data-company="' + object.company + '" data-first-name="' + object.firstName + '" data-last-name="' + object.lastName + '" data-id="' + object.id +
                        '"data-phone-number="' + object.phoneNumber + '" data-content-type="' + object.contentType + '" data-photo="' + object.photo +
                        '" onclick="loadContactData(this)" class=' + "editButton" + '>' + 'Edit' + '</button>';
                }
            },
            {
                "data": null,
                "render": function (object) {
                    return '<button  data-id="' + object.id + '" onclick="invokeDeleteContact(this)" class=' + "deleteButton" + '>' + 'Delete' + '</button>';
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
                "targets": [2],
                "visible": false,
                "searchable": false
            },
            {
                "targets": [5],
                "visible": true,
                "searchable": false
            },
            {
                "targets": [6],
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

function getEncodedPhotoAndContentType() {
    var file = document.querySelector('input[type=file]').files[0];
    var reader = new FileReader();
    reader.onload = function (readerEvt) {
        var binaryString = readerEvt.target.result;
        currentEncodedPhoto = (btoa(binaryString));
        currentContentType = file.type;
    };
    reader.readAsBinaryString(file);
}

$(document).ready(function () {
    $('#visibleCreate').click(function showContact() {
        loadContactData(this)
    });
    loadDataTable();
});