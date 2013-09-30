/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function($) {
    $.datepicker.setDefaults({
        showOn: "both",
        maxDate: null
    });

    $("#tr_costo").hide();
    $("#tr_tiempo").hide();
    $("#tr_auxiliar").hide();
    var perfil = $.ajax({
        type: "POST",
        url: "SolicitudServlet",
        async: false,
        data: {
            oper: "perfil"
        }
    }).responseText;
    if (perfil !== "4") {
        $("#auxiliar").load('getUsuarioPerfil.htm?perfil=4');
        $("#tr_auxiliar").show();
    }

    $($(".hora")[0]).mobiscroll('setValue', ["00", "00"]);
    $($(".hora")[1]).mobiscroll('setValue', ["23", "59"]);

    $('#novedad').load('getNovedades.htm');
    $('#novedad').change(function(e) {
        var val = $(e.target).val();
        $("#tr_costo").hide();
        $("#tr_tiempo").hide();
        var nov;
        $.ajax({
            dataType: "json",
            url: "RefNovedadServlet",
            async: false,
            type: "POST",
            data: {
                oper: "consulta",
                id: val
            },
            success: function(data) {
                nov = data.novedad;
            }


        });

        switch (nov.tipo) {
            case 0:
                $("#tr_tiempo").show();
                $(".hora").mobiscroll('option', 'readonly', false);
                break;
            case 1:
                $("#tr_tiempo").show();
                $(".hora").mobiscroll('option', 'readonly', true);

                $($(".hora")[0]).mobiscroll('setValue', ["00", "00"], true);
                $($(".hora")[1]).mobiscroll('setValue', ["23", "59"], true);
                break;
            case 2:
                $("#tr_costo").show();
                break;
        }
    });
    $('#detalle').keypress(function() {
        if ($('#detalle').val().length > 149) {
            return false;
        }
    });
    $(".fecha").val($.datepicker.formatDate("dd/mm/yy", new Date()));
    $(".fecha").datepicker();
    $(".hora").mobiscroll().time({
        width: '100',
        timeFormat: "HH:ii",
        timeWheels: "H:ii",
        hourText: 'Hora',
        minuteText: 'Minutos',
        setText: 'Aceptar',
        cancelText: 'Cancelar'
    });
    $("#reset").click(function() {
        $(".error").remove();
    });
    $("#registrar").click(function() {
//        alert("click");
        if (validar()) {
            if (!verificarNovedad()) {
                if (navigator.appName === "Microsoft Internet Explorer") {
                    $("#incapacidad").submit();
                    alert("Novedad Registrada");
                } else {

                    var form = new FormData($("form#incapacidad")[0]);
                    if (sessionStorage.length > 0) {
                        form.append('nomina', sessionStorage.getItem('nm'));
                    }
                    var result = $.ajax({
                        type: 'POST',
                        async: false,
                        url: "NovedadesServlet",
                        data: form,
                        processData: false,
                        contentType: false
                    }).responseText;
                    alert(result);
                    $("#reset").click();
                }
            } else {
                alert("Ya existe una novedad registrada en este rango de tiempo");
            }
        }
    });
    function validar() {
        $(".error").remove();
        if ($("#novedad").val() === "0") {
            $("#novedad").focus().after("<span class='error'>Seleccione una novedad</span>");
            return false;
        }
        if ($("#tr_auxiliar").is(":visible")) {
            if ($("#auxiliar").val() === "-1") {
                $("#auxiliar").focus().after("<span class='error'>Seleccione un auxiliar</span>");
                return false;
            }
        }

        for (var i = 0; i < $(".fecha:visible").length; i++) {
            if ($(".fecha:visible")[i].value === "") {
                $($(".fecha:visible")[i]).focus().after("<span class='error'>Especifique una fecha</span>");
                return false;
            }
        }

        var fechaInicio = $($(".fecha:visible")[0]).datepicker('getDate');
        var fechaFin = $($(".fecha:visible")[1]).datepicker('getDate');
        if (fechaInicio > fechaFin) {
            $($(".fecha")[1]).focus().after("<span class='error'>La fecha de inicio debe ser menor que la fecha final</span>");
            return false;
        }

        if ($("#tr_costo").is(":visible")) {
            if ($("#costo").val() === "") {
                $("#costo").focus().after("<span class='error'>Especifique el valor</span>");
                return false;
            }
            if (isNaN($("#costo").val())) {
                $("#costo").focus().after("<span class='error'>Digite solo numeros</span>");
                return false;
            }
        }
        if ($("#tr_tiempo").is(":visible")) {
            for (i = 0; i < $(".hora:visible").length; i++) {
                if ($(".hora:visible")[i].value === "") {
                    $($(".hora:visible")[i]).focus().after("<span class='error'>Especifique una hora</span>");
                    return false;
                }
            }

            var horaInicio = $($(".hora:visible")[0]).mobiscroll('getDate');
            var horaFin = $($(".hora:visible")[1]).mobiscroll('getDate');
            if (fechaInicio.toString() === fechaFin.toString()) {
                if (horaInicio >= horaFin) {
                    $($(".hora:visible")[1]).focus().after("<span class='error'>La hora de inicio debe ser menor que la hora fin</span>");
                    return false;
                }
            }
        }
        if ($("#archivo").val() !== "") {

            var extension = $("#archivo").val().split(".")[1];
            if (!(extension === "tiff" || extension === "jpg")) {
                $("#archivo").focus().after("<span class='error'>Seleccione un archivo tiff o jpg</span>");
                return false;
            }
        }
        if ($("#detalle").val() === "") {
            $("#detalle").focus().after("<span class='error'>Escriba una descripcion de su novedad</span>");
            return false;
        }


        return true;
    }

    function verificarNovedad() {
        var result = $.ajax({
            type: 'POST',
            async: false,
            url: "NovedadesServlet",
            data: {
                oper: "consulta",
                novedad: $('#novedad').val(),
                auxiliar: $('#auxiliar').val(),
                fechainicio: $(".fecha")[0].value,
                horainicio: $(".hora")[0].value,
                fechafin: $(".fecha")[1].value,
                horafin: $(".hora")[1].value
            }
        }).responseText;
        if (result === "true") {
            return true;
        } else {
            return false;
        }
    }
});

