/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    $('#periodo').monthpicker({
        pattern: 'mmm / yyyy'
    }).bind('monthpicker-click-month', refrescarGrilla);



    jQuery('#gridNov').jqGrid({
        autowidth: true,
        height: 255,
        ignoreCase: true,
        caption: "Novedades",
        url: "NovedadesServlet?mes=" + $.datepicker.formatDate('mm/yy', $('#periodo').monthpicker('getDate')),
        loadonce: true,
        rowNum: 10,
        rownumbers: true,
        datatype: "local",
        colNames: ["idusuario", "codnovedad", "Auxiliar", "Fecha Inicio", "Fecha Fin",
            "Tipo", "Observacion", "Estado", "Dias TNL", "Dias SIO", "Dias EPS", "Anulado por", "nroInc", "codDx", "tipoNovedad", "valorEmp"],
        colModel: [
            {
                "name": "idusuario",
                "index": "idusuario",
                editable: true,
                edittype: "text",
                hidden: true
            }, {
                "name": "codnovedad",
                "index": "codnovedad",
                editable: true,
                edittype: "text",
                hidden: true
            }, {
                "name": "auxiliar",
                "index": "auxiliar",
                editable: true,
                edittype: "text",
                width: 170
            }, {
                "name": "inicio",
                "index": "inicio",
                editable: true,
                edittype: "text",
                align: 'center',
                width: 60
            }, {
                "name": "fin",
                "index": "fin",
                editable: true,
                edittype: "text",
                align: 'center',
                width: 60
            }, {
                "name": "tipo",
                "index": "tipo",
                editable: true,
                edittype: "text",
                width: 200
            }, {
                "name": "observacion",
                "index": "observacion",
                editable: true,
                edittype: "text",
                width: 130
            }, {
                "name": "estado",
                "index": "estado",
                editable: true,
                align: 'center',
                edittype: "select",
                stype: "select",
                formatter: "select",
                editoptions: {value: {
                        0: 'Sin validar',
                        2: 'Validada'
                    }},
                searchoptions: {
                    value: {
                        0: 'Sin validar',
                        2: 'Validada'
                    },
                    defaultValue: 2},
                width: 50
            }, {
                "name": "diasTNL",
                "index": "diasTNL",
                editable: true,
                align: 'center',
                edittype: "text",
                width: 40
            }, {
                "name": "diassio",
                "index": "diassio",
                editable: true,
                align: 'center',
                edittype: "text",
                width: 40
            }, {
                "name": "diaseps",
                "index": "diaseps",
                editable: true,
                align: 'center',
                edittype: "text",
                width: 40
            }, {
                "name": "anulado",
                "index": "anulado",
                editable: true,
                edittype: "text",
                width: 80
            }, {
                "name": "nroInc",
                "index": "nroInc",
                editable: true,
                edittype: "text",
                hidden: true
            }, {
                "name": "codDx",
                "index": "codDx",
                editable: true,
                edittype: "text",
                hidden: true
            }, {
                "name": "tipoNov",
                "index": "tipoNov",
                editable: true,
                edittype: "text",
                hidden: true
            }, {
                "name": "valorNov",
                "index": "valorNov",
                editable: true,
                edittype: "text",
                hidden: true
            }
        ],
        onSelectRow: function(id) {
            var rowData = $('#gridNov').jqGrid('getRowData', id);
            if (rowData.estado === "0") {
                $("#btn_validar").removeClass('ui-state-disabled');
            } else {
                $("#btn_validar").addClass('ui-state-disabled');
            }
        },
        loadComplete: function() {
            if (this.p.datatype === 'json') {
                setTimeout(function() {
                    $('#gridNov').trigger("reloadGrid", [{
                            page: 1
                        }]);
                }, 1);
            }
        },
        postData: {
            oper: "oper"
        },
        loadError: function(xhr, status, err)
        {
            try
            {
                jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                        '<div class="ui-state-error">' + xhr.responseText + err + 'Nov</div>',
                        jQuery.jgrid.edit.bClose,
                        {
                            buttonalign: 'right'
                        });
            }
            catch (e)
            {
                alert(xhr.responseText);
            }
        },
        pager: "#pagerNov"

    });
    jQuery('#gridNov').jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn'});
    jQuery('#gridNov').jqGrid('bindKeys');

    jQuery('#gridNov').jqGrid('navGrid', '#pagerNov', {
        edit: false, add: false, view: true, search: true, del: true,
        deltitle: "Anular novedad seleccionada",
        refresh: true,
        beforeRefresh: refrescarGrilla
    }, {/*Edit*/}, {/*Add*/}, {
        /*del*/
        beforeInitData: function(formid) {

            var html = "<label>Causa: </label><select id=\"anulacion\"></select>";
            $($(".DelTable").find("td")[3]).html(html);
            $("#anulacion").load("getAnulaciones.htm");
        },
        onclickSubmit: function(options) {
            options.delData.anulado = $("#anulacion").val();
        },
        beforeSubmit: function() {
            if ($("#anulacion").val() === "-1") {
                return [false, "Seleccione una causa de anulacion para continuar"];
            }

            return [true, ""];

        },
        afterSubmit: refrescarGrilla,
        caption: "Anular",
        url: "NovedadesServlet",
        msg: "",
        bSubmit: "Anular",
        closeAfterDel: true
    }, {/*Search*/}, {/*view*/}
    ).jqGrid('navButtonAdd', "#pagerNov", {
        caption: "",
        title: "Validar novedad",
        buttonicon: "ui-icon-check",
        position: "first",
        id: "btn_validar",
        onClickButton: function() {

            $("#dlgvalidar").dialog("open");
        }
    });
    $("#btn_validar").addClass('ui-state-disabled');
});



function refrescarGrilla() {
    jQuery("#gridNov").jqGrid('setGridParam', {
        url: "NovedadesServlet?mes=" + $.datepicker.formatDate('mm/yy', $('#periodo').monthpicker('getDate')),
        datatype: "json"
    });
    jQuery("#gridNov").trigger("reloadGrid");
    $("#btn_validar").addClass('ui-state-disabled');
    return [true, ""];
}
;