/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function($) {

    var idusuario;
    var siImagenes = false;
    var siHoras = false;
    var mensajeSalida = "";
    $("#imagenes").hide();
    $("#tr_labor").hide();
    $("#lbr_tr").hide();
    $("#salida").hide();
    $("#guias").hide();
    $("#tr_dato").hide();
    $("#tr_registro").hide();
    $("#tr_entradaSalida").hide();

    $("#entrada").click(function() {
        if ($("#labor").val() > 0) {
            var result = $.ajax({
                type: "POST",
                url: "RegistrosServlet",
                async: false,
                data: {
                    oper: "in",
                    usuario: idusuario,
                    labor: $("#labor").val()
                }
            }).responseText;
            if (result === "true") {
                alert("Se registro la Entrada");
            } else {
                alert("No se pudo registrar entrada \n\nPuede que exista una labor sin registrar salida");
            }
            limpiar();
        } else {
            alert("Seleccione una labor para continuar");
        }
    });
    var cont = 1;
    $("#btnguias").click(function() {

        var linead = '';
        var linear = '';
        if (!$("#tr_dato").is(":visible")) {
            linead = 'style="display: none;"';
        }
        if (!$("#tr_registro").is(":visible")) {
            linear = 'style="display: none;"';
        }

        var tds = '<tr id="tr_guia_' + cont + '" class="guias">';
        tds += '<td ' + linead + '>Dato labor <input style="width: 200px;text-align: center" type="text" id="dato_' + cont + '" class="dato"/>  </td>';
        tds += '<td ' + linear + '>Registros <input type="text" style="width: 100px;text-align: center" id="registros_' + cont + '" class="registros" /></td>';
        tds += '<td><input type="button" value="-" onclick="$(\'#tr_guia_' + cont + '\').remove()"></td>';
        tds += '</tr>';
        cont++;
        $("#guias").after(tds);
    });
    $(".dato").keypress(function() {
        $(".error").remove();
    });
    $(".registros").keypress(function() {
        $(".error").remove();
    });
    $("#salida").click(function() {
        $(".error").remove();
        if ($("#tr_dato").is(":visible")) {

            for (var i = 0; i < $(".dato").length; i++) {
                if ($(".dato")[i].value === "") {
                    $($(".dato")[i]).focus().after("<span class='error'>Digite el dato de la labor</span>");
                    return false;
                }
            }
        }
        if ($("#tr_registro").is(":visible")) {
            for (var j = 0; j < $(".registros").length; j++) {
                if ($(".registros")[j].value === "") {
                    $($(".registros")[j]).focus().after("<span class='error'>Digite el numero de registros</span>");
                    return false;
                }
            }
        }
        if ($("#tr_imagenes").is(":visible")) {
            for (var j = 0; j < $(".imagenes").length; j++) {
                if ($(".imagenes")[j].value === "") {
                    $($(".imagenes")[j]).focus().after("<span class='error'>Digite el numero de imagenes</span>");
                    return false;
                }
            }
        }
        if ($(".guias").is(":visible")) {

            for (var k = 0; k < $(".guias").length; k++) {
                var dato = $(".dato")[k].value;
                var registros = $(".registros")[k].value;
                //quitar imagenes cuando tenga acceso a digitalización

                if (siImagenes === true) {
                    var imagenes = $(".imagenes")[k].value;
                } else {
                    var imagenes = 0;
                }

                if (registrarSalidas(dato, registros, imagenes) === "false") {
                    alert("No se pudo registrar salida para la guia " + dato);
                }

            }

        } else {
            if (registrarSalidas(0, 0, 0) === "false") {
                alert("No se pudo registrar salida, por favor informe al coordinador");
            }
        }
//Activar cuando se tenga acceso al esquema de digitalización

        if (siImagenes === true) {

            var imagenes = $.ajax({
                type: "GET",
                url: "CalculoImagenesServlet",
                async: false,
                data: {
                    login: $("#login").val()
                }
            }).responseText;
            if (imagenes === '-0') {
                alert("Ocurrio un error al validar el registro. Por favor informe al coordinador.");
            } else if (imagenes === '-1') {
                alert("Ocurrio un error al validar con formularios. Por favor informe al coordinador.");
            } else {
                alert("Se registro la salida con: " + imagenes + " imagenes.");
            }

        }

        var horas = $.ajax({
            type: "GET",
            url: "CalculoHorasServlet",
            async: false,
            data: {
                login: $("#login").val()
            }
        }).responseText;
        if (horas === '-1') {
            alert("Ocurrio un error al validar las horas. Por favor informe al coordinador.");
            limpiar();
            return false;
        }
        else {
            mensajeSalida = "con: " + horas + " minutos.";
        }



        alert("Se registro la salida " + mensajeSalida);

        document.location.reload();
        return true;
    });
    function registrarSalidas(dato, registros, imagenes) {
        //quitar variable imagenes cuando se tenga acceso
        var result = $.ajax({
            type: "POST",
            url: "RegistrosServlet",
            async: false,
            data: {
                oper: "out",
                usuario: idusuario,
                labor: $("#labor").val(),
                dato: dato,
                registros: registros,
                imagenes: imagenes
            }
        }).responseText;
        return result;
    }


    function limpiar() {
        $("#imagenes").hide();
        $("#login").val("");
        $("#nombre").val("");
        $("#pass").val("");
        $(".dato").val("");
        $(".registros").val("");
        $(".imagenes").val("");
        $("#tr_labor").hide();
        $("#lbr_tr").hide();
        $("#tr_dato").hide();
        $("#guias").hide();
        $(".guias").remove();
        $("#tr_registro").hide();
        $("#salida").hide();
        $("#entrada").show();
        $("#entrada").attr("disabled", true);
        $("#tr_entradaSalida").hide();
        siHoras = false;
        siImagenes = false;
    }

    $("#login").focus(function() {
        limpiar();
    });
    $("#login").keypress(function(event) {
        if (event.which === 13) {
            $("#login").blur();
        }
    });
    $("#pass").keypress(function(event) {
        if (event.which === 13) {
            $("#consulta").click();
        }
    });
    $("#login").blur(function(e) {
        var login = $(e.target).val();
        if (login !== "") {
            $.ajax({
                type: "GET",
                url: "LoginServlet",
                async: false,
                data: {
                    login: $("#login").val()
                }, success: function(data) {
                    if (data.existe) {
                        $("#nombre").val(data.nombre);
                    } else {
                        alert("El usuario no existe");
                        limpiar();
                    }
                }
            });
        }
    });

    $("#consulta").click(function() {
        $.ajax({
            type: "GET",
            url: "RegistrosServlet",
            async: false,
            data: {
                login: $("#login").val(),
                clave: $("#pass").val()
            }, success: function(data) {
                if (data.error === "null") {
                    $("#tr_entradaSalida").show();
                    idusuario = data.idusuario;
                    if (data.abierta) {
                        $("#lbr").val(data.labor);
                        $("#lbr_tr").show();
                        if (data.requiere !== 0) {
                            $("#guias").show();
                            $("#tr_dato").show();
                            $("#imagenes").hide();
                        }
                        switch (data.tipolabor) {
                            case 3:
                                {
                                    $("#guias").addClass("guias");
                                    $("#guias").show();
                                    $("#tr_registro").show();
                                    $("#imagenes").hide();
                                }
                                break;
                            case 4:
                                {
                                    siImagenes = true;
                                }
                                break;
                            case 5:
                                {
                                    $("#guias").addClass("guias");
                                    $("#guias").show();
                                    $("#tr_registro").show();
                                }
                                break;
                            default :
                                {
                                    siHoras = true;
                                }
                        }

                        $("#entrada").hide();
                        $("#salida").attr("disabled", false);
                        $("#salida").show();

                    } else {
                        $("#tr_labor").show();
                        $("#labor").load("getLaboresUsuarios.htm?idusuario=" + idusuario);
                        $("#labor").attr("disabled", false);
                        $("#labor").focus();
                        $("#entrada").attr("disabled", false);
                    }
                } else {
                    alert(data.error);
                    $("#pass").focus().val("");
                }
            }, error: function() {
                alert("Ocurrio un error inesperado por favor informe al administrador del sistema");
            }
        });
    });
});

