/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

var btn_search;

function setCustomButton() {
    setDtSearchBtn();
}

function setDtSearchBtn() {
    const filterOptions = {
        0: {text: "查下修", search: "下修"},
        1: {text: "查全部", search: ""}
    };
    var filterState = 0;

    btn_search = {
        text: filterOptions[filterState].text,
        action: function (e, dt, node, config) {

            const flagIndex = dt.columns().indexes().length - 2;
            const searchKey = filterOptions[filterState].search;
            dt.column(flagIndex).search(searchKey).draw();

            const len_options = Object.keys(filterOptions).length;
            filterState = (filterState + 1) % len_options;
            $(node).text(filterOptions[filterState].text);
        }
    };
}

function getDtCopyBtn() {
    return {
        extend: 'copy',
        text: 'copy',
        exportOptions: {
            columns: ':not(.not-export-col):visible, .export-col:hidden'
        }
    };
}

function getDtExcelBtn(tableName) {
    return {
        extend: 'excelHtml5',
        footer: true,
        exportOptions: {
            columns: ':not(.not-export-col):visible, .export-col:hidden',
            format: {
                body: function (data, row, column, node) {
                    if (data === 0 || data === '0')
                        return '0';
                    return data;
                }
            }
        },
        title: tableName
    };
}



function setTb2DataTable($table, ajax, tableName = '工時不足', isModuleVisible = false) {
    $table.DataTable({
        dom: 'Bfrtip',
        buttons: [
            getDtCopyBtn(), getDtExcelBtn(tableName)
        ],
        fixedHeader: {
            headerOffset: 50
        },
        "ajax": ajax,
        "columns": [
            {data: "floorName", title: "廠區", visible: false, class: 'not-export-col'},
            {data: "modelName", title: "機種"},
            {data: "station", title: "站別"},
            {data: "preModuleName", title: "模組", visible: isModuleVisible, class: 'not-export-col'},
            {data: "totalCnt", title: "數量"},
            {data: "opTime", title: "平均工時"},
            {data: "standardTime", title: "標工"},
            {data: "productivity", title: "生產效率"}
        ],
        "columnDefs": [
            {
                "type": "html",
                "targets": [5],
                'render': function (data, type, full, meta) {
                    if (data == null) {
                        return 'n/a';
                    } else {
                        return data == 0 ? 0 : roundDecimal(data, 2);
                    }
                }
            },
            {
                "type": "html",
                "targets": [7],
                'render': function (data, type, full, meta) {
                    return data == null ? 'n/a' : getPercent(data);
                }
            }
        ],
        "oLanguage": {
            "sLengthMenu": "顯示 _MENU_ 筆記錄",
            "sZeroRecords": "無符合資料",
            "sInfo": "目前記錄：_START_ 至 _END_, 總筆數：_TOTAL_"
        },
        displayLength: 10,
        "processing": true,
        "initComplete": function (settings, json) {
            $("#send").attr("disabled", false);
            $("#send").data("isProcessing", false);
            setTableCaption($table, tableName);
        },
        filter: true,
        destroy: true,
//                    paginate: false,
        "order": []
    });
}

function setTableCaption($table, tableName) {
    if ($table.find('caption').length === 0) {
        $table.prepend('<caption class="caption_table">' + tableName + '</caption>');
    }
}