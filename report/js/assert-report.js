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
    if (usecase != 'use_case' && usecase != "'use_case'" && !(usecase in assertSumMap)) {
      assertSumMap[usecase] = {ok: 0, notOk: 0};
    }

    if (status == 'OK' || status =='"OK"') {
      var sumOK = assertSumMap[usecase].ok + 1;
      assertSumMap[usecase].ok = sumOK;
    } else if (status == 'NOT_OK' || status =='"NOT_OK"') {
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
    row.append($("<input type='button' class='show-errors' value='ERRORS' data-usecase='" + x + "' />"));
    table.append(row);
  }
  $("#assert-sum-table").html('');
  $("#assert-sum-table").append(table);
  showErrors();
}

function showErrors() {
  $('.show-errors').unbind().click(function() {
    var usecase = this.getAttribute("data-usecase");
    var table = $("<table id='errors-assert-csv' />");
    $.each($('#full-assert-csv tr'), function() {
      var rowUseCase;
      var status;
      var row = $(this).clone();
      $.each(this.cells, function(cellIndex, cell) {
        if (cellIndex == 0) {
          rowUseCase = cell.textContent;
        }
        if (cellIndex == 3) {
          status = cell.textContent;
        }
      });
      if (rowUseCase == usecase || rowUseCase == 'use_case' || rowUseCase == "'use_case'") {
        if (status == 'NOT_OK' || status =='"NOT_OK"' || status == 'status' || status =='"status"') {
          table.append(row);
        }
      }

    });
    $("#assert-error-table").html('');
    $("#assert-error-table").append(table);
  });
}
