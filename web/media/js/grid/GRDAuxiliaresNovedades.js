/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridJust").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridJust").trigger("reloadGrid");

        return [true, ""];
    };


    jQuery('#gridJust').jqGrid({
                
                shrinkToFit: false,
                ignoreCase: true,
                caption: "Auxiliares con Permiso o Incapacidad",
                url: "UsuarioServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val(),
                loadonce: true,
                rowNum: 5,
                rownumbers: false,
                datatype: "json",
                colNames: ["Id", "Tipo", "Auxiliar", "Inicio", "Final"],
                colModel: [
                    {
                        "name": "Id",
                        "index": "Id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "tipo",
                        "index": "tipo",
                        editable: true,
                        edittype: "text",
                        align: 'center',
                        width: 70
                    },
                    {
                        "name": "nombre",
                        "index": "nombre",
                        editable: true,
                        edittype: "text",
                        width: 130
                    },
                    {
                        "name": "inicio",
                        "index": "inicio",
                        editable: true,
                        edittype: "text",
                        width: 70
                    },
                    {
                        "name": "fin",
                        "index": "fin",
                        editable: true,
                        edittype: "text",
                        width: 70
                    }


                ],
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridJust').trigger("reloadGrid", [{
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
                pager: "#pagerJust"
            });


});