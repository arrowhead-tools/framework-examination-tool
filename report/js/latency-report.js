$(document).ready(function() {
  $('#latency-filter-refresh').click(function() {
    showLatencyResults();
  });
});

function showLatencyResults() {
  $('#latency-chart').html('');
  $('#loading-div').show();
  var chartDiv = $("<div id='latency-plotly-chart' />");
  $('#latency-chart').append(chartDiv)
  Plotly.plot('latency-plotly-chart', [{
    y:[],
    type: 'line'
  }]);
  getLatencyDataForChart();
  $('#loading-div').hide();
}

function getLatencyDataForChart() {
  var usecaseFilter = $('#usecase-filter').val();
  var endpointFilter = $('#endpoint-filter').val();

  $.each($('#full-latency-csv tr'), function() {
    var usecase = "";
    var endpoint = "";
    $.each(this.cells, function(cellIndex, cell) {
      if (cellIndex == 0) {
        usecase = cell.textContent;
      }
      if (cellIndex == 2) {
        endpoint = cell.textContent;
      }

      if (cellIndex == 3 && cell.textContent != 'latency_ms' && (usecase == usecaseFilter || usecaseFilter == "none") && (endpoint == endpointFilter || endpointFilter == "none")) {
        var numb = cell.textContent.replace(/\D/g,'');
        Plotly.extendTraces('latency-plotly-chart', { y:[[numb]] }, [0]);
      }
    });
  });
}
