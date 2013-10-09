/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridAux").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridAux").trigger("reloadGrid");

        return [true, ""];
    };


    jQuery('#gridAux').jqGrid(
            {
                
                shrinkToFit: true,
                ignoreCase: true,
                caption: "Auxiliares Ausentes Hoy",
                url: "UsuarioServlet?grupo=" + $("#grupo").val(),
                loadonce: true,
                rowNum: 5,
                rownumbers: true,
                datatype: "json",
                colNames: ["Id", "Auxiliar", "Login"],
                colModel: [
                    {"name": "Id", "index": "Id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "nombres",
                        "index": "nombres",
                        editable: true,
                        edittype: "text",
                        width: 343

                    },
                    {
                        "name": "login",
                        "index": "login",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    }


                ],
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridAux').trigger("reloadGrid", [{
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
                pager: "#pagerAux"
            });

});