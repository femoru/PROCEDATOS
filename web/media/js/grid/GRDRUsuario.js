/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var myGrid = $("#gridAux");
$('#month').monthpicker({
    selectedMonth: $.datepicker.formatDate("mm", new Date()),
    pattern: 'mm/yyyy'
}).val($.datepicker.formatDate("mm/yy", new Date())).bind('monthpicker-click-month', function() {
    myGrid.jqGrid('setGridParam', {
        datatype: "json",
        postData: {
            mes: $('#month').val()
        }
    });
    myGrid.trigger("reloadGrid");
});
myGrid.jqGrid({
    url: 'ReportesServlet',
    autowidth: true,
    ignoreCase: true,
    height: 400,
    rowNum: 1000,
    viewrecords: true,
    loadonce: true,
    datatype: "json",
    rownumbers: true,
    colNames: ['Identificacion', 'Nombre', 'Estado', 'Dias con registros', 'Fecha de Ingreso', 'Fecha de Retiro'],
    colModel: [
        {name: 'identificacion', index: 'identificacion', width: 100},
        {name: 'nombre', index: 'nombre', width: 200},
        {name: 'estado', index: 'estado', width: 100, align: "center", edittype: "select", formatter: "select", stype: "select",
            searchoptions: {
                defaultValue: 1,
                value: {
                    1: "ACTIVO",
                    0: "INACTIVO"

                }
            },
            editoptions: {
                value: {
                    1: "ACTIVO",
                    0: "INACTIVO"

                }
            }
        },
        {name: 'diasRegistros', index: 'diasRegistros', width: 100, sorttype: 'number', searchoptions: {sopt: ['eq', 'lt', 'le', 'cn', 'gt', 'ge', 'nc']}},
        {name: 'fechaIngreso', index: 'fechaRetiro', formatter: 'date', formatoptions: {srcformat: 'd/m/y', newformat: 'd/m/Y'}, sorttype: 'date', width: 100},
        {name: 'fechaRetiro', index: 'fechaRetiro', formatter: 'date', formatoptions: {srcformat: 'd/m/y', newformat: 'd/m/Y'}, sorttype: 'date', width: 100}
    ],
    pager: "#pagerAux",
    multiSort: true,
    postData: {
        reporte: 'usuario',
        mes: $('#month').val()
    },
    caption: "Reporte de Estado de Auxiliares"
});
myGrid.jqGrid('bindKeys');
myGrid.jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn', searchOperators: true});
myGrid.jqGrid('navGrid', '#pagerAux', {edit: false, add: false, search: false, del: false, refresh: false},
{/*Edit*/}, {/*Add*/}, {/*Del*/}, {/*Sch*/}, {/*refr*/}
);
myGrid.jqGrid('navButtonAdd', '#pagerAux', {
    caption: "Exportar",
    title: "Exportar a Excel",
    buttonicon: "ui-icon-transfer-e-w",
    position: "first",
    id: "btn_export",
    onClickButton: function() {
        $('<form action="ReportesServlet?oper=u&mes=' + $('#month').val() + '" method="post"></form>').appendTo('body').submit();
    }
});

