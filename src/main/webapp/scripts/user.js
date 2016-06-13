/**
 * Created by birsan on 5/31/2016.
 */
function login(){
    var username=$("#username").val();
    var password=$("#password").val();
    var encPass=calcMD5(password);
    var data={"username":username,"password":encPass};
    $.ajax({
        url:"addressbook/user/authenticate",
        type:"POST",
        data:JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success:function(response){
                setCookie(COOKIE_NAME,response);
                window.location="index.html";
        },
        error: function (response) {
            alert(GENERIC_ERROR_MESSAGE);
        }
    });
}

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for(var i = 0; i <ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            console.log(c.substring(name.length,c.length));
            return c.substring(name.length,c.length);
        }
    }
    return "";
}

function setCookie(cname,cvalue){
    document.cookie=cname+"="+cvalue;
}