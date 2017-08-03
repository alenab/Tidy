$(function () {
    // showing calendar
    $('#datepicker').datepicker({
        onSelect: updateTimeLimits,
        altField: "#cleaningDate",
        dateFormat: "yy-mm-dd",
        maxDate: "+1m +1w",
        defaultDate: 0,
        minDate: 0
    });
});

function updateTimeLimits(date) {
    var $timeLimits = $('#timeLimits');
    $timeLimits.find('option').remove();
    $.get('/api/time-limitations/' + date, function (data) {
        $.each(data, function (idx, item) {
            var $option = $("").val(item).text(item);
            $timeLimits.append($option);
        });
    });
}
