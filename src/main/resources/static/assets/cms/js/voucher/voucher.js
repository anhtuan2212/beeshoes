var datatable = null;
activeSiderbar1EXXP("phieu_giam_giam");

function formatNumberMoney(input) {
    let number = parseInt(input);
    if (number >= 1000 && number < 1000000) {
        let rounded = Math.ceil(number / 1000);
        return rounded >= 1000 ? (Math.floor(rounded / 1000) + "M") : (rounded + "K");
    } else if (number >= 1000000) {
        let rounded = Math.floor(number / 100000) / 10;
        return rounded + "M";
    } else {
        return number;
    }
}

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


    $('.text-discount-value').each(function () {
        let type = $(this).data('type');
        let val = $(this).data('value');
        if (type === '%') {
            $(this).text(val + '%');
        } else {
            $(this).text(formatNumberMoney(val));
        }
    })
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

    function updateShowNum() {
        let pageInfo = datatable.page.info();
        let displayedRows = pageInfo.end - pageInfo.start;
        let show = $('#datatableEntries')
        if (Number(show.val()) < 10) {
            show.find('option:selected').remove();
            show.append(`<option value="${displayedRows}" selected>${displayedRows}</option>`)
        } else {
            if (show.find('option[value="10"]').length === 0) {
                show.append(`<option value="10" selected>10</option>`)
            }
            if (show.val() > displayedRows) {
                if (show.find(`option[value="${displayedRows}"]`).length === 0) {
                    show.append(`<option value="${displayedRows}" selected>${displayedRows}</option>`)
                } else {
                    show.val(displayedRows);
                }
            }
        }
    }

    // datatable.on('draw.dt', function () {
    //     updateShowNum();
    // })
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
    $('#voucher_type').on('change', function () {
        let val = $(this).val();
        console.log(val)
        if (val === '%') {
            $('#discount_percent').closest('div.col-sm-12.col-md-6').removeClass('d-none');
            $('#discount_money').closest('div.col-sm-12.col-md-6').addClass('d-none');
        } else {
            $('#discount_percent').closest('div.col-sm-12.col-md-6').addClass('d-none');
            $('#discount_money').closest('div.col-sm-12.col-md-6').removeClass('d-none');
        }
    })
    $('#btn-save-voucher').on('click', function () {
        let id = $('#id-voucher').val();
        let name = $('#voucher_name').val();
        let type = $('#voucher_type').val();
        let condition = $('#discount_condition').val().replace(/\D/g, '');
        let percent = $('#discount_percent').val().replace(/\D/g, '');
        let money = $('#discount_money').val().replace(/\D/g, '');
        let start_time = $('#discount_start_time').val();
        let end_time = $('#discount_end_time').val();
        let description = $('#description').val();
        let quantity = $('#quantity_discount').val().replace(/\D/g, '');
        let status = $('#discount_status').val();
        let max = $('#max_discount').val().replace(/\D/g, '');

        if (name.replace(/\s/g, '').length === 0) {
            ToastError('Vui lòng nhập tên.');
            return;
        }
        if (type.length === 0) {
            ToastError('Vui lòng chọn loại giảm.');
            return;
        }
        if (condition.length === 0) {
            ToastError('Vui lòng nhập giá trị đơn hàng.');
            return;
        }
        if (type === '%') {
            if (percent.length === 0) {
                ToastError('Vui lòng nhập phần trăm giảm.');
                return;
            }
        } else {
            if (money.length === 0) {
                ToastError('Vui lòng nhập phần số tiền giảm.');
                return;
            }
        }
        if (start_time.length===0){
            ToastError("Vui lòng chọn thời gian bắt đầu");
            return;
        }
        if (end_time.length===0){
            ToastError("Vui lòng chọn thời gian kết thúc");
            return;
        }
        if (quantity.replace(/\s/g, '').length === 0) {
            ToastError('Vui lòng nhập số lượng.');
            return;
        }
        if (max.replace(/\s/g, '').length === 0) {
            ToastError('Vui lòng nhập giá trị tối đa.');
            return;
        }
        if (id.length !== 0) {
            if (status.length === 0) {
                ToastError('Vui lòng chọn trạng thái.')
                return;
            }
        }

        $.ajax({
            url: '/cms/voucher/add-voucher',
            type: 'POST',
            data: {
                id: id,
                name: name,
                type: type,
                percent: percent,
                money: money,
                condition: condition,
                quantity: quantity,
                status: status,
                start_time: start_time,
                end_time: end_time,
                max_discount: max,
                description: description,
            }, success: function (data) {
                ToastSuccess("Lưu Thành Công.")
                console.log(data);
            }, error: function (e) {
                console.log(e)
                switch (e.getResponseHeader("status")) {
                    case 'NameNull':
                        ToastError("Vui lòng nhập tên.");
                        break;
                    case 'existsByName':
                        ToastError("Tên đã tồn tại.");
                        break;
                    case 'typeNull':
                        ToastError("Loại mã không hợp lệ.");
                        break;
                    case 'presentNull':
                        ToastError("Phần trăm giảm không hợp lệ.");
                        break;
                    case 'moneyNull':
                        ToastError("Số tiền giảm không hợp lệ.");
                        break;
                    case 'startNull':
                        ToastError("Thời gian bắt đầu không hợp lệ.");
                        break;
                    case 'endNull':
                        ToastError("Thời gian kết thúc không hợp lệ.");
                        break;
                    case 'quantityNull':
                        ToastError("Số lượng không hợp lệ.");
                        break;
                    case 'maxNull':
                        ToastError("Giá trị tối đa không hợp lệ.");
                        break;
                    case 'statusNull':
                        ToastError("Trạng thái không hợp lệ.");
                        break;
                    case 'conditionNull':
                        ToastError("Giá trị đơn hàng không hợp lệ.");
                        break;
                    default:
                        ToastError(e.getResponseHeader("status"));
                }
            }
        })
    })

    $('#save').on('click', function () {
        let id = $('#inputDataId').val();
        let name = $('#inputData').val();
        let trangThai = $('#selectedStaus').val();
        if (name.length === 0) {
            ToastError("Tên không được trống.");
            return;
        }
        if (trangThai === null) {
            ToastError("Trạng thái không được trống.");
            return;
        }
        $.ajax({
            url: '/api/them-mui-giay',
            type: 'POST',
            data: {
                id: id,
                ten: name,
                trangThai: trangThai
            },
            success: function (data, status, xhr) {
                console.log(data)
                let st = xhr.getResponseHeader('status');
                let created = convertTime(data.ngayTao)
                let updated = convertTime(data.ngaySua)
                let create = data.create == 'N/A' ? 'N/A' : `<a href="javascript:;">${data.create}</a>`;
                let update = data.update == 'N/A' ? 'N/A' : `<a href="javascript:;">${data.update}</a>`;
                let trangthai = data.trangThai == true ? ' <span class="legend-indicator bg-success"></span>Hiển Thị' : ' <span class="legend-indicator bg-danger"></span> Không Hiển Thị';
                if (data != null) {
                    var rowData = [
                        `<div class="custom-control custom-checkbox">
                                <input type="checkbox" class="custom-control-input" id="usersDataCheck${data.id}">
                                <label class="custom-control-label" for="usersDataCheck${data.id}"></label>
                        </div>`,
                        `<h5 class="text-hover-primary mb-0 pr-0" data-id="${data.id}" href="javascript:;">${data.ten}</h5>`,
                        `${created}`,
                        `${updated}`,
                        `${create}`,
                        `${update}`,
                        `${trangthai}`,
                        `<a class="btn btn-sm btn-white" href="javascript:;" data-toggle="modal" data-target="#editUserModal" data-status="${data.trangThai == true ? 1 : 0}" data-name="${data.ten}" data-id="${data.id}" onclick="edit(this)">
                                <i class="tio-edit"></i>
                            </a>
                            <a class="btn btn-sm btn-white" href="javascript:;" data-toggle="modal" data-id="${data.id}" onclick="deleteCategory(this)">
                                <i class="tio-delete"></i>
                            </a>`];

                    if (id !== '') {
                        let rowIndex = datatable.row($('h5[data-id=' + id + ']').closest('tr')).index();
                        datatable.row(rowIndex).remove();
                    }
                    let newdata = Array.from(datatable.data());
                    newdata.unshift(rowData);
                    datatable.clear();
                    for (const row of newdata) {
                        datatable.row.add(row);
                    }
                    datatable.draw();
                    $('#editUserModal').modal('hide');
                    $('#inputDataId').val('');
                    $('#inputData').val('');
                    $('#selectedStaus').val(1);
                }
                switch (st) {
                    case "oke":
                        ToastSuccess("Lưu thành công.")
                        break;
                    default:
                        ToastError("Thất Bại.")
                }

            }, error: function (xhr) {
                let st = xhr.getResponseHeader('status');
                console.log(st)
                switch (st) {
                    case "existsByTen":
                        ToastError("Tên đã tồn tại.")
                        break;
                    case "nameNull":
                        ToastError("Tên không được trống.")
                        break;
                    case "statusNull":
                        ToastError("Trạng thái không được trống.")
                        break;
                    case "oke":
                        ToastSuccess("Thêm thành công.")
                        break;
                    default:
                        ToastError("Thất Bại.")
                }
            }
        })
    })
});

function formatNumber(num) {
    return num < 10 ? '0' + num : num;
}

function convertTime(time) {
    var dateObject = new Date(time);
    var year = dateObject.getFullYear();
    var month = ('0' + (dateObject.getMonth() + 1)).slice(-2);
    var day = ('0' + dateObject.getDate()).slice(-2);
    var formattedDate = day + '-' + month + '-' + year;
    return formattedDate;
}

function edit(element) {
    let name = element.getAttribute('data-name');
    let st = element.getAttribute('data-status');
    let id = element.getAttribute('data-id');
    if (st == null) {
        $('#selectedStaus').val(1);
    }
    $('#inputData').val(name).focus();
    $('#inputDataId').val(id);
    $('#selectedStaus').val(st);
}

function Toast(status, message) {
    const Toast = Swal.mixin({
        toast: true,
        position: "top-end",
        showConfirmButton: false,
        timer: 3000,
        timerProgressBar: true,
        didOpen: (toast) => {
            toast.onmouseenter = Swal.stopTimer;
            toast.onmouseleave = Swal.resumeTimer;
        },
        customClass: {
            popup: 'custom-toast-class', // Thêm lớp CSS tùy chỉnh
        }
    });
    Toast.fire({
        icon: status,
        title: message
    });
}

function ToastSuccess(message) {
    Toast('success', message)
}

function ToastError(message) {
    Toast('error', message)
}

function deleteCategory(element) {
    let id = $(element).attr('data-id');
    Swal.fire({
        title: "Bạn chắc chứ?",
        text: "Sau khi xóa sẽ không thể khôi phục lại!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        cancelButtonText: "Hủy",
        confirmButtonText: "Xác Nhận"
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: "/api/xoa-mui-giay",
                type: "DELETE",
                data: {
                    id: id
                },
                success: function () {
                    ToastSuccess('Xóa Thành Công.');
                    var rowIndex = datatable.row($(element).closest('tr')).index();
                    datatable.row(rowIndex).remove().draw();
                },
                error: function (xhr, status, error) {
                    let st = xhr.getResponseHeader('status');
                    if (st == "constraint") {
                        ToastError('Mũi Giày được gắn với sản phẩm.');
                    } else {
                        console.error("Delete request failed:", status, error);
                        ToastError('Lỗi !, Vui lòng thử lại sau.');
                    }
                }
            });
        }
    });

}
