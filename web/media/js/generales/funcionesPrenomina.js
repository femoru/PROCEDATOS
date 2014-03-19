/* 
 Document   : FRMControlPrenomina
 Created on : 26-May-2013, 09:32:11
 Author     : jcarvajal
 */
var OK = "media/images/exito.png";
var error = "media/images/error2.png";
var datosnomina = new Object();


$(document).ready(function($) {

    if (navigator.appName === "Microsoft Internet Explorer") {
        jQuery(document).ready(function($) {
            $('ul > li').each(function() {
                PIE.attach(this);
            });
            $('ul > li > div').each(function() {
                PIE.attach(this);
            });
            $('.fondo').each(function() {
                PIE.attach(this);
            });
        });
    }


    $("#registros").hide();
    $("#novedades").hide();
    $("#prenomina").hide();
    $("#reversar").hide();

    $("#reversar").click(function(){
        if(validacion()){
            paso1();
            var confirmacion = $.ajax({
                type: "POST",
                async: false,
                url: "PrenominaServlet",
                data: {
                    oper: "rrhh",
                    dateIni: $("#dateIni").val(),
                    dateFin: $("#dateFin").val()
                }
            }).responseText;
            paso2();
        }
    });
    if (sessionStorage.length > 3) {
        $("#dateIni").val(sessionStorage.getItem('fI'));
        $("#dateFin").val(sessionStorage.getItem('fF'));
        cargar();
    }

    $.datepicker.setDefaults({
        showOn: "both",
        maxDate: 0,
        dateFormat: "dd/mm/yy",
        onSelect: function(date) {
            var validados = $.ajax({
                type: "POST",
                async: false,
                url: "PrenominaServlet",
                data: {
                    oper: "validacionDia",
                    fecha: date
                }
            }).responseText;
            limpiarPantalla();

            if (validados === "1") {
                alert('Ya existe una nomina creada para este dia. \n\nVerifique por favor.');
            }
            if ($("#dateIni").val() !== "" && $("#dateFin").val() !== "") {
                cargar();
            }

        }
    });
    $("#dateIni").datepicker();
    $("#dateFin").datepicker();

    $("#cargar").hide();


    $("#registros").click(function() {
        loadNewPage('ValidaLabores.htm');
    });
    $("#prenomina").click(function() {

        sessionStorage.setItem('nm', datosnomina.id);
        sessionStorage.setItem('fI', datosnomina.fechaInicio);
        sessionStorage.setItem('fF', datosnomina.fechaFin);
        sessionStorage.setItem('st', datosnomina.estado);

        loadNewPage('ControlPrenomina.htm');

    });
    $("#novedades").click(function() {
        $("#prenomina").click();
    });
});

function cargar() {

    if (validacion()) {
        limpiarPantalla();
        consultaNomina($("#dateIni").val(), $("#dateFin").val());
        if (datosnomina === null) {

            datosnomina = new Object();
            datosnomina.estado = 0;
        }
        estadoNomina();
    }
}

function limpiarPantalla() {
    $("#registros").hide();
    $("#novedades").hide();
    $("#prenomina").hide();
    $("#reversar").hide();
    var activos = $.find(".ball");
    var imag = $.find("li img");
    for (var i = 0; i < activos.length; i++) {
        $(activos[i]).removeClass("ball");
    }
    for (var i = 0; i < imag.length; i++) {
        $(imag[i]).hide();
    }

}

function estadoNomina() {

    if (datosnomina !== null) {
        switch (datosnomina.estado) {

            case 4:
                {
                    if (!$("#progress4 #layer1").hasClass("ball"))
                        efecto($("#progress4"));
                    if (datosnomina.estado === 4) {
                        $("#progress4").attr("onclick", "paso4()");
                    }
                }
            case 3:
                {
                    if (!$("#progress3 #layer1").hasClass("ball"))
                        efecto($("#progress3"));
                    if (datosnomina.estado === 3) {
                        $("#progress3").attr("onclick", "paso3()");
                        $("#novedades").fadeIn(1500);
                        $("#reversar").fadeIn(1500);
                    }
                    if (datosnomina.estado > 3) {
                        setTimeout(function() {
                            $("#progress3").find("li img").attr("src", OK);
                            $("#progress3").find("li img").fadeIn(1500);
                            $("#novedades").fadeOut();
                        }, 2000);
                    }
                }
            case 2:
                {
                    if (datosnomina.estado === 2) {
                        setTimeout(function() {
                            $("#progress2").find("li img").attr("src", OK);
                            $("#progress2").find("li img").fadeIn(1500);
                            $("#prenomina").fadeIn(1500);
                            $("#reversar").fadeIn(1500);
                        }, 2000);
                    }
                }
            case 1:
                {
                    if (!$("#progress2 #layer1").hasClass("ball"))
                        efecto($("#progress2"));
                    if (datosnomina.estado === 1) {
                        $("#progress2").attr("onclick", "paso2()");
                    }
                    else if (datosnomina.estado > 1) {
                        setTimeout(function() {
                            $("#progress2").find("li img").attr("src", OK);
                            $("#progress2").find("li img").fadeIn(1500);
                        }, 2000);
                    }
                }
            case 0:
                {
                    if (!$("#progress1 #layer1").hasClass("ball"))
                        efecto($("#progress1"));

                    if (datosnomina.estado === 0) {

                        $("#progress1").attr("onclick", "paso1()");
                    } else {
                        setTimeout(function() {
                            $("#progress1").find("li img").attr("src", OK);
                            $("#progress1").find("li img").fadeIn(1500);
                        }, 2000);

                    }
                }
        }
    }
}

function consultaNomina(ini, fin) {
    $.ajax({
        type: "POST",
        async: false,
        url: "PrenominaServlet",
        data: {
            oper: "fechaconsultanomina",
            fechainicio: ini,
            fechafin: fin
        },
        success: function(data) {
            datosnomina = data;
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert("Ocurrio un error inesperado, " + textStatus);
        }
    });
}

function validacion() {
    $(".error").remove();
    if ($("#dateIni").val() === "") {
        $("#dateIni").focus().after("<span class='error'>Ingrese fecha inicial</span>");
        return false;
    }
    if ($("#dateFin").val() === "") {
        $("#dateFin").focus().after("<span class='error'>Ingrese fecha Final</span>");
        return false;
    }

    return true;
}

function crearNomina() {

    $.ajax({
        type: "POST",
        async: false,
        url: "PrenominaServlet",
        data: {
            oper: "agregarNomina",
            dateIni: $("#dateIni").val(),
            dateFin: $("#dateFin").val()
        }, success: function(data) {
            if (data !== null) {
                alert('Se creo la nomina para el rango de fechas:\n' + data.fechaInicio + " - " + data.fechaFin);
                datosnomina = data;
            } else {
                alert('Ya existe una nomina que abarca este rango de fechas,\nPor favor verifique');
            }
        }
    });


}

function efecto(e) {
    $(e).find('#layer1').addClass("ball");
    $(e).find('#layer7').addClass("pulse");
    $(e).removeClass('running');
    $(e).removeClass('running').delay(10).queue(function(next) {
        $(this).addClass('running');
        next();
    });
}

function paso1() {
    $("#progress1").find("li img").fadeOut();
    setTimeout($.unblockUI, 100);

    var confirmacion = -1;
    confirmacion = $.ajax({
        type: "GET",
        async: false,
        url: "PrenominaServlet",
        data: "dateIni=" + $("#dateIni").val() + "&dateFin=" + $("#dateFin").val()
    }).responseText;
    switch (confirmacion) {
        case "0":
            alert('Validacion efectuada satisfactoriamente.');
            $("#progress1").find("li img").attr("src", OK);
            $("#progress1").find("li img").fadeIn(2500);
            $("#registros").fadeOut();
            consultaNomina($("#dateIni").val(), $("#dateFin").val());
            if (datosnomina === null) {
                if (confirm("No existe una nomina para este rango de fechas, ¿Desea crearla?")) {
                    crearNomina();
                    datosnomina.estado = 1;
                }
            }
            $("#progress1").removeAttr("onclick");
            estadoNomina();
            break;
        case "1":
            alert('Existen registros sin validar, por favor verifique');
            document.getElementById('layer1').className = "fondo";

            $("#progress1").find("li img").attr("src", error);
            $("#progress1").find("li img").fadeIn(2500);

            $("#registros").fadeIn();
            break;
        case "2":
            alert('Existen novedades sin validar, por favor verifique');
            document.getElementById('layer1').className = "fondo";

            $("#progress1").find("li img").attr("src", error);
            $("#progress1").find("li img").fadeIn(1500);

            break;
        case "3":
            alert('No existen registros para el rango de fechas solicitadas');
            document.getElementById('layer1').className = "fondo";

            $("#progress1").find("li img").attr("src", error);
            $("#progress1").find("li img").fadeIn(1500);

            break;
        case "4":
            alert('Ocurrio un error en la actualización de los datos, informe al administrador');
            document.getElementById('layer1').className = "fondo";

            $("#progress1").find("li img").attr("src", error);
            $("#progress1").find("li img").fadeIn(1500);

            break;
    }


    $("#progress1").attr("onclick", "paso1()");
}

function paso2() {
    $.ajax({
        url: "PrenominaServlet",
        type: "POST",
        async: false,
        data: {
            oper: "mCalculo",
            usuario: 0,
            nomina: datosnomina.id,
            finicial:datosnomina.fechaInicio,
            ffinal:datosnomina.fechaFin
        },
        success: function(data) {
            if (data === "true") {
                $("#prenomina").fadeIn();
                $("#progress2").find("li img").attr("src", OK);
                $("#progress2").find("li img").fadeIn(1500);
                datosnomina.estado = 2;
                $("#progress2").removeAttr("onclick");
            }
            else {
                alert("Ocurrio un error inesperado ");
                $("#progress2").find("li img").attr("src", error);
                $("#progress2").find("li img").fadeIn(1500);
            }
        }, error: function(jqXHR, textStatus, errorThrown) {
            alert("Ocurrio un error inesperado, " + textStatus);
            $("#progress2").find("li img").attr("src", error);
            $("#progress2").find("li img").fadeIn(1500);
        }
    });
}

function paso3() {

    $("#progress3").find("li img").fadeOut();

    var confirmacion = -1;
    confirmacion = $.ajax({
        type: "POST",
        async: false,
        url: "PrenominaServlet",
        data: {
            oper: "rrhh",
            dateIni: $("#dateIni").val(),
            dateFin: $("#dateFin").val()
        }
    }).responseText;
    switch (confirmacion) {
        case "0":
            alert('Validacion efectuada satisfactoriamente.');

            $("#progress3").find("li img").attr("src", OK);
            $("#progress3").find("li img").fadeIn(2500);
            $("#registros").fadeOut();
            //datosnomina.estado = 4;
            $("#progress3").removeAttr("onclick");
            estadoNomina();
            break;
        case "1":
            alert('Existen registros sin validar, por favor verifique');
            document.getElementById('layer1').className = "fondo";

            $("#progress3").find("li img").attr("src", error);
            $("#progress3").find("li img").fadeIn(1500);

            break;
        case "2":
            alert('Existen novedades sin validar, por favor verifique');
            document.getElementById('layer1').className = "fondo";

            $("#progress3").find("li img").attr("src", error);
            $("#progress3").find("li img").fadeIn(1500);
            $("#novedades").fadeIn();
            break;
        case "3":
            alert('No existen registros para el rango de fechas solicitadas');
            document.getElementById('layer1').className = "fondo";

            $("#progress3").find("li img").attr("src", error);
            $("#progress3").find("li img").fadeIn(1500);

            break;
        case "4":
            alert('Ocurrio un error en la actualización de los datos, informe al administrador');
            document.getElementById('layer1').className = "fondo";

            $("#progress3").find("li img").attr("src", error);
            $("#progress3").find("li img").fadeIn(1500);

            break;
    }


}

function paso4() {
    if (confirm("¿Esta seguro de cerrar el proceso de nomina ")) {
        $.ajax({
            type: "POST",
            async: false,
            url: "CGUNOServlet",
            data: {
                nomina: datosnomina.id
            }, success: function(data) {
                window.open(data.file);
                // or window.location.href = data.fileUrl;
            }, error: function(jqXHR, textStatus, errorThrown) {
                alert("Ocurrio un error inesperado, " + textStatus);
                $("#progress4").find("li img").attr("src", error);
                $("#progress4").find("li img").fadeIn(1500);
            }
        });
    }
}
