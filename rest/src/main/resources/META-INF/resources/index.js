var playNextTBars = function (amountOfBarsToLoad, whenShouldPlay) {
    console.log("retrieving notes for " + amountOfBarsToLoad + " amountOfBarsToLoad");
    var request = new XMLHttpRequest();
    request.open("GET", "/getNotes?number=" + amountOfBarsToLoad, true);
    request.addEventListener("load", function () {
        var responsedata = eval("(" + request.responseText + ")");
        for (var noteNumber = 0; noteNumber < responsedata.length; noteNumber++) {
            player.play(responsedata[noteNumber].pitch, responsedata[noteNumber].rhythmValue, true)
        }
    });
    request.send(null);
};