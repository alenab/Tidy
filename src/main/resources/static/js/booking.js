// onload jQuery function
$(function () {
    $.get("/api/price-matrix", function (data) {
        $.priceList = data;
        updatePrice();
        updateBathroomsOptions();
    });

    $('select#cleaningPlan, select#numberOfRooms').on('change', function () {
        updateBathroomsOptions();
    });

    $('select#cleaningPlan').on('change', function() {
        updateOptions(this.value);
    });

    $('select').on('change', function () {
        updatePrice();
    });

    $('input[type="checkbox"]').on('change', function () {
        updatePrice();
    });

    // showing calendar
    $('#datepicker').datepicker({
        onSelect: updateTime,
        altField: "#cleaningDate",
        dateFormat: "yy-mm-dd",
        maxDate: "+1m +1w",
        defaultDate: +1,
        minDate: 0
    });

    // initial request to show time values from the very beginning
    updateTime($('#cleaningDate').val());

    updateOptions($('#cleaningPlan').val());
});

function updateTime(cleaningDate) {
    var $cleaningTime = $('#cleaningTime');
    $cleaningTime.find('option').remove();

    $.get('/api/free-time/' + cleaningDate, function (data) {
        $.each(data, function (idx, item) {
            var $option = $("<option/>").val(item).text(item);
            $cleaningTime.append($option);
        });
    });
}

function updateOptions(planId) {
    $.get('/api/options/' + planId, function (data) {
        var $div = $('div.extraOptions');
        $div.find('div').remove();

        // var $rowDiv = $('<div/>').addClass('row');
        var $ol = $('<ol/>');
        $.each(data, function (idx, item) {
            // var element = $('<div class="col-md-4"/>');
            var element = $('<span/>');
            element.addClass('option-item');
            var $img = $('<img/>')
                .attr("src", "/images/options/" + item.imgName + ".png")
                .addClass('img-responsive');

            $img.attr('width', '60px');
            $img.attr('height', '60px');
            // element.append($('<div class="col-md-4"/>').append($img));
            element.append($('<span/>').append($img));

            // var $subDiv = $('<div class="col-md-8"/>');
            var $subDiv = $('<span>');
            $subDiv.append($('<span/>').text(item.name));
            $subDiv.append('<br/>');
            $subDiv.append($('<span/>').text('$' + item.price));
            element.append($subDiv);

            // $rowDiv.append(element);
            // if ((idx + 1) % 3 === 0) {
            //     $div.append($rowDiv);
            //     $rowDiv = $('<div/>').addClass('row');
            // }
            $ol.append($('<li/>').addClass('ui-state-default').append(element));
        });
        $div.append($ol);
        $ol.selectable({filter: "span.option-item"});
    });
}

function getPrice(planId, rooms, baths) {
    var resAr = $.priceList.filter(function (item) {
        return item.planId === planId && item.rooms === rooms && item.bathrooms === baths;
    });
    return resAr.length > 0 ? resAr[0].price : 0;
}

function updateBathroomsOptions() {
    var rooms = Number($('#numberOfRooms').val());
    var planId = Number($('#cleaningPlan').val());
    // filtered bathrooms, only ones which fits the plan and rooms quantity
    var resAr = $.priceList.filter(function (item) {
        return item.planId === planId && item.rooms === rooms;
    });

    // clean up list of bathroom options in html
    var $bathrooms = $("#numberOfBathrooms");
    var originallySelected = $bathrooms.find("option:selected").val();
    $bathrooms.find("option").remove();

    // forming new list of available options
    $.each(resAr, function (idx, item) {
        var text = item.bathrooms === 1 ? "bathroom" : "bathrooms";
        var $option = $("<option/>").val(item.bathrooms).text(item.bathrooms + " " + text);
        if (Number(item.bathrooms) === Number(originallySelected)) {
            $option.attr('selected', 'selected');
        }
        $bathrooms.append($option);
    });
}

function updatePrice() {
    var $price = $('span.booking-price');
    var $priceTax = $('span.booking-price-tax');
    var $priceTotal = $('span.booking-price-total');

    var planId = Number($('#cleaningPlan').val());
    var rooms = Number($('#numberOfRooms').val());
    var baths = Number($('#numberOfBathrooms').val());
    var res = getPrice(planId, rooms, baths);

    var $formPrice = $('input[type="hidden"]#price');
    if (res !== 0) {
        var rawPrice = res;

        $('input[type="checkbox"]:checked').each(function () {
            rawPrice += Number($(this).data('price'));
        });

        var $discountTag = $('span.booking-discount');
        var discount = $discountTag.length ? Number($discountTag.data('discount')) : 0;

        $formPrice.val(rawPrice.toFixed(2));
        var priceWithDiscount = ((100 - discount) / 100) * rawPrice;
        $price.text(priceWithDiscount.toFixed(2));
        $priceTax.text((priceWithDiscount * 0.05).toFixed(2));
        $priceTotal.text((priceWithDiscount * 1.05).toFixed(2));
    } else {
        $formPrice.val(0);
        $price.text("N/A");

        $priceTax.text("N/A");
        $priceTotal.text("N/A");
    }
}