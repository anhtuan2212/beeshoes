$(document).on('ready', function () {
    $('.js-fancybox-item').each(function () {
        var fancybox = $.HSCore.components.HSFancyBox.init($(this));
        // console.log(fancybox);
    })
});
$(document).ready(function () {
    $('.money-input-mask').mask('#.###đ', {reverse: true});
});
$(document).ready(function () {
    $('.money-input-mask-num').mask('#.###', {reverse: true});
});

function updateCounterInfo() {
    var checkedCount = $('.custom-control-input:checked').length;
    $('#datatableCounterInfo').text(checkedCount + ' Selected');
}

updateCounterInfo()
$(document).on('ready', function () {
    $(document).on('click', '.remove-item', function () {
        $(this).closest('tr').remove();
    });
    $('#datatableCheckAll').on('change', function () {
        let isChecked = $(this).prop('checked');
        if (isChecked) {
            $('#datatableCounterInfo').show();
        } else {
            $('#datatableCounterInfo').hide();
        }
        updateCounterInfo()
        $('.custom-control-input').prop('checked', isChecked);
    });

    $('.custom-control-input').on('change', function () {
        if (!$(this).prop('checked')) {
            $('#datatableCheckAll').prop('checked', false);
        } else {
            let allChecked = $('.custom-control-input:checked').length === $('.custom-control-input').length;
            $('#datatableCheckAll').prop('checked', allChecked);
        }
        updateCounterInfo()
    });
    // BUILDER TOGGLE INVOKER
    // =======================================================
    $('.js-navbar-vertical-aside-toggle-invoker').click(function () {
        $('.js-navbar-vertical-aside-toggle-invoker i').tooltip('hide');
    });


    // INITIALIZATION OF MEGA MENU
    // =======================================================
    let megaMenu = new HSMegaMenu($('.js-mega-menu'), {
        desktop: {
            position: 'left'
        }
    }).init();


    // INITIALIZATION OF NAVBAR VERTICAL NAVIGATION
    // =======================================================
    let sidebar = $('.js-navbar-vertical-aside').hsSideNav();


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
        let unfold = new HSUnfold($(this)).init();
    });


    // INITIALIZATION OF FORM SEARCH
    // =======================================================
    $('.js-form-search').each(function () {
        new HSFormSearch($(this)).init()
    });

    // INITIALIZATION OF DATATABLES
    // =======================================================
    // Khởi tạo mảng dataArray
    var dataArray = [];
    $('#datatableGetdata tr').each(function () {
        let dataObject = {};
        $(this).find('[name]').each(function () {
            var name = $(this).attr('name');
            var value = $(this).text();
            dataObject[name] = value;
        });
        dataArray.push(dataObject);
    });

    var datatable = $.HSCore.components.HSDatatables.init($('#datatable'), {
        columnDefs: [
            {targets: 0, data: 'id'},
            {targets: 1, data: 'img'},
            {targets: 2, data: 'kichCo'},
            {targets: 3, data: 'maMauSac'},
            {targets: 4, data: 'giaGoc'},
            {targets: 5, data: 'giaBan'},
            {targets: 6, data: 'soLuong'},
            {targets: 7, data: 'tenMau'},
        ],
        select: {
            style: 'multi', selector: 'td:first-child input[type="checkbox"]',
            classMap: {
                checkAll: '#datatableCheckAll', counter: '#datatableCounter', counterInfo: '#datatableCounterInfo'
            }
        },
        language: {
            zeroRecords: '<div class="text-center p-4">' + '<img class="mb-3" src="/assets/cms/svg/illustrations/sorry.svg" alt="Image Description" style="width: 7rem;">' + '<p class="mb-0">No data to show</p>' + '</div>'
        },
        createdRow: function (row, data, dataIndex) {
            let customHTML = `
                                <th class="table-column-pr-0" data-colum-index="0">
                                    <div class="custom-control custom-checkbox">
                                        <input type="text" class="form-control" name="id"
                                               value="${data.id}" hidden="">
                                        <input type="checkbox" class="custom-control-input"
                                               id="productVariationsCheck${data.id}">
                                        <label class="custom-control-label"
                                               for="productVariationsCheck${data.id}"></label>
                                    </div>
                                </th>
                                <th class="width-100 row-show-img" data-colum-index="1" data-color="${data.maMauSac}" style="width: 100px !important;">
                                 <div class="spinner-border text-primary d-none" role="status">
                                        <span class="sr-only">Loading...</span>
                                    </div>
                                    <label for="fileimgselected${data.id}">
                                        <img class="avatar" data-color-code-img="${data.maMauSac}"
                                        src="${data.img.length === 0 ? '/assets/cms/img/400x400/img2.jpg' : data.img}"
                                             alt="Image Description">
                                    </label>
                                    <i class="tio-delete btn-del-img"></i>
                                    <input class="formAddImg form-control" data-color-code-input="${data.maMauSac}" type="file" name="img" id="fileimgselected${data.id}" hidden="">
                                </th>
                                <th class="table-column-pl-0 width-100" data-colum-index="2">
                                 <input type="text" class="form-control" name="kichCo" value="${data.kichCo}" disabled>
                                </th>
                                <th class="table-column-pl-0 width-100" data-colum-index="3">
                                    <input type="text" class="form-control" name="mauSac" value="${data.maMauSac}" style="background-color:${data.maMauSac}" disabled>
                                </th>
                                <th class="table-column-pl-0" data-colum-index="4">
                                    <input type="text" class="form-control money-input-mask-num" name="giaGoc"
                                           value="${data.giaGoc}">
                                </th>
                                <th class="table-column-pl-0" data-colum-index="5">
                                    <input type="text" class="form-control money-input-mask-num" name="giaBan" value="${data.giaBan}">
                                </th>
                                <th class="table-column-pl-0" data-colum-index="6">
                                    <!-- Quantity Counter -->
                                    <div class="js-quantity-counter input-group-quantity-counter">
                                        <input type="number" name="soLuong"
                                               class="js-result form-control input-group-quantity-counter-control"
                                               value="${data.soLuong}">
                                        <div class="input-group-quantity-counter-toggle">
                                            <a class="js-minus input-group-quantity-counter-btn"
                                               href="javascript:;">
                                                <i class="tio-remove"></i>
                                            </a>
                                            <a class="js-plus input-group-quantity-counter-btn" href="javascript:;">
                                                <i class="tio-add"></i>
                                            </a>
                                        </div>
                                    </div>
                                </th>
                                <th class="table-column-pl-0" data-colum-index="7">
                                    <div class="btn-group" role="group" aria-label="Edit group">
                                        <a class="btn btn-white remove-item" href="javascript:;">
                                            <i class="tio-delete-outlined"></i>
                                        </a>
                                    </div>
                                </th>`;
            $(row).html(customHTML);
        }
    });
    datatable.rows.add(dataArray).draw();
    console.log(datatable.rows())
    $('#datatable').on('change', 'input', function () {
        let rowIndex = datatable.row($(this).closest('tr')).index();
        let columnIndex = $(this).closest('th').data('colum-index');
        var newValue = $(this).val();

        // Cập nhật giá trị trong dữ liệu DataTables
        datatable.cell(rowIndex, columnIndex).data(newValue).draw(false);
        console.log(datatable.data())
    });
    setIMG();

    function setIMG() {
        let mauSac =null;
        let arr = [];
        let oj ={};
       let img = $('.row-show-img');
        console.log(img)
        for (let i = 0; i < img.length; i++) {
            let color = $(img[i]).attr('data-color');
            $(img[i]).closest('tr').attr('color-code',color);
            console.log(color)
            if (mauSac == null){
                mauSac = color;
                let oj={color:color,ele:img[i]};
                arr.push(oj)
            }else if ( color == mauSac){
                $(img[i]).css('opacity',0);
                $(img[i]).attr('status-remove',true);
            }else {
                mauSac = color
                let oj={color:color,ele:img[i]};
                arr.push(oj)
            }
        }
        let elements = [];
        for (let i = 0; i < arr.length; i++) {
            $("th.row-show-img[status-remove='true']").each(function(index, element) {
               let mau = $(element).attr('data-color');
               if (mau == arr[i].color){
                   elements.push(element)
               }
            });
            $(arr[i].ele).attr('rowspan',$('tr[color-code='+arr[i].color+']').length);
        }
        $.each(elements, function(index, element) {
            $(element).remove();
        });
    }

    datatable.on('draw.dt', function () {
        setIMG();
    });


    $('#datatableSearch').on('mouseup', function (e) {
        var $input = $(this), oldValue = $input.val();

        if (oldValue == "") return;

        setTimeout(function () {
            var newValue = $input.val();

            if (newValue == "") {
                // Gotcha
                datatable.search('').draw();
            }
        }, 1);
    });


    $('#get-data').on('click', function () {
        console.log(datatable.data())
    })
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
$(window).on('load', function () {
    var mausac = $('#mauSac').val();
    mausac.forEach((mau) => {
        var ele = $('li[title="' + mau + '"]');
        var tenmau = $('option[value="' + mau + '"]').attr('data-name');
        var span = $(ele[0]).find('span:not(.select2-selection__choice__remove)').eq(0);
        $(span[0]).text(tenmau);
        $(ele[0]).css('background-color', mau);
        $(ele[0]).css('color', '#FFFFFF');
    });
    if ($('#status-product').val() == 1) {
        ToastError('Sản Phẩm không tồn tại.')
    }
});
