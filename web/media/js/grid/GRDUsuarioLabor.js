/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {
    //$('#grillaLabor').hide();

    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridUsr").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridUsr").trigger("reloadGrid");
        $('#gridLbr').jqGrid('setCaption', "Labores Asignadas");
        $('#gridLbr').jqGrid("clearGridData", true);
        return [true, ""];
    };

    var seleccionarReferencia = function() {
        //$('#grillaLabor').show();
        $('#btnAsignar').attr("disabled", false);

        var gridUsr = $('#gridUsr'),
                selRowIdUsr = gridUsr.jqGrid('getGridParam', 'selrow'),
                rowDataUsr = gridUsr.jqGrid('getRowData', selRowIdUsr);

        var idusuario = rowDataUsr.Id;

        var gridLabor = $('#gridLbr');
        gridLabor.jqGrid('setCaption', "Labores Asignadas a " + rowDataUsr.nombre);
        gridLabor.jqGrid('setGridParam', {
            url: "UsuarioLaboresServlet?usuario=" + idusuario,
            datatype: "json",
            pager: "#pagerLbr"
        });
        gridLabor.jqGrid('setGridState', 'visible');
        gridLabor.trigger("reloadGrid");
    };

    jQuery('#gridUsr').jqGrid(
            {
                height: 240,
                hoverrows: false,
                gridview: true,
                hidegrid: false,
                ignoreCase: true,
                caption: "Auxiliares",
                url: "UsuarioServlet",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["Id", "Nombre de Usuario ", "Auxiliares"],
                colModel: [
                    {
                        "name": "Id",
                        "index": "Id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "usuario",
                        "index": "usuario",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "text",
                        width: 120,
                        hidden: true
                    },
                    {
                        "name": "nombre",
                        "index": "nombre",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        edittype: "text",
                        width: 300
                    }
                ],
                onSelectRow: seleccionarReferencia,
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridUsr').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }, 1);
                    }
                },
                postData: {
                    oper: "oper"
                },
                loadError: function(xhr, status, err) {
                    try {
                        jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                                '<div class="ui-state-error">' + xhr.responseText + '</div>',
                                jQuery.jgrid.edit.bClose, {
                            buttonalign: 'right'
                        });
                    } catch (e) {
                        alert(xhr.responseText);
                    }
                },
                pager: "#pagerUsr"

            });


    jQuery('#gridUsr').jqGrid('navGrid', '#pagerUsr',
            {
                edit: false,
                add: false,
                search: true,
                searchtitle: "Buscar Usuario",
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            }, {/*Edit*/}, {/*Add*/}, {/*Del*/}, {searchCaption: "Buscar Usuario"},
    {
        reloadAfterSubmit: true
    }
    );
    jQuery('#gridUsr').jqGrid('navButtonAdd', "#pagerUsr", {
        caption: "",
        title: "Asignar nueva Labor",
        buttonicon: "ui-icon-plus",
        position: "first",
        id: "btnAsignar"
    });

    jQuery("#gridUsr").jqGrid('bindKeys');
});



