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
        $priceTax.text((priceWithDiscount * 0.12).toFixed(2));
        $priceTotal.text((priceWithDiscount * 1.12).toFixed(2));
    } else {
        $formPrice.val(0);
        $price.text("N/A");

        $priceTax.text("N/A");
        $priceTotal.text("N/A");
    }
}