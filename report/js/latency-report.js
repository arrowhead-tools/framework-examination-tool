function showLatencyResults() {
  var chartDiv = $("<div id='latency-plotly-chart' />")
  $('#latency-chart').append(chartDiv)
  Plotly.plot('latency-plotly-chart', [{
    y:[],
    type: 'line'
  }]);
  getLatencyDataForChart();
}

function getLatencyDataForChart() {
$.each($('#full-latency-csv tr'), function() {
    $.each(this.cells, function(cellIndex, cell) {
      if (cellIndex == 3 && cell.textContent != 'latency_ms') {
        var numb = cell.textContent.replace(/\D/g,'');
        Plotly.extendTraces('latency-plotly-chart', { y:[[numb]] }, [0]);
      }
    });
  });
}
