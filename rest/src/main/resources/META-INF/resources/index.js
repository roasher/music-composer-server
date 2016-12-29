var notes = [];

var retrieveNotesFromServer = function (numberOfNotes) {
    console.log("retrieving " + numberOfNotes + " notes");
    var request = new XMLHttpRequest();
    request.open("GET", "http://localhost:8080/getNotes?number=" + numberOfNotes, true);
    request.addEventListener("load", function () {
        var responsedata = eval("(" + request.responseText + ")");
        notes.push(responsedata);
        console.log(notes)
    });
    request.send(null);
};