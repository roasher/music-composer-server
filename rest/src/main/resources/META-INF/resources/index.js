var playNextTBars = function (compositionId, amountOfBarsToLoad, player) {
    console.log("retrieving notes for " + amountOfBarsToLoad + " amountOfBarsToLoad");
    var request = new XMLHttpRequest();
    request.open("GET", "/getBars?compositionId=" + compositionId + "&numberOfBars=" + amountOfBarsToLoad, true);
    request.addEventListener("load", function () {
        var notes = eval("(" + request.responseText + ")").notes;
        console.log(notes);
        for (var instrumentNumber = 0; instrumentNumber < notes.length; instrumentNumber++) {
            for (var noteNumber = 0; noteNumber < notes[instrumentNumber].length; noteNumber++) {
                player.partPlayers[instrumentNumber].play(
                    notes[instrumentNumber][noteNumber].pitch,
                    notes[instrumentNumber][noteNumber].rhythmValue,
                    true);
            }
        }
    });
    request.send(null);
};