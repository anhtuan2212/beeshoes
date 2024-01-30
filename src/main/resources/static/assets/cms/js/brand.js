var datatable = null;
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


    // INITIALIZATION OF SHOW PASSWORD
    // =======================================================
    $('.js-toggle-password').each(function () {
        new HSTogglePassword(this).init()
    });


    // INITIALIZATION OF FILE ATTACH
    // =======================================================
    $('.js-file-attach').each(function () {
        var customFile = new HSFileAttach($(this)).init();
    });


    // INITIALIZATION OF TABS
    // =======================================================
    $('.js-tabs-to-dropdown').each(function () {
        var transformTabsToBtn = new HSTransformTabsToBtn($(this)).init();
    });


    // INITIALIZATION OF STEP FORM
    // =======================================================
    $('.js-step-form').each(function () {
        var stepForm = new HSStepForm($(this), {
            finish: function () {
                $("#addUserStepFormProgress").hide();
                $("#addUserStepFormContent").hide();
                $("#successMessageContent").show();
            }
        }).init();
    });


    // INITIALIZATION OF MASKED INPUT
    // =======================================================
    $('.js-masked-input').each(function () {
        var mask = $.HSCore.components.HSMask.init($(this));
    });


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
        var select2 = $.HSCore.components.HSSelect2.init($(this));
    });


    // INITIALIZATION OF COUNTERS
    // =======================================================
    $('.js-counter').each(function () {
        var counter = new HSCounter($(this)).init();
    });


    // INITIALIZATION OF DATATABLES
    // =======================================================
     datatable = $.HSCore.components.HSDatatables.init($('#datatable'), {
        dom: 'Bfrtip',
        buttons: [
            {
                extend: 'copy',
                className: 'd-none'
            },
            {
                extend: 'excel',
                className: 'd-none'
            },
            {
                extend: 'csv',
                className: 'd-none'
            },
            {
                extend: 'pdf',
                className: 'd-none'
            },
            {
                extend: 'print',
                className: 'd-none'
            },
        ],
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

    $('#export-copy').click(function () {
        datatable.button('.buttons-copy').trigger()
    });

    $('#export-excel').click(function () {
        datatable.button('.buttons-excel').trigger()
    });

    $('#export-csv').click(function () {
        datatable.button('.buttons-csv').trigger()
    });

    $('#export-pdf').click(function () {
        datatable.button('.buttons-pdf').trigger()
    });

    $('#export-print').click(function () {
        datatable.button('.buttons-print').trigger()
    });

    $('.js-datatable-filter').on('change', function () {
        var $this = $(this),
            elVal = $this.val(),
            targetColumnIndex = $this.data('target-column-index');

        datatable.column(targetColumnIndex).search(elVal).draw();
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


    // INITIALIZATION OF QUICK VIEW POPOVER
    // =======================================================
    $('#editUserPopover').popover('show');

    $(document).on('click', '#closeEditUserPopover', function () {
        $('#editUserPopover').popover('dispose');
    });

    $('#editUserModal').on('show.bs.modal', function () {
        $('#editUserPopover').popover('dispose');
    });


    // DARK POPOVER
    // =======================================================
    $('[data-toggle="popover-dark"]').on('shown.bs.popover', function () {
        $('.popover').last().addClass('popover-dark')
    })
    //===============================================================================================
    $('#btn-add-new').on('click',function () {
        $('#editUserModal').modal('show');
        $('#inputData').val('').focus();
        $('#inputDataId').val('');
    })


    $('#save').on('click', function () {
        let id = $('#inputDataId').val();
        let name = $('#inputData').val();
        if (name.length===0){
            ToastError("Tên không được trống.");
            return;
        }
        $.ajax({
            url: '/api/them-thuong-hieu',
            type: 'POST',
            data: {
                id: id,
                ten: name
            },
            success: function (data, status, xhr) {
                let st = xhr.getResponseHeader('status');
                switch (st) {
                    case "oke":ToastSuccess("Thêm thành công.")
                        location.reload();
                        break;
                    default: ToastError("Thất Bại.")
                }
            },error:function (xhr) {
                let st = xhr.getResponseHeader('status');
                console.log(st)
                switch (st) {
                    case "existsByTen":ToastError("Tên đã tồn tại.")
                        break;
                    case "nameNull":ToastError("Tên không được trống.")
                        break;
                    case "oke":ToastSuccess("Thêm thành công.")
                        break;
                    default: ToastError("Thất Bại.")
                }
            }
        })
    })
});
function formatNumber(num) {
    return num < 10 ? '0' + num : num;
}

function formatDate(date) {
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    var formattedDate = formatNumber(hours) + ':' + formatNumber(minutes) + ' | ' + formatNumber(day) + '-' + formatNumber(month) + '-' + year;
    return formattedDate;
}
function edit(element) {
    let name = element.getAttribute('data-name');
    let id = element.getAttribute('data-id');
    $('#inputData').val(name).focus();
    $('#inputDataId').val(id);
}
function Toast(status, message) {
    $('#systoast').toast({delay: 5000, autohide: true, animation: true,});
    $('#systoast').toast('show');
    $('#system-toast-mesage').text(message);
    if (status == 'success') {
        $('#img-toast').attr('src', '/assets/cms/img/icon/success.svg')
        $('#toast-status').text("Thành Công !");
    } else if (status == 'error') {
        $('#img-toast').attr('src', '/assets/cms/img/icon/error.svg')
        $('#toast-status').text("Thất Bại !");
    } else {
        $('#toast-status').text("Sai Giá trị status !");
    }
}

function ToastSuccess(message) {
    Toast('success', message)
}

function ToastError(message) {
    Toast('error', message)
}

function deleteCategory(element) {
    let id = $(element).attr('data-id');

    if (confirm('Xác nhận xóa !')) {
        $.ajax({
            url: "/api/xoa-thuong-hieu",
            type: "DELETE",
            data: {
                id:id
            },
            success: function () {
                ToastSuccess('Xóa Thành Công.');
                location.reload();
            },
            error: function (xhr, status, error) {
                let st = xhr.getResponseHeader('status');
                if (st=="constraint"){
                    ToastError('Thể loại được gắn với sản phẩm.');
                }else{
                    console.error("Delete request failed:", status, error);
                    ToastError('Lỗi !, Vui lòng thử lại sau.');
                }
            }
        });
    }
}
