/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var myGrid = $("#gridRep");

$(".datepicker").datepicker();
$(".ui-button").button();
$("#nomina").load('getNominas.htm');
$("#generar").click(function() {
    if (validar()) {
        var lab = myGrid.jqGrid('getGridParam', 'selarrrow');
        var data = "oper=generar" +
                "&reporte=" + $("input[name='tipo']:checked").val() +
                "&clase=" + $("input[name='clase']:checked").val() +
                "&labores=" + lab.toString() +
                "&fini=" + $(".datepicker")[0].value +
                "&ffin=" + $(".datepicker")[1].value +
                "&nomina=" + $("#nomina").val();

        $('<form action="ReportesServlet?' + data + '" method="post"></form>').appendTo('body').submit();
    }
});

$("#previa").click(function() {
    if (validar()) {
        var lab = myGrid.jqGrid('getGridParam', 'selarrrow');
        var data = "oper=generar" +
                "&reporte=" + $("input[name='tipo']:checked").val() +
                "&labores=" + lab.toString() +
                "&nomina=" + $("#nomina").val();
    }
});

function validar() {
    $(".error").hide(400, function() {
        $(this).remove();
    });

    var lab = myGrid.jqGrid('getGridParam', 'selarrrow');
    if ($("#nomina").val() === '0' && $("#nomina").is(':visible')) {
        $("#validar").focus().after("<span class='error'>Seleccione una Nomina</span>");
        return false;
    }
    if (lab <= 0) {
        $("#validar").after("<span class='error'>Seleccione labores para generar reporte</span>");
        return false;
    }

    if ($("#dateIni").val() === "" && $("#dateIni").is(':visible')) {
        $('#dateIni').after("<span class='error'>llenar este campo para continuar</span>");
        return false;
    }
    if ($("#dateFin").val() === "" && $("#dateFin").is(':visible')) {
        $('#dateFin').after("<span class='error'>llenar este campo para continuar</span>");
        return false;
    }

    return true;

}

myGrid.jqGrid({
    url: 'ReportesServlet',
    autowidth: true,
    height: 500,
    rowNum: 100,
    ignoreCase: true,
    loadonce: true,
    viewrecords: true,
    datatype: "json",
    colNames: ['Cliente', 'Area', 'Grupo', 'Labor', 'Tipo', 'Extras'],
    colModel: [
        {name: 'cliente', index: 'cliente', width: 60},
        {name: 'area', index: 'area', width: 90},
        {name: 'grupo', index: 'grupo', width: 100},
        {name: 'labor', index: 'labor', width: 80},
        {name: 'tipo', index: 'tipo', width: 80},
        {name: 'extra', index: 'extra', width: 80}
    ],
    pager: "#pagerRep",
    postData: {
        reporte: 'facturacion'
    },
    grouping: true,
    multiselect: true,
    groupingView: {
        groupField: ['cliente', 'area', 'grupo'],
        groupColumnShow: [false],
        groupText: ['<b>{0} - {1} Labores(s)</b>', '<b>Area {0}</b>', '<b>Grupo {0}</b>'],
        groupCollapse: true
    },
    caption: "Labores Incluidas en el Reporte"
});

myGrid.jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn'});