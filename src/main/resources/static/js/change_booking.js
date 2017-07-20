$(function () {
    $.get("/api/price-matrix", function (data) {
        $.priceList = data;
        updatePrice();
        updateBathroomsOptions();
    });

    $('select#cleaningPlan, select#numberOfRooms').on('change', function () {
        updateBathroomsOptions();
    });

    $('select, input[type="checkbox"], input#discount').on('change', function () {
        updatePrice();
    });
});

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

    var rawPrice = Number(res.toFixed(2));

    $('input[type="checkbox"]:checked').each(function () {
        rawPrice += Number($(this).data('price'));
    });

    $('input#price').val(rawPrice);
    var discount = $('input#discount').val();
    $('span.booking-discount').text(discount + "%");

    var priceWithDiscount = ((100 - discount) / 100) * rawPrice;
    $price.text(priceWithDiscount.toFixed(2));
    $priceTax.text((priceWithDiscount * 0.05).toFixed(2));
    $priceTotal.text((priceWithDiscount * 1.05).toFixed(2));
}
