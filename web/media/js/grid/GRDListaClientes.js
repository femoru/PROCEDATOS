

jQuery(document).ready(function($) {

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridLstClt").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridLstClt").trigger("reloadGrid");
        return [true, ""]
    }

    var seleccionarReferencia = function() {
        var myGrid = $('#gridLstClt'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);

        var idcliente = rowData.id
        $('#areas').load("getAreasClientes.htm?idcliente=" + idcliente);
        $('#areas').attr("disabled", false);
        $('#grupos').attr("disabled", true);
        $('#grupos').val("0");

        //$('#grillaLaborUsuario').attr("style", "display:none");
        jQuery('#gridLbrUsr').jqGrid('setGridParam', {rowNum: 10});
        jQuery('#gridLbrUsr').jqGrid("clearGridData", true);
        jQuery('#gridLbrUsr').jqGrid('setCaption', "Labores");
    }

    jQuery('#gridLstClt').jqGrid(
            {
                height: 353,
                hoverrows: false,
                gridview: true,
                //caption: "Clientes",
                url: "ClientesServlet?list=list",
                loadonce: true,
                rowNum: 10,
                //   rownumbers: true,
                datatype: "json",
                colNames: ["Id", "Cliente - Contrato"],
                colModel: [
                    {
                        "name": "id", "index": "id", editable: true, edittype: "text", hidden: true
                    },
                    {
                        "name": "nombre", "index": "nombre", editable: true, edittype: "text", width: 250
                    }
                ],
                onSelectRow: seleccionarReferencia,
                loadComplete: function() {
                    $('gview_gridLstClt.ui-jqgrid-hdiv').hide();
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridLstClt').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }
                        , 1);
                    }
                },
                postData: {
                    oper: "oper"
                },
                loadError: function(xhr, status, err)
                {
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
                }
                //pager:"#pagerLstClt"

            });


    jQuery('#gridLstClt').jqGrid('navGrid', '#pagerLstClt',
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
