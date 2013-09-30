/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        var myGrid = jQuery("#gridLRef");
        myGrid.jqGrid('setGridParam', {
            datatype: "json"
        });
        myGrid[0].refreshIndex();
        myGrid.trigger("reloadGrid", [{
                page: $('#gridLRef').getGridParam('lastpage')
            }]);

        return [true, ""];
    };

    jQuery('#gridLRef').jqGrid(
            {
                hiddengrid: true,
                height: 252,
                hoverrows: false,
                viewrecords: true,
                ignoreCase: true,
                gridview: true,
                caption: "Referencias",
                loadonce: true,
                rowNum: 10,
                rownumbers: false,
                datatype: "local",
                colNames: ["Codigo", "Descripción"],
                colModel: [
                    {
                        "name": "Codigo",
                        index: "Codigo",
                        editable: false,
                        hidden: false,
                        edittype: "text",
                        width: 50
                    },
                    {
                        "name": "Descripcion",
                        index: "Descripcion",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        searchoptions: {
                            style: "text-transform: uppercase"

                        },
                        editoptions: {
                            maxlength: 100,
                            style: "text-transform: uppercase",
                            size: 85
                        },
                        edittype: "text",
                        width: 600
                    }
                ],
                postData: {
                    oper: "oper"
                },
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridLRef').trigger("reloadGrid", [{
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
                pager: "#pagerLRef"
            });

    jQuery('#gridLRef').jqGrid('navGrid', '#pagerLRef',
            {
                edit: true,
                edittitle: "Modificar referencia seleccionada",
                add: true,
                addtitle: "Agregar nueva referencia",
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true,
        editCaption: "Modificar referencia",
        width: 'auto'
    },
    {
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios",
        addCaption: "Agregar referencia",
        width: 'auto'
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
