/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function() {
    jQuery("#menu").menu({
        position: {my: 'left top', at: 'center-40% bottom'}
    });
    $("#menu li").css("width", Math.round(($("#menu").width() - 30) / ($("#menu > li").length)));
});

