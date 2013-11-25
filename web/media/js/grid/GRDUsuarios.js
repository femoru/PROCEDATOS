/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var myGrid = $('#grid1'),
        identificacion = $('#identificacion'),
        pnombre = $('#pnombre'),
        snombre = $('#snombre'),
        papellido = $('#papellido'),
        sapellido = $('#sapellido'),
        fechaNac = $('#fechaNac'),
        sexo = $('#sexo'),
        estadocivil = $('#estadocivil'),
        /**/personales = $([]).add(identificacion).add(pnombre).add(snombre).add(papellido).add(sapellido).add(fechaNac).add(sexo).add(estadocivil),
        direccion = $('#direccion'),
        email = $('#email'),
        telefono = $('#telefono'),
        celular = $('#celular'),
        /**/contacto = $([]).add(direccion).add(email).add(telefono).add(celular),
        codPerfil = $("#codPerfil"),
        sitio = $("#sitio"),
        horario = $("#horario"),
        estado = $("#estado"),
        usuariocliente = $("#usuariocliente"),
        fechaIngreso = $("#fechaIngreso"),
        fechaRetiro = $("#fechaRetiro"),
        salario = $("#salario"),
        /**/contrato = $([]).add(codPerfil).add(sitio).add(horario).add(estado).add(usuariocliente).add(fechaIngreso).add(fechaRetiro).add(salario);

var cerrar = {text: "Cancelar", click: function() {
        $(this).dialog("close");
    },
    icons: {
        primary: "ui-icon-close"
    }
};
jQuery(document).ready(function($)
{
    $("#users").dialog({
        width: "60%",
        modal: true,
        autoOpen: false,
        buttons: {
            "Cancelar": function() {
                $(this).dialog("close");
            }
        },
        close: function() {
            personales.removeAttr("disabled");
            personales.val("");
            contacto.removeAttr("disabled");
            contacto.val("");
            contrato.removeAttr("disabled");
            contrato.val("");
        }
    }).accordion({
        collapsible: false,
        icons: null,
        heightStyle: "content"
    });
    $(".datepicker").datepicker({
        dateFormat: 'dd/mm/yy',
        changeYear: true,
        yearRange: "c-70:c",
        changeMonth: true
    });
    $('#estadocivil').load('getEstadoCivil.htm');
    $('#sitio').load('getSitioTrabajo.htm');
    $('#codPerfil').load('getPerfiles.htm');
    $('#identificacion').blur(comprobarCedula);
    var lastSel;
    myGrid.jqGrid({
        ignoreCase: true,
        caption: "Usuarios Registrados",
        url: "PersonaServlet",
        editurl: "PersonaServlet",
        loadonce: true,
        rowNum: 15,
        height: 350,
        sortname: "nombres",
        rownumbers: true,
        datatype: "json",
        colNames: ["Id\t(*)", "Identificación\t(*)", "Primer Nombre\t(*)", "Segundo Nombre", "Primer Apellido\t(*)", "Segundo Apellido",
            "Fecha Nacimiento\t(*)", "Sexo\t(*)", "Direccion\t(*)", "Telefono\t(*)", "Celular\t(*)", "Correo\t(*)", "Estado Civil\t(*)", "Fecha Ingreso", "Fecha Retiro", "Nombres",
            "Perfil\t(*)", "Turno(*)", "Estado\t(*)", "Salario", "Usuario Cliente", "Contraseña", "Sitio de Trabajo"],
        colModel: [
            {
                "name": "idpersona",
                index: "idpersona",
                editable: false,
                edittype: "text",
                hidden: true
            },
            {
                "name": "identificacion",
                index: "identificacion",
                editable: false,
                edittype: 'text',
                width: 90
            },
            {
                "name": "pnombre",
                index: "pnombre",
                editable: false,
                edittype: "text",
                hidden: true
            },
            {
                "name": "snombre",
                index: "snombre",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "papellido",
                index: "papellido",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "sapellido",
                index: "sapellido",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "fechaNac",
                index: "fechaNac",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "sexo",
                index: "sexo",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "direccion",
                index: "direccion",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "telefono",
                index: "telefono",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "celular",
                index: "celular",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "email",
                index: "email",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "estadocivil",
                index: "estadocivil",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                name: "fechaIngreso",
                index: "fechaIngreso",
                editable: true,
                edittype: "text",
                hidden: true

            }, {
                name: "fechaRetiro",
                index: "fechaRetiro",
                editable: true,
                edittype: "text",
                hidden: true

            },
            {
                "name": "nombres",
                index: "nombres",
                editable: false,
                viewable: false,
                edittype: "text",
                width: 300
            },
            {
                "name": "codPerfil",
                index: "codPerfil",
                editable: false,
                edittype: "text"
            },{
                "name": "horario",
                index: "horario",
                editable: false,
                edittype: "text",
                formatter: "select",
                stype: "select",
                searchoptions: {
                    defaultValue: 0,
                    value: {
                        0: "Diurno",
                        1: "Nocturno"
                    }
                },
                editoptions: {
                    value: {
                        0: "Diurno",
                        1: "Nocturno"

                    }
                }
            },
            {
                "name": "estado",
                index: "estado",
                editable: true,
                align: "center",
                edittype: "select",
                formatter: "select",
                stype: "select",
                searchoptions: {
                    defaultValue: 1,
                    value: {
                        1: "Activo",
                        0: "Inactivo"

                    }
                },
                editoptions: {
                    value: {
                        1: "Activo",
                        0: "Inactivo"

                    },
                    dataEvents: [{
                            type: 'change',
                            fn: function(e) {
                                var editUrl = myGrid.jqGrid("getGridParam", "editurl");
                                var est = $(e.target).val();
                                var selRow = myGrid.jqGrid('getGridParam', 'selrow');
                                $.ajax({
                                    type: "POST",
                                    async: false,
                                    url: editUrl,
                                    data: {
                                        id: selRow,
                                        oper: "inline",
                                        estado: est,
                                        sitio: $("#" + selRow + "_sitio").val()
                                    },
                                    success: refrescarGrilla
                                });
                            }
                        }]
                }, width: 100
            },
            {
                name: "salario",
                index: "salario",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                name: "usuariocliente",
                index: "usuariocliente",
                editable: true,
                edittype: "text",
                hidden: true
            },
            {
                "name": "reestcontraseña",
                index: "reestcontraseña",
                editable: true,
                edittype: "button",
                align: "center",
                search: false,
                viewable: false,
                editoptions: {
                    value: "Reestablecer",
                    dataEvents: [{
                            type: 'click',
                            fn: reestablecerContrasena
                        }]
                }
                , width: 100
            }, {
                "name": "sitio",
                index: "sitio",
                editable: true,
                align: "center",
                edittype: "select",
                editoptions: {
                    dataUrl: "getSitioTrabajo.htm?grid=true",
                    dataEvents: [{
                            type: 'change',
                            fn: function(e) {
                                var editUrl = myGrid.jqGrid("getGridParam", "editurl"),
                                        site = $(e.target).val();
                                var selRow = myGrid.jqGrid('getGridParam', 'selrow');
                                $.ajax({
                                    type: "POST",
                                    async: false,
                                    url: editUrl,
                                    data: {
                                        id: selRow,
                                        oper: "inline",
                                        estado: $("#" + selRow + "_estado").val(),
                                        sitio: site
                                    },
                                    success: refrescarGrilla
                                });
                            }
                        }]
                }
            }
        ],
        onSelectRow: function(id) {
            myGrid.restoreRow(lastSel);
        },
        ondblClickRow: function(id) {

            if (id && id !== lastSel) {
                myGrid.restoreRow(lastSel);
                lastSel = id;
            }
            myGrid.editRow(id, true);
        },
        loadError: function(xhr, status, err)
        {
            try

            {
                jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                        '<div class="ui-state-error">' + xhr.responseText + err + '</div>',
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
        pager: "#pager1"
    });
    myGrid.jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn'});
    myGrid.jqGrid('bindKeys');
    myGrid.jqGrid('navGrid', '#pager1', {
        add: false,
        edit: false,
        search: true,
        searchtitle: "Buscar usuario",
        del: false,
        refresh: true,
        beforeRefresh: refrescarGrilla

    });
    myGrid.jqGrid('navButtonAdd', "#pager1", {
        caption: "",
        title: "Consultar datos de usuario",
        buttonicon: "ui-icon-document",
        position: "first",
        onClickButton: function() {
            personales.attr("disabled", "");
            contacto.attr("disabled", "");
            contrato.attr("disabled", "");
            if (llenarCampos()) {
                $("#users").dialog("option", "title", "Consultar datos de usuario").dialog("open");
                $("#users").dialog("option", "buttons", [cerrar]);
            } else {
                $.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, 'Seleccione una fila');
            }
        }

    });
    myGrid.jqGrid('navButtonAdd', "#pager1", {
        caption: "",
        title: "Modificar usuario",
        buttonicon: "ui-icon-pencil",
        position: "first",
        onClickButton: function() {
            personales.removeAttr("disabled");
            if (llenarCampos()) {
                $("#users").dialog("option", "title", "Modificar usuario").dialog("open");
                $("#users").dialog("option", "buttons",
                        [
                            {text: "Guardar",
                                click: function() {
                                    if (guardar('edit')) {
                                        refrescarGrilla();
                                        $(this).dialog("close");
                                    } else {
                                        $.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, 'Faltan datos por ingresar, por favor verifique');
                                    }
                                }, icons: {
                                    primary: "ui-icon-disk"
                                }
                            }, cerrar
                        ]
                        );
            } else {
                $.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, 'Seleccione una fila');
            }

        }

    });
    myGrid.jqGrid('navButtonAdd', "#pager1", {
        caption: "",
        title: "Agregar Usuario",
        buttonicon: "ui-icon-plus",
        position: "first",
        onClickButton: function() {
            personales.removeAttr("disabled");
            $("#users").dialog("option", "buttons",
                    [
                        {text: "Guardar",
                            click: function() {
                                if (guardar('add')) {
                                    refrescarGrilla();
                                    $(this).dialog("close");
                                } else {
                                    $.jgrid.info_dialog(jQuery.jgrid.nav.alertcap, 'Faltan datos por ingresar, por favor verifique');
                                }
                            }, icons: {primary: "ui-icon-disk"}
                        }, cerrar
                    ]
                    );
            $("#users").dialog("option", "title", "Agregar Usuario").dialog("open");
        }

    });
});
function comprobarCedula() {
    var ident = $(this);
    if (ident.val() !== '') {
        $.ajax({
            type: "POST",
            url: "PersonaServlet",
            data: "oper=" + 'c' + "&identificacion=" + ident.val(),
            success: function(result) {
                if (result === 'true') {
                    alert('Ya existe un registro con esta cedula');
                }
            }
        });
    }
}

function reestablecerContrasena() {

    var selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
            rowData = myGrid.jqGrid('getRowData', selRowId);
    $.ajax({
        type: "POST",
        url: "PersonaServlet",
        async: true,
        data: "oper=" + 'r' + "&idpersona=" + rowData.idpersona,
        success: function() {
            alert('Se reestablecio la contraseña');
        }
    });
}

function refrescarGrilla() {
    myGrid.jqGrid('setGridParam', {
        datatype: "json"
    });
    myGrid.trigger("reloadGrid");
    return [true, ""];
}

function llenarCampos() {
    myGrid.restoreRow(lastSel);
    var selRow = myGrid.jqGrid("getGridParam", "selrow"), rd = myGrid.jqGrid('getRowData', selRow);
    if (selRow) {
        identificacion.val(rd.identificacion);
        pnombre.val(rd.pnombre);
        snombre.val(rd.snombre);
        papellido.val(rd.papellido);
        sapellido.val(rd.sapellido);
        fechaNac.val(rd.fechaNac);
        sexo.val(rd.sexo);
        estadocivil.find('option:contains("' + rd.estadocivil + '")').prop("selected", true);
        direccion.val(rd.direccion);
        email.val(rd.email);
        telefono.val(rd.telefono);
        celular.val(rd.celular);
        codPerfil.find('option:contains("' + rd.codPerfil + '")').prop("selected", true);
        sitio.find('option:contains("' + rd.sitio + '")').prop("selected", true);
        horario.val(rd.horario);
        estado.val(rd.estado);
        usuariocliente.val(rd.usuariocliente);
        fechaIngreso.val(rd.fechaIngreso);
        fechaRetiro.val(rd.fechaRetiro);
        salario.val(rd.salario);
        return true;
    } else {
        return false;
    }
}
function guardar(oper) {
    var guarda = false;
    if (validar())
        $.ajax({
            type: "POST",
            url: "PersonaServlet",
            async: false,
            data: {
                oper: oper,
                id: myGrid.jqGrid('getGridParam', 'selrow'),
                identificacion: identificacion.val(),
                pnombre: pnombre.val(),
                snombre: snombre.val(),
                papellido: papellido.val(),
                sapellido: sapellido.val(),
                fechaNac: fechaNac.val(),
                sexo: sexo.val(),
                estadocivil: estadocivil.val(),
                direccion: direccion.val(),
                email: email.val(),
                telefono: telefono.val(),
                celular: celular.val(),
                codPerfil: codPerfil.val(),
                sitio: sitio.val(),
                horario: horario.val(),
                estado: estado.val(),
                usuariocliente: usuariocliente.val(),
                fechaIngreso: fechaIngreso.val(),
                fechaRetiro: fechaRetiro.val(),
                salario: salario.val()
            },
            success: function(data) {
                guarda = true;
            }


        });
    return guarda;
}
function validar() {
    var validado = true;
    $(':input').removeClass('ui-state-error');
    for (var i = 0; i < $(".requerido").length; i++) {
        if ($(".requerido")[i].value === "") {
            $($(".requerido")[i]).addClass('ui-state-error');
            validado = false;
        }
    }

    for (var i = 0; i < $(".numero").length; i++) {
        if (isNaN($(".numero")[i].value)) {
            $($(".numero")[i]).addClass('ui-state-error');
            validado = false;
        }
    }
    if (estadocivil.val() === '0') {
        estadocivil.addClass('ui-state-error');
        validado = false;
    }
    if (codPerfil.val() === '0') {
        codPerfil.addClass('ui-state-error');
        validado = false;
    }
    return validado;
}