jQuery(document).ready(function($) {
    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridLbr").jqGrid('setGridParam', {
            datatype: "json"
        })
        jQuery("#gridLbr").trigger("reloadGrid");
        return [true, ""]
    }

    jQuery('#gridLbr').jqGrid(
            {
                height: 240,
                width: $('#grillaLabor').width(),
                hidegrid: false,
                hoverrows: false,
                gridview: true,
                ignoreCase: true,
                url: "UsuarioLaboresServlet",
                caption: "Labores Asignadas",
                loadonce: true,
                shrinkToFit: false,
                rowNum: 10,
                rownumbers: true,
                datatype: "clientArray",
                colNames: ["id", "Cliente", "Area", "Grupo", "Labor", "Tipo", "Extra"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "cliente",
                        "index": "cliente",
                        editable: true,
                        edittype: "text",
                        width: 100

                    }, {
                        "name": "area",
                        "index": "area",
                        editable: true,
                        edittype: "text",
                        width: 70
                    }, {
                        "name": "grupo",
                        "index": "grupo",
                        editable: true,
                        edittype: "text",
                        width: 70
                    },
                    {
                        "name": "labor",
                        "index": "labor",
                        editable: false,
                        edittype: "text",
                        editoptions: {
                            readonly: true,
                            size: 90,
                            dataUrl: "getLabores.htm"
                        },
                        editrules: {
                            required: true
                        },
                        width: 470
                    }, {
                        "name": "tipo",
                        "index": "tipo",
                        editable: true,
                        edittype: "text",
                        width: 70
                    }, {
                        "name": "extra",
                        "index": "extra",
                        editable: true,
                        edittype: "text",
                        width: 100
                    }
                ],
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
                pager: "#pagerLbr"

            });


    jQuery('#gridLbr').jqGrid('navGrid', '#pagerLbr',
            {
                edit: false,
                add: false,
                search: true,
                searchtitle: "Buscar Labor",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            }, {/*Edit*/}, {/*Add*/}, {/*Del*/}, {
        searchCaption: "Buscar Labor"
    },
    {
        reloadAfterSubmit: true
    }
    );

});
