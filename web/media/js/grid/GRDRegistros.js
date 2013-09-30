/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    var guia = 0;
    var auxiliar = 0;
    var labor = 0;
    $('#dlg_registros').dialog({
        width: 'auto',
        autoOpen: false,
        modal: true,
        dialogClass: "no-close",
        open: function(event, ui) {
            auxiliar = $(this).data("datos").auxiliar;
            guia = $(this).data("datos").guia;
            labor = $(this).data("datos").labor;
            $("#dlg_registros").dialog("option", "title", "Registros de Guia # " + guia);

            jQuery("#gridReg").jqGrid('setGridParam', {
                url: "GuiasServlet?auxiliar=" + auxiliar + "&guia=" + guia + "&inicio=" + $("#inicio").val()
                        + "&fin=" + $("#fin").val() + "&labor=" + labor,
                datatype: "json"
            });
            jQuery("#gridReg").trigger("reloadGrid");


        },
        close: function() {
            refrescarGrilla();
        }
    });

    function refrescarGrilla(response, postdata) {
        jQuery("#gridReg").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridGuias").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridGuias").trigger("reloadGrid");
        jQuery("#gridReg").trigger("reloadGrid");



        return [true, ""]
    }
    var lastSel;
    jQuery('#gridReg').jqGrid(
            {
                //        height:"100%",
                hidegrid: false,
                gridview: true,
                ignoreCase: true,
                caption: "Reporte de Guia",
                url: "GuiasServlet?auxiliar=" + auxiliar + "&guia=" + guia + "&inicio=" + $("#inicio").val()
                        + "&fin=" + $("#fin").val() + "&labor=" + labor,
                editurl: "GuiasServlet",
                loadonce: true,
                rowNum: 5,
                datatype: "json",
                colNames: ["Id", "Fecha", "Registros Ingresados"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "fecha",
                        "index": "fecha",
                        align: "center",
                        edittype: "text"

                    },
                    {
                        "name": "ingresado",
                        "index": "ingresado",
                        editable: true,
                        align: "center",
                        edittype: "text"
                    }
                ],
                onSelectRow: function(id) {
                    if (id && id !== lastSel) {
                        jQuery('#gridReg').restoreRow(lastSel);
                        lastSel = id;
                    }
                    jQuery('#gridReg').editRow(id, true);
                },
                loadComplete: function(data) {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridReg').trigger("reloadGrid", [{
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
                                '<div class="ui-state-error">pagerReg' + xhr.responseText + '</div>',
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
                pager: "#pagerReg"

            });

    jQuery("#gridReg").jqGrid('bindKeys');
    jQuery('#gridReg').jqGrid('navGrid', '#pagerReg',
            {
                edit: false,
                add: false,
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla()
            }, {/*Edit*/}, {/*Add*/}, {/*Del*/}, {/*Search*/},
            {
                reloadAfterSubmit: true
            }
    );


});

