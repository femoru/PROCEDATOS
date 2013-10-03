/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var prorroga;
var selrow;
var vlrDia = 19650;
$("#incapacidad,#valores,#vacaciones").hide();
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
            selrow = jQuery('#gridNov').jqGrid('getGridParam', 'selrow');
            var rowdata = $('#gridNov').jqGrid('getRowData', selrow);
            $("#dateFin").attr("disabled", true);
            $("#dias").spinner("enable");
            $("#dateIni").val(rowdata.inicio);
            $("#vlrNov").val("");

            switch (parseInt(rowdata.tipoNov)) {
                case 0:
                    $("#vacaciones").slideDown();
                    $("#dateFin").addClass("datepicker").removeAttr("disabled").datepicker({onSelect: function(dateText) {
                            diasLaborales($("#dateIni").val(), dateText);
                        }});
                    diasLaborales($("#dateIni").val(), rowdata.fin);
                    $("#dias").spinner("disable");
                    break;
                case 2:
                    $("#incapacidad").slideDown();
                    break;
                case 3:
                    $("#valores").slideDown();
                    $("#vlrNov").val(rowdata.valorNov);
                    break;
            }



            $("#nroInc").focus();
            $('.datepicker').datepicker();
            $("#prc").val(100);
            $('#incAnt').load('NovedadesServlet?oper=l&idusuario=' + rowdata.idusuario + "&idnovedad=" + selrow);

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
                        tipo: rowdata.tipoNov,
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
                        diasSIO: parseInt(rowdata.tipoNov) === 2 ? $("#diasSIO").val() : $("#diasHab").val(),
                        diasEPS: parseInt(rowdata.tipoNov) === 2 ? $("#diasEPS").val() : $("#diasNoHab").val(),
                        diasComp: $("#diasComp").val() === "" ? 0 : parseInt($("#diasComp").val()),
                        vlrSIO: parseInt(rowdata.tipoNov) === 3 ? $("#vlrNov").val() : $("#vlrSIO").val(),
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
            $("#indPro").attr('checked', false).change();
            $("#incapacidad,#valores,#vacaciones").hide();
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
        $("#dateFin").val($.datepicker.formatDate("dd/mm/yy", fecha))
                ;
    }
    var dias = diasLaborales($("#dateIni").val(), $("#dateFin").val());
    $("#diasHab").val(dias);
    $("#diasNoHab").val($("#dias").val() - dias);
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
    if (cantDias !== parseInt($('#dias').val()) && $(".dias").is(':visible')) {
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
function diasLaborales(d1, d2) {
    var lab;
    $.ajax({
        type: "POST",
        url: "NovedadesServlet",
        async: false,
        data: {
            oper: "habiles",
            fechaInicial: d1,
            fechaFinal: d2
        },
        success: function(data) {
            lab = data;
        },
        error: function() {
            lab = 0;
        }
    });
    $("#diasHab").val(lab);
    var dias = $.datepicker.parseDate('dd/mm/yy', d2) - $.datepicker.parseDate('dd/mm/yy', d1);
    dias = (dias / 24 / 60 / 60 / 1000) + 1;
    $("#dias").val(dias);
    $("#diasNoHab").val(dias - lab);
    activarCompensados();
    return lab;
}

function activarCompensados() {
    if (parseInt($("#diasHab").val()) === 15) {
        $("#diasComp").removeAttr("disabled");
        $("#diasComp").val(0);
    } else {
        $("#diasComp").attr("disabled", true);
        $("#diasComp").val("");
    }
}