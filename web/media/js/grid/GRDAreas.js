/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridArea").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridArea").trigger("reloadGrid");
        $('#grillaGrp').hide();
        return [true, ""]
    }

    var seleccionarArea = function() {

        var myGrid = $('#gridArea'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId)

        var idarea = rowData.id


        var gridG = $('#gridGrp')

        gridG.jqGrid('setCaption', "Grupos de " + rowData.Descripcion)
        gridG.jqGrid('setGridParam', {
            url: "GrupoServlet?listar=area&area=" + idarea,
            editurl: "GrupoServlet?listar=area&area=" + idarea,
            datatype: "json",
            pager: "#pagerGrp"
        })
        gridG.jqGrid('setGridState', 'visible')


        gridG.trigger("reloadGrid");

        $('#grillaGrp').show();
    }

    jQuery('#gridArea').jqGrid(
            {
                height: 248,
                hiddengrid: true,
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                ignoreCase: true,
                caption: "Areas",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["id", "Descripción", "Estado"],
                colModel: [
                    {
                        "name": "id",
                        index: "id",
                        editable: false,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "Descripcion",
                        index: "Descripcion",
                        editable: true,
                        edittype: "select",
                        editoptions: {
                            dataUrl: "getAreas.htm"
                        },
                        editrules: {
                            required: true
                        }

                    },
                    {
                        "name": "Estado",
                        index: "Estado",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
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
                        editrules: {
                            required: true
                        },
                        align: 'center',
                        width: 100

                    }

                ],
                postData: {
                    oper: "oper"
                },
                onSelectRow: seleccionarArea,
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridArea').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }, 1);
                    }
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
                pager: "#pagerArea"
            });

    jQuery('#gridArea').jqGrid('navGrid', '#pagerArea',
            {
                edit: true,
                edittitle: "Modificar area seleccionada",
                add: true,
                addtitle: "Agregar area",
                search: true,
                searchtitle: "Buscar area",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        editCaption: "Modificar area",
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true
    },
    {
        addCaption: "Agregar area",
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        searchCaption: "Buscar area",
        multipleSearch: false
    },
    {
        reloadAfterSubmit: true
    }
    );


});


