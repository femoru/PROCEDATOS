/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var perfilUsuario;

jQuery(document).ready(function($) {
    restriccionCoordinador();

    $("#dlg_detalle").dialog({
        width: 'auto',
        height: 'auto',
        autoOpen: false,
        modal: true,
        dialogClass: "no-close",
        open: function(event, ui) {

            var data = $("#dlg_detalle").data();
            usuario = data.selectRow;
            jQuery("#refresh_gridNov").hide();
            jQuery("#refresh_gridLbr").hide();

/////////Condiciones Grilla NOVEDADES

            //   jQuery("#gridNov").showCol('valor');
            jQuery("#gridNov").jqGrid('setGridWidth', '150%');
            jQuery("#gridNov").jqGrid('navButtonAdd', "#pagerNov", {
                caption: "",
                title: "Recargar Datos",
                buttonicon: "ui-icon-refresh",
                position: "last",
                id: "btn_nn1",
                onClickButton: function() {
                    jQuery("#gridNov").jqGrid('setGridParam', {
                        datatype: "json"
                    }).trigger("reloadGrid");
                }
            }).jqGrid('navButtonAdd', "#pagerNov", {
                caption: "",
                title: "Agregar nueva Novedad",
                buttonicon: "ui-icon-plus",
                position: "first",
                id: "btn_nn",
                onClickButton: function() {


                    $("#registrarNovedad").dialog("open");

                    $("#registrarNovedad #tr_auxiliar").hide();
                    $("#registrarNovedad #auxiliar").val(usuario);


                    $(".fecha").change(function(e) {
                        if (!fechaNomina(e.target)) {
                            $(e.target).focus().after("<span class='error'>Fecha: fuera del rango de nomina </span>");
                        } else {
                            $("#registrar").removeAttr("disabled");
                        }
                    });
                    $("#registrar").focus(function(e) {
//                        alert("antes");
                        for (var i = 0; i < $(".fecha:visible").length; i++) {
                            if (!fechaNomina($(".fecha:visible")[i])) {
                                $($(".fecha:visible")[i]).focus().after("<span class='error'>Fecha: fuera del rango de nomina</span>");
                                e.preventDefault();
                                $(e.target).attr("disabled", "disabled");
                                return false;

                            }
                        }
                    });
                    $("#registrar").click(function() {
                        if ($(".error").length === 0) {
                            $("#registrarNovedad").dialog("close");
                        }
                    });

                }
            });



            jQuery("#gridNov").jqGrid('setGridParam', {
                url: "NovedadesServlet?id=" + usuario + "&nomina=" + nomina,
                rowNum: 6,
                datatype: "json"
            }).trigger("reloadGrid");


/////////Condiciones Grilla LABORES

            $("#edit_gridLbr").click(function() {
                condDialogoLabores();
            });
            $("#add_gridLbr").click(function() {
                condDialogoLabores();
                $("#auxiliar").load("getUsuarioPerfil.htm?perfil=4", function() {
                    $("#auxiliar").val(usuario);
                });
                $("#labor").load("getLaboresUsuarios.htm?usuario=" + usuario);
            });

            jQuery("#gridLbr").jqGrid('navButtonAdd', "#pagerLbr", {
                caption: "",
                title: "Recargar Datos",
                buttonicon: "ui-icon-refresh",
                position: "last",
                id: "btn_nn",
                onClickButton: function() {
                    jQuery("#gridLbr").jqGrid('setGridParam', {
                        datatype: "json"
                    }).trigger("reloadGrid");
                }
            });
            jQuery("#gridLbr").jqGrid('setGridParam', {
                url: "RegistrosServlet?id=" + usuario + "&nomina=" + nomina,
                editurl: "RegistrosServlet?idusuario=" + usuario + "&nomina=" + nomina,
                rowNum: 10,
                datatype: "json"
            }).trigger("reloadGrid");

            restriccionCoordinador();
        }, close: function() {
            terminarMod(usuario, nomina);
            jQuery("#gridNom").jqGrid('setGridParam', {
                datatype: "json"
            }).trigger("reloadGrid");
        }, buttons: {
            "Terminar": function() {
                $(this).dialog("close");
            }
        }
    });


    var usuario = 0;
    var nomina = sessionStorage.getItem('nm');

    $("#gridNov").jqGrid('setGridHeight', '140');

    $("#gridNov").jqGrid('hideCol', "usuario");

    $("#gridLbr").jqGrid('setGridWidth', '100%');
    $("#gridLbr").jqGrid('setGridHeight', '240');
    $("#gridLbr").jqGrid('hideCol', "auxiliar");
    $("#gridLbr").jqGrid('showCol', "costo");
    $("#add_gridLbr").removeClass('ui-state-disabled');
    $("#edit_gridLbr").removeClass('ui-state-disabled');
    $("#view_gridLbr").removeClass('ui-state-disabled');


    jQuery("#gridNov").jqGrid('setGridParam', {
        url: "NovedadesServlet?id=" + usuario + "&nomina=" + nomina,
        datatype: "json",
        onSelectRow: function(id) {
            $("#btn_validar").removeClass('ui-state-disabled');
        }
    }).trigger("reloadGrid");
    jQuery("#gridLbr").jqGrid('setGridParam', {
        url: "RegistrosServlet?id=" + usuario + "&nomina=" + nomina,
        editurl: "RegistrosServlet?idusuario=" + usuario + "&nomina=" + nomina,
        datatype: "json"

    }).trigger("reloadGrid");


    function fechaNomina(e) {

        var fechaNom = e.value;
        var resp = false;

        var dato = $.ajax({
            type: "POST",
            async: false,
            url: jQuery('#gridNom').jqGrid("getGridParam", "url"),
            data: {
                oper: "consulta",
                idnomina: nomina,
                fecha: fechaNom
            }
        }).responseText;

        if (dato === "true") {
            resp = true;
        }
        return resp;
    }

    $("#btn_terminar").click(function() {
        terminarMod(0, nomina);

        if (confirm('¿Esta seguro que desea finalizar la revision?')) {
            $.ajax({
                type: 'POST',
                url: 'PrenominaServlet',
                data: {
                    oper: 'updNomina',
                    nomina: sessionStorage.getItem('nm'),
                    estado: parseInt(sessionStorage.getItem('st')) + 1
                }, success: function(data) {
                    if (data) {
                        sessionStorage.setItem('st', parseInt(sessionStorage.getItem('st')) + 1);
//                        window.location = 'Prenomina.htm';
                        loadNewPage('Prenomina.htm');
                    }
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    alert("Ocurrio un error inesperado, " + textStatus);
                }
            });
        }

    });
    function terminarMod(u, n) {
        $.ajax({
            url: "PrenominaServlet",
            type: "POST",
            async: false,
            data: {
                oper: "modificar",
                usuario: u,
                nomina: n
            },
            success: function(data) {
                console.log("complete");
            }, error: function(jqXHR, textStatus, errorThrown) {
                alert(jqXHR + "-" + textStatus + "-" + errorThrown);
            }
        });
    }

    function condDialogoLabores() {

        $("#fecha").blur(function(e) {
            if (!fechaNomina(e.target)) {
                $("#FormError").show().find("td").text('Fecha: fuera del rango de nomina');
            } else {
                $("#FormError").hide();
            }
        });
        $("#sData").mousedown(function() {

            if (!fechaNomina($("#fecha")[0])) {
                $("#FormError").show().find("td").text('Fecha: fuera del rango de nomina');
            }

            if ($("#FormError:visible").length !== 0) {
                alert("La fecha no esta dentro del rango de la nomina");
                $("#fecha").focus();
            }
        });
        $("#sData").click(function() {
            $("#sData").focus();
        });

        $("#sData").focusout(function() {
            $("#auxiliar").val(usuario);
        });

        $("#tr_auxiliar").hide();
    }

    $("#registrarNovedad").dialog({
        width: 'auto',
        autoOpen: false,
        modal: true,
        close: function() {
            $("#reset").click();
            jQuery("#gridNov").jqGrid('setGridParam', {
                datatype: "json"
            }).trigger("reloadGrid");
        }

    });


    $("#validar_Novedad").dialog({
        width: 'auto',
        autoOpen: false,
        modal: true,
        open: function() {
            $("#divimg").focus();
        },
        close: function() {

            jQuery("#gridNov").jqGrid('setGridParam', {
                datatype: "json"
            }).trigger("reloadGrid");

        }
    });
});

function restriccionCoordinador() {
    perfilUsuario = consultaPerfil();

    switch (perfilUsuario) {
        case '6': //RRHH            
            $('#del_gridLbr').hide();
            $('#add_gridLbr').hide();
            $('#edit_gridLbr').hide();
            break;

        case '7': //UENP         
            $('#btn_nn').hide();
            $('#btn_validar').hide();
            $('#del_gridNov').show();
            break;
        case '1':
            break;
        default:
            window.location = ('home.htm');
            break;
    }
}