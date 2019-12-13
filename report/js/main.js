$(function () {
    $("#upload").bind("click", function () {
      readFullCSV();

    });
});

function readFullCSV() {
  var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)$/;
  if (regex.test($("#fileUpload").val().toLowerCase())) {
      if (typeof (FileReader) != "undefined") {
          var reader = new FileReader();
          reader.onload = function (e) {
              var table = $("<table id='full-assert-csv' />");
              var rows = e.target.result.split("\n");
              for (var i = 0; i < rows.length; i++) {
                  if (i > 0) {
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

function showUseCaseResults() {
  var assertSumMap = {};
  $.each($('#full-assert-csv tr'), function() {
    var usecase;
    var status;
    $.each(this.cells, function(cellIndex, cell) {
      if (cellIndex == 0) {
        usecase = cell.textContent;
      }
      if (cellIndex == 3) {
        status = cell.textContent;
      }
    });
    if (!(usecase in assertSumMap)) {
      assertSumMap[usecase] = {ok: 0, notOk: 0};
    }

    if (status == 'OK' || status =='"OK"') {
      var sumOK = assertSumMap[usecase].ok + 1;
      assertSumMap[usecase].ok = sumOK;
    } else {
      var sumNotOK = assertSumMap[usecase].notOk + 1;
      assertSumMap[usecase].notOk = sumNotOK;
    }
  });
  var table = $("<table id='sum-assert-csv' />");
  var row = $("<tr class='table-header'/>");
  row.append($("<td />").html("USE CASE"));
  row.append($("<td />").html("OK"));
  row.append($("<td />").html("NOT OK"));
  table.append(row);
  for (var x in assertSumMap) {
    var row = $("<tr class='table-row'/>");
    var useCaseCell = $("<td />").html(x);
    var okCell = $("<td />").html(assertSumMap[x].ok);
    var notOkCell = $("<td />").html(assertSumMap[x].notOk);
    row.append(useCaseCell);
    row.append(okCell);
    row.append(notOkCell);
    table.append(row);
  }
  $("#assert-sum-table").html('');
  $("#assert-sum-table").append(table);
}
