/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {

    $('#grid').jqGrid({
        caption: "Nomina",
        ignoreCase: true,
        url: 'NominaServlet',
        height: "240",
        datatype: "json",
        rownumbers: true,
        colNames: ["id", "Rango de Fechas","Produccion","Costo", "Estado"],
        colModel: [
            {index: "id", name: "id", hidden: true},
            {index: "rango", name: "rango", width: 200, align: 'center'},
            {index: "Produccion", name: "Produccion", width: 120, align: 'right',formatter: 'currency'},
            {index: "Costo", name: "Costo", width: 120, align: 'right',formatter: 'currency'},
            {index: "estado", name: "estado", width: 100, align: 'center',
                formatter: 'select',
                editoptions: {value: "0:Creada;1:Validada;2:Revision UENP;3:Revision RRHH;4:Cerrada"}}
        ],
        pager: '#pager'
    });

    $('#grid').jqGrid('navGrid', '#pager', {edit: false, add: false, search: false, del: false, refresh: false});
    $('#grid').jqGrid('navButtonAdd', '#pager', {
        caption: "Exportar",
        title: "Exportar a csv",
        buttonicon: "ui-icon-transfer-e-w",
        position: "first",
        id: "btn_export",
        onClickButton: function() {
            var myGrid = $('#grid'),
                    selRowId = myGrid.jqGrid('getGridParam', 'selrow');

            if (selRowId > 0) {
                var celValue = myGrid.jqGrid('getCell', selRowId, 'rango');
                var params = 'oper=export&idnomina=' + selRowId + '&rango=' + celValue;

                $('<form action="NominaServlet?' + params + '" method="post"></form>').appendTo('body').submit();
            } else {
                $.jgrid.info_dialog($.jgrid.nav.alertcap, $.jgrid.nav.alerttext);
            }

        }
    });
});

