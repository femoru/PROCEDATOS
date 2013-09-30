/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {

    $('#grillaArea').hide();
    $('#grillaGrp').hide();

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridClt").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridClt").trigger("reloadGrid");
        $('#grillaArea').hide();
        $('#grillaGrp').hide();
        return [true, ""]
    }

    var seleccionarArea = function() {

        $('#grillaGrp').hide();
        var myGrid = $('#gridClt'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId)

        var idcliente = rowData.Id


        var gridA = $('#gridArea')

        gridA.jqGrid('setCaption', "Areas de " + rowData.Nombre)
        gridA.jqGrid('setGridParam', {
            url: "AreasServlet?cliente=" + idcliente,
            editurl: "AreasServlet?cliente=" + idcliente,
            datatype: "json",
            pager: "#pagerArea"
        })
        gridA.jqGrid('setGridState', 'visible')


        gridA.trigger("reloadGrid");





        $('#grillaArea').show();
    }

    jQuery('#gridClt').jqGrid(
            {
                height: 248,
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                ignoreCase: true,
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
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 15
                        },
                        width: 100
                    },
                    {
                        "name": "Nombre",
                        "index": "Nombre",
                        editable: true,
                        edittype: "text",
                        width: 210,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 50
                        }
                    },
                    {
                        "name": "Direccion",
                        "index": "Direccion",
                        editable: true,
                        editrules: {
                            required: true,
                            edithidden: true
                        },
                        editoptions: {
                            maxlength: 50
                        },
                        edittype: "text",
                        hidden: true

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
                        width: 80,
                        edittype: "text"
                    },
                    {
                        "name": "Contacto",
                        "index": "Contacto",
                        editable: true,
                        editrules: {
                            required: true,
                            edithidden: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "TelefonoContacto",
                        "index": "TelefonoContacto",
                        editable: true,
                        editrules: {
                            required: true,
                            number: true,
                            edithidden: true
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
                        width: 105,
                        editrules: {
                            required: true,
                            email: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text"
                    }


                ],
                postData: {
                    oper: "oper"
                },
                onSelectRow: seleccionarArea,
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridClt').trigger("reloadGrid", [{
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
                pager: "#pagerClt"
            });

    jQuery('#gridClt').jqGrid('navGrid', '#pagerClt',
            {
                edit: true,
                edittitle: "Modificar cliente",
                add: true,
                addtitle: "Agregar cliente",
                search: true,
                searchtitle: "Buscar cliente",
                del: false,
                refresh: true,
                view: true,
                viewtitle: "Consultar datos del cliente",
                beforeRefresh: refrescarGrilla
            },
    {
        editCaption: "Modificar cliente",
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true
    },
    {
        addCaption: "Agregar cliente",
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        searchCaption: "Buscar cliente",
        multipleSearch: false

    },
    {
        width: 400,
        caption: "Consultar datos del cliente",
        reloadAfterSubmit: true
    }
    );


});
