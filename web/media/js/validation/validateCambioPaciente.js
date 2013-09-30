$(document).ready(function() {

    var emailreg = /^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$/;

    $(".actualizar").click(function() {
        $(".error").remove();
        if ($("#direccion").val() == "") {
            $("#direccion").focus().after("<span class='error'>Digite una direccón</span>");
            return false;
        }
        if ($("#telefono").val() == "") {
            $("#telefono").focus().after("<span class='error'>Digite un telefono</span>");
            return false;
        }
        if ($("#celular").val() == "") {
            $("#celular").focus().after("<span class='error'>Digite un telefono celular</span>");
            return false;
        }
        if ($("#email").val() == "" || !emailreg.test($("#email").val())) {
            $("#email").focus().after("<span class='error'>Digite un Email valido</span>");
            return false;
        }

        if (true) {
            $.blockUI({
                message: '<h2>Actualizando Datos Usuario.....</h2>',
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
    $("#direccion,#telefono,#celular,#email").keyup(function() {
        if ($(this).val() != "") {
            $(".error").fadeOut();
            return false;
        }
    });
});

