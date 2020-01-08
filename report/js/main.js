$(document).ready(function() {
  readFullCSV();
});

//------------------------------------------------------------------------------

function readFullCSV() {
  $("#upload").bind("click", function () {
    readFullCSVAssert();
  });
}

function readFullCSVAssert() {
  var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)$/;
  if (regex.test($("#fileUpload").val().toLowerCase())) {
      if (typeof (FileReader) != "undefined") {
          var reader = new FileReader();
          reader.onload = function (e) {
              var table = $("<table id='full-assert-csv' />");
              var rows = e.target.result.split("\n");
              for (var i = 0; i < rows.length; i++) {
                var row = $("<tr />");
                var cells = rows[i].split(",");
                if (cells.length > 1) {
                    for (var j = 0; j < cells.length; j++) {
                        var cell = $("<td />");
                        cell.html(cells[j]);
                        row.append(cell);
                    }
                    table.append(row);
                }
              }
              $("#assert-csv").html('');
              $("#assert-csv").append(table);
          }
          reader.readAsText($("#fileUpload")[0].files[0]);
          reader.onloadend = function() {
            showUseCaseResults();
          };
      } else {
          alert("This browser does not support HTML5.");
      }
  } else {
      alert("Please upload a valid CSV file.");
  }
}
