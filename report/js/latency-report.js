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
  var layout = {
    yaxis: {
      title: {
        text: 'latency (ms)'
      },
      rangemode: 'tozero',
      autorange: true
    },
    xaxis: {
      title: {
        text: 'number of requests'
      },
      rangemode: 'tozero',
      autorange: true
    }
  };
  Plotly.plot('latency-plotly-chart', [{
    y:[],
    type: 'line'
  }], layout);
  getLatencyDataForChart();
}

function getLatencyDataForChart() {
  return Promise.resolve().then(function() {
    setTimeout(function() {
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
      $('#loading-div').hide();
    }, 0);
  });
}
