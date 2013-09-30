/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    var emailreg = /^[a-zA-Z0-9_\.\-]+@[a-zA-Z0-9\-]+\.[a-zA-Z0-9\-\.]+$/;

    $(".guardar").click(function() {
        $(".error").remove();
        if ($("#identificacion").val() == "") {
            $("#identificacion").focus().after("<span class='error'>Ingrese número de identificación</span>");
            return false;
        }
        if ($("#email").val() == "" || !emailreg.test($("#email").val())) {
            $("#email").focus().after("<span class='error'>Digite un Email valido</span>");
            return false;
        }

        if (true) {
            $.blockUI({
                message: '<h2>Enviando Recuperación de contraseña.....</h2>',
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
    $("#identificacion,#email").keyup(function() {
        if ($(this).val() != "") {
            $(".error").fadeOut();
            return false;
        }
    });

    //Limpia el TAG error en el SELECT cuando se corrige
    $("#identificacion").change(function() {
        if ($(this).val() != "") {
            $(".error").fadeOut();
            return false;
        }
    });
});

