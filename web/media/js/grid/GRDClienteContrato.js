/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridCltCtr").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridCltCtr").trigger("reloadGrid");

        return [true, ""]
    }


    $('#next1').click(function() {


        var myGrid = $('#gridCltCtr'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);

        if (selRowId != null) {

            var idcliente = rowData.Id

            var gridA = $('#gridCtr')
            gridA.jqGrid('setCaption', "Contratos de " + rowData.Nombre)
            gridA.jqGrid('setGridParam', {
                url: "ContratosServlet?cliente=" + idcliente,
                editurl: "ContratosServlet?cliente=" + idcliente,
                datatype: "json",
                pager: "#pagerCtr"
            })
            gridA.jqGrid('setGridState', 'visible')
            gridA.trigger("reloadGrid");

            $('#grillaClt').attr('hidden', true)
            $('#grillaCtr').attr('hidden', false)
        } else {
            alert("Seleccione un cliente y luego presione siguiente")
        }
    })

    jQuery('#gridCltCtr').jqGrid(
            {
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                caption: "Clientes",
                url: "ClientesServlet",
                editurl: "ClientesServlet",
                cellurl: "ClientesServlet",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["Id", "NIT", "Nombre", "Direccion", "Telefono", "Contacto", "Telefono Contacto", "Email"],
                colModel: [
                    {
                        "name": "Id",
                        "index": "Id",
                        editable: true,
                        edittype: "text",
                        editoptions: {
                            maxlength: 10
                        },
                        hidden: true
                    },
                    {
                        "name": "Nit",
                        "index": "Nit",
                        editable: true,
                        edittype: "text",
                        editoptions: {
                            maxlength: 15
                        }
                    },
                    {
                        "name": "Nombre",
                        "index": "Nombre",
                        editable: true,
                        edittype: "text",
                        editoptions: {
                            maxlength: 30
                        }
                    },
                    {
                        "name": "Direccion",
                        "index": "Direccion",
                        editable: true,
                        editoptions: {
                            maxlength: 50
                        },
                        edittype: "text"

                    },
                    {
                        "name": "Telefono",
                        "index": "Telefono",
                        editable: true,
                        editrules: {
                            required: true,
                            number: true
                        },
                        editoptions: {
                            maxlength: 10
                        },
                        edittype: "text"
                    },
                    {
                        "name": "Contacto",
                        "index": "Contacto",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text"
                    },
                    {
                        "name": "TelefonoContacto",
                        "index": "TelefonoContacto",
                        editable: true,
                        editrules: {
                            required: true,
                            number: true
                        },
                        editoptions: {
                            maxlength: 10
                        },
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "Email",
                        "index": "Email",
                        editable: true,
                        editrules: {
                            required: true,
                            email: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text",
                        hidden: true
                    }


                ],
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
                pager: "#pagerCltCtr"
            });

    jQuery('#gridCltCtr').jqGrid('navGrid', '#pagerCltCtr',
            {
                edit: false,
                add: false,
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

