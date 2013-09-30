/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridLbr").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridLbr").trigger("reloadGrid");

        return [true, ""];
    };
    var lastSel;

    function editarGrilla() {

        jQuery('#gridLbr').setColProp('area', {
            editable: false
        });
        jQuery('#gridLbr').setColProp('grupo', {
            editable: false
        });
        jQuery('#gridLbr').setColProp('labor', {
            editable: false
        });
        jQuery('#gridLbr').setColProp('extra', {
            editable: false
        });
    }

    function edicionInline(e) {
        var editUrl = jQuery('#gridLbr').jqGrid("getGridParam", "editurl");
        var gridUsr = $('#gridLbr'),
                selRow = gridUsr.jqGrid('getGridParam', 'selrow');

        $.ajax({
            type: "POST",
            async: false,
            url: editUrl,
            data: {
                oper: "inline",
                id: selRow,
                cambiar: $(e.target).attr("id").split("_")[1],
                valor: $(e.target).val()

            },
            success: refrescarGrilla
        });

    }
    jQuery('#gridLbr').jqGrid(
            {
                height: 248,
                ignoreCase: true,
                caption: "Labores contratadas",
                loadonce: true,
                rowNum: 1000,
                datatype: "local",
                grouping: true,
                groupingView: {
                    groupField: ['area', 'grupo'],
                    groupColumnShow: [false, false],
                    groupText: ['<b>{0} - {1} Labores(s)</b>', '<b>Grupo {0}</b>'],
                    groupCollapse: true
                },
                colNames: ["Id", "Area", "Grupo", "Labor", "Tipo", "Extra", "Valor", "Costo", "Estado", "Dato", "Conciliacion"],
                colModel: [{name: "id", index: "id", hidden: true},
                    {name: "area", index: "area", width: 80},
                    {name: "grupo", index: "grupo", width: 80},
                    {name: "labor", index: "labor", width: 300},
                    {name: "tipo", index: "tipo", width: 60},
                    {name: "extra", index: "extra", width: 100},
                    {name: "valor", index: "valor", align: 'right', width: 50},
                    {name: "costo", index: "costo", align: 'right', width: 50},
                    {name: "estado", index: "estado", stype: "select",
                        editoptions: {
                            value: {
                                0: "Inactivo",
                                1: "Activo"
                            }
                        },
                        searchoptions: {
                            value: {
                                0: "Inactivo",
                                1: "Activo"
                            }
                        },
                        width: 70,
                        formatter: "select",
                        align: "center"
                    }, {name: "datolabor", index: "datolabor", stype: "select",
                        editoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        },
                        searchoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        },
                        width: 40,
                        formatter: "select",
                        align: "center"
                    }, {
                        name: "conciliacion",
                        index: "conciliacion",
                        stype: "select",
                        editoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        },
                        searchoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        },
                        width: 40,
                        formatter: "select",
                        align: "center"
                    }
                ],
                onSelectRow: function(id) {
                    $("#view_gridLbr").removeClass('ui-state-disabled');
                    $("#btn_extra").addClass('ui-state-disabled');
                    jQuery('#gridLbr').restoreRow(lastSel);
                    var row = $('#gridLbr').jqGrid('getRowData', id);

                    if (row.extra === "" && row.tipo.toLowerCase() !== "registros") {
                        var respuesta = $.ajax({
                            type: "POST",
                            async: false,
                            url: "LaboresServlet",
                            data: {
                                oper: "consulta",
                                idlabor: $("#gridLbr").jqGrid('getGridParam', 'selrow')
                            }
                        }).responseText;
                        $("#horaextras").load("getHorasExtras.htm");
                        var extras = respuesta.split(",");
                        for (var i = 0; i < extras.length; i++) {
                            $("#horaextras option[value=" + extras[i] + "]").remove();
                        }
                        if ($("#horaextras").find("option").length > 1) {
                            $("#btn_extra").removeClass('ui-state-disabled');
                        }
                    } else {
                        $("#btn_extra").addClass('ui-state-disabled');
                    }
                },
                ondblClickRow: function(id) {

                    $("#view_gridLbr").addClass('ui-state-disabled');
                    if (id && id !== lastSel) {
                        jQuery('#gridLbr').restoreRow(lastSel);
                        lastSel = id;
                    }
                    jQuery('#gridLbr').editRow(id, true, editarGrilla());
                },
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        $("#btn_extra").addClass('ui-state-disabled');
                        setTimeout(function() {
                            $('#gridLbr').trigger("reloadGrid", [{
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
                                '<div class="ui-state-error">' + xhr.responseText + '</div>',
                                jQuery.jgrid.edit.bClose,
                                {
                                    buttonalign: 'right'
                                });
                    } catch (e) {
                        alert(xhr.responseText);
                    }
                },
                pager: "#pagerLbr"
            });


    jQuery('#gridLbr').jqGrid('navGrid', '#pagerLbr',
            {
                edit: false,
                edittitle: "Modificar labor seleccionada",
                add: false,
                addtitle: "Agregar labor",
                view: true,
                viewtitle: "Consultar datos de la labor seleccionada",
                search: true,
                searchtitle: "Buscar labor",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla

            }, {/*Edit*/}, {/*Add*/}, {/*del*/}, {
        searchCaption: "Buscar labor",
        multipleSearch: true

    }, {
        caption: "Consultar datos de la labor",
        reloadAfterSubmit: true
    }
    ).jqGrid('navButtonAdd', "#pagerLbr", {
        caption: "",
        title: "Modificar labor seleccionada",
        buttonicon: "ui-icon-pencil",
        position: "first",
        id: "btn_edit",
        onClickButton: function() {

            var editUrl = jQuery('#gridLbr').jqGrid("getGridParam", "editurl");
            var gridUsr = $('#gridLbr'),
                    selRow = gridUsr.jqGrid('getGridParam', 'selrow');
            var datas;
            $.ajax({
                type: "POST",
                async: false,
                url: editUrl,
                data: {
                    oper: "editconsulta",
                    id: selRow
                },
                success: function(data) {
                    datas = data;
                }
            });
            $("#area").load("getAreasClientes.htm?idcliente=" + datas.contrato.idcontrato, function() {
                $("#area").val(datas.grupo.idarea);
            });
            $("#grupo").load("getGruposAreas.htm?idarea=" + datas.grupo.idarea, function() {
                $("#grupo").val(datas.grupo.idgrupo);
            });
            $("#tipo").load("getTipoLabor.htm", function() {
                $("#tipo").val(datas.tipolabor);
            });
            $("#labor").load("getLabores.htm", function() {
                $("#labor").val(datas.labor);
            });
            if (datas.horaextra !== 0) {
                $("#tr_extra").show();
                $("#extra").load("getHorasExtras.htm", function() {
                    $("#extra").val(datas.horaextra);
                });
            }
            else {
                $("#tr_extra").hide();
            }
            $("#conciliacion").val(datas.conciliacion);
            $("#valor").val(datas.valor);
            $("#costo").val(datas.costo);
            $("#estado").val(datas.activo);
            $("#datolabor").val(datas.datolabor);
            $("#dlglabor").data("id", selRow);
            $("#dlglabor").data("oper", "edit");
            $("#dlglabor").dialog("open");
            $("#dlglabor").dialog("option", "title", $("#cliente option[value='" + $("#cliente").val() + "']").text());

        }
    }
    ).jqGrid('navButtonAdd', "#pagerLbr", {
        caption: "",
        title: "Agregar labor extra",
        buttonicon: "ui-icon-circle-plus",
        position: "first",
        id: "btn_extra",
        onClickButton: function() {

            $("#dlgExtras").dialog("open");
        }
    }
    ).jqGrid('navButtonAdd', "#pagerLbr", {
        caption: "",
        title: "Agregar labor",
        buttonicon: "ui-icon-plus",
        position: "first",
        id: "btn_add",
        onClickButton: function() {
            var myGrid = $('#gridCtr'),
                    selRowId = myGrid.jqGrid('getGridParam', 'selrow');
            $("#tr_extra").hide();
            $("#area").load("getAreasClientes.htm?idcliente=" + selRowId);
            $("#grupo").load("getGruposAreas.htm?idarea=" + 0);
            $("#tipo").load("getTipoLabor.htm");
            $("#labor").load("getLabores.htm");
            $("#conciliacion").attr("checked", false);
            $("#valor").val("");
            $("#costo").val("");
            $("#estado").val(1);
            $("#datolabor").val(0);
            $("#dlglabor").data("oper", "add");
            $("#dlglabor").dialog("open");
            $("#dlglabor").dialog("option", "title", $("#cliente option[value='" + $("#cliente").val() + "']").text());

        }
    });

    $("#btn_extra").addClass('ui-state-disabled');
});

