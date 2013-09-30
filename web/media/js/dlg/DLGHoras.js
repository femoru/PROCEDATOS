/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {
    $('.fecha').datepicker();
    $('#fechas').dialog({
        title: "Rango de vigencia",
        //width: 950,
        autoOpen: false,
        modal: true,
        dialogClass: "no-close",
        open: function() {
            $($('.fecha')[0]).val($('#fechas').data("inicio"));
            $($('.fecha')[1]).val($('#fechas').data("fin"));
        }
    });
    $('#btnConfirmar').click(function() {
        $.ajax({
            type: "POST",
            async: false,
            url: $('#fechas').data('url'),
            data: {
                id: $('#fechas').data('id'),
                activo: $('#fechas').data('activo'),
                fechaI: $('.fecha')[0].value,
                fechaF: $('.fecha')[1].value
            },
            success: function() {
                $('#fechas').dialog("close");
                jQuery("#gridLbrUsr").jqGrid('setGridParam', {
                    datatype: "json"
                });
                jQuery("#gridLbrUsr").trigger("reloadGrid");

            }
        });
    });
});

