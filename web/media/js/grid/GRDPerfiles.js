/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($)
{

    $('#btnGrabar').click(function() {
        var myTree = $("#checkTree"),
                selectedNodes = myTree.dynatree("getSelectedNodes");

        var myGrid = $('#grid'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId)

        var codper = rowData.cod
        var nodes = "";
        for (var i = 0; i < selectedNodes.length; i++) {

            nodes = nodes + selectedNodes[i].data.key + ","

        }
        $.ajax({
            type: "POST",
            url: "PermisosServlet",
            data: "oper=i&perfil=" + codper + "&nodes=" + nodes,
            success: function(result) {
                if (result) {
                    seleccionarPerfil()
                }

            }
        });
    });

    $('#btnLimpiar').click(function() {
        $("#checkTree").dynatree("getTree").reload()

    });


    function seleccionarPerfil() {
        var myGrid = $('#grid'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId)

        var codper = rowData.cod
        $('#labelPerfil').val(rowData.cod)
        $('#btnGrabar').attr("disabled", false);
        $('#btnLimpiar').attr("disabled", false);

        $.ajax({
            type: "POST",
            url: "PerfilServlet",
            async: false,
            data: "oper=c&codperfil=" + codper,
            success: function(result) {
                if (result) {
                    $('#labelPerfil').text(result)
                    $("#checkTree").dynatree("option", "initAjax", {
                        url: "PermisosServlet",
                        data: {
                            perfil: codper
                        }
                    });

                    $("#checkTree").dynatree("getTree").reload();
                }
            }
        });

    }

    var refrescarGrilla = function(response, postdata) {
        jQuery("#grid").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#grid").trigger("reloadGrid");
        $("#checkTree").dynatree("option", "initAjax", {
            async: false,
            url: "PermisosServlet"
        });
        $("#checkTree").dynatree("getTree").reload();
        return [true, ""]
    }
    var lastSel;
    jQuery('#grid').jqGrid(
            {
                height: 240,
                hoverrows: false,
                hidegrid: false,
                //viewrecords:true,
                gridview: true,
                loadonce: true,
                ignoreCase: true,
                caption: "Perfiles",
                url: "PerfilServlet",
                editurl: "PerfilServlet",
                cellurl: "PerfilServlet",
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["Codigo", "Descripción", "Estado"],
                colModel: [
                    {
                        "name": "cod",
                        index: "cod",
                        editable: false,
                        hidden: true,
                        edittype: "text",
                        editoptions: {
                            style: "text-transform: uppercase",
                            maxlength: 4
                        }
                    },
                    {
                        "name": "descripcion",
                        index: "descripcion",
                        editable: false,
                        edittype: "text",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            style: "text-transform: uppercase",
                            maxlength: 30
                        }
                    },
                    {
                        "name": "estado",
                        index: "estado",
                        editable: true,
                        edittype: "select",
                        editrules: {
                            required: true
                        },
                        formatter: "select",
                        stype: "select",
                        searchoptions: {
                            value: {
                                0: "Inactivo",
                                1: "Activo"
                            }
                        },
                        editoptions: {
                            value: {
                                0: "Inactivo",
                                1: "Activo"
                            },
                            dataEvents: [{
                                    type: 'change',
                                    data: {
                                    },
                                    fn: function(e) {

                                        var editUrl = jQuery('#grid').jqGrid("getGridParam", "editurl");
                                        var est = $(e.target).val()
                                        var gridUsr = $('#grid'),
                                                selRow = gridUsr.jqGrid('getGridParam', 'selrow');

                                        $.ajax({
                                            type: "POST",
                                            async: false,
                                            url: editUrl,
                                            data: {
                                                id: selRow,
                                                oper: "edit",
                                                estado: est
                                            },
                                            success: refrescarGrilla
                                        })

                                    }
                                }]
                        }
                    }
                ],
                onSelectRow: function(id) {
                    if (id && id !== lastSel) {
                        seleccionarPerfil()
                        jQuery('#grid').restoreRow(lastSel);
                        lastSel = id;
                    }
                    jQuery('#grid').editRow(id, true);
                },
                //        ondblClickRow: seleccionarPerfil(),


                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#grid').trigger("reloadGrid", [{
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
                                '<div class="ui-state-error">' + xhr.responseText + '</div>',
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
                pager: "#pager"
            });
    jQuery("#grid").jqGrid('bindKeys');
    jQuery('#grid').jqGrid('navGrid', '#pager',
            {
                edit: false,
                add: false,
                del: false,
                search: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        checkOnSubmit: true,
        afterSubmit: refrescarGrilla,
        closeAfterEdit: true
    },
    {
        beforeInitData: function() {
            jQuery('#grid').setColProp('descripcion', {
                editable: true
            })
        },
        onClose: function() {
            jQuery('#grid').setColProp('descripcion', {
                editable: false
            })
        },
        afterSubmit: refrescarGrilla,
        closeAfterAdd: true
    },
    {
        closeAfterDel: true
    },
    {
        multipleSearch: true,
        closeAfterSearch: true
    },
    {
        reloadAfterSubmit: true
    }

    );

});

