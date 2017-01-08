var playNextTSeconds = function (seconds, whenShouldPlay) {
    console.log("retrieving notes for " + seconds + " seconds");
    var request = new XMLHttpRequest();
    request.open("GET", "/getNotes?number=" + seconds, true);
    request.addEventListener("load", function () {
        var responsedata = eval("(" + request.responseText + ")");
        var timeWhenLoaded = Date.now();
        var delayBeforePlay = whenShouldPlay >= timeWhenLoaded ? whenShouldPlay - timeWhenLoaded : 0 ;
        var noteStartTime = delayBeforePlay;
        for (var noteNumber = 0; noteNumber < responsedata.length; noteNumber++) {
            player.play(responsedata[noteNumber].pitch, responsedata[noteNumber].rhythmValue, true)
        }

    });
    request.send(null);
};