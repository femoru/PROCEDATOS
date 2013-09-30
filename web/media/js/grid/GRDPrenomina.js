/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var datosGrilla;

jQuery(document).ready(function($) {



//Cargar nombre y modelo de la tabla
    $.ajax({
        url: "PrenominaServlet",
        type: "POST",
        async: false,
        data: {
            oper: "n"
        },
        success: function(data) {
            datosGrilla = data;
        }, error: function(jqXHR, textStatus, errorThrown) {
            alert(jqXHR + "-" + textStatus + "-" + errorThrown);
        }
    });
    jQuery('#gridNom').jqGrid({
        height: 340,
        width: $('#grilla').width(),
        shrinkToFit: false,
        ignoreCase: true,
        caption: "Prenomina",
        url: "PrenominaServlet",
        loadonce: true,
        rowNum: 15,
        datatype: "json",
        colNames: eval(datosGrilla.nombres),
        colModel: eval(datosGrilla.modelo),
        loadComplete: function() {

            if (this.p.datatype === 'json') {
                setTimeout(function() {

                    $('#gridNom').trigger("reloadGrid", [{
                            page: 1
                        }]);
                }, 1);
            }

        },
        postData: {
            oper: "oper"
        },
        loadError: function(xhr, status, err) {
            try {
                jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                        '<div class="ui-state-error">' + err + xhr + status + '</div>',
                        jQuery.jgrid.edit.bClose,
                        {
                            buttonalign: 'right'
                        });
            } catch (e) {
                alert(xhr.responseText);
            }
        },
        pager: "#pagerNom"

    });
    var refrescarGrilla = function() {
        jQuery("#gridNom").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridNom").trigger("reloadGrid");
        return [true, ""];
    };
    jQuery("#gridNom").jqGrid('bindKeys');
    jQuery('#gridNom').jqGrid('navGrid', '#pagerNom', {
        edit: false,
        add: false,
        search: true,
        del: false,
        refresh: true,
        beforeRefresh: refrescarGrilla
    }
    , {/*Edit*/}, {/*Add*/}, {/*Del*/}, {/*Sch*/}, {
        reloadAfterSubmit: true
    }
    );
    $('#gridNom').jqGrid('navButtonAdd', '#pagerNom', {
        caption: "Exportar",
        title: "Exportar a csv",
        buttonicon: "ui-icon-transfer-e-w",
        position: "first",
        id: "btn_export",
        onClickButton: function() {

        $('<form action="PrenominaServlet?oper=x" method="post"></form>').appendTo('body').submit();
        
        }
    });
    jQuery('#gridNom').jqGrid('navButtonAdd', "#pagerNom", {
        caption: "Cancelar",
        title: "Cancelar Revision",
        buttonicon: "ui-icon-cancel",
        position: "first",
        id: "btn_cancelar",
        onClickButton: function() {
//            window.location = 'Prenomina.htm';
            loadNewPage('Prenomina.htm');
        }
    });
    jQuery('#gridNom').jqGrid('navButtonAdd', "#pagerNom", {
        caption: "Terminar",
        title: "Terminar Revision",
        buttonicon: "ui-icon-close",
        position: "first",
        id: "btn_terminar"
    });
    jQuery('#gridNom').jqGrid('navButtonAdd', "#pagerNom", {
        caption: "Modificar",
        title: "Modificar Registro",
        buttonicon: "ui-icon-pencil",
        position: "first",
        id: "btn_modificar",
        onClickButton: function() {

            $("#validar_Novedad").load("ValidarNovedad.htm #dlgvalidar", function() {
                $.getScript("media/js/dlg/DLGValidarNovedad.js", function() {
                });
            });
            $("#registrarNovedad").load("RegistrarNovedad.htm #bodyLeft", function() {
                $.getScript("media/js/validation/validateNovedad.js", function() {
                });
            });
            var selectedRow = $("#gridNom").getGridParam("selrow");
            var rowData = $('#gridNom').jqGrid('getRowData', selectedRow);
            if (selectedRow === null) {
                jQuery.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, jQuery.jgrid.nav.alerttext);
            } else {
                $("#dlg_detalle").data({selectRow: selectedRow}).dialog("option", "title", "Detalle Prenomina " + rowData.col2).dialog("open");
            }
        }
    });
});

