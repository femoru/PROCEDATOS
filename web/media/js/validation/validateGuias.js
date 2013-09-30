/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function($) {

    $.datepicker.setDefaults({
        dateFormat: "dd/mm/yy",
        defaultDate: 0
    });

    $("#grillaGuia").hide();
    $(".fecha").datepicker();
    $("#cliente").load('getContratos.htm');
    $("#labor").change(function() {
        $(".error").remove();
    });
    $("#cliente").change(function(e) {
        $(".error").remove();
        $("#labor").load('getLaboresContrato.htm?idcontrato=' + $(e.target).val());
    });

    $("#inicio").datepicker('setDate', '-30d');
    $("#fin").datepicker('setDate', '0d');

    $("#archivo").change(function(e) {
        $(".error").remove();
        var archivo = $(e.target).val();
        archivo = archivo.split(".");
        if (archivo[1].toLowerCase() !== "txt") {
            alert("Seleccione un archivo txt");
            $(e.target).trigger('click');
        }
    });
    $("#cargar").click(function() {
        if (validarCabecera() !== false) {
            $("#cabecera").submit();
            //$("#upload_target").fadeOut(5000)

//            $("#inicio").attr('disabled', 'disabled');
//            $("#fin").attr('disabled', 'disabled');
//            $("#cliente").attr('disabled', 'disabled');
//            $("#labor").attr('disabled', 'disabled');
            jQuery("#gridGuias").jqGrid('setGridParam', {
                url: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                        + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val(),
                subGridUrl: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                        + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val(),
                datatype: "json"
            });

            setTimeout(function() {
                jQuery("#gridGuias").trigger("reloadGrid");
                if ($("#upload_target").contents().find("span")[0].style.color !== "red") {
                    $("#grillaGuia").show();
                }
            }, 1000);

            return true;
        }
        return false;
    });

    $("#concilia").click(function() {
        var result = $.ajax({
            url: "GuiasServlet",
            type: "POST",
            async: false,
            data: {
                oper: "conciliar",
                labor: $("#labor").val(),
                inicio: $("#inicio").val(),
                fin: $("#fin").val()
            }
        }).responseText;
        if (result === "true") {
            alert("Se actualizaron correctamente todos los registros");
            jQuery("#gridGuias").jqGrid('setGridParam', {
                url: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                        + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val(),
                subGridUrl: "GuiasServlet?cliente=" + $("#cliente").val() + "&laborContrato=" + $("#labor").val()
                        + "&inicio=" + $("#inicio").val() + "&fin=" + $("#fin").val(),
                datatype: "json"
            });
            jQuery("#gridGuias").trigger("reloadGrid");
        } else {
            alert("No se pudieron actualizar algunos registros");
        }

    });



    function validarCabecera() {
        $(".error").remove();
        if ($("#cliente").val() === "0") {
            $("#cliente").focus().after("<span class='error'>Seleccione un cliente</span>");
            return false;
        }
        if ($("#labor").val() === "0") {
            $("#labor").focus().after("<span class='error'>Seleccione una labor</span>");
            return false;
        }
        if ($("#inicio").val() === "") {
            $("#inicio").focus().after("<span class='error'>Seleccione la fecha inicial del reporte</span>");
            return false;
        }
        if ($("#fin").val() === "") {
            $("#fin").focus().after("<span class='error'>Seleccione la fecha final del reporte</span>");
            return false;
        }
        if ($("#archivo").val() === "") {
            $("#archivo").focus().after("<span class='error'>Seleccione el archivo de reporte</span>");
            return false;
        }

        return true;
    }

});

