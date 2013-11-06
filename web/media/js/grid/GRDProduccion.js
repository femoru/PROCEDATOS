
$(document).ready(function($) {
    var now = new Date();
    //var daysAgo =new Date(now.getTime()-(7*24*3600*1000));
    restriccionCoordinador();

    $("#dateIni").val($.datepicker.formatDate("dd/mm/yy", now));
    $("#dateFin").val($.datepicker.formatDate("dd/mm/yy", now));
    $.datepicker.setDefaults({
        showOn: "both",
        maxDate: 0,
        onSelect: function() {
            refrescarGrilla();
        }
    });
    $("#dateIni").datepicker();
    $("#dateFin").datepicker();
    $("#comboEstado").change(function(e) {
        refrescarGrilla();
    });
    $("#consulta").click(function() {
        jQuery("#gridProd").jqGrid('setGridParam', {
            url: "ProduccionServlet?inicial=" + $("#dateIni").val() + "&fFinal=" + $("#dateFin").val()
                    + "&estado=" + $("#comboEstado").val() + "&identificacion=" + $("#documento").val(),
            datatype: "json",
            pager: "#pagerProd"
        });
        jQuery("#gridProd").trigger("reloadGrid");
        return [true, ""];
    });

    jQuery('#gridProd').jqGrid(
            {
                height: 240,
                autowidth: true,
                caption: "Registros de Producción",
                url: "ProduccionServlet?inicial=" + $("#dateIni").val() + "&fFinal=" + $("#dateFin").val()
                        + "&estado=" + $("#comboEstado").val() + "&identificacion=" + $("#documento").val(),
                loadonce: true,
                rowNum: 1000,
                datatype: "json",
                grouping: true,
                groupingView: {
                    groupField: ['grupoarea'],
                    groupColumnShow: [false],
                    groupText: ['<b>{0} --> Tiempo: {tiempolabor} min , Registros: {registroslabor} </b>'],
                    groupCollapse: true,
                    groupSummary:[true] 
                },
                colNames: ["Id", "Grupo - Area", "Labor", "Tipo Labor", 'Extra', "Fecha Inicio", "Fecha Fin",
                    "Observación", "Dato Labor", "Total Tiempo", "Total Registros", "Estado"],
                colModel: [
                    {"name": "id", "index": "id", editable: false, hidden: true},
                    {"name": "grupoarea", "index": "grupoarea", editable: false},
                    {
                        "name": "labor",
                        "index": "labor",
                        hidden: false,
                        search: true,
                        width: 260
                    },
                    {
                        "name": "destipolabor",
                        "index": "destipolabor",
                        hidden: false,
                        width: 80,
                        align: "center",
                        search: true,
                        stype: "select",
                        searchoptions: {
                            value: {
                                "PTM": "PTM",
                                "HORAS": "HORAS",
                                "REGISTROS": "REGISTROS",
                                "IMAGENES": "IMAGENES"
                            }
                        }
                    },
                    {
                        "name": "extra",
                        "index": "extra",
                        hidden: false,
                        search: true,
                        width: 100
                    },
                    {
                        "name": "fechainicio",
                        "index": "fechainicio",
                        search: false,
                        hidden: false,
                        width: 120,
                        align: "center"
                    },
                    {
                        "name": "fechafin",
                        "index": "fechafin",
                        search: false,
                        width: 120,
                        align: "center"
                    },
                    {
                        "name": "observacion",
                        "index": "observacion",
                        search: false,
//                        hidden:true,
                        editable: false,
                        width: 250
                    },
                    {
                        "name": "datolabor",
                        "index": "datolabor",
                        search: false,
                        editable: false,
                        edittype: "text",
                        width: 60
                    },
                    {
                        "name": "tiempolabor",
                        "index": "tiempolabor",
                        search: false,
                        editable: false,
                        edittype: "text",
                        align: "center",
                        summaryType:'sum',
                        summaryTpl:'<b>{0} min</b>',
                        width: 70
                    },
                    {
                        "name": "registroslabor",
                        "index": "registroslabor",
                        search: false,
                        editable: false,
                        edittype: "text",
                        align: "center",
                        summaryType:'sum',
                        width: 70
                    },
                    {
                        "name": "estado",
                        "index": "estado",
                        editable: false,
                        search: false,
                        hidden: true,
                        editoptions: {
                            value: {
                                0: "Sin Validar",
                                1: "Validada"
                            }
                        },
                        width: 80
                    }
                ],
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridProd').trigger("reloadGrid", [{
                                    page: 1
                                }]);
                        }, 1);

                    }
                },
                postData: {
                    oper: "oper"
                },
                loadError: function(xhr, status, err)
                {
                    try
                    {
                        jQuery.jgrid.info_dialog(jQuery.jgrid.errors.errcap,
                                '<div class="ui-state-error">' + xhr.responseText + '</div>',
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
                pager: "#pagerProd"
            });
    jQuery("#gridProd").jqGrid('bindKeys');
    jQuery('#gridProd').jqGrid('navGrid', '#pagerProd',
            {
                edit: false,
                add: false,
                view: true,
                search: true,
                del: false,
                refresh: true,
                searchtitle: "Filtrar Registros",
                beforeRefresh: refrescarGrilla
            },
    {
        multipleSearch: false
    },
    {
        reloadAfterSubmit: true
    }
    );
});

function restriccionCoordinador() {
    var perfilUsuario = consultaPerfil();

    switch (perfilUsuario) {
        case '4': //Auxiliar
            $("#tr_documento").hide();
            break;
    }
}

function refrescarGrilla( ) {
    jQuery("#gridProd").jqGrid('setGridParam', {
        url: "ProduccionServlet?inicial=" + $("#dateIni").val() + "&fFinal=" + $("#dateFin").val()
                + "&estado=" + $("#comboEstado").val() + "&identificacion=" + $("#documento").val(),
        datatype: "json",
        pager: "#pagerProd"
    });
    jQuery("#gridProd").trigger("reloadGrid");
    return [true, ""];
}