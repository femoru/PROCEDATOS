/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 
 */
jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridParam").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridParam").trigger("reloadGrid");
        return [true, ""];
    };
    jQuery('#gridParam').jqGrid(
            {
                width: 600,
                height: 240,
                hoverrows: false,
                hidegrid: false,
                //viewrecords:true,
                gridview: true,
                ignoreCase: true,
                caption: "Parametros",
                url: "ParametroServlet",
                editurl: "ParametroServlet",
                cellurl: "ParametroServlet",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["Id", "Descripción", "Valor"],
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
                        width: 80,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 100,
                            style: "text-transform: uppercase"
                        },
                        edittype: "text"
                    },
                    {
                        "name": "Valor",
                        "index": "Valor",
                        editable: true,
                        width: 100,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 100
                        },
                        edittype: "text",
                        search: false
                    }
                ],
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridParam').trigger("reloadGrid", [{
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
                pager: "#pagerParam"
            });
    jQuery('#gridParam').jqGrid('navGrid', '#pagerParam',
            {
                edit: true,
                edittitle: "Modificar Parametro",
                add: false,
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        beforeInitData: function() {
            jQuery('#gridParam').setColProp('Descripcion', {
                editable: true,
                edittype: "text",
                editoptions: {
                    readonly: 'readonly'
                }
            });
        },
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        editCaption: "Modificar parametro ",
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
        searchCaption: "Buscar parametro",
        multipleSearch: false

    },
    {
        reloadAfterSubmit: true
    }
    );

});
 