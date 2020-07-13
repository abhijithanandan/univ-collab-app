/*var modal = document.getElementById('signup');
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
        document.getElementById('signup').style.display='none'; 
        document.getElementById('in-si1').value = '';
        document.getElementById('in-si2').value = '';
    }
}*/

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
