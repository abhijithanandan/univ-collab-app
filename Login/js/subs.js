var modal = document.getElementById('signup');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
        document.getElementById('signup').style.display='none'; 
        document.getElementById('in-si1').value = '';
        document.getElementById('in-si2').value = '';
    }
}
 
function addAttr() { 
    document.getElementById('ip-sp1').setAttribute("required");
    document.getElementById('ip-sp2').setAttribute("required");
    document.getElementById('ip-sp3').setAttribute("required");
    document.getElementById('ip-sp4').setAttribute("required");
    document.getElementById('ip-sp5').setAttribute("required");
    document.getElementById('ip-sp6').setAttribute("required");
    document.getElementById('ip-sp7').setAttribute("required");
    document.getElementById('ip-sp8').setAttribute("required");
    document.getElementById('ip-sp9').setAttribute("required");

}


function removeAttr() { 
    document.getElementById('signup').style.display='block';
    document.getElementById('ip-sp1').removeAttribute("required");
    document.getElementById('ip-sp2').removeAttribute("required");
    document.getElementById('ip-sp3').removeAttribute("required");
    document.getElementById('ip-sp4').removeAttribute("required");
    document.getElementById('ip-sp5').removeAttribute("required");
    document.getElementById('ip-sp6').removeAttribute("required");
    document.getElementById('ip-sp7').removeAttribute("required");
    document.getElementById('ip-sp8').removeAttribute("required");
    document.getElementById('ip-sp9').removeAttribute("required");
}

document.addEventListener("DOMContentLoaded", function() {
    var elements = document.getElementsByTagName("INPUT");
    for (var i = 0; i < elements.length; i++) {
        elements[i].oninvalid = function(e) {
            e.target.setCustomValidity("");
            if (!e.target.validity.valid) {
                e.target.setCustomValidity("This field cannot be left blank");
            }
        };
        elements[i].oninput = function(e) {
            e.target.setCustomValidity("");
        };
    }
})

var imgArray = ['img/analyze.svg','img/bg.svg','img/reading.svg','img/discus.svg','img/brstorm.svg','img/answer.svg','img/busdis.svg','img/message.svg','img/nature.svg','img/secure.svg','img/talk.svg','img/training.svg'],
    curIndex = 0;
    imgDuration = 10000;

function slideShow() {
    document.getElementById('slider').className += "fadeOut";
    setTimeout(function() {
        document.getElementById('slider').src = imgArray[curIndex];
        document.getElementById('slider').className = "";
    },1000);
    curIndex++;
    if (curIndex == imgArray.length) { curIndex = 0; }
    setTimeout(slideShow, imgDuration);
}

slideShow();