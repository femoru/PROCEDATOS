/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

$(document).ready(function() {

    $(".boton").click(function() {
        $(".error").remove();
        if ($(".usuario").val() == "") {
            $(".usuario").focus().after("<span class='error'>Ingrese su Usuario</span>");
            return false;
        } else if ($(".password").val() == "") {
            $(".password").focus().after("<span class='error'>Ingrese su Clave</span>");
            return false;
        }
    });
});

