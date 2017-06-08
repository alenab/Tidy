// onload jQuery function
$(function () {
    $.get("/api/price-matrix", function (data) {
        $.priceList = data;
        updatePrice();
    });

    $('select').on('change', function () {
        updatePrice();
    });

    $('input[type="checkbox"]').on('change', function () {
        updatePrice();
    })
});

function getPrice(planId, rooms, baths) {
    var resAr = $.priceList.filter(function (item) {
        return item.planId === planId && item.rooms === rooms && item.bathrooms === baths;
    });
    return resAr.length > 0 ? resAr[0].price : 0;
}

function updatePrice() {
    var $price = $('span.booking-price');
    var $priceTax = $('span.booking-price-tax');
    var $priceTotal = $('span.booking-price-total');

    var planId = Number($('#cleaningPlan').val());
    var rooms = Number($('#numberOfRooms').val());
    var baths = Number($('#numberOfBathrooms').val());
    var res = getPrice(planId, rooms, baths);
    if (res !== 0) {
        var price = res;

        $('input[type="checkbox"]:checked').each(function () {
            price += Number($(this).data('price'));
        });

        $price.text(price.toFixed(2));
        $priceTax.text((price * 0.12).toFixed(2));
        $priceTotal.text((price * 1.12).toFixed(2));
    } else {
        $price.text("N/A");
        $priceTax.text("N/A");
        $priceTotal.text("N/A");
    }
}