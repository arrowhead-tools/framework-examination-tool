$(document).ready(function() {
  readFullCSV();
});

//------------------------------------------------------------------------------

function readFullCSV() {
  $("#upload").bind("click", function () {
    $("#loading-div").show();
    $("#assert-sum-table").html('');
    $("#assert-error-table").html('');
    $("#assert-csv").html('');
    $("#latency-filters").hide();
    $("#latency-chart").html('');
    $("#latency-csv").html('');

    if ($("#fileUpload").val().toLowerCase().includes("assert")) {
      readFullCSVAssert();
    } else if ($("#fileUpload").val().toLowerCase().includes("latency")) {
      readFullCSVLatency();
      $("#latency-filters").show();
    } else {
      alert("Invalid file name!");
      $("#fileUpload").val("");
    }
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

function readFullCSVLatency() {
  var usecaseSet = new Set();
  var endpointSet = new Set();

  var regex = /^([a-zA-Z0-9\s_\\.\-:])+(.csv|.txt)$/;
  if (regex.test($("#fileUpload").val().toLowerCase())) {
      if (typeof (FileReader) != "undefined") {
          var reader = new FileReader();
          reader.onload = function (e) {
              var table = $("<table id='full-latency-csv' />");
              var rows = e.target.result.split("\n");
              for (var i = 0; i < rows.length; i++) {
                var row = $("<tr />");
                var cells = rows[i].split(",");
                if (cells.length > 1) {
                    for (var j = 0; j < cells.length; j++) {
                        var cell = $("<td />");
                        cell.html(cells[j]);
                        row.append(cell);
                        if (j == 0 && cells[j] != 'use_case' && !usecaseSet.has(cells[j])) {
                          $('#usecase-list').append("<option value='" + cells[j] + "'>");
                          usecaseSet.add(cells[j]);
                        }
                        if (j == 2 && cells[j] != 'endpoint' && !endpointSet.has(cells[j])) {
                          $('#endpoint-list').append("<option value='" + cells[j] + "'>");
                          endpointSet.add(cells[j]);
                        }
                    }
                    table.append(row);
                }
              }
              $("#latency-csv").html('');
              $("#latency-csv").append(table);
          }
          reader.readAsText($("#fileUpload")[0].files[0]);
          reader.onloadend = function() {
            showLatencyResults();
          };
      } else {
          alert("This browser does not support HTML5.");
      }
  } else {
      alert("Please upload a valid CSV file.");
  }
}
