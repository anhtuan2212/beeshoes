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

    // INITIALIZATION OF QUANTITY COUNTER
    // =======================================================
    $('.js-quantity-counter').each(function () {
        var quantityCounter = new HSQuantityCounter($(this)).init();
    });


    // INITIALIZATION OF ADD INPUT FILED
    // =======================================================
    $('.js-add-field').each(function () {
        new HSAddField($(this), {
            addedField: function() {
                $('.js-add-field .js-select2-custom-dynamic').each(function () {
                    var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
                });

                $('[data-toggle="tooltip"]').tooltip();


                // INITIALIZATION OF QUANTITY COUNTER
                // =======================================================
                $('.js-quantity-counter').each(function () {
                    var quantityCounter = new HSQuantityCounter($(this)).init();
                });
            },
            deletedField: function() {
                $('.tooltip').hide();
            }
        }).init();
    });


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
    // $('.js-add-field').each(function () {
    //     new HSAddField($(this), {
    //         addedField: function () {
    //             $('.js-add-field .js-select2-custom-dynamic').each(function () {
    //                 var select2dynamic = $.HSCore.components.HSSelect2.init($(this));
    //             });
    //         }
    //     }).init();
    // });


    // INITIALIZATION OF TAGIFY
    // =======================================================
    $('.js-tagify').each(function () {
        var tagify = $.HSCore.components.HSTagify.init($(this));
    });


    // INITIALIZATION OF DROPZONE FILE ATTACH MODULE
    // =======================================================
    $('.js-dropzone').each(function () {
        var dropzone = $.HSCore.components.HSDropzone.init('#' + $(this).attr('id'));
    });


    // INITIALIZATION OF STEP FORM
    // =======================================================
    $('.js-step-form').each(function () {
        var stepForm = new HSStepForm($(this), {
            finish: function () {
                $("#checkoutStepFormProgress").hide();
                $("#checkoutStepFormContent").hide();
                $("#checkoutStepOrderSummary").hide();
                $("#checkoutStepSuccessMessage").show();
            }
        }).init();
    });


    // INITIALIZATION OF MASKED INPUT
    // =======================================================
    $('.js-masked-input').each(function () {
        var mask = $.HSCore.components.HSMask.init($(this));
    });
});


<!-- IE Support -->

if (/MSIE \d|Trident.*rv:/.test(navigator.userAgent)) document.write('<script src="/assets/vendor/babel-polyfill/polyfill.min.js"><\/script>');
