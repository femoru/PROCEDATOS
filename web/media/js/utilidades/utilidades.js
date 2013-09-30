/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

function enter2tab(e) {
    if (e.keyCode === 13) {
        cb = parseInt($(this).attr('tabindex'));

        if ($(':input[tabindex=\'' + (cb + 1) + '\']') !== null) {
            $(':input[tabindex=\'' + (cb + 1) + '\']').focus();
            $(':input[tabindex=\'' + (cb + 1) + '\']').select();
            e.preventDefault();
            return false;
        }
    }
}

function setReadonly(selectElementId) {
    var selectElement = document.getElementById(selectElementId);
    if (selectElement) {
        var parent = selectElement.parentElement;
        var textValue = selectElement.options[selectElement.options.selectedIndex].innerText;
        if (!parent) {
            parent = selectElement.parentNode;
            textValue = selectElement.options[selectElement.options.selectedIndex].text;
        }
        var input = document.createElement("input");
        input.setAttribute("id", selectElement.id);
        input.setAttribute("type", "text");
        input.setAttribute("value", textValue);
        input.readOnly = true;
        parent.appendChild(input);
    }
    selectElement.style.display = "none";
}

function consultaPerfil() {
    return $.ajax({
        type: "POST",
        url: "SolicitudServlet",
        async: false,
        data: {
            oper: "perfil"
        }
    }).responseText;
}



