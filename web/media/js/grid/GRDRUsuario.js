/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var myGrid = $("#gridAux");
myGrid.jqGrid({
    url: 'ReportesServlet',
    autowidth: true,
    height: 400,
    rowNum: 1000,
    viewrecords: true,
    loadonce: true,
    datatype: "json",
    rownumbers: true,
    colNames: ['Identificacion', 'Nombre', 'Estado'],
    colModel: [
        {name: 'identificacion', index: 'identificacion', width: 100},
        {name: 'nombre', index: 'nombre', width: 200},
        {name: 'estado', index: 'estado', width: 100}
    ],
    pager: "#pagerAux",
    postData: {
        reporte: 'usuario'
    },
    caption: "Reporte de Estado de Auxiliares"
});

myGrid.jqGrid('bindKeys');
myGrid.jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn'});
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
        $('<form action="ReportesServlet?oper=u" method="post"></form>').appendTo('body').submit();
    }
});

