/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {

    jQuery('#gridLbrUsr').jqGrid('setGridParam', {
        rowNum: 10
    });
    jQuery('#gridLbrUsr').jqGrid("clearGridData", true);

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridLbrUsr").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridLbrUsr").trigger("reloadGrid");

        return [true, ""];
    };
    var lastSel;
    jQuery('#gridLbrUsr').jqGrid(
            {
                height: 240,
                hidegrid: false,
                hoverrows: false,
                gridview: true,
                ignoreCase: true,
                caption: "Labores",
                url: "UsuarioLaboresServlet",
                loadonce: true,
                rowNum: 10,
                shrinkToFit: false,
                rownumbers: false,
                datatype: "clientArray",
                colNames: ["Id", "Labor", "Tipo", "Extra", "Vigencia inicial", "Vigencia final", " "],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "labor",
                        "index": "labor",
                        editable: false,
                        edittype: "text",
                        stype: "select",
                        searchoptions: {
                            dataUrl: "getLabores.htm"
                        },
                        width: 300
                    },
                    {
                        "name": "tipo",
                        "index": "tipo",
                        editable: false,
                        edittype: "text",
                        align: "center",
                        stype: "select",
                        searchoptions: {
                            dataUrl: "getTipoLabor.htm"
                        },
                        width: 65
                    },
                    {
                        "name": "extra",
                        "index": "extra",
                        editable: false,
                        edittype: "text",
                        align: "center",
                        stype: "select",
                        searchoptions: {
                            dataUrl: "getHorasExtras.htm"
                        },
                        width: 100
                    }, {
                        "name": "inicio",
                        "index": "inicio",
                        editable: false,
                        edittype: "text",
                        align: "center",
                        width: 70
                    }, {
                        "name": "fin",
                        "index": "fin",
                        editable: false,
                        edittype: "text",
                        align: "center",
                        width: 70
                    },
                    {
                        "name": "activo",
                        "index": "activo",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "checkbox",
                        formatter: "checkbox",
                        align: 'center',
                        stype: "select",
                        searchoptions: {
                            value: {
                                1: "Asignada",
                                0: "Sin asignar"
                            }
                        },
                        editoptions: {
                            value:
                                    "1:0"
                                    ,
                            dataEvents: [{
                                    type: 'change',
                                    data: {
                                    },
                                    fn: function(e) {
                                        var editUrl = jQuery('#gridLbrUsr').jqGrid("getGridParam", "editurl");
                                        var gridUsr = $('#gridLbrUsr'),
                                                selRow = gridUsr.jqGrid('getGridParam', 'selrow');
                                        if ($(e.target)[0].checked) {
                                            $("#fechas").data({id: selRow, activo: 1, url: editUrl}).dialog("open");
                                        } else {
                                            $.ajax({
                                                type: "POST",
                                                async: false,
                                                url: editUrl,
                                                data: {
                                                    id: selRow,
                                                    activo: 0
                                                },
                                                success: refrescarGrilla
                                            });
                                        }
                                    }
                                }]
                        },
                        width: 30
                    }
                ],
                onSelectRow: function(id) {
                    if (id && id !== lastSel) {
                        jQuery('#gridLbrUsr').restoreRow(lastSel);
                        lastSel = id;
                    }
                    jQuery('#gridLbrUsr').editRow(id);
                },
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridLbrUsr').trigger("reloadGrid", [{
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
                pager: "#pagerLbrUsr"

            });


    jQuery('#gridLbrUsr').jqGrid('navGrid', '#pagerLbrUsr',
            {
                edit: false,
                add: false,
                search: true,
                searchtitle: "Buscar labor",
                del: false,
                view: true,
                viewtitle: "Consultar vigencia",
                refresh: true,
                beforeRefresh: refrescarGrilla
            }, {
        width: 570,
        afterSubmit: refrescarGrilla,
        closeAfterEdit: true
    }, {/*Add*/}, {/*Del*/}, {/*Search*/},
            {
                reloadAfterSubmit: true
            }
    );

    jQuery('#gridLbrUsr').jqGrid('navButtonAdd', "#pagerLbrUsr", {
        caption: "",
        title: "Modificar vigencia",
        buttonicon: "ui-icon-pencil",
        position: "first",
        id: "modVigencia",
        onClickButton: function() {
            var selectedRow = $("#gridLbrUsr").getGridParam("selrow");
            var rowData = $('#gridLbrUsr').jqGrid('getRowData', selectedRow);
            if (selectedRow === null) {
                jQuery.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, jQuery.jgrid.nav.alerttext);
            } else {
                $("#fechas").data({url: $('#gridLbrUsr').jqGrid("getGridParam", "editurl"), id: selectedRow, inicio: rowData.inicio, fin: rowData.fin, activo: 1}).dialog("open");
            }
        }
    });
});



