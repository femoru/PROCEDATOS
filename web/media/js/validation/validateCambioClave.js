
$(document).ready(function() {
    $(".cambiar").click(function() {
        $(".error").remove();
        if ($("#actual").val() == "") {
            $("#actual").focus().after("<span class='error'>Digite contraseña actual</span>");
            return false;
        }
        if ($("#actual").val() != $("#passwordActual").val()) {
            $("#actual").focus().after("<span class='error'>La contraseña ingesada no coincide con la registrada</span>");
            return false;
        }
        if ($("#clave").val() == "") {
            $("#clave").focus().after("<span class='error'>Digite nueva contraseña</span>");
            return false;
        }
        if ($("#passwordconf").val() == "") {
            $("#passwordconf").focus().after("<span class='error'>Digite confirmación nueva contraseña</span>");
            return false;
        }
        if ($("#clave").val() != $("#passwordconf").val()) {
            $("#clave").focus().after("<span class='error'>La Contraseña nueva no Coincide</span>");
            return false;
        }
        if ($("#imagen").val() == "") {
            $("#imagen").focus().after("<span class='error'>Digite codigo verificación</span>");
            return false;
        }
        if (true) {
            $.blockUI({
                message: '<h2>Actualizando Contraseña.....</h2>',
                css: {
                    border: 'none',
                    padding: '15px',
                    backgroundColor: '#000',
                    '-webkit-border-radius': '10px',
                    '-moz-border-radius': '10px',
                    opacity: .5,
                    color: '#fff'
                }
            });
            setTimeout($.unblockUI, 5000);
        }
    });

    //Limpia el TAG error en el TEXT cuando se corrige
    $("#actual,#clave,#passwordconf,#usuario.imagen").keyup(function() {
        if ($(this).val() != "") {
            $(".error").fadeOut();
            return false;
        }
    });
});
