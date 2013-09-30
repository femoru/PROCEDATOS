/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery(document).ready(function($) {


    var refrescarGrilla = function(response, postdata) {
        jQuery("#gridNov").jqGrid('setGridParam', {
            datatype: "json"
        });
        jQuery("#gridNov").trigger("reloadGrid");

        return [true, ""];
    };

    jQuery('#gridNov').jqGrid(
            {
                height: 240,
                hoverrows: false,
                hidegrid: false,
                gridview: true,
                ignoreCase: true,
                caption: "Novedades",
                url: "RefNovedadServlet",
                editurl: "RefNovedadServlet",
                loadonce: true,
                rowNum: 10,
                rownumbers: true,
                datatype: "json",
                colNames: ["id", "Descripción", "Tipo", "Afecta Basico", "Afecta Auxilio"
                            , "Pagar", "Afecta Neto", "Aseguradora"],
                colModel: [
                    {
                        "name": "id",
                        "index": "id",
                        editable: true,
                        edittype: "text",
                        hidden: true
                    }, {
                        "name": "descripcion",
                        "index": "descripcion",
                        editable: true,
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            maxlength: 100,
                            style: "text-transform: uppercase"
                        },
                        edittype: "text"
                    }, {
                        "name": "tipo",
                        "index": "tipo",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Dias",
                                2: "Valor"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Dias",
                                2: "Valor"
                            }
                        }
                    }, {
                        "name": "basico",
                        "index": "basico",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }
                        }
                    }, {
                        "name": "auxilio",
                        "index": "auxilio",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }
                        }
                    }, {
                        "name": "pagar",
                        "index": "pagar",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        }
                    }, {
                        "name": "neto",
                        "index": "neto",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No Afecta",
                                1: "Suma",
                                2: "Resta"
                            }
                        }
                    }, {
                        "name": "aseguradora",
                        "index": "aseguradora",
                        editable: true,
                        edittype: "select",
                        stype: "select",
                        formatter: "select",
                        align: "center",
                        editrules: {
                            required: true
                        },
                        editoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }, style: "width: 90%"
                        },
                        searchoptions: {
                            value: {
                                0: "No",
                                1: "Si"
                            }
                        }
                    }


                ],
                loadComplete: function() {
                    if (this.p.datatype === 'json') {
                        setTimeout(function() {
                            $('#gridNov').trigger("reloadGrid", [{
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
                pager: "#pagerNov"
            });

    jQuery("#gridNov").jqGrid('bindKeys');
    jQuery('#gridNov').jqGrid('navGrid', '#pagerNov',
            {
                edit: true,
                edittitle: "Modificar novedad seleccionado",
                add: true,
                addtitle: "Agregar nueva novedad",
                search: true,
                del: false,
                refresh: true,
                beforeRefresh: refrescarGrilla
            },
    {
        beforeInitData: function() {
            jQuery('#gridNov').setColProp('descripcion', {
                editable: true,
                edittype: "text",
                editoptions: {
                    readonly: 'readonly'
                }
            });
        },
        afterSubmit: refrescarGrilla,
        bottominfo: "Todos los campos(*) son obligatorios",
        editCaption: "Modificar Novedad ",
        closeAfterEdit: true
    },
    {
        afterSubmit: refrescarGrilla,
        clearAfterAdd: true,
        addCaption: "Agregar Novedad",
        bottominfo: "Todos los campos(*) son obligatorios"
    },
    {
        closeAfterDel: true
    },
    {
        multipleSearch: false

    },
    {
        reloadAfterSubmit: true
    }
    );


});
