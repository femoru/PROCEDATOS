/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var prorroga;
$("#incapacidad").hide();
$("#incAnt, #nroIncCg").attr("disabled", "");

$("#formNov").ajaxForm({
    clearForm: true,
    beforeSubmit: function(arr) {

        var disableds = $(":disabled"), campo;

        for (var i = 0; i < disableds.length; i++) {
            campo = {
                name: disableds[i].name,
                value: disableds[i].value
            };
            arr.push(campo);
        }
        return validar();
    },
    success: function(data) {
        if (data[0]) {
            alert(data[1]);
        }
    }, error: function() {
        alert('Error al ingresar');
    }
});
$("#auxiliar").load('getUsuarioPerfil.htm?perfil=4');
$('#novedad').load('getNovedades.htm');
$("#auxiliar,#novedad,#clsInc").combobox();
$("#indPro,.ui-button, #sopInc").button();
$(".numero").keydown(function(e) {
    if (!((e.keyCode >= 48 && e.keyCode <= 57)
            || (e.keyCode >= 96 && e.keyCode <= 105)
            || (e.keyCode >= 35 && e.keyCode <= 38)
            || e.keyCode === $.ui.keyCode.COMMA
            || e.keyCode === $.ui.keyCode.BACKSPACE
            || e.keyCode === $.ui.keyCode.ENTER
            || e.keyCode === $.ui.keyCode.DELETE
            || e.keyCode === $.ui.keyCode.TAB
            || e.keyCode === 116)) {
        return false;
    }
});

$("#novedad_cb").autocomplete({select: function(event, ui) {
        var nov;
        $.ajax({
            dataType: "json",
            url: "RefNovedadServlet",
            async: false,
            type: "POST",
            data: {
                oper: "consulta",
                id: $("#novedad").val()
            },
            success: function(data) {
                nov = data.novedad;
            }
        });
        if (nov.tipo === 2) {
            $("#incapacidad").slideDown();
        } else {
            $("#incapacidad").slideUp();
        }
    }});

var vlrDia;

$('#dias').spinner({min: 1, max: 999, spin: fechaFin, change: fechaFin});
$('.datepicker').datepicker();
$('#dateIni').change(fechaFin);
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
        if ($("#incAnt").val() === "0") {
            $("#incAnt").focus().after("<span class='error' style='z-index:100;'>Selecciona una opción</span>");
            $('.error').hover(function() {
                $('.error').remove();
            });
            return false;
        }
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
