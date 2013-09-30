/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


jQuery((function($) {


    $("#checkTree").dynatree({
        checkbox: true,
        selectMode: 3,
        minExpandLevel: 2,
        initAjax: {
            url: "PermisosServlet"
        }
    });


}));

