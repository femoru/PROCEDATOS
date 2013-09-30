/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {

    //$("#grillaContrato").hide();
    $("#grillaAreaLabores").hide();

    $("#cliente").load("getClientes.htm")
    $("#cliente").change(function() {
        $('#grillaContrato').show();
        $("#grillaAreaLabores").hide();

        var gridContr = $('#gridCtr')
        gridContr.jqGrid('setGridParam', {
            url: "ContratosServlet?cliente=" + $("#cliente").val(),
            editurl: "ContratosServlet?cliente=" + $("#cliente").val(),
            datatype: "json",
            pager: "#pagerCtr"
        })
        gridContr.jqGrid('setGridState', 'visible')
        gridContr.trigger("reloadGrid");
        if ($("#cliente").val() != "0") {
            $("#add_gridCtr").removeClass('ui-state-disabled');
            $("#edit_gridCtr").removeClass('ui-state-disabled');
            $("#view_gridCtr").removeClass('ui-state-disabled');
        } else {
            $("#add_gridCtr").addClass('ui-state-disabled');
            $("#edit_gridCtr").addClass('ui-state-disabled');
            $("#view_gridCtr").addClass('ui-state-disabled');
        }
    })


    var datePickerGrid = function(element) {

        $(element).datepicker({
            dateFormat: 'dd/mm/yy',
            changeYear: true,
            yearRange: "c-5:c+5",
            changeMonth: true,
            showAnim: "slideDown"
        })
    };

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridCtr").jqGrid('setGridParam', {
            datatype: "json",
            pager: "#pagerCtr"
        })
        jQuery("#gridCtr").trigger("reloadGrid");
        $("#grillaAreaLabores").hide();

        return [true, ""]
    }

    var seleccionarReferencia = function() {

        var myGrid = $('#gridCtr'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);

        var contrato = rowData.id

        var gridLabor = $('#gridLbr');
        gridLabor.jqGrid('setGridParam', {
            url: "LaboresServlet?contrato=" + contrato,
            editurl: "LaboresServlet?contrato=" + contrato,
            datatype: "json",
            pager: "#pagerLbr"
        })
        gridLabor.jqGrid('setGridState', 'visible')
        gridLabor.trigger("reloadGrid");

        $("#grillaAreaLabores").show();

    }


    jQuery('#gridCtr').jqGrid(
            {
                hoverrows: false,
                height: 248,
                hidegrid: false,
                gridview: true,
                ignoreCase: true,
                caption: "Contratos",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "local",
                colNames: ["Id", "Numero ", "Fecha Inicio", "Fecha Termina", "Estado"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "numero",
                        "index": "numero",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 20
                        },
                        edittype: "text",
                        width: 180
                    },
                    {
                        "name": "inicio",
                        "index": "inicio",
                        editable: true,
                        edittype: "text",
                        editrules: {
                            required: true,
                            edithidden: true
                        },
                        editoptions: {
                            readonly: true,
                            dataInit: datePickerGrid
                        },
                        datefmt: "dd/mm/yyyy",
                        hidden: true
                    },
                    {
                        "name": "fin",
                        "index": "fin",
                        editable: true,
                        edittype: "text",
                        editrules: {
                            required: true,
                            edithidden: true
                        },
                        editoptions: {
                            readonly: true,
                            dataInit: datePickerGrid
                        },
                        datefmt: "dd/mm/yyyy",
                        hidden: true
                    },
                    {
                        "name": "activo",
                        "index": "activo",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "select",
                        formatter: "select",
                        align: 'center',
                        stype: "select",
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
                        width: 80
                    }


                ],
                onSelectRow: seleccionarReferencia,
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridCtr').trigger("reloadGrid", [{
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
                pager: "#pagerCtr"
            });

    jQuery('#gridCtr').jqGrid('navGrid', '#pagerCtr',
            {
                edit: true,
                edittitle: "Modificar contrato seleccionado",
                add: true,
                addtitle: "Agregar contrato",
                view: true,
                viewtitle: "Consultar datos del contrato seleccionado",
                search: true,
                searchtitle: "Buscar contrato",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla


            },
    {
        editCaption: "Modificar contrato",
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true
    },
    {
        addCaption: "Agregar contrato",
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        searchCaption: "Buscar contrato",
        multipleSearch: false

    },
    {
        caption: "Consultar datos del contrato ",
        reloadAfterSubmit: true
    }
    );
    $("#add_gridCtr").addClass('ui-state-disabled');
    $("#edit_gridCtr").addClass('ui-state-disabled');
    $("#view_gridCtr").addClass('ui-state-disabled');

});

