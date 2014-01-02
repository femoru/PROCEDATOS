/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
var lastSel, lastSelE;
var newRowID = 'new';
var idusuario = 0;
var vigencia, vigenciaE;
var mes;
var myGrid = $('#gridPlanilla');
var extraGrid = $('#gridPlanillaExtra');
var templateHoras = {
    editable: true,
    editrules: {
        required: true,
        time: true,
        custom: true,
        custom_func: checkHoras
    },
    editoptions: {
        maxlength: 4
    }

};
var templateHorasE = {
    editable: true,
    editrules: {
        required: true,
        time: true,
        custom: true,
        custom_func: checkHorasE
    },
    editoptions: {
        maxlength: 4
    }

};
$(document).ready(function() {
    cargarMeses();
    $("#cedula").keydown(function(e) {
        if (e.keyCode === $.ui.keyCode.ENTER || e.keyCode === $.ui.keyCode.TAB) {
            cargarCedula();
            myGrid.jqGrid('clearGridData');
            extraGrid.jqGrid('clearGridData');
        }
    });
    $('#labor').change(function(event) {
        var idlabor = event.target.value;
        vigencia = cargarVigencias(idlabor);
        mes = $('#mes').monthpicker('getDate');
        if (mes >= vigencia.inicial && mes < vigencia.final)
            recargarGrilla();
        else {
            setTimeout(recargarGrilla(), 100);
            alert("El mes seleccionado no esta dentro del rango de vigencia");
            myGrid.restoreRow(lastSel);
        }

    });
    $('#laborE').change(function(event) {
        var idlabor = event.target.value;
        vigenciaE = cargarVigencias(idlabor);
        mes = $('#mes').monthpicker('getDate');
        if (mes >= vigenciaE.inicial && mes < vigenciaE.final)
            recargarGrillaExtra();
        else {
            setTimeout(recargarGrillaExtra(), 100);
            alert("El mes seleccionado no esta dentro del rango de vigencia");
            extraGrid.restoreRow(lastSelE);
        }

    });
    $("#guardar").click(function() {
        loadNewPage('Planillas.htm');
    });
    $("#validar").click(function(e) {
        if (camposObligatorios()) {
            if (confirm("Se van a validar los registros, ¿Desea continuar?")) {
                $.ajax({
                    type: "POST",
                    async: false,
                    url: "PlanillaServlet",
                    data: {
                        oper: 'validar',
                        mes: $.datepicker.formatDate('mm/yy', $('#mes').monthpicker('getDate')),
                        idpersona: idusuario,
                        labor: $("#labor").val()
                    },
                    success: function(data) {
                        alert(data[0]);
                    }
                });
                recargarGrilla();
            }
        }
    });
    $("#validarE").click(function(e) {
        if (camposObligatorios()) {
            if (confirm("Se van a validar los registros, ¿Desea continuar?")) {
                $.ajax({
                    type: "POST",
                    async: false,
                    url: "PlanillaServlet",
                    data: {
                        oper: 'validar',
                        mes: $.datepicker.formatDate('mm/yy', $('#mes').monthpicker('getDate')),
                        idpersona: idusuario,
                        labor: $("#laborE").val()
                    },
                    success: function(data) {
                        alert(data[0]);
                    }
                });
                recargarGrillaExtra();
            }
        }
    });
    myGrid.jqGrid({
        autowidth: true,
        ignoreCase: true,
        url: 'PlanillaServlet',
        editurl: 'PlanillaServlet',
        datatype: "local",
        rowNum: 15,
        viewrecords: true,
        loadonce: true,
        colNames: ['Dia', 'H/Inicio D', 'H/Desc D', 'T/Diurno', 'H/Reinicio D', 'H/Final D', 'T/Tarde', 'Registros'],
        colModel: [
            {
                index: 'dia',
                name: 'dia',
                editable: true,
                editrules: {
                    required: true,
                    number: true,
                    minValue: 1,
                    maxValue: 31,
                    custom: true,
                    custom_func: checkDia
                },
                editoptions: {
                    maxlength: 2
                }

            }, {
                index: 'hInicioD',
                name: 'hInicioD',
                template: templateHoras

            }, {index: 'hDescD',
                name: 'hDescD',
                template: templateHoras

            }, {
                index: 'tiempoDiurno',
                name: 'tiempoDiurno',
                align: 'center'

            }, {
                index: 'hReinicioD',
                name: 'hReinicioD',
                template: templateHoras

            }, {
                index: 'hFinalD',
                name: 'hFinalD',
                template: templateHoras

            }, {
                index: 'tiempoTarde',
                name: 'tiempoTarde',
                align: 'center'

            }, {
                index: 'registros',
                name: 'registros',
                editable: true,
                editrules: {
                    required: true,
                    number: true
                },
                editoptions: {
                    defaultValue: "0"
                }
            }],
        ondblClickRow: function(id) {
            if (id && id !== lastSel) {
                myGrid.restoreRow(lastSel);
                lastSel = id;
            }
            myGrid.jqGrid('editRow', id, {
                keys: true,
                successfunc: recargarGrilla,
                extraparam: {
                    mes: $.datepicker.formatDate('/mm/yy ', $('#mes').monthpicker('getDate')),
                    idusuario: idusuario,
                    labor: $("#labor").val()
                }
            });
        },
        onSelectRow: function(id) {
            if (id !== newRowID)
                myGrid.restoreRow(lastSel);
            lastSel = id;
        },
        loadComplete: function() {
            if ((myGrid.jqGrid('getGridParam', 'datatype') !== "local")
                    && camposObligatorios()) {

                myGrid.jqGrid('addRow', {
                    rowID: newRowID,
                    addRowParams: {
                        keys: true,
                        successfunc: recargarGrilla,
                        extraparam: {
                            idusuario: idusuario,
                            labor: $('#labor').val(),
                            mes: $.datepicker.formatDate('/mm/yy ', $('#mes').monthpicker('getDate'))

                        }
                    }
                });
            }
        },
        pager: "#pagerPlanilla"
    });
    myGrid.jqGrid('navGrid', '#pagerPlanilla', {edit: false, add: false, search: true, del: true, refresh: true, beforeRefresh: recargarGrilla}
    , {/*Edit*/}, {/*Add*/}, {/*Del*/afterSubmit: recargarGrilla}, {/*Sch*/}, {reloadAfterSubmit: true});
    extraGrid.jqGrid({
        autowidth: true,
        ignoreCase: true,
        url: 'PlanillaServlet',
        editurl: 'PlanillaServlet',
        datatype: "local",
        rowNum: 15,
        loadonce: true,
        colNames: ['Dia', 'H/Inicio D', 'H/Desc D', 'T/Diurno', 'H/Reinicio D', 'H/Final D', 'T/Tarde', 'Registros'],
        colModel: [
            {
                index: 'dia',
                name: 'dia',
                editable: true,
                editrules: {
                    required: true,
                    number: true,
                    minValue: 1,
                    maxValue: 31
                },
                editoptions: {
                    maxlength: 2
                }

            }, {
                index: 'hInicioD',
                name: 'hInicioD',
                template: templateHorasE

            }, {index: 'hDescD',
                name: 'hDescD',
                template: templateHorasE

            }, {
                index: 'tiempoDiurno',
                name: 'tiempoDiurno',
                align: 'center'

            }, {
                index: 'hReinicioD',
                name: 'hReinicioD',
                template: templateHorasE

            }, {
                index: 'hFinalD',
                name: 'hFinalD',
                template: templateHorasE

            }, {
                index: 'tiempoTarde',
                name: 'tiempoTarde',
                align: 'center'

            }, {
                index: 'registros',
                name: 'registros',
                editable: true,
                editrules: {
                    required: true,
                    number: true
                },
                editoptions: {
                    defaultValue: "0"
                }
            }],
        ondblClickRow: function(id) {
            if (id && id !== lastSelE) {
                extraGrid.restoreRow(lastSelE);
                lastSelE = id;
            }
            extraGrid.jqGrid('editRow', id, {
                keys: true,
                successfunc: recargarGrillaExtra,
                extraparam: {
                    mes: $.datepicker.formatDate('/mm/yy ', $('#mes').monthpicker('getDate')),
                    idusuario: idusuario,
                    labor: $("#laborE").val()
                }
            });
        },
        onSelectRow: function(id) {
            if (id !== newRowID)
                extraGrid.restoreRow(lastSelE);
            lastSelE = id;
        },
        loadComplete: function() {
            if ((extraGrid.jqGrid('getGridParam', 'datatype') !== "local")
                    && camposObligatoriosE()) {

                extraGrid.restoreRow(lastSelE);
                extraGrid.jqGrid('addRow', {
                    rowID: newRowID,
                    addRowParams: {
                        keys: true,
                        successfunc: recargarGrillaExtra,
                        extraparam: {
                            idusuario: idusuario,
                            labor: $('#laborE').val(),
                            mes: $.datepicker.formatDate('/mm/yy ', $('#mes').monthpicker('getDate'))

                        }
                    }});
            }
        },
        pager: "#pagerPlanillaExtra"
    });
    extraGrid.jqGrid('navGrid', '#pagerPlanillaExtra', {
        edit: false, add: false, search: true, del: true, refresh: true,
        beforeRefresh: recargarGrillaExtra
    }
    , {/*Edit*/}, {/*Add*/}, {/*Del*/afterSubmit: recargarGrillaExtra}, {/*Sch*/}, {
        reloadAfterSubmit: true
    });
    myGrid.jqGrid('bindKeys');
    extraGrid.jqGrid('bindKeys');
});
function checkDia(value, colname) {

    var mes = $('#mes').monthpicker('getDate');
    mes = $.datepicker.formatDate("/mm/yy", mes);
    var dia = $.datepicker.parseDate("dd/mm/yy", (value + mes));
    if (dia >= vigencia.inicial && dia <= vigencia.final) {
        return [true, ""];
    } else {
        return [false, mes + " no esta en el rango de vigencia de la labor"];
    }
}


function checkHoras(value, colname, iCol) {
    var iColAnt = (iCol === 4) ? iCol - 2 : iCol - 1;
    var colModelDia = myGrid.jqGrid('getGridParam', 'colModel')[0].name;
    var colModelName = myGrid.jqGrid('getGridParam', 'colModel')[iColAnt].name;
    var colNameAnt = myGrid.jqGrid('getGridParam', 'colNames')[iColAnt];
    var rowID = myGrid.jqGrid('getGridParam', 'selrow');
    var regExp = /^(\d{1,2})(\d{2})?$/;
    var horaAnt = $('#' + rowID + '_' + colModelName, myGrid).val().match(regExp);
    var horaAct = value.match(regExp);
    var dia = $('#' + rowID + '_' + colModelDia, myGrid).val();
    var mes = $("#mes").monthpicker('getDate');
    mes.setDate(dia);
    var dateAnt = mes.setHours(horaAnt[1], horaAnt[2]);
    var dateAct = mes.setHours(horaAct[1], horaAct[2]);
    if (dateAnt > dateAct && !(iCol === 5 && parseInt(horaAct[1]) === 0 && parseInt(horaAct[2]) === 0)) {
        return [false, colname + " No debe ser menor que la hora en " + colNameAnt];
    }

    return [true, ''];
}
function checkHorasE(value, colname, iCol) {
    var iColAnt = (iCol === 4) ? iCol - 2 : iCol - 1;
    var colModelDia = extraGrid.jqGrid('getGridParam', 'colModel')[0].name;
    var colModelName = extraGrid.jqGrid('getGridParam', 'colModel')[iColAnt].name;
    var colNameAnt = extraGrid.jqGrid('getGridParam', 'colNames')[iColAnt];
    var rowID = extraGrid.jqGrid('getGridParam', 'selrow');
    var regExp = /^(\d{1,2})(\d{2})?$/;
    var horaAnt = $('#' + rowID + '_' + colModelName, extraGrid).val().match(regExp);
    var horaAct = value.match(regExp);
    var dia = $('#' + rowID + '_' + colModelDia, extraGrid).val();
    var mes = $("#mes").monthpicker('getDate');
    mes.setDate(dia);
    var dateAnt = mes.setHours(horaAnt[1], horaAnt[2]);
    var dateAct = mes.setHours(horaAct[1], horaAct[2]);
    if (dateAnt > dateAct) {
        return [false, colname + " No debe ser menor que la hora en " + colNameAnt];
    }

    return [true, ''];
}

function cargarVigencias(idlabor) {
    var dato;
    $.ajax({
        type: "POST",
        url: "PlanillaServlet",
        async: false,
        data: {
            oper: "labor",
            idusuario: idusuario,
            idlabor: idlabor
        }, success: function(data) {
            dato = data;
            dato.inicial = $.datepicker.parseDate("dd/mm/yy", data.inicial);
            dato.final = $.datepicker.parseDate("dd/mm/yy", data.final);
        }
    });
    return dato;
}

function cargarCedula() {
    $.ajax({
        type: "POST",
        url: "PlanillaServlet",
        async: false,
        data: {
            oper: "ced",
            cedula: $("#cedula").val()
        }, success: function(data) {
            $("#nombre").val(data.nombre);
            idusuario = data.idpersona;
            $("#labor").load("getLaboresUsuarios.htm?idusuario=" + idusuario, function() {
                $("#labor").val(0);
            });
            $("#laborE").load("getLaboresUsuarios.htm?idusuario=" + idusuario, function() {
                $("#laborE").val(0);
            });
            if ($("#mes").val() === "") {
                $("#mes").focus();
            } else {
                $("#labor").select();
            }

        }
    });
}

function cargarMeses() {
    var meses = $.datepicker.regional['es'].monthNamesShort;
    var now = new Date();
    var ano = now.getFullYear();
    var mes = now.getMonth() + 1;
    var disable = new Array();
    var cont = 0;
    for (var i = 1; i <= 12; i++) {
        if (i < (mes - 1) || i > mes){
            disable[cont++] = i;
        }
    }
    if(mes === 1){
        var index = disable.indexOf(12);
        disable.splice(index, 1);
    }
    
    $('#mes').monthpicker({
        startYear: ano-1,
        finalYear: ano,
        pattern: 'mmm/yyyy',
        monthNames: meses,
        disabledMonths: disable
    }).bind('monthpicker-click-month', recargarGrilla);
}

function camposObligatorios() {
    $(".error").hide(400, function() {
        $(this).remove();
    });
    if ($("#cedula").val() === "") {
        $("#cedula").focus().after("<span class='error'>Digite la cedula del auxiliar</span>");
        return false;
    }
    if ($("#mes").val() === "") {
        $("#mes").focus().after("<span class='error'>Seleccione un mes</span>");
        return false;
    }
    if ($("#labor").val() === "0") {
        $("#labor").focus().after("<span class='error'>Seleccione una labor</span>");
        return false;
    }
    return true;
}
function camposObligatoriosE() {
    $(".error").hide(400, function() {
        $(this).remove();
    });
    if ($("#cedula").val() === "") {
        $("#cedula").focus().after("<span class='error'>Digite la cedula del auxiliar</span>");
        return false;
    }
    if ($("#mes").val() === "") {
        $("#mes").focus().after("<span class='error'>Seleccione un mes</span>");
        return false;
    }
    if ($("#laborE").val() === "0") {
        $("#laborE").focus().after("<span class='error'>Seleccione una labor</span>");
        return false;
    }
    return true;
}

function recargarGrilla(response) {
    if (response && eval(response.responseText)) {
        var resp = eval(response.responseText);
        if (!resp[0]) {
            setTimeout(function() {
                if (resp[1]) {
                    alert(resp[1]);
                }
            }, 100);
        }
    }

    if (camposObligatorios()) {

        myGrid.jqGrid('setGridParam', {
            datatype: "json",
            postData: {
                mes: $.datepicker.formatDate('mm/yy', $('#mes').monthpicker('getDate')),
                idpersona: idusuario,
                labor: $("#labor").val()
            }
        });
        myGrid.trigger("reloadGrid");
    }
    return [true, ''];
}
function recargarGrillaExtra(response) {
    if (response && eval(response.responseText)) {
        var resp = eval(response.responseText);
        if (!resp[0]) {
            setTimeout(function() {
                if (resp[1]) {
                    alert(resp[1]);
                }
            }, 100);
        }
    }

    if (camposObligatoriosE()) {
        extraGrid.jqGrid('setGridParam', {
            datatype: "json",
            postData: {
                mes: $.datepicker.formatDate('mm/yy', $('#mes').monthpicker('getDate')),
                idpersona: idusuario,
                labor: $("#laborE").val()
            }
        });
        extraGrid.trigger("reloadGrid");
    }
    return [true, ''];
}

