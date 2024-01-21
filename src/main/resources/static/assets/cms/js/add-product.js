
    function backToSelected(url, element) {
    if (element) {
    var ele = $('div[data-img-src="' + url + '"]');
    if (ele.length > 0) {
    var div = ele.parent();
    $(element).removeClass("dis-selected-img");
    $(element).addClass("selected-img");
    $(element).text("Chọn");
    var target = $("#anh_co_san");
    if (target.length > 0) {
    target.append(div);
} else {
    return false;
}
} else {
    return false;
}
} else {
    return false;
}
}

    $(document).on('ready', function () {
    $('.js-fancybox-item').each(function () {
        var fancybox = $.HSCore.components.HSFancyBox.init($(this));
        // console.log(fancybox);
    })
});
    $(document).ready(function () {
    $('.money-input-mask').mask('#.##0đ', {reverse: true});
});
    $(document).ready(function () {
        $('.money-input-mask-num').mask('#.##0', {reverse: true});
    });
    $(document).on('ready', function () {
    // BUILDER TOGGLE INVOKER
    // =======================================================
    $('.js-navbar-vertical-aside-toggle-invoker').click(function () {
        $('.js-navbar-vertical-aside-toggle-invoker i').tooltip('hide');
    });


    // INITIALIZATION OF MEGA MENU
    // =======================================================
    var megaMenu = new HSMegaMenu($('.js-mega-menu'), {
    desktop: {
    position: 'left'
}
}).init();


    // INITIALIZATION OF NAVBAR VERTICAL NAVIGATION
    // =======================================================
    var sidebar = $('.js-navbar-vertical-aside').hsSideNav();


    // INITIALIZATION OF TOOLTIP IN NAVBAR VERTICAL MENU
    // =======================================================
    $('.js-nav-tooltip-link').tooltip({boundary: 'window'})

    $(".js-nav-tooltip-link").on("show.bs.tooltip", function (e) {
    if (!$("body").hasClass("navbar-vertical-aside-mini-mode")) {
    return false;
}
});


    // INITIALIZATION OF UNFOLD
    // =======================================================
    $('.js-hs-unfold-invoker').each(function () {
    var unfold = new HSUnfold($(this)).init();
});


    // INITIALIZATION OF FORM SEARCH
    // =======================================================
    $('.js-form-search').each(function () {
    new HSFormSearch($(this)).init()
});


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
    var select2 = $.HSCore.components.HSSelect2.init($(this));
});


    // INITIALIZATION OF QUILLJS EDITOR
    // =======================================================
    var quill = $.HSCore.components.HSQuill.init('.js-quill');

    // INITIALIZATION OF ADD INPUT FILED
    // =======================================================
    $('.js-add-field').each(function () {
    new HSAddField($(this), {
    addedField: function () {
    $('.js-add-field .js-select2-custom-dynamic').each(function () {
    var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
});
}
}).init();
});


    // INITIALIZATION OF TAGIFY
    // =======================================================
    $('.js-tagify').each(function () {
    var tagify = $.HSCore.components.HSTagify.init($(this));
});
    // DATATABLES
    // =======================================================
    var datatable = $.HSCore.components.HSDatatables.init($('#datatable'), {
    select: {
    style: 'multi',
    selector: 'td:first-child input[type="checkbox"]',
    classMap: {
    checkAll: '#datatableCheckAll',
    counter: '#datatableCounter',
    counterInfo: '#datatableCounterInfo'
}
}
});

    // INITIALIZATION OF DROPZONE FILE ATTACH MODULE
    // =======================================================
    $('.js-dropzone').each(function () {
    var dropzone = $.HSCore.components.HSDropzone.init('#' + $(this).attr('id'));
    // console.log(dropzone)
});
});
    $(document).ready(function () {
    $('#mauSac').on('change', function () {
        var mausac = $('#mauSac').val();
        mausac.forEach((mau) => {
            var ele = $('li[title="' + mau + '"]');
            var tenmau = $('option[value="' + mau + '"]').attr('data-name');
            var span = $(ele[0]).find('span:not(.select2-selection__choice__remove)').eq(0);
            $(span[0]).text(tenmau);
            $(ele[0]).css('background-color', mau);
            $(ele[0]).css('color', '#FFFFFF');
        });
    });
});

