/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var prorroga;
var selrow;
var vlrDia = 19650;
$("#incapacidad").hide();
$("#incAnt, #nroIncCg").attr("disabled", "");
jQuery(document).ready(function($) {
    $('#clsInc').combobox();
    $("#indPro").button();
    $('#dias').spinner({min: 1, max: 999, spin: fechaFin, change: fechaFin});
    $('#dateIni').change(fechaFin);
    $("#clsInc_cb").autocomplete({select: function() {
            if ($("#clsInc").val() === "3") {
                $("#dateAcc").removeAttr("disabled");
            } else {
                $("#dateAcc").attr("disabled", "");
            }
        }});
    $(".dias").change(function() {
        $('.error').remove();
        var cantDias = (parseInt($(".dias")[0].value) + parseInt($(".dias")[1].value));
        if (cantDias !== parseInt($('#dias').val())) {
            $(this).focus().after("<span class='error' style='z-index:100;'>Suma de dias diferente</span>");
            $('.error').hover(function() {
                $('.error').remove();
            });
        }
        var vlr = $(this).val() * vlrDia;
        if ($(this).attr('name').indexOf("SIO") !== -1) {
            $("#vlrSIO").val(vlr);
        } else {
            $("#vlrEPS").val(vlr);
        }

    });
    $('#dlgvalidar').dialog({
        width: 'auto',
        autoOpen: false,
        modal: true,
        dialogClass: "no-close",
        open: function(event, ui) {
            $("#nroInc").focus();
            $('.datepicker').datepicker();
            $("#prc").val(100);
            selrow = jQuery('#gridNov').jqGrid('getGridParam', 'selrow');
            var rowdata = $('#gridNov').jqGrid('getRowData', selrow);
            $('#incAnt').load('NovedadesServlet?oper=l&idusuario=' + rowdata.idusuario + "&idnovedad=" + selrow);
            $("#dateIni").val(rowdata.inicio);
            $("#dias").val(rowdata.diasTNL);
            $("#dateFin").val(rowdata.fin);
            $("#nroInc").val(rowdata.nroInc);
            $("#codDx").val(rowdata.codDx);
            $('#dlgvalidar').dialog("option", "title", rowdata.tipo + " DE " + rowdata.auxiliar);
            $.ajax({
                dataType: "json",
                url: "PersonaServlet",
                async: false,
                type: "POST",
                data: {
                    oper: "get",
                    idusuario: rowdata.idusuario
                },
                success: function(data) {
                    vlrDia = data;
                }
            });
            calcularDias(rowdata.diasTNL);
        },
        buttons: [
            {text: "Cancelar", click: function() {
                    $(this).dialog("close");
                }},
            {text: "Guardar/Validar", click: function() {
                    var rowdata = $('#gridNov').jqGrid('getRowData', selrow);
                    var enviar = {
                        oper: "validar",
                        idnovedad: selrow,
                        idusuario: rowdata.idusuario,
                        dateIni: $("#dateIni").val(),
                        dias: $("#dias").val(),
                        dateFin: $("#dateFin").val(),
                        nroInc: $("#nroInc").val(),
                        codDx: $("#codDx").val(),
                        clsInc: $("#clsInc").val(),
                        dateAcc: $("#dateAcc").val(),
                        indPro: $("#indPro").is(':checked') ? 1 : 0,
                        incAnt: $("#incAnt").val(),
                        nroIncCg: $("#nroIncCg").val(),
                        diasSIO: $("#diasSIO").val(),
                        diasEPS: $("#diasEPS").val(),
                        vlrSIO: $("#vlrSIO").val(),
                        prc: $("#prc").val(),
                        vlrEPS: $("#vlrEPS").val()
                    };
                    if (validar()) {
                        var confirm = false;
                        $.ajax({
                            dataType: "json",
                            url: "NovedadesServlet",
                            async: false,
                            type: "POST",
                            data: enviar,
                            success: function(data) {
                                confirm = data;
                            }, error: function() {
                                alert("Error al ingresar");
                            }
                        });
                        if (confirm) {
                            $(this).dialog("close");
                        } else {
                            alert("Ocurrio un error Inesperado");
                        }
                    }

                }}
        ],
        close: function() {
            $('#dlgvalidar input').val("");
            refrescarGrilla();
        }
    });
});


$("#prc").change(function() {

    $("#vlrEPS").val((($("#diasEPS").val()) * ((vlrDia * $("#prc").val()) / 100)).toFixed(0));
});
$("#indPro").change(function() {
    if ($("#indPro").is(":checked")) {
        prorroga = true;
        $("#indPro").button("option", "icons", {primary: "ui-icon-check"});
        $("#incAnt, #nroIncCg").removeAttr("disabled");
    } else {
        prorroga = false;
        $("#indPro").button("option", "icons", {primary: null});
        $("#incAnt, #nroIncCg").attr("disabled", "");
    }
});
$(".numero").keydown(function(e) {
    if (!((e.keyCode >= 48 && e.keyCode <= 57)
            || (e.keyCode >= 96 && e.keyCode <= 105)
            || (e.keyCode >= 35 && e.keyCode <= 39)
            || e.keyCode === 110
            || e.keyCode === $.ui.keyCode.BACKSPACE
            || e.keyCode === $.ui.keyCode.ENTER
            || e.keyCode === $.ui.keyCode.DELETE
            || e.keyCode === $.ui.keyCode.TAB
            || e.keyCode === 116)) {
        return false;
    }
});
function fechaFin(event, ui) {
    var number = 1;
    if (ui && ui.value) {
        number = ui.value;
        $('#dias').spinner("value", 1);
    } else {
        number = parseInt($('#dias').val());
    }
    var fecha = $("#dateIni").datepicker('getDate');
    if (isNaN(number)) {
        number = 1;
        $('#dias').spinner("value", number);
    }
    if (fecha) {
        fecha.setDate(fecha.getDate() + number - 1);
        $("#dateFin").val($.datepicker.formatDate("dd/mm/yy", fecha));
    }

    calcularDias(number);
}
function calcularDias(dias) {
    var number, porc;
    if (isNaN(dias))
        number = parseInt($('#dias').val());
    else
        number = dias;
    if ($('#prc').val() !== "") {
        porc = parseFloat($('#prc').val());
    } else {
        porc = 100;
    }
    if (number > 3) {
        $("#diasSIO").val(3);
        $("#vlrSIO").val(3 * vlrDia);
        $("#diasEPS").val((number - 3));
        $("#vlrEPS").val(((number - 3) * ((vlrDia * porc) / 100)).toFixed(0));
    } else {
        $("#diasSIO").val(number);
        $("#vlrSIO").val(number * vlrDia);
        $("#diasEPS").val(0);
        $("#vlrEPS").val(0);
    }
}
function validar() {
    $('.error').slideDown().remove();
    var inputs = $(':text:visible:enabled');
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].value === "") {
            $(inputs[i]).focus().after("<span class='error' style='z-index:100;'>Por favor llena este campo</span>");
            $('.error').hover(function() {
                $('.error').remove();
            });
            return false;
        }
    }
    if (prorroga) {
        if ($("#incAnt").val() === "0" && $("#nroIncCg").val() === "") {
            $("#incAnt").focus().after("<span class='error' style='z-index:100;'>Selecciona una opción</span>");
            $('.error').hover(function() {
                $('.error').remove();
            });
            return false;
        }
    }

    var cantDias = (parseInt($(".dias")[0].value) + parseInt($(".dias")[1].value));
    if (cantDias !== parseInt($('#dias').val())) {
        $($(".dias")[1]).focus().after("<span class='error' style='z-index:100;'>Suma de dias diferente</span>");
        $('.error').hover(function() {
            $('.error').remove();
        });
        return false;
    }

    if ($("#obsv").val() === "") {
        $("#obsv").focus().after("<span class='error' style='z-index:100;'>Escribe una pequeña descripción</span>");
        $('.error').hover(function() {

            $('.error').remove();
        });
        return false;
    }
    return true;
}