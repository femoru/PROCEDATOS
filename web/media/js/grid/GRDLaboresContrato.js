/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridLbrCtr").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridLbrCtr").trigger("reloadGrid");

        return [true, ""]
    }

    $('#back2').click(function() {
        $('#grillaCtr').attr('hidden', false)
        $('#grillaLbrCtr').attr('hidden', true)
    })

    $('#next3').click(function() {
        var clienteGrid = $('#gridCltCtr'),
                selRowId = clienteGrid.jqGrid('getGridParam', 'selrow'),
                rowData = clienteGrid.jqGrid('getRowData', selRowId);

        var idcliente = rowData.Id

        var gridG = $('#gridGrp')
        gridG.jqGrid('setCaption', "Grupos de " + rowData.Nombre)
        gridG.jqGrid('setGridParam', {
            url: "GrupoServlet?cliente=" + idcliente,
            datatype: "json",
            pager: "#pagerGrp"
        })
        gridG.jqGrid('setGridState', 'visible')
        gridG.trigger("reloadGrid");

        $('#grillaCtr').attr('hidden', true)
        $('#grillaLbrCtr').attr('hidden', true)
        $('#last').attr('hidden', false)
        $('#grillaGrp').attr('hidden', false)

    })

    jQuery('#gridLbrCtr').jqGrid(
            {
                hiddengrid: true,
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                caption: "Labores",
                url: "LaboresServlet",
                editurl: "LaboresServlet",
                loadonce: true,
                cellEdit: true,
                cellsubmit: 'clientArray',
                rowNum: 7,
                rownumbers: true,
                colNames: ["id", "Descripción ", "Valor", "Costo", "Contrato", "Valida"],
                colModel: [
                    {"name": "id", index: "id", editable: false, edittype: "text", hidden: true},
                    {"name": "descripcion", index: "descripcion", editable: false, edittype: "select", editoptions: {dataUrl: "getLabores.htm"}, width: 470},
                    {"name": "valor", index: "valor", editable: true, edittype: "text", editrules: {required: true}, editoptions: {maxlength: 5}, hidden: false, width: 50},
                    {"name": "costo", index: "costo", editable: true, edittype: "text", editrules: {required: true}, editoptions: {maxlength: 5}, hidden: false, width: 50},
                    {"name": "contrato", index: "contrato", editable: true, edittype: "text", hidden: true},
                    {"name": "valida", index: "valida", editable: true, edittype: "select", formatter: "select", editoptions: {value: {0: "NoValida", 1: "Valida"}}, editrules: {required: true}, hidden: false}
                ]
                        , postData: {
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
                pager: "#pagerLbrCtr"/*,
                 onSelectRow : function(){   //enviar con el evento click
                 contrato =$('#id').val();
                 //alert(contrato);
                 }*/
            });

    jQuery('#gridLbrCtr').jqGrid('navGrid', '#pagerLbrCtr',
            {
                edit: true,
                add: true,
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true
    },
    {
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        multipleSearch: false
    },
    {
        reloadAfterSubmit: true
    }
    );


});


