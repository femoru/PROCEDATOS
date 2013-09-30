/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function($) {
    $("#tr_filtros").hide();
    if (consultaPerfil() === "1" || consultaPerfil() === "3") {
        $("#tr_filtros").show();
    }
    $("#consulta").click(function() {
        $("#dlgRegistros").dialog("open");
    });
    $("#grupo").load("getGrupoCoordinador.htm");
    $("#grupo").change(function(e) {
        jQuery("#gridLbr").jqGrid('setGridParam', {
            url: "RegistrosServlet?grupo=" + $(e.target).val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            editurl: "RegistrosServlet?grupo=" + $(e.target).val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            datatype: "json",
            pager: "#pagerLbr"
        });
        refrescarGrilla();
    });
    $("#vTodos").click(function() {
        if (confirm("Esta seguro que desea validar todas las labores")) {
            var confirmacion = $.ajax({
                type: "POST",
                async: false,
                url: "RegistrosServlet",
                data: {
                    oper: "todos",
                    filtro: rdb_flt,
                    grupo: $("#grupo").val(),
                    sitio: $("#sitio").val(),
                    dia: $("#dateIni").val()
                }

            }).responseText;
            if (confirmacion === "true") {
                alert("Se han validado las labores para la fecha " + $("#dateIni").val());
            } else {
                alert("Existen registros pendientes de validacion. \n Verifique por favor.");
            }
        }

        refrescarGrilla();
    });
    var rdb_filtro = $("input[name='rdio']").val();
    $("input[name='rdio']").change(function(e) {
        rdb_filtro = $(e.target).val();
        jQuery("#gridLbr").jqGrid('setGridParam', {
            url: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            editurl: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            datatype: "json",
            pager: "#pagerLbr"
        });
        refrescarGrilla();
    });
    var rdb_flt = $("input[name='flt']:checked").val();
    $("input[name='flt']").change(function(e) {
        rdb_flt = $(e.target).val();
        if (rdb_flt === "1") {
            $("#flt_grp").hide();
            $("#flt_sitio").show();
        } else {
            $("#flt_sitio").hide();
            $("#flt_grp").show();
        }
        jQuery("#gridLbr").jqGrid('setGridParam', {
            url: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            editurl: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
            datatype: "json",
            pager: "#pagerLbr"
        });
        refrescarGrilla();
    });
    $("#sitio").load("getSitioTrabajo.htm");
    $("#sitio").change(function(e) {
        jQuery("#gridLbr").jqGrid('setGridParam', {
            url: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $(e.target).val() + "&filtro=" + rdb_flt,
            editurl: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $(e.target).val() + "&filtro=" + rdb_flt,
            datatype: "json",
            pager: "#pagerLbr"
        });
        refrescarGrilla();
    });
    var resp;

    var datePickerGrid = function(element) {
        $(element).val($.datepicker.formatDate("dd/mm/yy", new Date()));
        $(element).datepicker({
            dateFormat: 'dd/mm/yy'
        }
        );
    };
    var now = new Date();
    $("#dateIni").val($.datepicker.formatDate("dd/mm/yy", now));
    $("#dateIni").datepicker({
        showOn: "both",
        maxDate: 0,
        dateFormat: 'dd/mm/yy',
        onSelect: function() {

            jQuery("#gridLbr").jqGrid('setGridParam', {
                url: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
                editurl: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
                datatype: "json",
                pager: "#pagerLbr"
            });
            refrescarGrilla();
        },
        beforeShowDay: function(date) {
            if (date > now) {
                return [false, '', "Sin registros"];
            }
            var validados = $.ajax({
                type: "POST",
                async: false,
                url: "RegistrosServlet",
                data: {
                    oper: "dia",
                    filtro: rdb_flt,
                    grupo: $("#grupo").val(),
                    sitio: $("#sitio").val(),
                    dia: $.datepicker.formatDate("dd/mm/yy", date)
                }

            }).responseText;
            if (validados === "1") {
                return [true, 'validados', "Todos los registros estan validados"];
            }
            if (validados === "0") {
                return [true, 'sinValidar', "Faltan registros por validar"];
            }
            return [true, '', "Sin registros"];
        }

    });
    function laborvalida(e) {
        var aux = $(e.target).val();
        var editUrl = jQuery('#gridLbr').jqGrid("getGridParam", "editurl");
        resp = $.ajax({
            type: "POST",
            async: false,
            url: editUrl,
            data: {
                oper: "valida",
                idlabor: aux
            }
        }).responseText;
        var myGrid = $('#gridLbr'),
                selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                rowData = myGrid.jqGrid('getRowData', selRowId);
        if (resp === "0") {
            $("#tr_dato").hide();
        } else {
            if (rowData.tipo === "HORAS") {
                $("#tr_dato").hide();
            } else {
                $("#tr_dato").show();
            }
        }
    }

    function validarLabores(idRow) {
        var myGrid = $('#gridLbr'),
                editUrl = myGrid.jqGrid("getGridParam", "editurl"),
                rowData = myGrid.jqGrid('getRowData', idRow);
        if (rowData.fin !== "") {
            var validar = false;
            if (rowData.valida === "1") {
                if (rowData.dato !== "") {
                    validar = confirm("Esta seguro que desea validar la labor " + rowData.labor + " para " + rowData.auxiliar);
                } else {
                    if (rowData.tipo === "HORAS") {
                        if ($("#gridLbr_" + rowData.id).length) {
                            validar = confirm("Esta seguro que desea validar la labor " + rowData.labor + " para " + rowData.auxiliar);
                        } else {
                            alert('Verifique el Dato labor');
                        }
                    } else {
                        alert('Verifique el Dato labor');
                    }
                }
            } else {
                validar = confirm("Esta seguro que desea validar la labor " + rowData.labor + " para " + rowData.auxiliar);
            }

            if (validar) {
                $.ajax({
                    type: "POST",
                    async: false,
                    url: editUrl,
                    data: {
                        oper: "row",
                        id: idRow,
                        est: 2
                    }
                });
                refrescarGrilla();
            }

        } else {
            alert('verifique la hora de finalización');
        }
    }

    function refrescarGrilla() {
//refresca validar labores
        jQuery("#gridLbr").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridLbr").trigger("reloadGrid");
        //refresca novedades
        jQuery("#gridJust").jqGrid('setGridParam', {
            url: "UsuarioServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val(),
            datatype: "json"
        });
        jQuery("#gridJust").trigger("reloadGrid");
        //refresca ausentes
        var gr = $("#grupo").val();
        var today = $.datepicker.formatDate('dd/mm/yy', new Date());
        if (today !== $("#dateIni").val()) {
            gr = 0;
        }
        jQuery("#gridAux").jqGrid('setGridParam', {
            url: "UsuarioServlet?grupo=" + gr,
            datatype: "json"
        });
        jQuery("#gridAux").trigger("reloadGrid");
        return [true, ""];
    }

    function validacionHora() {
        if ($("#fechas").val() === "") {
            return[false, "Seleccione primero una fecha"];
        }
        if ($("#inicio").val() === "") {
            return[false, "Seleccione la hora de inicio de la labor"];
        }
        if ($("#fin").val() === "") {
            return[false, "Seleccione la hora de fin de la labor"];
        }
        var checkInicio = checkHoras($("#inicio").val());
        var checkFin = checkHoras($("#fin").val());
        if (!checkInicio[0]) {
            return checkInicio;
        }
        if (!checkFin[0]) {
            return checkFin;
        }

        var now = $("#fechas").datepicker('getDate');
        var ini = now.setHours($("#inicio").val().split(':')[0], $("#inicio").val().split(':')[1]);
        var fin = now.setHours($("#fin").val().split(':')[0], $("#fin").val().split(':')[1]);
        if (ini > fin) {
            return[false, "La hora de inicio debe ser menor que la hora de salida"];
        }
        var editUrl = jQuery('#gridLbr').jqGrid("getGridParam", "editurl");
        var idregistro = $('#gridLbr').jqGrid('getGridParam', 'selrow');
        var condicionHora = $.ajax({
            type: "POST",
            async: false,
            url: editUrl,
            data: {
                oper: "horaValidar",
                id: idregistro,
                idusuario: $("#auxiliar").val(),
                fechas: $("#fechas").val(),
                inicio: $("#inicio").val(),
                fin: $("#fin").val()
            }
        }).responseText;
        if (condicionHora === "1") {
            return [false, "La hora de inicio se cruza con una labor existente"];
        }
        if (condicionHora === "2") {
            return [false, "La hora de fin se cruza con una labor existente"];
        }
        if (condicionHora === "3") {
            return [false, "La hora de fin no debe ser mayor a hora actual"];
        }
        if (condicionHora === "4") {
            return [false, "Existe una labor anterior sin registrar salida"];
        }
        return true;
    }


    jQuery('#gridLbr').jqGrid(
            {
                height: 350,
                width: $('#grilla').width(),
                ignoreCase: true,
                caption: "Registro de Labores",
                url: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
                editurl: "RegistrosServlet?grupo=" + $("#grupo").val() + "&fecha=" + $("#dateIni").val() + "&estado=" + rdb_filtro + "&sitio=" + $("#sitio").val() + "&filtro=" + rdb_flt,
                loadonce: true,
                rowNum: 15,
                shrinkToFit: false,
                multiSort: true,
                subGrid: true,
                prmNames: {deloper: "cancelar"},
                rownumbers: true,
                datatype: "json",
                colNames: ["id", "idlaborcontrato", "idusuario", "Auxiliar", "Labor", "Descripcion Extra", "Tipo", "Fecha",
                    "Hora Inicio", "Hora Fin", "Tiempo", "Registros", "Costo", "Observación", "Dato Labor", "datovalida", "Anulado"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "idlaborcontrato",
                        "index": "idlaborcontrato",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "idusuario",
                        "index": "idusuario",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "auxiliar",
                        "index": "auxiliar",
                        editable: true,
                        edittype: "select",
                        width: 150
                    },
                    {
                        "name": "labor",
                        "index": "labor",
                        editable: true,
                        edittype: "select",
                        width: 300
                    },
                    {
                        name: "extra",
                        index: "extra",
                        editable: false,
                        edittype: "text",
                        width: 70
                    },
                    {
                        "name": "tipo",
                        "index": "tipo",
                        editable: false,
                        align: 'center',
                        width: 60
                    },
                    {
                        name: "fechas",
                        index: "fechas",
                        editable: true,
                        edittype: "text",
                        align: 'center',
                        editrules: {
                            date: true,
                            required: true},
                        editoptions: {
                            dataInit: function(element) {
                                $(element).datepicker({dateFormat: "dd/mm/yy", maxDate: 0}
                                );
                            }
                        },
                        datefmt: "dd/mm/yyyy",
                        width: 65
                    },
                    {
                        "name": "inicio",
                        "index": "inicio",
                        editable: true,
                        edittype: "text",
                        editoptions: {
                            maxlength: 5
                        },
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: checkHoras
                        },
                        align: 'center',
                        width: 35
                    },
                    {
                        "name": "fin",
                        "index": "fin",
                        editable: true,
                        edittype: "text",
                        editoptions: {
                            maxlength: 5
                        },
                        editrules: {
                            required: true,
                            custom: true,
                            custom_func: checkHoras
                        },
                        align: 'center',
                        width: 35
                    },
                    {
                        "name": "tiempo",
                        "index": "tiempo",
                        editable: false,
                        edittype: "text",
                        align: 'center',
                        width: 50
                    },
                    {
                        "name": "registros",
                        "index": "registros",
                        editable: true,
                        edittype: "text",
                        align: 'center',
                        width: 60
                    },
                    {
                        "name": "costo",
                        "index": "costo",
                        editable: false,
                        edittype: "text",
                        hidden: true,
                        width: 80
                    },
                    {
                        "name": "observacion",
                        "index": "observacion",
                        editable: true,
                        edittype: "text",
                        editrules: {
                            edithidden: true
                        },
                        hidden: true
                    },
                    {
                        "name": "dato",
                        "index": "dato",
                        editable: true,
                        edittype: "text",
                        width: 80
                    },
                    {
                        "name": "valida",
                        "index": "valida",
                        editable: true,
                        edittype: "text",
                        width: 80,
                        hidden: true
                    },
                    {
                        "name": "anulacion",
                        "index": "anulacion",
                        editable: false,
                        edittype: "text"
                    }
                ],
                subGridRowExpanded: function(subgrid_id, row_id) {
// we pass two parameters
// subgrid_id is a id of the div tag created whitin a table data
// the id of this elemenet is a combination of the "sg_" + id of the row
// the row_id is the id of the row
// If we wan to pass additinal parameters to the url we can use
// a method getRowData(row_id) - which returns associative array in type name-value
// here we can easy construct the flowing
                    var subgrid_table_id, pager_id;
                    subgrid_table_id = subgrid_id + "_t";
                    pager_id = "p_" + subgrid_table_id;
                    $("#" + subgrid_id).html("<table id='" + subgrid_table_id + "' class='scroll'></table><div id='" + pager_id + "' class='scroll'></div>");
                    jQuery("#" + subgrid_table_id).jqGrid({
                        url: "RegistrosServlet?id=" + row_id,
                        editurl: "RegistrosServlet?reg=" + row_id,
                        datatype: "json",
                        loadonce: true,
                        prmNames: {editoper: "linea", addoper: "jnsertardato"},
                        rowNum: 4,
                        rownumbers: true,
                        gridview: true,
                        pager: pager_id,
                        height: '100%',
                        colNames: ['Registro', 'Dato Labor'],
                        colModel: [
                            {
                                name: "registro",
                                index: "registro",
                                align: "center",
                                hidden: true
                            }, {
                                name: "dato",
                                index: "dato",
                                editable: true,
                                align: "center",
                                width: 250
                            }
                        ]

                    });
                    jQuery("#" + subgrid_table_id).jqGrid('navGrid', "#" + pager_id, {
                        edit: true,
                        add: true,
                        del: false},
                    {afterSubmit: refrescarGrilla,
                        closeAfterEdit: true},
                    {afterSubmit: refrescarGrilla,
                        closeAfterAdd: true}
                    );
                },
                subGridRowColapsed: function(subgrid_id, row_id) {
//// this function is called before removing the data
                    var subgrid_table_id;
                    subgrid_table_id = subgrid_id + "_t";
                    jQuery("#" + subgrid_table_id).remove();
                },
                ondblClickRow: function(id) {
                    if (rdb_filtro === "0") {
                        validarLabores(id);
                    }
                },
                loadComplete: function(data) {
                    setTimeout(function() {
                        if (data.rows) {
                            for (var i = 0; i < data.rows.length; i++) {
                                if (data.rows[i].tipo !== "HORAS") {
                                    $("#" + data.rows[i].id).find("td.ui-sgcollapsed").removeClass("ui-sgcollapsed sgcollapsed").find("span").removeClass("ui-icon ui-icon-plus");
                                } else {
                                    if (data.rows[i].valida === "0") {
                                        $("#" + data.rows[i].id).find("td.ui-sgcollapsed").removeClass("ui-sgcollapsed sgcollapsed").find("span").removeClass("ui-icon ui-icon-plus");
                                    }
                                }
                            }
                        }
                    }, 1);
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {

                            $('#gridLbr').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }, 1);
                    }
                },
                postData: {
                    oper: "oper"
                },
                loadError: function(xhr, status, err) {
                    try {
                        jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                                '<div class="ui-state-error">' + xhr.responseText + err + 'Val</div>',
                                jQuery.jgrid.edit.bClose,
                                {
                                    buttonalign: 'right'
                                });
                    } catch (e) {
                        alert(xhr.responseText);
                    }
                },
                pager: "#pagerLbr"
            });
    jQuery("#gridLbr").jqGrid('filterToolbar', {searchOnEnter: false, defaultSearch: 'cn'});
    jQuery("#gridLbr").jqGrid('bindKeys');
    jQuery('#gridLbr').jqGrid('navGrid', '#pagerLbr',
            {
                edit: true,
                edittitle: "Modificar labor seleccionada",
                add: true,
                addtitle: "Agregar nueva labor",
                view: true,
                viewtitle: "Consultar datos de la labor seleccionada",
                search: true,
                del: true,
                deltitle: "Anular labor seleccionada",
                refresh: true,
                beforeRefresh: refrescarGrilla

            },
    {//Edit
        beforeInitData: function() {
            jQuery('#gridLbr').setColProp('auxiliar', {
                editable: true,
                edittype: "text",
                editoptions: {
                    readonly: 'readonly'
                }
            });
            jQuery('#gridLbr').setColProp('labor', {
                editable: true,
                edittype: "select"
            });
            jQuery('#gridLbr').setColProp('inicio', {
                editable: true
            });
            jQuery('#gridLbr').setColProp('fin', {
                editable: true
            });
            jQuery('#gridLbr').setColProp('observacion', {
                editable: true
            });
            jQuery('#gridLbr').setColProp('dato', {
                editable: true
            });
        },
        onInitializeForm: function() {
            resp = 0;
            $("#tr_validar").hide();
            $("#tr_labor > td.DataTD > select").attr("name", "labor");
            $("#tr_labor > td.DataTD > select").attr("id", "labor");
            $("#labor").load("getLaboresUsuarios.htm?usuario=" + $("#idusuario").val(), function() {
                var myGrid = $('#gridLbr'),
                        selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                        rowData = myGrid.jqGrid('getRowData', selRowId);
                $("#labor option[value=" + rowData.idlaborcontrato + "]").attr("selected", true);
                if (rowData.valida === "0") {
                    $("#tr_dato").hide();
                } else {
                    $("#tr_dato").show();
                }
                if (rowData.tipo !== "REGISTROS") {
                    $("#tr_registros").hide();
                } else {
                    $("#tr_registros").show();
                }
            });
            $("#labor").change(function(e) {
                laborvalida(e);
                if ($("#labor :selected").text().indexOf("REGISTROS") < 0) {
                    $("#tr_registros").hide();
                } else {
                    $("#tr_registros").show();
                }
            });
            $("#labor").show();
            $("#inicio, #fin").change(function() {
                var fecha = validacionHora();
                if (fecha.length > 0) {
                    $("#FormError").show().find("td").text(fecha[1]);
                } else {
                    $("#FormError").hide();
                }
            });
        },
        beforeSubmit: function(postdata, formid) {

            if ($("#auxiliar").val() === "0") {
                return[false, "Seleccione un auxiliar"];
            }
            if ($("#labor").val() === "0") {
                return[false, "Seleccione una labor"];
            }

            var fecha = validacionHora();
            if (fecha.length > 0) {
                return fecha;
            }

            if ($("#dato").is(":visible") && postdata.dato === "") {
                return [false, "Es necesario ingresar el dato labor"];
            }
            if ($("#labor :selected").text().indexOf("REGISTROS") > 0) {
                if ($("#registros").val() === "")
                    return [false, "Es necesario ingresar el numero de registros"];
            }
            return[true, ""];
        },
        width: 'auto',
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        closeAfterEdit: true,
        editCaption: "Modificar labor",
        recreateForm: true
    },
    {//Add
        beforeInitData: function(formid) {
            jQuery('#gridLbr').setColProp('auxiliar', {
                editable: true,
                edittype: "select",
                editoptions: {
                    dataUrl: "getUsuariosSitio.htm?sitio=" + $("#sitio").val(),
                    dataEvents: [{
                            type: 'change',
                            fn: function(e) {
                                var aux = $(e.target).val();
                                $("#labor").load("getLaboresUsuarios.htm?usuario=" + aux);
                                $("#tr_labor").show();
                            }
                        }]
                }
            });
            jQuery('#gridLbr').setColProp('labor', {
                editable: true,
                edittype: "select"
            });
            jQuery('#gridLbr').setColProp('inicio', {
                editable: true
            });
            jQuery('#gridLbr').setColProp('fin', {
                editable: true
            });
            jQuery('#gridLbr').setColProp('observacion', {
                editable: true
            });
        },
        afterShowForm: function(formid) {
            resp = 0;
            $("#tr_dato").hide();
            $("#tr_registros").hide();
            $("#tr_labor > td.DataTD > select").load("getLaboresUsuarios.htm?usuario=0");
            $("#tr_labor > td.DataTD > select").attr("name", "labor");
            $("#tr_labor > td.DataTD > select").attr("id", "labor");
            $("#tr_validar").hide();
            $("#labor").change(function(e) {
                laborvalida(e);
                if ($("#labor :selected").text().indexOf("REGISTROS") < 0) {
                    $("#tr_registros").hide();
                } else {
                    $("#tr_registros").show();
                }
            });
            $("#inicio, #fin").change(function() {
                var fecha = validacionHora();
                if (fecha.length > 0) {
                    $("#FormError").show().find("td").text(fecha[1]);
                } else {
                    $("#FormError").hide();
                }
            });
        },
        beforeSubmit: function(postdata, formid) {

            if ($("#auxiliar").val() === "0") {
                return[false, "Seleccione un auxiliar"];
            }
            if ($("#labor").val() === "0") {
                return[false, "Seleccione una labor"];
            }

            var fecha = validacionHora();
            if (fecha.length > 0) {
                return fecha;
            }
            if ($("#dato").is(":visible") && postdata.dato === "") {
                return [false, "Es necesario ingresar el dato de la labor"];
            }
            if ($("#labor :selected").text().indexOf("REGISTROS") > 0) {
                if ($("#registros").val() === "")
                    return [false, "Es necesario ingresar el numero de registros"];
            }
            return[true, ""];
        },
        width: 'auto',
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        bottominfo: "Todos los campos(*) son obligatorios",
        addCaption: "Agregar labor",
        recreateForm: true
    },
    {
        beforeInitData: function(formid) {

            var html = "<label>Causa: </label><select id=\"anulacion\"></select>";
            $($(".DelTable").find("td")[3]).html(html);
            $("#anulacion").load("getAnulaciones.htm");
        },
        onclickSubmit: function(options) {
            options.delData.anulado = $("#anulacion").val();
        },
        beforeSubmit: function() {

            if ($("#anulacion").val() === "-1") {
                return [false, "Seleccione una causa de anulacion para continuar"];
            }
            return [true, ""];
        },
        afterSubmit: refrescarGrilla,
        caption: "Anular",
        msg: "",
        bSubmit: "Anular",
        closeAfterDel: true
    },
    {
        multipleSearch: false
    },
    {
        caption: "Consultar datos de la labor",
        reloadAfterSubmit: true,
        width: '620'
    }
    );
});

function checkHoras(value) {
    var patt = /^(([0-9]|0[0-9])|1[0-9]|2[0-3]):([0-5][0-9])?$/;
    var resp = patt.test(value);
    if (!resp) {
        return [resp, value + " No corresponde a un formato de hora valido, ingrese HH:MM"];
    } else {
        return [resp, ""];
    }
}