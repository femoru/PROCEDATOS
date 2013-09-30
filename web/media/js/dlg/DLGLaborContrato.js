/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {


    $('#dlglabor').dialog({
        autoOpen: false,
        modal: true,
        width: 'auto',
        dialogClass: "no-close",
        buttons: [
            {
                text: "Cancelar",
                click: function() {
                    $(this).dialog("close");
                }
            },
            {text: "Guardar", click: function() {
                    if (validar()) {


                        var myGrid = $('#gridCtr'),
                                selRowId = myGrid.jqGrid('getGridParam', 'selrow');

                        var labor = new Object();

                        labor.id = $('#dlglabor').data("id");
                        labor.contrato = selRowId;
                        labor.grupo = $("#grupo").val();
                        labor.labor = $("#labor").val();
                        labor.tipo = $("#tipo").val();
                        labor.extra = $("#extra").val() === null ? "0" : $("#extra").val();
                        labor.valor = $("#valor").val();
                        labor.costo = $("#costo").val();
                        labor.conciliacion = $("#conciliacion").is(":checked");
                        labor.estado = $("#estado").val();
                        labor.datolabor = $("#datolabor").val();
                        labor.oper = $('#dlglabor').data("oper");

                        ingresar(labor);


                    }
                }
            }
        ]

    });

    $("#area").change(function(e) {

        var area = $(e.target).val();
        $("#grupo").load("getGruposAreas.htm?idarea=" + area);
    });

    $("#extras").change(function(e) {
        var extra = $(e.target).val();
        if (extra === "0") {
            $("#otro").attr("disabled", "disabled");
        } else {
            $("#otro").removeAttr("disabled");
        }
    });

    function validar() {
        $(".error").remove();

        if ($("#area").val() === "0") {
            $("#area").focus().after("<span class='error'>Seleccione un area</span>");
            return false;
        }
        if ($("#grupo").val() === "0") {
            $("#grupo").focus().after("<span class='error'>Seleccione un grupo</span>");
            return false;
        }
        if ($("#tipo").val() === "0") {
            $("#tipo").focus().after("<span class='error'>Seleccione un tipo</span>");
            return false;
        }
        if ($("#labor").val() === "0") {
            $("#labor").focus().after("<span class='error'>Seleccione una labor</span>");
            return false;
        }

        for (var i = 0; i < $(".numero").length; i++) {
            if ($(".numero")[i].value === "") {
                $($(".numero")[i]).focus().after("<span class='error'>Digite precio</span>");
                return false;
            }

            if (isNaN($(".numero")[i].value)) {
                $($(".numero")[i]).focus().after("<span class='error'>Digite solo numeros</span>");
                return false;
            }
        }
        if ($("#extra").is(":visible")) {
            if ($("#extra").val() === "0") {
                $("#extra").focus().after("<span class='error'>Seleccione extra</span>");
                return false;
            }
        }
        return true;
    }

    function ingresar(obj) {
        var respuesta = $.ajax({
            type: "POST",
            async: false,
            url: "LaboresServlet",
            data: {
                oper: obj.oper,
                id: obj.id,
                contrato: obj.contrato,
                grupo: obj.grupo,
                labor: obj.labor,
                tipo: obj.tipo,
                extra: obj.extra,
                valor: obj.valor,
                costo: obj.costo,
                conciliacion: obj.conciliacion,
                estado: obj.estado,
                datolabor: obj.datolabor

            }
        }).responseText;
        if (respuesta === "true") {
            jQuery("#gridLbr").jqGrid('setGridParam', {
                datatype: "json"
            });
            jQuery("#gridLbr").trigger("reloadGrid");
        }
        if (obj.oper === "edit") {
            $('#dlglabor').dialog("close");
        }
    }
});

