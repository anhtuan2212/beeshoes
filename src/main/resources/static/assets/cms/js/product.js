$(document).on('ready', function () {
    // ONLY DEV
    // =======================================================

    if (window.localStorage.getItem('hs-builder-popover') === null) {
        $('#builderPopover').popover('show')
            .on('shown.bs.popover', function () {
                $('.popover').last().addClass('popover-dark')
            });

        $(document).on('click', '#closeBuilderPopover', function () {
            window.localStorage.setItem('hs-builder-popover', true);
            $('#builderPopover').popover('dispose');
        });
    } else {
        $('#builderPopover').on('show.bs.popover', function () {
            return false
        });
    }

    // END ONLY DEV
    // =======================================================


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


    // INITIALIZATION OF NAV SCROLLER
    // =======================================================
    $('.js-nav-scroller').each(function () {
        new HsNavScroller($(this)).init()
    });


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
        var select2 = $.HSCore.components.HSSelect2.init($(this));
    });


    // INITIALIZATION OF DATATABLES
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
        },
        language: {
            zeroRecords: '<div class="text-center p-4">' +
                '<img class="mb-3" src="/assets/cms/svg/illustrations/sorry.svg" alt="Image Description" style="width: 7rem;">' +
                '<p class="mb-0">No data to show</p>' +
                '</div>'
        }
    });
    // Tạo số thứ tự || đang lỗi
    // datatable.on('order.dt search.dt', function() {
    //     datatable.columns(1, { search: 'applied', order: 'applied' }).nodes().each(function(cell, i) {
    //         cell.innerHTML = i + 1;
    //     });
    // }).draw();
    $('input[name="status"]').on('change', function() {
        var value = $(this).val();
        if (value === 'active') {
            datatable.columns(9).search('Đang Bán').draw();
        } else if (value === 'noActive') {
            datatable.columns(9).search('Chưa Bán').draw();
        } else {
            datatable.columns(9).search('').draw();
        }
    });
    $('#thuongHieu, #theLoai').on('change', function() {
        var thuongHieuValue = $('#thuongHieu').val();
        var theLoaiValue = $('#theLoai').val();
        var thuongHieuFilter = (thuongHieuValue === 'all') ? '' : thuongHieuValue;
        var theLoaiFilter = (theLoaiValue === 'all') ? '' : theLoaiValue;
        datatable.columns(4).search(thuongHieuFilter).draw();
        datatable.columns(3).search(theLoaiFilter).draw();
    });


    $('#datatableSearch').on('mouseup', function (e) {
        var $input = $(this),
            oldValue = $input.val();

        if (oldValue == "") return;

        setTimeout(function () {
            var newValue = $input.val();

            if (newValue == "") {
                // Gotcha
                datatable.search('').draw();
            }
        }, 1);
    });
    $('#toggleColumn_stt').change(function (e) {
        datatable.columns(1).visible(e.target.checked)
    })
    $('#toggleColumn_product').change(function (e) {
        datatable.columns(2).visible(e.target.checked)
    })
    $('#toggleColumn_type').change(function (e) {
        datatable.columns(3).visible(e.target.checked)
    })
    $('#toggleColumn_vendor').change(function (e) {
        datatable.columns(4).visible(e.target.checked)
    })
    datatable.columns(5).visible(false)
    $('#toggleColumn_stocks').change(function (e) {
        datatable.columns(5).visible(e.target.checked)
    })
    datatable.columns(6).visible(false)
    $('#toggleColumn_sku').change(function (e) {
        datatable.columns(6).visible(e.target.checked)
    })
    $('#toggleColumn_price').change(function (e) {
        datatable.columns(7).visible(e.target.checked)
    })
    $('#toggleColumn_quantity').change(function (e) {
        datatable.columns(8).visible(e.target.checked)
    })
    $('#toggleColumn_variants').change(function (e) {
        datatable.columns(9).visible(e.target.checked)
    })
    // $(window).on('load', function () {
    //     setTimeout(function () {
    //         $('#spinner').addClass('d-none');
    //         $('#dataInTable').removeClass('d-none');
    //     }, 3000);
    // });
    // INITIALIZATION OF TAGIFY
    // =======================================================
    $('.js-tagify').each(function () {
        var tagify = $.HSCore.components.HSTagify.init($(this));
    });


});
