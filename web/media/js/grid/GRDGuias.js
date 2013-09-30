/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridGuias").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridGuias").trigger("reloadGrid");

        return [true, ""];
    };




    var lastSel;
    var conciliar = true;
    jQuery('#gridGuias').jqGrid(
            {
                height: 240,
                hidegrid: false,
                hoverrows: false,
                gridview: true,
                ignoreCase: true,
                caption: "Reporte de Guias y Registros",
                url: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                        + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val(),
                loadonce: true,
                rowNum: 10,
                multiselect: false,
                subGrid: true,
                datatype: "array",
                colNames: ["Id", "Auxiliar", "Registros Ingresados", "Registros Cargados"],
                colModel: [
                    {
                        "name": "Id",
                        "index": "Id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "auxiliar",
                        "index": "auxiliar",
                        editable: true,
                        edittype: "text"

                    },
                    {
                        "name": "ingresado",
                        "index": "ingresado",
                        editable: true,
                        edittype: "text"
                    },
                    {
                        "name": "cargado",
                        "index": "cargado",
                        editable: true,
                        edittype: "text"
                    }
                ],
                subGridRowExpanded: function(subgrid_id, row_id) {
                    // we pass two parameters
                    // subgrid_id is a id of the div tag created whitin a table data
                    // the id of this elemenet is a combination of the "sg_" + id of the row
                    // the row_id is the id of the row
                    // If we wan to pass additinal parameters to the url we can use
                    // a method getRowData(row_id) - which returns associative array in type name-value
                    // here we can easy construct the flowing
                    var subgrid_table_id, pager_id;
                    subgrid_table_id = subgrid_id + "_t";
                    pager_id = "p_" + subgrid_table_id;
                    $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table><div id='" + pager_id + "' class='scroll'></div>");

                    jQuery("#" + subgrid_table_id).jqGrid({
                        url: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                                + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val() + "&id=" + row_id,
                        datatype: "json",
                        loadonce: true,
                        rowNum: 4,
                        gridview: true,
                        pager: pager_id,
                        height: '100%',
                        colNames: ['Guia', 'Registros Ingresados'],
                        colModel: [
                            {
                                name: "guia",
                                index: "guia",
                                width: 120,
                                align: "center"
                            }, {
                                name: "ingresado",
                                index: "ingresado",
                                width: 120,
                                align: "center"
                            }
                        ],
                        ondblClickRow: function(rowid) {
                            $("#dlg_registros").data("datos", {
                                auxiliar: $("#" + rowid).parent().parent().attr('id').split("_")[1],
                                guia: rowid,
                                labor: $("#labor").val()
                            }).dialog("open");
                        }

                    });
                    jQuery("#" + subgrid_table_id).jqGrid('navGrid', "#" + pager_id, {
                        edit: false,
                        add: false,
                        del: false
                    }).jqGrid('navButtonAdd', "#" + pager_id, {
                        caption: "",
                        title: "Modificar Registros",
                        buttonicon: "ui-icon-pencil",
                        position: "first",
                        id: "btn_mod",
                        onClickButton: function() {
                            var myGrid = $("#" + subgrid_table_id),
                                    selRowId = myGrid.jqGrid('getGridParam', 'selrow');

                            $("#dlg_registros").data("datos", {
                                auxiliar: $("#" + selRowId).parent().parent().attr('id').split("_")[1],
                                guia: selRowId,
                                labor: $("#labor").val()
                            }).dialog("open");
                        }
                    });
                },
                subGridRowColapsed: function(subgrid_id, row_id) {
                    // this function is called before removing the data
                    var subgrid_table_id;
                    subgrid_table_id = subgrid_id + "_t";
                    jQuery("#" + subgrid_table_id).remove();
                },
                loadComplete: function(data) {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridLbr').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }, 1);
                        if (data.length > 0) {
                            var datos = data.rows;
                            conciliar = true;
                            for (var i = 0; i < datos.length; i++) {
                                if (datos[i].cell[2] !== datos[i].cell[3]) {
                                    $("#" + datos[i].cell[0]).attr("style", "background: red");//<-- Cambiar color por pastel
                                    conciliar = false;
                                }
                            }
                            if (data.records > 0) {
                                if (conciliar === true) {
                                    $("#concilia").removeAttr("disabled");
                                } else {
                                    $("#concilia").attr("disabled", "disabled");
                                }
                            }
                        }

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
                                '<div class="ui-state-error"> pagerGuias' + xhr.responseText + '-' + err + '-' + status + '</div>',
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
                pager: "#pagerGuias"

            });


    jQuery('#gridGuias').jqGrid('navGrid', '#pagerGuias',
            {
                edit: false,
                add: false,
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            }, {/*Edit*/}, {/*Add*/}, {/*Del*/}, {/*Search*/},
            {
                reloadAfterSubmit: true
            }
    );

});

