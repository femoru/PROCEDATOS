jQuery(document).ready(function($) {


    $('#asignarDialog').dialog({
        title: "Asignar Labor",
        width: 950,
        autoOpen: false,
        modal: true,
        dialogClass: "no-close",
        buttons: [
            {
                text: "Terminar",
                click: function() {
                    $(this).dialog("close");

                    jQuery('#gridLbrUsr').jqGrid('setGridParam', {
                        rowNum: 10
                    });
                    jQuery('#gridLbrUsr').jqGrid("clearGridData", true);
                }
            }
        ],
        close: function() {
            $('#areas').attr("disabled", true);
            $('#areas').val(0);
            $('#grupos').attr("disabled", true);
            $('#grupos').val(0);
            //$('#grillaLaborUsuario').show();
            jQuery("#gridLbr").jqGrid('setGridParam', {
                datatype: "json"
            });
            jQuery("#gridLbr").trigger("reloadGrid");

            jQuery('#gridLbrUsr').jqGrid('setGridParam', {
                rowNum: 10
            });
            jQuery('#gridLbrUsr').jqGrid("clearGridData", true);


        }, open: function() {
            var gridUsr = $('#gridUsr'),
                    selRowIdUsr = gridUsr.jqGrid('getGridParam', 'selrow'),
                    rowDataUsr = gridUsr.jqGrid('getRowData', selRowIdUsr);
            $("#asignarDialog").dialog({
                title: "Asignar labores para " + rowDataUsr.nombre
            });
        }

    });
    $('#areas').change(function() {
        $('#grupos').attr("disabled", false);
        $('#grupos').load("getGruposAreas.htm?idarea=" + $('#areas').val());

        jQuery('#gridLbrUsr').jqGrid('setGridParam', {rowNum: 10});
        jQuery('#gridLbrUsr').jqGrid("clearGridData", true);
        jQuery('#gridLbrUsr').jqGrid('setCaption', "Labores");

    });
    $('#grupos').change(function() {

        var myGrid = $('#gridLstClt'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);

        var gridUsr = $('#gridUsr'),
                selRowIdUsr = gridUsr.jqGrid('getGridParam', 'selrow'),
                rowDataUsr = gridUsr.jqGrid('getRowData', selRowIdUsr);


        var idusuario = rowDataUsr.Id;
        var idcontrato = rowData.id;
        var idgrupo = $('#grupos').val();


        var gridLabor = $('#gridLbrUsr');

        gridLabor.jqGrid('setCaption', "Labores de " + rowDataUsr.nombre + " en " + $("#areas option[value='" + $("#areas").val() + "']").text()
                + " - " + $("#grupos option[value='" + $("#grupos").val() + "']").text() + " para " + rowData.nombre);
        gridLabor.jqGrid('setGridParam', {
            url: "UsuarioLaboresServlet?idgrupo=" + idgrupo + "&idcontrato=" + idcontrato + "&usuario=" + idusuario,
            editurl: "UsuarioLaboresServlet?idgrupo=" + idgrupo + "&idcontrato=" + idcontrato + "&usuario=" + idusuario,
            datatype: "json",
            pager: "#pagerLbrUsr"
        });
        gridLabor.jqGrid('setGridState', 'visible');
        gridLabor.trigger("reloadGrid");


        //$('#grillaLaborUsuario').show();
    });
    $('#btnAsignar').click(function() {
        var gridUsr = $('#gridUsr'),
                selRowIdUsr = gridUsr.jqGrid('getGridParam', 'selrow');
        if (selRowIdUsr !== null) {
            $("#asignarDialog").dialog("open");
        }else{
             jQuery.jgrid.info_dialog("",
                                '<div class="ui-state-error">' + "Seleccione un auxiliar" + '</div>',
                                jQuery.jgrid.edit.bClose, {
                            buttonalign: 'right'
                        });
        }
    });

});