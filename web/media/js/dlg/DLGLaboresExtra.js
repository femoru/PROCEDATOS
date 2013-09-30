/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
jQuery(document).ready(function($) {


    $('#dlgExtras').dialog({
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
            {
                text: "Guardar",
                click: function() {
                    var extra = 0;

                    var idrowlabor = $('#gridLbr').jqGrid('getGridParam', 'selrow');
                    var labor = new Object();

                    labor.idlabor = idrowlabor;

                    for (var i = 0; i < $("#tablaExtra tr").length; i++) {
                        extra = $($($("#tablaExtra tr")[i]).find("td")[0]).attr("id").split("_")[1]
                        labor.valor = $("#valor_" + extra).val();
                        labor.costo = $("#costo_" + extra).val();
                        labor.extra = extra;

                        ingresarExtra(labor);
                    }
                    $(this).dialog("close");
                }
            }],
        close: function() {
            $("#tablaExtra").find("tbody").remove();
        },
        open: function(event, ui) {
            var respuesta = $.ajax({
                type: "POST",
                async: false,
                url: "LaboresServlet",
                data: {
                    oper: "consulta",
                    idlabor: $("#gridLbr").jqGrid('getGridParam', 'selrow')
                }
            }).responseText;

            var extras = respuesta.split(",");
            for (var i = 0; i < extras.length; i++) {
                $("#horaextras option[value=" + extras[i] + "]").remove();
            }

        }
    });

    $("#horaextras").change(function(e) {
        var extra = $(e.target).val();
        if (extra == "0") {
            $("#otroExtra").attr("disabled", "disabled")
            $("#valorExtra").attr("disabled", "disabled")
            $("#costoExtra").attr("disabled", "disabled")
        } else {
            $("#otroExtra").removeAttr("disabled")
            $("#valorExtra").removeAttr("disabled")
            $("#costoExtra").removeAttr("disabled")
        }
    })
    $("#otroExtra").click(function() {

        var extra = $("#horaextras").val();
        var valor = $("#valorExtra");
        var costo = $("#costoExtra");
        if (extra == "0") {
            return false;
        }

        if (valor.val() == "") {
            valor.focus().after("<span class='error'>Digite precio</span>");
            return false;
        }

        if (isNaN(valor.val())) {
            valor.focus().after("<span class='error'>Digite solo numeros</span>");
            return false;
        }
        if (costo.val() == "") {
            costo.focus().after("<span class='error'>Digite precio</span>");
            return false;
        }

        if (isNaN(costo.val())) {
            costo.focus().after("<span class='error'>Digite solo numeros</span>");
            return false;
        }

        var tds = '<tr id="tr_extra_' + extra + '">';
        tds += '<td id="extra_' + extra + '">' + $("#horaextras option[value='" + extra + "']").text();
        tds += '</td>';
        tds += '<td>Valor</td>';
        tds += '<td><input id="valor_' + extra + '"  class="numero" value="' + valor.val() + '" disabled></td>';
        tds += '<td>Costo</td>';
        tds += '<td><input id="costo_' + extra + '"  class="numero" value="' + costo.val() + '" disabled></td>';
        tds += '<td><input type="button" value="-" onclick="$(\'#tr_extra_' + extra + '\').remove()"></td>';
        tds += '</tr>';

        $("#horaextras option[value=" + extra + "]").remove();
        $("#otroExtra").attr("disabled", "disabled")
        $("#tablaExtra").append(tds);

        $("#valorExtra").val("");
        $("#costoExtra").val("");
        $("#valorExtra").attr("disabled", "disabled")
        $("#costoExtra").attr("disabled", "disabled")
        return true;
    });
    $("select").change(function() {
        $(".error").fadeOut();
    })
    $("input").keyup(function() {
        $(".error").fadeOut();
    })

    function ingresarExtra(obj) {
        var respuesta = $.ajax({
            type: "POST",
            async: false,
            url: "LaboresServlet",
            data: {
                oper: "more",
                idlabor: obj.idlabor,
                extra: obj.extra,
                valor: obj.valor,
                costo: obj.costo
            }
        }).responseText
        if (respuesta == "true") {
            jQuery("#gridLbr").jqGrid('setGridParam', {
                datatype: "json"
            })
            jQuery("#gridLbr").trigger("reloadGrid");

        }
    }
})