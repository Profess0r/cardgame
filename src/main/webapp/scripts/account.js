"use strict";

var imagesLoaded = false;
var newAvatarNumber;

function showImages() {
    if (!imagesLoaded) {
        var xmlhttp;
        if (window.XMLHttpRequest) {
            //code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp=new XMLHttpRequest();
        } else {
            //code for IE6, IE5
            xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
        }

        xmlhttp.onreadystatechange = function() {
            if (xmlhttp.readyState==4 && xmlhttp.status==200) {
                document.getElementById("avatarImages").innerHTML=xmlhttp.responseText;
            }
        };
        xmlhttp.open("GET","avatarImages.do",false);
        xmlhttp.send();

        imagesLoaded = true;
    }

    document.getElementById("avatarImages").hidden = document.getElementById("avatarImages").hidden !== true;

    imageListenersOn();
}

function imageListenersOn() {
    var images = document.getElementsByClassName('avatarImage');
    for (var i = 0; i < images.length; i++) {
        images[i].addEventListener('click', chooseImage, false);
    }
}

function chooseImage() {
    newAvatarNumber = this.getAttribute('id');
    var images = document.getElementsByClassName('avatarImage');
    for (var i = 0; i < images.length; i++) {
        images[i].style.borderColor = "white";
    }
    this.style.borderColor = "black";
}

function confirmAvatarChange() {

    var xmlhttp;
    if (window.XMLHttpRequest) {
        //code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    } else {
        //code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }

    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState==4 && xmlhttp.status==200) {
            //alert("avatar updated");
            document.getElementById("avatar").innerHTML=xmlhttp.responseText;
        }
    };

    xmlhttp.open("GET","setAvatar.do?id=" + newAvatarNumber,false);
    xmlhttp.send();
}




function changePassword() {

}