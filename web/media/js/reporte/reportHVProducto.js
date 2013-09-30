/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function Reporte_HV() {

    var Serial = $("#serialB").val();
    var CodBarras = $("#codigoBarrasB").val();

    //Carga un contenido de un html en un div oh parte que necesites del formulario
    refDiv = document.getElementById('show')
    contenido = '<object type="application/pdf" data="ProductServlet?Serial=' + Serial + '&CodBarras=' + CodBarras + '" width="700" height="850"></object>'
    refDiv.innerHTML = contenido
}
