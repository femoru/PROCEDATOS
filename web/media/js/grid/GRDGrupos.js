/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridGrp").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridGrp").trigger("reloadGrid");

        return [true, ""]
    }

    jQuery('#gridGrp').jqGrid(
            {
                height: 248,
                hiddengrid: true,
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                ignoreCase: true,
                caption: "Grupos",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["id", "idarea", "Descripción", "Coordinador"],
                colModel: [
                    {
                        "name": "id",
                        index: "id",
                        editable: false,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "idarea",
                        index: "idarea",
                        editable: false,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "Descripcion",
                        index: "Descripcion",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text",
                        width: 100

                    },
                    {
                        "name": "Coordinador",
                        index: "Coordinador",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        editrules: {
                            required: true
                                    , edithidden: true
                        },
                        searchoptions: {
                            dataUrl: "getCoordinadores.htm"
                        },
                        editoptions: {
                            dataUrl: "getCoordinadores.htm"
                        },
                        width: 150

                    }
                ],
                postData: {
                    oper: "oper"
                },
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridGrp').trigger("reloadGrid", [{
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
                pager: "#pagerGrp"
            });

    jQuery('#gridGrp').jqGrid('navGrid', '#pagerGrp',
            {
                edit: true,
                edittitle: "Modificar grupo seleccionado",
                add: true,
                addtitle: "Agregar grupo",
                search: true,
                searchtitle: "Buscar grupo",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        editCaption: "Modificar grupo",
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true
    },
    {
        addCaption: "Agregar grupo",
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        searchCaption: "Buscar grupo",
        multipleSearch: false
    },
    {
        reloadAfterSubmit: true
    }
    );


});

