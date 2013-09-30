/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function($) {
    var now = new Date();
    var daysAgo = new Date(now.getTime() - (7 * 24 * 3600 * 1000));

    $("#dateIni").val($.datepicker.formatDate("dd/mm/yy", daysAgo));
    $("#dateFin").val($.datepicker.formatDate("dd/mm/yy", now));

    $("#dateIni").datepicker({showOn: "both",
        maxDate: 0,
        onSelect: function() {
            refrescarGrilla();
        }});
    $("#dateFin").datepicker({showOn: "both",
        maxDate: 0,
        onSelect: function() {
            refrescarGrilla();
        }
    });
    $("#comboFecha").change(function() {
        refrescarGrilla();
    });
    $("#comboEstado").change(function(e) {
        refrescarGrilla();
    });
    var rdb_perfil = $("input[name='rdio']").val();
    $("input[name='rdio']").change(function(e) {
        rdb_perfil = $(e.target).val();
        refrescarGrilla();
    });


    function refrescarGrilla( ) {
        jQuery("#gridSlt").jqGrid('setGridParam', {
            url: "SolicitudServlet?inicial=" + $("#dateIni").val() + "&fFinal=" + $("#dateFin").val()
                    + "&opcion=" + $("#comboFecha").val() + "&estado=" + $("#comboEstado").val() + "&filtro=" + rdb_perfil,
            datatype: "json",
            pager: "#pagerSlt"
        });
        jQuery("#gridSlt").trigger("reloadGrid");
        $("#edit_gridSlt").addClass('ui-state-disabled');
        $("#del_gridSlt").addClass('ui-state-disabled');

        return [true, ""];
    }
    var perfilUsuario;
    function restriccionCoordinador() {
        perfilUsuario = consultaPerfil();

        switch (perfilUsuario) {
            case '1': //Administrador            
                jQuery('#gridSlt').setColProp('respuesta', {
                    editable: true
                });
                $('#del_gridSlt').hide();
                $('#add_gridSlt').hide();
                $('#edit_gridSlt').hide();
                $('#view_gridSlt').show();
                break;

            case '3': //Coordinador SIO           
                jQuery('#gridSlt').setColProp('respuesta', {
                    editable: true
                });
                $('#del_gridSlt').hide();
                $('#add_gridSlt').hide();
                $('#view_gridSlt').show();
                break;

            case '4': //Auxiliar
                jQuery("#gridSlt").jqGrid('hideCol', "usuario");
                $('#edit_gridSlt').hide();
                //$('#del_gridSlt').hide();
                $('#td_radio').hide();
                break;

            case '5': //Coordinador Grupo
                jQuery('#gridSlt').setColProp('respuesta', {
                    editable: true
                });
                break;
        }
    }

    jQuery('#gridSlt').jqGrid(
            {
                height: 240,
                hoverrows: false,
                viewrecords: true,
                gridview: true,
                hidegrid: false,
                caption: "Solicitudes",
                url: "SolicitudServlet?inicial=" + $("#dateIni").val() + "&fFinal=" + $("#dateFin").val()
                        + "&opcion=" + $("#comboFecha").val() + "&estado=" + $("#comboEstado").val() + "&filtro=" + rdb_perfil,
                editurl: "SolicitudServlet",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["Id", "Tipo", "Coordinador solicitud  ", "Usuario", "Fecha Solicitud", "Descripcion",
                    "Estado", "Fecha Respuesta", "Respuesta"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    },
                    {
                        "name": "tipo",
                        "index": "tipo",
                        editable: true,
                        edittype: "select",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            readonly: 'readonly',
                            dataUrl: "getSolicitudes.htm"
                        }
                    },
                    {
                        "name": "grupo",
                        "index": "grupo",
                        editable: true,
                        search: false,
                        edittype: "select",
                        editoptions: {
                            readonly: 'readonly',
                            dataUrl: "getGruposAuxiliar.htm"
                        },
                        editrules: {
                            edithidden: true
                        },
                        hidden: true
                    },
                    {
                        "name": "usuario",
                        "index": "usuario",
                        editable: false,
                        edittype: "text"
                    },
                    {
                        "name": "fechasolicitud",
                        "index": "fechasolicitud",
                        search: false,
                        editable: false,
                        edittype: "text",
                        width: 130
                    },
                    {
                        "name": "descripcion",
                        "index": "descripcion",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            style: "text-transform: uppercase",
                            readonly: 'readonly',
                            maxlength: 150,
                            rows: "5",
                            cols: "30"
                        },
                        edittype: "textarea",
                        width: 200
                    },
                    {
                        "name": "estado",
                        "index": "estado",
                        editable: false,
                        search: false,
                        edittype: "select",
                        formatter: "select",
                        editoptions: {
                            value: {
                                0: "REGISTRADA",
                                1: "CONTESTADA",
                                2: "CANCELADA"
                            }
                        },
                        width: 80
                    },
                    {
                        "name": "fecharespuesta",
                        "index": "fecharespuesta",
                        search: false,
                        editable: false,
                        edittype: "text",
                        width: 130
                    },
                    {
                        "name": "respuesta",
                        "index": "respuesta",
                        editable: false,
                        editoptions: {
                            style: "text-transform: uppercase",
                            readonly: 'readonly',
                            maxlength: 150,
                            rows: "5",
                            cols: "30"
                        },
                        edittype: "textarea",
                        width: 200
                    }


                ],
                onSelectRow: function(id) {
                    var row = $('#gridSlt').jqGrid('getRowData', id);
                    if (row.estado === "0") {
                        $("#del_gridSlt").removeClass('ui-state-disabled');
                        $("#edit_gridSlt").removeClass('ui-state-disabled');
                    } else {
                        $("#del_gridSlt").addClass('ui-state-disabled');
                        $("#edit_gridSlt").addClass('ui-state-disabled');
                    }
                    if (perfilUsuario === "5" && rdb_perfil === "5") {
                        $("#edit_gridSlt").addClass('ui-state-disabled');
                    }
                },
                loadComplete: function() {
                    if (this.p.datatype === 'json') {

                        restriccionCoordinador();

                        setTimeout(function() {
                            $('#gridSlt').trigger("reloadGrid", [{
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
                pager: "#pagerSlt"
            });

    jQuery('#gridSlt').jqGrid('navGrid', '#pagerSlt', {
        edit: true,
        add: true,
        view: true,
        search: true,
        del: true,
        refresh: true,
        addtitle: "Nueva Solicitud",
        edittitle: "Responder Solicitud",
        viewtitle: "Consultar Solicitud",
        deltitle: "Cancelar Solicitud",
        searchtitle: "Filtrar Solicitudes",
        beforeRefresh: refrescarGrilla
    },
    {
        beforeInitData: function() {
            jQuery('#gridSlt').setColProp('tipo', {
                edittype: "text",
                editoptions: {
                    size: 32,
                    readonly: 'readonly'
                }
            });
            jQuery('#gridSlt').setColProp('grupo', {
                edittype: "text",
                editoptions: {
                    size: 52,
                    readonly: 'readonly'

                }
            });
        },
        beforeShowForm: function(frm) {

            $("#descripcion").attr('readonly', 'readonly');

            var myGrid = $('#gridSlt'),
                    selRowId = myGrid.jqGrid('getGridParam', 'selrow'),
                    rowData = myGrid.jqGrid('getRowData', selRowId);
            var estado = rowData.estado;
            $("#tr_respuesta").show();
            if (estado === 0) {
                $("#respuesta").removeAttr('readonly');
            } else {
                $("#respuesta").attr('readonly', 'readonly');
            }

        },
        beforeSubmit: function(postdata) {
            if (postdata.respuesta === "") {
                return [false, "Campo respuesta requerido"];
            }
            return [true, ""];
        },
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        recreateForm: true,
        width: 'auto',
        closeAfterEdit: true

    },
    {//Add

        beforeInitData: function() {
            jQuery('#gridSlt').setColProp('tipo', {
                edittype: "select",
                editoptions: {
                    readonly: 'readonly',
                    dataUrl: "getSolicitudes.htm"
                }
            });
            jQuery('#gridSlt').setColProp('grupo', {
                edittype: "select",
                editoptions: {
                    readonly: 'readonly',
                    dataUrl: "getGruposAuxiliar.htm"
                }
            });
        },
        beforeShowForm: function(frm) {
            $("#tr_respuesta").hide();
            if (perfilUsuario !== "4") {
                $("#tr_grupo").hide();
            }
            $("#descripcion").removeAttr('readonly');


        },
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        recreateForm: true,
        width: 'auto',
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        afterSubmit: refrescarGrilla,
        caption: "Cancelar solicitud",
        msg: "¿Desea cancelar la solicitud seleccionada?",
        bSubmit: "Anular",
        closeAfterDel: true
    },
    {/* multipleSearch: false*/},
            {
                reloadAfterSubmit: true
            }
    );

    $("#edit_gridSlt").addClass('ui-state-disabled');
    $("#del_gridSlt").addClass('ui-state-disabled');
    jQuery("#gridSlt").jqGrid('bindKeys');
});

