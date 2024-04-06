let data_oder_yesterday_in_store = [];
let data_oder_today_in_store = [];
let data_oder_today = [];
let data_oder_yesterday = [];
let data_oder_yesterday_online = [];
let data_oder_today_online = [];
let total_money_online = 0;
let total_money_in_store = 0;
let quantity_online = 0;
let quantity_in_store = 0;

function getData() {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/api/get-detail-arr-count',
            type: 'GET',
            success: function (response) {
                resolve(response)
            }, error: function (error) {
                reject(error)
            }
        })
    })
}

function convertDataToArray(data, name) {
    let dataArray = [];
    for (let key in data[name]) {
        dataArray.push(data[name][key]);
    }
    return dataArray;
}

function addCommasToNumber(number) {
    let numberStr = number.toString().replace(/[^\d]/g, '');
    let parts = [];
    for (let i = numberStr.length, j = 0; i >= 0; i--, j++) {
        parts.unshift(numberStr[i]);
        if (j > 0 && j % 3 === 0 && i > 0) {
            parts.unshift('.');
        }
    }
    return parts.join('');
}

function sumArrays(arr1, arr2) {
    if (arr1.length !== arr2.length) {
        console.log("Độ dài của hai mảng không bằng nhau");
        return null;
    }
    let result = [];
    for (let i = 0; i < arr1.length; i++) {
        result.push(arr1[i] + arr2[i]);
    }
    return result;
}

let optionsDomainChart = {
    scales: {
        yAxes: [{
            gridLines: {
                color: "#e7eaf3",
                drawBorder: false,
                zeroLineColor: "#e7eaf3"
            },
            ticks: {
                beginAtZero: true,
                stepSize: 10,
                fontSize: 12,
                fontColor: "#97a4af",
                fontFamily: "Open Sans, sans-serif",
                padding: 10,
                postfix: ""
            }
        }],
        xAxes: [{
            gridLines: {
                display: false,
                drawBorder: false
            },
            ticks: {
                fontSize: 12,
                fontColor: "#97a4af",
                fontFamily: "Open Sans, sans-serif",
                padding: 5
            }
        }]
    },
    tooltips: {
        postfix: "",
        hasIndicator: true,
        mode: "index",
        intersect: false,
        lineMode: true,
        lineWithLineColor: "rgba(19, 33, 68, 0.075)"
    },
    hover: {
        mode: "nearest",
        intersect: true
    }
};

$(document).on('ready', function () {
    let chartToday = $.HSCore.components.HSChartJS.init($('#total-data-today'));
    let oderAll = $.HSCore.components.HSChartJS.init($('#total-oder-all'));
    getData().then((res) => {
        total_money_online = res.data.total_online_revenue;
        total_money_in_store = res.data.total_store_revenue;
        quantity_online = res.data.quantity_online
        quantity_in_store = res.data.quantity_store
        data_oder_today_in_store = convertDataToArray(res, 'storeToday')
        data_oder_yesterday_in_store = convertDataToArray(res, 'storeYesterday')
        data_oder_today_online = convertDataToArray(res, 'onlineToday')
        data_oder_yesterday_online = convertDataToArray(res, 'onlineYesterday')
        data_oder_today = sumArrays(data_oder_today_in_store, data_oder_today_online)
        data_oder_yesterday = sumArrays(data_oder_yesterday_in_store, data_oder_yesterday_online)

        // oderAll.options = optionsDomainChart;
        oderAll.data.datasets = [{
            data: data_oder_today,
            backgroundColor: "transparent",
            borderColor: "#377dff",
            borderWidth: 2,
            pointRadius: 0,
            hoverBorderColor: "#377dff",
            pointBackgroundColor: "#377dff",
            pointBorderColor: "#fff",
            pointHoverRadius: 0
        },
            {
                data: data_oder_yesterday,
                backgroundColor: "transparent",
                borderColor: "#e7eaf3",
                borderWidth: 2,
                pointRadius: 0,
                hoverBorderColor: "#e7eaf3",
                pointBackgroundColor: "#e7eaf3",
                pointBorderColor: "#fff",
                pointHoverRadius: 0
            }]
        oderAll.update();
        chartToday.data.datasets = [
            {
                data: data_oder_today_in_store,
                backgroundColor: "#377dff",
                hoverBackgroundColor: "#377dff",
                borderColor: "#377dff"
            },
            {
                data: data_oder_yesterday_in_store,
                backgroundColor: "#e7eaf3",
                borderColor: "#e7eaf3"
            }
        ];
        chartToday.update();
        $("#show-total-money").text(addCommasToNumber(total_money_in_store) + 'đ');
        $("#text-quantity-oder-today").text(quantity_in_store);
        $("#total-quantity-today").text(quantity_in_store + quantity_online);
        $("#total-money-today").text(addCommasToNumber(total_money_online + total_money_in_store) + 'đ');
    })
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


    // INITIALIZATION OF SELECT2
    // =======================================================
    $('.js-select2-custom').each(function () {
        var select2 = $.HSCore.components.HSSelect2.init($(this));
    });


    // INITIALIZATION OF CHARTJS
    // =======================================================
    Chart.plugins.unregister(ChartDataLabels);

    $('.js-chart').each(function () {
        $.HSCore.components.HSChartJS.init($(this));
    });


    $(document).on('change', '#total-oder-today-type', function () {
        let val = $(this).val();
        if (val === 'in-website') {
            chartToday.data.datasets = [
                {
                    "data": data_oder_today_online,
                    "backgroundColor": "#377dff",
                    "hoverBackgroundColor": "#377dff",
                    "borderColor": "#377dff"
                },
                {
                    "data": data_oder_yesterday_online,
                    "backgroundColor": "#e7eaf3",
                    "borderColor": "#e7eaf3"
                }
            ];
            $("#show-total-money").text(addCommasToNumber(total_money_online) + 'đ')
            $("#text-quantity-oder-today").text(quantity_online)
        } else {
            chartToday.data.datasets = [
                {
                    "data": data_oder_today_in_store,
                    "backgroundColor": "#377dff",
                    "hoverBackgroundColor": "#377dff",
                    "borderColor": "#377dff"
                },
                {
                    "data": data_oder_yesterday_in_store,
                    "backgroundColor": "#e7eaf3",
                    "borderColor": "#e7eaf3"
                }
            ];
            $("#show-total-money").text(addCommasToNumber(total_money_in_store) + 'đ')
            $("#text-quantity-oder-today").text(quantity_in_store)
        }
        chartToday.update();
    })
    var updatingChart = $.HSCore.components.HSChartJS.init($('#updatingData'));

    // CALL WHEN TAB IS CLICKED
    // =======================================================
    $('[data-toggle="chart-bar"]').click(function (e) {
        let keyDataset = $(e.currentTarget).attr('data-datasets')

        if (keyDataset === 'lastWeek') {
            updatingChart.data.labels = ["Apr 22", "Apr 23", "Apr 24", "Apr 25", "Apr 26", "Apr 27", "Apr 28", "Apr 29", "Apr 30", "Apr 31"];
            updatingChart.data.datasets = [
                {
                    "data": [120, 250, 300, 200, 300, 290, 350, 100, 125, 320],
                    "backgroundColor": "#377dff",
                    "hoverBackgroundColor": "#377dff",
                    "borderColor": "#377dff"
                },
                {
                    "data": [250, 130, 322, 144, 129, 300, 260, 120, 260, 245, 110],
                    "backgroundColor": "#e7eaf3",
                    "borderColor": "#e7eaf3"
                }
            ];
            updatingChart.update();
        } else {
            updatingChart.data.labels = ["May 1", "May 2", "May 3", "May 4", "May 5", "May 6", "May 7", "May 8", "May 9", "May 10"];
            updatingChart.data.datasets = [
                {
                    "data": [200, 300, 290, 350, 150, 350, 300, 100, 125, 220],
                    "backgroundColor": "#377dff",
                    "hoverBackgroundColor": "#377dff",
                    "borderColor": "#377dff"
                },
                {
                    "data": [150, 230, 382, 204, 169, 290, 300, 100, 300, 225, 120],
                    "backgroundColor": "#e7eaf3",
                    "borderColor": "#e7eaf3"
                }
            ]
            updatingChart.update();
        }
    })


    // INITIALIZATION OF BUBBLE CHARTJS WITH DATALABELS PLUGIN
    // =======================================================
    $('.js-chart-datalabels').each(function () {
        $.HSCore.components.HSChartJS.init($(this), {
            plugins: [ChartDataLabels],
            options: {
                plugins: {
                    datalabels: {
                        anchor: function (context) {
                            var value = context.dataset.data[context.dataIndex];
                            return value.r < 20 ? 'end' : 'center';
                        },
                        align: function (context) {
                            var value = context.dataset.data[context.dataIndex];
                            return value.r < 20 ? 'end' : 'center';
                        },
                        color: function (context) {
                            var value = context.dataset.data[context.dataIndex];
                            return value.r < 20 ? context.dataset.backgroundColor : context.dataset.color;
                        },
                        font: function (context) {
                            var value = context.dataset.data[context.dataIndex],
                                fontSize = 25;

                            if (value.r > 50) {
                                fontSize = 35;
                            }

                            if (value.r > 70) {
                                fontSize = 55;
                            }

                            return {
                                weight: 'lighter',
                                size: fontSize
                            };
                        },
                        offset: 2,
                        padding: 0
                    }
                }
            },
        });
    });


    // INITIALIZATION OF DATERANGEPICKER
    // =======================================================
    $('.js-daterangepicker').daterangepicker();

    $('.js-daterangepicker-times').daterangepicker({
        timePicker: true,
        startDate: moment().startOf('hour'),
        endDate: moment().startOf('hour').add(32, 'hour'),
        locale: {
            format: 'M/DD hh:mm A'
        }
    });

    var start = moment();
    var end = moment();

    function cb(start, end) {
        $('#js-daterangepicker-predefined .js-daterangepicker-predefined-preview').html(start.format('MMM D') + ' - ' + end.format('MMM D, YYYY'));
    }

    $('#js-daterangepicker-predefined').daterangepicker({
        startDate: start,
        endDate: end,
        ranges: {
            'Hôm Nay': [moment(), moment()],
            'Hôm Qua': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            '7 Ngày Qua': [moment().subtract(6, 'days'), moment()],
            '30 Ngày Qua': [moment().subtract(29, 'days'), moment()],
            'Tháng Này': [moment().startOf('month'), moment().endOf('month')],
            'Tháng Trước': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, cb);

    cb(start, end);


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


    // INITIALIZATION OF CLIPBOARD
    // =======================================================
    $('.js-clipboard').each(function () {
        var clipboard = $.HSCore.components.HSClipboard.init(this);
    });
});