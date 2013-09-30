/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    $("#grilla2").hide();
    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridRef").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridRef").trigger("reloadGrid");
        $("#grilla2").hide();
        return [true, ""];
    };

    function generarGrillaListado(nomtabla, Descripcion) {

        $('#grilla2').show();

        var myGrid = jQuery("#gridLRef");
        myGrid.jqGrid('setCaption', Descripcion);
        myGrid.jqGrid('setGridState', 'visible');
        myGrid.jqGrid('setGridParam', {
            url: "ReferenciasServlet?nomtabla=" + nomtabla,
            editurl: "ReferenciasServlet?nomtabla=" + nomtabla,
            datatype: "json",
            pager: "#pagerLRef"
        });
        myGrid.trigger("reloadGrid");

        return [true, ""];
    }
    var seleccionarReferencia = function() {
        var myGrid = $('#gridRef'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);

        var desRef = rowData.Descripcion;
        var tablaRef = rowData.Tabla;
        $('#labelReferencia').text(desRef);
        generarGrillaListado(tablaRef, desRef);
    };

    jQuery('#gridRef').jqGrid(
            {
                width: 300,
                height: 252,
                hidegrid: false,
                hoverrows: false,
                gridview: true,
                ignoreCase: true,
                caption: "Referencias",
                url: "ReferenciasServlet",
                loadonce: true,
                rowNum: 10,
                datatype: "json",
                colNames: ["Id", "Descripción", "Tabla"],
                colModel: [
                    {
                        "name": "Id",
                        "index": "Id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "Descripcion",
                        "index": "Descripcion",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "text"

                    },
                    {
                        "name": "Tabla",
                        "index": "Tabla",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "text",
                        hidden: true
                    }
                ],
                onSelectRow: seleccionarReferencia,
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
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
                pager: "#pagerRef"

            });

    jQuery("#gridRef").jqGrid('bindKeys');
    jQuery('#gridRef').jqGrid('navGrid', '#pagerRef',
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

